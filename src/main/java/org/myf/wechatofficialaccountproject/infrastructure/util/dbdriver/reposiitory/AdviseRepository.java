package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.AdviseDO;

/**
 * @Author: myf
 * @CreateTime: 2023-03-08 22:01
 * @Description: AdviseRepository
 */
public interface AdviseRepository {

    int saveOrUpdateById(AdviseDO adviseDO);
}
