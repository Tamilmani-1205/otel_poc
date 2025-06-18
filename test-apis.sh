#!/bin/bash

echo "🧪 Testing Microservices APIs..."
echo "================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to test endpoint
test_endpoint() {
    local url=$1
    local description=$2
    local method=${3:-GET}
    local data=${4:-""}
    
    echo -n "Testing $description... "
    
    if [ "$method" = "POST" ] && [ -n "$data" ]; then
        response=$(curl -s -w "%{http_code}" -X $method -H "Content-Type: application/json" -d "$data" "$url")
    else
        response=$(curl -s -w "%{http_code}" -X $method "$url")
    fi
    
    http_code="${response: -3}"
    body="${response%???}"
    
    if [ "$http_code" -ge 200 ] && [ "$http_code" -lt 300 ]; then
        echo -e "${GREEN}✅ Success (HTTP $http_code)${NC}"
    else
        echo -e "${RED}❌ Failed (HTTP $http_code)${NC}"
        echo "   Response: $body"
    fi
}

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 10

# Test Product Management Service
echo ""
echo "📦 Testing Product Management Service..."
test_endpoint "http://localhost:8080/actuator/health" "Health Check"
test_endpoint "http://localhost:8080/api/products" "Get Products"

# Test User Management Service
echo ""
echo "👤 Testing User Management Service..."
test_endpoint "http://localhost:8081/actuator/health" "Health Check"

# Test user registration
register_data='{
    "username": "testuser",
    "email": "test@example.com",
    "password": "testpass123",
    "firstName": "Test",
    "lastName": "User"
}'

test_endpoint "http://localhost:8081/api/auth/register" "User Registration" "POST" "$register_data"

# Test login
login_data='{
    "usernameOrEmail": "admin",
    "password": "admin123"
}'

test_endpoint "http://localhost:8081/api/auth/login" "Admin Login" "POST" "$login_data"

# Test Prometheus
echo ""
echo "📊 Testing Monitoring..."
test_endpoint "http://localhost:9090/-/healthy" "Prometheus Health"

echo ""
echo "🎉 API Testing Complete!"
echo ""
echo "💡 If any tests failed, make sure:"
echo "   1. All services are running: docker compose ps"
echo "   2. Services are healthy: docker compose logs"
echo "   3. Ports are available: 8080, 8081, 9090, 3000" 