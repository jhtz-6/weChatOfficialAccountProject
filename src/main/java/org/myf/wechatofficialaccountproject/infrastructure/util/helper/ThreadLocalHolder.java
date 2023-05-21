package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import org.myf.wechatofficialaccountproject.infrastructure.base.enums.SystemBelongEnum;

/**
 * @Author: myf
 * @CreateTime: 2023-05-18  18:21
 * @Description: ThreadLocalHolder
 */
public class ThreadLocalHolder {
    /**
     * 区分请求
     */
    public static ThreadLocal<SystemBelongEnum> BELONGER_THREAD_LOCAL = new ThreadLocal<>();

}
