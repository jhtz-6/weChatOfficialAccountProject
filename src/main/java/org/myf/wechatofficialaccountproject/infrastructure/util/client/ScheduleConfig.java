package org.myf.wechatofficialaccountproject.infrastructure.util.client;

import com.alibaba.fastjson.JSON;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author: myf
 * @CreateTime: 2023-05-08 23:11
 * @Description: ScheduleConfig
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleConfig.class);

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetChatgptNumMap() {
        for (String key : WeChatUtil.CHATGPT_NUM_MAP.keySet()) {
            WeChatUtil.CHATGPT_NUM_MAP.put(key, 0);
        }
        LOGGER.info("ScheduleConfig.resetChatgptNumMap执行完毕:" + JSON.toJSONString(WeChatUtil.CHATGPT_NUM_MAP));
    }
}
