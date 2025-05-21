# Test the GET endpoint
Write-Host "Testing GET /bfhl endpoint..."
$getResponse = Invoke-WebRequest -Uri http://localhost:8081/bfhl -Method GET
Write-Host "Response: $($getResponse.Content)"
Write-Host ""

# Test the POST endpoint
Write-Host "Testing POST /bfhl endpoint..."
$body = @{data=@("a", "1", "334", "4", "R")} | ConvertTo-Json
$postResponse = Invoke-WebRequest -Uri http://localhost:8081/bfhl -Method POST -Body $body -ContentType "application/json"
Write-Host "Response: $($postResponse.Content)"
Write-Host ""

# Get the SQL query
Write-Host "Getting SQL query..."
$queryResponse = Invoke-WebRequest -Uri http://localhost:8081/sql/query -Method GET
Write-Host "Response: $($queryResponse.Content)"
Write-Host ""

# Test SQL query submission with token
Write-Host "To submit the SQL query to the actual webhook, run:"
Write-Host "Invoke-WebRequest -Uri http://localhost:8081/sql/submit-with-token -Method POST -Headers @{Authorization='YOUR_ACCESS_TOKEN'}"
Write-Host ""
Write-Host "Or use the following curl command:"
Write-Host "curl -X POST -H 'Authorization: YOUR_ACCESS_TOKEN' http://localhost:8081/sql/submit-with-token" 