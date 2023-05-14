package org.myf.wechatofficialaccountproject.application.service;

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
    String handleMsgbyMap(Map<String, String> map);

}
