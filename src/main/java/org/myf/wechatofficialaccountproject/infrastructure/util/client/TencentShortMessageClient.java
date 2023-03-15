package org.myf.wechatofficialaccountproject.infrastructure.util.client;

import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.myf.wechatofficialaccountproject.infrastructure.util.helper.InitData.SSENDER;
import static org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil.CONFIGURATION_MAP;

/**
 * @Author: myf
 * @CreateTime: 2023-03-13 13:16
 * @Description: TencentShortMessageClient
 */
@Component
public class TencentShortMessageClient {

    public SmsSingleSenderResult sendShortMessagebyParams(String[] params, Integer templateId)
        throws HTTPException, IOException {
        // 签名参数未提供或者为空时，会使用默认签名发送短信
        return SSENDER.sendWithParam("86", CONFIGURATION_MAP.get(WeChatUtil.TENCENT_PHONE), templateId, params,
            CONFIGURATION_MAP.get(WeChatUtil.TENCENT_SIGN), "", "");
    }

}
