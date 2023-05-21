package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.AccompanyDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.AccompanyQueryParam;

import java.util.List;

/**
 * @author myf
 */
public interface AccompanyRepository extends BaseRepository{

    /**
     * 根据param获取list数据
     *
     * @param accompanyQueryParam
     * @return
     */
    List<AccompanyDO> getListByParam(AccompanyQueryParam accompanyQueryParam);

    /**
     * 保存或更新数据
     * 
     * @param accompanyDO
     * @return
     */
    int saveOrUpdateById(AccompanyDO accompanyDO);
}
