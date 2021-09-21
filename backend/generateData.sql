-- User Table
INSERT INTO "user"(id, username, password, userType)
VALUES (gen_random_uuid(), 'customer_1', crypt('password_1', gen_salt('bf')), 'customer'),
       (gen_random_uuid(), 'customer_2', crypt('password_2', gen_salt('bf')), 'customer'),
       (gen_random_uuid(), 'customer_3', crypt('password_3', gen_salt('bf')), 'customer');

INSERT INTO "user"(id, username, password, userType)
VALUES (gen_random_uuid(), 'airline_1', crypt('password_1', gen_salt('bf')), 'airline'),
       (gen_random_uuid(), 'airline_2', crypt('password_2', gen_salt('bf')), 'airline'),
       (gen_random_uuid(), 'airline_3', crypt('password_3', gen_salt('bf')), 'airline');

INSERT INTO "user"(id, username, password, userType)
VALUES (gen_random_uuid(), 'admin_1', crypt('password_1', gen_salt('bf')), 'administrator'),
       (gen_random_uuid(), 'admin_2', crypt('password_2', gen_salt('bf')), 'administrator'),
       (gen_random_uuid(), 'admin_3', crypt('password_3', gen_salt('bf')), 'administrator');

-- Customer Table
INSERT INTO customer(id, givenName, surname, email)
VALUES ((SELECT id FROM "user" WHERE username = 'customer_1'), 'John', 'Doe', 'johndoe@gmail.com'),
       ((SELECT id FROM "user" WHERE username = 'customer_2'), 'Jane', 'Doe', 'janedoe@gmail.com'),
       ((SELECT id FROM "user" WHERE username = 'customer_3'), 'James', 'Doe', 'jamesdoe@gmail.com');

-- Airline Table
INSERT INTO airline(id, code, name)
VALUES ((SELECT id FROM "user" WHERE username = 'airline_1'), 'QFA', 'Qantas'),
       ((SELECT id FROM "user" WHERE username = 'airline_2'), 'VIR', 'Virgin'),
       ((SELECT id FROM "user" WHERE username = 'airline_3'), 'UAE', 'Emirates');

-- Admin Table
INSERT INTO administrator(id)
VALUES ((SELECT id FROM "user" WHERE username = 'admin_1')),
       ((SELECT id FROM "user" WHERE username = 'admin_2')),
       ((SELECT id FROM "user" WHERE username = 'admin_3'));

-- Airport Table
INSERT INTO airport(id, code, name, location, utcOffset)
VALUES (gen_random_uuid(), 'MEL', 'Tullamarine Airport', 'Melbourne', 'Australia/Melbourne'),
       (gen_random_uuid(), 'AVV', 'Avalon Airport', 'Geelong', 'Australia/Melbourne'),
       (gen_random_uuid(), 'SYD', 'Sydney Airport', 'Sydney', 'Australia/Sydney');

-- Airplane Table
-- Qantas Planes
INSERT INTO airplaneProfile(id, code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns,
                            economyClassRows, economyClassColumns)
VALUES (gen_random_uuid(), 'planecode_1', 'boeing', 6, 3, 8, 4, 40, 7),
       (gen_random_uuid(), 'planecode_2', 'airbus', 6, 3, 10, 4, 40, 10),
       (gen_random_uuid(), 'planecode_test', 'airbus', 2, 1, 2, 1, 3, 1);


-- Flight Table
-- Qantas Flights
INSERT INTO flight(id, code, departure, arrival, origin, destination, airlineId, airplaneId, firstClassCost,
                   businessClassCost, economyClassCost)
VALUES (gen_random_uuid(),
        'QN111', '2021-01-01 08:00', '2021-01-01 13:00',
        (SELECT id FROM airport WHERE code = 'MEL'),
        (SELECT id FROM airport WHERE code = 'SYD'),
        (SELECT id FROM airline WHERE code = 'QFA'),
        (SELECT id FROM airplaneProfile WHERE code = 'planecode_test'),
        100.0,
        80.0,
        50.0),
       (gen_random_uuid(),
        'QN112', '2021-01-01 15:00', '2021-01-01 18:00',
        (SELECT id FROM airport WHERE code = 'SYD'),
        (SELECT id FROM airport WHERE code = 'MEL'),
        (SELECT id FROM airline WHERE code = 'QFA'),
        (SELECT id FROM airplaneProfile WHERE code = 'planecode_test'),
        100.0,
        80.0,
        50.0),
       (gen_random_uuid(),
        'QN113', '2021-01-02 19:00', '2021-01-02 21:00',
        (SELECT id FROM airport WHERE code = 'AVV'),
        (SELECT id FROM airport WHERE code = 'SYD'),
        (SELECT id FROM airline WHERE code = 'QFA'),
        (SELECT id FROM airplaneProfile WHERE code = 'planecode_test'),
        100.0,
        80.0,
        50.0);

