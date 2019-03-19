package com.pkk.dynamic.use.dao;

import com.pkk.dynamic.use.entity.DataSource;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DataSourceDAO {

    /**
     * 查询数据源信息
     *
     * @param dbName
     * @return
     */
    DataSource selectDbInfoByDbName(String dbName);

}