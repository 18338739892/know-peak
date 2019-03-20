package com.pkk.database.utils;

import com.pkk.database.common.SpringContextUtils;
import com.pkk.database.constants.DataSourceContants;
import com.pkk.database.dynamic.DynamicDataSource;
import com.pkk.database.exception.DynamicDataSourceException;
import com.pkk.database.exception.code.DyDataSourceCode;
import com.pkk.database.strategy.DataSourceGetStrategy;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

@Slf4j
public class DataSourceUtil {


    /**
     * 获取动态数据源的策略bean
     * 【第一次加载的时候为空,然后从spring中去出】
     */
    private static DataSourceGetStrategy dataSourceGetStrategy;

    /**
     * <p>Title: activateDataSource<／p>
     * <p>Description: 切换和使用数据源<／p>
     * <p>Copyright: Copyright (c) 2018<／p>
     *
     * @author pkk
     * @date 2019/3/19 0019
     * @version 1.0
     */
    public static final void activateDataSource(String dataSourceKey) {
        //不包含此数据源
        if (!DataSourceSet.containsDataSource(dataSourceKey)) {
            log.debug("正在尝试创建数据源[{}] ", dataSourceKey);
            setDataSource2Context(dataSourceKey);
            log.debug("数据源[{}]创建成功", dataSourceKey);
        } else {
            log.debug("[{}]数据源被激活", dataSourceKey);
            DataSourceSet.setCurrDataSource(dataSourceKey);
        }

    }

    private static void setDataSource2Context(String dataSourceKey) {
        //0.通过DataSourceGetStrategy策略创建数据源信息的code字段查找对应数据源的信息
        //载入数据源
        DataSource newDataSource = loadDataSource(dataSourceKey);

        //1.检查数据源是否合法
        newDataSource = checkDataSource(newDataSource);

        //检查数据源是否能连接
        testConnection(newDataSource);

        //2.存储新的数据源到数据源集合中
        DataSourceSet.putTargetDataSourceMap(dataSourceKey, newDataSource);

        //3.同步数据源
        syncDataSource();

        //4.数据源创建完成后立即使用
        DataSourceSet.setCurrDataSource(dataSourceKey);
    }

    /**
     * <p>Title: syncDataSource<／p>
     * <p>Description: 设置数据源的列表和默认数据源到配置中并使其生效<／p>
     * <p>Copyright: Copyright (c) 2018<／p>
     *
     * @author pkk
     * @date 2019/3/20 0020
     * @version 1.0
     */
    private static void syncDataSource() {
        //1.获得已经注册的dataSourceBean并重新设置包含数据源的map
        DynamicDataSource dataSource = (DynamicDataSource) SpringContextUtils.getContext().getBean("dataSource");
        //2.将DataSourcesMap设置回数据源
        dataSource.setTargetDataSources(DataSourceSet.getTargetDataSourcesMap());
        //3.确保后添加到targetDataSources的数据源同步到resolvedDataSources;
        dataSource.afterPropertiesSet();
    }

    private static boolean testConnection(DataSource dataSource) {
        try {
            dataSource.getConnection();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DynamicDataSourceException(DyDataSourceCode.DATABASECONNECTIONERROR);
        }
        return true;
    }

    private static DataSource checkDataSource(DataSource dataSource) {
        if (dataSource == null) {
            throw new DynamicDataSourceException(DyDataSourceCode.GETDATASOURCEINFOGRESULTNULL);
        }

        long maxWait = -1l;
        try {//建立数据源之后检查是否设置了最大的等待时间
            //maxWait = Long.valueOf(dataSource.getClass().getMethod("getMaxWait").invoke(dataSource).toString());
            maxWait = Long.valueOf(dataSource.getClass().getMethod("getConnectionTimeout").invoke(dataSource).toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("该连接池没有实现getMaxWait方法，不能获取最大的等待的超时时间");
            throw new DynamicDataSourceException(DyDataSourceCode.GETMAXWAI_METHODUNDEFINED);
        }
        if (maxWait <= 0) {
            throw new DynamicDataSourceException(DyDataSourceCode.MUSTSETWAITTIME);
        }
        return dataSource;
    }

    /**
     * <p>Title: loadDataSource<／p>
     * <p>Description: 获取数据源<／p>
     * <p>Copyright: Copyright (c) 2018<／p>
     *
     * @author pkk
     * @date 2019/3/19 0019
     * @version 1.0
     */
    private static DataSource loadDataSource(String dataSourceKey) {
        if (null == dataSourceGetStrategy) {
            synchronized (DataSourceUtil.class) {
                if (null == dataSourceGetStrategy) {
                    if (!SpringContextUtils.containsBean(DataSourceContants.DATASOURCEGETSTRATEGY)) {
                        throw new DynamicDataSourceException(DyDataSourceCode.OVERRIDEGETDATASOURCEINFO);
                    }
                    dataSourceGetStrategy = (DataSourceGetStrategy) SpringContextUtils.getContext()
                            .getBean(DataSourceContants.DATASOURCEGETSTRATEGY);
                }
            }
        }
        return dataSourceGetStrategy.getDataSource(dataSourceKey);
    }

    /**
     * <p>Title: resetDataSource<／p>
     * <p>Description: 重置数据源<／p>
     * <p>Copyright: Copyright (c) 2018<／p>
     *
     * @author pkk
     * @date 2019/3/20 0020
     * @version 1.0
     */
    public static void resetDataSource(String keyDataSource) {
        if (keyDataSource != null) {
            log.debug("数据源[{}]使用完毕，重置为默认数据源", keyDataSource);
        } else {
            log.debug("已经重置为默认数据源");
        }
        DataSourceSet.cleanCurrDataSource();
    }
}
