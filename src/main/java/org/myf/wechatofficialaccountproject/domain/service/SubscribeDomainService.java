package org.myf.wechatofficialaccountproject.domain.service;

import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;

/**
 * @author myf
 */
public interface SubscribeDomainService {

    /**
     * 区服登记
     * 
     * @param weChatMessageDTO
     * @return
     */
    String registerArea(WeChatMessageDTO weChatMessageDTO);
}
