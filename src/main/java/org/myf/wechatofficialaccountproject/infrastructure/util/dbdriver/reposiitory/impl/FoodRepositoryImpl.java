package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.FoodDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.FoodQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.FoodMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.FoodRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 23:01
 * @Description: FoodRepositoryImpl
 */
@Service
public class FoodRepositoryImpl implements FoodRepository {

    @Resource
    FoodMapper foodMapper;

    @Override
    public List<FoodDO> selectListByParam(FoodQueryParam foodQueryParam) {
        QueryWrapper<FoodDO> foodDOQueryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(foodQueryParam.getId())) {
            foodDOQueryWrapper.eq(ID, foodQueryParam.getId());
        }
        if (StringUtils.isNotBlank(foodQueryParam.getFoodName())) {
            foodDOQueryWrapper.eq(FOOD_NAME, foodQueryParam.getFoodName());
        }
        if (StringUtils.isNotBlank(foodQueryParam.getSfyx())) {
            foodDOQueryWrapper.eq(SFYX, foodQueryParam.getSfyx());
        }
        return foodMapper.selectList(foodDOQueryWrapper);
    }

    @Override
    public int saveOrUpdateById(FoodDO foodDO) {
        if (Objects.isNull(foodDO)) {
            return 0;
        }
        FoodDO preFoodDO = foodMapper.selectById(foodDO.getId());
        if (Objects.nonNull(preFoodDO)) {
            CommonUtil.copyPropertiesExceptNull(foodDO, preFoodDO);
            return foodMapper.updateById(preFoodDO);
        } else {
            return foodMapper.insert(foodDO);
        }
    }
}
