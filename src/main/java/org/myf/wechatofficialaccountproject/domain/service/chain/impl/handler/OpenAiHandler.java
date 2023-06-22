package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler;

import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.OpenAiClient;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.RedisClient;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler.OpenAi.*;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:17
 * @Description: 使用chatgpt处理用户消息相关逻辑
 */
@Service
public class OpenAiHandler implements MessageContentHandler {
    @Autowired
    RedisClient redisClient;
    @Autowired
    OpenAiClient openAiClient;

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        // 从redis中取
        String redisOpenAiValue =
            redisClient.getValueByKey(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName());
        String redisProcessValue =
            redisClient.getValueByKey(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName());
        if (StringUtils.equalsAny(weChatMessageDTO.getContent(), WeChatUtil.CHATGPT, WeChatUtil.CHATGPT_ONE)) {
            if (StringUtils.isEmpty(redisOpenAiValue) && BooleanEnum.FALSE.value.equals(redisProcessValue)) {
                return IN_PROCESS;
            }
            if (StringUtils.isEmpty(redisOpenAiValue)) {
                return DEFAULT_OPENAI_RESULT;
            } else {
                boolean firstGet = weChatMessageDTO.getContent().equals(WeChatUtil.CHATGPT);
                if (redisOpenAiValue.length() > 550) {
                    redisOpenAiValue =
                        redisOpenAiValue.substring(firstGet ? 0 : 550, firstGet ? 550 : redisOpenAiValue.length());
                    if (!firstGet) {
                        redisClient.deleteValueByKey(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName());
                        redisClient
                            .deleteValueByKey(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName());
                        CommonUtil.addValueToChatgptNumMap(weChatMessageDTO.getFromUserName());
                    } else {
                        return NEED_MORE_REQUEST + redisOpenAiValue;
                    }
                } else {
                    redisClient.deleteValueByKey(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName());
                    redisClient.deleteValueByKey(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName());
                    CommonUtil.addValueToChatgptNumMap(weChatMessageDTO.getFromUserName());
                }
                return RESULT + redisOpenAiValue;
            }
        } else if (weChatMessageDTO.getContent().startsWith(WeChatUtil.CHATGPT)) {
            if (Objects.nonNull(WeChatUtil.CHATGPT_NUM_MAP.get(weChatMessageDTO.getFromUserName()))
                && WeChatUtil.CHATGPT_NUM_MAP.get(weChatMessageDTO.getFromUserName()) >= WeChatUtil.CHATGPT_NUM) {
                return "您今日请求chatgpt次数(" + WeChatUtil.CHATGPT_NUM + "次)已使用完,请于明天再来!!";
            }
            if (StringUtils.isNotBlank(redisOpenAiValue)) {
                return NEED_TO_GET_RESULT;
            }
            if (BooleanEnum.FALSE.value.equals(redisProcessValue)) {
                return IN_PROCESS;
            }
            return RESULT + openAiClient.getResultByOpenAi(weChatMessageDTO, 4000, TimeUnit.MILLISECONDS);
        }
        return null;
    }

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        return StringUtils.equalsAny(weChatMessageDTO.getMsgType(), MsgTypeEnum.TEXT.name);
    }

    @Override
    public boolean shouldContinue(WeChatMessageDTO weChatMessageDTO) {
        return false;
    }
}
