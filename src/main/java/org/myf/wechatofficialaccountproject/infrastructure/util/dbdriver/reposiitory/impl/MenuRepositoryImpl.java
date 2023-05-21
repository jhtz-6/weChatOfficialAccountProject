package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.MenuDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.MenuQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.MenuMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.MenuRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 15:47
 * @Description: MenuRepositoryImpl
 */
@Service
public class MenuRepositoryImpl implements MenuRepository {
    @Resource
    MenuMapper menuMapper;

    @Override
    public List<MenuDO> selectListByParam(MenuQueryParam menuQueryParam) {
        QueryWrapper<MenuDO> menuDOWrapper = new QueryWrapper<>();
        if (Objects.nonNull(menuQueryParam.getId())) {
            menuDOWrapper.eq(ID, menuQueryParam.getId());
        }
        if (StringUtils.isNotBlank(menuQueryParam.getFood())) {
            menuDOWrapper.eq(FOOD, menuQueryParam.getFood());
        }
        if (Objects.nonNull(menuQueryParam.getBelonger())) {
            menuDOWrapper.eq(BELONGER, menuQueryParam.getBelonger());
        }
        return menuMapper.selectList(menuDOWrapper);
    }

    @Override
    public int saveOrUpdateById(MenuDO menuDO) {
        if (Objects.isNull(menuDO)) {
            return 0;
        }
        MenuDO preMenuDO = menuMapper.selectById(menuDO.getId());
        if (Objects.nonNull(preMenuDO)) {
            CommonUtil.copyPropertiesExceptNull(menuDO, preMenuDO);
            return menuMapper.updateById(preMenuDO);
        } else {
            return menuMapper.insert(menuDO);
        }
    }
}
