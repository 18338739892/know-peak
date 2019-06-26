package com.pkk;

import com.pkk.penguin.support.dynamic.datasource.EnableDynamicDB;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @description: 动态数据源
 * @author: peikunkun
 * @create: 2019-03-04 14:30
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.pkk.*.*.dao")
@EnableDynamicDB
public class DynamicDSApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(DynamicDSApplication.class, args);
  }

  /**
   * 如此配置打包后可以用tomcat下使用
   *
   * @param builder
   * @return
   */
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(DynamicDSApplication.class);
  }


}
