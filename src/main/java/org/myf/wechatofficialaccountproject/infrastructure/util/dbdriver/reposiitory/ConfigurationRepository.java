package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.ConfigurationDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.ConfigurationQueryParam;

import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2023-03-13 14:29
 * @Description: ConfigurationRepository
 */
public interface ConfigurationRepository {

    /**
     *
     * @param name
     * @return
     */
    ConfigurationDO selectValueByName(String name);

    List<ConfigurationDO> selectListByParam(ConfigurationQueryParam configurationQueryParam);

    /**
     * 保存或更新数据
     *
     * @param configurationDO
     * @return
     */
    int saveOrUpdateById(ConfigurationDO configurationDO);
}
