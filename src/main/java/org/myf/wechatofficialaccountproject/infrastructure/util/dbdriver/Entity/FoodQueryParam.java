package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity;

import lombok.Data;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 22:59
 * @Description: FoodQueryParam
 */
@Data
public class FoodQueryParam extends BaseQueryParam {

    private String foodName;

    private String sfyx = "1";
}
