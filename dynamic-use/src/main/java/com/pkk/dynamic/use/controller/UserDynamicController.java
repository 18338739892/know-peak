package com.pkk.dynamic.use.controller;

import com.pkk.dynamic.use.entity.DataSource;
import com.pkk.dynamic.use.service.DataSourceService;
import com.pkk.dynamic.use.vo.DataSourceChangeReqVo;
import com.pkk.dynamic.use.vo.DataSourceReqVo;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: 测试动态数据源
 * @author: peikunkun
 * @create: 2019-03-20 16:00
 **/
@Slf4j
@RequestMapping("userDynamic")
@RestController
public class UserDynamicController {

    @Resource
    private DataSourceService dataSourceService;


    /**
     * @Description: 获取数据源的信息
     * @Param: [dataSourceReqVo]
     * @return: void
     * @Author: peikunkun
     * @Date: 2019/3/20 0020 下午 4:24
     */
    @RequestMapping("findDataSource")
    public DataSource findDataSource(DataSourceReqVo dataSourceReqVo) {
        if (null == dataSourceReqVo || StringUtils.isBlank(dataSourceReqVo.getDbName())) {
            return new DataSource();
        }
        return dataSourceService.selectDbInfoByDbName(dataSourceReqVo.getDbName());
    }

    /**
     * @Description: 获取数据源的信息
     * @Param: [dataSourceReqVo]
     * @return: void
     * @Author: peikunkun
     * @Date: 2019/3/20 0020 下午 4:24
     */
    @RequestMapping("changeDataSourceByDbName")
    public Object changeDataSourceByDbName(DataSourceChangeReqVo dataSourceChangeReqVo) {
        if (null == dataSourceChangeReqVo || StringUtils.isBlank(dataSourceChangeReqVo.getDbName())) {
            return new DataSource();
        }
        List<Object> objects = dataSourceService.selectDbInfoByChangeDbName(dataSourceChangeReqVo);
        return objects;
    }


}
