package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.domain.service.common.util.RequestParser;
import org.myf.wechatofficialaccountproject.domain.service.common.util.ResponseParser;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ThreadLocalHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class HttpRequestHandler implements MessageContentHandler {


    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        if(weChatMessageDTO.getContent().contains("access_token") && weChatMessageDTO.getContent().contains("openid")){
            String content = RequestParser.parseContent(weChatMessageDTO.getContent());
            // 创建 OkHttpClient 实例
            OkHttpClient client = new OkHttpClient();
            // 定义请求的 URL
            String url = "http://113.44.155.212:9000/query";
            // 构建请求体，这里使用 JSON 格式
            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            Map<String,String> map =new HashMap<>();
            map.put("full_input",content);
            String json = JSONUtil.toJsonStr(map);
            RequestBody body = RequestBody.create(json, JSON);
            // 构建请求
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            // 执行请求
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    // 打印响应体
                    String responseData = response.body().string();
                    log.info("HttpRequestHandler.handlerMessageContent.weChatMessageDTO:{},responseData:{}",
                            JSONUtil.toJsonStr(weChatMessageDTO),responseData);
                    return ResponseParser.getResult(responseData);
                } else {
                    System.out.println("请求失败，状态码: " + response.code());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new StringBuilder("状态:鉴权失败\n失败原因:账号数据异常\n" +"处理时间:"+ DateUtil.date(new Date())+
                        "\n解决方案:请重新获取账号数据或更新账号密码").toString();
            }
        }
        if(ThreadLocalHolder.BELONGER_THREAD_LOCAL.get().equals(SystemBelongEnum.GAME)){
            return "输入的内容不合法,查询失败";
        }
        return "";
    }

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        return true;
    }
}
