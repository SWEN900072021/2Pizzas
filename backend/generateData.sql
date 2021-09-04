-- User Table
INSERT INTO "user"(username, password) VALUES
      ('customer_1', 'password_1'),
      ('customer_2', 'password_2'),
      ('customer_3', 'password_3');

INSERT INTO "user"(username, password) VALUES
      ('airline_1', 'password_1'),
      ('airline_2', 'password_2'),
      ('airline_3', 'password_3');

INSERT INTO "user"(username, password) VALUES
      ('admin_1', 'password_1'),
      ('admin_2', 'password_2'),
      ('admin_3', 'password_3');

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
INSERT INTO airport(code, name, location, utcOffset) VALUES
       ('MEL', 'Tullamarine Airport', 'Melbourne', 'Australia/Melbourne'),
       ('AVV', 'Avalon Airport', 'Geelong', 'Australia/Melbourne'),
       ('SYD', 'Sydney Airport', 'Sydney', 'Australia/Sydney');

-- Airplane Table
    -- Qantas Planes
INSERT INTO airplane(code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns, economyClassRows, economyClassColumns) VALUES
       ('planecode_1', 'boeing', 3, 6, 4, 8, 7, 40),
       ('planecode_2', 'airbus', 3, 6, 4, 10, 10, 40);
    -- Virgin Planes
INSERT INTO airplane(code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns, economyClassRows, economyClassColumns) VALUES
       ('planecode_3', 'boeing', 3, 6, 4, 8, 7, 40),
       ('planecode_4', 'airbus', 3, 6, 4, 10, 10, 40);
    -- Emirates Planes
INSERT INTO airplane(code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns, economyClassRows, economyClassColumns) VALUES
       ('planecode_5', 'boeing', 3, 6, 4, 8, 7, 40);

-- Flight Table
    -- Qantas Flights
INSERT INTO flight(code, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES
       (
            'QN111', '2021-01-01 08:00', '2021-01-01 13:00',
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airline WHERE code = 'QFA' ),
            ( SELECT id FROM airplane WHERE code = 'planecode_1' )
       ),
       (
            'QN112', '2021-01-01 15:00', '2021-01-01 18:00',
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airline WHERE code = 'QFA' ),
            ( SELECT id FROM airplane WHERE code = 'planecode_1' )
       ),
       (
            'QN113', '2021-01-02 19:00', '2021-01-02 21:00',
            ( SELECT id FROM airport WHERE code = 'AVV'),
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airline WHERE code = 'QFA' ),
            ( SELECT id FROM airplane WHERE code = 'planecode_2' )
       );
    -- Virgin Flights
INSERT INTO flight(code, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES
       (
            'VA111', '2021-02-01 08:00', '2021-02-01 13:00',
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airline WHERE code = 'VIR' ),
            ( SELECT id FROM airplane WHERE code = 'planecode_3' )
       ),
       (
            'VA112', '2021-02-01 15:00', '2021-02-01 18:00',
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airline WHERE code = 'VIR' ),
            ( SELECT id FROM airplane WHERE code = 'planecode_3' )
       ),
       (
            'VA113', '2021-02-02 19:00', '2021-02-02 21:00',
            ( SELECT id FROM airport WHERE code = 'AVV'),
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airline WHERE code = 'VIR' ),
            ( SELECT id FROM airplane WHERE code = 'planecode_4' )
       );
    -- Emirates Flights
INSERT INTO flight(code, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES
       (
            'EM111', '2021-03-01 08:00', '2021-03-01 13:00',
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airline WHERE code = 'UAE' ),
            ( SELECT id FROM airplane WHERE code = 'planecode_5' )
       ),
       (
            'EM112', '2021-03-01 15:00', '2021-03-01 18:00',
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airline WHERE code = 'UAE' ),
            ( SELECT id FROM airplane WHERE code = 'planecode_5' )
       ),
       (
            'EM113', '2021-03-02 19:00', '2021-03-02 21:00',
            ( SELECT id FROM airport WHERE code = 'MEL' ),
            ( SELECT id FROM airport WHERE code = 'SYD' ),
            ( SELECT id FROM airline WHERE code = 'UAE' ),
            ( SELECT id FROM airplane WHERE code = 'planecode_5' )
       );

