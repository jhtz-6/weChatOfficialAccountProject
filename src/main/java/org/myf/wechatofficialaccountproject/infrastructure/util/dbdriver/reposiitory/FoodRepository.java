package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.FoodDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.FoodQueryParam;

import java.util.List;

/**
 * @author myf
 */
public interface FoodRepository {

    /**
     * selectListByParam
     * 
     * @param foodQueryParam
     * @return
     */
    List<FoodDO> selectListByParam(FoodQueryParam foodQueryParam);
}
