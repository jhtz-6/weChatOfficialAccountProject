package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-05-27 18:41
 * @Description: ChatgptMessageQueryParam
 */
@Data
public class ChatgptMessageQueryParam extends BaseQueryParam {

    private String fromUserName;

    private Integer limitNum;
}
