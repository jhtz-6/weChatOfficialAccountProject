package org.myf.wechatofficialaccountproject.application.service.impl;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageResponse;
import org.myf.wechatofficialaccountproject.application.service.WeChatApplicationService;
import org.myf.wechatofficialaccountproject.domain.service.RecommendDomainService;
import org.myf.wechatofficialaccountproject.domain.service.SubscribeDomainService;
import org.myf.wechatofficialaccountproject.domain.service.WeChatDomainService;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.WeChatMessageDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.EventEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 12:44
 * @Description: WeChatApplicationService实现类
 */
@Service
public class WeChatApplicationServiceImpl implements WeChatApplicationService {

    @Autowired
    WeChatDomainService weChatDomainService;
    @Autowired
    SubscribeDomainService subscribeDomainService;
    @Autowired
    RecommendDomainService recommendDomainService;
    static ExecutorService weChatSaveReceiveThreadPoolExecutor, weChatSaveSendThreadPoolExecutor;
    static {
        AtomicInteger saveReceiveThreadNumber = new AtomicInteger(1);
        weChatSaveReceiveThreadPoolExecutor =
            new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), task -> {
                Thread thread = new Thread(task);
                thread.setName("weChatMessageSaveReceiveThread-" + saveReceiveThreadNumber.incrementAndGet() + "-"
                    + thread.getName());
                return thread;
            });

        AtomicInteger saveSendThreadNumber = new AtomicInteger(1);
        weChatSaveSendThreadPoolExecutor =
            new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), task -> {
                Thread thread = new Thread(task);
                thread.setName("weChatMessageSaveSendThreadPoolExecutor-" + saveSendThreadNumber.incrementAndGet() + "-"
                    + thread.getName());
                return thread;
            });
    }

    @Override
    public WeChatMessageResponse handleMsgbyMap(Map<String, String> map) {
        WeChatMessageDTO weChatMessageDTO = convertMapToWeChatMessageDTO(map);
        weChatSaveReceiveThreadPoolExecutor.submit(new WeChatSaveReceiveMessageTask(weChatMessageDTO));
        replaceCharacter(weChatMessageDTO);
        String handleMsgResult = handleMsg(weChatMessageDTO);
        if (StringUtils.isEmpty(handleMsgResult)) {
            handleMsgResult = "服务处理异常,请稍后再试或联系管理员处理";
        }
        String currentPersonNum =
            weChatDomainService.getCurrentPersonNum("current_person_" + weChatMessageDTO.getFromUserName(),
                weChatMessageDTO.getFromUserName(), WeChatUtil.CURRENT_PERSON_TIMEOUT);;
        WeChatMessageResponse weChatMessageResponse =
            buildHandleMsgResult("当前在线人数:" + currentPersonNum + "\n" + handleMsgResult, weChatMessageDTO);
        weChatMessageResponse.setType("send");
        weChatSaveSendThreadPoolExecutor.submit(new WeChatSaveSendTask(weChatMessageResponse));
        return weChatMessageResponse;
    }

    class WeChatSaveReceiveMessageTask implements Callable<Void> {
        WeChatMessageDTO weChatMessageDTO;

        public WeChatSaveReceiveMessageTask(WeChatMessageDTO weChatMessageDTO) {
            this.weChatMessageDTO = weChatMessageDTO;
        }

        @Override
        public Void call() throws Exception {
            weChatMessageDTO.setType("receive");
            WeChatMessageDO weChatMessageDO = weChatDomainService.convertDtoToDo(weChatMessageDTO);
            weChatDomainService.saveWeChatMessageDO(weChatMessageDO);
            return null;
        }
    }

    class WeChatSaveSendTask implements Callable<Void> {
        WeChatMessageResponse weChatMessageResponse;

        public WeChatSaveSendTask(WeChatMessageResponse weChatMessageResponse) {
            this.weChatMessageResponse = weChatMessageResponse;
        }

        @Override
        public Void call() throws Exception {
            weChatDomainService.saveWeChatMessageDO(weChatDomainService.convertDtoToDo(weChatMessageResponse));
            return null;
        }
    }

    private WeChatMessageResponse buildHandleMsgResult(String handleMsgResult, WeChatMessageDTO weChatMessageDTO) {
        WeChatMessageResponse weChatMessageResponse = new WeChatMessageResponse();
        weChatMessageResponse.setToUserName(weChatMessageDTO.getFromUserName());
        weChatMessageResponse.setFromUserName(weChatMessageDTO.getToUserName());
        weChatMessageResponse.setMsgType("text");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date(System.currentTimeMillis()));
        weChatMessageResponse.setCreateTime(date);
        weChatMessageResponse.setContent(handleMsgResult);
        return weChatMessageResponse;
    }

    private String handleMsg(WeChatMessageDTO weChatMessageDTO) {
        String headResult = subscribeDomainService.registerArea(weChatMessageDTO);
        if (StringUtils.isNotBlank(headResult)) {
            return headResult;
        }
        String characterRecognitionResult = recommendDomainService.characterRecognition(weChatMessageDTO);
        if (StringUtils.isNotBlank(characterRecognitionResult)) {
            return characterRecognitionResult;
        }
        String handleImageResult = weChatDomainService.handleImageByBaiduOcr(weChatMessageDTO);
        if (StringUtils.isNotBlank(handleImageResult)) {
            return handleImageResult;
        }
        String handleTextResult = weChatDomainService.handleText(weChatMessageDTO);
        if (StringUtils.isNotBlank(handleTextResult)) {
            return handleTextResult;
        }
        String handleWeChatMessageResult = weChatDomainService.handleWeChatMessageDTO(weChatMessageDTO);
        if (StringUtils.isNotBlank(handleWeChatMessageResult)) {
            return handleWeChatMessageResult;
        }
        // chatgpt
        String hadleByOpenAiResult = weChatDomainService.handleByOpenAi(weChatMessageDTO);
        if (StringUtils.isNotBlank(hadleByOpenAiResult)) {
            return hadleByOpenAiResult;
        }
        String handleByTuLingResult = weChatDomainService.handleByTuLing(weChatMessageDTO.getContent());
        if (StringUtils.isNotBlank(handleByTuLingResult)) {
            return handleByTuLingResult;
        }
        return null;
    }

    private void replaceCharacter(WeChatMessageDTO weChatMessageDTO) {
        Map<String, String> replaceMap = Maps.newHashMap();
        replaceMap.put("推荐王爷菜谱", "推荐王爷性价比菜谱");
        replaceMap.put("推荐皇帝菜谱", "推荐皇帝性价比菜谱");
        replaceMap.put("推荐贵妃菜谱", "推荐贵妃性价比菜谱");
        replaceMap.put("推荐太医菜谱", "推荐太医性价比菜谱");
        replaceMap.put("；", ";");
        replaceMap.put("，", ";");
        replaceMap.put("块", "个");
        replaceMap.put("条", "个");
        replaceMap.put("。", ";");
        replaceMap.put("和", ";");
        weChatMessageDTO.setContent(CommonUtil.replaceCharacterByMap(replaceMap, weChatMessageDTO.getContent()));
    }

    private WeChatMessageDTO convertMapToWeChatMessageDTO(Map<String, String> map) {
        WeChatMessageDTO weChatMessageDTO = new WeChatMessageDTO();
        weChatMessageDTO.setToUserName(map.get("ToUserName"));
        weChatMessageDTO.setFromUserName(map.get("FromUserName"));
        weChatMessageDTO.setCreateTime(CommonUtil.getDateTimeByTimeStamp(map.get("CreateTime")));
        weChatMessageDTO.setMsgType(map.get("MsgType"));
        weChatMessageDTO.setContent(map.get("Content"));
        weChatMessageDTO.setMsgId(map.get("MsgId"));
        weChatMessageDTO.setEvent(EventEnum.getEventEnumByName(map.get("Event")));
        weChatMessageDTO.setPicUrl(map.get("PicUrl"));
        weChatMessageDTO.setMediaId(map.get("MediaId"));
        weChatMessageDTO.setFormat(map.get("Format"));
        return weChatMessageDTO;
    }
}
