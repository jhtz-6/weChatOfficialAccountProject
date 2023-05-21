package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-05-19  23:25
 * @Description: UserQueryParam
 */
@Data
public class UserQueryParam extends BaseQueryParam{

    @TableField("login_name")
    private String loginName;

    @TableField("login_password")
    private String loginPassword;

    @TableField("is_valid")
    private Boolean isValid;
}
