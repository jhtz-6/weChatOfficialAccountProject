package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.AdviseDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.AdviseMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.AdviseRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-03-08 22:02
 * @Description: AdviseRepositoryImpl
 */
@Service
public class AdviseRepositoryImpl implements AdviseRepository {

    @Resource
    AdviseMapper adviseMapper;

    @Override
    public int saveOrUpdateById(AdviseDO adviseDO) {
        if (Objects.nonNull(adviseDO.getId())) {
            AdviseDO adviseDOResult = adviseMapper.selectById(adviseDO.getId());
            if (Objects.nonNull(adviseDOResult)) {
                CommonUtil.copyPropertiesExceptNull(adviseDO, adviseDOResult);
                return adviseMapper.updateById(adviseDOResult);
            }
        }
        return adviseMapper.insert(adviseDO);
    }
}
