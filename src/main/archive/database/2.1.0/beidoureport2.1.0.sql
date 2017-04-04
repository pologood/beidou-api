/*==============================================================*/
/*                                                              */
/* beidou-api v2.0.1(cpweb398)数据库建表SQL                            */
/*                                                              */
/*==============================================================*/

use beidoureport;
set names utf8;

drop table if exists api_accountfile;

/*==============================================================*/
/* Table: api_accountfile                                          */
/*==============================================================*/
CREATE TABLE `api_accountfile` (
`id` bigint(20) NOT NULL auto_increment,
`userid` int(10) NOT NULL COMMENT '被请求数据文件所属userid',
`planids` varchar(1500) NOT NULL default '' COMMENT '被请求数据文件所属planid，planid为多个时以逗号分隔',
`status` int(4)	NOT NULL default '1' COMMENT '任务处理状态：1: 未开始；2: 处理中；3: 完成; 4:失败; ',
`fileid` varchar(40)	NOT NULL COMMENT '数据文件id，给予请求者的token',
`md5` char(32)	NOT NULL default '' COMMENT '数据文件md5',
`machineid`	varchar(50) NOT NULL COMMENT '正在处理的机器hostname',
`format` tinyint(3) NOT NULL default '0' COMMENT '压缩文件格式, 0为gzip,1为zip格式',
`opuser` int(10) NOT NULL COMMENT '请求报告操作人userid',
`addtime` datetime NOT NULL COMMENT '请求时间',
`modtime` datetime NOT NULL COMMENT '程序处理当中的修改时间',
`retry`	int(10)	NOT NULL default '0' COMMENT '重试次数,暂未用,超时任务直接标记为失败',
PRIMARY KEY (`id`),
KEY `IN_APIACCOUNTFILE_USERID` (`userid`),
KEY `IN_APIACCOUNTFILE_FILEID` (`fileid`),
KEY `IN_APIACCOUNTFILE_STATUS` (`status`),
KEY `IN_APIACCOUNTFILE_USERID_FILEID` (`userid`,`fileid`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='API账户数据表' ;

