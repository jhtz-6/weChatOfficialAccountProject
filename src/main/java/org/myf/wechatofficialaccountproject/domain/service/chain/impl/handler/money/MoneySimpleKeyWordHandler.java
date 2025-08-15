package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler.money;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler.SimpleKeyWordHandler;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.MsgTypeEnum;

import org.myf.wechatofficialaccountproject.infrastructure.util.helper.ThreadLocalHolder;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.stereotype.Service;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:14
 * @Description: money 处理简单关键字相关逻辑
 */
@Service
public class MoneySimpleKeyWordHandler extends SimpleKeyWordHandler {

    @Override
    public String handlerMessageContent(WeChatMessageDTO weChatMessageDTO) {
        //对于价格的文字 不做处理
        if(weChatMessageDTO.getContent().contains(".")){
            return "输入内容不正确,无法处理";
        }
        String backWord = WeChatUtil.WeChatKeyWordMap.get(
                ThreadLocalHolder.BELONGER_THREAD_LOCAL.get() + weChatMessageDTO.getContent());
        if(StrUtil.isNotBlank(backWord)){
            return backWord;
        }
        return "输入内容不正确,无法处理";
    }

}
