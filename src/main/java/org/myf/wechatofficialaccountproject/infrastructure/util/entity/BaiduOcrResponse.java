package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import lombok.Data;

import java.util.List;

/**
 * @author myf
 */
@Data
public class BaiduOcrResponse {

    private String log_id;

    private String words_result_num;

    private List<BaiduOcrWordsResult> words_result;
}
