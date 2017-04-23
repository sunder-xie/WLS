 alter table MCHORDER add quantitiy VARCHAR(20);
  alter table MCHORDER add weight BIGINT;
  alter table MCHORDER add address VARCHAR(50);
  alter table MCHORDER add contacts VARCHAR(20);
  alter table MCHORDER add expressfee int(11);
  alter table MCHORDER add disreason VARCHAR(50);
  alter table MCHORDER add rmark VARCHAR(50);
 
  alter table CAFESHOPPROFILE add CREATETIME1 datetime;
   alter table CAFESHOPPROFILE add ENDTIME1 datetime;
    alter table CAFESHOPPROFILE add CREATETIME2 datetime;
     alter table CAFESHOPPROFILE add ENDTIME2 datetime;
     
      alter table CAFESHOPPROFILE add CREATETIME3 datetime;
       alter table CAFESHOPPROFILE add ENDTIME3 datetime;
       alter table wheelys_shop add ESNAME VARCHAR(100);
