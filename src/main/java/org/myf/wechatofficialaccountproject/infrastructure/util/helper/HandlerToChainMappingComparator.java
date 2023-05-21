package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import com.alibaba.fastjson.JSONObject;
import org.myf.wechatofficialaccountproject.infrastructure.util.entity.HandlerToChainMapping;

import java.util.Comparator;

/**
 * @Author: myf
 * @CreateTime: 2023-05-21 19:12
 * @Description: HandlerToChainMappingComparator
 */
public class HandlerToChainMappingComparator implements Comparator<HandlerToChainMapping> {
    @Override
    public int compare(HandlerToChainMapping obj1, HandlerToChainMapping obj2) {
        int priority1 = obj1.getPriority();
        int priority2 = obj2.getPriority();
        return Integer.compare(priority2, priority1); // 降序排序
    }
}
