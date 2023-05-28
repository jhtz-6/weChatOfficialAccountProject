package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author: myf
 * @CreateTime: 2023-03-15 17:34
 * @Description: OpenAiResponse
 */
@Data
public class OpenAiResponse {

    private String id;

    private String object;

    private long created;

    private List<ChoiceDTO> choices;

    private String model;

}
