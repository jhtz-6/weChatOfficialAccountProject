package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.ChatgptMessageDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.ChatgptMessageQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.ChatgptMessageMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.ChatgptMessageRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-03-16 15:41
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
                CommonUtil.copyPropertiesExceptNull(chatgptMessageDO, chatgptMessageResult);
                return chatgptMessageMapper.updateById(chatgptMessageResult);
            }
        }
        return chatgptMessageMapper.insert(chatgptMessageDO);
    }

    @Override
    public List<ChatgptMessageDO> getListByParam(ChatgptMessageQueryParam chatgptMessageQueryParam) {
        QueryWrapper<ChatgptMessageDO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(chatgptMessageQueryParam.getFromUserName())) {
            queryWrapper.eq(FROM_USER_NAME, chatgptMessageQueryParam.getFromUserName());
        }
        if (Objects.nonNull(chatgptMessageQueryParam.getId())) {
            queryWrapper.eq(ID, chatgptMessageQueryParam.getId());
        }
        queryWrapper.orderByDesc(CREATE_TIME);
        if (Objects.nonNull(chatgptMessageQueryParam.getLimitNum())) {
            queryWrapper.last("limit " + chatgptMessageQueryParam.getLimitNum());
        }
        return chatgptMessageMapper.selectList(queryWrapper);
    }
}
