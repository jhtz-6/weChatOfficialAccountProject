package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.MenuDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.MenuQueryParam;

import java.util.List;

/**
 * @author myf
 */
public interface MenuRepository {

    /**
     * selectListByParam
     * 
     * @param menuQueryParam
     * @return
     */
    List<MenuDO> selectListByParam(MenuQueryParam menuQueryParam);
}
