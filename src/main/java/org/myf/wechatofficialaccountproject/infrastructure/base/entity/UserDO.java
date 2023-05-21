package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

/**
 * @Author: myf
 * @CreateTime: 2023-05-19 23:22
 * @Description: UserDO
 */
@TableName("user")
@Data
public class UserDO extends BaseDO {

    @TableField("login_name")
    private String loginName;

    @TableField("login_password")
    private String loginPassword;

    @TableField("is_valid")
    private Boolean isValid;

    @TableField("belonger")
    private SystemBelongEnum belonger;
}
