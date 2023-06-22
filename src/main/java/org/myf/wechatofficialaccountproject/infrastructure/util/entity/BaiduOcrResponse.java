package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import com.unfbx.chatgpt.entity.chat.Message;
import lombok.Data;

import java.util.List;

/**
 * @author myf
 */
@Data
public class BaiduOcrResponse {
    Message  message =new Message();
    private String log_id;

    private String words_result_num;

    private List<BaiduOcrWordsResult> words_result;
}
