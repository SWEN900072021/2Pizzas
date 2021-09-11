-- User Table
INSERT INTO "user"(id, username, password) VALUES
      (gen_random_uuid(), 'customer_1', 'password_1'),
      (gen_random_uuid(), 'customer_2', 'password_2'),
      (gen_random_uuid(), 'customer_3', 'password_3');

INSERT INTO "user"(id, username, password) VALUES
      (gen_random_uuid(), 'airline_1', 'password_1'),
      (gen_random_uuid(), 'airline_2', 'password_2'),
      (gen_random_uuid(), 'airline_3', 'password_3');

INSERT INTO "user"(id, username, password) VALUES
      (gen_random_uuid(), 'admin_1', 'password_1'),
      (gen_random_uuid(), 'admin_2', 'password_2'),
      (gen_random_uuid(), 'admin_3', 'password_3');

-- Customer Table
INSERT INTO customer(id, givenName, surname, email) VALUES
       ( ( SELECT id FROM "user" WHERE username = 'customer_1' ), 'John', 'Doe', 'johndoe@gmail.com'),
       ( ( SELECT id FROM "user" WHERE username = 'customer_2' ), 'Jane', 'Doe', 'janedoe@gmail.com'),
       ( ( SELECT id FROM "user" WHERE username = 'customer_3' ), 'James', 'Doe', 'jamesdoe@gmail.com');

-- Airline Table
INSERT INTO airline(id, code, name) VALUES
       ( ( SELECT id FROM "user" WHERE username = 'airline_1' ), 'QFA', 'Qantas'),
       ( ( SELECT id FROM "user" WHERE username = 'airline_2' ), 'VIR', 'Virgin'),
       ( ( SELECT id FROM "user" WHERE username = 'airline_3' ), 'UAE', 'Emirates');

-- Admin Table
INSERT INTO administrator(id) VALUES
       ( ( SELECT id FROM "user" WHERE username = 'admin_1' ) ),
       ( ( SELECT id FROM "user" WHERE username = 'admin_2' ) ),
       ( ( SELECT id FROM "user" WHERE username = 'admin_3' ) );

-- Airport Table
INSERT INTO airport(id, code, name, location, utcOffset) VALUES
       (gen_random_uuid(), 'MEL', 'Tullamarine Airport', 'Melbourne', 'Australia/Melbourne'),
       (gen_random_uuid(), 'AVV', 'Avalon Airport', 'Geelong', 'Australia/Melbourne'),
       (gen_random_uuid(), 'SYD', 'Sydney Airport', 'Sydney', 'Australia/Sydney');

-- Airplane Table
    -- Qantas Planes
INSERT INTO airplaneProfile(id, code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns, economyClassRows, economyClassColumns) VALUES
       (gen_random_uuid(), 'planecode_1', 'boeing', 3, 6, 4, 8, 7, 40),
       (gen_random_uuid(), 'planecode_2', 'airbus', 3, 6, 4, 10, 10, 40);
    -- Virgin Planes
INSERT INTO airplaneProfile(id, code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns, economyClassRows, economyClassColumns) VALUES
       (gen_random_uuid(), 'planecode_3', 'boeing', 3, 6, 4, 8, 7, 40),
       (gen_random_uuid(), 'planecode_4', 'airbus', 3, 6, 4, 10, 10, 40);
    -- Emirates Planes
INSERT INTO airplaneProfile(id, code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns, economyClassRows, economyClassColumns) VALUES
       (gen_random_uuid(), 'planecode_5', 'boeing', 3, 6, 4, 8, 7, 40);

-- Flight Table
    -- Qantas Flights
INSERT INTO flight(id, code, departure, arrival, origin, destination, airlineId, airplaneId) VALUES
       (
           gen_random_uuid(),
           'QN111', '2021-01-01 08:00', '2021-01-01 13:00',
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airline WHERE code = 'QFA' ),
            ( SELECT id FROM airplaneProfile WHERE code = 'planecode_1' )
       ),
       (
           gen_random_uuid(),
           'QN112', '2021-01-01 15:00', '2021-01-01 18:00',
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airline WHERE code = 'QFA' ),
            ( SELECT id FROM airplaneProfile WHERE code = 'planecode_1' )
       ),
       (
           gen_random_uuid(),
           'QN113', '2021-01-02 19:00', '2021-01-02 21:00',
            ( SELECT id FROM airport WHERE code = 'AVV'),
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airline WHERE code = 'QFA' ),
            ( SELECT id FROM airplaneProfile WHERE code = 'planecode_2' )
       );
    -- Virgin Flights
