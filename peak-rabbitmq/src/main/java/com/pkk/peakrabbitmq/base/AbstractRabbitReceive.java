package com.pkk.peakrabbitmq.base;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * @description: 消息处理类
 * @author: peikunkun
 * @create: 2019-04-19 17:07
 **/
@Slf4j
public abstract class AbstractRabbitReceive {


  /**
   * @Description: 消息处理
   * @Param: [obj]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/4/19 0019 下午 5:17
   */
  @RabbitHandler
  public void consumerHandle(@Payload String obj) {
    try {
      JSONObject jsonObject = this.beforeHandle(obj);
      System.out.println(jsonObject.get("body"));
      /**
       *消息处理
       */
      this.handleMessage(obj);

      this.afterHandle(obj);
    } catch (Exception e) {
      throw e;
    }
  }

  protected abstract void handleMessage(Object obj);

  /**
   * 消息处理之后
   *
   * @param obj
   */
  protected void afterHandle(Object obj) {
    log.info("消息处理结束--->" + JSONObject.toJSONString(obj));
  }

  /**
   * 消息处理之前
   *
   * @param obj
   */
  protected JSONObject beforeHandle(Object obj) {
    JSONObject jsonObject = new JSONObject();
    if (null != obj) {
      jsonObject = JSONObject.parseObject(JSONObject.toJSONString(obj));
    }
    log.info("消息开始处理--->[元]" + obj + " [宋]" + jsonObject);
    return jsonObject;
  }
}
