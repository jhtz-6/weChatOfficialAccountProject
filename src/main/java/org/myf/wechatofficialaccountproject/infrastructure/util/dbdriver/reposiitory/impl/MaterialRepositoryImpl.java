package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.MaterialDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.MaterialQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.MaterialMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.MaterialRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 17:40
 * @Description: MaterialRepositoryImpl实现类
 */
@Service
public class MaterialRepositoryImpl implements MaterialRepository {

    @Resource
    MaterialMapper materialMapper;
    private final static String ID = "id";

    @Override
    public List<MaterialDO> selectListByParam(MaterialQueryParam materialQueryParam) {
        QueryWrapper<MaterialDO> materialDOQueryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(materialQueryParam.getId())) {
            materialDOQueryWrapper.eq(ID, materialQueryParam.getId());
        }
        return materialMapper.selectList(materialDOQueryWrapper);
    }
}
