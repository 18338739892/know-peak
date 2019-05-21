package com.pkk.dynamic;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.pkk.apollo.util.PeakApolloUtil;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 日志级别配置
 * @author: peikunkun
 * @create: 2019-05-20 10:01
 **/
@Slf4j
@Configuration
public class LoggerApolloConfiguration {

  /**
   * properties的配置
   */
  private static final String LOGGER_TAG = "logging.level.";

  @ApolloConfig("application")
  private Config config;
  @Resource
  private LoggingSystem loggingSystem;


  @ApolloConfigChangeListener(interestedKeys = {LOGGER_TAG})
  private void configLoggerChangeListener(ConfigChangeEvent configChangeEvent) {
    refreshLoggingLevels();
  }

  //1.@PostConstruct说明
  //被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次，类似于Serclet的init()方法。被@PostConstruct修饰的方法会在构造函数之后，init()方法之前运行。
  //2.@PreDestroy说明
  //被@PreDestroy修饰的方法会在服务器卸载Servlet的时候运行，并且只会被服务器调用一次，类似于Servlet的destroy()方法。被@PreDestroy修饰的方法会在destroy()方法之后运行，在Servlet被彻底卸载之前。（详见下面的程序实践）
  @PostConstruct
  private void refreshLoggingLevels() {
    final Set<String> keyNames = config.getPropertyNames();
    for (String key : keyNames) {
      //是否包含此属性
      if (StringUtils.containsIgnoreCase(key, LOGGER_TAG)) {
        String strLevel = config.getProperty(key, "info");
        LogLevel level = LogLevel.valueOf(strLevel.toUpperCase());
        loggingSystem.setLogLevel(key.replace(LOGGER_TAG, ""), level);
      }
    }

    //发送消息
    PeakApolloUtil.pubilshDynamicPropertiesLog(null, keyNames);
  }

}
