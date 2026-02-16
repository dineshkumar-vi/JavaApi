# Capital Markets API - Technical Specifications

## System Architecture

### Application Type
**Microservice-based REST API** for secure authentication with captcha validation and IP-based attack prevention.

### Architecture Pattern
**Layered Architecture (N-Tier)**
```
┌─────────────────────────────────────┐
│     Controller Layer (REST API)     │
│   (LoginApi, CaptchaApi)            │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│       Service Layer                  │
│   (UserService, CaptchaService)     │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     Repository Layer (Data Access)  │
│   (UserRepo, CaptchaRepo)           │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     MongoDB Database                 │
│     (NoSQL Document Store)          │
└─────────────────────────────────────┘
```

---

## Technology Stack

### Backend Framework
| Component | Technology | Version |
|-----------|------------|---------|
| Language | Java | 1.8 |
| Framework | Spring Boot | 2.1.4.RELEASE |
| Parent | spring-boot-starter-parent | 2.1.4.RELEASE |

### Spring Modules
- **spring-boot-starter-web** - RESTful web services
- **spring-boot-starter-data-mongodb** - MongoDB integration

### Utilities & Libraries
| Library | Version | Purpose |
|---------|---------|---------|
| commons-lang3 | 3.0 | Common utilities |
| httpclient | 4.5.8 | HTTP client operations |
| gson | 2.8.5 | JSON serialization/deserialization |

### Database
| Technology | Version | Purpose |
|------------|---------|---------|
| MongoDB | 4.0+ | NoSQL document database |

### Build Tools
- **Apache Maven** - Dependency management and build automation
- **Maven Wrapper** - Included for consistent builds across environments

---

## Component Specifications

### 1. Controller Layer

#### CaptchaApi Controller
**File:** `src/main/java/com/example/controllor/CaptchaApi.java`

**Annotations:**
- `@RestController` - Marks class as REST controller
- `@RequestMapping(path="/captcha")` - Base path mapping
- `@CrossOrigin(origins = "http://localhost:4200")` - CORS configuration

**Dependencies:**
- `@Autowired CaptchaService` - Service layer injection

**Endpoints:**

1. **POST /captcha**
   - Annotations: `@PostMapping(consumes = "application/json", produces = "application/json")`
   - Method: `create(@RequestBody Captcha captcha)`
   - Validation: Checks if captcha and ipAddress are not null
   - Returns: `ResponseEntity<Captcha>`

2. **GET /captcha**
   - Annotations: `@GetMapping(produces= "application/json")`
   - Method: `get(@RequestParam String captcha, @RequestParam String ipAddress)`
   - Validation: Checks if ipAddress is not null
   - Returns: `ResponseEntity<Captcha>`

#### LoginApi Controller
**File:** `src/main/java/com/example/controllor/LoginApi.java`

**Annotations:**
- `@RestController`
- `@RequestMapping(path="/login")`
- `@CrossOrigin(origins = "http://localhost:4200")`

**Dependencies:**
- `@Autowired UserService` - Service layer injection

**Endpoints:**

1. **POST /login**
   - Annotations: `@PostMapping(consumes = "application/json", produces = "application/json")`
   - Method: `create(@RequestBody User user)`
   - Validation Flow:
     1. Validates user, userName, password not null
     2. Validates captcha with IP address
     3. Checks user credentials
   - Returns: `ResponseEntity<String>`
   - Response Messages:
     - Success: "User Validated successfully" (200)
     - Invalid Captcha: "Invalid Captcha , Please enter correct captcha" (400)
     - Invalid Credentials: "Invalid username and password" (400)

---

### 2. Service Layer

#### CaptchaService Interface
**File:** `src/main/java/com/example/service/CaptchaService.java`

**Methods:**
- `Captcha createCaptcha(Captcha captcha)` - Create new captcha entry
- `Captcha get(String captcha, String ipAddress)` - Retrieve captcha by value and IP

#### CaptchaServiceImpl
**File:** `src/main/java/com/example/serviceimpl/CaptchaServiceImpl.java`

**Dependencies:**
- `@Autowired CaptchaRepo` - Repository layer injection

**Implementation:**
- Implements business logic for captcha management
- Interacts with MongoDB through repository layer

#### UserService Interface
**File:** `src/main/java/com/example/service/UserService.java`

**Methods:**
- `boolean validateCaptcha(String captcha, String ipAddress)` - Validate captcha
- `User checkUser(User user)` - Validate user credentials

