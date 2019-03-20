-- 初始化表
CREATE TABLE `bi_data_source` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `source_name` varchar(50) NOT NULL COMMENT '数据源名称',
  `db_ip` varchar(64) NOT NULL COMMENT '数据库的ip地址',
  `db_port` varchar(10) NOT NULL COMMENT '数据库的端口',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `db_name` varchar(50) NOT NULL COMMENT 'db名称',
  `parameters` varchar(255) DEFAULT NULL COMMENT '额外参数追加在URL后面的参数',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 否  -1 是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `del_time` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_db_name` (`db_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='数据源配置表';