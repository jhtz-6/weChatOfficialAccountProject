package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

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

    private SystemBelongEnum belonger;
}
