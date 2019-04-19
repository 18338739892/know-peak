package com.pkk.peakrabbitmq.service;

import com.alibaba.fastjson.JSONObject;
import com.pkk.peakrabbitmq.base.AbstractRabbitReceive;
import com.pkk.peakrabbitmq.constand.TopicExchangeConstand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @description: rabbit消息处理业务类
 * @author: peikunkun
 * @create: 2019-04-19 14:43
 **/
@Slf4j
@Component
@RabbitListener(queues = TopicExchangeConstand.TOPIC_NAME_MASTER)
public class RabbitReceiveBusinessService extends AbstractRabbitReceive {

 /* @RabbitListener(queues = TopicExchangeConstand.TOPIC_NAME_RETRY)
  public void handleRetry(JSONObject obj) {
    System.out.println(TopicExchangeConstand.TOPIC_NAME_RETRY + ":" + JSONObject.toJSONString(obj));
  }


  @RabbitListener(queues = TopicExchangeConstand.TOPIC_NAME_FIELD)
  public void handleField(JSONObject obj) {
    System.out.println(TopicExchangeConstand.TOPIC_NAME_FIELD + ":" + JSONObject.toJSONString(obj));
  }*/


  @Override
  protected void handleMessage(Object obj) {
    System.out.println(TopicExchangeConstand.TOPIC_NAME_MASTER + ":" + JSONObject.toJSONString(obj));
  }
}
