package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler;

import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.MessageContentHandler;
import org.myf.wechatofficialaccountproject.infrastructure.util.client.RedisClient;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil.REDIS_FESTIVAL_KEY;

/**
 * @Author: myf
 * @CreateTime: 2024-03-30  10:11
 * @Description: ReturnWordSuffixHandler
 */
@Service
public class ReturnWordSuffixHandler implements MessageContentHandler {

    @Autowired
    private RedisClient redisClient;

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        String date = LocalDate.now().toString();
        String festivalBless = WeChatUtil.getFestivalBless(date);
        if(StringUtils.isNotBlank(festivalBless)){
            //判断今天是否已经发过节日祝福
            String isSendFestival = redisClient.getValueByKey(REDIS_FESTIVAL_KEY + date+weChatMessageDTO.getFromUserName());
            if(StringUtils.isBlank(isSendFestival)){
                redisClient.addValueToRedis(REDIS_FESTIVAL_KEY + date+weChatMessageDTO.getFromUserName(),festivalBless,
                        1000 * 60 * 60 * 24L);
                return "("+festivalBless+")";
            }
        }
        return StringUtils.isNotBlank(WeChatUtil.careMessage()) ? "("+WeChatUtil.careMessage()+")" : null;
    }

    @Override
    public boolean isMatched(WeChatMessageDTO weChatMessageDTO) {
        return true;
    }

    @Override
    public boolean shouldContinue(WeChatMessageDTO weChatMessageDTO) {
        return true;
    };

    @Override
    public boolean alwaysExecute(){
        return true;
    }
}
