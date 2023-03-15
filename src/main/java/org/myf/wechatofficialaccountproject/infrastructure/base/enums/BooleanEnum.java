package org.myf.wechatofficialaccountproject.infrastructure.base.enums;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 14:58
 * @Description: BooleanEnum
 */
@AllArgsConstructor
public enum BooleanEnum {

    TRUE("true", "y"), FALSE("false", "n");

    public String name;

    public String value;

}
