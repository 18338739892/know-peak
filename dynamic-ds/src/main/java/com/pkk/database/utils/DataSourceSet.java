package com.pkk.database.utils;

import com.pkk.database.exception.DynamicDataSourceException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class DataSourceSet {

    /**
     * 当前数据源的全局
     * ThreadLocal,叫线程本地变量或线程本地存储。
     * ThreadLocal为变量在每个线程中都创建了一个副本，那么每个线程可以访问自己内部的副本变量。
     * 这里使用它的子类InheritableThreadLocal用来保证父子线程都能拿到值。
     * new InheritableThreadLocal<String>();
     */
    private static final ThreadLocal<String> contextHolder = new ThreadLocal();

    /**
     * 安全并发list的读写容器[添加的时候会复制一个容器然后再引用地址执行新增加数据的list上]
     */
    private static List<String> dataSourceKeyList = new CopyOnWriteArrayList<>();

    /**
     * 并发容器【存动态数据源的信息】
     */
    private static Map targetDataSourceMap = new ConcurrentHashMap();


    /**
     * @param key
     * @param dataSource
     * @return
     */
    public static final Object putTargetDataSourceMap(Object key, Object dataSource) {
        dataSourceKeyList.add(key.toString());
        return targetDataSourceMap.put(key, dataSource);
    }

    public static Map getTargetDataSourcesMap() {
        return targetDataSourceMap;
    }

    /**
     * @param dataSourceKey
     * @return
     */
    public static final Object removeTargetDataSource(Object dataSourceKey) {
        try {
            dataSourceKeyList.remove(dataSourceKey);
            return targetDataSourceMap.remove(dataSourceKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DynamicDataSourceException(00000, "移除DataSourceSet数据源信息时出现异常，可能由于dataSourceKeyList或targetDataSourcesMap没有该item项");
        }
    }


    /**
     * 设置当前数据源
     *
     * @param dataSourceKey
     */
    public static final void setCurrDataSource(String dataSourceKey) {
        contextHolder.set(dataSourceKey);
    }

    public static final String getCurrDataSource() {
        return contextHolder.get();
    }

    public static final void cleanCurrDataSource() {
        contextHolder.remove();
    }

    public static boolean containsDataSource(String dataSourceKey) {
        return dataSourceKeyList.contains(dataSourceKey);
    }


}
