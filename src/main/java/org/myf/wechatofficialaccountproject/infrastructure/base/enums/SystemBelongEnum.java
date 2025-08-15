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

    /**
     * pdx账号
     */
    YHJ("YHJ", "一壶酒"),

    ZZNH("ZZNH", "猪猪女孩"),

    ANSUI("ANSUI", "安岁"),

    GAME("GAME", "GAME"),

    WEMONEY("WEMONEY", "微信支付成功"),
    ;

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
