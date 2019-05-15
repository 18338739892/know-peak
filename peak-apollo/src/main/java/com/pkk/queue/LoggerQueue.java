package com.pkk.queue;

import com.alibaba.fastjson.JSONObject;
import com.pkk.message.LoggerMessage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 日志队列
 * @author: peikunkun
 * @create: 2019-05-15 11:03
 **/
@Slf4j
public class LoggerQueue {

  //队列大小
  public static final int QUEUE_MAX_SIZE = 10000;
  private static LoggerQueue alarmMessageQueue = new LoggerQueue();
  //阻塞队列
  private BlockingQueue blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

  private LoggerQueue() {
  }

  public static LoggerQueue getInstance() {
    return alarmMessageQueue;
  }

  /**
   * 消息入队
   *
   * @param loggerMessage
   * @return
   */
  public boolean push(LoggerMessage loggerMessage) {
    log.info("发送消息:" + JSONObject.toJSONString(loggerMessage));
    return this.blockingQueue.add(loggerMessage);//队列满了就抛出异常，不阻塞
  }

  /**
   * 消息出队
   *
   * @return
   */
  public LoggerMessage poll() {
    LoggerMessage result = null;
    try {
      result = (LoggerMessage) this.blockingQueue.take();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return result;
  }
}
