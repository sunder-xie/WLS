CREATE TABLE `report_orderwheelys` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shopid` bigint(20) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `paymethod` varchar(20) DEFAULT NULL,
  `shopname` varchar(20) DEFAULT NULL,
  `tnum` int(11) DEFAULT NULL,
  `tmoney` int(11) DEFAULT NULL,
  `tavermoney` int(11) DEFAULT NULL,
  `avnum` int(11) DEFAULT NULL,
  `avmoney` int(11) DEFAULT NULL,
  `avemoney` int(11) DEFAULT NULL,
  `numdan` int(11) DEFAULT NULL,
  `ukey` varchar(50) NOT NULL,
  `discount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ukey` (`ukey`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;