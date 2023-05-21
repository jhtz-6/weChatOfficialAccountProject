package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.MenuDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.MenuQueryParam;

import java.util.List;

/**
 * @author myf
 */
public interface MenuRepository extends BaseRepository{

    /**
     * selectListByParam
     * 
     * @param menuQueryParam
     * @return
     */
    List<MenuDO> selectListByParam(MenuQueryParam menuQueryParam);

    /**
     * 保存或更新数据
     *
     * @param menuDO
     * @return
     */
    int saveOrUpdateById(MenuDO menuDO);
}
