CREATE TABLE `CAFEITEM` (
  `RECORDID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `NAME` varchar(11) NOT NULL COMMENT '类别名称',
  `STATE` varchar(20) DEFAULT NULL COMMENT '是否显示状态Y/N',
  `SORTKEY` int(11) DEFAULT NULL COMMENT '排序key',
  `CREATETIME` DATETIME DEFAULT NULL COMMENT '创建时间',
  `UPDATETIME` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`recordid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
alter table CAFEITEM
  add constraint UK_CAFEITEM_NAME UNIQUE (NAME);


-------------------------------------------------------------------------
CREATE TABLE `WHEELYS_CAFEORDER` (
  `ID` bigint NOT NULL AUTO_INCREMENT primary key,
  `ORDER_VERSION` int(11),
  `ORDERTITLE` varchar(50),
  `TRADENO` varchar(30),
  `MOBILE` varchar(11),
  `CREATETIME` DATETIME,
  `PAIDTIME` DATETIME,
  `FINISHTIME` DATETIME,
  ORDERTYPE varchar(10),
  `TAKETIME` DATETIME,
  `VALIDTIME` DATETIME,
  `UPDATETIME` DATETIME,
  `STATUS` varchar(10),
  `PAYSTATUS` varchar(10),
  `RESTATUS` varchar(10),
  `MEMBERID` bigint ,
  `MEMBERNAME` varchar(30),
  `PAYSEQNO` varchar(50),
  `TOTALFEE` int(11),
  `PAYFEE` int(11),
  `NETPAID` int(11),
  `QUANTITY` int(11),
  `UKEY` varchar(50),
  `TAKEKEY` varchar(50),
  `OTHERINFO` varchar(255),
  `CITYCODE` varchar(10),
  `PRICATEGORY` varchar(10),
  `CATEGORY` varchar(10),
  `PREPAY` varchar(10),
  `SHOPID` bigint,
  `GATEWAYCODE` varchar(30) ,
  `MERCHANTCODE` varchar(30),
  `PAYMETHOD` varchar(30),
  `ORIGIN` varchar(30),
  `DESCRIPTION` varchar(255),
  `REMARK` varchar(255),
  `DISCOUNT` bigint,
  `DISREASON` varchar(255),
  `SDID` bigint 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

alter table WHEELYS_CAFEORDER
  add constraint UK_WHEELYS_CAFEORDER_TRADENO UNIQUE (TRADENO);

---------------------------------------------------------------------

create table OPENMEMBER
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  MEMBERID     BIGINT,
  LOGINNAME    varchar(50),
  SOURCE varchar(20),
  CATEGORY      varchar(10),
  NICKNAME  varchar(30),
  HEADPIC    varchar(200),
  OTHERINFO  varchar(100),
  RELATEID   BIGINT,
  MOBILE     varchar(11),
  VALIDTIME  datetime,
  UNIONID    varchar(50)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
alter table OPENMEMBER
  add constraint UK_OPENMEMBER_LOGINNAME UNIQUE (LOGINNAME);


---------------------------------------------------------------------
		create table WHEELYSMEMBER
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  NICKNAME     varchar(30),
  EMAIL    varchar(30),
  PASSWORD varchar(32),
  MOBILE      varchar(11),
  REJECTED  varchar(10),
  BINDSTATUS    varchar(10),
  PRIKEY  varchar(20),
  NEEDVALID    varchar(10),
  LASTLOGINTIME    BIGINT,
  HEADPIC  varchar(100),
  ADDTIME    datetime,
  IP           varchar(30)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-------------------------------------------------
create table WHEELYSORDERDETAIL
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  ORDERID     BIGINT,
  PRODUCTID   BIGINT,
  QUANTITY  int(11),
  PRICE      int(11),
  PRODUCTNAME  varchar(30),
  PRODUCTENAME  varchar(30),
  PRODUCTIMG    varchar(100),
  TOTALFEE  int(11),
  DESCRIPTION    varchar(60),
  DISCOUNT   int(11),
  PAIDFEE  int(11)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


create table CARTPRODUCT
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  PRODUCTID     BIGINT,
  QUANTITY  int(11),
  PRICE      int(11),
  UKEY  varchar(50),
  PKEY  varchar(50),
  TOTALFEE  int(11),
  OTHERINFO    varchar(60),
  CREATETIME   datetime
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

alter table CARTPRODUCT
  add constraint UK_UKEY_PKEY UNIQUE (UKEY, PKEY);
  
  
create table DISCOUNTACTIVITY
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  NAME		varchar(50),
  DTYPE  	varchar(20),
  PRICE      int(11),
  STATUS  varchar(10),
  BEGINTIME   datetime,
  ENDTIME   datetime,
  CREATETIME   datetime,
  UPDATETIME   datetime
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
	