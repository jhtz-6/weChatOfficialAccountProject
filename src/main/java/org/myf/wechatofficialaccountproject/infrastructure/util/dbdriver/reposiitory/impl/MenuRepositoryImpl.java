package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.MenuDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.MenuQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.MenuMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.MenuRepository;
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
    private final static String ID = "id";
    private final static String FOOD = "food";

    @Override
    public List<MenuDO> selectListByParam(MenuQueryParam menuQueryParam) {
        QueryWrapper<MenuDO> menuDOWrapper = new QueryWrapper<>();
        if (Objects.nonNull(menuQueryParam.getId())) {
            menuDOWrapper.eq(ID, menuQueryParam.getId());
        }
        if (StringUtils.isNotBlank(menuQueryParam.getFood())) {
            menuDOWrapper.eq(FOOD, menuQueryParam.getFood());
        }
        return menuMapper.selectList(menuDOWrapper);
    }
}
