package org.myf.wechatofficialaccountproject.infrastructure.base.enums;

/**
 *
 * @author myf
 */
public enum ErrorEnum {

    SUCCESS(200, null, null, "SUCCESS"), NOT_FOUND(404, 404, "NOT FOUND", "NOT FOUND"),
    INTERNAL_SERVER_ERROR(500, 500, "Internal_Server_Error", "INTERNAL_SERVER_ERROR");

    public Integer code;

    public Integer errorCode;

    public String errorMessage;

    public String message;

    ErrorEnum() {}

    ErrorEnum(Integer code, Integer errorCode, String errorMessage, String message) {
        this.code = code;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
