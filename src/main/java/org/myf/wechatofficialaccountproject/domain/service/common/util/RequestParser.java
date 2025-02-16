package org.myf.wechatofficialaccountproject.domain.service.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {
    public static void main(String[] args) {
        String input = "Callback({\"ret\":0,\"url\":\"auth://tauth.qq.com/?#access_token=BB3DB8244CE1BC48F65FCFE452B7A15F&expires_in=604800&openid=C422AE1FD40B5127530F6C5970B20A17&pay_token=7004506638C0B9812BAD2B7F70777733&ret=0&pf=desktop_m_qq-10000144-android-2002-&pfkey=05ae27c070976bd976a8380fcd9e6069&auth_time=1739562193313&page_type=1\"}";
        String s = parseContent(input);
        System.out.println(s);
    }

    public static String parseContent(String content) {
        if(!content.contains("{")){
            return content;
        }
        try {
            // 提取 JSON 格式的字符串部分
            String jsonStr = content.substring(content.indexOf("{"), content.lastIndexOf("}") + 1);

            // 创建 ObjectMapper 实例用于解析 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            // 解析 JSON 字符串
            JsonNode rootNode = objectMapper.readTree(jsonStr);
            // 获取 url 字段的值
            String url = rootNode.get("url").asText();

            // 提取 access_token 的值
            String accessToken = extractParameterValue(url, "access_token");
            System.out.println("access_token: " + accessToken);

            // 提取 openid 的值
            String openid = extractParameterValue(url, "openid");
            System.out.println("openid: " + openid);
            return "access_token="+accessToken+"&openid="+openid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("获取token和openid失败,content:%s",content));
    }

    /**
     * 从 URL 中提取指定参数的值
     * @param url URL 字符串
     * @param paramName 参数名
     * @return 参数的值，如果未找到则返回 null
     */
    private static String extractParameterValue(String url, String paramName) {
        String regex = paramName + "=([^&]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
