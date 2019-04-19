package com.pkk;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description: rabbitmq的启动类
 * @author: peikunkun
 * @create: 2019-04-18 13:58
 **/
@EnableRabbit
@ComponentScan(basePackages = "com.pkk")
@SpringBootApplication
public class PeakRabbitMqApplication extends SpringBootServletInitializer {


  public static void main(String[] args) {
    SpringApplication.run(PeakRabbitMqApplication.class, args);
  }

  /*支持tomcat的启动方式*/
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(PeakRabbitMqApplication.class);
  }
}
