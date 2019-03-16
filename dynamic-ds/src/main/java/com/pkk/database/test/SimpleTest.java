package com.pkk.database.test;

import org.springframework.core.Ordered;

/**
 * @description:
 * @author: peikunkun
 * @create: 2019-03-04 14:21
 **/
public class SimpleTest implements Ordered {


  //设置AOP执行顺序, 这里设置优于事务
  @Override
  public int getOrder() {
    return 0;
  }
}
