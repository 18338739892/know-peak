package com.pkk.dynamic.use.dao;

import com.pkk.dynamic.use.entity.DataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataSourceDAO {

    /**
     * 查询数据源信息
     *
     * @param dbName
     * @return
     */
    DataSource selectDbInfoByDbName(String dbName);

    /**
     * @Description: 查询数据信息依据表名
     * @Param: [tableName]
     * @return: com.pkk.dynamic.use.entity.DataSource
     * @Author: peikunkun
     * @Date: 2019/3/20 0020 下午 4:37
     */
    List<Object> selectTableInfoByTable(@Param("tableName") String tableName);
}