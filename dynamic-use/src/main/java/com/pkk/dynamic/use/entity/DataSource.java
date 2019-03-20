package com.pkk.dynamic.use.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSource implements Serializable {
    private static final long serialVersionUID = -3960311279336176151L;

    private Long id;

    private String sourceName;

    private String dbPort;

    private String dbIp;

    private String parameters;

    private String username;

    private String password;

    private String dbName;

    private Boolean delFlag;

    private Date createTime;

    private Date delTime;
}