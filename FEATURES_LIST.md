# JavaApi - Existing Features List

## Project Overview
A Spring Boot REST API application with MongoDB integration providing user authentication and CAPTCHA validation services.

---

## Core Features

### 1. User Authentication System
**Location:** `src/main/java/com/example/controllor/LoginApi.java`

#### Capabilities:
- **User Login Validation**
  - REST endpoint: `POST /login`
  - Validates username and password against MongoDB database
  - Returns authentication status with appropriate HTTP responses

- **Input Validation**
  - Validates required fields (username, password)
  - Returns `400 BAD_REQUEST` for missing or invalid inputs

- **CAPTCHA Integration**
  - Validates CAPTCHA before processing login
  - Blocks authentication attempts with invalid CAPTCHA
  - IP address-based CAPTCHA verification

- **CORS Support**
  - Enabled for `http://localhost:4200` (Angular frontend)
  - Supports cross-origin requests

#### Response Messages:
- Success: `"User Validated successfully"` (HTTP 200)
- Failure: `"Invalid username and password"` (HTTP 400)
- CAPTCHA Error: `"Invalid Captcha , Please enter correct captcha"` (HTTP 400)

---

### 2. CAPTCHA Management System
**Location:** `src/main/java/com/example/controllor/CaptchaApi.java`

#### Features:

##### A. CAPTCHA Generation
- **Endpoint:** `POST /captcha`
- **Input:** JSON payload with IP address
- **Process:**
  - Generates random 6-character alphanumeric CAPTCHA
  - Uses `SecureRandom` for cryptographic randomness
  - Character set: `0-9, a-z, A-Z`
- **Storage:** Persists CAPTCHA with IP address in MongoDB
- **Response:** Returns generated CAPTCHA object

##### B. CAPTCHA Validation
- **Endpoint:** `GET /captcha?captcha={value}&ipAddress={ip}`
- **Parameters:**
  - `captcha`: The CAPTCHA text to validate
  - `ipAddress`: Client IP address
- **Process:**
  - Queries MongoDB for matching CAPTCHA and IP address
  - Returns most recent CAPTCHA if multiple matches exist
- **Response:**
  - Success: Returns CAPTCHA object (HTTP 200)
  - Failure: Returns `400 BAD_REQUEST`

##### C. CORS Support
- Enabled for `http://localhost:4200`

---

### 3. MongoDB Data Persistence

#### Collections:

##### A. User Collection
**Location:** `src/main/java/com/example/data/User.java`

**Fields:**
- `userName` (String) - User's login name
- `password` (String) - User's password (plaintext storage)
- `ipAddress` (String) - Client IP address
- `captcha` (String) - CAPTCHA text for validation

**Repository Operations:**
- `findByUserNameAndPassword()` - Authentication query

##### B. Captcha Collection
**Location:** `src/main/java/com/example/data/Captcha.java`

**Document Structure:**
- `id` (String) - MongoDB document ID
- `captcha` (String) - Generated CAPTCHA text
- `ipAddress` (String) - Client IP address

**Repository Operations:**
- `findByCaptchaAndIpAddress()` - Custom query for validation
- Standard MongoDB CRUD operations via `MongoRepository`

---

### 4. Service Layer Architecture

#### A. User Service
**Location:** `src/main/java/com/example/serviceimpl/UserServiceImpl.java`

**Methods:**
1. `checkUser(User user)`
   - Validates username and password against database
   - Returns user object if found, null otherwise
   - Exception handling for database errors

2. `validateCaptcha(String captcha, String ipAddress)`
   - Makes HTTP GET request to `/captcha` endpoint
   - Uses Apache HttpClient for communication
   - Parses JSON response using Gson
   - Returns boolean validation result

#### B. CAPTCHA Service
**Location:** `src/main/java/com/example/serviceimpl/CaptchaServiceImpl.java`

**Methods:**
1. `createCaptcha(Captcha captcha)`
   - Generates 6-character random string
   - Saves to MongoDB
   - Exception handling for database errors
   - Console logging for debugging

2. `get(String enteredCaptcha, String ipAddress)`
   - Retrieves CAPTCHA by value and IP
   - Returns most recent match if multiple exist
   - Returns null if no match found

3. `getRandomString(int count)` (Private)
   - Uses `SecureRandom` for cryptographic randomness
   - Alphanumeric character set (62 characters)
   - Configurable length

