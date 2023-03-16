package org.myf.wechatofficialaccountproject.domain.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.ChatgptMessageDomainService;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.ChatgptMessageDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.ChatgptMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-03-16 15:48
 * @Description: ChatgptMessageDomainServiceImpl
 */
@Service
public class ChatgptMessageDomainServiceImpl implements ChatgptMessageDomainService {

    @Autowired
    ChatgptMessageRepository chatgptMessageRepository;

    @Override
    public void handleByOpenAiResult(WeChatMessageDTO weChatMessageDTO, String openAiText) {
        if (Objects.isNull(weChatMessageDTO) || StringUtils.isEmpty(openAiText)) {
            return;
        }
        ChatgptMessageDO chatgptMessageDO = new ChatgptMessageDO();
        chatgptMessageDO.setSendMessage(weChatMessageDTO.getContent());
        chatgptMessageDO.setReceiveMessage(openAiText);
        chatgptMessageDO.setFromUserName(weChatMessageDTO.getFromUserName());
        chatgptMessageRepository.saveOrUpdateById(chatgptMessageDO);
    }
}
