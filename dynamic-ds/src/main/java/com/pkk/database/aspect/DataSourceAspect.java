package com.pkk.database.aspect;

import com.pkk.database.annotation.ActivateDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @description: 切换数据源的切面编程
 * @author: peikunkun
 * @create: 2019-03-18 17:16
 **/

@Aspect
@Order(-88)// AOP在@Transactional之前执行，否则会事物执行失败
@Component
@Slf4j
public class DataSourceAspect {


  /**
   * @Description: 切换数据源的操作
   * @Param: [joinPoint, activateDataSource]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/3/18 0018 下午 5:34
   */
  @Before("@annotation(activateDataSource)")
  public void activateDataSource(JoinPoint joinPoint, ActivateDataSource activateDataSource) {

  }


}
