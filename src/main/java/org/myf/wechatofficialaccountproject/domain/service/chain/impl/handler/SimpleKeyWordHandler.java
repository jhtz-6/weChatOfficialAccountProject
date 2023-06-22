package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler;

import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.entity.WeChatMessage;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ThreadLocalHolder;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.stereotype.Service;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:14
 * @Description: 处理简单关键字相关逻辑
 */
@Service
public class SimpleKeyWordHandler implements MessageContentHandler {

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        WeChatMessage weChatMessage = WeChatMessage.builder().fromUserName(weChatMessageDTO.getFromUserName())
            .msgType(MsgTypeEnum.getMsgTypeEnumByName(weChatMessageDTO.getMsgType()))
            .picUrl(weChatMessageDTO.getPicUrl()).content(weChatMessageDTO.getContent())
            .menuList(WeChatUtil.MENU_LIST_MAP.get(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get())).build();
        String handKeyWordResult = weChatMessage.handKeyWord();
        if (StringUtils.isNotBlank(handKeyWordResult)) {
            return handKeyWordResult;
        }
        if (WeChatUtil.ACCOMPANY_MAP
            .containsKey(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get() + weChatMessageDTO.getContent())) {
            return CommonUtil.conactAccompany(WeChatUtil.ACCOMPANY_MAP
                .get(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get() + weChatMessageDTO.getContent()));
        }
        return null;
    }

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        return StringUtils.equalsAny(weChatMessageDTO.getMsgType(), MsgTypeEnum.TEXT.name, MsgTypeEnum.VOICE.name);
    }

    @Override
    public boolean shouldContinue(WeChatMessageDTO weChatMessageDTO) {
        return false;
    }
}