INSERT INTO seat(id, name, flightId, class)
VALUES (gen_random_uuid(), '1A', (SELECT id FROM flight WHERE code = 'QN111'), 'FIRST'),
       (gen_random_uuid(), '2A', (SELECT id FROM flight WHERE code = 'QN111'), 'FIRST'),
       (gen_random_uuid(), '3A', (SELECT id FROM flight WHERE code = 'QN111'), 'BUSINESS'),
       (gen_random_uuid(), '4A', (SELECT id FROM flight WHERE code = 'QN111'), 'BUSINESS'),
       (gen_random_uuid(), '5A', (SELECT id FROM flight WHERE code = 'QN111'), 'ECONOMY'),
       (gen_random_uuid(), '6A', (SELECT id FROM flight WHERE code = 'QN111'), 'ECONOMY'),
       (gen_random_uuid(), '7A', (SELECT id FROM flight WHERE code = 'QN111'), 'ECONOMY');

INSERT INTO seat(id, name, flightId, class)
VALUES (gen_random_uuid(), '1A', (SELECT id FROM flight WHERE code = 'QN112'), 'FIRST'),
       (gen_random_uuid(), '2A', (SELECT id FROM flight WHERE code = 'QN112'), 'FIRST'),
       (gen_random_uuid(), '3A', (SELECT id FROM flight WHERE code = 'QN112'), 'BUSINESS'),
       (gen_random_uuid(), '4A', (SELECT id FROM flight WHERE code = 'QN112'), 'BUSINESS'),
       (gen_random_uuid(), '5A', (SELECT id FROM flight WHERE code = 'QN112'), 'ECONOMY'),
       (gen_random_uuid(), '6A', (SELECT id FROM flight WHERE code = 'QN112'), 'ECONOMY'),
       (gen_random_uuid(), '7A', (SELECT id FROM flight WHERE code = 'QN112'), 'ECONOMY');

INSERT INTO seat(id, name, flightId, class)
VALUES (gen_random_uuid(), '1A', (SELECT id FROM flight WHERE code = 'QN113'), 'FIRST'),
       (gen_random_uuid(), '2A', (SELECT id FROM flight WHERE code = 'QN113'), 'FIRST'),
       (gen_random_uuid(), '3A', (SELECT id FROM flight WHERE code = 'QN113'), 'BUSINESS'),
       (gen_random_uuid(), '4A', (SELECT id FROM flight WHERE code = 'QN113'), 'BUSINESS'),
       (gen_random_uuid(), '5A', (SELECT id FROM flight WHERE code = 'QN113'), 'ECONOMY'),
       (gen_random_uuid(), '6A', (SELECT id FROM flight WHERE code = 'QN113'), 'ECONOMY'),
       (gen_random_uuid(), '7A', (SELECT id FROM flight WHERE code = 'QN113'), 'ECONOMY');

-- Virgin Flights
INSERT INTO flight(id, code, departure, arrival, origin, destination, airlineId, airplaneId, firstClassCost,
                   businessClassCost, economyClassCost)
VALUES (gen_random_uuid(),
        'VA111', '2021-02-01 08:00', '2021-02-01 13:00',
        (SELECT id FROM airport WHERE code = 'MEL'),
        (SELECT id FROM airport WHERE code = 'SYD'),
        (SELECT id FROM airline WHERE code = 'VIR'),
        (SELECT id FROM airplaneProfile WHERE code = 'planecode_test'),
        101.0,
        81.0,
        51.0),
       (gen_random_uuid(),
        'VA112', '2021-02-01 15:00', '2021-02-01 18:00',
        (SELECT id FROM airport WHERE code = 'SYD'),
        (SELECT id FROM airport WHERE code = 'MEL'),
        (SELECT id FROM airline WHERE code = 'VIR'),
        (SELECT id FROM airplaneProfile WHERE code = 'planecode_test'),
        101.0,
        81.0,
        51.0),
       (gen_random_uuid(),
        'VA113', '2021-02-02 19:00', '2021-02-02 21:00',
        (SELECT id FROM airport WHERE code = 'AVV'),
        (SELECT id FROM airport WHERE code = 'SYD'),
        (SELECT id FROM airline WHERE code = 'VIR'),
        (SELECT id FROM airplaneProfile WHERE code = 'planecode_test'),
        101.0,
        81.0,
        51.0);

