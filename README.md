# Online Travel Reservation System

## Team 2Pizzas 🍕
| Team Member | Username |
| - | - |
| Ben Nguyen | [@bennguyen96](https://github.com/bennguyen96) |
| Mahardini Rizky Putri | [@mhrdini](https://github.com/mhrdini) |
| Max Plumley | [@maxplumley](https://github.com/maxplumley) |
| Sothea-Roth Bak | [@sothbak](https://github.com/sothbak) |

## Submissions
| Submission | Release |
| - | - |
| Part 1 | [SWEN90007_2021_Part1_2Pizzas](https://github.com/SWEN900072021/2Pizzas/releases/tag/SWEN90007_2021_Part1_2Pizzas) |
| Part 2 | [SWEN90007_2021_Part2_2Pizzas](https://github.com/SWEN900072021/2Pizzas/releases/tag/SWEN90007_2021_Part2_2Pizzas) |
| Part 3 | [SWEN90007_2021_Part3_2Pizzas](https://github.com/SWEN900072021/2Pizzas/releases/tag/SWEN90007_2021_Part3_2Pizzas) |
| Part 4 | [SWEN90007_2021_Part3_2Pizzas](https://github.com/SWEN900072021/2Pizzas/releases/tag/SWEN90007_2021_Part4_2Pizzas) |

# Part 2 - Application 

Link to the application [here](https://frontend-2-pizzas.herokuapp.com/)

## Accounts
There are various account types present in the application which can be accessed with the following credentials:
| User Type | Username | Password |
| - | - | - |
| Administrator | *admin* | *password*
| Airline | *qantas* | *password* |
| Airline | *virgin* | *password* |
| Airline | *emirates* | *password* |
| Customer | *john* | *password* |
| Customer | *jane* | *password* |
| Customer | *james* | *password* |

To log in with these accounts, click on the 'Log In' button on the top right of the screen

## Database
#### administrator
A user entity that is able to create new airports in the database and new airline accounts

#### airline
A user entity that represents an airline that is able to create new flights for their airline 

#### airplaneprofile
A description of particular airplanes which are used in the process of creating flights to know the seating layout

#### airport
An entity used for stating the departure and arrival location of flights

#### booking
An entity which contains the details of a users' booking, this includes the flight details, passenger details and the seating allocations

#### customer
A user entity that is able to book flights

#### flight
An entity which contains the details of a specific flight which include the airline, airplane and the departure and arrival locations and times

#### passenger
An entity that exists when a booking is created to represent the individual that is travelling in the booking

#### seat
An entity used to represent an individual seat on a flight

#### seatallocation
An entity used to represent a passenger allocated to a seat

#### stopover
An entity used to represent a stopover within a flight

#### user
The parent of Administrator, Airline and Customer

## Common Flows
#### As Admin
Viewing existing airlines
  - Once logged in, click on the 'View Airlines' tab on the left of the dashboard, this will provide a list of all existing airlines in the database.

Viewing existing airports
  - Once logged in, click on the 'View Airports' tab on the left of the dashboard, this will provide a list of all existing airports in the database.

Creating new airline account
  - Once logged in, click on the 'Add Airline' tab on the left of the dashboard, enter in the airline Name, Code, and assign a Username and Password. This will create an airline account that will persist in the database.

Creating new airport
  - Once logged in, click on the 'Add Airport' tab on the left of the dashboard, enter in the airport Name, Code, Location and select a Time Zone from the options available. This will create an airport that will persist in the database.

#### As Airline
Viewing existing flights
  - Once logged in, click on the 'View Flights' tab on the left of the dashboard, this will provide a list of all flights that the airline has created

Creating new flight
  - Once logged in, click on the 'Create Flight' tab on the left of the dashboard, enter in the Flight Code, select the type of airplane the flight will be using under Airplane Profile, enter the costs of an individual seat in each class, select the departing and arrival airports, select the departure and arrival times and finally, if there are any stopovers, click 'Add Stopover' and enter in the stopover airport and the specified arrival and departure times, more than one stopover can be created by pressing the 'Add Stopover' button again. This will create a flight that will persist in the database.

#### As Customer
Viewing current bookings
  - Once logged in, open the dashboard by clicking the 'Hi, *username*' button on the top right and then clicking 'Dashboard'. Once at the dashboard, click 'Current Bookings', this will provide a list of all scheduled bookings that a customer has booked. The customer can view the details of the flights within the booking and the passengers who are booked on those flights.

Viewing previous bookings
  - Once logged in, open the dashboard by clicking the 'Hi, *username*' button on the top right and then clicking 'Dashboard'. Once at the dashboard, click 'Previous Bookings', this will provide a list of all past bookings that a customer has booked. The customer can view the details of the flights within the booking and the passengers who were booked on those flights.

Creating a booking
  - Once logged in, a customer can create a booking after searching and selecting for a flight. When a flight is selected, click 'Confirm' to move onto the booking page. Once on the booking page, for each passenger, enter the given name, surname, passport number, nationality, date of birth, and select the flight class and seat for each flight. Once finished, click 'Submit', this will create a booking that will persist in the database.
