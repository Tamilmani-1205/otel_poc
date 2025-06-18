#!/bin/bash

echo "🚀 Starting Microservices Architecture with Monitoring..."
echo "=================================================="

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Build and start all services
echo "📦 Building and starting services..."
docker compose up -d --build

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 30

# Check service status
echo "🔍 Checking service status..."
docker compose ps

echo ""
echo "✅ Services are starting up!"
echo ""
echo "🌐 Access Points:"
echo "   Product Management API: http://localhost:8080"
echo "   User Management API:    http://localhost:8081"
echo "   Prometheus:             http://localhost:9090"
echo "   Grafana:                http://localhost:3000"
echo ""
echo "🔑 Default Users:"
echo "   Admin:  username=admin, password=admin123"
echo "   User:   username=user,  password=user123"
echo ""
echo "📊 To view logs: docker compose logs -f"
echo "🛑 To stop services: docker compose down"
echo ""
echo "🎉 Enjoy your microservices architecture!" 