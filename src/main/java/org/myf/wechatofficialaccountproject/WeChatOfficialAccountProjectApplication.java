package org.myf.wechatofficialaccountproject;

import org.mybatis.spring.annotation.MapperScan;
import org.myf.wechatofficialaccountproject.infrastructure.util.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author myf
 */
@MapperScan("org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper")
@SpringBootApplication
public class WeChatOfficialAccountProjectApplication {

    private static final int NETTY_PORT = 8824;

    public static void main(String[] args) {
        SpringApplication.run(WeChatOfficialAccountProjectApplication.class, args);

        NettyServer nettyServer =new NettyServer(NETTY_PORT);
        nettyServer.start();
    }

}
