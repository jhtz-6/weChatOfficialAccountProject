package org.myf.wechatofficialaccountproject.domain.service.factory;

import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;

/**
 * @author myf
 */
public interface MessageTypeHandler {

    /**
     * 执行消息处理器链
     * @param
     * @return
     */
    String handleMessageChain();
}
