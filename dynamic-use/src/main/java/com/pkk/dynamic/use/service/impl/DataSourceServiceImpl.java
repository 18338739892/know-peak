package com.pkk.dynamic.use.service.impl;

import com.pkk.database.annotation.ActivateDataSource;
import com.pkk.dynamic.use.dao.DataSourceDAO;
import com.pkk.dynamic.use.entity.DataSource;
import com.pkk.dynamic.use.service.DataSourceService;
import com.pkk.dynamic.use.vo.DataSourceChangeReqVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataSourceServiceImpl implements DataSourceService {


  @Autowired
  private DataSourceDAO dataSourceDAO;

  @Override
  public DataSource selectDbInfoByDbName(String dbName) {
    if (StringUtils.isEmpty(dbName)) {
      return null;
    }
    return dataSourceDAO.selectDbInfoByDbName(dbName);
  }


  /**
   * 切换数据源
   *
   * @param dataSourceChangeReqVo
   * @return
   */
  @Override
  @ActivateDataSource(spel = "#dataSourceChangeReqVo.dbName")
  public Object selectDbInfoByChangeDbName(DataSourceChangeReqVo dataSourceChangeReqVo) {
    return dataSourceDAO.selectTableInfoByTable(dataSourceChangeReqVo.getTableName());
  }
}
