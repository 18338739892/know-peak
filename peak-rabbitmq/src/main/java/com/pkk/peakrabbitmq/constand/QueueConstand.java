package com.pkk.peakrabbitmq.constand;

/**
 * @description: 队列的常量信息
 * @author: peikunkun
 * @create: 2019-04-22 11:26
 **/
public class QueueConstand {

  /**
   * 队列失败的后缀
   */
  public static final String FAILED_SUFFER = "@failed";

  /**
   * 队列重试的后缀
   */
  public static final String RETRY_SUFFER = "@retry";


  /**
   * 队列失败的队列
   */
  public static final String FAILED_QUEUE = TopicExchangeConstand.TOPIC_NAME_MASTER + FAILED_SUFFER;

  /**
   * 队列重试的队列
   */
  public static final String RETRY_QUEUE = TopicExchangeConstand.TOPIC_NAME_MASTER + RETRY_SUFFER;


  /**
   * master队列
   */
  public static final String MASTER_QUEUE = "master";


}
