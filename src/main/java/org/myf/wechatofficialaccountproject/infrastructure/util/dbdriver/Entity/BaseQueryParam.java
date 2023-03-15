package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author: myf
 * @CreateTime: 2023-03-06 21:11
 * @Description: BaseQueryParam
 */
@Data
public class BaseQueryParam {

    private Date createTime;

    private Date updateTime;

    private Integer id;

}
