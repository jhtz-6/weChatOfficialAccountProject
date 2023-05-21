package org.myf.wechatofficialaccountproject.infrastructure.base.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SfyxEnum {

    YOU_XIAO("1", "有效"), WU_XIAO("0", "无效");

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
}
