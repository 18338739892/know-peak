package com.pkk.dynamic;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @description: 动态配置属性
 * @author: peikunkun
 * @create: 2019-05-15 16:16
 **/
@Slf4j
public abstract class DynamicPropertiesConfig implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @ApolloConfigChangeListener
  private void onChange(ConfigChangeEvent changeEvent) {
    this.refreshProperties(applicationContext, changeEvent.changedKeys());
  }


  /**
   * @Description: 刷新配置
   * @Param: [changedKeys]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/5/15 0015 下午 4:18
   */
  protected void refreshProperties(ApplicationContext applicationContext, Set<String> changedKeys) {
    log.info("Refreshing business log : {}", changedKeys);
    applicationContext.publishEvent(new EnvironmentChangeEvent(changedKeys));

    //发送消息
    this.pubilshDynamicPropertiesLog(applicationContext, changedKeys);
  }


  /**
   * @Description: 改变的消息
   * @Param: [changedKeys]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/5/15 0015 下午 4:21
   */
  protected abstract void pubilshDynamicPropertiesLog(ApplicationContext applicationContext, Set<String> changedKeys);

}
