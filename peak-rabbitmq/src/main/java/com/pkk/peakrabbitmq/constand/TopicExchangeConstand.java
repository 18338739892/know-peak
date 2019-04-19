package com.pkk.peakrabbitmq.constand;

/**
 * @description: 主题模式的一些基本配置
 * @author: peikunkun
 * @create: 2019-04-19 13:14
 **/
public class TopicExchangeConstand {


  /**
   * 模式
   */
  public static final String TYPE = "topic";


  /**
   * 任意单词
   */
  public static final String TOPIC_MATCH_ANY = "#";

  /**
   * 一个单词
   */
  public static final String TOPIC_MATCH_ONE = "*";


  /**
   * change名称
   */
  public static final String TOPIC_NAME_RETRY = "master.retry";


  /**
   * change名称
   */
  public static final String TOPIC_NAME_FIELD = "master.field";


  /**
   * change名称
   */
  public static final String TOPIC_NAME_MASTER = "master";


}
