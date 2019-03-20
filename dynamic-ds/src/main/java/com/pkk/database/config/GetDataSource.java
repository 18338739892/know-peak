package com.pkk.database.config;

import com.pkk.database.strategy.DataSourceGetStrategy;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 获取数据源
 */
public class GetDataSource/* extends DataSourceGetStrategy*/ {

    /**
     * <p>Title: getDataSource<／p>
     * <p>Description: 获取数据源的策略<／p>
     * <p>Copyright: Copyright (c) 2018<／p>
     *
     * @author pkk
     * @date 2019/3/19 0019
     * @version 1.0
     */
//    @Override
    public DataSource getDataSource(String dataSourceKey) {


        /*try {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setUsername();
            dataSource.setPassword();
            dataSource.setJdbcUrl();
            dataSource.setDriverClassName();
            dataSource.setLoginTimeout(1000 * 60);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;*/
        return null;
    }
}
