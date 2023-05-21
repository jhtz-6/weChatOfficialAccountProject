package org.myf.wechatofficialaccountproject.infrastructure.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;


/**
 * @Author: myf
 * @CreateTime: 2023-03-06 20:59
 * @Description: SubscribeDO
 */
@TableName("subscribe")
@Data
public class SubscribeDO extends BaseDO {

    @TableField("subscriber")
    private String subscriber;

    @TableField("status")
    private String status;

    @TableField("area")
    private String area;

    @TableField("belonger")
    private SystemBelongEnum belonger;
}
