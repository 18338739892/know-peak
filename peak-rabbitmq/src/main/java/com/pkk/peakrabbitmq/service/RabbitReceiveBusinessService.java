package com.pkk.peakrabbitmq.service;

import com.alibaba.fastjson.JSONObject;
import com.pkk.peakrabbitmq.base.AbstractRabbitReceive;
import com.pkk.peakrabbitmq.constand.QueueConstand;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @description: rabbit消息处理业务类
 * @author: peikunkun
 * @create: 2019-04-19 14:43
 **/
@Slf4j
@Component
@RabbitListener(queues = QueueConstand.MASTER_QUEUE)
public class RabbitReceiveBusinessService extends AbstractRabbitReceive {


  @Override
  protected void handleMessage(JSONObject msg, Map<String, Object> headers) {
    try {
      handle(msg, headers);
    } catch (Exception e) {
      throw e;
    }
  }
}
