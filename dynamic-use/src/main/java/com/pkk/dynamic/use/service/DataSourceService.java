package com.pkk.dynamic.use.service;

import com.pkk.dynamic.use.entity.DataSource;
import com.pkk.dynamic.use.vo.DataSourceChangeReqVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
public interface DataSourceService {


  /**
   * 查询数据源信息
   *
   * @param dbName
   * @return
   */
  DataSource selectDbInfoByDbName(String dbName);


  /**
   * 切换数据源
   *
   * @param dataSourceChangeReqVo
   * @return
   */
  List<Object> selectDbInfoByChangeDbName(DataSourceChangeReqVo dataSourceChangeReqVo);
}