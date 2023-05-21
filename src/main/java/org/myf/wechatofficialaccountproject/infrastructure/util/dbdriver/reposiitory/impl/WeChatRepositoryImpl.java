package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import org.myf.wechatofficialaccountproject.infrastructure.base.entity.WeChatMessageDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.WeChatMessageMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.WeChatRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 22:53
 * @Description: WeChatRepositoryImpl实现类
 */
@Service
public class WeChatRepositoryImpl implements WeChatRepository {

    @Resource
    private WeChatMessageMapper weChatMessageMapper;

    @Override
    public void saveOrUpdateById(WeChatMessageDO weChatMessageDO) {
        if (Objects.isNull(weChatMessageDO)) {
            return;
        }
        WeChatMessageDO preWeChatMessageDO = weChatMessageMapper.selectById(weChatMessageDO.getId());
        if (Objects.nonNull(preWeChatMessageDO)) {
            CommonUtil.copyPropertiesExceptNull(weChatMessageDO, preWeChatMessageDO);
            weChatMessageMapper.updateById(preWeChatMessageDO);
            return;
        } else {
            weChatMessageMapper.insert(weChatMessageDO);
        }
    }
}
