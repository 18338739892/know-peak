package com.pkk.peakrabbitmq.utils;

import com.alibaba.fastjson.JSONObject;
import com.pkk.peakrabbitmq.base.Message;
import javax.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 生产者
 * @author: peikunkun
 * @create: 2019-04-18 17:52
 **/
@Configuration
public class RabbitMqProduct {

  @Resource
  private RabbitTemplate rabbitTemplate;

  /**
   * @Description: 发送消息
   * @Param: [queueName, object]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/4/19 0019 下午 2:41
   */
  public void send(String exchangeName, String queueName, JSONObject msgJson) {
    CorrelationData correlationData = new CorrelationData(String.valueOf(System.currentTimeMillis()));
    final Message message = new Message(msgJson.toJSONString());
    rabbitTemplate.convertAndSend(exchangeName, queueName, message, correlationData);
  }

}
