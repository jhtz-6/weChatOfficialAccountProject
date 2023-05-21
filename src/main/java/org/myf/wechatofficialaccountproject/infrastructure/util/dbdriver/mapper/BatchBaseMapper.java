package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface BatchBaseMapper<T> extends BaseMapper<T> {


    /**
     * 批量插入
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(List<T> entityList);

}
