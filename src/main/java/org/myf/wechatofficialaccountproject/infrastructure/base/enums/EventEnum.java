package org.myf.wechatofficialaccountproject.infrastructure.base.enums;

/**
 * @author myf
 * @description EventEnum微信消息时间类型枚举
 */
public enum EventEnum {

    SUBSCRIBE("subscribe", "订阅"), UNSUBSCRIBE("unsubscribe", "取消订阅");

    public String name;
    public String value;

    EventEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static EventEnum getEventEnumByName(String name) {
        for (EventEnum eventEnum : EventEnum.values()) {
            if (eventEnum.name.equals(name)) {
                return eventEnum;
            }
        }
        return null;
    }
}
