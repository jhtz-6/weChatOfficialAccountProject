package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.ConfigurationDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.ConfigurationQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.ConfigurationMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.ConfigurationRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2023-03-13 14:31
 * @Description: ConfigurationRepositoryImpl
 */
@Service
public class ConfigurationRepositoryImpl implements ConfigurationRepository {

    @Resource
    ConfigurationMapper configurationMapper;

    @Override
    public ConfigurationDO selectValueByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        QueryWrapper<ConfigurationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return configurationMapper.selectOne(queryWrapper);
    }

    @Override
    public List<ConfigurationDO> selectListByParam(ConfigurationQueryParam configurationQueryParam) {
        QueryWrapper<ConfigurationDO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(configurationQueryParam.getName())) {
            queryWrapper.eq("name", configurationQueryParam.getName());
        }
        return configurationMapper.selectList(queryWrapper);
    }
}
