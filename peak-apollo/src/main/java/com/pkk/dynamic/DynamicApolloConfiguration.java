package com.pkk.dynamic;

import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.pkk.message.LoggerMessage;
import com.pkk.queue.LoggerQueue;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * @description: 动态改变配置的配置[https://github.com/flying632/ConfigRefresh/blob/master/PropertiesRefresher.java]
 * @author: peikunkun
 * @create: 2019-05-15 16:14
 **/
@Slf4j
@Configuration
@EnableApolloConfig
public class DynamicApolloConfiguration implements ApplicationContextAware {


  @Autowired
  private ApplicationContext applicationContext;
  @Autowired
  private RefreshScope refreshScope;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }


  @ApolloConfigChangeListener
  private void onChange(ConfigChangeEvent changeEvent) {
    this.refreshProperties(changeEvent.changedKeys());
  }


  /**
   * @Description: 刷新配置
   * @Param: [changedKeys]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/5/15 0015 下午 4:18
   */
  protected void refreshProperties(Set<String> changedKeys) {
    log.info("Refreshing business log : {}", changedKeys);
    applicationContext.publishEvent(new EnvironmentChangeEvent(changedKeys));

    /*最后一个问题，@RefreshScope作用的类，不能是final类，否则启动时会报错
     * @RefreshScope  这个注解会生成代理类,如果不识别代理请不要使用
     * 用于SpringCloud Config的配置比较多
       刷新配置可以
       refreshScope.refresh("sampleRedisConfig");
      刷新被RefreshScope修饰的配置类
      需要对boot的java config进行刷新处理
      因为config初始化之后不会再初始化了*/
    refreshScope.refreshAll();

    //发送消息
    this.pubilshDynamicPropertiesLog(changedKeys);
  }


  /**
   * @Description: 改变的消息
   * @Param: [changedKeys]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/5/15 0015 下午 4:21
   */
  protected void pubilshDynamicPropertiesLog(Set<String> changedKeys) {
    JSONObject jsonObject = new JSONObject();
    changedKeys.stream().forEach(c -> {
      jsonObject.put(c, applicationContext.getEnvironment().getProperty(c));
    });
    log.info("信息:" + jsonObject.toJSONString());
    LoggerMessage log = new LoggerMessage(changedKeys == null ? "无配置修改,请确认!" : jsonObject.toJSONString());
    LoggerQueue.getInstance().push(log);
  }


}
