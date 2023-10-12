package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity;

import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 22:59
 * @Description: FoodQueryParam
 */
@Data
public class FoodQueryParam extends BaseQueryParam {

    private String foodName;

    private SystemBelongEnum belonger;

    private String sfyx = "1";
}
