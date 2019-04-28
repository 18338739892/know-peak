package com.pkk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @description: 启动类
 * @author: peikunkun
 * @create: 2019-04-28 10:05
 **/
@SpringBootApplication
@MapperScan(value = "com.pkk.peaksmall.dao")
@EnableEurekaClient
@EnableScheduling
@EnableTransactionManagement
//放在com.pkk，默认扫描com.pkk,那jar包的配置也会被扫描，下面就可以省略
//@ComponentScan(basePackages = {"com.pkk"})
public class PeakSmallApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(PeakSmallApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(PeakSmallApplication.class);
  }
}
