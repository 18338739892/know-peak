package com.pkk.database.config;

import com.pkk.database.constants.DataSourceContants;
import com.pkk.database.dynamic.DynamicDataSource;
import com.pkk.database.utils.DataSourceSet;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DynamicDataSourceConfig {

    /**
     * 启动的时候加载数据库的配置
     */
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource mainDataSource() {
        return new HikariDataSource();
    }

    /**
     * ，对同一个接口，可能会有几种不同的实现类，而默认只会采取其中一种的情况下 @Primary 的作用就出来了
     *  启动时加载,将默认的数据源设置到数据源集合中【启动时只有一个默认的数据源】
     * @param dataSource
     * @return
     */
    @Bean("dataSource")
    @Primary
    public DynamicDataSource dataSource(DataSource dataSource) {
        DataSourceSet.putTargetDataSourceMap(DataSourceContants.DEFAULT_MAIN_DATASOURCE, dataSource);
        return new DynamicDataSource(dataSource, DataSourceSet.getTargetDataSourcesMap());
    }
}