#### UserServiceImpl
**File:** `src/main/java/com/example/serviceimpl/UserServiceImpl.java`

**Dependencies:**
- `@Autowired UserRepo` - Repository layer injection
- `@Autowired CaptchaService` - Captcha service injection

**Implementation:**
- Implements authentication logic
- Validates captcha before user authentication
- Implements IP-based attack prevention

---

### 3. Repository Layer

#### CaptchaRepo
**File:** `src/main/java/com/example/repository/CaptchaRepo.java`

**Type:** Spring Data MongoDB Repository
**Extends:** `MongoRepository<Captcha, String>`

**Custom Queries:**
- Implements custom query methods for captcha retrieval
- Supports search by captcha value and IP address

#### UserRepo
**File:** `src/main/java/com/example/repository/UserRepo.java`

**Type:** Spring Data MongoDB Repository
**Extends:** `MongoRepository<User, String>` (inferred)

**Custom Queries:**
- User authentication queries
- User lookup by username

---

### 4. Data Models

#### Captcha Entity
**File:** `src/main/java/com/example/data/Captcha.java`

**Annotations:**
- `@Document(collection = "captcha")` - MongoDB collection mapping
- `@Id` - Primary key
- `@Field("captcha")` - Field mapping
- `@Field("ipAddress")` - Field mapping

**Properties:**
```java
private String id;           // MongoDB ObjectId
private String captcha;      // Captcha value
private String ipAddress;    // Client IP address
```

**Methods:**
- Standard getters and setters
- `toString()` - String representation for logging

**MongoDB Collection:** `captcha`

#### User Model
**File:** `src/main/java/com/example/data/User.java`

**Type:** Plain Java Bean (POJO)

**Properties:**
```java
private String ipAddress;    // Client IP address
private String userName;     // Username for authentication
private String password;     // User password (plain text)
private String captcha;      // Captcha value for validation
```

**Methods:**
- Standard getters and setters

**Note:** User is not a MongoDB document (no @Document annotation) - used as DTO

---

### 5. Application Configuration

#### Main Application Class
**File:** `src/main/java/com/example/DemoApplication.java`

**Annotations:**
- `@SpringBootApplication` - Auto-configuration and component scanning
- `@ComponentScan(basePackages = { "com.example"})` - Component scanning configuration

**Main Method:**
```java
public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
}
```

#### Application Properties
**File:** `src/main/resources/application.properties`

**Configuration:**
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/demo
```

**Database Settings:**
- Host: localhost
- Port: 27017
- Database: demo

---

## Security Architecture

### 1. Captcha-Based Authentication
```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ 1. Request Captcha
       ▼
┌─────────────┐
│ POST /captcha│
└──────┬──────┘
       │ 2. Generate & Store (captcha + IP)
       ▼
┌─────────────┐
│  MongoDB    │
└──────┬──────┘
       │ 3. Return Captcha
       ▼
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ 4. Submit Login (credentials + captcha + IP)
       ▼
┌─────────────┐
│ POST /login │
└──────┬──────┘
       │ 5. Validate Captcha + IP
       │ 6. Validate Credentials
       ▼
┌─────────────┐
│  Response   │
└─────────────┘
```

### 2. IP-Based Attack Prevention

**Monitoring:**
- Tracks all incoming requests with IP addresses
- Monitors for continuous non-blocking request patterns
- Compares request patterns across sessions

**Detection:**
- Identifies suspicious activity patterns
- Detects automated attack attempts
- Monitors captcha validation failures

**Response:**
- Blocks suspicious IP addresses
- Automatically removes malicious users
- Prevents system compromise

### 3. CORS Security
**Configuration:**
- Allowed Origin: `http://localhost:4200`
- Methods: POST, GET
- Content-Type: application/json

---

## Database Schema

### Captcha Collection
**Collection Name:** `captcha`

**Schema:**
```javascript
{
  "_id": ObjectId("507f1f77bcf86cd799439011"),
  "captcha": "ABC123",
  "ipAddress": "192.168.1.100"
}
```

**Indexes:** (Recommended)
- Compound index on (captcha, ipAddress)
- TTL index on creation timestamp for auto-expiration

### User Collection
**Collection Name:** `users` (inferred)

**Schema:** (Inferred from User model)
```javascript
{
  "_id": ObjectId("..."),
  "userName": "john.doe",
  "password": "hashedPassword",
  "ipAddress": "192.168.1.100"
}
```

---

## Build Configuration

### Maven POM (pom.xml)

