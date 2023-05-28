package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.MapUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: myf
 * @CreateTime: 2023-05-28 09:44
 * @Description: ResponseResult
 */
public class ResponseResult {

    private ResponseResult() {
    }

    public static String ErrorOfMapToJson(Object errorMessage) {
        Map<String, Object> errorMap = new HashMap(4);
        errorMap.put("result", false);
        errorMap.put("errorMessage", errorMessage);
        return JSON.toJSONString(errorMap);
    }

    public static String TrueOfMapToJson(Map paramMap) {
        if (MapUtils.isNotEmpty(paramMap)) {
            paramMap.put("result", true);
        }
        return JSON.toJSONString(paramMap);
    }

    public static String TrueOfMapToJson() {
        return JSON.toJSONString(Collections.singletonMap("result", true));
    }
}
