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
 * @CreateTime: 2023-04-17 15:35
 * @Description: TextMessageContentHandlerChain
 */
@Service
public class TextMessageContentHandlerChain extends MessageContentHandlerChain {
    private static final List<MessageContentHandler> textMessageContentHandlerList = Lists.newLinkedList();

    public void addMessageContentHandler(MessageContentHandler messageContentHandler) {
        super.addMessageContentHandler(messageContentHandler, textMessageContentHandlerList);
    }

    @Override
    public String handleMessageContentByChain(WeChatMessageDTO weChatMessageDTO,
        List<MessageContentHandler> messageContentHandlerList) {
        if (CollectionUtils.isNotEmpty(messageContentHandlerList)) {
            return super.handleMessageContentByChain(weChatMessageDTO, messageContentHandlerList);
        }
        return super.handleMessageContentByChain(weChatMessageDTO, textMessageContentHandlerList);
    }

    @PostConstruct
    @Override
    protected void createMessageContentHandlerChain() {
        textMessageContentHandlerList.add(new RegisterAreaHandler());
        textMessageContentHandlerList.add(new QueryFoodOrMaterialHandler());
        textMessageContentHandlerList.add(new SimpleKeyWordHandler());
        textMessageContentHandlerList.add(new CharacterRecognitionHandler());
        textMessageContentHandlerList.add(new OpenAiHandler());
        textMessageContentHandlerList.add(new SendMobileMessageHandler());
        textMessageContentHandlerList.add(new TuLingHandler());
    }

}
