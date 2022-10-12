--Insert locations---------------
INSERT INTO locations(
    name, street, suite, city,
    state, zipcode, time_zone,
    location_type, lat, lon)
VALUES ( 'USA place','The street', '656', 'Big city',
        'Big state','338487', 'CST',
        'BUSINESS', 23.23, 23.23);

INSERT INTO locations(
    name, street, suite, city,
    state, zipcode, time_zone,
    location_type, lat, lon)
VALUES ( 'USA place','The street', '656', 'Big city',
        'Big state','338487', 'CST',
        'BUSINESS', 23.23, 23.23);

INSERT INTO locations(
    name, street, suite, city,
    state, zipcode, time_zone,
    location_type, lat, lon)
VALUES ( 'USA place','The street', '656', 'Big city',
        'Big state','338487', 'CST',
        'BUSINESS', 23.23, 23.23);

--Insert Companies---------------
INSERT INTO companies(
    company_name, location_id)
VALUES ('Big Company', 1);

INSERT INTO companies(
    company_name, location_id)
VALUES ('small Company', 2);

INSERT INTO companies(
    company_name, location_id)
VALUES ('The Book Store', 3);

--Insert Contacts---------------
INSERT INTO contacts(
    name, company_id)
VALUES ('The guy from the gas station', 1);

INSERT INTO contacts(
    name, company_id)
VALUES ('Mark Anthony', 2);

INSERT INTO contacts(
    name, company_id)
VALUES ('Jarule', 3);

--Insert Shipments---------------
INSERT INTO shipments(
    creation_date, delivery_date, contact_id)
VALUES ('2022-11-11', '2022-11-11', 1);

INSERT INTO shipments(
    creation_date, delivery_date, contact_id)
VALUES ('2022-11-11', '2022-11-11', 2);

INSERT INTO shipments(
    creation_date, delivery_date, contact_id)
VALUES ('2022-11-11', '2022-11-11', 3);