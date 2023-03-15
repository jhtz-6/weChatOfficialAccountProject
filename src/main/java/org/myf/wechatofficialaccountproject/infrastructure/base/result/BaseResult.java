package org.myf.wechatofficialaccountproject.infrastructure.base.result;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 11:11
 * @Description: 返回结果基础实体
 */
@Data
public class BaseResult {

    private Integer code;

    private Integer errorCode;

    private String errorMessage;

    private String message;

    public BaseResult() {}

    public BaseResult(Integer code, Integer errorCode, String errorMessage, String message) {
        this.code = code;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.message = message;
    }
}
