
create table OPENMEMBER
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  MEMBERID     BIGINT,
  LOGINNAME  	varchar(40),
  SOURCE      varchar(40),
  CATEGORY  varchar(40),
  NICKNAME      varchar(24),
  HEADPIC		varchar(40),
  OTHERINFO		varchar(40),
  RELATEID		BIGINT,
  MOBILE		varchar(40),
  VALIDTIME		datetime,
  UNIONID		varchar(40)
);

create table OPERATORACTIVITY
(
  ID      BIGINT NOT NULL AUTO_INCREMENT primary key,
  SHOPID  BIGINT,
  OPERATORID BIGINT,
  OPERATORINFO VARCHAR(200),
  OPERATORSTATUS VARCHAR(50)
);