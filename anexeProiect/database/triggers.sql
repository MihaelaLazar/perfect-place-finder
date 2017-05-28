create or replace TRIGGER MOVE_TO_ARCHIVE AFTER INSERT ON PF_ANNOUNCEMENTS
  BEGIN
    DECLARE    
        CURSOR get_estates IS SELECT * FROM PF_ANNOUNCEMENTS;
        estates_registration get_estates%ROWTYPE;
        number_of_days NUMBER;
        address_to_add VARCHAR2(255 BYTE);
        id_announcement_to_add NUMBER;
        bathrooms_to_add NUMBER;
        BUY_PRICE_to_add NUMBER;
        CAR_DISPOSAL_to_add NUMBER;
        CITY_to_add VARCHAR2(255 BYTE);
        YEAR_OF_CONSTRUCTION_to_add NUMBER;
        CONTACT_NUMBER_to_add VARCHAR2(255 BYTE);
        CREATION_DATE_to_add DATE;
        DIVISION_to_add NUMBER;
        FLOOR_to_add NUMBER;
        ID_USER_to_add NUMBER;
        LAST_UPDATE_to_add DATE;
        LEVEL_OF_CONFORT_to_add NUMBER;
        RENT_PRICE_to_add NUMBER;
        ROOMS_to_add NUMBER;
        SURFACE_to_add NUMBER;
        TYPE_OF_ESTATE_to_add VARCHAR2(255 BYTE);
        TYPE_OF_TRANSACTION_to_add VARCHAR2(255 BYTE);
        UTILITIES_to_add NUMBER;
        VERSION_to_add NUMBER;
        DESCRIPTION_to_add CLOB;
    BEGIN
        open get_estates;
        LOOP
        FETCH get_estates INTO estates_registration;
        EXIT WHEN get_estates%NOTFOUND;
          number_of_days := 0;
          SELECT trunc(sysdate) - TRUNC(PF_ANNOUNCEMENTS.creation_date) DAYS into number_of_days FROM PF_ANNOUNCEMENTS where PF_ANNOUNCEMENTS.id = estates_registration.id and rownum = 1; 
          if (number_of_days >= 90 ) THEN
              select address,bathrooms, buy_price, car_disposal, city, year_of_construction, contact_number, creation_date,division, floor, id_user, last_update,
                    level_of_confort, rent_price, rooms,surface, type_of_estate, type_of_transaction, utilities, version, description 
                    into
                    address_to_add,bathrooms_to_add, buy_price_to_add, car_disposal_to_add, city_to_add, year_of_construction_to_add, contact_number_to_add, creation_date_to_add,division_to_add,
                    floor_to_add, id_user_to_add, last_update_to_add,level_of_confort_to_add, rent_price_to_add, rooms_to_add,surface_to_add, type_of_estate_to_add, type_of_transaction_to_add,
                    utilities_to_add, version_to_add, description_to_add from PF_ANNOUNCEMENTS where PF_ANNOUNCEMENTS.ID = estates_registration.id and rownum = 1;
                    
              insert into PF_ARCHIVE(address,bathrooms, buy_price, car_disposal, city, year_of_construction, contact_number, creation_date,division, floor, id_user, last_update,
                    level_of_confort, rent_price, rooms,surface, type_of_estate, type_of_transaction, utilities, version, description) 
                  VALUES(address_to_add,bathrooms_to_add, buy_price_to_add, car_disposal_to_add, city_to_add, year_of_construction_to_add, contact_number_to_add, creation_date_to_add,division_to_add,
                    floor_to_add, id_user_to_add, last_update_to_add,level_of_confort_to_add, rent_price_to_add, rooms_to_add,surface_to_add, type_of_estate_to_add, type_of_transaction_to_add,
                    utilities_to_add, version_to_add, description_to_add );
                  
              DELETE from PF_ANNOUNCEMENTS where PF_ANNOUNCEMENTS.id = estates_registration.id;
          END IF;
        END LOOP;
    END;
    
  END;
  

CREATE OR REPLACE TRIGGER messages_deletions
  AFTER DELETE ON PF_ANNOUNCEMENTS
  FOR EACH ROW
BEGIN
  DELETE FROM PF_MESSAGES WHERE ID_ANNOUNCEMENT = :OLD.ID;
END;

/

CREATE OR REPLACE TRIGGER ATTACHMENTS_deletions
  AFTER DELETE ON PF_ANNOUNCEMENTS
  FOR EACH ROW
BEGIN
  DELETE FROM PF_ATTACHEMENTS WHERE ID_ANNOUNCEMENT = :OLD.ID;
END;
/

CREATE OR REPLACE TRIGGER FAV_ANNOUNCEMENT_deletions_2
  AFTER DELETE ON PF_USERS
  FOR EACH ROW
BEGIN
  DELETE FROM PF_FAV_ANNOUNCEMENTS WHERE ID_USER = :OLD.ID;
END;
/

CREATE OR REPLACE TRIGGER FAV_ANNOUNCEMENT_deletions
  AFTER DELETE ON PF_ANNOUNCEMENTS
FOR EACH ROW
BEGIN
  delete from PF_FAV_ANNOUNCEMENTS where id_announcement = :old.id;
end;
/