**Project Coordinates:**
```xml
<groupId>com.example</groupId>
<artifactId>demo</artifactId>
<version>0.0.1-SNAPSHOT</version>
<packaging>jar</packaging>
```

**Parent:**
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.4.RELEASE</version>
</parent>
```

**Properties:**
```xml
<java.version>1.8</java.version>
```

**Dependencies:**
```xml
<!-- Spring Boot Starters -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Utilities -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.0</version>
</dependency>

<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.8</version>
</dependency>

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.5</version>
</dependency>
```

**Build Plugins:**
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

---

## Deployment Specifications

### Development Environment
- **Java Version:** 1.8+
- **MongoDB:** localhost:27017
- **Application Port:** 8080 (default)
- **Frontend:** http://localhost:4200

### Build Commands
```bash
# Clean and build
./mvnw clean install

# Run application
./mvnw spring-boot:run

# Package as JAR
./mvnw clean package

# Run packaged JAR
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Runtime Requirements
- JRE 1.8 or higher
- MongoDB 4.0+ (running and accessible)
- Minimum 512MB RAM
- 100MB disk space

---

## Performance Considerations

### Database Optimization
1. **Indexes:**
   - Compound index on captcha collection: (captcha, ipAddress)
   - Index on ipAddress for attack monitoring
   - TTL index for automatic captcha cleanup

2. **Connection Pooling:**
   - Spring Data MongoDB handles connection pooling
   - Default pool size: 100 connections

### API Performance
- **Response Time Target:** < 200ms
- **Concurrent Users:** Designed for moderate load (< 1000 concurrent)
- **Throughput:** Depends on MongoDB performance

### Scalability
- **Horizontal Scaling:** Supported (stateless design)
- **Load Balancing:** Compatible with standard load balancers
- **Session Management:** Consider Redis for distributed sessions

---

## Monitoring & Logging

### Logging Framework
- **SLF4J** (Spring Boot default)
- **Logback** for log management

### Log Levels
- ERROR: Critical failures
- WARN: Invalid login attempts, captcha failures
- INFO: Successful authentications
- DEBUG: Detailed request/response logging

### Metrics to Monitor
- Authentication success/failure rate
- Captcha validation rate
- Response times
- Database connection pool usage
- IP-based attack detection events

---

## Security Vulnerabilities & Recommendations

### Current Security Issues

1. **Password Storage**
   - Issue: Plain text passwords
   - Recommendation: Implement BCrypt encryption

2. **Session Management**
   - Issue: No session tokens after authentication
   - Recommendation: Implement JWT tokens

3. **Captcha Expiration**
   - Issue: No time-based expiration
   - Recommendation: Add TTL to captcha documents

4. **Rate Limiting**
   - Issue: No request rate limiting
   - Recommendation: Implement rate limiting per IP

5. **HTTPS**
   - Issue: Running on HTTP
   - Recommendation: Enable SSL/TLS for production

### Security Best Practices
- Input validation and sanitization
- SQL/NoSQL injection prevention
- XSS protection (handled by Spring Security)
- CSRF protection (for stateful sessions)
- Secure headers (HSTS, X-Frame-Options, etc.)

---

## Testing Specifications

### Unit Testing
- Framework: JUnit
- Mocking: Mockito
- Coverage Target: 80%+

### Integration Testing
- Spring Boot Test
- Embedded MongoDB (Flapdoodle)
- REST API testing

### Test Scenarios
1. Captcha generation and validation
2. User authentication flow
3. Invalid credentials handling
4. IP mismatch detection
5. Attack pattern detection

---

## API Versioning Strategy

### Current State
- No versioning implemented
- Direct endpoint mapping (/login, /captcha)

### Recommended Approach
- URI versioning: /api/v1/login, /api/v1/captcha
- Header versioning: Accept: application/vnd.api.v1+json
- Backwards compatibility maintenance

---

## Error Handling Strategy

### Current Implementation
- HTTP status codes (200, 400)
- String messages for errors

### Recommended Enhancement
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid Captcha",
  "path": "/login",
  "requestId": "abc-123-def"
}
```

---

## Documentation Standards

### Code Documentation
- Javadoc for public methods
- Inline comments for complex logic
- README for setup instructions

### API Documentation
- Swagger/OpenAPI specification
- Postman collections
- Example requests/responses

---

## Compliance & Standards

### Code Standards
- Java 8 conventions
- Spring Boot best practices
- RESTful API design principles

### Data Protection
- GDPR considerations for IP address storage
- Password encryption requirements
- Audit logging for security events
