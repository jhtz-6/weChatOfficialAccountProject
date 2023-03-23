package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.ChatgptMessageDO;

/**
 * @author myf
 */
public interface ChatgptMessageRepository {

    /**
     * 保存或更新数据
     * @param chatgptMessageDO
     * @return
     */
    int saveOrUpdateById(ChatgptMessageDO chatgptMessageDO);
}
