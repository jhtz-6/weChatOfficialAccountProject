package org.myf.wechatofficialaccountproject.infrastructure.base.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18 16:06
 * @Description: KeyTypeEnum
 */
@AllArgsConstructor
public enum KeyTypeEnum {

    /**
     * 精确类型
     */
    PRECISE("PRECISE", "精确"),
    /**
     * 模糊类型
     */
    FUZZY("FUZZY", "模糊");

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static KeyTypeEnum getKeyTypeByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (KeyTypeEnum keyTypeEnum : KeyTypeEnum.values()) {
            if (keyTypeEnum.value.equals(value)) {
                return keyTypeEnum;
            }
        }
        return null;
    }
}
