SET names utf8;

create database olap;

USE olap;
DROP TABLE IF EXISTS DailyAdSizeStats;
CREATE TABLE `DailyAdSizeStats` (
  `Date` date NOT NULL,
  `DspId` int(10) NOT NULL COMMENT '用户 ID',
  `Adsize` bigint(20) NOT NULL,
  `CreativeSize` bigint(20) NOT NULL,
  `Search` bigint(20) NOT NULL,
  `Click` bigint(20) NOT NULL,
  `Cost` bigint(20) NOT NULL,
  KEY `userid_date_idx` (`DspId`,`Date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


USE olap;
DROP TABLE IF EXISTS DailyAdSizeUvStats;
CREATE TABLE `DailyAdSizeUvStats` (
  `Date` date NOT NULL,
  `DspId` int(10) NOT NULL COMMENT '用户 ID',
  `Adsize` bigint(20) NOT NULL,
  `CreativeSize` bigint(20) NOT NULL,
  `SearchUv` bigint(20) NOT NULL,
  `ClickUv` bigint(20) NOT NULL,
  KEY `userid_date_idx` (`DspId`,`Date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


USE olap;
DROP TABLE IF EXISTS HourlyAdSizeStats;
CREATE TABLE `HourlyAdSizeStats` (
  `Date` date NOT NULL,
  `Hour` bigint(20) NOT NULL,
  `DspId` int(10) NOT NULL COMMENT '用户 ID',
  `Adsize` bigint(20) NOT NULL,
  `CreativeSize` bigint(20) NOT NULL,
  `Search` bigint(20) NOT NULL,
  `Click` bigint(20) NOT NULL,
  `Cost` bigint(20) NOT NULL,
  KEY `userid_date_idx` (`DspId`,`Date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;