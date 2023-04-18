package org.myf.wechatofficialaccountproject.domain.service.chain.impl;

import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.entity.WeChatMessage;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.AdviseDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.AdviseRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ApplicationContextUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;

import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:14
 * @Description: 处理简单关键字相关逻辑
 */
public class SimpleKeyWordHandler implements MessageContentHandler {

    static AdviseRepository adviseRepository;

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        if (Objects.isNull(adviseRepository)) {
            adviseRepository = (AdviseRepository)ApplicationContextUtil.getBeanByName("adviseRepositoryImpl");
        }
        WeChatMessage weChatMessage = new WeChatMessage(weChatMessageDTO.getFromUserName(),
            MsgTypeEnum.getMsgTypeEnumByName(weChatMessageDTO.getMsgType()), weChatMessageDTO.getPicUrl(),
            weChatMessageDTO.getContent());
        String handKeyWordResult = weChatMessage.handKeyWord();
        if (StringUtils.isNotBlank(handKeyWordResult)) {
            return handKeyWordResult;
        }
        String handleNumTextResult = weChatMessage.handleBelongOrNumText();
        if (StringUtils.isNotBlank(handleNumTextResult)) {
            return handleNumTextResult;
        }
        String handleSpecialWordResult = weChatMessage.handleSpecialWord();
        if (StringUtils.isNotBlank(handleSpecialWordResult)) {
            return handleSpecialWordResult;
        }
        if (WeChatUtil.ACCOMPANY_MAP.containsKey(weChatMessageDTO.getContent())) {
            return CommonUtil.conactAccompany(WeChatUtil.ACCOMPANY_MAP.get(weChatMessageDTO.getContent()));
        }
        String handleCostPerformanceResult = weChatMessage.handleCostPerformance();
        if (StringUtils.isNotBlank(handleCostPerformanceResult)) {
            return handleCostPerformanceResult;
        }
        String handleFishTextResult = weChatMessage.handleFishText();
        if (StringUtils.isNotBlank(handleFishTextResult)) {
            return handleFishTextResult;
        }
        String handleCategoryResult = weChatMessage.handleCategory();
        if (StringUtils.isNotBlank(handleCategoryResult)) {
            return handleCategoryResult;
        }
        if (weChatMessage.getContent().startsWith("建议")) {
            if (weChatMessage.getContent().length() > 2000) {
                return "大人,您的建议内容过长~~~ 系统录入失败";
            } else {
                AdviseDO adviseDO = new AdviseDO();
                adviseDO.setContent(weChatMessage.getContent());
                adviseDO.setUserId(weChatMessage.getFromUserName());
                adviseRepository.saveOrUpdateById(adviseDO);
                return "大人,您的宝贵建议【" + weChatMessage.getContent() + "】已被系统成功录入~~~";
            }
        }
        if (StringUtils.isNotBlank(WeChatUtil.RECOMMEND_MENU_LIST.stream()
            .filter(x -> weChatMessage.getContent().startsWith(x)).findAny().orElse(null))) {
            return CommonUtil.recommendMenuByContent(weChatMessage.getContent());
        }
        String errorCorrectionResult = weChatMessage.errorCorrection();
        if (StringUtils.isNotBlank(errorCorrectionResult)) {
            return errorCorrectionResult;
        }
        return null;
    }

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        if (!StringUtils.equalsAny(weChatMessageDTO.getMsgType(), MsgTypeEnum.TEXT.name, MsgTypeEnum.VOICE.name)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldContinue(WeChatMessageDTO weChatMessageDTO) {
        return false;
    }
}
