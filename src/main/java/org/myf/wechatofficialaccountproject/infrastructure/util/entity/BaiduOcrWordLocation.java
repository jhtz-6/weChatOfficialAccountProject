package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import lombok.Data;

/**
 * @author myf
 */
@Data
public class BaiduOcrWordLocation {

    private Integer top;

    private Integer left;

    private Integer width;

    private Integer height;
}
