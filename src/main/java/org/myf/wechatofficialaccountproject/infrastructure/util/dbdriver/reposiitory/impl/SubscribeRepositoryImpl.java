package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.SubscribeDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.SubscribeQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.SubscribeMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.SubscribeRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-03-06 21:24
 * @Description: SubscribeRepositoryimpl
 */
@Service
public class SubscribeRepositoryImpl implements SubscribeRepository {

    @Resource
    SubscribeMapper subscribeMapper;



    @Override
    public SubscribeDO selectOneByParam(SubscribeQueryParam subscribeQueryParam) {
        QueryWrapper<SubscribeDO> subscribeDOQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(subscribeQueryParam.getSubscriber())) {
            subscribeDOQueryWrapper.eq(SUBSCRIBER, subscribeQueryParam.getSubscriber());
        }
        if (StringUtils.isNotBlank(subscribeQueryParam.getArea())) {
            subscribeDOQueryWrapper.eq(AREA, subscribeQueryParam.getArea());
        }
        if (StringUtils.isNotBlank(subscribeQueryParam.getStatus())) {
            subscribeDOQueryWrapper.eq(STATUS, subscribeQueryParam.getStatus());
        }
        if (Objects.nonNull(subscribeQueryParam.getId())) {
            subscribeDOQueryWrapper.eq(ID, subscribeQueryParam.getId());
        }
        if (Objects.nonNull(subscribeQueryParam.getBelonger())) {
            subscribeDOQueryWrapper.eq(BELONGER, subscribeQueryParam.getBelonger());
        }
        return subscribeMapper.selectOne(subscribeDOQueryWrapper);
    }

    @Override
    public void saveOrUpdateId(SubscribeDO subscribeDO) {
        if (Objects.isNull(subscribeDO)) {
            return;
        }
        SubscribeDO preSubscribeDO = subscribeMapper.selectById(subscribeDO.getId());
        if (Objects.nonNull(preSubscribeDO)) {
            CommonUtil.copyPropertiesExceptNull(subscribeDO, preSubscribeDO);
            subscribeMapper.updateById(preSubscribeDO);
        } else {
            subscribeMapper.insert(subscribeDO);
        }
    }

    @Override
    public Integer selectCountByArea(String area) {
        if (StringUtils.isBlank(area)) {
            return null;
        }
        QueryWrapper<SubscribeDO> subscribeDOQueryWrapper = new QueryWrapper<>();
        subscribeDOQueryWrapper.eq(AREA, area);
        return Math.toIntExact(subscribeMapper.selectCount(subscribeDOQueryWrapper));
    }

    @Override
    public Integer selectCountByParam(SubscribeQueryParam subscribeQueryParam) {
        QueryWrapper<SubscribeDO> subscribeDOQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(subscribeQueryParam.getSubscriber())) {
            subscribeDOQueryWrapper.eq(SUBSCRIBER, subscribeQueryParam.getSubscriber());
        }
        if (StringUtils.isNotBlank(subscribeQueryParam.getStatus())) {
            subscribeDOQueryWrapper.eq(STATUS, subscribeQueryParam.getStatus());
        }
        if (Objects.nonNull(subscribeQueryParam.getId())) {
            subscribeDOQueryWrapper.eq(ID, subscribeQueryParam.getId());
        }
        return Math.toIntExact(subscribeMapper.selectCount(subscribeDOQueryWrapper));
    }
}
