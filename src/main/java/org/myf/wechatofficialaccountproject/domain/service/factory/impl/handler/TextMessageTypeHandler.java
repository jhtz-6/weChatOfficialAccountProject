package org.myf.wechatofficialaccountproject.domain.service.factory.impl.handler;

import lombok.AllArgsConstructor;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandlerChain;
import org.myf.wechatofficialaccountproject.domain.service.factory.MessageTypeHandler;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ApplicationContextUtil;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17  11:07
 * @Description: 文字消息处理器
 */
@AllArgsConstructor
public class TextMessageTypeHandler implements MessageTypeHandler {

    static final MessageContentHandlerChain textMessageContentHandlerChain = (MessageContentHandlerChain)
            ApplicationContextUtil.getBeanByName("textMessageContentHandlerChain");
    private WeChatMessageDTO weChatMessageDTO;
    @Override
    public String handleMessageChain() {
        return textMessageContentHandlerChain.handleMessageContentByChain(weChatMessageDTO, null);
    }
}
