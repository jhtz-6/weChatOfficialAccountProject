package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.AccompanyDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.UserDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.AccompanyQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.UserQueryParam;

import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2023-05-19 23:27
 * @Description: UserRepository
 */
public interface UserRepository extends BaseRepository{

    /**
     * selectOneByParam
     *
     * @param userQueryParam
     * @return
     */
    UserDO selectOneByParam(UserQueryParam userQueryParam);

    /**
     * 根据param获取list数据
     *
     * @param userQueryParam
     * @return
     */
    List<UserDO> getListByParam(UserQueryParam userQueryParam);
}
