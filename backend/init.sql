DROP SCHEMA public CASCADE;

CREATE SCHEMA public;

CREATE TABLE customer(
     customerId SERIAL PRIMARY KEY,
     firstName varchar(255),
     surname varchar(255),
     email varchar(255)
);

CREATE TABLE airline(
    airlineId SERIAL PRIMARY KEY,
    airlineCode varchar(255),
    name varchar(255)
);

CREATE TABLE administrator(
    adminId SERIAL PRIMARY KEY
);

CREATE TABLE airport(
    airportId SERIAL PRIMARY KEY,
    airportCode char(3),
    name varchar(255),
    location varchar(255),
    adminId integer,
    CONSTRAINT adminFK
        FOREIGN KEY(adminId)
            REFERENCES administrator(adminId)
);

CREATE TYPE airplaneType as ENUM(
    'airbus',
    'boeing'
);

CREATE TABLE airplane(
    airplaneId SERIAL PRIMARY KEY,
    planeCode varchar(255),
    airplaneType airplaneType,
    airlineId integer,
    CONSTRAINT airlineId
        FOREIGN KEY(airlineId)
            REFERENCES airline(airlineId)
);

CREATE TABLE flight(
    flightId SERIAL PRIMARY KEY,
    flightCode varchar(255),
    departureTime time,
    arrivalTime time,
    origin integer,
    destination integer,
    airlineId integer,
    airplaneId integer,
    CONSTRAINT airlineFK
        FOREIGN KEY(airlineId)
            REFERENCES airline(airlineId),
    CONSTRAINT originFK
        FOREIGN KEY(origin)
            REFERENCES airport(airportId),
    CONSTRAINT destinationFK
        FOREIGN KEY(destination)
            REFERENCES airport(airportId),
   CONSTRAINT airplaneFK
        FOREIGN KEY(airplaneId)
            REFERENCES airplane(airplaneId)
);

CREATE TABLE stopover(
    flightId integer,
    duration interval,
    location integer,
    CONSTRAINT flightFK
        FOREIGN KEY(flightId)
            REFERENCES flight(flightId),
    CONSTRAINT locationFK
        FOREIGN KEY(location)
            REFERENCES airport(airportId)
);


CREATE TABLE booking(
    bookingId SERIAL PRIMARY KEY,
    date DATE,
    time time,
    totalCost varchar(255),
    bookingReference varchar(255),
    customerId integer,
    flight integer NOT NULL,
    returnFlight integer,
    CONSTRAINT customerFK
        FOREIGN KEY(customerId)
            REFERENCES customer(customerId),
    CONSTRAINT flightFK
        FOREIGN KEY(flight)
            REFERENCES flight(flightId),
    CONSTRAINT returnFlightFK
        FOREIGN KEY(returnFlight)
            REFERENCES flight(flightId)
);

CREATE TABLE passenger(
    bookingId integer,
    givenName varchar(255),
    surname varchar(255),
    age INTEGER,
    nationality varchar(255),
    passportNumber varchar(255),
    CONSTRAINT bookingFK
        FOREIGN KEY(bookingId)
            REFERENCES booking(bookingId)
);

CREATE TYPE seatType as ENUM(
    'first',
    'business',
    'economy'
    );

CREATE TABLE seat(
    bookingId integer,
    flightId integer,
    airplaneId integer,
    seatType seatType,
    CONSTRAINT bookingFK
        FOREIGN KEY(bookingId)
            REFERENCES booking(bookingId),
    CONSTRAINT flightFK
        FOREIGN KEY(flightId)
            REFERENCES flight(flightId),
    CONSTRAINT airplaneFK
        FOREIGN KEY(airplaneId)
            REFERENCES airplane(airplaneId)
);


-- DO $FN$
-- BEGIN
--     FOR counter IN 1..100 LOOP
--         EXECUTE $$ INSERT INTO airline(name, id) VALUES ('airline'||$1, $1) RETURNING * $$
--             USING counter;
--     END LOOP;
-- END;
-- $FN$;

-- DO $FN$
--     BEGIN
--         EXECUTE $$ INSERT INTO passenger VALUES ('1', 'john', 'doe', '25', 'australian', 'p1123') RETURNING * $$;
--         EXECUTE $$ INSERT INTO booking VALUES ('1', '01/01/21', '08:00', '25', '120f3') RETURNING * $$;
--         EXECUTE $$ INSERT INTO passengerBooking VALUES ('1', '1') $$;
--     END;
--  $FN$;
