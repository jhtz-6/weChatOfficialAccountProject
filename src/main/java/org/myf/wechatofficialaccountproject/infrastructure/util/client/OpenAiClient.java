package org.myf.wechatofficialaccountproject.infrastructure.util.client;

import com.alibaba.fastjson.JSON;
import com.unfbx.chatgpt.entity.completions.Completion;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.InitData;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: myf
 * @CreateTime: 2023-03-15 16:28
 * @Description: OpenAiClient
 */
@Component
public class OpenAiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAiClient.class);

    private static final String TIMEOUT_WORD = "数据较多,正在处理中,请于一两分钟后发送chatgpt来获取结果;注意:在您获取当前结果前,您不可以再次请求chatgpt。";
    private static final String ERROR_WORD = "数据处理异常，请稍后再试";
    @Autowired
    RedisClient redisClient;
    @Autowired
    TencentCosClient tencentCosClient;
    static ExecutorService openAiThreadPoolExecutor;
    static {
        AtomicInteger openAiThreadNumber = new AtomicInteger(1);
        openAiThreadPoolExecutor =
            new ThreadPoolExecutor(3, 10, 30, TimeUnit.SECONDS, new LinkedBlockingDeque<>(20), task -> {
                Thread thread = new Thread(task);
                thread.setName("openAiThread-" + openAiThreadNumber.incrementAndGet() + "-" + thread.getName());
                return thread;
            });
    }

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
        try {
            Future<String> future = openAiThreadPoolExecutor.submit(new OpenAiTaskClient(weChatMessageDTO));
            redisClient.addValueToRedis(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName(),
                BooleanEnum.FALSE.value, null);
            CommonUtil.addValueToChatgptNumMap(weChatMessageDTO.getFromUserName());
            return Objects.isNull(unit) ? getResultByFuture(weChatMessageDTO, future)
                : getResultByTimeFuture(weChatMessageDTO, future, timeout, unit);
        } catch (TimeoutException timeoutException) {
            return TIMEOUT_WORD;
        } catch (InterruptedException e) {
            LOGGER.error("getResultByOpenAi.e.weChatMessageDTO {}", JSON.toJSONString(weChatMessageDTO), e);
        } catch (Exception exception) {
            LOGGER.error("getResultByOpenAi.exception.weChatMessageDTO {}", JSON.toJSONString(weChatMessageDTO),
                exception);
        }
        return ERROR_WORD;
    }

    private String getResultByFuture(WeChatMessageDTO weChatMessageDTO, Future<String> future)
        throws ExecutionException, InterruptedException {
        redisClient.deleteValueByKey(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName());
        return future.get();
    }

    private String getResultByTimeFuture(WeChatMessageDTO weChatMessageDTO, Future<String> future, long timeout,
        TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        String result = future.get(timeout, unit);
        redisClient.deleteValueByKey(WeChatUtil.CHATGPT_PROCESS + "-" + weChatMessageDTO.getFromUserName());
        redisClient.deleteValueByKey(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName());
        return result;
    }

}
