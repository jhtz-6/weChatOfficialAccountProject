package org.myf.wechatofficialaccountproject.infrastructure.base.enums;

import lombok.AllArgsConstructor;

/**
 *
 * @author myf
 */
@AllArgsConstructor
public enum MsgTypeEnum {

    TEXT("text", "文本"), IMAGE("image", "图片"), VOICE("voice", "语音"), VIDEO("video", "视频"), EVENT("event", "订阅/取消订阅");

    public String name;
    public String value;

    public static MsgTypeEnum getMsgTypeEnumByName(String name) {
        for (MsgTypeEnum msgTypeEnum : MsgTypeEnum.values()) {
            if (msgTypeEnum.name.equals(name)) {
                return msgTypeEnum;
            }
        }
        return null;
    }

}
