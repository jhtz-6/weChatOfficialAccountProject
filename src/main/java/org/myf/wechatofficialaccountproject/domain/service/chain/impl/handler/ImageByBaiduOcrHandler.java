package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.MenuDTO;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.BaiduOcrClient;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.RedisClient;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.BaiduOcrResponse;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.BaiduOcrWordsResult;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ThreadLocalHolder;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:13
 * @Description: 百度OCR处理图片相关逻辑
 */
@Service
public class ImageByBaiduOcrHandler implements MessageContentHandler {
    @Autowired
    BaiduOcrClient baiduOcrClient;
    @Autowired
    RedisClient redisClient;

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        String handleImageResult = "";
        if (BooleanEnum.TRUE.value
            .equals(redisClient.getValueByKey(WeChatUtil.OCR_MENU_ACTION + weChatMessageDTO.getFromUserName()))) {
            BaiduOcrResponse generalImageResult =
                baiduOcrClient.getGeneralImageByPhotoUrl(weChatMessageDTO.getPicUrl());
            if (Objects.isNull(generalImageResult)) {
                return "公众号当前访问图片识别人数过多,请稍后再试~~";
            } else {
                List<BaiduOcrWordsResult> resultList = generalImageResult.getWords_result();
                resultList = resultList.stream().map(x -> {
                    if (x.getWords().equals("萝ト")) {
                        x.setWords("萝卜");
                    }
                    return x;
                }).collect(Collectors.toList());
                for (BaiduOcrWordsResult baiduOcrResultWords : resultList) {
                    BaiduOcrWordsResult MaterialNum;
                    if (WeChatUtil.MATERIAL_NAME_LIST.contains(baiduOcrResultWords.getWords())) {
                        Integer materialLeft = baiduOcrResultWords.getLocation().getLeft();
                        Integer materialTop = baiduOcrResultWords.getLocation().getTop();
                        MaterialNum = resultList.stream()
                            .filter(x -> !x.getWords().equals(baiduOcrResultWords.getWords())
                                && StringUtils.isNumeric(x.getWords()) && (x.getLocation().getLeft() - materialLeft) > 0
                                && (x.getLocation().getLeft() - materialLeft) < 200
                                && materialTop - x.getLocation().getTop() > 0
                                && materialTop - x.getLocation().getTop() < 60)
                            .findAny().orElse(null);

                        if (Objects.isNull(MaterialNum)) {
                            handleImageResult += "1个" + baiduOcrResultWords.getWords() + ";";
                        } else {
                            handleImageResult += MaterialNum.getWords() + "个" + baiduOcrResultWords.getWords() + ";";
                        }
                    }
                }
                if (StringUtils.isNotBlank(handleImageResult)) {
                    if (StringUtils.isNotBlank(
                        redisClient.getValueByKey(WeChatUtil.OCR_MENU_ACTION + weChatMessageDTO.getFromUserName()))) {
                        redisClient.addValueToRedis(WeChatUtil.OCR_MENU_CONTENT + weChatMessageDTO.getFromUserName(),
                            redisClient.getValueByKey(WeChatUtil.OCR_MENU_CONTENT + weChatMessageDTO.getFromUserName())
                                + handleImageResult,
                            1000 * 60 * 60 * 24L);
                    }
                    return "收到的内容是:" + handleImageResult;
                }
            }
        } else {
            // 说明是进行菜谱查询
            BaiduOcrResponse webImageResult = baiduOcrClient.getWebImageByPhotoUrl(weChatMessageDTO.getPicUrl());
            if (Objects.isNull(webImageResult)) {
                return "公众号当前访问图片识别人数过多,请稍后再试~~";
            } else {
                List<BaiduOcrWordsResult> resultList = webImageResult.getWords_result();
                if (CollectionUtils.isEmpty(resultList)) {
                    return "大人,您是要和我斗图吗？\n对于图片我有如下功能关键词:【文字识别开始】、【菜谱识图】";
                }
                List<String> ResultStrList = resultList.stream().map(x -> x.getWords()).collect(Collectors.toList());
                List<MenuDTO> menuList = WeChatUtil.MENU_LIST_MAP.get(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get())
                    .stream().filter(x -> ResultStrList.contains(x.getFood().trim())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(menuList)) {
                    for (int i = 0; i < menuList.size(); i++) {
                        MenuDTO menuDTO = new MenuDTO();
                        CommonUtil.copyPropertiesWithNull(menuList.get(i), menuDTO);
                        handleImageResult =
                            handleImageResult + (i + 1) + "、" + CommonUtil.conactMenuOneUserWithCost(menuDTO) + "\n";
                    }
                    return handleImageResult;
                }
            }
            handleImageResult = "大人,您是要和我斗图吗？\n对于图片我有如下功能关键词:【文字识别开始】、【菜谱识图】";
        }
        return handleImageResult;
    }

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        return MsgTypeEnum.IMAGE.name.equals(weChatMessageDTO.getMsgType());
    }

    @Override
    public boolean shouldContinue(WeChatMessageDTO weChatMessageDTO) {
        return false;
    }
}
