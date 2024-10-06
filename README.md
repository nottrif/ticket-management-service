# Ticket Management Service

## Overview
Since the problem left room for interpretation for seats to be limited or not, I have chosen to mirror the real-world scenario where seats are limited.
I have assumed each section to have **50** seats (A1 - A50 and B1 - B50).

The application contains **unit tests** for service methods using **Junit 5**. The tests are located in the `src/test` directory.

The application has a **Github Actions** workflow which will run on pull request creation and on commits to the PR branch, it runs the tests and builds the application.

The Ticket Management Service is a Spring Boot application that provides an API for managing train ticket operations. It allows users to purchase tickets, retrieve receipts, get users by section, remove users, and modify seats.

## Prerequisites
- Java 17
- Gradle
- Docker (optional, for containerization)

## Installation

### Clone the Repository
```sh
git clone https://github.com/nottrif/ticket-management-service.git
cd ticket-management-service
```

### Build the Application
```sh
./gradlew clean build
```

### Run the Application
```sh
./gradlew bootRun
```

## Docker

### Build Docker Image
```sh
./gradlew jibDockerBuild
```

### Run Docker Container
```sh
docker run -p 8080:8080 ticket-management-service:1.0
```

## API Endpoints

### Purchase Ticket
- **URL**: `/api/v1/tickets/purchase`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "section": "A",
    "seat": "1"
  }
  ```
- **Response**:
    - **201 Created**: Returns the receipt.
    - **400 Bad Request**: Returns an error message if the seat is not available or the user already has a ticket.
    - **500 Internal Server Error**: Returns an error message for unexpected errors.

### Retrieve Receipt
- **URL**: `/api/v1/tickets/receipt/{email}`
- **Method**: `GET`
- **Response**:
    - **200 OK**: Returns the receipt.
    - **404 Not Found**: Returns an error message if the ticket is not found.

### Get Users by Section
- **URL**: `/api/v1/tickets/users-by-section`
- **Method**: `GET`
- **Query Parameter**: `section`
- **Response**:
    - **200 OK**: Returns a map of users and their seats.
    - **404 Not Found**: Returns an error message if no users are found in the section.

### Remove User
- **URL**: `/api/v1/tickets/remove/{email}`
- **Method**: `DELETE`
- **Response**:
    - **200 OK**: Returns a success message if the user is removed.
    - **404 Not Found**: Returns an error message if the user is not found.

### Modify Seat
- **URL**: `/api/v1/tickets/modify`
- **Method**: `PUT`
- **Request Body**:
  ```json
  {
    "email": "john.doe@example.com",
    "section": "A",
    "seat": "2"
  }
  ```
- **Response**:
    - **200 OK**: Returns the updated receipt.
    - **400 Bad Request**: Returns an error message if the new seat is not available or no ticket is found for the user.
    - **500 Internal Server Error**: Returns an error message for unexpected errors.

## Running Tests
```sh
./gradlew test
```

## CI/CD
The project uses GitHub Actions for continuous integration. The workflow is defined in `.github/workflows/gradle-build-test.yaml`.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.