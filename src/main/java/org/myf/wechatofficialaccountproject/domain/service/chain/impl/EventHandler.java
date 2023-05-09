package org.myf.wechatofficialaccountproject.domain.service.chain.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.SubscribeDO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.EventEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity.SubscribeQueryParam;
import org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.reposiitory.SubscribeRepository;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ApplicationContextUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:18
 * @Description: 订阅/取消订阅事件处理逻辑
 */
public class EventHandler implements MessageContentHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);

    static SubscribeRepository subscribeRepositoryImpl;
    private static final ReentrantLock lock = new ReentrantLock();

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        if (Objects.isNull(subscribeRepositoryImpl)) {
            subscribeRepositoryImpl =
                (SubscribeRepository)ApplicationContextUtil.getBeanByName("subscribeRepositoryImpl");
        }
        SubscribeQueryParam subscribeQueryParam = new SubscribeQueryParam();
        subscribeQueryParam.setSubscriber(weChatMessageDTO.getFromUserName());
        lock.lock();
        try {
            SubscribeDO subscribeDO = subscribeRepositoryImpl.selectOneByParam(subscribeQueryParam);
            // 幂等处理
            if (Objects.nonNull(subscribeDO) && weChatMessageDTO.getEvent().name.equals(subscribeDO.getStatus())) {
                return WeChatUtil.SUBSCRIBE_CONTENT;
            }
            // 说明是关注/取消关注
            if (EventEnum.SUBSCRIBE.name.equals(weChatMessageDTO.getEvent().name)) {
                subscribeQueryParam = new SubscribeQueryParam();
                subscribeQueryParam.setStatus(weChatMessageDTO.getEvent().name);
                Integer count = subscribeRepositoryImpl.selectCountByParam(subscribeQueryParam);
                String unSubsceibeWord = "";
                subscribeQueryParam.setStatus(EventEnum.UNSUBSCRIBE.name);
                if (Objects.isNull(subscribeDO)) {
                    subscribeDO = new SubscribeDO();
                    subscribeDO.setStatus(weChatMessageDTO.getEvent().name);
                    subscribeDO.setSubscriber(weChatMessageDTO.getFromUserName());
                    subscribeRepositoryImpl.saveOrUpdateId(subscribeDO);
                } else {
                    unSubsceibeWord = "谢谢你取关之后又来关注我,";
                    subscribeDO.setStatus(weChatMessageDTO.getEvent().name);
                    subscribeRepositoryImpl.saveOrUpdateId(subscribeDO);
                }
                return "你好," + unSubsceibeWord + "你是第" + (count + 1) + "位关注我的人,欢迎你~。\n" + WeChatUtil.SUBSCRIBE_CONTENT;
            } else if (EventEnum.UNSUBSCRIBE.name.equals(weChatMessageDTO.getEvent().name)) {
                subscribeDO.setStatus(weChatMessageDTO.getEvent().name);
                subscribeRepositoryImpl.saveOrUpdateId(subscribeDO);
                return "取关成功";
            }
        } catch (Exception e) {
            LOGGER.error("EventHandler.handlerMessageContent.weChatMessageDTO:{}", JSON.toJSONString(weChatMessageDTO),
                e);
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        return StringUtils.equalsAny(weChatMessageDTO.getEvent().name, EventEnum.UNSUBSCRIBE.name,
            EventEnum.SUBSCRIBE.name);
    }

    @Override
    public boolean shouldContinue(WeChatMessageDTO weChatMessageDTO) {
        return false;
    }
}
