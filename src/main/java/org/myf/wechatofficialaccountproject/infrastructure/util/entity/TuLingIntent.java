package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author myf
 */
@Data
public class TuLingIntent {

    /**
     * 输出功能code
     */
    private Integer code;

    /**
     * 意图名称
     */
    private String intentName;

    /**
     * 意图动作名称
     */
    private String actionName;

    /**
     * 功能相关参数
     */
    private Map<String, String> parameters;
}
