
CREATE TABLE JOBLOCK
(
  JOBNAME character varying(80) NOT NULL,
  FIRETIME character varying(20),
  IP character varying(15),
  STATUS character varying(10),
  NEXTFIRE character varying(20) NOT NULL,
  CONSTRAINT pk_joblock PRIMARY KEY (JOBNAME , NEXTFIRE)
);

create table APP_USER(
  ID              bigint not null,
  USERNAME        VARCHAR(100) not null,
  DISPLAYNAME     VARCHAR(100),
  PASSWORD        VARCHAR(50),
  ACCOUNT_ENABLED CHAR(1),
  CITYCODE        VARCHAR(200),
  MOBILE          VARCHAR(11),
  ROLENAMES       VARCHAR(2000),
  usertype        VARCHAR(10)
);

alter table APP_USER add constraint PK_USER_ID primary key (ID);
alter table APP_USER add constraint UK_USER_NAME unique (USERNAME);

create table ROLE
(
  ID          bigint not null  AUTO_INCREMENT primary key ,
  NAME        VARCHAR(20) not null,
  DESCRIPTION VARCHAR(64),
  TAG         VARCHAR(20)
);

alter table ROLE add constraint UK_ROLENAME unique (NAME);

create table USER_ROLE
(
  USER_ID bigint not null,
  ROLE_ID bigint not null,
  ID      bigint not null AUTO_INCREMENT primary key
);
alter table USER_ROLE
  add constraint UK_USER_ROLE UNIQUE (USER_ID, ROLE_ID);

create table WEBMODULE
(
  RECORDID   bigint not null AUTO_INCREMENT primary key,
  MODULEURL  VARCHAR(1000),
  MATCHORDER integer not null,
  MENUCODE   VARCHAR(20) not null,
  MENUTITLE  VARCHAR(50) not null,
  TARGET     VARCHAR(10),
  DISPLAY    VARCHAR(10) not null,
  TAG        VARCHAR(3) not null,
  ROLENAMES  VARCHAR(2000)
);

create table WEBMODULE_ROLE
(
  MODULE_ID bigint not null,
  ROLE_ID   bigint not null,
  ID        bigint not null AUTO_INCREMENT primary key
);
alter table WEBMODULE_ROLE
  add constraint UK_WEBMODULEROLE UNIQUE (MODULE_ID, ROLE_ID);

create table DICTIONARY
(
  TABLE_NAME    VARCHAR(50) not null,
  COLUMN_NAME   VARCHAR(50) not null,
  OBJ_NAME      VARCHAR(50) not null,
  PROPERTY_NAME VARCHAR(50) not null,
  CN_ALIAS      VARCHAR(20),
  DATATYPE      VARCHAR(20),
  DATALENGTH    VARCHAR(10),
  NULLABLE      VARCHAR(5),
  DESCRIPTION   VARCHAR(500),
  OTHERINFO     VARCHAR(1000),
  FORMULA       VARCHAR(100)
);
alter table DICTIONARY
  add constraint PK_DICTIONARY primary key (TABLE_NAME, COLUMN_NAME);

create unique index IDXPK_DICTIONARY on DICTIONARY (TABLE_NAME, COLUMN_NAME);

create table GEWACONFIG
(
  CKEY        VARCHAR(15) not null,
  DESCRIPTION VARCHAR(100) not null,
  UPDATETIME  DATETIME not null,
  CONTENT     VARCHAR(2000)
);
alter table GEWACONFIG add constraint PK_GEWACONFIG primary key (CKEY);

create table USER_OPERATION
(
  OPKEY      VARCHAR(200) not null,
  TAG        VARCHAR(20),
  ADDTIME    DATETIME,
  UPDATETIME DATETIME,
  OPNUM      integer,
  VALIDTIME  DATETIME,
  REFUSED    bigint
);
alter table USER_OPERATION
  add constraint PK_USER_OPERATION primary key (OPKEY);

insert into GEWACONFIG (ckey,description,updatetime,content) values('cacheVersion', 'cache version', current_timestamp, '{"oneMin":"1v1","tenMin":"10v1","halfHour":"30v1","oneHour":"60v1","twoHour":"2hv1","halfDay":"12hv1","loginKey":"lkv1","loginAuth":"lav1"}');
insert into GEWACONFIG (ckey,description,updatetime,content) values('cacheVerTest', 'cache version test', current_timestamp, '{"oneMin":"t1v1","tenMin":"t10v1","halfHour":"t30v1","oneHour":"t60v1","twoHour":"t2hv1","halfDay":"t12hv1","loginKey":"tlkv1","loginAuth":"tlav1"}');


insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (1, 'tt260', 32, '32', '权限管理', 'mainFrame', 'Y', 'G', 'accountManager');
insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (2, '/admin/acl/userList.xhtml', 3202, '3202', '用户管理', '_blank', 'Y', 'G', 'accountManager');
insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (3, '/admin/acl/moduleList.xhtml?tag=G', 3203, '3203', '功能模块[Gewa]', 'mainFrame', 'Y', 'G', 'accountManager');
insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (4, '/admin/acl/moduleList.xhtml?tag=GP', 3204, '3204', '功能模块[Partner]', 'mainFrame', 'Y', 'G', 'accountManager');
insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (5, '/admin/acl/moduleList.xhtml?tag=A', 3205, '3205', '功能模块[API]', 'mainFrame', 'Y', 'G', 'accountManager');
insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (6, '/admin/acl/ROLEList.xhtml?tag=G', 3206, '3206', '角色管理[Gewa]', 'mainFrame', 'Y', 'G', 'accountManager');
insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (7, '/admin/acl/ROLEList.xhtml?tag=GP', 3207, '3207', '角色管理[Partner]', 'mainFrame', 'Y', 'G', '');
insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (8, '/admin/acl/ROLEList.xhtml?tag=A', 3208, '3208', '角色管理[API]', 'mainFrame', 'Y', 'G', '');
insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (9, '/admin/acl/reloadAcl.xhtml', 3210, '3210', '刷新权限', 'mainFrame', 'Y', 'G', 'accountManager');
insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (10, '/admin/acl/*', 3299, '3299', '权限必须', '', 'N', 'G', 'accountManager');

insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (11, 'admin260', 99, '99', '登录拦截', 'mainFrame', 'N', 'G', 'admin');
insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (12, '/admin/**/*', 9999, '9999', '权限必须', '', 'N', 'G', 'admin');


insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (13, 'member260', 66, '66', '用户登录拦截', 'mainFrame', 'N', 'G', 'member');
insert into WEBMODULE (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (14, '/order/**/*', 6666, '6666', '用户权限必须', '', 'N', 'G', 'member');

insert into ROLE (ID, NAME, DESCRIPTION, TAG)
values (1, 'accountManager', '账户管理', 'G');

insert into ROLE (ID, NAME, DESCRIPTION, TAG)
values (2, 'admin', '超级用户', 'G');

insert into ROLE (ID, NAME, DESCRIPTION, TAG)
values (3, 'member', '用户', 'G');

insert into APP_USER (ID, USERNAME, DISPLAYNAME, PASSWORD, ACCOUNT_ENABLED, CITYCODE, MOBILE, ROLENAMES)
values (1, 'admin', 'adminUser', 'cc03e747a6afbbcbf8be7668acfebee5', 'Y', '310000,330100,330200,330400,330600,330700,331200,320600,440100,440300,440400,440600,441900,442000', '', 'admin,accountManager');

insert into USER_ROLE (USER_ID, ROLE_ID, ID) values (1, 1, 1);
insert into USER_ROLE (USER_ID, ROLE_ID, ID) values (1, 2, 2);

insert into WEBMODULE_ROLE (MODULE_ID, ROLE_ID, ID) values (1, 1, 1);
insert into WEBMODULE_ROLE (MODULE_ID, ROLE_ID, ID) values (2, 1, 2);
insert into WEBMODULE_ROLE (MODULE_ID, ROLE_ID, ID) values (3, 1, 3);
insert into WEBMODULE_ROLE (MODULE_ID, ROLE_ID, ID) values (4, 1, 4);
insert into WEBMODULE_ROLE (MODULE_ID, ROLE_ID, ID) values (5, 1, 5);
insert into WEBMODULE_ROLE (MODULE_ID, ROLE_ID, ID) values (6, 1, 6);
insert into WEBMODULE_ROLE (MODULE_ID, ROLE_ID, ID) values (7, 1, 7);
insert into WEBMODULE_ROLE (MODULE_ID, ROLE_ID, ID) values (8, 1, 8);
insert into WEBMODULE_ROLE (MODULE_ID, ROLE_ID, ID) values (9, 1, 9);
insert into WEBMODULE_ROLE (MODULE_ID, ROLE_ID, ID) values (10, 1, 10);

insert into WEBMODULE_ROLE (MODULE_ID, ROLE_ID, ID) values (11, 2, 11);
insert into WEBMODULE_ROLE (MODULE_ID, ROLE_ID, ID) values (12, 2, 12);


create table ELECCARD
(
  id   bigint not null AUTO_INCREMENT primary key,
  ebatchid  bigint,
  status   	VARCHAR(20) not null not null,
  memberid   bigint,
  mobile  	VARCHAR(50),
  orderid   bigint,
  version   int(20),
  begintime datetime,
  endtime  	datetime,
  remark     VARCHAR(255),
  bindtime   datetime,
  useamount  int(20)
);


create table CARDCOUPONSORDER
(
  ID   bigint not null AUTO_INCREMENT primary key,
  VERSION      int(20),
  ORDERTITLE   VARCHAR(20),
  TRADENO      VARCHAR(100),
  MOBILE  	   VARCHAR(50),
  CREATETIME   datetime,
  PAIDTIME     datetime,
  VALIDTIME    datetime
  UPDATETIME   datetime
  STATUS       VARCHAR(50),
  PAYSTATUS    VARCHAR(50),
  USEAMOUNT    int(20),
  RESTATUS     VARCHAR(20),
  MEMBERID     bigint,
  MEMBERNAME   VARCHAR(100),
  PAYSEQNO     VARCHAR(100),
  PAYFEE       int(100),
  NETPAID      int(100),
  QUANTITY     int(100),
  CITYCODE     VARCHAR(100),
  CATEGORY     VARCHAR(50),
  GATEWATCODE  VARCHAR(100),
  MERCHANTCODE VARCHAR(100),
  PAYMETHOD    VARCHAR(50),
  ORIGIN       VARCHAR(50),
  OTHERINFO    VARCHAR(200),
  DESCRIPTION  VARCHAR(200),
  REMARK       VARCHAR(200),
  UKEY         VARCHAR(50)
);