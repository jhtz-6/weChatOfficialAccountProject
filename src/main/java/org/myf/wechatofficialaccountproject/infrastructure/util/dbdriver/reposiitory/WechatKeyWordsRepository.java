package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.WechatKeyWordsDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.WechatKeyWordQueryParam;

import java.util.List;

/**
 * @author myf
 */
public interface WechatKeyWordsRepository extends BaseRepository{

    /**
     * 根据param获取list数据
     * 
     * @param wechatKeyWordQueryParam
     * @return
     */
    List<WechatKeyWordsDO> getListByParam(WechatKeyWordQueryParam wechatKeyWordQueryParam);

    /**
     * 保存或更新数据
     * @param wechatKeyWordsDO
     * @return
     */
    int saveOrUpdateById(WechatKeyWordsDO wechatKeyWordsDO);

    /**
     * 批量保存或更新数据
     * @param wechatKeyWordsDOList
     * @return
     */
    int saveByIds(List<WechatKeyWordsDO> wechatKeyWordsDOList);
}
