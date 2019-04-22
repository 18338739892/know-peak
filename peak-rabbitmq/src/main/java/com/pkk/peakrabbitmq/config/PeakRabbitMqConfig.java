package com.pkk.peakrabbitmq.config;

import static com.pkk.peakrabbitmq.constand.TopicExchangeConstand.TOPIC_MATCH_ANY;
import static com.pkk.peakrabbitmq.constand.TopicExchangeConstand.TOPIC_NAME_FAILED;
import static com.pkk.peakrabbitmq.constand.TopicExchangeConstand.TOPIC_NAME_MASTER;
import static com.pkk.peakrabbitmq.constand.TopicExchangeConstand.TOPIC_NAME_RETRY;

import com.pkk.peakrabbitmq.constand.QueueConstand;
import com.pkk.peakrabbitmq.constand.TopicExchangeConstand;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @description: mq的基础配置
 * @author: peikunkun
 * @create: 2019-04-18 15:24
 **/
@Configuration
public class PeakRabbitMqConfig {

  @Resource
  private ConnectionFactory connectionFactory;


  /**
   * @Description: 消息序列化器
   * @Param: []
   * @return: org.springframework.amqp.support.converter.MessageConverter
   * @Author: peikunkun
   * @Date: 2019/4/19 0019 下午 6:36
   */
  @Bean
  public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }


  /**
   * @Description: 发送消息的
   * @必须是prototype类型
   * @Param: []
   * @return: org.springframework.amqp.rabbit.core.RabbitTemplate
   * @Author: peikunkun
   * @Date: 2019/4/19 0019 下午 6:32
   */
  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public RabbitTemplate rabbitTemplate(MessageConverter messageConverter) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(messageConverter);
    return rabbitTemplate;
  }

  /**
   * @Description: 这个指定的是配置监听的消息的消息序列化信息
   * @Param: [connectionFactory]
   * @return: org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
   * @Author: peikunkun
   * @Date: 2019/4/19 0019 下午 6:31
   */
  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
      MessageConverter messageConverter) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(messageConverter);
    return factory;
  }


  /**
   * 订阅服务标识是客户端自己对订阅的分类标识符，比如用户中心服务（服务名称ucenter），包含两个订阅：user和enterprise，这里两个订阅的队列名称就为
   * ucenter@user和ucenter@enterprise，其对应的重试队列为 ucenter@user@retry和ucenter@enterprise@retry。
   *
   * @return
   */
  @Bean
  public Channel topicChanel() throws IOException {
    final Connection connection = connectionFactory.createConnection();
    final Channel channel = connection.createChannel(true);
    //此处设置的exchange是持久化的，和消息无关系,消息只能在队列中进行持久化
    channel.exchangeDeclare(TOPIC_NAME_MASTER, TopicExchangeConstand.TYPE, true);
    channel.exchangeDeclare(TOPIC_NAME_RETRY, TopicExchangeConstand.TYPE, true);
    channel.exchangeDeclare(TOPIC_NAME_FAILED, TopicExchangeConstand.TYPE, true);

    //定义队列
    //exclusive	false	排他，指定该选项为true则队列只对当前连接有效，连接断开后自动删除
    //master[[服务名称]@订阅服务标识]
    channel.queueDeclare(TOPIC_NAME_MASTER, true, false, false, null);
    channel.queueDeclare(TOPIC_NAME_MASTER + QueueConstand.FAILED_SUFFER, true, false, false, null);

    /**
     * 重试队列需要追加额外的参数
     * 这里的两个header字段的含义是，在队列中延迟30s后，将该消息重新投递到x-dead-letter-exchange对应的Exchange中，
     * 并且routing key指定为消费队列的名称，这样就可以实现消息只投递给原始出错时的队列，避免消息重新投递给所有关注当前routing key的消费者了。
     */
    Map<String, Object> retryArguments = new HashMap<String, Object>();
    retryArguments.put("x-dead-letter-exchange", TOPIC_NAME_MASTER);
    retryArguments.put("x-message-ttl", 30 * 1000);//重试时间30秒
    retryArguments.put("x-dead-letter-routing-key", TOPIC_NAME_MASTER);
    channel.queueDeclare(TOPIC_NAME_MASTER + QueueConstand.RETRY_SUFFER, true, false, false, retryArguments);

    //队列绑定
    // 绑定监听队列到Exchange
    //[queue,exchange,routingKey]
    channel.queueBind(TOPIC_NAME_MASTER, TOPIC_NAME_MASTER, TOPIC_NAME_MASTER + "." + TOPIC_MATCH_ANY);
    channel.queueBind(TOPIC_NAME_MASTER, TOPIC_NAME_MASTER, TOPIC_NAME_MASTER);
    channel.queueBind(TOPIC_NAME_MASTER + "@failed", TOPIC_NAME_FAILED, TOPIC_NAME_MASTER);
    channel.queueBind(TOPIC_NAME_MASTER + "@retry", TOPIC_NAME_RETRY, TOPIC_NAME_MASTER);
    return channel;
  }

  /**
   * @Description: 主题模式
   * @Param: []
   * @return: org.springframework.amqp.core.TopicExchange
   * @Author: peikunkun
   * @Date: 2019/4/19 0019 下午 4:38
   */
  /*@Bean
  public TopicExchange topicExchange() {
    return new TopicExchange(TOPIC_NAME_MASTER, true, false);
  }*/


  /*//有参数connectFactory，若spring容器中只有一个ConnectionFactory 类型的bean，则不论参数取名为何都是按类型取bean ConnectionFactory 为参数，若有多个则参数取名必须为多个bean中的一个，否则报错。
  @Bean
  public Binding bindMasterToMaster(@Qualifier(TOPIC_NAME_MASTER) Queue queue,
      TopicExchange topicExchange) {
    return BindingBuilder.bind(queue).to(topicExchange).with(TOPIC_NAME_MASTER);
  }

  //有参数connectFactory，若spring容器中只有一个ConnectionFactory 类型的bean，则不论参数取名为何都是按类型取bean ConnectionFactory 为参数，若有多个则参数取名必须为多个bean中的一个，否则报错。
  @Bean
  public Binding bindRetryToMaster(@Qualifier(TopicExchangeConstand.TOPIC_NAME_RETRY) Queue queue,
      TopicExchange topicExchange) {
    return BindingBuilder.bind(queue).to(topicExchange).with(TopicExchangeConstand.TOPIC_NAME_RETRY);
  }*/

  //<!-- 排他性，exclusive=true:首次申明的connection连接下可见; exclusive=false：所有connection连接下都可见 -->
  /*@Bean(TOPIC_NAME_MASTER)
  public Queue topicMaster() {
    return new Queue(TOPIC_NAME_MASTER, true);
  }


  @Bean(TopicExchangeConstand.TOPIC_NAME_RETRY)
  public Queue topicRetry() {
    return new Queue(TopicExchangeConstand.TOPIC_NAME_RETRY, true);
  }


  @Bean(TopicExchangeConstand.TOPIC_NAME_FAILED)
  public Queue topicField() {
    return new Queue(TopicExchangeConstand.TOPIC_NAME_FAILED, true);
  }*/

}
