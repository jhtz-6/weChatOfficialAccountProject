package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.RecommendMenuDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.RecommendMenuQueryParam;

import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2023-03-06 22:03
 * @Description: RecommendMenuRepository
 */
public interface RecommendMenuRepository extends BaseRepository{

    /**
     * selectListByParam
     * 
     * @param recommendMenuQueryParam
     * @return
     */
    List<RecommendMenuDO> selectListByParam(RecommendMenuQueryParam recommendMenuQueryParam);

    /**
     * 保存或更新数据
     * @param recommendMenuDO
     */
    void saveOrUpdateByID(RecommendMenuDO recommendMenuDO);
}
