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

    ConfigurationDO selectValueByName(String name);

    List<ConfigurationDO> selectListByParam(ConfigurationQueryParam configurationQueryParam);
}
