package org.myf.wechatofficialaccountproject.domain.service.chain.impl;

import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.OpenAiClient;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.RedisCilent;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ApplicationContextUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;

import java.util.Objects;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:17
 * @Description: 使用chatgpt处理用户消息相关逻辑
 */
public class OpenAiHandler implements MessageContentHandler {

    static RedisCilent redisCilent;
    static OpenAiClient openAiClient;

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        if (Objects.isNull(redisCilent)) {
            redisCilent = (RedisCilent)ApplicationContextUtil.getBeanByName("redisCilent");
        }
        if (Objects.isNull(openAiClient)) {
            openAiClient = (OpenAiClient)ApplicationContextUtil.getBeanByName("openAiClient");
        }
        // 从redis中取
        String redisOpenAiValue =
            redisCilent.getValueByKey(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName());
        if (WeChatUtil.CHATGPT.equals(weChatMessageDTO.getContent())) {
            if (StringUtils.isEmpty(redisOpenAiValue)) {
                return "您尚未发送chatgpt相关请求;请参考示例: chatgpt帮我写一份情书、chatgpt以我爱打游戏写一首打油诗";
            } else {
                redisCilent.deleteValueByKey(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName());
            }
            return "以下数据来自chatgpt:\n" + redisOpenAiValue;
        } else if (weChatMessageDTO.getContent().contains(WeChatUtil.CHATGPT)) {
            String redisProcessValue =
                redisCilent.getValueByKey(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName());
            if (BooleanEnum.FALSE.value.equals(redisProcessValue)) {
                return "数据较多,正在处理中,请于一两分钟后发送chatgpt来获取结果;注意:在您获取当前结果前,您不可以再次请求chatgpt。";
            }
            if (StringUtils.isNotBlank(redisOpenAiValue)) {
                return "您有chatgpt结果尚未接收,请发送chatgpt来接收。";
            }
            return openAiClient.getResultByOpenAi(weChatMessageDTO);
        }
        return null;
    }

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        if (StringUtils.equalsAny(weChatMessageDTO.getMsgType(), MsgTypeEnum.TEXT.name)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinue(WeChatMessageDTO weChatMessageDTO) {
        return false;
    }
}
