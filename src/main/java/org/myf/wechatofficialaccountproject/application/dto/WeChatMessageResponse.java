package org.myf.wechatofficialaccountproject.application.dto;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-14 11:39
 * @Description: WeChatMessageResponse
 */
@Data
public class WeChatMessageResponse {

    private String toUserName;

    private String fromUserName;

    private String createTime;

    private String msgType;

    private String content;

    private String type;

    private String msgId;

    private String picUrl;

    private String mediaId;

    private String format;

    /**
     * 图文消息个数；当用户发送文本、图片、语音、视频、图文、地理位置这六种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条
     */
    private Integer articleCount = 1;
}
