package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-06 21:14
 * @Description: SubscribeQueryParam
 */
@Data
public class SubscribeQueryParam extends BaseQueryParam {

    private String subscriber;

    private String status;

    private String area;
}
