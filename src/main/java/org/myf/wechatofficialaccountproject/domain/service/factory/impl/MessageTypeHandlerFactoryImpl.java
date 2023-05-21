package org.myf.wechatofficialaccountproject.domain.service.factory.impl;

import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.factory.MessageTypeHandler;
import org.myf.wechatofficialaccountproject.domain.service.factory.MessageTypeHandlerFactory;
import org.myf.wechatofficialaccountproject.domain.service.factory.impl.handler.EventMessageTypeHandler;
import org.myf.wechatofficialaccountproject.domain.service.factory.impl.handler.ImageMessageTypeHandler;
import org.myf.wechatofficialaccountproject.domain.service.factory.impl.handler.TextMessageTypeHandler;
import org.myf.wechatofficialaccountproject.domain.service.factory.impl.handler.VoiceMessageTypeHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.springframework.stereotype.Service;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 18:02
 * @Description: MessageTypeHandlerFactoryImpl
 */
@Service
public class MessageTypeHandlerFactoryImpl implements MessageTypeHandlerFactory {

    @Override
    public MessageTypeHandler createMessageTypeHandler(WeChatMessageDTO weChatMessageDTO) {
        MsgTypeEnum msgTypeEnum = MsgTypeEnum.getMsgTypeEnumByName(weChatMessageDTO.getMsgType());
        switch (msgTypeEnum) {
            case IMAGE:
                return new ImageMessageTypeHandler(weChatMessageDTO);
            case TEXT:
                return new TextMessageTypeHandler(weChatMessageDTO);
            case VOICE:
                return new VoiceMessageTypeHandler(weChatMessageDTO);
            case EVENT:
                return new EventMessageTypeHandler(weChatMessageDTO);
            default:
                throw new IllegalArgumentException("暂不支持该类型:" + weChatMessageDTO.getMsgType() + "的消息");
        }
    }
}
