package org.myf.wechatofficialaccountproject.domain.service.factory.impl.handler;

import lombok.AllArgsConstructor;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandlerChain;
import org.myf.wechatofficialaccountproject.domain.service.factory.MessageTypeHandler;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ApplicationContextUtil;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17  11:09
 * @Description: 语音消息处理器
 */
@AllArgsConstructor
public class VoiceMessageTypeHandler implements MessageTypeHandler {

    private WeChatMessageDTO weChatMessageDTO;

    static final MessageContentHandlerChain voiceMessageContentHandlerChain = (MessageContentHandlerChain)
            ApplicationContextUtil.getBeanByName("voiceMessageContentHandlerChain");

    @Override
    public String handleMessageChain() {
        return voiceMessageContentHandlerChain.handleMessageContentByChain(weChatMessageDTO,null);
    }
}
