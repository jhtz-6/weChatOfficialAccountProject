package org.myf.wechatofficialaccountproject.domain.service.chain;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.HandlerToChainMapping;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.HandlerToChainMappingComparator;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.InitData;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ThreadLocalHolder;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.myf.wechatofficialaccountproject.interfaces.facade.WeChatFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 14:24
 * @Description: 消息内容处理器链
 */
public abstract class MessageContentHandlerChain {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageContentHandlerChain.class);

    protected Map<String, List<MessageContentHandler>> CHAIN_TO_HANDLER_MAP = new HashMap<>(24);

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
        String lastHandlerResult = WeChatUtil.DEFAULT_LAST_HANDLER_RESULT;
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
    protected List<MessageContentHandler>
        createMessageContentHandlerChain(List<MessageContentHandler> originMessageContentHandlerList, Class<?> clazz) {
        try {
            List<MessageContentHandler> finalMessageContentHandlerList = Lists.newArrayList();
            Map<String, List<HandlerToChainMapping>> classToChainMap =
                InitData.CHAIN_TO_HANDLER_MAP.get(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get().name());
            if (MapUtils.isEmpty(classToChainMap)) {
                return Collections.emptyList();
            }
            List<HandlerToChainMapping> tempHandlerList = new ArrayList<>();
            List<HandlerToChainMapping> HandlerToChainMappingList =
                JSON.parseObject(classToChainMap.get(clazz.getName()).toString(), List.class);
            for (Object jsonObject : HandlerToChainMappingList) {
                HandlerToChainMapping mapping = JSON.parseObject(jsonObject.toString(), HandlerToChainMapping.class);
                tempHandlerList.add(mapping);
            }
            tempHandlerList.sort(Comparator.comparingInt(HandlerToChainMapping::getPriority));
            for (HandlerToChainMapping handlerToChainMapping : tempHandlerList) {
                String handlerName = handlerToChainMapping.getHandlerName();
                Optional<MessageContentHandler> matchingHandler = originMessageContentHandlerList.stream()
                    .filter(handler -> handler.getClass().getName().equals(handlerName)).findFirst();
                matchingHandler.ifPresent(finalMessageContentHandlerList::add);
            }
            return finalMessageContentHandlerList;
        } catch (Exception e) {
            LOGGER.error("createMessageContentHandlerChain.messageContentHandlerList : {}",
                JSON.toJSONString(originMessageContentHandlerList), e);
            return Collections.emptyList();
        }

    }

}
