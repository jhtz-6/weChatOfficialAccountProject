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
    @Autowired
    RedisCilent redisCilent;

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

    public String getResultByOpenAi(WeChatMessageDTO weChatMessageDTO) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        OpenAiEventSourceListener eventSourceListener = new OpenAiEventSourceListener(countDownLatch, weChatMessageDTO);
        List<String> chatgptMessageList =
            redisCilent.addStrValueToLimitedList(WeChatUtil.CHATGPT_LIST + weChatMessageDTO.getFromUserName(),
                weChatMessageDTO.getContent(), WeChatUtil.CHATGPT_LIST_SIZE);
        ChatCompletion chatCompletion = ChatCompletion
            .builder().model(ChatCompletion.Model.GPT_3_5_TURBO.getName()).messages(chatgptMessageList.stream()
                .map(x -> Message.builder().role(Message.Role.USER).content(x).build()).collect(Collectors.toList()))
            .build();
        InitData.OPENAI_STREAM_CLIENT.streamChatCompletion(chatCompletion, eventSourceListener);
        try {
            // 超时返回false
            boolean awaitResult = countDownLatch.await(4000, TimeUnit.MILLISECONDS);
            if (awaitResult) {
                String redisResultByOpenAi =
                    redisCilent.getValueByKey(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName());
                redisCilent.deleteValueByKey(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName());
                CommonUtil.addValueToChatgptNumMap(weChatMessageDTO.getFromUserName());
                return "以下数据来自chatgpt:\n" + redisResultByOpenAi;
            } else {
                countDownLatch.countDown();
                return "数据较多,正在处理中,请于一两分钟后发送chatgpt来获取结果;注意:在您获取当前结果前,您不可以再次请求chatgpt。";
            }
        } catch (InterruptedException e) {
            LOGGER.error("getResultByOpenAi.weChatMessageDTO {}", JSON.toJSONString(weChatMessageDTO), e);
        }
        return null;
    }

}
