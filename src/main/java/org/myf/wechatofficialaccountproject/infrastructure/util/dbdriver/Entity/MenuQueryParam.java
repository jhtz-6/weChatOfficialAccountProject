package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity;

import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 16:07
 * @Description: MenuQueryParam
 */
@Data
public class MenuQueryParam extends BaseQueryParam {

    private String food;

    private String sfyx;

    private SystemBelongEnum belonger;

}
