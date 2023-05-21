package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity;

import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.CharacterTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.DepartmentEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18  20:54
 * @Description: AccompanyQueryParam
 */
@Data
public class AccompanyQueryParam extends BaseQueryParam{

    private String characterName;

    private Boolean isElite;

    private CharacterTypeEnum characterType;

    private DepartmentEnum department;

    private SystemBelongEnum belonger;

    private Boolean isValid;

}
