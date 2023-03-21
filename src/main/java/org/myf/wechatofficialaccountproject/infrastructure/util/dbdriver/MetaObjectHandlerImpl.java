package org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: myf
 * @CreateTime: 2023-03-05 23:29
 * @Description: MetaObjectHandlerImpl实现类
 */
@Component
public class MetaObjectHandlerImpl implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", DateUtils.stringToDate(DateUtils.dateToString(new Date(), null), null));
        metaObject.setValue("updateTime", DateUtils.stringToDate(DateUtils.dateToString(new Date(), null), null));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", DateUtils.stringToDate(DateUtils.dateToString(new Date(), null), null));
    }
}
