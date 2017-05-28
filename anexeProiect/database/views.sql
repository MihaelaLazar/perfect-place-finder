
  CREATE MATERIALIZED VIEW "RALUCA"."ANNOUNCEMENTS_ATTACHEMENTS_M" ("ADDRESS", "BATHROOMS", "BUY_PRICE", "CAR_DISPOSAL", "CITY", "YEAR_OF_CONSTRUCTION", "CONTACT_NUMBER", "CREATION_DATE", "DIVISION", "FLOOR", "ID_USER", "LAST_UPDATE", "LEVEL_OF_CONFORT", "RENT_PRICE", "ROOMS", "SURFACE", "TYPE_OF_ESTATE", "TYPE_OF_TRANSACTION", "UTILITIES", "VERSION", "DESCRIPTION", "TEXT", "SECOND_PART_TEXT", "MESSAGE_CREATTION_DATE")
  ORGANIZATION HEAP PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" 
 LOB ("DESCRIPTION") STORE AS BASICFILE (
  TABLESPACE "SYSTEM" ENABLE STORAGE IN ROW CHUNK 8192 RETENTION 
  NOCACHE LOGGING 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)) 
  BUILD IMMEDIATE
  USING INDEX 
  REFRESH FORCE ON DEMAND
  USING DEFAULT LOCAL ROLLBACK SEGMENT
  USING ENFORCED CONSTRAINTS DISABLE QUERY REWRITE
  AS         pm.TEXT, pm.SECOND_PART_TEXT, pm.CREATED_AT as "MESSAGE_CREATTION_DATE"FROM PF_ANNOUNCEMENTS pa JOIN PF_MESSAGES pm ON pm.ID_ANNOUNCEMENT = pa.ID;