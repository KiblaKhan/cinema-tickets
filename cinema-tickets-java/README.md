# Cinema Tickets System

This project implements a cinema ticket purchasing system. It allows users to purchase tickets for adults, children, and infants, with validation rules to ensure the purchase is valid. The system calculates the total cost and reserves seats based on the ticket types and quantities provided.

## Features

### Ticket Types

Adult: £25 per ticket.

Child: £15 per ticket.

Infant: £0 per ticket (no seat reservation).

### Validation Rules

A valid account ID must be provided.

At least one ticket must be purchased.

A maximum of 20 tickets can be purchased in a single transaction.

Child and infant tickets cannot be purchased without an adult ticket.

The number of infant tickets cannot exceed the number of adult tickets.

### Payment and Seat Reservation

The system calculates the total cost and reserves seats based on the ticket types and quantities.

Payment and seat reservation are handled by third-party services (TicketPaymentService and SeatReservationService).


## How to Run Unit Tests

The project uses JUnit 5 and Mockito for unit testing. To run the unit tests, follow these steps:

1. Ensure Maven is installed. Download and install Maven from Apache Maven.

2. Navigate to the project directory

- cd cinema-tickets\cinema-tickets-java

### Run the unit tests

- mvn clean install (first time building the project)  
- mvn test

This will execute all the unit tests in the TicketServiceImplTest class.

## How to Run the Main Application

The main application is a command-line interface (CLI) that allows you to interact with the TicketServiceImpl class. To run the application, follow these steps:

#### Navigate to the project directory

- cd cinema-tickets\cinema-tickets-java

#### Compile the project

- mvn clean compile

#### Run the Main class

-  java -cp "target/classes;target/lib/*" uk.gov.dwp.uc.pairtest.Main

The application will prompt you for the following inputs:

Example Input:

Enter account ID: 123  
Enter number of adult tickets: 2  
Enter number of child tickets: 1  
Enter number of infant tickets: 1

Example Output:

Payment processed: Account ID = 123  
Total number of tickets: 4  
Total amount to pay: 65  
Total seats to reserve: 3  
Tickets comprise of:  
   Adult Tickets: 2  
   Child Tickets: 1  
   Infant Tickets: 1  
Ticket Service completed.  

## Dependencies

The project uses openjdk version 11.0.26 2025-01-21  and the following dependencies:

- JUnit 5: For unit testing.

- Mockito: For mocking dependencies in unit tests.

These dependencies are managed by Maven and are defined in the pom.xml file.

## Code Quality

Unit Tests: The project includes comprehensive unit tests to ensure the correctness of the TicketServiceImpl class.

Validation: The system enforces strict validation rules to prevent invalid purchases.

Logging: The system logs detailed information about each purchase for debugging and auditing purposes.

## Future Enhancements

Web Interface: Add a web-based interface using a framework like Spring Boot.

Database Integration: Store purchase history in a database using Spring Data JPA.

Logging Framework: Replace System.out.println with a logging framework like SLF4J or Log4j.

## Author

Kibla Khan  
Application ID number: 13288401

kibla.khan@gmail.com

Writen for Java Software Engineer role - 381321
