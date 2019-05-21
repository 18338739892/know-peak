package com.pkk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 日志的控制器
 * @author: peikunkun
 * @create: 2019-05-20 13:31
 **/
@Slf4j
@RestController
@RequestMapping("log")
public class LogController {

  @RequestMapping("getLogger")
  public void getLogger() {
    final String baseLog =
        this.getClass().getSimpleName() + "-" + Thread.currentThread().getStackTrace()[1].getMethodName() + "-";
    log.debug(baseLog + "debug");
    log.info(baseLog + "info");
    log.warn(baseLog + "warn");
    log.error(baseLog + "error");
    log.trace(baseLog + "trace");
  }


}
