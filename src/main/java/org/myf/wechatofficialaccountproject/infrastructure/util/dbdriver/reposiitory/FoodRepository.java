package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.FoodDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.FoodQueryParam;

import java.util.List;

/**
 * @author myf
 */
public interface FoodRepository extends BaseRepository{

    /**
     * selectListByParam
     * 
     * @param foodQueryParam
     * @return
     */
    List<FoodDO> selectListByParam(FoodQueryParam foodQueryParam);

    /**
     * 保存或更新数据
     *
     * @param foodDO
     * @return
     */
    int saveOrUpdateById(FoodDO foodDO);
}
