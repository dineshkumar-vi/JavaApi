# Capital Markets API - Project Details

## Project Overview
**Project Name:** Java Microservices - Security Authentication API
**Artifact ID:** demo
**Group ID:** com.example
**Version:** 0.0.1-SNAPSHOT
**Java Version:** 1.8
**Spring Boot Version:** 2.1.4.RELEASE

## Project Description
This is a Spring Boot-based microservices application that handles secure authentication with advanced captcha validation and IP-based attack prevention. The system validates incoming captcha requests along with client IP addresses to detect and prevent malicious attacks. It implements IP validation logic combined with client session tracking to identify and block continuous non-blocking requests, automatically removing suspicious users from the system.

## Core Technologies

### Backend Framework
- **Java:** 1.8
- **Spring Boot:** 2.1.4.RELEASE
- **Spring Rest:** RESTful API implementation
- **Spring Data:** Data access layer
- **MongoDB:** NoSQL database for data persistence

### Key Dependencies
```xml
- spring-boot-starter-data-mongodb
- spring-boot-starter-web
- commons-lang3 (3.0)
- httpclient (4.5.8)
- gson (2.8.5)
```

## Architecture

### Project Structure
```
JavaApi/
├── src/
│   └── main/
│       ├── java/com/example/
│       │   ├── controllor/
│       │   │   ├── CaptchaApi.java
│       │   │   └── LoginApi.java
│       │   ├── data/
│       │   │   ├── Captcha.java
│       │   │   └── User.java
│       │   ├── repository/
│       │   │   ├── CaptchaRepo.java
│       │   │   └── UserRepo.java
│       │   ├── service/
│       │   │   ├── CaptchaService.java
│       │   │   └── UserService.java
│       │   ├── serviceimpl/
│       │   │   ├── CaptchaServiceImpl.java
│       │   │   └── UserServiceImpl.java
│       │   └── DemoApplication.java
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

### Layer Architecture
1. **Controller Layer** (`controllor/`)
   - REST API endpoints
   - Request/Response handling
   - CORS configuration for frontend integration

2. **Service Layer** (`service/` & `serviceimpl/`)
   - Business logic implementation
   - Captcha validation
   - User authentication

3. **Data Layer** (`data/`)
   - Entity models
   - MongoDB document mappings

4. **Repository Layer** (`repository/`)
   - Data access interfaces
   - MongoDB operations

## API Endpoints

### 1. Captcha API
**Base Path:** `/captcha`

#### POST /captcha
- **Purpose:** Create a new captcha entry
- **Consumes:** application/json
- **Produces:** application/json
- **Request Body:** Captcha object with IP address
- **Response:** Captcha object with generated ID
- **CORS:** Enabled for http://localhost:4200

#### GET /captcha
- **Purpose:** Retrieve captcha by value and IP address
- **Produces:** application/json
- **Query Parameters:**
  - `captcha` - Captcha value
  - `ipAddress` - Client IP address
- **Response:** Captcha object if found
- **CORS:** Enabled for http://localhost:4200

### 2. Login API
**Base Path:** `/login`

#### POST /login
- **Purpose:** Authenticate user with captcha validation
- **Consumes:** application/json
- **Produces:** application/json
- **Request Body:** User object containing:
  - userName
  - password
  - captcha
  - ipAddress
- **Validation Flow:**
  1. Validates captcha with IP address
  2. Checks user credentials
  3. Returns appropriate response
- **Response:**
  - Success: "User Validated successfully" (HTTP 200)
  - Invalid captcha: "Invalid Captcha, Please enter correct captcha" (HTTP 400)
  - Invalid credentials: "Invalid username and password" (HTTP 400)
- **CORS:** Enabled for http://localhost:4200

## Data Models

### User Entity
```java
- ipAddress: String
- userName: String
- password: String
- captcha: String
```

### Captcha Entity (MongoDB Document)
```java
- id: String (MongoDB ObjectId)
- captcha: String
- ipAddress: String
```
**Collection Name:** `captcha`

## Database Configuration

### MongoDB
- **Database:** demo
- **Host:** localhost
- **Port:** 27017
- **Connection URI:** mongodb://localhost:27017/demo

## Security Features

### 1. Captcha Validation
- Server-side captcha verification
- IP address binding to prevent replay attacks
- Captcha stored with associated IP address

### 2. IP-Based Attack Prevention
- IP address tracking for all requests
- Session management with IP validation
- Automatic blocking of suspicious patterns
- Continuous non-blocking request detection
- Automatic user removal on detected attacks

### 3. Cross-Origin Resource Sharing (CORS)
- Configured for frontend at http://localhost:4200
- Applied to both Login and Captcha APIs

## Component Scanning
- **Base Package:** com.example
- Automatic component discovery for:
  - Controllers
  - Services
  - Repositories

## Build Configuration

### Maven
- **Build Tool:** Apache Maven
- **Maven Wrapper:** Included (mvnw, mvnw.cmd)
- **Plugin:** spring-boot-maven-plugin

### Build Commands
```bash
# Using Maven Wrapper
./mvnw clean install
./mvnw spring-boot:run

# Using Maven
mvn clean install
mvn spring-boot:run
```

## Development Setup

### Prerequisites
- Java 1.8 or higher
- MongoDB 4.0+ running on localhost:27017
- Maven 3.6+ (or use included Maven Wrapper)

### Running the Application
1. Start MongoDB service
2. Execute: `./mvnw spring-boot:run`
3. Application runs on default Spring Boot port (8080)

### Frontend Integration
- CORS configured for Angular frontend at http://localhost:4200
- API base URL: http://localhost:8080

## File Statistics
- **Total Java Files:** 11
- **Controllers:** 2
- **Services:** 4 (2 interfaces, 2 implementations)
- **Repositories:** 2
- **Data Models:** 2
- **Main Application:** 1

## Key Features
1. RESTful API design
2. MongoDB integration with Spring Data
3. Captcha-based authentication
4. IP address validation and tracking
5. Attack detection and prevention
6. CORS support for frontend integration
7. Layered architecture (Controller-Service-Repository)
8. Maven-based build system

## Security Measures
- Captcha validation before authentication
- IP address tracking and validation
- Continuous request monitoring
- Automatic threat detection
- User blocking mechanism
- Session-based security

## Future Enhancements
Based on the current implementation, potential areas for enhancement:
- JWT token-based authentication
- Rate limiting middleware
- Redis caching for captcha data
- Enhanced logging and monitoring
- Password encryption (BCrypt)
- Refresh token mechanism
- Role-based access control (RBAC)
- API documentation (Swagger/OpenAPI)
