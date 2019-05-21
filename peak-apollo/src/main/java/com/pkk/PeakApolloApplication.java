package com.pkk;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

/**
 * @description: 启动类【动态修改配置可依据:https://blog.csdn.net/qq_39470733/article/details/88632566】
 * @author: peikunkun
 * @create: 2019-05-14 16:19
 **/
@EnableEurekaClient
@EnableApolloConfig
@SpringBootApplication
@EnableWebSocketMessageBroker
public class PeakApolloApplication {

  public static void main(String[] args) {
    SpringApplication.run(PeakApolloApplication.class, args);
  }

}
