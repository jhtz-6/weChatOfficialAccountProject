package org.myf.wechatofficialaccountproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author myf
 */
@MapperScan("org.myf.wechatofficialaccountproject.infrastructure.util.dbdriver.mapper")
@SpringBootApplication
public class WeChatOfficialAccountProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeChatOfficialAccountProjectApplication.class, args);
    }

}
