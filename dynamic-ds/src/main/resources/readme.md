1. 操作之前需要执行初始化数据库脚本
2. 此包为动态切换数据源的jar包
3. 主要核心操作
```
数据源切换代码
com.pkk.database.dynamic.DynamicDataSource
    实现接口org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
        重写其中的方法determineCurrentLookupKey

在启动的时候操作类
com.pkk.database.config.DynamicDataSourceConfig
    配置启动默认使用的dataSource
    

数据源切换的策略类
com.pkk.database.strategy.DataSourceGetStrategy
实现DataSourceGetStrategy类来去选择(getDataSource)不同数据源的方式
    另一个包的com.pkk.dynamic.use.config.GetDataSource来去实现数据源的切换
    
    
主要是操作数据源的临时存放
com.pkk.database.utils.DataSourceSet
    这里面的变量维护当前要使用的数据源
            
            
```