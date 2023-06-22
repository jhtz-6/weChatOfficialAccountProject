package org.myf.wechatofficialaccountproject.infrastructure.util.client;

import com.alibaba.fastjson.JSON;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.images.ImageResponse;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.ChatgptMessageDomainService;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ApplicationContextUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.InitData;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @Author: myf
 * @CreateTime: 2023-06-22 17:49
 * @Description: OpenAITaskClient
 */
public class OpenAiTaskClient implements Callable<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAiTaskClient.class);

    static final RedisClient REDIS_CILENT = (RedisClient)ApplicationContextUtil.getBeanByName("redisClient");
    static final ChatgptMessageDomainService chatgptMessageDomainService =
        (ChatgptMessageDomainService)ApplicationContextUtil.getBeanByName("chatgptMessageDomainServiceImpl");
    static final TencentCosClient TENCENT_COS_CLIENT =
        (TencentCosClient)ApplicationContextUtil.getBeanByName("tencentCosClient");
    private static final String STANDARD_MESSAGE_PREFIX = "图片";
    private static final String ERROR_IMAGEURL = "图片生成失败";
    private static final String ERROR_WORD = "chatgpt返回数据处理异常,请稍后再试。";

    private WeChatMessageDTO weChatMessageDTO;

    public OpenAiTaskClient(WeChatMessageDTO weChatMessageDTO) {
        this.weChatMessageDTO = weChatMessageDTO;
    }

    @Override
    public String call() throws Exception {
        String standardMessage = weChatMessageDTO.getContent().substring(7);
        if (standardMessage.startsWith(STANDARD_MESSAGE_PREFIX)) {
            ImageResponse imageResponse = InitData.UNFBX_OPENAI_CLIENT.genImages(standardMessage.substring(2));
            LOGGER.info(JSON.toJSONString(imageResponse));
            String imageUrl = ERROR_IMAGEURL;
            try {
                imageUrl = TENCENT_COS_CLIENT.uploadByImageUrl(imageResponse.getData().get(0).getUrl());
            } catch (Exception e) {
                LOGGER.error("OpenAITaskClient.imageResponse.error:{}", JSON.toJSONString(imageResponse), e);
            }
            handlerResult(imageUrl);
            return imageUrl;
        }
        List<String> chatgptMessageList =
            REDIS_CILENT.addStrValueToLimitedList(WeChatUtil.CHATGPT_LIST + weChatMessageDTO.getFromUserName(),
                standardMessage, WeChatUtil.CHATGPT_LIST_SIZE);
        ChatCompletion chatCompletion = ChatCompletion
            .builder().model(ChatCompletion.Model.GPT_3_5_TURBO.getName()).messages(chatgptMessageList.stream()
                .map(x -> Message.builder().role(Message.Role.USER).content(x).build()).collect(Collectors.toList()))
            .build();
        ChatCompletionResponse chatCompletionResponse = InitData.UNFBX_OPENAI_CLIENT.chatCompletion(chatCompletion);
        LOGGER.info(JSON.toJSONString(chatCompletionResponse));
        String content = ERROR_WORD;
        try {
            content = chatCompletionResponse.getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            LOGGER.error("OpenAITaskClient.content:{}", JSON.toJSONString(chatCompletionResponse), e);
        }
        handlerResult(content);
        return content;
    }

    private void handlerResult(String openAiText) {
        // 数据落库
        REDIS_CILENT.addValueToRedis(WeChatUtil.CHATGPT + "-" + weChatMessageDTO.getFromUserName(), openAiText, null);
        chatgptMessageDomainService.handleByOpenAiResult(weChatMessageDTO, openAiText);
    }
}
