package com.pkk.peakrabbitmq.service;

import com.alibaba.fastjson.JSONObject;
import com.pkk.peakrabbitmq.base.AbstractRabbitReceive;
import com.pkk.peakrabbitmq.constand.PeakRabbitmqConstand;
import com.pkk.peakrabbitmq.constand.QueueConstand;
import com.pkk.peakrabbitmq.constand.RoutingConstand;
import com.pkk.peakrabbitmq.constand.TopicExchangeConstand;
import com.pkk.peakrabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @description: rabbit消息处理业务类
 * @author: peikunkun
 * @create: 2019-04-19 14:43
 **/
@Slf4j
//@Component
public class RabbitReceiveBusinessServiceBack {


  @Resource
  private RabbitTemplate rabbitTemplate;

  //@RabbitListener(queues = QueueConstand.MASTER_QUEUE)
  public void handleRetry(Message message, Channel channel) throws IOException {
    try {
      //消费者自己做幂等
      final String messageStr = new String(message.getBody(), PeakRabbitmqConstand.CHART_URF8);
      handle(JSONObject.parseObject(messageStr), message.getMessageProperties().getHeaders());

      //手动抛出异常,测试消息重试
      int i = 5 / 0;
    } catch (Exception e) {
      long retryCount = RabbitMqUtil.getRetryCount(message.getMessageProperties());
      CorrelationData correlationData = new CorrelationData(message.getMessageProperties().getCorrelationId());
      Message newMessage = null;
      if (retryCount >= 3) {
        //如果重试次数大于3,则将消息发送到失败队列等待人工处理
        newMessage = RabbitMqUtil.buildMessage(message);
        try {
          rabbitTemplate
              .convertAndSend(TopicExchangeConstand.TOPIC_CHANGE_FAILED, RoutingConstand.ROUTING_MASTER, newMessage,
                  correlationData);
          log.info("用户体系服务消费者消费消息在重试3次后依然失败，将消息发送到fail队列,发送消息:" + new String(newMessage.getBody()));
        } catch (Exception e1) {
          log.error("用户体系服务消息在发送到fail队列的时候报错:" + e1.getMessage() + ",原始消息:" + new String(newMessage.getBody()));
        }
      } else {
        newMessage = RabbitMqUtil.buildMessage2(message);
        try {
          //如果当前消息被重试的次数小于3,则将消息发送到重试队列,等待重新被消费{延迟消费}
          //String exchange, String routingKey, Object object, CorrelationData correlationData
          rabbitTemplate
              .convertAndSend(TopicExchangeConstand.TOPIC_CHANGE_RETRY, RoutingConstand.ROUTING_MASTER, newMessage,
                  correlationData);
          String baseMsg =
              "基础消息:exchange:" + TopicExchangeConstand.TOPIC_CHANGE_RETRY + ",routingkey:"
                  + RoutingConstand.ROUTING_MASTER + ",消息id:" + correlationData;
          log.info("{" + baseMsg + "}\n" + "用户服务消费者消费失败，消息发送到重试队列;" + "原始消息：" + new String(newMessage.getBody()) + ";第"
              + (retryCount + 1) + "次重试");
        } catch (Exception e1) {
          // 如果消息在重发的时候,出现了问题,可用nack,经过开发中的实际测试，当消息回滚到消息队列时，
          // 这条消息不会回到队列尾部，而是仍是在队列头部，这时消费者会立马又接收到这条消息，进行处理，接着抛出异常，
          // 进行回滚，如此反复进行。这种情况会导致消息队列处理出现阻塞，消息堆积，导致正常消息也无法运行
          // channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
          // 改为重新发送消息,经过多次重试后，如果重试次数大于3,就不会再走这，直接丢到了fail queue等待人工处理
          log.error("消息发送到重试队列的时候，异常了:" + e1.getMessage() + ",重新发送消息");
        }
      }
    } finally {
      /**
       * 关闭rabbitmq的自动ack,改为手动ack 1、因为自动ack的话，其实不管是否成功消费了，rmq都会在收到消息后立即返给生产者ack,但是很有可能 这条消息我并没有成功消费
       * 2、无论消费成功还是消费失败,都要手动进行ack,因为即使消费失败了,也已经将消息重新投递到重试队列或者失败队列 如果不进行ack,生产者在超时后会进行消息重发,如果消费者依然不能处理，则会存在死循环
       */
      //此处需开启手动确认消息
      //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
  }

  //@RabbitListener(queues = QueueConstand.RETRY_QUEUE)
  public void handleRetry(@Payload JSONObject obj, @Headers Map<String, Object> headers, Message message,
      Channel channel) throws IOException {
    try {
      handle(obj, headers);
    } catch (Exception e) {
      throw e;
    }
  }

  //@RabbitListener(queues = QueueConstand.FAILED_QUEUE)
  public void handleField(@Payload JSONObject obj, @Headers Map<String, Object> headers) {
    try {
      handle(obj, headers);
    } catch (Exception e) {
      throw e;
    }
  }

  //@RabbitListener(queues = "master")
  protected void handleMessage(@Payload JSONObject obj, @Headers Map<String, Object> headers) {
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

  /**
   * @Description: 自定义产生错误
   * @Param: [obj, headers]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/4/22 0022 下午 4:26
   */
  private static void error(JSONObject obj, Map<String, Object> headers) {
    final boolean error = (Boolean) JSONObject.parseObject(obj.getString(PeakRabbitmqConstand.ERROR_OBJ_KEY))
        .getJSONObject(PeakRabbitmqConstand.ERROR_OBJ_KEY).getOrDefault(PeakRabbitmqConstand.ERROR_KEY, false);
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
