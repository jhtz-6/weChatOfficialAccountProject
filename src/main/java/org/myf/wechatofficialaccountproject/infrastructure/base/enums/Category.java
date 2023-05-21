package org.myf.wechatofficialaccountproject.infrastructure.base.enums;

import lombok.AllArgsConstructor;


/**
 * @author myf
 */
@AllArgsConstructor
public enum Category {

    /**
     * 菜谱分类
     */
    SPECIAL("特"), COMMON("普"), PREMIUM("精");

    /**
     * 分类值
     */
    private final String value;

    public String getValue() {
        return value;
    }

}
