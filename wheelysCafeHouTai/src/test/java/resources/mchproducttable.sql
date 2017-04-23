
create table MCHPRODUCTITEM
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  NAME     varchar(40),
  STATUS  	varchar(10),
  SORTKEY      int(11),
  CREATETIME  datetime,
  UPDATETIME	datetime
);


create table MCHPRODUCT
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  UKEY      int(11),
  NAME  	varchar(40),
  ITEMID      BIGINT,
  UNIT      varchar(40),
  WEIGHT	int(11),
  DESCRIPTION  varchar(40),
   PRICE	int(11),
   STATUS    varchar(10),
   DELSTATUS  varchar(10),
  	SORTKEY   int(11),
  CREATETIME  datetime,
  UPDATETIME	datetime
);

