package com.pkk.peakrabbitmq.base;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * @description: 消息处理类
 * 延迟队列重试队列,参考[https://github.com/mylxsw/growing-up/blob/master/doc/RabbitMQ%E5%8F%91%E5%B8%83%E8%AE%A2%E9%98%85%E5%AE%9E%E6%88%98-%E5%AE%9E%E7%8E%B0%E5%BB%B6%E6%97%B6%E9%87%8D%E8%AF%95%E9%98%9F%E5%88%97.md]
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
  public void consumerHandle(@Payload Message message) {
    try {
      final JSONObject msg = JSONObject.parseObject(message.getMsg());
      /**
       * 消息处理之前
       */
      JSONObject jsonObject = this.beforeHandle(msg);

      /**
       *消息处理
       */
      this.handleMessage(msg);

      /**
       * 消息处理之后
       */
      this.afterHandle(msg);
    } catch (Exception e) {
      /*Envelope envelope = null;
      AMQP.BasicProperties properties = null;
      long retryCount = getRetryCount(properties);
      if (retryCount > 3) {
        // 重试次数大于3次，则自动加入到失败队列
        log.info("failed. send message to failed exchange");

        Map<String, Object> headers = new HashMap<>();
        headers.put("x-orig-routing-key", getOrigRoutingKey(properties, envelope.getRoutingKey()));
        channel.basicPublish(failedExchangeName(), queueName, createOverrideProperties(properties, headers), body);
      } else {
        // 重试次数小于3，则加入到重试队列，30s后再重试
        log.info("exception. send message to retry exchange");
        Map<String, Object> headers = properties.getHeaders();
        if (headers == null) {
          headers = new HashMap<>();
        }

        headers.put("x-orig-routing-key", getOrigRoutingKey(properties, envelope.getRoutingKey()));
        channel.basicPublish(retryExchangeName(), queueName, createOverrideProperties(properties, headers), body);
      }*/
    }
  }


  /**
   * 获取原始的routingKey
   *
   * @param properties AMQP消息属性
   * @param defaultValue 默认值
   * @return 原始的routing-key
   */
  protected String getOrigRoutingKey(AMQP.BasicProperties properties, String defaultValue) {
    String routingKey = defaultValue;
    try {
      Map<String, Object> headers = properties.getHeaders();
      if (headers != null) {
        if (headers.containsKey("x-orig-routing-key")) {
          routingKey = headers.get("x-orig-routing-key").toString();
        }
      }
    } catch (Exception ignored) {
    }

    return routingKey;
  }


  /**
   * 获取消息重试次数
   *
   * @param properties AMQP消息属性
   * @return 消息重试次数
   */
  protected Long getRetryCount(AMQP.BasicProperties properties) {
    Long retryCount = 0L;
    try {
      Map<String, Object> headers = properties.getHeaders();
      if (headers != null) {
        if (headers.containsKey("x-death")) {
          List<Map<String, Object>> deaths = (List<Map<String, Object>>) headers.get("x-death");
          if (deaths.size() > 0) {
            Map<String, Object> death = deaths.get(0);
            retryCount = (Long) death.get("count");
          }
        }
      }
    } catch (Exception ignored) {
    }

    return retryCount;
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