INSERT INTO seat(id, name, flightId, class)
VALUES (gen_random_uuid(), '1A', (SELECT id FROM flight WHERE code = 'VA111'), 'FIRST'),
       (gen_random_uuid(), '2A', (SELECT id FROM flight WHERE code = 'VA111'), 'FIRST'),
       (gen_random_uuid(), '3A', (SELECT id FROM flight WHERE code = 'VA111'), 'BUSINESS'),
       (gen_random_uuid(), '4A', (SELECT id FROM flight WHERE code = 'VA111'), 'BUSINESS'),
       (gen_random_uuid(), '5A', (SELECT id FROM flight WHERE code = 'VA111'), 'ECONOMY'),
       (gen_random_uuid(), '6A', (SELECT id FROM flight WHERE code = 'VA111'), 'ECONOMY'),
       (gen_random_uuid(), '7A', (SELECT id FROM flight WHERE code = 'VA111'), 'ECONOMY');

INSERT INTO seat(id, name, flightId, class)
VALUES (gen_random_uuid(), '1A', (SELECT id FROM flight WHERE code = 'VA112'), 'FIRST'),
       (gen_random_uuid(), '2A', (SELECT id FROM flight WHERE code = 'VA112'), 'FIRST'),
       (gen_random_uuid(), '3A', (SELECT id FROM flight WHERE code = 'VA112'), 'BUSINESS'),
       (gen_random_uuid(), '4A', (SELECT id FROM flight WHERE code = 'VA112'), 'BUSINESS'),
       (gen_random_uuid(), '5A', (SELECT id FROM flight WHERE code = 'VA112'), 'ECONOMY'),
       (gen_random_uuid(), '6A', (SELECT id FROM flight WHERE code = 'VA112'), 'ECONOMY'),
       (gen_random_uuid(), '7A', (SELECT id FROM flight WHERE code = 'VA112'), 'ECONOMY');

INSERT INTO seat(id, name, flightId, class)
VALUES (gen_random_uuid(), '1A', (SELECT id FROM flight WHERE code = 'VA113'), 'FIRST'),
       (gen_random_uuid(), '2A', (SELECT id FROM flight WHERE code = 'VA113'), 'FIRST'),
       (gen_random_uuid(), '3A', (SELECT id FROM flight WHERE code = 'VA113'), 'BUSINESS'),
       (gen_random_uuid(), '4A', (SELECT id FROM flight WHERE code = 'VA113'), 'BUSINESS'),
       (gen_random_uuid(), '5A', (SELECT id FROM flight WHERE code = 'VA113'), 'ECONOMY'),
       (gen_random_uuid(), '6A', (SELECT id FROM flight WHERE code = 'VA113'), 'ECONOMY'),
       (gen_random_uuid(), '7A', (SELECT id FROM flight WHERE code = 'VA113'), 'ECONOMY');

-- Emirates Flights
INSERT INTO flight(id, code, departure, arrival, origin, destination, airlineId, airplaneId, firstClassCost,
                   businessClassCost, economyClassCost)
VALUES (gen_random_uuid(),
        'EM111', '2021-03-01 08:00', '2021-03-01 13:00',
        (SELECT id FROM airport WHERE code = 'MEL'),
        (SELECT id FROM airport WHERE code = 'SYD'),
        (SELECT id FROM airline WHERE code = 'UAE'),
        (SELECT id FROM airplaneProfile WHERE code = 'planecode_test'),
        102.0,
        82.0,
        52.0),
       (gen_random_uuid(),
        'EM112', '2021-03-01 15:00', '2021-03-01 18:00',
        (SELECT id FROM airport WHERE code = 'SYD'),
        (SELECT id FROM airport WHERE code = 'MEL'),
        (SELECT id FROM airline WHERE code = 'UAE'),
        (SELECT id FROM airplaneProfile WHERE code = 'planecode_test'),
        102.0,
        82.0,
        52.0),
       (gen_random_uuid(),
        'EM113', '2021-03-02 19:00', '2021-03-02 21:00',
        (SELECT id FROM airport WHERE code = 'MEL'),
        (SELECT id FROM airport WHERE code = 'SYD'),
        (SELECT id FROM airline WHERE code = 'UAE'),
        (SELECT id FROM airplaneProfile WHERE code = 'planecode_test'),
        102.0,
        82.0,
        52.0);

INSERT INTO seat(id, name, flightId, class)
VALUES (gen_random_uuid(), '1A', (SELECT id FROM flight WHERE code = 'EM111'), 'FIRST'),
       (gen_random_uuid(), '2A', (SELECT id FROM flight WHERE code = 'EM111'), 'FIRST'),
       (gen_random_uuid(), '3A', (SELECT id FROM flight WHERE code = 'EM111'), 'BUSINESS'),
       (gen_random_uuid(), '4A', (SELECT id FROM flight WHERE code = 'EM111'), 'BUSINESS'),
       (gen_random_uuid(), '5A', (SELECT id FROM flight WHERE code = 'EM111'), 'ECONOMY'),
       (gen_random_uuid(), '6A', (SELECT id FROM flight WHERE code = 'EM111'), 'ECONOMY'),
       (gen_random_uuid(), '7A', (SELECT id FROM flight WHERE code = 'EM111'), 'ECONOMY');

