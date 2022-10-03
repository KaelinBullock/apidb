--Insert locations---------------
INSERT INTO locations(id,
    name, street, suite, city,
    state, zipcode, time_zone,
    location_type, lat, lon)
VALUES (99, 'USA place','The street', '656', 'Big city',
        'Big state','338487', 'CST',
        'BUSINESS', 23.23, 23.23);

INSERT INTO locations(id,
    name, street, suite, city,
    state, zipcode, time_zone,
    location_type, lat, lon)
VALUES (89, 'USA place','The street', '656', 'Big city',
        'Big state','338487', 'CST',
        'BUSINESS', 23.23, 23.23);

INSERT INTO locations(id,
    name, street, suite, city,
    state, zipcode, time_zone,
    location_type, lat, lon)
VALUES (69, 'USA place','The street', '656', 'Big city',
        'Big state','338487', 'CST',
        'BUSINESS', 23.23, 23.23);

--Insert Companies---------------
INSERT INTO companies(id,
    company_name, location_id)
VALUES (99, 'Big Company', 99);

INSERT INTO companies(id,
    company_name, location_id)
VALUES (89, 'Big Company', 89);

INSERT INTO companies(id,
    company_name, location_id)
VALUES (69, 'Big Company', 69);

--Insert Contacts---------------
INSERT INTO contacts(id,
    name, company_id)
VALUES (99, 'Jarule', 99);

INSERT INTO contacts(id,
    name, company_id)
VALUES (89, 'Mark Anthony', 89);

INSERT INTO contacts(id,
    name, company_id)
VALUES (69, 'The guy from the gas station', 69);

--Insert Shipments---------------
INSERT INTO shipments(id,
    creation_date, delivery_date, contact_id)
VALUES (99, '2022-11-11', '2022-11-11', 99);

INSERT INTO shipments(id,
    creation_date, delivery_date, contact_id)
VALUES (89, '2022-11-11', '2022-11-11', 89);

INSERT INTO shipments(id,
    creation_date, delivery_date, contact_id)
VALUES (69, '2022-11-11', '2022-11-11', 69);