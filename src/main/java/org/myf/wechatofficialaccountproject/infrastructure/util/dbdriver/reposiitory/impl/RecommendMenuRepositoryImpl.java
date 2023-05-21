package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.RecommendMenuDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.RecommendMenuQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.RecommendMenumapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.RecommendMenuRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-03-06 22:04
 * @Description: RecommendMenuRepositoryImpl
 */
@Service
public class RecommendMenuRepositoryImpl implements RecommendMenuRepository {

    @Resource
    RecommendMenumapper recommendMenumapper;

    @Override
    public List<RecommendMenuDO> selectListByParam(RecommendMenuQueryParam recommendMenuQueryParam) {
        QueryWrapper<RecommendMenuDO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(recommendMenuQueryParam.getFromUserName())) {
            queryWrapper.eq(FROM_USER_NAME, recommendMenuQueryParam.getFromUserName());
        }
        if (Objects.nonNull(recommendMenuQueryParam.getId())) {
            queryWrapper.eq(ID, recommendMenuQueryParam.getId());
        }
        return recommendMenumapper.selectList(queryWrapper);
    }

    @Override
    public void saveOrUpdateByID(RecommendMenuDO recommendMenuDO) {
        if (Objects.isNull(recommendMenuDO)) {
            return;
        }
        RecommendMenuDO preRecommendDO = recommendMenumapper.selectById(recommendMenuDO.getId());
        if (Objects.nonNull(preRecommendDO)) {
            CommonUtil.copyPropertiesExceptNull(recommendMenuDO, preRecommendDO);
            recommendMenumapper.updateById(preRecommendDO);
        } else {
            recommendMenumapper.insert(recommendMenuDO);
        }
    }
}
