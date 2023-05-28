package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-15 17:37
 * @Description: ChoiceDTO
 */
@Data
public class ChoiceDTO {

    private String text;

    private String resultText;

    private String index;

    private String logprobs;

    private String finish_reason;

    private DetailDTO delta;
}
