package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.CharacterTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.DepartmentEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18 20:48
 * @Description: AccompanyDO
 */
@Data
@TableName("accompany")
public class AccompanyDO extends BaseDO {

    @TableField("character_name")
    private String characterName;

    @TableField("is_elite")
    private Boolean isElite;

    @TableField("character_type")
    private CharacterTypeEnum characterType;

    @TableField("characterristic")
    private String characterristic;

    @TableField("department")
    private DepartmentEnum department;

    @TableField("score")
    private Integer score;

    @TableField("jade_pendant")
    private String jadePendant;

    @TableField("evaluate")
    private String evaluate;

    @TableField("url")
    private String url;

    @TableField("is_valid")
    private Boolean isValid;

    @TableField("belonger")
    private SystemBelongEnum belonger;

}
