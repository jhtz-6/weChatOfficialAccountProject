package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity;

import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.BooleanEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.KeyTypeEnum;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18  15:49
 * @Description: WechatKeyWordQueryParam
 */
@Data
public class WechatKeyWordQueryParam extends BaseQueryParam{

    private String keyName;

    private String valueContent;

    private KeyTypeEnum keyType;

    private SystemBelongEnum belonger;

    private BooleanEnum isValid;
}
