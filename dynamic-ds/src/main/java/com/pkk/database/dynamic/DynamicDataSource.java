package com.pkk.database.dynamic;

import com.pkk.database.utils.DataSourceSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {


    /**
     * <p>Title: DynamicDataSource<／p>
     * <p>Description: 创建使用数据源的时候进行设置<／p>
     * <p>Copyright: Copyright (c) 2018<／p>
     * com.pkk.database.config.DynamicDataSourceConfig#dataSource(javax.sql.DataSource)的调用,设置默认的数据源
     *
     * @author pkk
     * @date 2019/3/20 0020
     * @version 1.0
     */
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    /**
     * <p>Title: DynamicDataSource<／p>
     * <p>Description: 操作数据库的时候都会执行此方法,以此来获取当前数据源<／p>
     * <p>Copyright: Copyright (c) 2018<／p>
     *
     * @author pkk
     * @date 2019/3/20 0020
     * @version 1.0
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String keyDataSource = DataSourceSet.getCurrDataSource();
        log.debug("***当前数据源为[{}]", keyDataSource == null ? "默认数据源" : keyDataSource);
        return keyDataSource;
    }
}
