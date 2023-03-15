package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.WeChatMessageDO;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 22:53
 * @Description: WeChatRepository
 */
public interface WeChatRepository {

    /**
     * 保存或更新weChatMessageDO到表
     * 
     * @param weChatMessageDO
     */
    void saveOrUpdateById(WeChatMessageDO weChatMessageDO);
}
