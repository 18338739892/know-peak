package com.pkk.controller;

import com.pkk.message.LoggerMessage;
import com.pkk.queue.LoggerQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 发布消息的控制器
 * @author: peikunkun
 * @create: 2019-05-15 13:28
 **/
@Slf4j
@RestController
@RequestMapping("publish")
public class PublishMessageController {

  /**
   * @Description: 发送消息
   * @Param: [msg]
   * @return: boolean
   * @Author: peikunkun
   * @Date: 2019/5/15 0015 下午 4:08
   */
  @RequestMapping("publishMessage")
  public boolean publishMessage(String msg) {
    try {
      LoggerMessage log = new LoggerMessage(msg);
      LoggerQueue.getInstance().push(log);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
