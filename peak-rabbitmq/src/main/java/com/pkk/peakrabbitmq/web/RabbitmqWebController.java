package com.pkk.peakrabbitmq.web;

import com.alibaba.fastjson.JSONObject;
import com.pkk.peakrabbitmq.constand.TopicExchangeConstand;
import com.pkk.peakrabbitmq.service.RabbitProductBusinessService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: mq的请求控制器
 * @author: peikunkun
 * @create: 2019-04-19 14:47
 **/
@Slf4j
@RequestMapping("web")
@RestController
public class RabbitmqWebController {

  @Resource
  private RabbitProductBusinessService rabbitProductBusinessService;


  /**
   * @Description: 发送队列消息
   * @Param: [q, obg]
   * @return: java.lang.Object
   * @Author: peikunkun
   * @Date: 2019/4/19 0019 下午 3:50
   */
  @RequestMapping("send")
  public Object send(String q, String obg) {
    if (StringUtils.isBlank(q)) {
      q = TopicExchangeConstand.TOPIC_NAME_MASTER;
    }
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("msg", JSONObject.toJSONString(obg));
    final boolean b = rabbitProductBusinessService.sendMessage(q, jsonObject);
    if (b) {
      return "SUCCESS";
    }
    return "ERROR";
  }

}
