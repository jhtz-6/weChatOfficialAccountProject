package org.myf.wechatofficialaccountproject.application.dto;

import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

/**
 * @Author: myf
 * @CreateTime: 2023-05-19  10:22
 * @Description: UserDTO
 */
@Data
public class UserDTO {

    private String loginName;

    private String loginPassword;

    private SystemBelongEnum belonger;
}
