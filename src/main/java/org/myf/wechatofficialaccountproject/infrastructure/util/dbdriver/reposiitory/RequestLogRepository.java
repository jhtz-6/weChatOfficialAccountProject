package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.RequestLogDO;

/**
 * @author myf
 */
public interface RequestLogRepository {


    /**
     * 保存数据
     *
     * @param requestLogDO
     * @return
     */
    int saveRequestLog(RequestLogDO requestLogDO);
}
