SET names utf8;

create database olap;

USE olap;
DROP TABLE IF EXISTS DailyGroupStats;
CREATE TABLE `DailyGroupStats` (
  `Date` date NOT NULL COMMENT '日期',
  `UserId` int(10) NOT NULL COMMENT '用户id',
  `PlanId` int(10) NOT NULL COMMENT '计划id',
  `GroupId` int(10) NOT NULL COMMENT '推广组id',
  `Search` bigint(20) NOT NULL COMMENT '展现',
  `Click` bigint(20) NOT NULL COMMENT '点击',
  `Cost` bigint(20) NOT NULL COMMENT '消费',
  KEY `userid_date_idx` (`UserId`,`Date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='推广组维度';


USE olap;
DROP TABLE IF EXISTS DailyGroupUvStats;
CREATE TABLE `DailyGroupUvStats` (
  `Date` date NOT NULL COMMENT '日期',
  `UserId` int(10) NOT NULL COMMENT '用户id',
  `PlanId` int(10) NOT NULL COMMENT '计划id',
  `GroupId` int(10) NOT NULL COMMENT '推广组id',
  `SrchUv` bigint(20) NOT NULL COMMENT '展现uv',
  `ClkUv` bigint(20) NOT NULL COMMENT '点击uv',
  `TransUv` bigint(20) NOT NULL COMMENT '消费uv',
  KEY `userid_date_idx` (`UserId`,`Date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='推广组Uv维度';


USE olap;
DROP TABLE IF EXISTS DailyGroupTransStats;
CREATE TABLE `DailyGroupTransStats` (
  `Date` date NOT NULL COMMENT '日期',
  `TransSiteId` int(10) NOT NULL COMMENT '网站id',
  `TransTargetId` int(10) NOT NULL COMMENT '转化id',
  `UserId` int(10) NOT NULL COMMENT '用户id',
  `PlanId` int(10) NOT NULL COMMENT '计划id',
  `GroupId` int(10) NOT NULL COMMENT '推广组id',
  `DirectTransCnt` bigint(20) NOT NULL COMMENT '直接转化',
  `IndirectTransCnt` bigint(20) NOT NULL COMMENT '间接转化',
  KEY `userid_date_idx` (`UserId`,`Date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='推广组维度';