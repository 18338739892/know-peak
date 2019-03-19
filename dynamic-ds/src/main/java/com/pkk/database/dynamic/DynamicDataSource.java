package com.pkk.database.dynamic;

import com.pkk.database.utils.DataSourceSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {


    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
        logger.info("设置数据类型看看什么时候执行");
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String keyDataSource = DataSourceSet.getCurrDataSource();
        log.debug("***当前数据源为[{}]", keyDataSource == null ? "默认数据源" : keyDataSource);
        return keyDataSource;
    }
}
