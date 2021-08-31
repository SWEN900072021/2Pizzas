-- User Table
INSERT INTO "user"(userId, username, password) VALUES ('1', 'customer_1', 'password_1');
INSERT INTO "user"(userId, username, password) VALUES ('2', 'customer_2', 'password_2');
INSERT INTO "user"(userId, username, password) VALUES ('3', 'customer_3', 'password_3');

INSERT INTO "user"(userId, username, password) VALUES ('4', 'airline_1', 'password_1');
INSERT INTO "user"(userId, username, password) VALUES ('5', 'airline_2', 'password_2');
INSERT INTO "user"(userId, username, password) VALUES ('6', 'airline_3', 'password_3');

INSERT INTO "user"(userId, username, password) VALUES ('7', 'admin_1', 'password_1');
INSERT INTO "user"(userId, username, password) VALUES ('8', 'admin_2', 'password_2');
INSERT INTO "user"(userId, username, password) VALUES ('9', 'admin_3', 'password_3');

-- Customer Table
INSERT INTO customer(customerId, firstName, surname, email) VALUES ('1', 'John', 'Doe', 'johndoe@gmail.com');
INSERT INTO customer(customerId, firstName, surname, email) VALUES ('2', 'Jane', 'Doe', 'janedoe@gmail.com');
INSERT INTO customer(customerId, firstName, surname, email) VALUES ('3', 'James', 'Doe', 'jamesdoe@gmail.com');

-- Airline Table
INSERT INTO airline(airlineId, airlineCode, name) VALUES ('4', 'QFA', 'Qantas');
INSERT INTO airline(airlineId, airlineCode, name) VALUES ('5', 'VIR', 'Virgin');
INSERT INTO airline(airlineId, airlineCode, name) VALUES ('6', 'UAE', 'Emirates');

-- Admin Table
INSERT INTO administrator(adminId) VALUES ('7');
INSERT INTO administrator(adminId) VALUES ('8');
INSERT INTO administrator(adminId) VALUES ('9');

-- Airport Table
INSERT INTO airport(airportId, airportCode, name, location, adminId) VALUES ('1', 'MEL', 'Tullamarine Airport', 'Melbourne', '7');
INSERT INTO airport(airportId, airportCode, name, location, adminId) VALUES ('2', 'AVV', 'Avalon Airport', 'Geelong', '7');
INSERT INTO airport(airportId, airportCode, name, location, adminId) VALUES ('3', 'SYD', 'Sydney Airport', 'Sydney', '8');

-- Airplane Table
    -- Qantas Planes
INSERT INTO airplane(airplaneId, planeCode, airplaneType, airlineId) VALUES ('1', 'planecode_1', 'boeing', '4');
INSERT INTO airplane(airplaneId, planeCode, airplaneType, airlineId) VALUES ('2', 'planecode_2', 'airbus', '4');
    -- Virgin Planes
INSERT INTO airplane(airplaneId, planeCode, airplaneType, airlineId) VALUES ('3', 'planecode_3', 'boeing', '5');
INSERT INTO airplane(airplaneId, planeCode, airplaneType, airlineId) VALUES ('4', 'planecode_4', 'airbus', '5');
    -- Emirates Planes
INSERT INTO airplane(airplaneId, planeCode, airplaneType, airlineId) VALUES ('5', 'planecode_5', 'boeing', '6');

-- Flight Table
    -- Qantas Flights
INSERT INTO flight(flightId, flightCode, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('1', 'QN111', '2021-01-01 08:00', '2021-01-01 13:00', '1', '3', '4', '1');
INSERT INTO flight(flightId, flightCode, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('2', 'QN112', '2021-01-01 15:00', '2021-01-01 18:00', '3', '1', '4', '1');
INSERT INTO flight(flightId, flightCode, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('3', 'QN113', '2021-01-02 19:00', '2021-01-02 21:00', '2', '3', '4', '2');
    -- Virgin Flights
INSERT INTO flight(flightId, flightCode, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('4', 'VA111', '2021-02-01 08:00', '2021-02-01 13:00', '1', '3', '5', '3');
INSERT INTO flight(flightId, flightCode, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('5', 'VA112', '2021-02-01 15:00', '2021-02-01 18:00', '3', '1', '5', '3');
INSERT INTO flight(flightId, flightCode, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('6', 'VA113', '2021-02-02 19:00', '2021-02-02 21:00', '2', '3', '5', '4');
    -- Emirates Flights
INSERT INTO flight(flightId, flightCode, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('7', 'EM111', '2021-03-01 08:00', '2021-03-01 13:00', '1', '3', '6', '5');
INSERT INTO flight(flightId, flightCode, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('8', 'EM112', '2021-03-01 15:00', '2021-03-01 18:00', '3', '1', '6', '5');
INSERT INTO flight(flightId, flightCode, departureTime, arrivalTime, origin, destination, airlineId, airplaneId) VALUES ('9', 'EM113', '2021-03-02 19:00', '2021-03-02 21:00', '1', '3', '6', '5');

-- Stopover Table
    -- Qantas
INSERT INTO stopover(flightId, duration, location) VALUES ('1', '2 hours', '2');
    -- Virgin
INSERT INTO stopover(flightId, duration, location) VALUES ('4', '2 hours', '2');
    -- Emirates
INSERT INTO stopover(flightId, duration, location) VALUES ('7', '2 hours', '2');

-- Booking Table
INSERT INTO booking(bookingId, date, time, totalCost, bookingReference, customerId, flight, returnFlight) VALUES ('1', '2020-10-31', '21:08', '180.00', 'REF0001', '1', '1', '2');
INSERT INTO booking(bookingId, date, time, totalCost, bookingReference, customerId, flight) VALUES ('2', '2020-10-31', '21:20', '60.00', 'REF0002', '1', '3');

INSERT INTO booking(bookingId, date, time, totalCost, bookingReference, customerId, flight, returnFlight) VALUES ('3', '2020-11-24', '08:02', '105.00', 'REF0003', '2', '6', '8');

INSERT INTO booking(bookingId, date, time, totalCost, bookingReference, customerId, flight, returnFlight) VALUES ('4', '2020-12-12', '11:08', '180.00', 'REF0004', '3', '1', '2');
INSERT INTO booking(bookingId, date, time, totalCost, bookingReference, customerId, flight) VALUES ('5', '2020-12-12', '11:20', '60.00', 'REF0005', '3', '9');

-- Seat Table
INSERT INTO seat(seatNumber, bookingId, flightId, airplaneId, seatType) VALUES ('A1', '1', '1', '1', 'economy');
INSERT INTO seat(seatNumber, bookingId, flightId, airplaneId, seatType) VALUES ('A2', '1', '1', '1', 'economy');


-- Passenger Table
INSERT INTO passenger(bookingId, seatNumber, givenName, surname, age, nationality, passportNumber) VALUES ('1', 'A1', 'John', 'Doe', '25', 'Australian', 'PA11231');
INSERT INTO passenger(bookingId, seatNumber, givenName, surname, age, nationality, passportNumber) VALUES ('1', 'A2', 'Jane', 'Doe', '22', 'American', 'US1293');