package com.pkk.peakrabbitmq.constand;

/**
 * @description: rabbitmq的常量类
 * @author: peikunkun
 * @create: 2019-04-22 16:07
 **/
public class PeakRabbitmqConstand {

  /**
   * 如果队列设置了Dead Letter Exchange（DLX），那么这些Dead Letter就会被重新publish到Dead Letter Exchange，通过Dead Letter Exchange路由到其他队列
   * 死信队列 交换机标识符【声明了队列里的死信转发到的DLX名称】
   */
  public static final String DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
  /**
   * 死信队列交换机绑定键标识符【声明了这些死信在转发时携带的routing-key名称】
   */
  public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
  /**
   * 死信队列里面消息的超时时间
   */
  public static final String X_MESSAGE_TTL = "x-message-ttl";


  /**
   * 小数点
   */
  public static final String POINT = ".";


  /**
   * 错误码
   */
  public static final String ERROR_KEY = "error";
  /**
   * 信息
   */
  public static final String ERROR_OBJ = "obj";
  /**
   * 信息key
   */
  public static final String ERROR_OBJ_KEY = "msg";

  /**
   * 编码utf-8
   */
  public static final String CHART_URF8 = "UTF-8";

}
