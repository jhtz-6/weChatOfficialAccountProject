package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-09 20:32
 * @Description: TuLingRequest
 */
@Data
public class TuLingRequest {

    /**
     * 输入类型:0-文本(默认)、1-图片、2-音频
     */
    private Integer reqType;

    private TuLingPerception perception;

    private TuLingUserInfo userInfo;
}
