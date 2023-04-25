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
 * @CreateTime: 2023-04-17 15:38
 * @Description: ImageMessageContentHandlerChain
 */
@Service
public class ImageMessageContentHandlerChain extends MessageContentHandlerChain {
    private static final List<MessageContentHandler> imageMessageContentHandlerList = Lists.newLinkedList();

    public void addMessageContentHandler(MessageContentHandler messageContentHandler) {
        super.addMessageContentHandler(messageContentHandler, imageMessageContentHandlerList);
    }

    @Override
    public String handleMessageContentByChain(WeChatMessageDTO weChatMessageDTO,
        List<MessageContentHandler> messageContentHandlerList) {
        return super.handleMessageContentByChain(weChatMessageDTO, CollectionUtils.isNotEmpty(messageContentHandlerList)
            ? messageContentHandlerList : imageMessageContentHandlerList);
    }

    @PostConstruct
    @Override
    protected void createMessageContentHandlerChain() {
        imageMessageContentHandlerList.add(new ImageByBaiduOcrHandler());
    }
}
