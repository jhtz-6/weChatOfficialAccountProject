package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.RecommendMenuDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.RedisClient;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.RecommendMenuQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.RecommendMenuRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:12
 * @Description: 关键字"文字识别"相关逻辑
 */
@Service
public class CharacterRecognitionHandler implements MessageContentHandler {

    @Autowired
    RecommendMenuRepository recommendMenuRepository;
    @Autowired
    RedisClient redisClient;

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        String handleResult = "";
        String ocrMenuContentKey = WeChatUtil.OCR_MENU_CONTENT + weChatMessageDTO.getFromUserName();
        String ocrMenuActionKey = WeChatUtil.OCR_MENU_ACTION + weChatMessageDTO.getFromUserName();
        if (CharacterRecognition.START_OCR.equals(weChatMessageDTO.getContent())) {
            redisClient.addValueToRedis(ocrMenuActionKey, BooleanEnum.TRUE.value, 1000 * 60 * 60 * 24L);
            if (StringUtils.isNotBlank(redisClient.getValueByKey(ocrMenuContentKey))
                && redisClient.deleteValueByKey(ocrMenuContentKey)) {
                RecommendMenuQueryParam recommendMenuQueryParam = new RecommendMenuQueryParam();
                recommendMenuQueryParam.setFromUserName(weChatMessageDTO.getFromUserName());
                List<RecommendMenuDO> recommendMenuDOList =
                    recommendMenuRepository.selectListByParam(recommendMenuQueryParam);
                if (CollectionUtils.isNotEmpty(recommendMenuDOList)) {
                    for (RecommendMenuDO x : recommendMenuDOList) {
                        RecommendMenuDO recommendMenu = new RecommendMenuDO();
                        recommendMenu.setSfyx("0");
                        recommendMenu.setId(x.getId());
                        recommendMenuRepository.saveOrUpdateByID(recommendMenu);
                    }
                }
            }
            handleResult = WeChatUtil.OCR_GEGIN_CONTENT;
        } else if (StringUtils.equalsAny(weChatMessageDTO.getContent(), CharacterRecognition.END_OCR)
            || weChatMessageDTO.getContent().contains(CharacterRecognition.OCR)) {
            if (StringUtils.isEmpty(redisClient.getValueByKey(ocrMenuContentKey))) {
                handleResult = CharacterRecognition.DEFAULT_RESULT;
            } else {
                redisClient.deleteValueByKey(ocrMenuActionKey);
                String afterwords = weChatMessageDTO.getContent().substring(4);
                String ocrMenuContentCalue = redisClient.getValueByKey(ocrMenuContentKey);
                if (WeChatUtil.RECOMMEND_MENU_LIST.contains(afterwords)) {
                    if (StringUtils.isBlank(ocrMenuContentCalue)) {
                        RecommendMenuQueryParam recommendMenuQueryParam = new RecommendMenuQueryParam();
                        recommendMenuQueryParam.setFromUserName(weChatMessageDTO.getFromUserName());
                        List<RecommendMenuDO> recommendMenuDOList =
                            recommendMenuRepository.selectListByParam(recommendMenuQueryParam);
                        if (CollectionUtils.isNotEmpty(recommendMenuDOList)) {
                            ocrMenuContentCalue = recommendMenuDOList.get(0).getMenuContent();
                            redisClient.addValueToRedis(ocrMenuContentKey, ocrMenuContentCalue, 1000 * 60 * 60 * 24L);
                        }
                    }
                    handleResult = CommonUtil.recommendMenuByContent(afterwords + ":" + ocrMenuContentCalue);
                } else {
                    RecommendMenuDO recommendMenuDO = new RecommendMenuDO();
                    recommendMenuDO.setFromUserName(weChatMessageDTO.getFromUserName());
                    recommendMenuDO.setMenuContent(ocrMenuContentCalue);
                    recommendMenuRepository.saveOrUpdateByID(recommendMenuDO);
                    handleResult =
                        CommonUtil.recommendMenuByContent(WeChatUtil.RECOMMENDED_MENU + ":" + ocrMenuContentCalue);
                }
            }
        }
        return handleResult;
    }

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        return StringUtils.equalsAny(weChatMessageDTO.getMsgType(), MsgTypeEnum.TEXT.name, MsgTypeEnum.VOICE.name);
    }

    @Override
    public boolean shouldContinue(WeChatMessageDTO weChatMessageDTO) {
        return false;
    }
}
