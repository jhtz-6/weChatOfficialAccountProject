package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-09 20:32
 * @Description: TuLingPerception
 */
@Data
public class TuLingPerception {

    /**
     * 文本信息
     */
    private TuLingInputText inputText;

    /**
     * 图片地址
     */
    private TuLingInputImage inputImage;
}
