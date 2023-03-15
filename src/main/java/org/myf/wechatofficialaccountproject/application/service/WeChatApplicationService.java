package org.myf.wechatofficialaccountproject.application.service;

import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageResponse;

import java.util.Map;

/**
 * @author myf
 */
public interface WeChatApplicationService {
    /**
     * 处理微信消息map,返回最终处理结果。
     * @param map
     * @return WeChatMessageDTO
     */
    WeChatMessageResponse handleMsgbyMap(Map<String, String> map);

}
