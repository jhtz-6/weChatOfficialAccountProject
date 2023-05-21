package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.UserDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.WechatKeyWordsDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.UserQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.UserMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-05-19 23:29
 * @Description: UserRepositoryImpl
 */
@Service
public class UserRepositoryImpl implements UserRepository {

    @Resource
    UserMapper userMapper;

    @Override
    public UserDO selectOneByParam(UserQueryParam userQueryParam) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userQueryParam.getLoginName())) {
            queryWrapper.eq(LOGIN_NAME, userQueryParam.getLoginName());
        }
        if (StringUtils.isNotBlank(userQueryParam.getLoginPassword())) {
            queryWrapper.eq(LOGIN_PASSWORD, userQueryParam.getLoginPassword());
        }
        queryWrapper.eq(IS_VALID,
            Objects.nonNull(userQueryParam.getIsValid()) ? userQueryParam.getIsValid() : Boolean.TRUE);
        UserDO userDO = userMapper.selectOne(queryWrapper);
        return userDO;
    }

    @Override
    public List<UserDO> getListByParam(UserQueryParam userQueryParam) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userQueryParam.getLoginName())) {
            queryWrapper.eq(LOGIN_NAME, userQueryParam.getLoginName());
        }
        if (StringUtils.isNotBlank(userQueryParam.getLoginPassword())) {
            queryWrapper.eq(LOGIN_PASSWORD, userQueryParam.getLoginPassword());
        }
        queryWrapper.eq(IS_VALID,
            Objects.nonNull(userQueryParam.getIsValid()) ? userQueryParam.getIsValid() : Boolean.TRUE);
        return userMapper.selectList(queryWrapper);
    }
}
