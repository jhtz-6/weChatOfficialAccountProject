package org.myf.wechatofficialaccountproject.domain.service.factory;


import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;

/**
 * @author myf
 */
public interface MessageTypeHandlerFactory {

    /**
     * 创建对应处理器
     * @param weChatMessageDTO
     * @return
     */
    MessageTypeHandler createMessageTypeHandler(WeChatMessageDTO weChatMessageDTO);
}
