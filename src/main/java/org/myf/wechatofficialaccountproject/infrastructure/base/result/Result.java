package org.myf.wechatofficialaccountproject.infrastructure.base.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.ErrorEnum;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 11:40
 * @Description: 返回结果
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Result<T> extends BaseResult {

    private T data;

    private ErrorEnum errorEnum;

    public Result(T data, ErrorEnum errorEnum) {
        super(errorEnum.getCode(), errorEnum.getErrorCode(), errorEnum.getErrorMessage(), errorEnum.getMessage());
        this.data = data;
        this.errorEnum = errorEnum;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, ErrorEnum.SUCCESS);
    }

    public static <T> Result<T> defaultFail(T data) {
        return new Result<>(data, ErrorEnum.INTERNAL_SERVER_ERROR);
    }

    public static <T> Result<T> fail(T data, ErrorEnum errorEnum) {
        return new Result<>(data, errorEnum);
    }

}
