#!/bin/bash

echo "🛑 Stopping Microservices Architecture..."
echo "======================================="

# Stop all services
echo "📦 Stopping services..."
docker compose down

# Remove volumes if requested
if [ "$1" = "--volumes" ]; then
    echo "🗑️  Removing volumes..."
    docker compose down -v
    echo "✅ All services and volumes stopped."
else
    echo "✅ All services stopped. Data is preserved."
    echo "💡 Use './stop-services.sh --volumes' to also remove data volumes."
fi

echo ""
echo "👋 Services stopped successfully!" 