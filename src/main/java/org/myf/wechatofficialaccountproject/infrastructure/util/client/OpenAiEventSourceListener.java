package org.myf.wechatofficialaccountproject.infrastructure.util.client;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.ChatgptMessageDomainService;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.OpenAiResponse;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ApplicationContextUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.OpenAiUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: myf
 * @CreateTime: 2023-03-15 17:26
 * @Description: OpenAiEventSourceListener
 */
public class OpenAiEventSourceListener extends EventSourceListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAiEventSourceListener.class);

    static final ThreadLocal<List<OpenAiResponse>> OPENAI_RESPONSE_LIST_THREADLOCAL = new ThreadLocal<>();
    static final RedisClient REDIS_CILENT = (RedisClient)ApplicationContextUtil.getBeanByName("redisClient");
    static final ChatgptMessageDomainService chatgptMessageDomainService =
        (ChatgptMessageDomainService)ApplicationContextUtil.getBeanByName("chatgptMessageDomainServiceImpl");

    CountDownLatch countDownLatch;
    WeChatMessageDTO weChatMessageDTO;

    public OpenAiEventSourceListener(CountDownLatch countDownLatch, WeChatMessageDTO weChatMessageDTO) {
        this.countDownLatch = countDownLatch;
        this.weChatMessageDTO = weChatMessageDTO;
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        // 数据处理开始
        REDIS_CILENT.addValueToRedis(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName(),
            BooleanEnum.FALSE.value, null);
        OPENAI_RESPONSE_LIST_THREADLOCAL.set(Lists.newArrayList());
        LOGGER.info("OpenAI建立sse连接...");
    }

    @Override
    public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
        try {
            if (("[DONE]").equals(data)) {
                LOGGER.info("OpenAI返回数据结束了");
            } else {
                OpenAiResponse openAiResponse = JSON.parseObject(data, OpenAiResponse.class);
                List<OpenAiResponse> openAiResponseList = OPENAI_RESPONSE_LIST_THREADLOCAL.get();
                openAiResponseList.add(openAiResponse);
                LOGGER.info(data);
            }
        } catch (Exception e) {
            if (StringUtils.isNotBlank(
                REDIS_CILENT.getValueByKey(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName()))) {
                REDIS_CILENT.deleteValueByKey(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName());
            }
            LOGGER.error("onEvent.data {}", data, e);
        }
    }

    @Override
    public void onClosed(EventSource eventSource) {
        try {

            String openAiText = getOpenAiText();
            LOGGER.info("openAiText:" + openAiText);
            handlerResult(openAiText);
        } catch (Exception e) {
            if (StringUtils
                .isBlank(REDIS_CILENT.getValueByKey(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName()))) {
                REDIS_CILENT.addValueToRedis(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName(),
                    "该条数据chatgpt处理异常,请重试或尝试其它数据", null);
            }
            LOGGER.error("onClosed.eventSource {}", eventSource, e);
        }
    }

    @Override
    public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        handlerResult(response.message());
        LOGGER.error("response {},t {}", response, t);
    }


    private void handlerResult(String openAiText){
        // 数据落库
        chatgptMessageDomainService.handleByOpenAiResult(weChatMessageDTO, openAiText);
        if (Objects.nonNull(countDownLatch)) {
            // 数据放到redis
            REDIS_CILENT.addValueToRedis(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName(), openAiText,
                    null);
            if (1 == countDownLatch.getCount()) {
                countDownLatch.countDown();
            }
            REDIS_CILENT.deleteValueByKey(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName());
        }
    }

    protected static String getOpenAiText() {
        StringBuilder openAiText = new StringBuilder();
        for (OpenAiResponse openAiResponse : OPENAI_RESPONSE_LIST_THREADLOCAL.get()) {
            openAiText.append(BuildResultTextByRes(openAiResponse));
        }
        return String.valueOf(openAiText);
    }

    protected static String BuildResultTextByRes(OpenAiResponse openAiResponse) {
        String model = openAiResponse.getModel();
        switch (model) {
            case "gpt-3.5-turbo-0301":
            case "GPT_3_5_TURBO":
                return OpenAiUtil.BuildResultTextByGPT3_5(openAiResponse);
            default:
                return "";
        }
    }
}
