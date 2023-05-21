package org.myf.wechatofficialaccountproject.infrastructure.util.helper.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.google.common.collect.Lists;
import org.myf.wechatofficialaccountproject.application.dto.WechatKeyWordsDTO;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.WechatKeyWordsDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.WechatKeyWordsRepository;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2023-05-21 09:17
 * @Description: KeyTypeImport
 */
public class KeyTypeImportListener implements ReadListener<WechatKeyWordsDTO> {

    WechatKeyWordsRepository wechatKeyWordsRepository;
    SystemBelongEnum belonger;

    public KeyTypeImportListener(WechatKeyWordsRepository wechatKeyWordsRepository, SystemBelongEnum belonger) {
        this.wechatKeyWordsRepository = wechatKeyWordsRepository;
        this.belonger = belonger;
    }

    private List<WechatKeyWordsDO> wechatKeyWordsDTOList = Lists.newArrayList();

    @Override
    public void invoke(WechatKeyWordsDTO wechatKeyWordsDTO, AnalysisContext context) {
        WechatKeyWordsDO wechatKeyWordsDO = new WechatKeyWordsDO();
        BeanUtils.copyProperties(wechatKeyWordsDTO, wechatKeyWordsDO);
        wechatKeyWordsDO.setBelonger(belonger);
        wechatKeyWordsDO.setIsValid(BooleanEnum.TRUE);
        wechatKeyWordsRepository.saveOrUpdateById(wechatKeyWordsDO);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //wechatKeyWordsRepository.saveByIds(wechatKeyWordsDTOList);
    }

}
