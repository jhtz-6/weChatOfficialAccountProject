package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.RecommendMenuDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.RecommendMenuQueryParam;

import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2023-03-06 22:03
 * @Description: RecommendMenuRepository
 */
public interface RecommendMenuRepository {

    /**
     * selectListByParam
     * 
     * @param recommendMenuQueryParam
     * @return
     */
    List<RecommendMenuDO> selectListByParam(RecommendMenuQueryParam recommendMenuQueryParam);

    void saveOrUpdateByID(RecommendMenuDO recommendMenuDO);
}
