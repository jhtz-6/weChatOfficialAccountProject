package org.myf.wechatofficialaccountproject.application.dto;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-05-19  21:54
 * @Description: RequestLogDTO
 */
@Data
public class RequestLogDTO {

    private String userName;

    private String requestUrl;

    private String requestMethod;

    private String requestParams;

    private String clientType;

    private String ipAddress;

    private String location;

    private String userAgent;

}
