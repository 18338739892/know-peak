package com.pkk.database.strategy;

import com.pkk.database.constants.DataSourceContants;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * 获取动态数据源策略的类
 *
 * @author Administrator
 */
public abstract class DataSourceGetStrategy {

    /**
     * @param dataSourceKey
     * @return
     * @Desc 获取动态数据源
     */
    public abstract DataSource getDataSource(String dataSourceKey);


    /**
     * <p>Title: getDataSourceReadStrategy<／p>
     * <p>Description: 作为一个bean注入到spring容器中<／p>
     * <p>Copyright: Copyright (c) 2018<／p>
     *
     * @author pkk
     * @date 2019/3/19 0019
     * @version 1.0
     */
    @Bean(name = DataSourceContants.DATASOURCEGETSTRATEGY)
    public DataSourceGetStrategy getDataSourceReadStrategy() {
        return this;
    }

}
