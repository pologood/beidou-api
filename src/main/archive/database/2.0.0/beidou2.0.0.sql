/*==============================================================*/
/*                                                              */
/* beidou-api v2(cpweb357)数据库建表SQL                         */
/*                                                              */
/*==============================================================*/

use beidou;
set names utf8;

drop table if exists api_blacklist;

/*==============================================================*/
/* Table: api_blacklist                                         */
/*==============================================================*/
CREATE TABLE `api_blacklist` (
`userid` int(10) NOT NULL COMMENT '被禁止访问api的黑名单用户id'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='api黑名单用户表' 