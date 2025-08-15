package org.myf.wechatofficialaccountproject.infrastructure.util.client;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.json.JSONObject;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.BaiduOcrResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static org.myf.wechatofficialaccountproject.infrastructure.util.helper.InitData.CLIENT;

/**
 * @Author: myf
 * @CreateTime: 2023-03-06 10:20
 * @Description: 调用百度ocr客户端
 */
@Component
public class BaiduOcrClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaiduOcrClient.class);

    /**
     * 不包含位置 https://cloud.baidu.com/doc/OCR/s/Sk3h7xyad
     * @param photoUrl
     * @return
     */
    public BaiduOcrResponse getWebImageByPhotoUrl(String photoUrl) {
        buildOcrClientParams();
        JSONObject res = CLIENT.webImageUrl(photoUrl, Maps.newHashMap());
        LOGGER.info("webImageUrl.photoUrl:{},res:{}", photoUrl, res);
        return getOcrResult(res);
    }

    /**
     * 包含位置信息 https://cloud.baidu.com/doc/OCR/s/vk3h7y58v
     * @param photoUrl
     * @return
     */
    public BaiduOcrResponse getGeneralImageByPhotoUrl(String photoUrl) {
        HashMap<String, String> options = Maps.newHashMap();
        options.put("recognize_granularity", "big");
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "true");
        options.put("detect_language", "true");
        options.put("vertexes_location", "true");
        options.put("probability", "true");
        buildOcrClientParams();
        JSONObject res = CLIENT.generalUrl(photoUrl, options);
        return getOcrResult(res);
    }

    private void buildOcrClientParams() {
        CLIENT.setConnectionTimeoutInMillis(2000);
        CLIENT.setSocketTimeoutInMillis(30000);
    }

    private BaiduOcrResponse getOcrResult(JSONObject res) {
        BaiduOcrResponse baiduOcrResponse = null;
        try {
            baiduOcrResponse = JSON.parseObject(res.toString(), BaiduOcrResponse.class);
        } catch (Exception e) {
            LOGGER.error("getOcrResult.res {}", JSON.toJSONString(res), e);
        }
        return baiduOcrResponse;
    }
}
