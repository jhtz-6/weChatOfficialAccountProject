package org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler.money;

import org.myf.wechatofficialaccountproject.domain.service.chain.impl.handler.EventHandler;

import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: myf
 * @CreateTime: 2023-04-17 12:18
 * @Description: 订阅/取消订阅事件处理逻辑
 */
@Service
public class MoneyEventHandler extends EventHandler {
    private static final ReentrantLock lock = new ReentrantLock();

}
