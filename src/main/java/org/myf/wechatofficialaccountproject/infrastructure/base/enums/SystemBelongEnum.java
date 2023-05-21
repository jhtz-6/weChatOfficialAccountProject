package org.myf.wechatofficialaccountproject.infrastructure.base.enums;

import lombok.AllArgsConstructor;

/**
 * @author myf
 */
@AllArgsConstructor
public enum SystemBelongEnum {

    /**
     * 领导
     */
    LEADER("LEADER","领导"),

    YNSS("YNSS","予你盛世");

    private String name;

    private String value;
}
