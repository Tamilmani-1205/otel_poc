# Product Management System

A robust Spring Boot backend application for managing products with JWT authentication and PostgreSQL database.

## Technology Stack

- Java 17
- Spring Boot 3.2.3
- PostgreSQL
- Spring Security with JWT
- Spring Data JPA
- Flyway for database migrations
- Maven
- JUnit 5 & Mockito for testing

## Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

## Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE product_management;
```

2. Update the database credentials in `src/main/resources/application.yml` if needed:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/product_management
    username: your_username
    password: your_password
```

## Building and Running

1. Clone the repository:
```bash
git clone <repository-url>
cd product-management-system
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Products

- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

## Authentication

The API uses JWT (JSON Web Token) for authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

## Example API Usage

### Register a new user
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "email": "user1@example.com",
    "password": "password123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "password123"
  }'
```

### Create a product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <your_jwt_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sample Product",
    "description": "This is a sample product",
    "price": 99.99
  }'
```

## Security

- JWT tokens expire after 24 hours
- Passwords are encrypted using BCrypt
- All product endpoints require authentication
- Input validation is implemented using Jakarta Bean Validation

## Development

### Project Structure

```
src/main/java/com/productmanagement/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
├── exception/      # Custom exceptions
├── model/          # Entity classes
├── repository/     # JPA repositories
├── security/       # Security configuration
└── service/        # Business logic
```

### Running Tests

```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 