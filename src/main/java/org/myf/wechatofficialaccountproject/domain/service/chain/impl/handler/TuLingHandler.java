package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler;

import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.TuLingClient;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.TuLingResponse;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.TuLingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler.TuLing.ERROR_RES;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:18
 * @Description: 使用图灵机器人处理用户消息相关逻辑
 */
@Service
public class TuLingHandler implements MessageContentHandler {
    @Autowired
    TuLingClient tuLingClient;

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        TuLingResponse tuLingResponse = tuLingClient.getResultByTuling(weChatMessageDTO.getContent());
        if (Objects.isNull(tuLingResponse)) {
            return ERROR_RES;
        } else if (TuLingUtil.TULING_ERROR_MAP.containsKey(tuLingResponse.getIntent().getCode())) {
            return TuLingUtil.TULING_ERROR_MAP.get(tuLingResponse.getIntent().getCode());
        } else if (StringUtils.isNotBlank(tuLingResponse.getResults().get(0).getValues().getText())) {
            return tuLingResponse.getResults().get(0).getValues().getText() + "\n(来自机器人:"
                + tuLingResponse.getRebotName() + ")";
        }
        return "看来我还需要继续学习,小的看不懂大人输入的:[" + weChatMessageDTO.getContent() + "]是什么意思啊~~\n"
            + "我有很多功能哦,大人可以发送【小屋写随笔】来知悉哦~~";
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
