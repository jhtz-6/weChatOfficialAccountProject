package org.myf.wechatofficialaccountproject.infrastructure.util.client;

import com.alibaba.fastjson.JSON;
import com.unfbx.chatgpt.entity.completions.Completion;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.application.service.impl.WeChatApplicationServiceImpl;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.InitData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author: myf
 * @CreateTime: 2023-03-15 16:28
 * @Description: OpenAiClient
 */
@Component
public class OpenAiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAiClient.class);

    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        WeChatMessageDTO weChatMessageDTO =new WeChatMessageDTO();
        weChatMessageDTO.setContent("chatgpthello");
        OpenAiEventSourceListener eventSourceListener = new OpenAiEventSourceListener(countDownLatch, weChatMessageDTO);
        Completion q = Completion.builder().prompt("hello").stream(true).build();
        InitData.OPENAI_STREAM_CLIENT.streamCompletions(q, eventSourceListener);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(OpenAiEventSourceListener.getOpenAiText());
    }

    public String getResultByOpenAi(WeChatMessageDTO weChatMessageDTO) {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        OpenAiEventSourceListener eventSourceListener = new OpenAiEventSourceListener(countDownLatch, weChatMessageDTO);
        Completion completion =
            Completion.builder().prompt(weChatMessageDTO.getContent().substring(7)).stream(true).build();
        InitData.OPENAI_STREAM_CLIENT.streamCompletions(completion, eventSourceListener);
        try {
            // 超时返回false
            boolean awaitResult = countDownLatch.await(4000, TimeUnit.MILLISECONDS);
            if (awaitResult) {
                return "以下数据来自chatgpt:\n" + OpenAiEventSourceListener.getOpenAiText();
            } else {
                countDownLatch.countDown();
                return "数据较多,正在处理中,请于一两分钟后发送chatgpt来获取结果;注意:在您获取当前结果前,您不可以再次请求chatgpt。";
            }
        } catch (InterruptedException e) {
            LOGGER.error("getResultByOpenAi.e {},weChatMessageDTO {}",e,JSON.toJSONString(weChatMessageDTO));
        }
        return null;
    }

}
