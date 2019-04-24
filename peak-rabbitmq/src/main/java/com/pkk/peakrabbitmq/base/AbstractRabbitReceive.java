package com.pkk.peakrabbitmq.base;

import com.alibaba.fastjson.JSONObject;
import com.pkk.peakrabbitmq.constand.PeakRabbitmqConstand;
import com.pkk.peakrabbitmq.constand.RoutingConstand;
import com.pkk.peakrabbitmq.constand.TopicExchangeConstand;
import com.pkk.peakrabbitmq.utils.RabbitMqUtil;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * @description: 消息处理类 延迟队列重试队列,参考[https://github.com/mylxsw/growing-up/blob/master/doc/RabbitMQ%E5%8F%91%E5%B8%83%E8%AE%A2%E9%98%85%E5%AE%9E%E6%88%98-%E5%AE%9E%E7%8E%B0%E5%BB%B6%E6%97%B6%E9%87%8D%E8%AF%95%E9%98%9F%E5%88%97.md]
 * @author: peikunkun
 * @create: 2019-04-19 17:07
 **/
@Slf4j
public abstract class AbstractRabbitReceive {

  @Resource
  private RabbitTemplate rabbitTemplate;

  /**
   * @Description: 消息处理
   * @Param: [obj]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/4/19 0019 下午 5:17
   */
  @RabbitHandler(isDefault = true)
  public void consumerHandle(Message message) {
    try {
      final JSONObject msg = JSONObject.parseObject(new String(message.getBody(), PeakRabbitmqConstand.CHART_URF8));
      //消费者自己做幂等
      /**
       * 消息处理之前
       */
      JSONObject jsonObject = this.beforeHandle(msg);

      /**
       *消息处理
       */
      this.handleMessage(msg, message.getMessageProperties().getHeaders());

      /**
       * 消息处理之后
       */
      this.afterHandle(jsonObject);
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
          log.info(
              "用户服务消费者消费失败，消息发送到重试队列;" + "原始消息：" + new String(newMessage.getBody()) + ";第" + (retryCount + 1) + "次重试");
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


  /**
   * @Description: 消息处理
   * @Param: [msg, headers]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/4/24 0024 下午 3:11
   */
  protected abstract void handleMessage(JSONObject msg, Map<String, Object> headers);

  /**
   * 消息处理之后
   *
   * @param obj
   */
  protected void afterHandle(Object obj) {
    log.info("消息处理结束--->" + JSONObject.toJSONString(obj));
  }

  /**
   * 消息处理之前
   *
   * @param obj
   */
  protected JSONObject beforeHandle(Object obj) {
    JSONObject jsonObject = new JSONObject();
    if (null != obj) {
      jsonObject = JSONObject.parseObject(JSONObject.toJSONString(obj));
    }
    log.info("消息开始处理--->[元]" + obj + " [宋]" + jsonObject);
    return jsonObject;
  }


  /**
   * @Description: 消息处理
   * @Param: [obj, headers]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/4/22 0022 下午 4:53
   */
  protected void handle(JSONObject obj, Map<String, Object> headers) {
    error(obj, headers);
  }

  /**
   * @Description: 自定义产生错误
   * @Param: [obj, headers]
   * @return: void
   * @Author: peikunkun
   * @Date: 2019/4/22 0022 下午 4:26
   */
  protected static void error(JSONObject obj, Map<String, Object> headers) {
    final boolean error = (Boolean) JSONObject.parseObject(obj.getString(PeakRabbitmqConstand.ERROR_OBJ_KEY))
        .getOrDefault(PeakRabbitmqConstand.ERROR_KEY, false);
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
