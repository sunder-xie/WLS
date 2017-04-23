CREATE TABLE DYNAMICREPORT (
	ID BIGINT NOT NULL AUTO_INCREMENT primary key,
	NAME VARCHAR(20) NOT NULL,
	CATEGORY VARCHAR(30) DEFAULT NULL,
	QRYSQL VARCHAR(4000) NOT NULL,
	PARAMS VARCHAR(500) DEFAULT NULL,
	FIELDS VARCHAR(500) DEFAULT NULL,
	DISPLAYNAME VARCHAR(500) DEFAULT NULL,
	MAXNUM SMALLINT NOT NULL,
	ROLES VARCHAR(500) DEFAULT NULL,
	DESCRIPTION VARCHAR(2000) DEFAULT NULL
)
