package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.WechatKeyWordsDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.WechatKeyWordQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.WechatKeyWordsMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.WechatKeyWordsRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18 15:59
 * @Description: WechatKeyWordsRepositoryImpl
 */
@Service
public class WechatKeyWordsRepositoryImpl implements WechatKeyWordsRepository {

    @Resource
    private WechatKeyWordsMapper wechatKeyWordsMapper;

    @Override
    public List<WechatKeyWordsDO> getListByParam(WechatKeyWordQueryParam wechatKeyWordQueryParam) {
        QueryWrapper<WechatKeyWordsDO> wechatKeyWordsDoQueryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(wechatKeyWordQueryParam.getId())) {
            wechatKeyWordsDoQueryWrapper.eq(ID, wechatKeyWordQueryParam.getId());
        }
        if (Objects.nonNull(wechatKeyWordQueryParam.getBelonger())) {
            wechatKeyWordsDoQueryWrapper.eq(BELONGER, wechatKeyWordQueryParam.getBelonger());
        }
        if (Objects.nonNull(wechatKeyWordQueryParam.getKeyType())) {
            wechatKeyWordsDoQueryWrapper.eq(KEY_TYPE, wechatKeyWordQueryParam.getKeyType().name());
        }
        if (Objects.nonNull(wechatKeyWordQueryParam.getKeyName())) {
            wechatKeyWordsDoQueryWrapper.apply("BINARY key_name = {0}", wechatKeyWordQueryParam.getKeyName());
        }
        if (StringUtils.isNotBlank(wechatKeyWordQueryParam.getValueContent())) {
            wechatKeyWordsDoQueryWrapper.eq(VALUE_CONTENT, wechatKeyWordQueryParam.getValueContent());
        }
        wechatKeyWordsDoQueryWrapper.eq(IS_VALID, Objects.nonNull(wechatKeyWordQueryParam.getIsValid())
            ? wechatKeyWordQueryParam.getIsValid() : BooleanEnum.TRUE.name);
        return wechatKeyWordsMapper.selectList(wechatKeyWordsDoQueryWrapper);
    }

    @Override
    public int saveOrUpdateById(WechatKeyWordsDO keyWordsDO) {
        if (Objects.isNull(keyWordsDO)) {
            return 0;
        }
        WechatKeyWordsDO preWechatKeyWordsDO = null;
        if (Objects.nonNull(keyWordsDO.getId())) {
            preWechatKeyWordsDO = wechatKeyWordsMapper.selectById(keyWordsDO.getId());
        } else if (StringUtils.isNotBlank(keyWordsDO.getKeyName()) && Objects.nonNull(keyWordsDO.getBelonger())
        && Objects.nonNull(keyWordsDO.getKeyType())) {
            QueryWrapper<WechatKeyWordsDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(BELONGER, keyWordsDO.getBelonger());
            queryWrapper.apply("BINARY key_name = {0}", keyWordsDO.getKeyName());
            queryWrapper.eq(IS_VALID,
                    Objects.nonNull(keyWordsDO.getIsValid()) ? keyWordsDO.getIsValid() : BooleanEnum.TRUE.name);
            //queryWrapper.eq(KEY_TYPE, keyWordsDO.getKeyType().name());
            preWechatKeyWordsDO = wechatKeyWordsMapper.selectOne(queryWrapper);
            //如果content为空,则 删除对应的数据
            if(StrUtil.isEmpty(keyWordsDO.getValueContent())) {
                return wechatKeyWordsMapper.deleteById(preWechatKeyWordsDO);
            }
        }
        if (Objects.nonNull(preWechatKeyWordsDO)) {
            CommonUtil.copyPropertiesExceptNull(keyWordsDO, preWechatKeyWordsDO);
            return wechatKeyWordsMapper.updateById(preWechatKeyWordsDO);
        } else {
            return wechatKeyWordsMapper.insert(keyWordsDO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveByIds(List<WechatKeyWordsDO> wechatKeyWordsDOList) {
        return wechatKeyWordsMapper.insertBatchSomeColumn(wechatKeyWordsDOList);
    }

}
