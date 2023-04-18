package org.myf.wechatofficialaccountproject.domain.service.chain;

import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;

import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 14:24
 * @Description: 消息内容处理器链
 */
public abstract class MessageContentHandlerChain {

    /**
     * 添加消息内容处理器
     * 
     * @param messageContentHandler
     */
    public void addMessageContentHandler(MessageContentHandler messageContentHandler,
        List<MessageContentHandler> messageContentHandlerList) {
        synchronized (messageContentHandlerList) {
            if (!messageContentHandlerList.contains(messageContentHandler)) {
                messageContentHandlerList.add(messageContentHandler);
            }
        }
    };

    /**
     * 调用处理器链处理消息内容
     * 
     * @param weChatMessageDTO
     * @return
     */
    public String handleMessageContentByChain(WeChatMessageDTO weChatMessageDTO,
        List<MessageContentHandler> messageContentHandlerList) {
        String lastHandlerResult = "";
        for (MessageContentHandler messageContentHandler : messageContentHandlerList) {
            if (messageContentHandler.isMatched(weChatMessageDTO)) {
                String handlerMessageContentResult = messageContentHandler.handlerMessageContent(weChatMessageDTO);
                if (StringUtils.isNotBlank(handlerMessageContentResult)) {
                    if (!messageContentHandler.shouldContinue(weChatMessageDTO)) {
                        return handlerMessageContentResult;
                    } else {
                        lastHandlerResult = handlerMessageContentResult;
                    }
                }
            }
        }
        return lastHandlerResult;
    };

    /**
     * 创建消息内容处理器链
     */
    protected abstract void createMessageContentHandlerChain();

}
