package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.MaterialDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.MaterialQueryParam;

import java.util.List;

/**
 * @author myf
 */
public interface MaterialRepository {

    /**
     * selectListByParam
     * 
     * @param materialQueryParam
     * @return
     */
    List<MaterialDO> selectListByParam(MaterialQueryParam materialQueryParam);
}
