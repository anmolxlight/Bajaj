# Project Summary

This project implements a Spring Boot API as per the requirements specified in the Bajaj Finserv Health Limited Java Test.

## Project Structure

```
Bajaj/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bajaj/
│   │   │           └── bfhl/
│   │   │               ├── controller/
│   │   │               │   ├── BfhlController.java
│   │   │               │   └── SqlSubmissionController.java
│   │   │               ├── model/
│   │   │               │   ├── BfhlRequest.java
│   │   │               │   └── JsonResponse.java
│   │   │               ├── service/
│   │   │               │   ├── BfhlService.java
│   │   │               │   ├── BfhlServiceImpl.java
│   │   │               │   └── SqlSubmissionService.java
│   │   │               └── BfhlApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── bajaj/
│                   └── bfhl/
│                       └── BfhlApplicationTests.java
├── pom.xml
├── README.md
├── SUMMARY.md
├── test_api.ps1
└── test_api.sh
```

## Features

1. **BFHL API**
   - GET /bfhl: Returns operation code
   - POST /bfhl: Processes an array of data and separates numbers and alphabets

2. **SQL Query Submission**
   - POST /sql/submit: Submits the SQL query with an optional access token in header
   - POST /sql/submit-with-token: Submits the SQL query with a required access token in header
   - GET /sql/query: Returns the SQL query to be submitted
   - GET /sql/status: Returns the current status of the webhook and access token

## Technologies Used

- Java 11
- Spring Boot 2.7.0
- Maven
- RESTful API

## How to Run

1. Clone the repository
2. Navigate to the project directory
3. Run `mvn clean package` to build the project
4. Run `java -jar target/bfhl-0.0.1-SNAPSHOT.jar` to start the application
5. The application will start on port 8081

## Testing

Use the provided test scripts:
- test_api.ps1 (PowerShell)
- test_api.sh (Bash)

## Implementation Details

### Part 1: BFHL API

1. **API Endpoints**:
   - GET `/bfhl`: Returns operation code 1
   - POST `/bfhl`: Processes an array of data and returns separated numbers and alphabets

2. **Models**:
   - `BfhlRequest`: Contains a list of strings as input data
   - `JsonResponse`: Contains the response format with is_success, user_id, email, roll_number, numbers, and alphabets

3. **Service Layer**:
   - `BfhlService`: Interface defining the processData method
   - `BfhlServiceImpl`: Implementation that separates numbers and alphabets from the input data

4. **Controller**:
   - `BfhlController`: Handles the HTTP requests and returns appropriate responses

### Part 2: SQL Query Submission

1. **SQL Query**:
   ```sql
   SELECT DISTINCT c.customer_id, c.name, c.email, c.mobile, COUNT(o.order_id) AS total_orders 
   FROM customers c 
   JOIN orders o ON c.customer_id = o.customer_id 
   WHERE o.order_date >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY) 
   GROUP BY c.customer_id, c.name, c.email, c.mobile 
   HAVING COUNT(o.order_id) > 5 
   ORDER BY total_orders DESC;
   ```

2. **Service Layer**:
   - `SqlSubmissionService`: Handles the submission of the SQL query to the webhook URL

3. **Controller**:
   - `SqlSubmissionController`: Exposes endpoints for submitting the SQL query and retrieving information

4. **Submission Process**:
   - The SQL query can be submitted using an access token provided in the Authorization header
   - The access token must be obtained from the API provider

## Test Results

The API successfully processes the input data and returns the expected response format with separated numbers and alphabets. The SQL query submission functionality is ready to use with a valid access token. 