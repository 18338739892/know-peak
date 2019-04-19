package com.pkk.peakrabbitmq.config;

import java.io.Serializable;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: mq的属性配置
 * @author: peikunkun
 * @create: 2019-04-18 15:25
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class PeakRabbitProperties implements Serializable {

  private static final long serialVersionUID = -6829172645331304764L;


}
