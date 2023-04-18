package org.myf.wechatofficialaccountproject.domain.service.chain.impl;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandlerChain;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 15:39
 * @Description: VoiceMessageContentHandlerChain
 */
@Service
public class VoiceMessageContentHandlerChain extends MessageContentHandlerChain {

    private static final List<MessageContentHandler> voiceMessageContentHandlerList = Lists.newLinkedList();

    public void addMessageContentHandler(MessageContentHandler messageContentHandler) {
        super.addMessageContentHandler(messageContentHandler, voiceMessageContentHandlerList);
    }

    @Override
    public String handleMessageContentByChain(WeChatMessageDTO weChatMessageDTO,
        List<MessageContentHandler> messageContentHandlerList) {
        if (CollectionUtils.isNotEmpty(messageContentHandlerList)) {
            return super.handleMessageContentByChain(weChatMessageDTO, messageContentHandlerList);
        }
        return super.handleMessageContentByChain(weChatMessageDTO, voiceMessageContentHandlerList);
    }

    @PostConstruct
    @Override
    protected void createMessageContentHandlerChain() {
        voiceMessageContentHandlerList.add(new RegisterAreaHandler());
        voiceMessageContentHandlerList.add(new QueryFoodOrMaterialHandler());
        voiceMessageContentHandlerList.add(new SimpleKeyWordHandler());
        voiceMessageContentHandlerList.add(new CharacterRecognitionHandler());
        voiceMessageContentHandlerList.add(new OpenAiHandler());
        voiceMessageContentHandlerList.add(new SendMobileMessageHandler());
        voiceMessageContentHandlerList.add(new TuLingHandler());
    }
}
