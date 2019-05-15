package com.pkk.config;

import com.pkk.message.LoggerMessage;
import com.pkk.queue.LoggerQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @description: 长连接配置
 * @author: peikunkun
 * @create: 2019-05-15 11:06
 **/
@Configuration
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

  //用于转发数据(sendTo)
  @Autowired
  private SimpMessagingTemplate messagingTemplate;


  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/websocket")
        .setAllowedOrigins("*")
        .withSockJS();
  }

  /**
   * 推送日志到/topic/pullLogger
   */
  @PostConstruct
  public void pushLogger() {
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            LoggerMessage log = LoggerQueue.getInstance().poll();
            if (log != null) {
              if (messagingTemplate != null) {
                messagingTemplate.convertAndSend("/topic/pullLogger", log);
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    };
    executorService.submit(runnable);
    executorService.submit(runnable);
  }

}
