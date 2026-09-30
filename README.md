# Bajaj Finserv Health Limited API

This is a Spring Boot application that implements the BFHL API as per the requirements.

## Requirements
- Java 11
- Maven

## Features
- Uses RestTemplate for HTTP communication
- Implements JWT-based authentication for the SQL submission API
- Automatically triggers SQL query submission on application startup (no endpoint required)

## How to Run
1. Clone the repository
2. Navigate to the project directory
3. Build the project:
   ```
   mvn clean package
   ```
4. Run the application:
   ```
   mvn spring-boot:run
   ```
   or
   ```
   java -jar target/bfhl-0.0.1-SNAPSHOT.jar
   ```
5. The application will start on port 8081
6. The SQL query will be automatically submitted on startup

## Note on Webhook Authentication
The application attempts to submit the SQL query to the webhook with multiple authentication methods:
1. Using Bearer token in Authorization header
2. Using plain token in Authorization header
3. Using custom headers (x-access-token, x-api-key, etc.)

However, the webhook API may require specific credentials or may not be accessible. If you have the correct credentials, you can add them to `application.properties` in the `bfhl.webhook.token` property.

## API Endpoints

### 1. GET /bfhl
Returns the operation code.

**Response:**
```json
{
  "operation_code": 1
}
```

### 2. POST /bfhl
Processes an array of data and returns the separated numbers and alphabets.

**Request Body:**
```json
{
  "data": ["a", "1", "334", "4", "R"]
}
```

**Response:**
```json
{
  "is_success": true,
  "user_id": "anmol_sharma_16052001",
  "email": "anmol0516.be20@chitkara.edu.in",
  "roll_number": "2011981051",
  "numbers": ["1", "334", "4"],
  "alphabets": ["a", "R"]
}
```

### 3. SQL Query Submission

The SQL query is automatically submitted when the application starts. However, the following endpoints are still available:

#### POST /sql/submit-with-token
Submits the SQL query to the webhook URL using the provided JWT token.

**Headers:**
```
Authorization: <jwtToken>
```

**Response:**
The response from the webhook API.

#### POST /sql/submit
Submits the SQL query to the webhook URL using the stored JWT token or one provided in the header.

**Headers (optional):**
```
Authorization: <jwtToken>
```

**Response:**
The response from the webhook API.

#### GET /sql/query
Returns the SQL query that will be submitted.

**Response:**
```json
{
  "finalQuery": "SELECT DISTINCT c.customer_id, c.name, c.email, c.mobile, COUNT(o.order_id) AS total_orders FROM customers c JOIN orders o ON c.customer_id = o.customer_id WHERE o.order_date >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY) GROUP BY c.customer_id, c.name, c.email, c.mobile HAVING COUNT(o.order_id) > 5 ORDER BY total_orders DESC;"
}
```

#### GET /sql/status
Returns the current status of the webhook and JWT token.

**Response:**
```json
{
  "hasAccessToken": true,
  "webhookUrl": "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA"
}
```

## SQL Query

The SQL query implemented for the second part of the assignment is:

```sql
SELECT DISTINCT
    c.customer_id,
    c.name,
    c.email,
    c.mobile,
    COUNT(o.order_id) AS total_orders
FROM
    customers c
JOIN
    orders o ON c.customer_id = o.customer_id
WHERE
    o.order_date >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY)
GROUP BY
    c.customer_id, c.name, c.email, c.mobile
HAVING
    COUNT(o.order_id) > 5
ORDER BY
    total_orders DESC;
```

This query retrieves customers who have placed more than 5 orders in the last 30 days, ordered by the number of orders in descending order. 