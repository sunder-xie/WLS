----replace MYSCHEMA  to real schema, 
CREATE TABLE joblock
(
  jobname varchar(80) NOT NULL,
  firetime varchar(20),
  ip varchar(15),
  status varchar(10),
  nextfire varchar(20) NOT NULL,
  CONSTRAINT pk_joblock PRIMARY KEY (jobname , nextfire)
);

-- Create table
create table MYSCHEMA.APP_USER(
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
ALTER TABLE MYSCHEMA.app_user  OWNER TO MYSCHEMA;

-- Create/Recreate primary, unique and foreign key constraints 
alter table MYSCHEMA.APP_USER add constraint PK_USER_ID primary key (ID);
alter table MYSCHEMA.APP_USER add constraint UK_USER_NAME unique (USERNAME);

create table MYSCHEMA.ROLE
(
  ID          bigint not null,
  NAME        VARCHAR(20) not null,
  DESCRIPTION VARCHAR(64),
  TAG         VARCHAR(20)
);
ALTER TABLE MYSCHEMA.ROLE  OWNER TO MYSCHEMA;
alter table MYSCHEMA.ROLE add constraint PK_ROLE primary key (ID);

alter table MYSCHEMA.ROLE add constraint UK_ROLENAME unique (NAME);

create table MYSCHEMA.USER_ROLE
(
  USER_ID bigint not null,
  ROLE_ID bigint not null,
  ID      bigint not null
);
ALTER TABLE MYSCHEMA.USER_ROLE  OWNER TO MYSCHEMA;
alter table MYSCHEMA.USER_ROLE
  add constraint UK_USER_ROLE primary key (USER_ID, ROLE_ID);
--  using index 
--  tablespace WEBDATA;
create table MYSCHEMA.WEBMODULE
(
  RECORDID   bigint not null,
  MODULEURL  VARCHAR(1000),
  MATCHORDER integer not null,
  MENUCODE   VARCHAR(20) not null,
  MENUTITLE  VARCHAR(50) not null,
  TARGET     VARCHAR(10),
  DISPLAY    VARCHAR(10) not null,
  TAG        VARCHAR(3) not null,
  ROLENAMES  VARCHAR(2000)
);
ALTER TABLE MYSCHEMA.WEBMODULE  OWNER TO MYSCHEMA;
alter table MYSCHEMA.WEBMODULE
  add constraint PK_WEBMODULE primary key (RECORDID);
--  using index 
--  tablespace WEBDATA;
create table MYSCHEMA.WEBMODULE_ROLE
(
  MODULE_ID bigint not null,
  ROLE_ID   bigint not null,
  ID        bigint not null
);
ALTER TABLE MYSCHEMA.WEBMODULE_ROLE  OWNER TO MYSCHEMA;
alter table MYSCHEMA.WEBMODULE_ROLE
  add constraint PK_WEBMODULEROLE primary key (MODULE_ID, ROLE_ID);
--  using index 
--  tablespace WEBDATA

create table MYSCHEMA.DICTIONARY
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
ALTER TABLE MYSCHEMA.DICTIONARY  OWNER TO MYSCHEMA;
alter table MYSCHEMA.DICTIONARY
  add constraint PK_DICTIONARY primary key (TABLE_NAME, COLUMN_NAME);
-- Create/Recreate indexes 
create unique index IDXPK_DICTIONARY on MYSCHEMA.DICTIONARY (TABLE_NAME, COLUMN_NAME);
--  tablespace TBS_INDEX

create table MYSCHEMA.GEWACONFIG
(
  CKEY        VARCHAR(15) not null,
  DESCRIPTION VARCHAR(100) not null,
  UPDATETIME  TIMESTAMP not null,
  CONTENT     VARCHAR(2000)
);
ALTER TABLE MYSCHEMA.GEWACONFIG  OWNER TO MYSCHEMA;
alter table MYSCHEMA.GEWACONFIG add constraint PK_GEWACONFIG primary key (CKEY);
--  using index 
--  tablespace WEBDATA
create table MYSCHEMA.USER_OPERATION
(
  OPKEY      VARCHAR(200) not null,
  TAG        VARCHAR(20),
  ADDTIME    TIMESTAMP,
  UPDATETIME TIMESTAMP,
  OPNUM      integer,
  VALIDTIME  TIMESTAMP,
  REFUSED    bigint
);
ALTER TABLE MYSCHEMA.USER_OPERATION  OWNER TO MYSCHEMA;
alter table MYSCHEMA.USER_OPERATION
  add constraint PK_USER_OPERATION primary key (OPKEY);
CREATE SEQUENCE MYSCHEMA.hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 20;
ALTER TABLE MYSCHEMA.hibernate_sequence OWNER TO MYSCHEMA;
--  using index 
--  tablespace WEBDATA
-------------------------------------------------------------------------
insert into MYSCHEMA.gewaconfig (ckey,description,updatetime,content) values('cacheVersion', 'cache version', current_timestamp, '{"oneMin":"1v1","tenMin":"10v1","halfHour":"30v1","oneHour":"60v1","twoHour":"2hv1","halfDay":"12hv1","loginKey":"lkv1","loginAuth":"lav1"}');
insert into MYSCHEMA.gewaconfig (ckey,description,updatetime,content) values('cacheVerTest', 'cache version test', current_timestamp, '{"oneMin":"t1v1","tenMin":"t10v1","halfHour":"t30v1","oneHour":"t60v1","twoHour":"t2hv1","halfDay":"t12hv1","loginKey":"tlkv1","loginAuth":"tlav1"}');

---------modules---------------------------------------------------------
insert into MYSCHEMA.webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (1, 'tt260', 32, '32', '权限管理', 'mainFrame', 'Y', 'G', 'accountManager');
insert into MYSCHEMA.webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (2, '/admin/acl/userList.xhtml', 3202, '3202', '用户管理', '_blank', 'Y', 'G', 'accountManager');
insert into MYSCHEMA.webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (3, '/admin/acl/moduleList.xhtml?tag=G', 3203, '3203', '功能模块[Gewa]', 'mainFrame', 'Y', 'G', 'accountManager');
insert into MYSCHEMA.webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (4, '/admin/acl/moduleList.xhtml?tag=GP', 3204, '3204', '功能模块[Partner]', 'mainFrame', 'Y', 'G', 'accountManager');
insert into MYSCHEMA.webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (5, '/admin/acl/moduleList.xhtml?tag=A', 3205, '3205', '功能模块[API]', 'mainFrame', 'Y', 'G', 'accountManager');
insert into MYSCHEMA.webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (6, '/admin/acl/roleList.xhtml?tag=G', 3206, '3206', '角色管理[Gewa]', 'mainFrame', 'Y', 'G', 'accountManager');
insert into MYSCHEMA.webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (7, '/admin/acl/roleList.xhtml?tag=GP', 3207, '3207', '角色管理[Partner]', 'mainFrame', 'Y', 'G', '');
insert into MYSCHEMA.webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (8, '/admin/acl/roleList.xhtml?tag=A', 3208, '3208', '角色管理[API]', 'mainFrame', 'Y', 'G', '');
insert into MYSCHEMA.webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (9, '/admin/acl/reloadAcl.xhtml', 3210, '3210', '刷新权限', 'mainFrame', 'Y', 'G', 'accountManager');
insert into MYSCHEMA.webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (10, '/admin/acl/*', 3299, '3299', '权限必须', '', 'N', 'G', 'accountManager');

insert into MYSCHEMA.webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (11, 'admin260', 99, '99', '登录拦截', 'mainFrame', 'N', 'G', 'admin');
insert into MYSCHEMA.webmodule (RECORDID, MODULEURL, MATCHORDER, MENUCODE, MENUTITLE, TARGET, DISPLAY, TAG, ROLENAMES)
values (12, '/admin/**/*', 9999, '9999', '权限必须', '', 'N', 'G', 'admin');

---------roles--------------------------------------------------------------
insert into MYSCHEMA.role (ID, NAME, DESCRIPTION, TAG)
values (1, 'accountManager', '账户管理', 'G');

insert into MYSCHEMA.role (ID, NAME, DESCRIPTION, TAG)
values (2, 'admin', '超级用户', 'G');
---------users--------------------------------------------------------------
insert into MYSCHEMA.app_user (ID, USERNAME, DISPLAYNAME, PASSWORD, ACCOUNT_ENABLED, CITYCODE, MOBILE, ROLENAMES)
values (1, 'admin', 'adminUser', 'cc03e747a6afbbcbf8be7668acfebee5', 'Y', '310000,330100,330200,330400,330600,330700,331200,320600,440100,440300,440400,440600,441900,442000', '', 'admin,accountManager');
---------user2role----------------------------------------------------------
insert into MYSCHEMA.user_role (USER_ID, ROLE_ID, ID) values (1, 1, 1);
insert into MYSCHEMA.user_role (USER_ID, ROLE_ID, ID) values (1, 2, 2);
---------webmodule2role-----------------------------------------------------
insert into MYSCHEMA.webmodule_role (MODULE_ID, ROLE_ID, ID) values (1, 1, 1);
insert into MYSCHEMA.webmodule_role (MODULE_ID, ROLE_ID, ID) values (2, 1, 2);
insert into MYSCHEMA.webmodule_role (MODULE_ID, ROLE_ID, ID) values (3, 1, 3);
insert into MYSCHEMA.webmodule_role (MODULE_ID, ROLE_ID, ID) values (4, 1, 4);
insert into MYSCHEMA.webmodule_role (MODULE_ID, ROLE_ID, ID) values (5, 1, 5);
insert into MYSCHEMA.webmodule_role (MODULE_ID, ROLE_ID, ID) values (6, 1, 6);
insert into MYSCHEMA.webmodule_role (MODULE_ID, ROLE_ID, ID) values (7, 1, 7);
insert into MYSCHEMA.webmodule_role (MODULE_ID, ROLE_ID, ID) values (8, 1, 8);
insert into MYSCHEMA.webmodule_role (MODULE_ID, ROLE_ID, ID) values (9, 1, 9);
insert into MYSCHEMA.webmodule_role (MODULE_ID, ROLE_ID, ID) values (10, 1, 10);

insert into MYSCHEMA.webmodule_role (MODULE_ID, ROLE_ID, ID) values (11, 2, 11);
insert into MYSCHEMA.webmodule_role (MODULE_ID, ROLE_ID, ID) values (12, 2, 12);
