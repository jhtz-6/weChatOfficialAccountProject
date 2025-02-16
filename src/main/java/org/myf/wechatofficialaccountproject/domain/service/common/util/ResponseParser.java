package org.myf.wechatofficialaccountproject.domain.service.common.util;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.myf.wechatofficialaccountproject.domain.service.common.GameDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseParser {
    public static String getResult(String response) {
        GameDTO gameDTO = new GameDTO();
        try {
            // 去除字符串中的回车符和换行符
            response = response.replaceAll("[\r\n]", "");
            // 创建 ObjectMapper 实例用于解析 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            // 解析最外层的 JSON 数据
            JsonNode rootNode = objectMapper.readTree(response);
            // 获取 data 字段的值
            String data = rootNode.get("data").asText();
            //2978658 9768092
            // 使用正则表达式提取 JavaScript 变量的值部分
            Pattern pattern = Pattern.compile("var query_role_result=(.*?);");
            Matcher matcher = pattern.matcher(data);
            if (matcher.find()) {
                String jsonStr = matcher.group(1);
                // 将单引号替换为双引号以符合 JSON 格式
                jsonStr = jsonStr.replace("'", "\"");
                // 解析 data 字段中的键值对
                String[] keyValuePairs = JSONUtil.parseObj(jsonStr).getStr("data").split("&");
                for (String pair : keyValuePairs) {
                    String[] parts = pair.split("=");
                    if (parts.length == 2) {
                        String key = parts[0];
                        String value = parts[1];
                        value = java.net.URLDecoder.decode(value, String.valueOf(StandardCharsets.UTF_8));
                        if ("charac_name".equals(key)) {
                            //角色名称
                            gameDTO.setCharacName(value);
                        }else if ("level".equals(key)) {
                            //等级
                            gameDTO.setLevel(value);
                        }else if("hafcoinnum".equals(key)) {
                            //哈夫币
                            gameDTO.setHafcoinnum(value);
                        }else if("propcapital".equals(key)) {
                            //道具价值
                            gameDTO.setPropcapital(value);
                        }else if("islogined".equals(key)) {
                            //在线状态 0 离线  1 在线
                            gameDTO.setIslogined(value);
                        }else if("logintoday".equals(key)) {
                            //今日登录 logintoday 1是 0 否
                            gameDTO.setLogintoday(value);
                        }else if("lastlogintime".equals(key)) {
                            //最后登录 lastlogintime
                            gameDTO.setLastlogintime(value);
                        }else if("lastlogouttime".equals(key)) {
                            //最后登出 lastlogouttime
                            gameDTO.setLastlogouttime(value);
                        }else if("isbanuser".equals(key)) {
                            //封号状态 isbanuser
                            gameDTO.setIsbanuser(value);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gameDTO.toString();
    }


}