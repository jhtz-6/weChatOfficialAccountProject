package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler.pdx;

import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.entity.WeChatMessage;
import org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler.SimpleKeyWordHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ThreadLocalHolder;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.stereotype.Service;

@Service
public class PdxSimpleKeyWordHandler extends SimpleKeyWordHandler {


    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        WeChatMessage weChatMessage = WeChatMessage.builder().fromUserName(weChatMessageDTO.getFromUserName())
                .msgType(MsgTypeEnum.getMsgTypeEnumByName(weChatMessageDTO.getMsgType()))
                .picUrl(weChatMessageDTO.getPicUrl()).content(weChatMessageDTO.getContent()).build();
        String handKeyWordResult = weChatMessage.handKeyWord();
        if (StringUtils.isNotBlank(handKeyWordResult)) {
            return handKeyWordResult;
        }
        return WeChatUtil.WeChatKeyWordMap.get(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get() + "error");
    }

}
