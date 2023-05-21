package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler;

import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.entity.WeChatMessage;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.AdviseDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.AdviseRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ThreadLocalHolder;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18 20:28
 * @Description: ComplexKeyWordHandler 复杂关键词的处理
 */
@Service
public class ComplexKeyWordHandler implements MessageContentHandler {
    @Autowired
    AdviseRepository adviseRepository;

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        WeChatMessage weChatMessage = new WeChatMessage(weChatMessageDTO.getFromUserName(),
            MsgTypeEnum.getMsgTypeEnumByName(weChatMessageDTO.getMsgType()), weChatMessageDTO.getPicUrl(),
            weChatMessageDTO.getContent(), WeChatUtil.MENU_LIST_MAP.get(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get()));
        String handleNumTextResult = weChatMessage.handleBelongOrNumText();
        if (StringUtils.isNotBlank(handleNumTextResult)) {
            return handleNumTextResult;
        }
        String handleSpecialWordResult = weChatMessage.handleSpecialWord();
        if (StringUtils.isNotBlank(handleSpecialWordResult)) {
            return handleSpecialWordResult;
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
        if (StringUtils.isNotBlank(WeChatUtil.RECOMMEND_MENU_LIST.stream()
            .filter(x -> weChatMessage.getContent().startsWith(x)).findAny().orElse(null))) {
            return CommonUtil.recommendMenuByContent(weChatMessage.getContent());
        }
        if (weChatMessage.getContent().startsWith("建议")) {
            if (weChatMessage.getContent().length() > 2000) {
                return "大人,您的建议内容过长~~~ 系统录入失败";
            } else {
                AdviseDO adviseDO = new AdviseDO();
                adviseDO.setContent(weChatMessage.getContent());
                adviseDO.setUserId(weChatMessage.getFromUserName());
                adviseDO.setBelonger(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get());
                adviseRepository.saveOrUpdateById(adviseDO);
                return "大人,您的宝贵建议【" + weChatMessage.getContent() + "】已被系统成功录入~~~";
            }
        }

        String errorCorrectionResult = weChatMessage.errorCorrection();
        if (StringUtils.isNotBlank(errorCorrectionResult)) {
            return errorCorrectionResult;
        }
        return null;
    }

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        return StringUtils.equalsAny(weChatMessageDTO.getMsgType(), MsgTypeEnum.TEXT.name, MsgTypeEnum.VOICE.name);
    }
}
