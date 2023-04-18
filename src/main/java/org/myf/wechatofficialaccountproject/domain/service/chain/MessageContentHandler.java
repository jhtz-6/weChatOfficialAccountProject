package org.myf.wechatofficialaccountproject.domain.service.chain;

import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;

/**
 * @author myf
 */
public interface MessageContentHandler {

    /**
     * 处理消息内容,返回处理结果
     * @param weChatMessageDTO
     * @return
     */
    String handlerMessageContent(WeChatMessageDTO weChatMessageDTO);

    /**
     * 匹配条件,返回才会去执行具体处理方法
     * @param weChatMessageDTO
     * @return
     */
    boolean isMatched(WeChatMessageDTO weChatMessageDTO);

    /**
     * 是否要继续执行后面的处理器,默认不执行,如果需要执行,处理器则只会返回最后一次的处理结果。
     * @param weChatMessageDTO
     * @return
     */
    default boolean shouldContinue(WeChatMessageDTO weChatMessageDTO){
        return false;
    };


}
