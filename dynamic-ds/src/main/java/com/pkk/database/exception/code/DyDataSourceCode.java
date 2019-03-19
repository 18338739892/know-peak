package com.pkk.database.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DyDataSourceCode {

  /**
   * DataSrcTypeRepository 对象未创建~空指针异常
   */
  DATASOURCE301(301, "DataSrcTypeRepository 对象未创建~空指针异常"),
  DATASOURCE302(302, "非正常操作数据，由于根据表主键未查询到数据或查询出多条数据"),

  PARAMSINCOMPLETE(460, "参数传递不完整"),
  OVERRIDEGETDATASOURCEINFO(410,
      "未找到DataSourceReadStrategy类中注册的Bean；没有实现抽象类DataSourceReadStrategy的抽象方法getDataSourceInfo，在使用动态数据源时该方法用于获取动态数据源的信息"),
  GETDATASOURCEINFOGRESULTNULL(420,
      "重写抽象类DataSourceReadStrategy的抽象方法getDataSourceInfo返回值为null，***可能由于数据源表中存储了，不存在或有问题的数据库信息***"),
  DATABASECONNECTIONERROR(430, "尝试连接数据库失败，无法创建该数据源"),
  GETMAXWAI_METHODUNDEFINED(440, "该连接池没有实现getMaxWait方法，不能获取最大等待的超时时间"),
  MUSTSETWAITTIME(450, "必须设置数据源获取连接的超时时间，一般数据源对象会提供setMaxWait方法来设置"),
  DATABASEERROR(460, "数据源不存在"),;
  private int code;
  private String msg;

  public DyDataSourceCode superadditionMsg(String msg) {
    this.msg += msg;
    return this;
  }

}