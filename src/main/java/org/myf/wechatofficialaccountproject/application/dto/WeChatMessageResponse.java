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
}
