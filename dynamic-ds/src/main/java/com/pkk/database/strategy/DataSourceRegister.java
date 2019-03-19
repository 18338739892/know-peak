package com.pkk.database.strategy;

import com.pkk.database.constants.DataSourceContants;
import com.pkk.database.utils.DataSourceSet;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;

/**
 * 使用方法：
 * 1、增加db_info 配置文件
 * # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
 * driverClassName: com.mysql.jdbc.Driver
 * url: jdbc:mysql://117.143.217.94:3306/bi_manager?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8
 * username: root
 * password: 123456
 * # 下面为连接池的补充设置，应用到上面所有数据源中
 * # 初始化大小，最小，最大
 * initialSize: 5
 * minIdle: 5
 * maxActive: 20
 * # 配置获取连接等待超时的时间
 * maxWait: 60000
 * # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
 * timeBetweenEvictionRunsMillis: 80000
 * # 配置一个连接在池中最小生存的时间，单位是毫秒
 * validationQuery: SELECT 1 FROM DUAL
 * testWhileIdle: true
 * testOnBorrow: false
 * testOnReturn: false
 * # 打开PSCache，并且指定每个连接上PSCache的大小
 * poolPreparedStatements: true
 * maxPoolPreparedStatementPerConnectionSize: 20
 * # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
 * filters: stat,wall,log4j
 * # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
 * connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
 * # 合并多个DruidDataSource的监控数据
 * #spring.datasource.useGlobalDataSourceStat=true
 * 2、import 此类即可
 * 用于配置数据库为单独文件
 * [暂时没用-动态数据源参考上面的哪一个com.pkk.database.strategy.DataSourceGetStrategy]
 */
@Deprecated
public class DataSourceRegister implements EnvironmentAware, ImportBeanDefinitionRegistrar {

    private DataSource defaultTargetDataSource;

    @Override
    public final void setEnvironment(Environment environment) {
      /*  DruidEntity druidEntity = FileUtil
                .readYmlByClassPath("db_info", DruidEntity.class);
        defaultTargetDataSource = DataSourceUtil.createMainDataSource(druidEntity);*/
    }

    @Override
    public final void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
                                              BeanDefinitionRegistry beanDefinitionRegistry) {
       /* // 0.将主数据源添加到数据源集合中
        DataSourceSet.putTargetDataSourceMap(DataSourceContants.MAINDATASOURCE, defaultTargetDataSource);
        //1.创建DataSourceBean
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        //spring名称约定为defaultTargetDataSource和targetDataSources
        mpv.addPropertyValue("defaultTargetDataSource", defaultTargetDataSource);
        mpv.addPropertyValue("targetDataSources", DataSourceSet.getTargetDataSourcesMap());
        beanDefinitionRegistry.registerBeanDefinition("dataSource", beanDefinition);*/
    }
}