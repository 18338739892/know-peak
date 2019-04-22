package com.pkk.peakrabbitmq.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @description: rabbit消息处理业务类
 * @author: peikunkun
 * @create: 2019-04-19 14:43
 **/
@Slf4j
@Component
public class RabbitReceiveBusinessService {

  @RabbitListener(queues = "master@retry")
  public void handleRetry(@Payload JSONObject obj, @Header Object header) {
    try {
      System.out
          .println("master.retry" + ":" + JSONObject.toJSONString(header) + ":" + JSONObject.toJSONString(obj));
    } catch (Exception e) {
      throw e;
    }

  }

  @RabbitListener(queues = "master@failed")
  public void handleField(@Payload JSONObject obj, @Header JSONObject header) {
    try {
      System.out.println("master@failed" + ":" + JSONObject.toJSONString(header) + ":" + JSONObject.toJSONString(obj));
    } catch (Exception e) {
      throw e;
    }
  }

  @RabbitListener(queues = "master")
  protected void handleMessage(@Payload JSONObject obj, @Header JSONObject header) {
    try {
      System.out.println("master" + ":" + JSONObject.toJSONString(header) + ":" + JSONObject.toJSONString(obj));
    } catch (Exception e) {
      throw e;
    }
  }
}
