package com.pkk.dynamic.use.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 数据源查询请求类
 * @author: peikunkun
 * @create: 2019-03-20 16:22
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceReqVo implements Serializable {

  private static final long serialVersionUID = 1703148175675639264L;

  /**
   * 数据源名称
   */
  private String dbName;
}
