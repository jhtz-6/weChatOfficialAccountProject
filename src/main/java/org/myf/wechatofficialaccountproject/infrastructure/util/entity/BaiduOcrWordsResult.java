package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import lombok.Data;

/**
 * @author myf
 */
@Data
public class BaiduOcrWordsResult {

    private String words;

    private BaiduOcrWordLocation location;
}
