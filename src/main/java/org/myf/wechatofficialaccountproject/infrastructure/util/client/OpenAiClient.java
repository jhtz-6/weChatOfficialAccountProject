package org.myf.wechatofficialaccountproject.infrastructure.util.client;

import com.alibaba.fastjson.JSON;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.completions.Completion;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.InitData;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: myf
 * @CreateTime: 2023-03-15 16:28
 * @Description: OpenAiClient
 */
@Component
public class OpenAiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAiClient.class);

    private static final String TIMEOUT_WORD = "数据较多,正在处理中,请于一两分钟后发送chatgpt来获取结果;注意:在您获取当前结果前,您不可以再次请求chatgpt。";
    @Autowired
    RedisClient redisClient;

    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        WeChatMessageDTO weChatMessageDTO = new WeChatMessageDTO();
        weChatMessageDTO.setContent("chatgpthello");
        OpenAiEventSourceListener eventSourceListener = new OpenAiEventSourceListener(countDownLatch, weChatMessageDTO);
        Completion q = Completion.builder().prompt("hello").stream(true).build();
        InitData.OPENAI_STREAM_CLIENT.streamCompletions(q, eventSourceListener);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getResultByOpenAi(WeChatMessageDTO weChatMessageDTO, long timeout, TimeUnit unit) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        invokeOpenAiListener(countDownLatch, weChatMessageDTO);
        try {
            if (Objects.nonNull(unit)) {
                // 超时返回false
                if (!countDownLatch.await(timeout, unit)) {
                    countDownLatch.countDown();
                    return TIMEOUT_WORD;
                }
            } else {
                countDownLatch.await();
            }
            String redisResultByOpenAi =
                redisClient.getValueByKey(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName());
            redisClient.deleteValueByKey(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName());
            CommonUtil.addValueToChatgptNumMap(weChatMessageDTO.getFromUserName());
            return redisResultByOpenAi;
        } catch (InterruptedException e) {
            LOGGER.error("getResultByOpenAi.weChatMessageDTO {}", JSON.toJSONString(weChatMessageDTO), e);
        } catch (Exception exception) {
            LOGGER.error("getResultByOpenAi.exception.weChatMessageDTO {}", JSON.toJSONString(weChatMessageDTO),
                exception);
        }
        return null;
    }

    private void invokeOpenAiListener(CountDownLatch countDownLatch, WeChatMessageDTO weChatMessageDTO) {
        OpenAiEventSourceListener eventSourceListener = new OpenAiEventSourceListener(countDownLatch, weChatMessageDTO);
        List<String> chatgptMessageList =
            redisClient.addStrValueToLimitedList(WeChatUtil.CHATGPT_LIST + weChatMessageDTO.getFromUserName(),
                weChatMessageDTO.getContent().substring(7), WeChatUtil.CHATGPT_LIST_SIZE);
        ChatCompletion chatCompletion = ChatCompletion
            .builder().model(ChatCompletion.Model.GPT_3_5_TURBO.getName()).messages(chatgptMessageList.stream()
                .map(x -> Message.builder().role(Message.Role.USER).content(x).build()).collect(Collectors.toList()))
            .build();
        InitData.OPENAI_STREAM_CLIENT.streamChatCompletion(chatCompletion, eventSourceListener);
    }

}
