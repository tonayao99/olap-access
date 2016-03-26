SET MODE MYSQL;

CREATE SCHEMA IF NOT EXISTS olap;
USE olap;

SET @MDATABASE_TO_UPPER=false;
CREATE ALIAS UNIX_TIMESTAMP FOR "com.baidu.unbiz.olap.utils.H2Support.getUnixTime";
CREATE ALIAS DATE FOR "com.baidu.unbiz.olap.utils.H2Support.getDate";


DROP TABLE IF EXISTS DailyGroupStats;
CREATE TABLE `DailyGroupStats` (
  `Date` date NOT NULL COMMENT '日期',
  `UserId` int(10) NOT NULL COMMENT '用户id',
  `PlanId` int(10) NOT NULL COMMENT '计划id',
  `GroupId` int(10) NOT NULL COMMENT '推广组id',
  `Search` bigint(20) NOT NULL COMMENT '展现',
  `Click` bigint(20) NOT NULL COMMENT '点击',
  `Cost` bigint(20) NOT NULL COMMENT '消费'
) COMMENT='推广组维度';


DROP TABLE IF EXISTS DailyGroupUvStats;
CREATE TABLE `DailyGroupUvStats` (
  `Date` date NOT NULL COMMENT '日期',
  `UserId` int(10) NOT NULL COMMENT '用户id',
  `PlanId` int(10) NOT NULL COMMENT '计划id',
  `GroupId` int(10) NOT NULL COMMENT '推广组id',
  `SrchUv` bigint(20) NOT NULL COMMENT '展现uv',
  `ClkUv` bigint(20) NOT NULL COMMENT '点击uv',
  `TransUv` bigint(20) NOT NULL COMMENT '消费uv'
) COMMENT='推广组Uv维度';


DROP TABLE IF EXISTS DailyGroupTransStats;
CREATE TABLE `DailyGroupTransStats` (
  `Date` date NOT NULL COMMENT '日期',
  `TransSiteId` int(10) NOT NULL COMMENT '网站id',
  `TransTargetId` int(10) NOT NULL COMMENT '转化id',
  `UserId` int(10) NOT NULL COMMENT '用户id',
  `PlanId` int(10) NOT NULL COMMENT '计划id',
  `GroupId` int(10) NOT NULL COMMENT '推广组id',
  `DirectTransCnt` bigint(20) NOT NULL COMMENT '直接转化',
  `IndirectTransCnt` bigint(20) NOT NULL COMMENT '间接转化'
) COMMENT='推广组维度';

DROP TABLE IF EXISTS DailyAdSizeStats;
CREATE TABLE `DailyAdSizeStats` (
  `Date` date NOT NULL,
  `DspId` int(10) NOT NULL COMMENT '用户 ID',
  `Adsize` bigint(20) NOT NULL,
  `CreativeSize` bigint(20) NOT NULL,
  `Search` bigint(20) NOT NULL,
  `Click` bigint(20) NOT NULL,
  `Cost` bigint(20) NOT NULL
) ;


DROP TABLE IF EXISTS DailyAdSizeUvStats;
CREATE TABLE `DailyAdSizeUvStats` (
  `Date` date NOT NULL,
  `DspId` int(10) NOT NULL COMMENT '用户 ID',
  `Adsize` bigint(20) NOT NULL,
  `CreativeSize` bigint(20) NOT NULL,
  `SearchUv` bigint(20) NOT NULL,
  `ClickUv` bigint(20) NOT NULL
) ;


DROP TABLE IF EXISTS HourlyAdSizeStats;
CREATE TABLE `HourlyAdSizeStats` (
  `Date` date NOT NULL,
  `Hour` bigint(20) NOT NULL,
  `DspId` int(10) NOT NULL COMMENT '用户 ID',
  `Adsize` bigint(20) NOT NULL,
  `CreativeSize` bigint(20) NOT NULL,
  `Search` bigint(20) NOT NULL,
  `Click` bigint(20) NOT NULL,
  `Cost` bigint(20) NOT NULL
) ;

DROP TABLE IF EXISTS dsp_site_stats;
CREATE TABLE `dsp_site_stats` (
  `Date` date NOT NULL,
  `userid` int(10) NOT NULL COMMENT '用户 ID',
  `siteid` bigint(20) NOT NULL,
  `sitename` varchar(20) NOT NULL,
  `pv` bigint(20) NOT NULL,
  `uv` bigint(20) NOT NULL
) ;

INSERT INTO DailyGroupStats values ('2015-7-13',8,1,1,9,8,700);
INSERT INTO DailyGroupStats values ('2015-7-14',8,1,2,19,18,1700);
INSERT INTO DailyGroupStats values ('2015-7-15',8,1,3,90,80,7000);

INSERT INTO DailyGroupUvStats values ('2015-7-13',8,1,1,9,8,7);
INSERT INTO DailyGroupUvStats values ('2015-7-14',8,1,2,19,18,17);
INSERT INTO DailyGroupUvStats values ('2015-7-15',8,1,3,90,80,70);

INSERT INTO DailyGroupTransStats values ('2015-7-13',1,1,8,1,1,9,8);
INSERT INTO DailyGroupTransStats values ('2015-7-14',1,1,8,1,2,19,18);
INSERT INTO DailyGroupTransStats values ('2015-7-15',1,1,8,1,3,90,80);

INSERT INTO DailyAdSizeStats values ('2015-7-13',8,1,1,1,9,8);
INSERT INTO DailyAdSizeStats values ('2015-7-14',8,1,1,2,19,18);
INSERT INTO DailyAdSizeStats values ('2015-7-15',8,1,1,3,90,80);

INSERT INTO DailyAdSizeUvStats values ('2015-7-13',8,1,1,1,1);
INSERT INTO DailyAdSizeUvStats values ('2015-7-14',8,1,1,1,2);
INSERT INTO DailyAdSizeUvStats values ('2015-7-15',8,1,1,1,3);

INSERT INTO HourlyAdSizeStats values ('2015-7-13',1,8,1,1,1,9,8);
INSERT INTO HourlyAdSizeStats values ('2015-7-14',1,8,1,1,2,19,18);
INSERT INTO HourlyAdSizeStats values ('2015-7-15',1,8,1,1,3,90,80);

INSERT INTO dsp_site_stats values ('2015-7-13',1,1,'abc.com',1,9);
INSERT INTO dsp_site_stats values ('2015-7-14',1,1,'cc.com',2,19);
INSERT INTO dsp_site_stats values ('2015-7-15',1,1,'bba.com',3,90);