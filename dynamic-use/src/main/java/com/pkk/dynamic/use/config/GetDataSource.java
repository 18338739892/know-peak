package com.pkk.dynamic.use.config;

import com.pkk.database.exception.DynamicDataSourceException;
import com.pkk.database.exception.code.DyDataSourceCode;
import com.pkk.database.strategy.DataSourceGetStrategy;
import com.pkk.dynamic.use.service.DataSourceService;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 获取数据源
 */
@Configuration
public class GetDataSource extends DataSourceGetStrategy {

    @Autowired
    private DataSourceService dataSourceService;

    /**
     * <p>Title: getDataSource<／p>
     * <p>Description: 获取数据源的策略<／p>
     * <p>Copyright: Copyright (c) 2018<／p>
     *
     * @author pkk
     * @date 2019/3/19 0019
     * @version 1.0
     */
    @Override
    protected DataSource getDataSource(String dataSourceKey) {

        com.pkk.dynamic.use.entity.DataSource entity = dataSourceService.selectDbInfoByDbName(dataSourceKey);
        if (null == entity) {
            throw new DynamicDataSourceException(DyDataSourceCode.DATABASEERROR.getCode(), "不存在此:[" + dataSourceKey + "],数据源");
        }

        //连接操作
        try {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setUsername(entity.getUsername());
            dataSource.setPassword(entity.getPassword());
            dataSource.setJdbcUrl(entity.getDbUrl());
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setLoginTimeout(1000 * 60);
            return dataSource;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DynamicDataSourceException(DyDataSourceCode.DATABASECONNECTIONERROR);
        }

    }
}
