package com.pkk.database.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;

/**
 * @description:
 * @author: peikunkun
 * @create: 2019-03-04 14:21
 **/
@Slf4j
public class SimpleTest implements Ordered {


    //设置AOP执行顺序, 这里设置优于事务
    @Override
    public int getOrder() {
        return 0;
    }


    @Test
    public void SimpleTest_23() {
        System.out.println("欢迎使用单元测试方法【SimpleTest_23()】");
        System.out.println("此方法测试描述：【SimpleTest】");
    }
}
