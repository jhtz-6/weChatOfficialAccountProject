package org.myf.wechatofficialaccountproject.domain.service;

import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;

/**
 * @author myf
 */
public interface RecommendDomainService {

    /**
     * 关键词文字识别相关逻辑处理
     * 
     * @param weChatMessageDTO
     * @return
     */
    String characterRecognition(WeChatMessageDTO weChatMessageDTO);

}
