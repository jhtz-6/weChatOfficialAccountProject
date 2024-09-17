package org.myf.wechatofficialaccountproject.application.dto;

import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.EventEnum;

import java.util.Date;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 15:09
 * @Description: WeChatMessageDTO
 */
@Data
public class WeChatMessageDTO {

    private String toUserName;

    private String fromUserName;

    private Date createTime;

    private String msgType;

    private String content;

    private String msgId;

    private EventEnum event;

    private String picUrl;

    private String type;

    private String mediaId;

    private String format;

    private String returnResult;

}
