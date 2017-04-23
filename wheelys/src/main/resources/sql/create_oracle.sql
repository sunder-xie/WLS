-- Create table
CREATE TABLE joblock
(
  jobname varchar2(80) NOT NULL,
  firetime varchar2(20),
  ip varchar2(15),
  status varchar2(10),
  nextfire varchar2(20) NOT NULL,
  CONSTRAINT pk_joblock PRIMARY KEY (jobname , nextfire)
);

create table APP_USER(
  ID              NUMBER(19) not null,
  USERNAME        VARCHAR2(100) not null,
  DISPLAYNAME     VARCHAR2(100),
  PASSWORD        VARCHAR2(50),
  ACCOUNT_ENABLED CHAR(1),
  CITYCODE        VARCHAR2(200),
  MOBILE          VARCHAR2(11),
  ROLENAMES       VARCHAR2(2000),
  usertype        VARCHAR2(10)
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table APP_USER add constraint PK_USER_ID primary key (ID);
alter table APP_USER add constraint UK_USER_NAME unique (USERNAME);
--  using index 
--  tablespace --WEBDATA

create table ROLE
(
  ID          NUMBER(19) not null,
  NAME        VARCHAR2(20) not null,
  DESCRIPTION VARCHAR2(64),
  TAG         VARCHAR2(20)
);
alter table ROLE
  add constraint PK_ROLE primary key (ID);
--  using index 
--  tablespace WEBDATA;

alter table ROLE
  add constraint UK_ROLENAME unique (NAME);
--  using index 
--  tablespace WEBDATA

create table USER_ROLE
(
  USER_ID NUMBER(19) not null,
  ROLE_ID NUMBER(19) not null,
  ID      NUMBER(19) not null
);
alter table USER_ROLE
  add constraint UK_USER_ROLE primary key (USER_ID, ROLE_ID);
--  using index 
--  tablespace WEBDATA;
create table WEBMODULE
(
  RECORDID   NUMBER(19) not null,
  MODULEURL  VARCHAR2(1000),
  MATCHORDER NUMBER(4) not null,
  MENUCODE   VARCHAR2(20) not null,
  MENUTITLE  VARCHAR2(50) not null,
  TARGET     VARCHAR2(10),
  DISPLAY    VARCHAR2(10) not null,
  TAG        VARCHAR2(3) not null,
  ROLENAMES  VARCHAR2(2000)
);
alter table WEBMODULE
  add constraint PK_WEBMODULE primary key (RECORDID);
--  using index 
--  tablespace WEBDATA;
create table WEBMODULE_ROLE
(
  MODULE_ID NUMBER(19) not null,
  ROLE_ID   NUMBER(19) not null,
  ID        NUMBER(19) not null
);
alter table WEBMODULE_ROLE
  add constraint PK_WEBMODULEROLE primary key (MODULE_ID, ROLE_ID);
--  using index 
--  tablespace WEBDATA

create table DICTIONARY
(
  TABLE_NAME    VARCHAR2(50) not null,
  COLUMN_NAME   VARCHAR2(50) not null,
  OBJ_NAME      VARCHAR2(50) not null,
  PROPERTY_NAME VARCHAR2(50) not null,
  CN_ALIAS      VARCHAR2(20),
  DATATYPE      VARCHAR2(20),
  DATALENGTH    VARCHAR2(10),
  NULLABLE      VARCHAR2(5),
  DESCRIPTION   VARCHAR2(500),
  OTHERINFO     VARCHAR2(1000),
  FORMULA       VARCHAR2(100)
);
alter table DICTIONARY
  add constraint PK_DICTIONARY primary key (TABLE_NAME, COLUMN_NAME);
-- Create/Recreate indexes 
create unique index IDXPK_DICTIONARY on DICTIONARY (TABLE_NAME, COLUMN_NAME);
--  tablespace TBS_INDEX

create table GEWACONFIG
(
  CKEY        VARCHAR2(15) not null,
  DESCRIPTION VARCHAR2(100) not null,
  UPDATETIME  TIMESTAMP(6) not null,
  CONTENT     VARCHAR2(2000)
);
alter table GEWACONFIG
  add constraint PK_GEWACONFIG primary key (CKEY);
--  using index 
--  tablespace WEBDATA
create table USER_OPERATION
(
  OPKEY      VARCHAR2(200) not null,
  TAG        VARCHAR2(20),
  ADDTIME    TIMESTAMP(6),
  UPDATETIME TIMESTAMP(6),
  OPNUM      NUMBER(10),
  VALIDTIME  TIMESTAMP(6),
  REFUSED    NUMBER(19)
);
alter table USER_OPERATION
  add constraint PK_USER_OPERATION primary key (OPKEY);
--  using index 
--  tablespace WEBDATA
-------------------------------------------------------------------------
insert into gewaconfig (ckey,description,updatetime,content) values('cacheVersion', 'cache version', sysdate, '{"oneMin":"1v1","tenMin":"10v1","halfHour":"30v1","oneHour":"60v1","twoHour":"2hv1","halfDay":"12hv1","loginKey":"lkv1","loginAuth":"lav1"}');
insert into gewaconfig (ckey,description,updatetime,content) values('cacheVerTest', 'cache version test', sysdate, '{"oneMin":"t1v1","tenMin":"t10v1","halfHour":"t30v1","oneHour":"t60v1","twoHour":"t2hv1","halfDay":"t12hv1","loginKey":"tlkv1","loginAuth":"tlav1"}');

---------modules---------------------------------------------------------
insert into webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (1, 'tt260', 32, '32', '权限管理', 'mainFrame', 'Y', 'G', 'accountManager');
insert into webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (2, '/admin/acl/userList.xhtml', 3202, '3202', '用户管理', '_blank', 'Y', 'G', 'accountManager');
insert into webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (3, '/admin/acl/moduleList.xhtml?tag=G', 3203, '3203', '功能模块[Gewa]', 'mainFrame', 'Y', 'G', 'accountManager');
insert into webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (4, '/admin/acl/moduleList.xhtml?tag=GP', 3204, '3204', '功能模块[Partner]', 'mainFrame', 'Y', 'G', 'accountManager');
insert into webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (5, '/admin/acl/moduleList.xhtml?tag=A', 3205, '3205', '功能模块[API]', 'mainFrame', 'Y', 'G', 'accountManager');
insert into webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (6, '/admin/acl/roleList.xhtml?tag=G', 3206, '3206', '角色管理[Gewa]', 'mainFrame', 'Y', 'G', 'accountManager');
insert into webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (7, '/admin/acl/roleList.xhtml?tag=GP', 3207, '3207', '角色管理[Partner]', 'mainFrame', 'Y', 'G', '');
insert into webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (8, '/admin/acl/roleList.xhtml?tag=A', 3208, '3208', '角色管理[API]', 'mainFrame', 'Y', 'G', '');
insert into webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (9, '/admin/acl/reloadAcl.xhtml', 3210, '3210', '刷新权限', 'mainFrame', 'Y', 'G', 'accountManager');
insert into webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (10, '/admin/acl/*', 3299, '3299', '权限必须', '', 'N', 'G', 'accountManager');

insert into webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (11, 'admin260', 99, '99', '登录拦截', 'mainFrame', 'N', 'G', 'admin');
insert into webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (12, '/admin/**/*', 9999, '9999', '权限必须', '', 'N', 'G', 'admin');

---------roles--------------------------------------------------------------
insert into role (ID, NAME, DESCRIPTION, TAG)
values (1, 'accountManager', '账户管理', 'G');

insert into role (ID, NAME, DESCRIPTION, TAG)
values (2, 'admin', '超级用户', 'G');
---------users--------------------------------------------------------------
insert into app_user (ID, USERNAME, DISPLAYNAME, PASSWORD, ACCOUNT_ENABLED, CITYCODE, MOBILE, ROLENAMES)
values (1, 'admin', 'adminUser', 'cc03e747a6afbbcbf8be7668acfebee5', 'Y', '310000,330100,330200,330400,330600,330700,331200,320600,440100,440300,440400,440600,441900,442000', '', 'admin,accountManager')
---------user2role----------------------------------------------------------
insert into user_role (USER_ID, ROLE_ID, ID) values (1, 1, 1);
insert into user_role (USER_ID, ROLE_ID, ID) values (1, 2, 2);
---------webmodule2role-----------------------------------------------------
insert into webmodule_role (MODULE_ID, ROLE_ID, ID) values (1, 1, 1);
insert into webmodule_role (MODULE_ID, ROLE_ID, ID) values (2, 1, 2);
insert into webmodule_role (MODULE_ID, ROLE_ID, ID) values (3, 1, 3);
insert into webmodule_role (MODULE_ID, ROLE_ID, ID) values (4, 1, 4);
insert into webmodule_role (MODULE_ID, ROLE_ID, ID) values (5, 1, 5);
insert into webmodule_role (MODULE_ID, ROLE_ID, ID) values (6, 1, 6);
insert into webmodule_role (MODULE_ID, ROLE_ID, ID) values (7, 1, 7);
insert into webmodule_role (MODULE_ID, ROLE_ID, ID) values (8, 1, 8);
insert into webmodule_role (MODULE_ID, ROLE_ID, ID) values (9, 1, 9);
insert into webmodule_role (MODULE_ID, ROLE_ID, ID) values (10, 1, 10);

insert into webmodule_role (MODULE_ID, ROLE_ID, ID) values (11, 2, 11);
insert into webmodule_role (MODULE_ID, ROLE_ID, ID) values (12, 2, 12);
