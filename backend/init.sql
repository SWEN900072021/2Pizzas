DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE "user"(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username varchar(255),
    password varchar(255),
    userType varchar(255)
);

CREATE TABLE customer(
     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
     firstName varchar(255),
     surname varchar(255),
     email varchar(255),
     CONSTRAINT customerFK
        FOREIGN KEY(id)
            REFERENCES "user"(id) ON DELETE CASCADE
);



CREATE TABLE airline(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code varchar(255),
    name varchar(255),
    CONSTRAINT airlineFK
        FOREIGN KEY(id)
            REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE administrator(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    CONSTRAINT adminFK
        FOREIGN KEY(id)
            REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE airport(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code char(3),
    name varchar(255),
    location varchar(255),
    utcOffset varchar(255)
);

CREATE TABLE airplane(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
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
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code varchar(255),
    departureTime timestamp,
    arrivalTime timestamp,
    origin UUID,
    destination UUID,
    airlineId UUID,
    airplaneId UUID,
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
    flightId UUID,
    duration interval,
    airportId UUID,
    CONSTRAINT flightFK
        FOREIGN KEY(flightId)
            REFERENCES flight(id) ON DELETE CASCADE,
    CONSTRAINT locationFK
        FOREIGN KEY(airportId)
            REFERENCES airport(id),
    UNIQUE(flightId, airportId)
);

CREATE TABLE booking(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    date timestamp,
    totalCost numeric,
    reference varchar(255),
    customerId UUID,
    flight UUID NOT NULL,
    returnFlight UUID,
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
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name varchar(255),
    seatClass varchar(255),
    status varchar(255)
);

CREATE TABLE seatAllocation(
     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
     flightId UUID,
     bookingId UUID,
     passengerId UUID,
     seatId UUID,
     CONSTRAINT flightFK
         FOREIGN KEY(flightId)
             REFERENCES flight(id) ON DELETE CASCADE,
     CONSTRAINT bookingFK
            FOREIGN KEY(bookingId)
             REFERENCES booking(id) ON DELETE CASCADE,
     CONSTRAINT seatFK
         FOREIGN KEY(seatId)
             REFERENCES seat(id),
    CONSTRAINT passengerFK
        FOREIGN KEY(passengerId)
             REFERENCES passenger(id)
);

CREATE TABLE passenger(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    givenName varchar(255),
    surname varchar(255),
    dob date,
    nationality varchar(255),
    passportNumber varchar(255)
);
