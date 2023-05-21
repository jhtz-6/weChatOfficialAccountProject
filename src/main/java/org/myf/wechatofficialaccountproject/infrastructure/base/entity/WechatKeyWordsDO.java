package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.KeyTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18  15:51
 * @Description: WechatKeyWords
 */
@TableName("wechat_key_words")
@Data
public class WechatKeyWordsDO extends BaseDO{

    @TableField("key_name")
    private String keyName;

    @TableField("value_content")
    private String valueContent;

    @TableField("key_type")
    private KeyTypeEnum keyType;

    @TableField("belonger")
    private SystemBelongEnum belonger;

    @TableField("is_valid")
    private BooleanEnum isValid;
}
