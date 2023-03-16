package org.myf.wechatofficialaccountproject.infrastructure.util.client;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import lombok.Data;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.ChatgptMessageDomainService;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ApplicationContextUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: myf
 * @CreateTime: 2023-03-15 17:26
 * @Description: OpenAiEventSourceListener
 */
@Component
@Scope("prototype")
public class OpenAiEventSourceListener extends EventSourceListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleEventSourceListener.class);

    static List<OpenAiResponse> openAiResponseList = Collections.synchronizedList(Lists.newArrayList());

    static String openAiText;

    CountDownLatch countDownLatch;
    WeChatMessageDTO weChatMessageDTO;

    RedisCilent redisCilent = (RedisCilent)ApplicationContextUtil.getBeanByName("redisCilent");
    ChatgptMessageDomainService chatgptMessageDomainService =
        (ChatgptMessageDomainService)ApplicationContextUtil.getBeanByName("chatgptMessageDomainServiceImpl");

    public OpenAiEventSourceListener(CountDownLatch countDownLatch, WeChatMessageDTO weChatMessageDTO) {
        this.countDownLatch = countDownLatch;
        this.weChatMessageDTO = weChatMessageDTO;
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        // 数据处理开始
        redisCilent.addValueToRedis(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName(),
            BooleanEnum.FALSE.value, null);
        LOGGER.info("weweweOpenAI建立sse连接...");
    }

    @Override
    public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
        try {
            if (("[DONE]").equals(data)) {
                LOGGER.info("OpenAI返回数据结束了");
            } else {
                OpenAiResponse openAiResponse = JSON.parseObject(data, OpenAiResponse.class);
                openAiResponseList.add(openAiResponse);
                LOGGER.info(data);
            }
        } catch (Exception e) {
            LOGGER.error("onEvent.e {},data {}", e, data);
        }
    }

    @Override
    public void onClosed(EventSource eventSource) {
        try {
            for (OpenAiResponse openAiResponse : openAiResponseList) {
                for (ChoiceDTO choiceDTO : openAiResponse.getChoices()) {
                    choiceDTO.setResultText(CommonUtil.unicodeToUtf8(choiceDTO.getText()));
                }
            }
            openAiText = null;
            openAiText = getOpenAiText();
            LOGGER.info("openAiText:" + openAiText);
            // 数据落库
            chatgptMessageDomainService.handleByOpenAiResult(weChatMessageDTO, openAiText);
            if (Objects.nonNull(countDownLatch)) {
                if (1 == countDownLatch.getCount()) {
                    countDownLatch.countDown();
                    redisCilent.deleteValueByKey(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName());
                } else {
                    // 说明超过了四秒,数据放到redis
                    redisCilent.addValueToRedis(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName(),
                        openAiText, null);
                    // 数据处理完毕
                    redisCilent.addValueToRedis(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName(),
                        BooleanEnum.TRUE.value, null);
                }
            }
            openAiResponseList = Collections.synchronizedList(Lists.newArrayList());
        } catch (Exception e) {
            LOGGER.error("onClosed.e {},eventSource {}", e, eventSource);
        }
    }

    @Override
    public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        LOGGER.error("response {},t {}",response,t);
    }

    public static String getOpenAiText() {
        if (StringUtils.isNotBlank(openAiText)) {
            return openAiText;
        }
        StringBuilder openAiText = new StringBuilder();
        for (OpenAiResponse openAiResponse : openAiResponseList) {
            for (ChoiceDTO choiceDTO : openAiResponse.getChoices()) {
                if (StringUtils.isNotBlank(choiceDTO.getResultText())) {
                    openAiText.append(choiceDTO.getResultText());
                }
            }
        }
        return String.valueOf(openAiText);
    }
}