---

## Technical Stack

### Backend Framework
- **Spring Boot** 2.1.4.RELEASE
- **Java** 1.8

### Dependencies
1. **spring-boot-starter-data-mongodb** - MongoDB integration
2. **spring-boot-starter-web** - REST API support
3. **commons-lang3** (3.0) - Apache Commons utilities
4. **httpclient** (4.5.8) - Apache HTTP client
5. **gson** (2.8.5) - JSON serialization/deserialization

### Database
- **MongoDB** running on `localhost:27017`
- Database name: `demo`

### Build Tool
- **Maven** - Project management and dependency resolution
- Maven wrapper included (`mvnw`, `mvnw.cmd`)

---

## API Endpoints Summary

| Method | Endpoint | Purpose | Request Body | Response |
|--------|----------|---------|--------------|----------|
| POST | `/login` | User authentication | User JSON | Success/Error message |
| POST | `/captcha` | Generate CAPTCHA | Captcha JSON (IP) | Captcha object |
| GET | `/captcha` | Validate CAPTCHA | Query params | Captcha object or error |

---

## Security Features

### Implemented:
1. **CAPTCHA Protection**
   - Prevents automated bot attacks
   - IP address tracking
   - Random secure CAPTCHA generation

2. **Input Validation**
   - Null checks on all inputs
   - Required field validation

3. **CORS Configuration**
   - Controlled cross-origin access
   - Limited to specific frontend origin

### Security Concerns:
⚠️ **Critical Issues Found:**
1. Passwords stored in plaintext (no hashing)
2. No JWT or session token implementation
3. HTTP calls between services (no HTTPS)
4. CAPTCHA stored indefinitely (no expiration)
5. No rate limiting on login attempts
6. No SQL/NoSQL injection protection
7. No authentication token for subsequent requests

---

## Architecture Patterns

### Design Pattern: Layered Architecture
1. **Controller Layer** - REST API endpoints
2. **Service Layer** - Business logic
3. **Repository Layer** - Data access
4. **Data Layer** - Entity/Domain models

### Communication:
- RESTful API between client and server
- Internal HTTP calls between services
- MongoDB for data persistence

---

## Configuration

### Application Properties
**Location:** `src/main/resources/application.properties`

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/demo
```

### CORS Configuration
- Allowed origin: `http://localhost:4200`
- Supports POST and GET methods
- JSON content type

---

## Project Structure

```
JavaApi/
├── src/
│   └── main/
│       ├── java/com/example/
│       │   ├── controllor/
│       │   │   ├── LoginApi.java
│       │   │   └── CaptchaApi.java
│       │   ├── service/
│       │   │   ├── UserService.java
│       │   │   └── CaptchaService.java
│       │   ├── serviceimpl/
│       │   │   ├── UserServiceImpl.java
│       │   │   └── CaptchaServiceImpl.java
│       │   ├── repository/
│       │   │   ├── UserRepo.java
│       │   │   └── CaptchaRepo.java
│       │   ├── data/
│       │   │   ├── User.java
│       │   │   └── Captcha.java
│       │   └── DemoApplication.java
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

---

## Main Application Entry Point
**Location:** `src/main/java/com/example/DemoApplication.java`
- Spring Boot application main class
- Default embedded Tomcat server (port 8080)

---

## Logging and Debugging
- Console output for CAPTCHA generation
- Exception stack traces printed to console
- Debug messages for IP address and CAPTCHA validation

---

## Future Enhancement Opportunities
1. Implement password hashing (BCrypt)
2. Add JWT token-based authentication
3. Implement CAPTCHA expiration (time-based)
4. Add rate limiting
5. Implement HTTPS
6. Add comprehensive logging framework (SLF4J/Logback)
7. Add unit and integration tests
8. Implement API documentation (Swagger/OpenAPI)
9. Add input sanitization
10. Implement refresh token mechanism

---

## Summary Statistics
- **Total Controllers:** 2
- **Total Services:** 2
- **Total Repositories:** 2
- **Total Entities:** 2
- **Total API Endpoints:** 3
- **Database Collections:** 2
- **External Dependencies:** 5

---

*Document Generated: 2026-02-16*
*Based on codebase analysis of JavaApi Spring Boot application*
