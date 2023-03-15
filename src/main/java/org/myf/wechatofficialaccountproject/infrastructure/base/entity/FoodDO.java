package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 22:55
 * @Description: FoodDO
 */
@TableName("food")
@Data
public class FoodDO extends BaseDO {

    @TableField("foodName")
    private String foodName;

    @TableField("sfyx")
    private String sfyx = "1";

}
