package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.ChatgptMessageDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.ChatgptMessageQueryParam;

import java.util.List;

/**
 * @author myf
 */
public interface ChatgptMessageRepository extends BaseRepository{

    /**
     * 保存或更新数据
     * @param chatgptMessageDO
     * @return
     */
    int saveOrUpdateById(ChatgptMessageDO chatgptMessageDO);

    /**
     * 根据param获取list数据
     *
     * @param chatgptMessageQueryParam
     * @return
     */
    List<ChatgptMessageDO> getListByParam(ChatgptMessageQueryParam chatgptMessageQueryParam);
}
