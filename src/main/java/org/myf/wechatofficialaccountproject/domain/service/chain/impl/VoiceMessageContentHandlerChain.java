package org.myf.wechatofficialaccountproject.domain.service.chain.impl;

import org.apache.commons.collections.CollectionUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandlerChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 15:39
 * @Description: VoiceMessageContentHandlerChain
 */
@Service
public class VoiceMessageContentHandlerChain extends MessageContentHandlerChain {

    @Autowired
    List<MessageContentHandler> messageContentHandlerList;

    @Override
    public String handleMessageContentByChain(WeChatMessageDTO weChatMessageDTO,
        List<MessageContentHandler> messageContentHandlerList) {
        return super.handleMessageContentByChain(weChatMessageDTO,
            CollectionUtils.isNotEmpty(messageContentHandlerList) ? messageContentHandlerList
                : createMessageContentHandlerChain(this.messageContentHandlerList, this.getClass()));
    }

}
