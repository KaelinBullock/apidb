DROP ALL OBJECTS DELETE FILES;

--DELETE FROM SHIPMENTS;
--DELETE FROM COMPANIES;
--DELETE FROM CONTACTS;
--DELETE FROM LOCATIONS;

--DELETE FROM LOCATIONS
--DBCC CHECKIDENT ('DEMO.dbo.LOCATIONS', RESEED, 0)

--this is a lot of effort to go through,so I will  probably just keep the regular run init with the loaders and only do this for tests, but then again i could just use the loader for both idk
--INSERT INTO LOCATIONS
--    ()
--VALUES ();
--
--INSERT INTO CONTACTS
--    ()
--VALUES ();
--
--INSERT INTO shipments (id,
--    creation_date,
--    delivery_date,
--    contact_id,
--    location_id)
--VALUES (54,'2022-11-11','2022-11-11', 1, 5);