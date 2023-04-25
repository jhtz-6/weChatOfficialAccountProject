package org.myf.wechatofficialaccountproject.domain.service.factory.impl;

import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.factory.MessageTypeHandler;
import org.myf.wechatofficialaccountproject.domain.service.factory.MessageTypeHandlerFactory;
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
        if (MsgTypeEnum.IMAGE.name.equals(weChatMessageDTO.getMsgType())) {
            return new ImageMessageTypeHandler(weChatMessageDTO);
        } else if (MsgTypeEnum.TEXT.name.equals(weChatMessageDTO.getMsgType())) {
            return new TextMessageTypeHandler(weChatMessageDTO);
        } else if (MsgTypeEnum.VOICE.name.equals(weChatMessageDTO.getMsgType())) {
            return new VoiceMessageTypeHandler(weChatMessageDTO);
        } else if (MsgTypeEnum.EVENT.name.equals(weChatMessageDTO.getMsgType())) {
            return new EventMessageTypeHandler(weChatMessageDTO);
        } else {
            throw new IllegalArgumentException("暂不支持该类型:" + weChatMessageDTO.getMsgType() + "的消息");
        }
    }
}
