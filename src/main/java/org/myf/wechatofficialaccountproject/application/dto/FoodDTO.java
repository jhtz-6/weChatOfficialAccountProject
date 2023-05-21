package org.myf.wechatofficialaccountproject.application.dto;

import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 23:30
 * @Description: FoodDTO
 */
@Data
public class FoodDTO {

    private Integer id;

    private String foodName;

    private String sfyx = "1";

    private SystemBelongEnum belonger;

}
