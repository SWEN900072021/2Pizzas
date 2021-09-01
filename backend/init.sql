DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE "user"(
    id varchar(36) PRIMARY KEY,
    username varchar(255),
    password varchar(255)
);

CREATE TABLE customer(
     id varchar(36) PRIMARY KEY,
     firstName varchar(255),
     surname varchar(255),
     email varchar(255),
     CONSTRAINT customerFK
        FOREIGN KEY(id)
            REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE airline(
    id varchar(36) PRIMARY KEY,
    code varchar(255),
    name varchar(255),
    CONSTRAINT airlineFK
        FOREIGN KEY(id)
            REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE administrator(
    id varchar(36) PRIMARY KEY,
    CONSTRAINT adminFK
        FOREIGN KEY(id)
            REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE airport(
    id varchar(36) PRIMARY KEY,
    code char(3),
    name varchar(255),
    location varchar(255),
    utcOffset numeric
);

CREATE TABLE airplane(
    id varchar(36) PRIMARY KEY,
    code varchar(255),
    type varchar(255),
    firstClassRows integer,
    firstClassColumns integer,
    businessClassRows integer,
    businessClassColumns integer,
    economyClassRows integer,
    economyClassColumns integer
);

CREATE TABLE flight(
    id varchar(36) PRIMARY KEY,
    code varchar(255),
    departureTime timestamp,
    arrivalTime timestamp,
    origin varchar(36),
    destination varchar(36),
    airlineId varchar(36),
    airplaneId varchar(36),
    status varchar(255),
    CONSTRAINT airlineFK
        FOREIGN KEY(airlineId)
            REFERENCES airline(id),
    CONSTRAINT originFK
        FOREIGN KEY(origin)
            REFERENCES airport(id),
    CONSTRAINT destinationFK
        FOREIGN KEY(destination)
            REFERENCES airport(id),
   CONSTRAINT airplaneFK
        FOREIGN KEY(airplaneId)
            REFERENCES airplane(id)
);

CREATE TABLE stopover(
    flightId varchar(36),
    duration interval,
    airportId varchar(36),
    CONSTRAINT flightFK
        FOREIGN KEY(flightId)
            REFERENCES flight(id) ON DELETE CASCADE,
    CONSTRAINT locationFK
        FOREIGN KEY(airportId)
            REFERENCES airport(id),
    UNIQUE(flightId, airportId)
);


CREATE TABLE booking(
    id varchar(36) PRIMARY KEY,
    date timestamp,
    totalCost numeric,
    reference varchar(255),
    customerId varchar(36),
    flight varchar(36) NOT NULL,
    returnFlight varchar(36),
    CONSTRAINT customerFK
        FOREIGN KEY(customerId)
            REFERENCES customer(id),
    CONSTRAINT flightFK
        FOREIGN KEY(flight)
            REFERENCES flight(id),
    CONSTRAINT returnFlightFK
        FOREIGN KEY(returnFlight)
            REFERENCES flight(id)
);

CREATE TABLE seat(
    id varchar(36) PRIMARY KEY,
    row varchar(255),
    "column" varchar(255),
    flightId varchar(36),
    seatType varchar(255),
    status varchar(255),
    CONSTRAINT flightFK
        FOREIGN KEY(flightId)
            REFERENCES flight(id) ON DELETE CASCADE
);

CREATE TABLE passenger(
    bookingId varchar(36),
    seatId varchar(36),
    givenName varchar(255),
    surname varchar(255),
    dob date,
    nationality varchar(255),
    passportNumber varchar(255),
    CONSTRAINT bookingFK
        FOREIGN KEY(bookingId)
            REFERENCES booking(id) ON DELETE CASCADE,
    CONSTRAINT seatFK
        FOREIGN KEY(seatId)
            REFERENCES seat(id)
);
