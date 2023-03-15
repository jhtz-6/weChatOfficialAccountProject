package org.myf.wechatofficialaccountproject.infrastructure.util.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.*;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.TuLingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: myf
 * @CreateTime: 2023-03-09 20:25
 * @Description: TuLingClient
 */
@Component
public class TuLingClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TuLingClient.class);

    private static final String TULING_URL = "http://openapi.turingapi.com/openapi/api/v2";

    public TuLingResponse getResultByTuling(String message) {
        String result = "";
        for (String appKey : TuLingUtil.TULING_REBOT_NAME_MAP.keySet()) {
            TuLingRequest tuLingRequest = buildTuLingReq(message, appKey);
            try {
                result = CommonUtil.getResultByPostJson(TULING_URL, JSON.toJSONString(tuLingRequest));
                TuLingResponse tuLingResponse = JSONObject.parseObject(result, TuLingResponse.class);
                tuLingResponse.setRebotName(TuLingUtil.TULING_REBOT_NAME_MAP.get(appKey));
                return tuLingResponse;
            } catch (Exception e) {
                LOGGER.error("getResultByTuling.e {},result {},message {}", e, result, JSON.toJSONString(message));
            }
        }
        return null;
    }

    private static TuLingRequest buildTuLingReq(String message, String appKey) {
        TuLingRequest tuLingRequest = new TuLingRequest();
        // 目前仅支持0
        tuLingRequest.setReqType(0);
        TuLingPerception tuLingPerception = new TuLingPerception();
        TuLingInputText tuLingInputText = new TuLingInputText();
        tuLingInputText.setText(message);
        tuLingPerception.setInputText(tuLingInputText);
        tuLingRequest.setPerception(tuLingPerception);
        tuLingRequest.setUserInfo(new TuLingUserInfo(appKey, appKey));
        return tuLingRequest;
    }
}
