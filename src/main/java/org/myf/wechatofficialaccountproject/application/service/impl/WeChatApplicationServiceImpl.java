package org.myf.wechatofficialaccountproject.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.application.service.WeChatApplicationService;
import org.myf.wechatofficialaccountproject.domain.service.RecommendDomainService;
import org.myf.wechatofficialaccountproject.domain.service.SubscribeDomainService;
import org.myf.wechatofficialaccountproject.domain.service.WeChatDomainService;
import org.myf.wechatofficialaccountproject.domain.service.factory.MessageTypeHandlerFactory;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.WeChatMessageDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.EventEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ThreadLocalHolder;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 12:44
 * @Description: WeChatApplicationService实现类
 */
@Service
public class WeChatApplicationServiceImpl implements WeChatApplicationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatApplicationServiceImpl.class);

    @Autowired
    WeChatDomainService weChatDomainService;
    @Autowired
    SubscribeDomainService subscribeDomainService;
    @Autowired
    RecommendDomainService recommendDomainService;
    @Autowired
    MessageTypeHandlerFactory messageTypeHandlerFactory;
    static ExecutorService weChatSaveThreadPoolExecutor;
    static {
        AtomicInteger saveSendThreadNumber = new AtomicInteger(1);
        weChatSaveThreadPoolExecutor =
            new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS, new LinkedBlockingDeque<>(20), task -> {
                Thread thread = new Thread(task);
                thread.setName(
                    "weChatMessageSaveThread-" + saveSendThreadNumber.incrementAndGet() + "-" + thread.getName());
                return thread;
            });
    }

    @Override
    public String handleMsgByMap(Map<String, String> map) {
        WeChatMessageDTO weChatMessageDTO = convertMapToWeChatMessageDTO(map);
        String handleMsgResult = "";
        try {
            weChatMessageDTO.setType("receive");
            weChatSaveThreadPoolExecutor
                .submit(new WeChatSaveSendTask(weChatMessageDTO, ThreadLocalHolder.BELONGER_THREAD_LOCAL.get()));
            replaceCharacter(weChatMessageDTO);
            handleMsgResult = messageTypeHandlerFactory.createMessageTypeHandler(weChatMessageDTO).handleMessageChain();
            if (StringUtils.isEmpty(handleMsgResult)) {
                handleMsgResult = "服务处理异常,请稍后再试或联系管理员处理";
            }
            String currentPersonNum = weChatDomainService.getCurrentPersonNum(
                ThreadLocalHolder.BELONGER_THREAD_LOCAL.get() + WeChatUtil.CURRENT_PERSON_KEY
                    + weChatMessageDTO.getFromUserName(),
                weChatMessageDTO.getFromUserName(), WeChatUtil.CURRENT_PERSON_TIMEOUT);
            handleMsgResult = "当前在线人数:" + currentPersonNum + "\n" + handleMsgResult;
        } catch (RejectedExecutionException rejectedExecutionException) {
            if (StringUtils.isBlank(handleMsgResult)) {
                handleMsgResult = "当前访问人数较多,请稍后再试!";
            }
        } catch (Exception e) {
            LOGGER.error("map:{}", JSON.toJSONString(map), e);
            if (StringUtils.isBlank(handleMsgResult)) {
                handleMsgResult = e.getMessage();
            }
        }
        WeChatMessageDTO weChatSendMessageDTO = new WeChatMessageDTO();
        try {
            buildAndSaveWechatSendMessageDTO(weChatSendMessageDTO, weChatMessageDTO, handleMsgResult);
        } catch (RejectedExecutionException rejectedExecutionException) {
            LOGGER.error("handleMsgbyMap.map:{}", JSON.toJSONString(map), rejectedExecutionException);
        }
        return handleMsgResult;
    }

    class WeChatSaveSendTask implements Callable<Void> {
        WeChatMessageDTO weChatMessageDTO;
        SystemBelongEnum systemBelongEnum;

        public WeChatSaveSendTask(WeChatMessageDTO weChatMessageDTO, SystemBelongEnum systemBelongEnum) {
            this.weChatMessageDTO = weChatMessageDTO;
            this.systemBelongEnum = systemBelongEnum;
        }

        @Override
        public Void call() {
            WeChatMessageDO weChatMessageDO = weChatDomainService.convertDtoToDo(weChatMessageDTO);
            if (Objects.nonNull(systemBelongEnum)) {
                weChatMessageDO.setBelonger(systemBelongEnum);
            }
            weChatDomainService.saveWeChatMessageDO(weChatMessageDO);
            return null;
        }
    }

    @Deprecated
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
        weChatMessageDTO.setEvent(EventEnum.getEventEnumByName(map.get("Event")));
        if (MsgTypeEnum.VOICE.name.equals(weChatMessageDTO.getMsgType())) {
            String recognition = map.get("Recognition");
            if (StringUtils.isNotBlank(recognition)) {
                weChatMessageDTO.setContent(recognition.substring(0, recognition.length() - 1));
            }
        } else if (MsgTypeEnum.VIDEO.name.equals(weChatMessageDTO.getMsgType())) {
            weChatMessageDTO.setContent(map.get("MediaId"));
        } else if (EventEnum.VIEW.equals(weChatMessageDTO.getEvent())) {
            weChatMessageDTO.setContent(map.get("EventKey"));
        } else {
            weChatMessageDTO.setContent(map.get("Content"));
        }
        weChatMessageDTO.setMsgId(map.get("MsgId"));
        weChatMessageDTO.setPicUrl(map.get("PicUrl"));
        weChatMessageDTO.setMediaId(map.get("MediaId"));
        weChatMessageDTO.setFormat(map.get("Format"));
        return weChatMessageDTO;
    }

    private void buildAndSaveWechatSendMessageDTO(WeChatMessageDTO weChatSendMessageDTO,
        WeChatMessageDTO weChatMessageDTO, String handleMsgResult) {
        weChatSendMessageDTO.setContent(handleMsgResult);
        weChatSendMessageDTO.setType("send");
        weChatSendMessageDTO.setCreateTime(null);
        weChatSendMessageDTO.setFromUserName(weChatMessageDTO.getToUserName());
        weChatSendMessageDTO.setToUserName(weChatMessageDTO.getFromUserName());
        weChatSendMessageDTO.setMsgType(MsgTypeEnum.TEXT.name);
        weChatSendMessageDTO.setMsgId(weChatMessageDTO.getMsgId());
        weChatSaveThreadPoolExecutor
            .submit(new WeChatSaveSendTask(weChatSendMessageDTO, ThreadLocalHolder.BELONGER_THREAD_LOCAL.get()));
    }
}
