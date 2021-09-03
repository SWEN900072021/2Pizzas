-- User Table
INSERT INTO "user"(username, password) VALUES ('customer_1', 'password_1');
INSERT INTO "user"(username, password) VALUES ('customer_2', 'password_2');
INSERT INTO "user"(username, password) VALUES ('customer_3', 'password_3');

INSERT INTO "user"(username, password) VALUES ('airline_1', 'password_1');
INSERT INTO "user"(username, password) VALUES ('airline_2', 'password_2');
INSERT INTO "user"(username, password) VALUES ('airline_3', 'password_3');

INSERT INTO "user"(username, password) VALUES ('admin_1', 'password_1');
INSERT INTO "user"(username, password) VALUES ('admin_2', 'password_2');
INSERT INTO "user"(username, password) VALUES ('admin_3', 'password_3');

-- Customer Table
INSERT INTO customer(firstName, surname, email) VALUES ('John', 'Doe', 'johndoe@gmail.com');
INSERT INTO customer(firstName, surname, email) VALUES ('Jane', 'Doe', 'janedoe@gmail.com');
INSERT INTO customer(firstName, surname, email) VALUES ('James', 'Doe', 'jamesdoe@gmail.com');

-- Airline Table
INSERT INTO airline(code, name) VALUES ('QFA', 'Qantas');
INSERT INTO airline(code, name) VALUES ('VIR', 'Virgin');
INSERT INTO airline(code, name) VALUES ('UAE', 'Emirates');

-- Admin Table
INSERT INTO administrator(id) VALUES ('7');
INSERT INTO administrator(id) VALUES ('8');
INSERT INTO administrator(id) VALUES ('9');

-- Airport Table
INSERT INTO airport(code, name, location, utcOffset) VALUES ('MEL', 'Tullamarine Airport', 'Melbourne', 'Australia/Melbourne');
INSERT INTO airport(code, name, location, utcOffset) VALUES ('AVV', 'Avalon Airport', 'Geelong', 'Australia/Melbourne');
INSERT INTO airport(code, name, location, utcOffset) VALUES ('SYD', 'Sydney Airport', 'Sydney', 'Australia/Sydney');

-- Airplane Table
    -- Qantas Planes
INSERT INTO airplane(code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns, economyClassRows, economyClassColumns) VALUES ('planecode_1', 'boeing', 3, 6, 4, 8, 7, 40);
INSERT INTO airplane(code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns, economyClassRows, economyClassColumns) VALUES ('planecode_2', 'airbus', 3, 6, 4, 10, 10, 40);
    -- Virgin Planes
INSERT INTO airplane(code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns, economyClassRows, economyClassColumns) VALUES ('planecode_3', 'boeing', 3, 6, 4, 8, 7, 40);
INSERT INTO airplane(code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns, economyClassRows, economyClassColumns) VALUES ('planecode_4', 'airbus', 3, 6, 4, 10, 10, 40);
    -- Emirates Planes
INSERT INTO airplane(code, type, firstClassRows, firstClassColumns, businessClassRows, businessClassColumns, economyClassRows, economyClassColumns) VALUES ('planecode_5', 'boeing', 3, 6, 4, 8, 7, 40);

-- -- Flight Table
--     -- Qantas Flights
-- INSERT INTO flight(code, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('QN111', '2021-01-01 08:00', '2021-01-01 13:00', '1', '3', '4', '1');
-- INSERT INTO flight(code, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('QN112', '2021-01-01 15:00', '2021-01-01 18:00', '3', '1', '4', '1');
-- INSERT INTO flight(code, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('QN113', '2021-01-02 19:00', '2021-01-02 21:00', '2', '3', '4', '2');
--     -- Virgin Flights
-- INSERT INTO flight(code, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('VA111', '2021-02-01 08:00', '2021-02-01 13:00', '1', '3', '5', '3');
-- INSERT INTO flight(code, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('VA112', '2021-02-01 15:00', '2021-02-01 18:00', '3', '1', '5', '3');
-- INSERT INTO flight(code, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('VA113', '2021-02-02 19:00', '2021-02-02 21:00', '2', '3', '5', '4');
--     -- Emirates Flights
-- INSERT INTO flight(code, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('EM111', '2021-03-01 08:00', '2021-03-01 13:00', '1', '3', '6', '5');
-- INSERT INTO flight(code, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('EM112', '2021-03-01 15:00', '2021-03-01 18:00', '3', '1', '6', '5');
-- INSERT INTO flight(code, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('EM113', '2021-03-02 19:00', '2021-03-02 21:00', '1', '3', '6', '5');
--
-- -- Stopover Table
--     -- Qantas
-- INSERT INTO stopover(flightId, duration, airportId) VALUES ('1', '2 hours', 'AVV');
--     -- Virgin
-- INSERT INTO stopover(flightId, duration, airportId) VALUES ('4', '2 hours', 'MEL');
--     -- Emirates
-- INSERT INTO stopover(flightId, duration, airportId) VALUES ('7', '2 hours', '2');
--
-- -- Booking Table
-- INSERT INTO booking(date, totalCost, reference, customerId, flight, returnFlight) VALUES ('2020-10-31', '21:08', '180.00', 'REF0001', '1', '1', '2');
-- INSERT INTO booking(date, totalCost, reference, customerId, flight) VALUES ('2020-10-31', '21:20', '60.00', 'REF0002', '1', '3');
--
-- INSERT INTO booking(date, totalCost, reference, customerId, flight, returnFlight) VALUES ('2020-11-24', '08:02', '105.00', 'REF0003', '2', '6', '8');
--
-- INSERT INTO booking(date, totalCost, reference, customerId, flight, returnFlight) VALUES ('2020-12-12', '11:08', '180.00', 'REF0004', '3', '1', '2');
-- INSERT INTO booking(date, totalCost, reference, customerId, flight) VALUES ('2020-12-12', '11:20', '60.00', 'REF0005', '3', '9');
--
-- -- Seat Table
-- INSERT INTO seat(seatNumber, bookingId, flightId, airplaneId, seatType) VALUES ('A1', '1', '1', '1', 'economy');
-- INSERT INTO seat(seatNumber, bookingId, flightId, airplaneId, seatType) VALUES ('A2', '1', '1', '1', 'economy');
--
--
-- -- Passenger Table
-- INSERT INTO passenger(bookingId, seatNumber, givenName, surname, age, nationality, passportNumber) VALUES ('1', 'A1', 'John', 'Doe', '25', 'Australian', 'PA11231');
-- INSERT INTO passenger(bookingId, seatNumber, givenName, surname, age, nationality, passportNumber) VALUES ('1', 'A2', 'Jane', 'Doe', '22', 'American', 'US1293');