USE beidou;

set names utf8;

drop table if exists user_tag;

CREATE TABLE IF NOT EXISTS `beidou`.`user_tag` (
  `userid` INT NOT NULL COMMENT '用户id',
  `user_tag_1` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '用户层级标注1，每3位代表一个标注，从低位开始',
  `user_tag_2` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '用户层级标注2，每3位代表一个标注，从低位开始',
  `user_tag_3` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '用户层级标注3，每3位代表一个标注，从低位开始',
  `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY `pk_user_tag` (`userid`)
  )ENGINE = InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT '用户层级标签';