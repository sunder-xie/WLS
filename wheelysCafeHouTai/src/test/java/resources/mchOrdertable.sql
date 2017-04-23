
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


¡¡