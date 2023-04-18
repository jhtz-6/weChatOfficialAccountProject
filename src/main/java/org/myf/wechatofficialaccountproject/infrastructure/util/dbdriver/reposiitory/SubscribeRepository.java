package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.SubscribeDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.SubscribeQueryParam;

/**
 * @author myf
 */
public interface SubscribeRepository {

    /**
     * selectOneByParam
     * 
     * @param subscribeQueryParam
     * @return
     */
    SubscribeDO selectOneByParam(SubscribeQueryParam subscribeQueryParam);

    /**
     * 保存或者更新SubscribeDO
     * 
     * @param subscribeDO
     */
    void saveOrUpdateId(SubscribeDO subscribeDO);

    /**
     * selectCountByArea
     * 
     * @param area
     * @return
     */
    Integer selectCountByArea(String area);

    /**
     * selectCountByParam
     * @param subscribeQueryParam
     * @return
     */
    Integer selectCountByParam(SubscribeQueryParam subscribeQueryParam);
}
