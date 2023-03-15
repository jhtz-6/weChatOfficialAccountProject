package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: myf
 * @CreateTime: 2023-03-08 21:56
 * @Description: AdviseDO
 */
@Data
@TableName("advise")
public class AdviseDO extends BaseDO {

    @TableField("userId")
    private String userId;

    @TableField("sfyx")
    private String sfyx = "1";

    @TableField("content")
    private String content;

    /**
     * 是否回复 0收到建议 1待回复建议 2已回复建议
     */
    @TableField("is_reply")
    private String isReply;

    @TableField("reply_content")
    private String replyContent;
}
