DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE "user"(
    userId SERIAL PRIMARY KEY,
    username varchar(255),
    password varchar(255)
);

CREATE TABLE customer(
     customerId integer PRIMARY KEY,
     firstName varchar(255),
     surname varchar(255),
     email varchar(255),
     CONSTRAINT customerFK
        FOREIGN KEY(customerId)
            REFERENCES "user"(userId) ON DELETE CASCADE
);

CREATE TABLE airline(
    airlineId integer PRIMARY KEY,
    airlineCode varchar(255),
    name varchar(255),
    CONSTRAINT airlineFK
        FOREIGN KEY(airlineId)
            REFERENCES "user"(userId) ON DELETE CASCADE
);

CREATE TABLE administrator(
    adminId integer PRIMARY KEY,
    CONSTRAINT adminFK
        FOREIGN KEY(adminId)
            REFERENCES "user"(userId) ON DELETE CASCADE
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
    departureTime timestamp,
    arrivalTime timestamp,
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
            REFERENCES flight(flightId) ON DELETE CASCADE,
    CONSTRAINT locationFK
        FOREIGN KEY(location)
            REFERENCES airport(airportId),
    PRIMARY KEY (flightId, location)
);


CREATE TABLE booking(
    bookingId SERIAL PRIMARY KEY,
    date DATE,
    time time,
    totalCost numeric,
    bookingReference varchar(255),
    customerId integer,
    flight integer NOT NULL,
    returnFlight integer,
    CONSTRAINT customerFK
        FOREIGN KEY(customerId)
            REFERENCES customer(customerId),
    CONSTRAINT flightFK
        FOREIGN KEY(flight)
            REFERENCES flight(flightId) ON DELETE CASCADE,
    CONSTRAINT returnFlightFK
        FOREIGN KEY(returnFlight)
            REFERENCES flight(flightId) ON DELETE CASCADE
);

CREATE TYPE seatType as ENUM(
    'first',
    'business',
    'economy'
    );

CREATE TABLE seat(
    seatNumber varchar(255) UNIQUE,
    bookingId integer,
    flightId integer,
    airplaneId integer,
    seatType seatType,
    CONSTRAINT bookingFK
        FOREIGN KEY(bookingId)
            REFERENCES booking(bookingId) ON DELETE CASCADE,
    CONSTRAINT flightFK
        FOREIGN KEY(flightId)
            REFERENCES flight(flightId) ON DELETE CASCADE,
    CONSTRAINT airplaneFK
        FOREIGN KEY(airplaneId)
            REFERENCES airplane(airplaneId) ON DELETE CASCADE,
    PRIMARY KEY (bookingId, seatNumber)
);

CREATE TABLE passenger(
    bookingId integer,
    seatNumber varchar(255),
    givenName varchar(255),
    surname varchar(255),
    age INTEGER,
    nationality varchar(255),
    passportNumber varchar(255),
    CONSTRAINT bookingFK
      FOREIGN KEY(bookingId)
          REFERENCES booking(bookingId) ON DELETE CASCADE,
    CONSTRAINT seatNumber
        FOREIGN KEY(seatNumber)
            REFERENCES seat(seatNumber) ON DELETE CASCADE,
    PRIMARY KEY (bookingId, seatNumber)
);
