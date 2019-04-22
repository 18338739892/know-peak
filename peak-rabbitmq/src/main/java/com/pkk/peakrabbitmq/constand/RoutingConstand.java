package com.pkk.peakrabbitmq.constand;

/**
 * @description: 路由key的常量类
 * @author: peikunkun
 * @create: 2019-04-22 15:47
 **/
public class RoutingConstand {


  /**
   * 任意单词
   */
  public static final String ROUTING_MATCH_ANY = "#";

  /**
   * 一个单词
   */
  public static final String ROUTING_MATCH_ONE = "*";


  /**
   * routing的master的
   */
  public static final String ROUTING_MASTER = "master";

  /**
   * routing的[master.*]
   */
  public static final String ROUTING_MASTER_ANY = ROUTING_MASTER + PeakRabbitmqConstand.POINT + ROUTING_MATCH_ANY;

}
