package org.myf.wechatofficialaccountproject.domain.service;

import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;

/**
 * @author myf
 */
public interface ChatgptMessageDomainService {

    /**
     * handleByOpenAiResult
     * 
     * @param weChatMessageDTO
     * @param openAiText
     */
    void handleByOpenAiResult(WeChatMessageDTO weChatMessageDTO, String openAiText);
}
