package org.myf.wechatofficialaccountproject.infrastructure.base.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author myf
 */
@AllArgsConstructor
public enum SystemBelongEnum {

    /**
     * 领导
     */
    LEADER("LEADER", "领导"),

    YNSS("YNSS", "予你盛世"),

    ANSUI("ANSUI", "安岁");

    private String name;

    private String value;

    public static SystemBelongEnum getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (SystemBelongEnum systemBelongEnum : SystemBelongEnum.values()) {
            if (systemBelongEnum.name.equals(name)) {
                return systemBelongEnum;
            }
        }
        return null;
    }
}
