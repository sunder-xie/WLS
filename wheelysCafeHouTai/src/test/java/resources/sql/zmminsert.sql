-------------------------------------------------------------------
insert into CAFEITEM(RECORDID,NAME,STATE) VALUES (1,'“‚ Ωøß∑»','Y')
insert into CAFEITEM(RECORDID,NAME,STATE) VALUES (2,' ÷≥Âøß∑»','Y')
insert into CAFEITEM(RECORDID,NAME,STATE) VALUES (4,'Ω°øµ≤Ë“˚','Y')

CREATE
    TRIGGER wheelys_orderform_updatestatus
    after UPDATE
    ON wheelys_orderform FOR EACH ROW
BEGIN
	if new.orderstate = 3 THEN
UPDATE WHEELYS_CAFEORDER SET `STATUS` = 'finish' WHERE `TRADENO`= new.tradeno;
	END if;
END