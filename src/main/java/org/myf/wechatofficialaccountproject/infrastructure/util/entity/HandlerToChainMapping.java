package org.myf.wechatofficialaccountproject.infrastructure.util.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18  22:45
 * @Description: HandlerToChainMapping
 */
@Data
public class HandlerToChainMapping implements Serializable {

    private String handlerName;

    private Integer priority;


}
