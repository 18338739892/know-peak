package com.pkk.dynamic.use.config;

import com.pkk.database.exception.DynamicDataSourceException;
import com.pkk.database.exception.code.DyDataSourceCode;
import com.pkk.database.strategy.DataSourceGetStrategy;
import com.pkk.dynamic.use.service.DataSourceService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
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
    public DataSource getDataSource(String dataSourceKey) {

        com.pkk.dynamic.use.entity.DataSource entity = dataSourceService.selectDbInfoByDbName(dataSourceKey);
        if (null == entity) {
            throw new DynamicDataSourceException(DyDataSourceCode.DATABASEERROR.getCode(),
                    "不存在此:[" + dataSourceKey + "],数据源");
        }

        //连接操作
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String validationQuery = "select 1";
        String url = "jdbc:mysql://%s:%s/%s";
        url = String.format(url, entity.getDbIp(), entity.getDbPort(), entity.getDbName());
        if (StringUtils.isNotBlank(entity.getParameters())) {
            url += "?" + entity.getParameters();
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url); //数据源
        config.setUsername(entity.getUsername()); //用户名
        config.setPassword(entity.getPassword()); //密码
        config.setDriverClassName(driverClassName);
        config.setConnectionTimeout(1000 * 60);
        config.setConnectionTestQuery(validationQuery);
        config.addDataSourceProperty("cachePrepStmts", "true"); //是否自定义配置，为true时下面两个参数才生效
        config.addDataSourceProperty("prepStmtCacheSize", "250"); //连接池大小默认25，官方推荐250-500
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048"); //单条语句最大长度默认256，官方推荐2048
        config.addDataSourceProperty("useServerPrepStmts", "true"); //新版本MySQL支持服务器端准备，开启能够得到显著性能提升
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("useLocalTransactionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        return new HikariDataSource(config);
    }
}
