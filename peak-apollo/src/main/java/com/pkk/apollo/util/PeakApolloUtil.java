package com.pkk.apollo.util;

import com.alibaba.fastjson.JSONObject;
import com.pkk.message.LoggerMessage;
import com.pkk.queue.LoggerQueue;
import java.util.Set;
import org.springframework.context.ApplicationContext;

/**
 * @description:
 * @author: peikunkun
 * @create: 2019-05-14 16:19
 **/
public class PeakApolloUtil {


  /**
   * @Description: 改变的消息
   * @Param: [changedKeys]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/5/15 0015 下午 4:21
   */
  public static void pubilshDynamicPropertiesLog(ApplicationContext applicationContext, Set<String> changedKeys) {
    JSONObject jsonObject = new JSONObject();
    changedKeys.stream().forEach(c -> {
      jsonObject.put(c, c);
      //jsonObject.put(c, applicationContext == null ? null : applicationContext.getEnvironment().getProperty(c));
    });
    LoggerMessage log = new LoggerMessage(changedKeys == null ? "无配置修改,请确认!" : jsonObject.toJSONString());
    LoggerQueue.getInstance().push(log);
  }
}
