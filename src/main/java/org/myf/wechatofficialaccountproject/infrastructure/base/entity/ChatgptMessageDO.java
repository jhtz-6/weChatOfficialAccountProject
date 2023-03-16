package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-16 15:34
 * @Description: ChatgptMessageDO
 */
@TableName("chatgpt_message")
@Data
public class ChatgptMessageDO extends BaseDO {

    @TableField(value = "from_user_name")
    private String fromUserName;

    @TableField(value = "send_message")
    private String sendMessage;

    @TableField(value = "receive_message")
    private String receiveMessage;

}
