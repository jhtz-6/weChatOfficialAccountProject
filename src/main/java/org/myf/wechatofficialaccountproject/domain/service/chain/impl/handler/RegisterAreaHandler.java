package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler;

import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.SubscribeDO;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.SubscribeQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.SubscribeRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler.RegisterArea.REGISTER_TIP;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:11
 * @Description: 处理区服登记相关处理器
 */
@Service
public class RegisterAreaHandler implements MessageContentHandler {
    @Autowired
    SubscribeRepository subscribeRepository;

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        SubscribeQueryParam subscribeQueryParam = new SubscribeQueryParam();
        subscribeQueryParam.setSubscriber(weChatMessageDTO.getFromUserName());
        SubscribeDO subscribeDO = subscribeRepository.selectOneByParam(subscribeQueryParam);
        if (Objects.nonNull(subscribeDO) && StringUtils.isEmpty(subscribeDO.getArea())) {
            subscribeDO.setArea("已发送");
            subscribeRepository.saveOrUpdateId(subscribeDO);
            return REGISTER_TIP;
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
