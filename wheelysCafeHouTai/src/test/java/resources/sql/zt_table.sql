CREATE TABLE `CAFESHOPPROFILE` (
	`SHOPID`  bigint(11) NOT NULL ,
	`NAME`  varchar(100) NULL ,
	`MOBILE`  varchar(11) NULL ,
	`TAKEAWAYSTATUS`  char(1) NULL ,
	`RESERVEDSTATUS`  char(1) NULL ,
	`DELSTATUS`  char(1) NULL ,
	`CREATETIME`  datetime NULL ,
	`UPDATETIME`  datetime NULL ,
	PRIMARY KEY (`SHOPID`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-------------
CREATE TABLE `EXPRESSADDRESS` (
`ID`  bigint(11) NOT NULL AUTO_INCREMENT,
`SHOPID`  bigint(11) NULL ,
`ADDRESS`  varchar(200) NULL ,
`CREATETIME`  datetime NULL ,
`UPDATETIME`  datetime NULL ,
PRIMARY KEY (`ID`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

----------------
CREATE TABLE `report_wheelysorder_member_shop_date` (
`ID`  bigint(11) NOT NULL AUTO_INCREMENT ,
`SHOPID`  bigint NULL ,
`SHOPNAME`  varchar(255) NULL ,
`DAY`  date NULL ,
`DAILYKEY`  varchar(255) NULL ,
`FIRSTORDERNUM`  int NULL COMMENT '�������׵�����',
`FIRSTORDERUSERNUM`  int NULL COMMENT '�������׵��û���',
`SUMDAILYORDERNUM`  int NULL COMMENT 'ÿ������ܹ���������',
`SUMDAILYORDERUSERNUM`  int NULL COMMENT 'ÿ������ܹ��µ����û���',
`NEWREGISTEREDMEMBERORDER`  int NULL COMMENT 'ÿ�������ע���û��µ���',
PRIMARY KEY (`ID`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARACTER SET=utf8;

------------------------
CREATE TABLE `WHEELYS_NEWS` (
`ID`  bigint(11) NOT NULL AUTO_INCREMENT ,
`TITLE`  varchar(255) NULL ,
`OVERVIEW`  varchar(255) NULL ,
`WRITER`  varchar(255) NULL ,
`NEWSPICTURE`  varchar(255) NULL ,
`CATEGORY`  varchar(255) NULL COMMENT '���·���' ,
`PUBLISHSTATUS`  varchar(255) NULL COMMENT '����״̬ Y/N' ,
`DELSTATUS`  varchar(255) NULL COMMENT 'ɾ��״̬' ,
`PUBLISHTIME`  datetime NULL COMMENT '����ʱ��' ,
`BEGINTIME`  datetime NULL COMMENT '��ʼʱ��' ,
`ENDTIME`  datetime NULL COMMENT '����ʱ��' ,
`CREATETIME`  datetime NULL ,
`UPDATETIME`  datetime NULL ,
PRIMARY KEY (`ID`)
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8;

------------------
CREATE TABLE `WHEELYS_NEWSCONTENT` (
`NEWSID`  bigint(11) NOT NULL ,
`NEWSCONTENT`  text NULL ,
PRIMARY KEY (`NEWSID`)
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 ;

------------------------------------
CREATE TABLE `WHEELYS_BANNER` (
`ID`  int NOT NULL AUTO_INCREMENT ,
`TITLE`  varchar(255) NULL ,
`TOURL`  varchar(255) NULL ,
`IMAGEURL`  varchar(255) NULL ,
`BANNERTYPE`  varchar(255) NULL ,
`SHOWSTATUS`  varchar(255) NULL ,
`DELSTATUS`  varchar(255) NULL ,
`BEGINTIME`  datetime NULL ,
`ENDTIME`  datetime NULL ,
`CREATETIME`  datetime NULL ,
`UPDATETIME`  datetime NULL ,
PRIMARY KEY (`ID`)
)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 ;