INSERT INTO flight(id, code, departure, arrival, origin, destination, airlineId, airplaneId) VALUES
       (
           gen_random_uuid(),
           'VA111', '2021-02-01 08:00', '2021-02-01 13:00',
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airline WHERE code = 'VIR' ),
            ( SELECT id FROM airplaneProfile WHERE code = 'planecode_3' )
       ),
       (
           gen_random_uuid(),
           'VA112', '2021-02-01 15:00', '2021-02-01 18:00',
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airline WHERE code = 'VIR' ),
            ( SELECT id FROM airplaneProfile WHERE code = 'planecode_3' )
       ),
       (
           gen_random_uuid(),
           'VA113', '2021-02-02 19:00', '2021-02-02 21:00',
            ( SELECT id FROM airport WHERE code = 'AVV'),
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airline WHERE code = 'VIR' ),
            ( SELECT id FROM airplaneProfile WHERE code = 'planecode_4' )
       );
    -- Emirates Flights
INSERT INTO flight(id, code, departure, arrival, origin, destination, airlineId, airplaneId) VALUES
       (
           gen_random_uuid(),
           'EM111', '2021-03-01 08:00', '2021-03-01 13:00',
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airline WHERE code = 'UAE' ),
            ( SELECT id FROM airplaneProfile WHERE code = 'planecode_5' )
       ),
       (
           gen_random_uuid(),
           'EM112', '2021-03-01 15:00', '2021-03-01 18:00',
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airline WHERE code = 'UAE' ),
            ( SELECT id FROM airplaneProfile WHERE code = 'planecode_5' )
       ),
       (
           gen_random_uuid(),
           'EM113', '2021-03-02 19:00', '2021-03-02 21:00',
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airline WHERE code = 'UAE' ),
            ( SELECT id FROM airplaneProfile WHERE code = 'planecode_5' )
       );

-- Stopover Table
    -- Qantas
INSERT INTO stopover(flightId, departure, arrival, airportId) VALUES
        (

            ( SELECT id FROM flight WHERE code = 'QN111' ),
            '2021-01-01 10:00', '2021-01-01 10:30',
            ( SELECT id FROM airport WHERE code = 'AVV' )
        );
    -- Virgin
INSERT INTO stopover(flightId, departure, arrival, airportId) VALUES
        (
            ( SELECT id FROM flight WHERE code = 'VA111' ),
            '2021-01-01 10:00', '2021-01-01 10:30',
            ( SELECT id FROM airport WHERE code = 'AVV' )
        );

-- Booking Table
INSERT INTO booking(id, date, totalCost, reference, customerId, flightId, returnFlightId) VALUES
        (
            gen_random_uuid(),
            '2020-10-31 21:08', '180.00', 'REF0001',
            ( SELECT id FROM "user" WHERE username = 'customer_1' ),
            ( SELECT id FROM flight WHERE code = 'QN111' ),
            ( SELECT id FROM flight WHERE code = 'QN112' )
        );
INSERT INTO booking(id, date, totalCost, reference, customerId, flightId) VALUES
        (
            gen_random_uuid(),
            '2020-10-31 21:20', '60.00', 'REF0002',
            ( SELECT id FROM "user" WHERE username = 'customer_1' ),
            ( SELECT id FROM flight WHERE code = 'QN113' )
        );

INSERT INTO booking(id, date, totalCost, reference, customerId, flightId, returnFlightId) VALUES
        (
            gen_random_uuid(),
            '2020-11-24 08:02', '105.00', 'REF0003',
            ( SELECT id FROM "user" WHERE username = 'customer_2' ),
            ( SELECT id FROM flight WHERE code = 'VA113' ),
            ( SELECT id FROM flight WHERE code = 'EM112' )
        );

INSERT INTO booking(id, date, totalCost, reference, customerId, flightId, returnFlightId) VALUES
        (
            gen_random_uuid(),
            '2020-12-12 11:08', '180.00', 'REF0004',
            ( SELECT id FROM "user" WHERE username = 'customer_3' ),
            ( SELECT id FROM flight WHERE code = 'QN111' ),
            ( SELECT id FROM flight WHERE code = 'QN112' )
        );
INSERT INTO booking(id, date, totalCost, reference, customerId, flightId) VALUES
        (
            gen_random_uuid(),
            '2020-12-12 11:20', '60.00', 'REF0005',
            ( SELECT id FROM "user" WHERE username = 'customer_3' ),
            ( SELECT id FROM flight WHERE code = 'EM113' )
        );

-- Seat Table
INSERT INTO seat(id, name, flightId, class) VALUES
        (
            gen_random_uuid(),
            '1A',
            ( SELECT id FROM flight WHERE code = 'QN111' ),
            'ECONOMY'
        ),
        (
            gen_random_uuid(),
            '1B',
            ( SELECT id FROM flight WHERE code = 'QN111' ),
            'ECONOMY'
        );


-- Passenger Table
INSERT INTO passenger(id, givenName, surname, dob, nationality, passportNumber) VALUES
        (
            gen_random_uuid(),
            'John', 'Doe', '1996-01-01', 'Australian', 'PA11231'
        ),
        (
            gen_random_uuid(),
            'Jane', 'Doe', '1999-01-01', 'American', 'US1293'
        );