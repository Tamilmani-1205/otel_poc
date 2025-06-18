# Microservices Architecture with Monitoring

This project demonstrates a microservices architecture with two Spring Boot services:
1. **Product Management Service** - Manages product catalog and inventory
2. **User Management Service** - Handles user authentication, authorization, and user management

## Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Product Mgmt  │    │   User Mgmt     │    │   Monitoring    │
│   Service       │    │   Service       │    │   Stack         │
│   (Port: 8080)  │    │   (Port: 8081)  │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    │   Database      │
                    │                 │
                    │ - productdb     │
                    │   (shared)      │
                    └─────────────────┘
```

## Services

### 1. Product Management Service
- **Port**: 8080
- **Database**: productdb (shared)
- **Features**:
  - Product CRUD operations
  - Inventory management
  - RESTful API endpoints
  - Prometheus metrics

### 2. User Management Service
- **Port**: 8081
- **Database**: productdb (shared)
- **Features**:
  - User registration and authentication
  - Role-based access control (RBAC)
  - User CRUD operations
  - Password encryption
  - JWT token support (placeholder)
  - Prometheus metrics

## Monitoring Stack

### Prometheus
- **Port**: 9090
- **Purpose**: Metrics collection and storage
- **Configuration**: `prometheus/prometheus.yml`

### Grafana
- **Port**: 3000
- **Purpose**: Metrics visualization and dashboards
- **Default Credentials**: admin/admin

## Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 17 (for local development)

### Option 1: Using Convenience Scripts (Recommended)

1. **Start all services**:
   ```bash
   ./start-services.sh
   ```

2. **Test the APIs**:
   ```bash
   ./test-apis.sh
   ```

3. **Stop all services**:
   ```bash
   ./stop-services.sh
   ```

### Option 2: Manual Docker Compose

1. **Clone and navigate to the project**:
   ```bash
   cd otel_poc_8
   ```

2. **Start all services**:
   ```bash
   docker compose up -d
   ```

3. **Verify services are running**:
   ```bash
   docker compose ps
   ```

### Access Points

- **Product Management API**: http://localhost:8080
- **User Management API**: http://localhost:8081
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000

## API Documentation

### Product Management Service

#### Endpoints
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

#### Example Request
```bash
curl -X GET http://localhost:8080/api/products
```

### User Management Service

#### Authentication Endpoints
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `POST /api/auth/logout` - Logout user

#### User Management Endpoints
- `GET /api/users` - Get all users (ADMIN only)
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user (ADMIN only)
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user (ADMIN only)

#### Example Requests

**Register a new user**:
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "password123",
    "firstName": "New",
    "lastName": "User"
  }'
```

**Login**:
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "admin123"
  }'
```

## Default Users

The user management service comes with pre-configured users:

### Admin User
- **Username**: `admin`
- **Password**: `admin123`
- **Email**: `admin@example.com`
- **Role**: `ADMIN`

### Regular User
- **Username**: `user`
- **Password**: `user123`
- **Email**: `user@example.com`
- **Role**: `USER`

## Monitoring

### Prometheus Metrics
Both services expose metrics at `/actuator/prometheus`:
- HTTP request metrics
- JVM metrics
- Database connection metrics
- Custom business metrics

### Grafana Dashboards
1. Access Grafana at http://localhost:3000
2. Login with admin/admin
3. Add Prometheus as a data source (URL: http://prometheus:9090)
4. Create dashboards for monitoring:
   - Service health
   - Request rates and latencies
   - Error rates
   - Database metrics

## Development

### Local Development

#### Product Management Service
```bash
cd product_management
./mvnw spring-boot:run
```

#### User Management Service
```bash
cd user_management
./mvnw spring-boot:run
```

### Database Access

#### Shared Database
- **Host**: localhost
- **Port**: 5433
- **Database**: productdb
- **Username**: postgres
- **Password**: postgres

**Note**: Both services now share the same database for simplified deployment and reduced resource usage.

## Security

### Authentication
- Spring Security with JWT support (placeholder implementation)
- Password encryption using BCrypt
- Role-based access control

### Authorization
- `ADMIN` role: Full access to all endpoints
- `USER` role: Limited access to own resources
- Public endpoints: `/api/auth/**`, `/actuator/**`

## Configuration

### Environment Variables
- `SPRING_PROFILES_ACTIVE`: Active Spring profile
- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password

### Application Properties
Each service has its own `application.yml` with service-specific configurations.

## Convenience Scripts

The project includes several convenience scripts to make development easier:

### `start-services.sh`
- Starts all services with Docker Compose
- Shows access points and default credentials
- Checks service status

### `stop-services.sh`
- Stops all services
- Optionally removes volumes with `--volumes` flag
- Preserves data by default

### `test-apis.sh`
- Tests all API endpoints
- Verifies service health
- Shows colored output for pass/fail status

## Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure ports 8080, 8081, 5433, 9090, 3000 are available
2. **Database connection**: Check if PostgreSQL container is running
3. **Service startup**: Check logs with `docker compose logs <service-name>`

### Logs
```bash
# View all logs
docker compose logs

# View specific service logs
docker compose logs app
docker compose logs user-service

# Follow logs
docker compose logs -f
```

### Testing
```bash
# Run API tests
./test-apis.sh

# Check service health
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is for educational purposes. 