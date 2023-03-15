package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.EventEnum;

import java.util.Date;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 14:56
 * @Description: WeChatMessageDO
 */
@TableName("wechat_message")
@Data
public class WeChatMessageDO extends BaseDO {

    @TableField("to_user_name")
    private String toUserName;

    @TableField("from_user_name")
    private String fromUserName;

    @TableField("msg_type")
    private String msgType;

    @TableField("content")
    private String content;

    @TableField("msg_id")
    private String msgId;

    @TableField("type")
    private String type;

    @TableField("event")
    private EventEnum event;

    @TableField("pic_url")
    private String picUrl;

    @TableField("media_id")
    private String mediaId;

    @TableField("format")
    private String format;

}
