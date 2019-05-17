package com.pkk.controller;

import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.pkk.message.LoggerMessage;
import com.pkk.queue.LoggerQueue;
import java.util.HashMap;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 发布消息的控制器
 * @author: peikunkun
 * @create: 2019-05-15 13:28
 **/
@Slf4j
@RestController
@RequestMapping("publish")
public class PublishMessageController {

  @ApolloConfig
  private Config config;


  /**
   * @Description: 获取配置信息
   * @Param: []
   * @return: java.lang.Object
   * @Author: peikunkun
   * @Date: 2019/5/17 0017 上午 9:31
   */
  @RequestMapping("getConfig")
  public Object getConfig() {
    Set<String> propertyNames = config.getPropertyNames();
    HashMap<Object, Object> configMap = new HashMap<>();
    propertyNames.forEach(key -> {
      configMap.put(key, config.getProperty(key, "无任何配置"));
      System.err.println(key + "=" + config.getIntProperty(key, 0));
    });
    return JSONObject.toJSONString(configMap);
  }


  /**
   * @Description: 发送消息
   * @Param: [msg]
   * @return: boolean
   * @Author: peikunkun
   * @Date: 2019/5/15 0015 下午 4:08
   */
  @RequestMapping("publishMessage")
  public boolean publishMessage(String msg) {
    try {
      LoggerMessage log = new LoggerMessage(msg);
      LoggerQueue.getInstance().push(log);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
