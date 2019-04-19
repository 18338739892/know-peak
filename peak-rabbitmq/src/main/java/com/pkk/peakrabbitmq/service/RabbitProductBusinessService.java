package com.pkk.peakrabbitmq.service;

import com.alibaba.fastjson.JSONObject;
import com.pkk.peakrabbitmq.utils.RabbitMqProduct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description: 队列生产业务类
 * @author: peikunkun
 * @create: 2019-04-19 14:43
 **/
@Slf4j
@Service
public class RabbitProductBusinessService {

  @Resource
  private RabbitMqProduct rabbitMqProduct;


  /**
   * @Description: 发送消息
   * @Param: [queueName, obj]
   * @return: boolean
   * @Author: peikunkun
   * @Date: 2019/4/19 0019 下午 3:36
   */
  public boolean sendMessage(String queueName, JSONObject message) {
    try {
      rabbitMqProduct.send(queueName, message);
      return true;
    } catch (RuntimeException e) {
      return false;
    }
  }

}
