package com.pkk.message;

import com.pkk.components.common.constant.SysUtilConstant;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Data;

/**
 * @description: 日志队列
 * @author: peikunkun
 * @create: 2019-05-15 11:04
 **/
@Data
public class LoggerMessage {

  /**
   * 消息体
   */
  private String body;
  /**
   * 时间戳
   */
  private String timestamp = String.valueOf(
      new SimpleDateFormat(SysUtilConstant.YYYYMMDDHHMMSS_STANDARD).format(new Date(System.currentTimeMillis())));

  public LoggerMessage(String body) {
    this.body = body;
  }

  public LoggerMessage(String body, String timestamp) {
    this.body = body;
    this.timestamp = timestamp;
  }


}
