package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 17:35
 * @Description: MaterialDO
 */
@TableName("material")
@Data
public class MaterialDO extends BaseDO {

    @TableField("materialName")
    private String materialName;

    @TableField("sfyx")
    private String sfyx = "1";

    @TableField("price")
    private Integer price;

    @TableField("num")
    private Integer num;

    @TableField("spent_time")
    private String spentTime;
}
