package com.pkk.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 数据源注解
 * @author: peikunkun
 * @create: 2019-03-18 17:09
 **/
//作用于类接口枚举方法上
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivateDataSource {

  /**
   * specl的数据源名称
   *
   * @return
   */
  String spel() default "";

}
