package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler;

import com.alibaba.fastjson.JSON;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.TencentShortMessageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler.SendMobile.EXCEED_LENGTH;
import static org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler.SendMobile.MAX_LENGTH;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:14
 * @Description: 发送手机短信相关逻辑
 */
@Service
public class SendMobileMessageHandler implements MessageContentHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMobileMessageHandler.class);
    @Autowired
    TencentShortMessageClient tencentShortMessageClient;

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        String weChatMessageDTOContent = weChatMessageDTO.getContent();
        if (weChatMessageDTOContent.length() > MAX_LENGTH) {
            return EXCEED_LENGTH;
        }
        String[] params = new String[9];
        try {
            for (int i = 0; i < params.length; i++) {
                int start = 6 * 0;
                int end = Math.min(start + 6, weChatMessageDTOContent.length());
                params[i] = weChatMessageDTOContent.substring(start, end);
            }
        } catch (Exception e) {
            LOGGER.error("weChatMessageDTOContent:{}", e, JSON.toJSONString(weChatMessageDTOContent), e);
        }
        SmsSingleSenderResult smsSingleSenderResult;
        try {
            smsSingleSenderResult = tencentShortMessageClient.sendShortMessagebyParams(params, 415610);
        } catch (Exception e) {
            LOGGER.error("handlerMessageContent.weChatMessageDTOContent:{}", JSON.toJSONString(weChatMessageDTOContent),
                e);
            return "短信发送失败:" + e;
        }
        if (smsSingleSenderResult.result != 0) {
            LOGGER.warn("weChatMessageDTOContent:" + JSON.toJSONString(weChatMessageDTOContent));
            return "短信发送失败错误信息:" + smsSingleSenderResult.errMsg;
        }
        return "大人的短信已经成功发送到小的手机上了~~~";
    }

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        return StringUtils.equalsAny(weChatMessageDTO.getMsgType(), MsgTypeEnum.TEXT.name, MsgTypeEnum.VOICE.name)
            && weChatMessageDTO.getContent().startsWith("紧急");
    }

    @Override
    public boolean shouldContinue(WeChatMessageDTO weChatMessageDTO) {
        return false;
    }
}
