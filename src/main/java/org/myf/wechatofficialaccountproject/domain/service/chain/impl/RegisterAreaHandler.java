package org.myf.wechatofficialaccountproject.domain.service.chain.impl;

import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.SubscribeDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.SubscribeQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.SubscribeRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ApplicationContextUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;

import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:11
 * @Description: 处理区服登记相关处理器
 */

public class RegisterAreaHandler implements MessageContentHandler {

    static SubscribeRepository subscribeRepository;

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        if (Objects.isNull(subscribeRepository)) {
            subscribeRepository = (SubscribeRepository)ApplicationContextUtil.getBeanByName("subscribeRepositoryImpl");
        }
        SubscribeQueryParam subscribeQueryParam = new SubscribeQueryParam();
        subscribeQueryParam.setSubscriber(weChatMessageDTO.getFromUserName());
        SubscribeDO subscribeDO = subscribeRepository.selectOneByParam(subscribeQueryParam);
        if (Objects.nonNull(subscribeDO) && StringUtils.isEmpty(subscribeDO.getArea())) {
            subscribeDO.setArea("已发送");
            subscribeRepository.saveOrUpdateId(subscribeDO);
            return "(请大人登记下所在区服(多个区服之间用;隔开),示例:【区服:倾国倾城;有凤来仪】)";
        }
        if (StringUtils.isNotBlank(weChatMessageDTO.getContent()) && weChatMessageDTO.getContent().contains("区服")) {
            subscribeDO.setSubscriber(weChatMessageDTO.getFromUserName());
            subscribeDO.setArea(weChatMessageDTO.getContent());
            subscribeRepository.saveOrUpdateId(subscribeDO);
            String area = WeChatUtil.AREA_LIST.stream().filter(x -> weChatMessageDTO.getContent().contains(x))
                .findFirst().orElse(null);
            if (StringUtils.isNotBlank(area)) {
                Integer areaCount = subscribeRepository.selectCountByArea(area);
                return "大人您的区:【" + weChatMessageDTO.getContent() + "】,已成功登记;目前【" + area + "】共登记" + areaCount + "人";
            } else {
                return "大人您的区:【" + weChatMessageDTO.getContent() + "】,已成功登记;";
            }
        } else if (WeChatUtil.AREA_LIST.contains(weChatMessageDTO.getContent())) {
            Integer areaCount = subscribeRepository.selectCountByArea(weChatMessageDTO.getContent());
            return "查询到目前" + "【" + weChatMessageDTO.getContent() + "】共登记" + areaCount + "人;登记区服请参考:【区服:倾国倾城;有凤来仪】";
        }
        return null;
    }

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        // 区服登记暂时去掉
        return false;
    }

    @Override
    public boolean shouldContinue(WeChatMessageDTO weChatMessageDTO) {
        return false;
    }
}
