package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

import java.util.Date;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 15:39
 * @Description: MenuDO
 */
@TableName("menu")
@Data
public class MenuDO extends BaseDO {

    @TableField("food")
    private String food;

    @TableField("raw_material")
    private String rawMaterial;

    @TableField("sfyx")
    private String sfyx;

    @TableField("category")
    private String category;

    @TableField("min_num")
    private Integer minNum;

    @TableField("max_num")
    private Integer maxNum;

    @TableField("belong_user")
    private String belongUser;

    @TableField("price")
    private Double price;

    @TableField("cost_performance")
    private Double costPerformance;

    @TableField("belonger")
    private SystemBelongEnum belonger;
}
