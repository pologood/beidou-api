/*==============================================================*/
/*                                                              */
/* beidou-api v2(cpweb357)数据库建表SQL                            */
/*                                                              */
/*==============================================================*/

use beidoureport;
set names utf8;

drop table if exists api_report;

/*==============================================================*/
/* Table: api_report                                          */
/*==============================================================*/
CREATE TABLE `api_report` (
`id` bigint(20) NOT NULL auto_increment,
`queryparam` varchar(2048) NOT NULL COMMENT '提交报告的查询JSON串',
`status` int(4)	NOT NULL default '1' COMMENT '任务处理状态：1: 未开始；2: 处理中；3: 完成; 4XX:失败; 5: 查询范围过大失败; ',
`userid`  int(10) NOT NULL COMMENT '被请求报告所属userid',
`reportid` varchar(100)	NOT NULL COMMENT '报告id，给予请求者的token',
`machineid`	varchar(50) NOT NULL COMMENT '正在处理的机器host',
`performancedata`  int(10) NOT NULL default '63' COMMENT '显示哪些绩效数据，包括展现、点击、消费、CTR、CPM、ACP，默认63的二进制值为111111',
`iszip` tinyint(3) NOT NULL default '0' COMMENT '是否是压缩过到报告文件, 0为zip,1为csv格式',
`opuser` int(10) NOT NULL COMMENT '请求报告操作人userid',
`addtime` datetime NOT NULL COMMENT '请求时间',
`modtime` datetime NOT NULL COMMENT '程序处理当中的修改时间',
`retry`	int(10)	NOT NULL default '0' COMMENT '重试次数',
PRIMARY KEY (`id`),
KEY `IN_APIREPORT_STATUS_REPORTID` (`reportid`),
KEY `IN_APIREPORT_STATUS_MACHINEID` (`status`,`machineid`),
KEY `IN_APIREPORT_USERID_STATUS_MACHINEID` (`userid`,`status`,`machineid`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='API报告表' ;