-- Stopover Table
    -- Qantas
INSERT INTO stopover(flightId, duration, airportId) VALUES
        (
            ( SELECT id FROM flight WHERE code = 'QN111' ),
            '2 hours',
            ( SELECT id FROM airport WHERE code = 'AVV' )
        );
    -- Virgin
INSERT INTO stopover(flightId, duration, airportId) VALUES
        (
            ( SELECT id FROM flight WHERE code = 'VA111' ),
            '2 hours',
            ( SELECT id FROM airport WHERE code = 'AVV' )
        );
    -- Emirates
INSERT INTO stopover(flightId, duration, airportId) VALUES
        (
            ( SELECT id FROM flight WHERE code = 'EM111' ),
            '2 hours',
            ( SELECT id FROM airport WHERE code = 'AVV' )
        );

-- Booking Table
INSERT INTO booking(date, totalCost, reference, customerId, flightId, returnFlightId) VALUES
        (
            '2020-10-31 21:08', '180.00', 'REF0001',
            ( SELECT id FROM "user" WHERE username = 'customer_1' ),
            ( SELECT id FROM flight WHERE code = 'QN111' ),
            ( SELECT id FROM flight WHERE code = 'QN112' )
        );
INSERT INTO booking(date, totalCost, reference, customerId, flightId) VALUES
        (
            '2020-10-31 21:20', '60.00', 'REF0002',
            ( SELECT id FROM "user" WHERE username = 'customer_1' ),
            ( SELECT id FROM flight WHERE code = 'QN113' )
        );

INSERT INTO booking(date, totalCost, reference, customerId, flightId, returnFlightId) VALUES
        (
            '2020-11-24 08:02', '105.00', 'REF0003',
            ( SELECT id FROM "user" WHERE username = 'customer_2' ),
            ( SELECT id FROM flight WHERE code = 'VA113' ),
            ( SELECT id FROM flight WHERE code = 'EM112' )
        );

INSERT INTO booking(date, totalCost, reference, customerId, flightId, returnFlightId) VALUES
        (
            '2020-12-12 11:08', '180.00', 'REF0004',
            ( SELECT id FROM "user" WHERE username = 'customer_3' ),
            ( SELECT id FROM flight WHERE code = 'QN111' ),
            ( SELECT id FROM flight WHERE code = 'QN112' )
        );
INSERT INTO booking(date, totalCost, reference, customerId, flightId) VALUES
        (
            '2020-12-12 11:20', '60.00', 'REF0005',
            ( SELECT id FROM "user" WHERE username = 'customer_3' ),
            ( SELECT id FROM flight WHERE code = 'EM113' )
        );

-- Seat Table
INSERT INTO seat(row, "column", flightId, seatType, status) VALUES
        (
            '1', '1',
            ( SELECT id FROM flight WHERE code = 'QN111' ),
            'economy', 'occupied'
        ),
        (
            '1', '2',
            ( SELECT id FROM flight WHERE code = 'QN111' ),
            'economy', 'occupied'
        );


-- Passenger Table
INSERT INTO passenger(bookingId, seatId, givenName, surname, dob, nationality, passportNumber) VALUES
        (
            ( SELECT id FROM booking WHERE reference = 'REF0001' ),
            ( SELECT id FROM seat WHERE row = '1' AND "column" = '2' ),
            'John', 'Doe', '1996-01-01', 'Australian', 'PA11231'
        ),
        (
            ( SELECT id FROM booking WHERE reference = 'REF0004' ),
            ( SELECT id FROM seat WHERE row = '1' AND "column" = '2' ),
            'Jane', 'Doe', '1999-01-01', 'American', 'US1293'
        );