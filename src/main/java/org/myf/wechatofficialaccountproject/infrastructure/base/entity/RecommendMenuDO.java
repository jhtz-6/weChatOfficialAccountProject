package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-06 21:58
 * @Description: RecommendMenuDO
 */
@TableName("recommend_menu")
@Data
public class RecommendMenuDO extends BaseDO {

    @TableField("menu_content")
    private String menuContent;

    @TableField("from_user_name")
    private String fromUserName;

    @TableField("sfyx")
    private String sfyx = "1";
}
