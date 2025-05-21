#!/bin/bash

# Test the GET endpoint
echo "Testing GET /bfhl endpoint..."
curl -X GET http://localhost:8081/bfhl
echo -e "\n"

# Test the POST endpoint
echo "Testing POST /bfhl endpoint..."
curl -X POST -H "Content-Type: application/json" -d '{"data": ["a", "1", "334", "4", "R"]}' http://localhost:8081/bfhl
echo -e "\n"

# Get the SQL query
echo "Getting SQL query..."
curl -X GET http://localhost:8081/sql/query
echo -e "\n"

# Test SQL query submission with token
echo "To submit the SQL query to the actual webhook, run:"
echo "curl -X POST -H 'Authorization: YOUR_ACCESS_TOKEN' http://localhost:8081/sql/submit-with-token"
echo -e "\n" 