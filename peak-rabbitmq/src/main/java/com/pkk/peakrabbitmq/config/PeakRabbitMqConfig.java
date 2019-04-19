package com.pkk.peakrabbitmq.config;

import com.pkk.peakrabbitmq.constand.TopicExchangeConstand;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import javax.annotation.Resource;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
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
  public RabbitTemplate rabbitTemplate() {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
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
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    return factory;
  }


  @Bean
  public Channel topicChanel() throws IOException {
    final Connection connection = connectionFactory.createConnection();
    final Channel channel = connection.createChannel(true);
    //此处设置的exchange是持久化的，和消息无关系,消息只能在队列中进行持久化
    channel.exchangeDeclare(TopicExchangeConstand.TOPIC_NAME_MASTER, TopicExchangeConstand.TYPE, true);
    channel.exchangeDeclare(TopicExchangeConstand.TOPIC_NAME_RETRY, TopicExchangeConstand.TYPE, true);
    channel.exchangeDeclare(TopicExchangeConstand.TOPIC_NAME_FIELD, TopicExchangeConstand.TYPE, true);
    return channel;
  }


  /**
   * @Description: 主题模式
   * @Param: []
   * @return: org.springframework.amqp.core.TopicExchange
   * @Author: peikunkun
   * @Date: 2019/4/19 0019 下午 4:38
   */
  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange(TopicExchangeConstand.TOPIC_NAME_MASTER, true, false);
  }


  //有参数connectFactory，若spring容器中只有一个ConnectionFactory 类型的bean，则不论参数取名为何都是按类型取bean ConnectionFactory 为参数，若有多个则参数取名必须为多个bean中的一个，否则报错。
  @Bean
  public Binding bindMasterToMaster(@Qualifier(TopicExchangeConstand.TOPIC_NAME_MASTER) Queue queue,
      TopicExchange topicExchange) {
    return BindingBuilder.bind(queue).to(topicExchange).with(TopicExchangeConstand.TOPIC_NAME_MASTER);
  }

  //有参数connectFactory，若spring容器中只有一个ConnectionFactory 类型的bean，则不论参数取名为何都是按类型取bean ConnectionFactory 为参数，若有多个则参数取名必须为多个bean中的一个，否则报错。
  @Bean
  public Binding bindRetryToMaster(@Qualifier(TopicExchangeConstand.TOPIC_NAME_RETRY) Queue queue,
      TopicExchange topicExchange) {
    return BindingBuilder.bind(queue).to(topicExchange).with(TopicExchangeConstand.TOPIC_NAME_RETRY);
  }


  //<!-- 排他性，exclusive=true:首次申明的connection连接下可见; exclusive=false：所有connection连接下都可见 -->
  @Bean(TopicExchangeConstand.TOPIC_NAME_MASTER)
  public Queue topicMaster() {
    return new Queue(TopicExchangeConstand.TOPIC_NAME_MASTER, true);
  }


  @Bean(TopicExchangeConstand.TOPIC_NAME_RETRY)
  public Queue topicRetry() {
    return new Queue(TopicExchangeConstand.TOPIC_NAME_RETRY, true);
  }


  @Bean(TopicExchangeConstand.TOPIC_NAME_FIELD)
  public Queue topicField() {
    return new Queue(TopicExchangeConstand.TOPIC_NAME_FIELD, true);
  }

}
