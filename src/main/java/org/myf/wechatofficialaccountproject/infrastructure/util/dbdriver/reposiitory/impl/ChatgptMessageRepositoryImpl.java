package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.ChatgptMessageDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.ChatgptMessageMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.ChatgptMessageRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-03-16  15:41
 * @Description: ChatgptMessageRepositoryImpl
 */
@Service
public class ChatgptMessageRepositoryImpl implements ChatgptMessageRepository {

    @Resource
    ChatgptMessageMapper chatgptMessageMapper;

    @Override
    public int saveOrUpdateById(ChatgptMessageDO chatgptMessageDO) {
        if (Objects.nonNull(chatgptMessageDO.getId())) {
            ChatgptMessageDO chatgptMessageResult = chatgptMessageMapper.selectById(chatgptMessageDO.getId());
            if (Objects.nonNull(chatgptMessageResult)) {
                CommonUtil.copyProperties(chatgptMessageDO, chatgptMessageResult);
                return chatgptMessageMapper.updateById(chatgptMessageResult);
            }
        }
        return chatgptMessageMapper.insert(chatgptMessageDO);
    }
}
