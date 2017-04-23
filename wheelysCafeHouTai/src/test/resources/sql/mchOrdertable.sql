
create table MCHORDER
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  TRADENO     varchar(40),
  ORDERTITLE  varchar(40),
  MCHID      BIGINT,
  SHOPID 	 BIGINT,
  CITYCODE	varchar(10),
  CITYNAME  varchar(10),
  USENAME   varchar(40),
  MOBILE    varchar(20),
  TOTALFEE  int(11),
  DISCOUNT  int(11),
  PAYFEE    int(11),
  NETPAID   int(11),
  CREATETIME  datetime,
  UPDATETIME	datetime
);


create table MCHORDERDETAIL
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  ORDERID      	BIGINT,
  PRODUCTID  	BIGINT,
  PRICE         int(11),
  PRODUCTNAME   varchar(40),
  PRODUCTIMG	varchar(100),
  QUANTITY      int(11),
   TOTALFEE	    int(11),
   PAIDFEE   	int(11),
   DISCOUNT     int(11)
);

create table MCHORDERDETAIL
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  ORDERID      	BIGINT,
  PRODUCTID  	BIGINT,
  PRICE         int(11),
  PRODUCTNAME   varchar(40),
  PRODUCTIMG	varchar(100),
  QUANTITY      int(11),
   TOTALFEE	    int(11),
   PAIDFEE   	int(11),
   DISCOUNT     int(11)
);

create table SUPPLIER
(
  ID     			BIGINT NOT NULL AUTO_INCREMENT primary key,
  SUPPNAME    		varchar(50),
  SHOPID      		BIGINT,
  MCHPRODUCTID   	BIGINT,
  ORDERID        	BIGINT
);
create table NOTICEMANAGE
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  TRADENO     varchar(50),
  NOTICENAME  varchar(50),
  SHOPID 	 BIGINT,
  SHOPNAME	varchar(10),
  STATUS  varchar(20),
  BEGINTIME  datetime,
  ENDTIME  datetime
);
create table NOTICEDETAIL
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  NOTICEID     BIGINT,
  SHOPID       BIGINT,
  BEGINTIME    datetime,
  CLOSETIME    datetime,
  STATUS	datetime
);

create table BOOKING
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  SHOPID     BIGINT,
  REASON      varchar(1000),
  OPENTIME    datetime,
  CLOSETIME    datetime,
  STATUS	  varchar(20)
);

create table MCHRULES
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  CITYCODE     varchar(100),
  LESSFREIGHT  int(20),
  LESSCOST     int(20),
  MOREFREIGHT  int(20),
  MORECOST     int(20),
  TOTALFEE     int(20)
);

