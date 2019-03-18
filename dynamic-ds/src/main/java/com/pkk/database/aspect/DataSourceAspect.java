package com.pkk.database.aspect;

import com.pkk.database.annotation.ActivateDataSource;
import com.pkk.database.utils.SpelParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description: 切换数据源的切面编程
 * @author: peikunkun
 * @create: 2019-03-18 17:16
 **/

@Slf4j
@Aspect
@Order(-88)// AOP在@Transactional之前执行，否则会事物执行失败
@Component
public class DataSourceAspect {


    /**
     * @Description: 切换数据源的操作
     * @Param: [joinPoint, activateDataSource]
     * @return: void
     * @Author: peikunkun
     * @Date: 2019/3/18 0018 下午 5:34
     */
    @Before("@annotation(activateDataSource)")
    public void activateDataSource(JoinPoint joinPoint, ActivateDataSource activateDataSource) {
        //获取连接点的方法签名对象；
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取连接点的方法
        Method method = signature.getMethod();
        //获取参数
        Object[] args = joinPoint.getArgs();

        //获取数据源的key[解析参数]
        String dataSourceKey = this.getDynamicDataSourceName(args, method, activateDataSource);


    }

    /**
     * <p>Title: getDynamicDataSourceName<／p>
     * <p>Description: 获取数据源的key解析参数<／p>
     * <p>Copyright: Copyright (c) 2018<／p>
     *
     * @author pkk
     * @date 2019/3/18 0018
     * @version 1.0
     */
    private String getDynamicDataSourceName(Object[] args, Method method, ActivateDataSource activateDataSource) {
        if (StringUtils.isEmpty(activateDataSource.spel())) {
            log.info("place specified datasource spel value");
            return null;
        }

        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        String key = SpelParseUtil.parserKey(args, parameterNames, activateDataSource.spel());
        if (StringUtils.isEmpty(key)) {
            log.debug("The activateDataSource is use,but the spel is null!");
            return null;
        }
        return key;
    }


}
