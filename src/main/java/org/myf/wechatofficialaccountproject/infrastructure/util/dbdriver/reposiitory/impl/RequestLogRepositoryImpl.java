package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.RequestLogDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.RequestLogMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.RequestLogRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: myf
 * @CreateTime: 2023-05-19 21:46
 * @Description: RequestLogRepositoryImpl
 */
@Service
public class RequestLogRepositoryImpl implements RequestLogRepository {

    @Resource
    RequestLogMapper requestLogMapper;

    @Override
    public int saveRequestLog(RequestLogDO requestLogDO) {
        return requestLogMapper.insert(requestLogDO);
    }
}
