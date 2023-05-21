package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-05-19  21:38
 * @Description: RequestLogDO
 */
@Data
@TableName("request_log")
public class RequestLogDO extends BaseDO{

    @TableField("user_name")
    private String userName;

    @TableField("request_url")
    private String requestUrl;

    @TableField("request_method")
    private String requestMethod;

    @TableField("request_params")
    private String requestParams;

    @TableField("client_type")
    private String clientType;

    @TableField("ip_address")
    private String ipAddress;

    @TableField("location")
    private String location;

    @TableField("user_agent")
    private String userAgent;

    @TableField("additional_data")
    private String additionalAata;

    @TableField("additional_data01")
    private String additionalData01;

}
