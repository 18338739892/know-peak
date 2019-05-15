package com.pkk.dynamic;

import com.pkk.message.LoggerMessage;
import com.pkk.queue.LoggerQueue;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * @description: 动态改变配置的配置
 * @author: peikunkun
 * @create: 2019-05-15 16:14
 **/
@Slf4j
public class DynamicApolloConfiguration extends DynamicPropertiesConfig {


  /**
   * @param applicationContext
   * @param changedKeys
   * @Description: 改变的消息
   * @Param: [changedKeys]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/5/15 0015 下午 4:21
   */
  @Override
  protected void pubilshDynamicPropertiesLog(ApplicationContext applicationContext, Set<String> changedKeys) {
    LoggerMessage log = new LoggerMessage(changedKeys == null ? "无配置修改,请确认!" : changedKeys.toArray().toString());
    LoggerQueue.getInstance().push(log);
  }
}
