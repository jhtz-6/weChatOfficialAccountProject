package org.myf.wechatofficialaccountproject.domain.service;

import org.myf.wechatofficialaccountproject.application.dto.WeChatMessageDTO;
import org.myf.wechatofficialaccountproject.infrastructure.base.entity.WeChatMessageDO;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 00:00
 * @Description: WeChatDomainService接口
 */
public interface WeChatDomainService {
    /**
     * weChatMessageDTO转为weChatMessageDO
     * 
     * @param t
     * @return
     */
    <T> WeChatMessageDO convertDtoToDo(T t);

    /**
     * 保存weChatMessageDO到表
     * 
     * @param weChatMessageDO
     */
    void saveWeChatMessageDO(WeChatMessageDO weChatMessageDO);

    /**
     * 解析处理weChatMessageDTO消息并返回结果
     * 
     * @param weChatMessageDTO
     * @return
     */
    String handleWeChatMessageDTO(WeChatMessageDTO weChatMessageDTO);

    /**
     * 处理用户文字消息相关逻辑
     * 
     * @param weChatMessageDTO
     * @return
     */
    String handleText(WeChatMessageDTO weChatMessageDTO);

    /**
     * 调用百度ocr进行图片处理的逻辑
     * 
     * @param weChatMessageDTO
     * @return
     */
    String handleImageByBaiduOcr(WeChatMessageDTO weChatMessageDTO);

    /**
     * 获取图灵机器人处理结果
     * 
     * @param message
     * @return
     */
    String handleByTuLing(String message);

    /**
     * 获取当前在线人数
     * 
     * @param setKey
     * @param value
     * @param timeOut
     * @return
     */
    String getCurrentPersonNum(String setKey, String value, Long timeOut);

    /**
     * 获取openai处理结果
     * 
     * @param weChatMessageDTO
     * @return
     */
    String handleByOpenAi(WeChatMessageDTO weChatMessageDTO);
}
