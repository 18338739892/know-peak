package com.pkk.peakrabbitmq.constand;

import static com.pkk.peakrabbitmq.constand.QueueConstand.FAILED_SUFFER;

/**
 * @description: 主题模式的一些基本配置
 * @author: peikunkun
 * @create: 2019-04-19 13:14
 **/
public class TopicExchangeConstand {


  /**
   * change名称
   */
  public static final String TOPIC_NAME_RETRY = "master.retry";

  /**
   * change名称
   */
  public static final String TOPIC_NAME_FAILED = "master.failed";


  /**
   * change名称
   */
  public static final String TOPIC_NAME_MASTER = "master";


}
