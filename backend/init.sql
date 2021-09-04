<<<<<<< Updated upstream
=======
y
>>>>>>> Stashed changes
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE "user"(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username varchar(255),
    password varchar(255),
    userType varchar(255)
);

CREATE TABLE customer(
     id UUID PRIMARY KEY,
     givenName varchar(255),
     surname varchar(255),
     email varchar(255),
     CONSTRAINT customerFK
        FOREIGN KEY(id)
            REFERENCES "user"(id) ON DELETE CASCADE
);



CREATE TABLE airline(
    id UUID PRIMARY KEY,
    code varchar(255),
    name varchar(255),
    CONSTRAINT airlineFK
        FOREIGN KEY(id)
            REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE administrator(
    id UUID PRIMARY KEY,
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
    flightId UUID NOT NULL,
    returnFlightId UUID,
    CONSTRAINT customerFK
        FOREIGN KEY(customerId)
            REFERENCES customer(id),
    CONSTRAINT flightFK
        FOREIGN KEY(flightId)
            REFERENCES flight(id),
    CONSTRAINT returnFlightFK
        FOREIGN KEY(returnFlightId)
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

<<<<<<< Updated upstream
CREATE TABLE passenger(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    givenName varchar(255),
    surname varchar(255),
    dob date,
    nationality varchar(255),
    passportNumber varchar(255)
);
=======
-- DO $FN$
-- BEGIN
--     FOR counter IN 1..100 LOOP
--         EXECUTE $$ INSERT INTO airline(name, id) VALUES ('airline'||$1, $1) RETURNING * $$
--             USING counter;
--     END LOOP;
-- END;
-- $FN$;

DO $FN$
    BEGIN
        EXECUTE $$ INSERT INTO passenger VALUES ('1', 'john', 'doe', '25', 'australian', 'p1123') RETURNING * $$;
        EXECUTE $$ INSERT INTO booking VALUES ('1', '01/01/21', '08:00', '25', '120f3') RETURNING * $$;
        EXECUTE $$ INSERT INTO passengerBooking VALUES ('1', '1') $$;
    END;
 $FN$;
>>>>>>> Stashed changes
