package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-13 14:22
 * @Description: ConfigurationDO
 */
@Data
@TableName("configuration")
public class ConfigurationDO extends BaseDO {

    @TableField("name")
    private String name;
    @TableField("value")
    private String value;
}
