package com.pkk.peakrabbitmq.service;

import com.alibaba.fastjson.JSONObject;
import com.pkk.peakrabbitmq.constand.PeakRabbitmqConstand;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
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
  public void handleRetry(@Payload JSONObject obj, @Headers Map<String, Object> headers) {
    try {
      handle(obj, headers);
    } catch (Exception e) {
      throw e;
    }

  }

  /**
   * @Description: 消息处理
   * @Param: [obj, headers]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/4/22 0022 下午 4:53
   */
  private void handle(JSONObject obj, Map<String, Object> headers) {
    error(obj, headers);
  }

  @RabbitListener(queues = "master@failed")
  public void handleField(@Payload JSONObject obj, @Headers Map<String, Object> headers) {
    try {
      handle(obj, headers);
    } catch (Exception e) {
      throw e;
    }
  }

  @RabbitListener(queues = "master")
  protected void handleMessage(@Payload JSONObject obj, @Headers Map<String, Object> headers) {
    try {
      handle(obj, headers);
    } catch (Exception e) {
      throw e;
    }
  }


  /**
   * @Description: 自定义产生错误
   * @Param: [obj, headers]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/4/22 0022 下午 4:26
   */
  private static void error(JSONObject obj, Map<String, Object> headers) {
    final boolean error = (Boolean) obj.getOrDefault(PeakRabbitmqConstand.ERROR_OBJ, false);
    final String exchange = (String) headers.getOrDefault("amqp_receivedExchange", "未知交换器");
    final String routing = (String) headers.getOrDefault("amqp_receivedRoutingKey", "未知路由");
    final String msg =
        "交换器:" + exchange + ":" + "路由:" + routing + "头信息:" + JSONObject.toJSONString(headers) + "参数信息:" + JSONObject
            .toJSONString(obj);
    System.out.println(msg);
    if (error) {
      throw new RuntimeException("自定义错误:" + msg);
    }
  }
}
