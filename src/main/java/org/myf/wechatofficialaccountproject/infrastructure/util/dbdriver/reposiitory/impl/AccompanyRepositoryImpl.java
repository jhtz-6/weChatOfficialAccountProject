package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.AccompanyDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.AccompanyQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper.AccompanyMapper;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.AccompanyRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.BaseRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18 20:57
 * @Description: AccompanyRepositoryImpl
 */
@Service
public class AccompanyRepositoryImpl implements AccompanyRepository {

    @Resource
    AccompanyMapper accompanyMapper;

    @Override
    public List<AccompanyDO> getListByParam(AccompanyQueryParam accompanyQueryParam) {
        QueryWrapper<AccompanyDO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(accompanyQueryParam.getCharacterName())) {
            queryWrapper.eq(BaseRepository.CHARACTER_NAME, accompanyQueryParam.getCharacterName());
        }
        if (Objects.nonNull(accompanyQueryParam.getDepartment())) {
            queryWrapper.eq(BaseRepository.DEPARTMENT, accompanyQueryParam.getDepartment());
        }
        if (Objects.nonNull(accompanyQueryParam.getCharacterType())) {
            queryWrapper.eq(BaseRepository.CHARACTER_TYPE, accompanyQueryParam.getCharacterType());
        }
        if (Objects.nonNull(accompanyQueryParam.getIsElite())) {
            queryWrapper.eq(BaseRepository.IS_ELITE, accompanyQueryParam.getIsElite());
        }
        if(Objects.nonNull(accompanyQueryParam.getBelonger())){
            queryWrapper.eq(BaseRepository.BELONGER,accompanyQueryParam.getBelonger());
        }
        if(Objects.nonNull(accompanyQueryParam.getIsValid())){
            queryWrapper.eq(BaseRepository.IS_VALID,accompanyQueryParam.getIsValid());
        }
        return accompanyMapper.selectList(queryWrapper);
    }

    @Override
    public int saveOrUpdateById(AccompanyDO accompanyDO) {
        if (Objects.isNull(accompanyDO)) {
            return 0;
        }
        AccompanyDO preAccompanyDO = accompanyMapper.selectById(accompanyDO.getId());
        if (Objects.nonNull(preAccompanyDO)) {
            CommonUtil.copyPropertiesExceptNull(accompanyDO, preAccompanyDO);
            return accompanyMapper.updateById(preAccompanyDO);
        } else {
            return accompanyMapper.insert(accompanyDO);
        }
    }
}
