package com.pkk.database.exception;

import com.pkk.database.exception.code.DyDataSourceCode;

/**
 * @description: 动态切换数据源异常类
 * @author: peikunkun
 * @create: 2019-03-19 17:02
 **/
public class DynamicDataSourceException extends RuntimeException {

  public int getResCode() {
    return resCode;
  }

  //响应编号
  private int resCode;

  public DynamicDataSourceException(DyDataSourceCode resType) {
    super(resType.getMsg());
    this.resCode = resType.getCode();
  }

  public DynamicDataSourceException(int code, String msg) {
    super(msg);
    this.resCode = code;
  }

  public DynamicDataSourceException() {
    super("没有定义异常信息描述，默认的BIException");
    this.resCode = 99998;
  }
}