INSERT INTO seat(id, name, flightId, class)
VALUES (gen_random_uuid(), '1A', (SELECT id FROM flight WHERE code = 'EM112'), 'FIRST'),
       (gen_random_uuid(), '2A', (SELECT id FROM flight WHERE code = 'EM112'), 'FIRST'),
       (gen_random_uuid(), '3A', (SELECT id FROM flight WHERE code = 'EM112'), 'BUSINESS'),
       (gen_random_uuid(), '4A', (SELECT id FROM flight WHERE code = 'EM112'), 'BUSINESS'),
       (gen_random_uuid(), '5A', (SELECT id FROM flight WHERE code = 'EM112'), 'ECONOMY'),
       (gen_random_uuid(), '6A', (SELECT id FROM flight WHERE code = 'EM112'), 'ECONOMY'),
       (gen_random_uuid(), '7A', (SELECT id FROM flight WHERE code = 'EM112'), 'ECONOMY');

INSERT INTO seat(id, name, flightId, class)
VALUES (gen_random_uuid(), '1A', (SELECT id FROM flight WHERE code = 'EM113'), 'FIRST'),
       (gen_random_uuid(), '2A', (SELECT id FROM flight WHERE code = 'EM113'), 'FIRST'),
       (gen_random_uuid(), '3A', (SELECT id FROM flight WHERE code = 'EM113'), 'BUSINESS'),
       (gen_random_uuid(), '4A', (SELECT id FROM flight WHERE code = 'EM113'), 'BUSINESS'),
       (gen_random_uuid(), '5A', (SELECT id FROM flight WHERE code = 'EM113'), 'ECONOMY'),
       (gen_random_uuid(), '6A', (SELECT id FROM flight WHERE code = 'EM113'), 'ECONOMY'),
       (gen_random_uuid(), '7A', (SELECT id FROM flight WHERE code = 'EM113'), 'ECONOMY');

-- Stopover Table
-- Qantas
INSERT INTO stopover(flightId, departure, arrival, airportId)
VALUES ((SELECT id FROM flight WHERE code = 'QN111'),
        '2021-01-01 10:30', '2021-01-01 10:00',
        (SELECT id FROM airport WHERE code = 'AVV'));
-- Virgin
INSERT INTO stopover(flightId, departure, arrival, airportId)
VALUES ((SELECT id FROM flight WHERE code = 'VA111'),
        '2021-01-01 10:30', '2021-01-01 10:00',
        (SELECT id FROM airport WHERE code = 'AVV'));

-- Booking Table
INSERT INTO booking(id, date, customerId, flightId, returnFlightId)
VALUES (gen_random_uuid(),
        '2020-10-31 21:08',
        (SELECT id FROM "user" WHERE username = 'customer_1'),
        (SELECT id FROM flight WHERE code = 'QN111'),
        (SELECT id FROM flight WHERE code = 'QN112'));
INSERT INTO booking(id, date, customerId, flightId)
VALUES (gen_random_uuid(),
        '2020-10-31 21:20',
        (SELECT id FROM "user" WHERE username = 'customer_1'),
        (SELECT id FROM flight WHERE code = 'QN113'));

INSERT INTO booking(id, date, customerId, flightId, returnFlightId)
VALUES (gen_random_uuid(),
        '2020-11-24 08:02',
        (SELECT id FROM "user" WHERE username = 'customer_2'),
        (SELECT id FROM flight WHERE code = 'VA113'),
        (SELECT id FROM flight WHERE code = 'EM112'));

INSERT INTO booking(id, date, customerId, flightId, returnFlightId)
VALUES (gen_random_uuid(),
        '2020-12-12 11:08',
        (SELECT id FROM "user" WHERE username = 'customer_3'),
        (SELECT id FROM flight WHERE code = 'QN111'),
        (SELECT id FROM flight WHERE code = 'QN112'));
INSERT INTO booking(id, date, customerId, flightId)
VALUES (gen_random_uuid(),
        '2020-12-12 11:20',
        (SELECT id FROM "user" WHERE username = 'customer_3'),
        (SELECT id FROM flight WHERE code = 'EM113'));

-- Passenger Table
INSERT INTO passenger(id, givenName, surname, dob, nationality, passportNumber)
VALUES (gen_random_uuid(),
        'John', 'Doe', '1996-01-01', 'Australian', 'PA11231'),
       (gen_random_uuid(),
        'Jane', 'Doe', '1999-01-01', 'American', 'US1293');
