# Capital Markets API - API Documentation

## Base URL
```
http://localhost:8080
```

## Frontend Integration
- **CORS Enabled For:** http://localhost:4200
- **Content-Type:** application/json

---

## API Endpoints

### 1. Captcha Management API

#### Create Captcha
Generate and store a new captcha associated with an IP address.

**Endpoint:** `POST /captcha`

**Headers:**
```
Content-Type: application/json
Origin: http://localhost:4200
```

**Request Body:**
```json
{
  "captcha": "ABC123",
  "ipAddress": "192.168.1.100"
}
```

**Success Response:**
- **Code:** 200 OK
- **Content:**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "captcha": "ABC123",
  "ipAddress": "192.168.1.100"
}
```

**Error Response:**
- **Code:** 400 BAD REQUEST
- **Condition:** Missing captcha object or ipAddress field
- **Content:** Empty response

**Example cURL:**
```bash
curl -X POST http://localhost:8080/captcha \
  -H "Content-Type: application/json" \
  -H "Origin: http://localhost:4200" \
  -d '{
    "captcha": "ABC123",
    "ipAddress": "192.168.1.100"
  }'
```

---

#### Retrieve Captcha
Retrieve a captcha entry by captcha value and IP address.

**Endpoint:** `GET /captcha`

**Headers:**
```
Origin: http://localhost:4200
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| captcha | String | Yes | Captcha value to search for |
| ipAddress | String | Yes | Client IP address |

**Success Response:**
- **Code:** 200 OK
- **Content:**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "captcha": "ABC123",
  "ipAddress": "192.168.1.100"
}
```

**Error Response:**
- **Code:** 400 BAD REQUEST
- **Condition:** Missing ipAddress parameter or captcha not found
- **Content:** Empty response

**Example cURL:**
```bash
curl -X GET "http://localhost:8080/captcha?captcha=ABC123&ipAddress=192.168.1.100" \
  -H "Origin: http://localhost:4200"
```

**Example Request URL:**
```
http://localhost:8080/captcha?captcha=ABC123&ipAddress=192.168.1.100
```

---

### 2. Authentication API

#### User Login
Authenticate a user with username, password, captcha validation, and IP verification.

**Endpoint:** `POST /login`

**Headers:**
```
Content-Type: application/json
Origin: http://localhost:4200
```

**Request Body:**
```json
{
  "userName": "john.doe",
  "password": "SecurePassword123",
  "captcha": "ABC123",
  "ipAddress": "192.168.1.100"
}
```

**Request Body Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| userName | String | Yes | User's username |
| password | String | Yes | User's password |
| captcha | String | Yes | Captcha value for validation |
| ipAddress | String | Yes | Client IP address |

**Success Response:**
- **Code:** 200 OK
- **Content:**
```json
"User Validated successfully"
```

**Error Responses:**

1. **Invalid Request**
- **Code:** 400 BAD REQUEST
- **Condition:** Missing user object, userName, or password
- **Content:** Empty response

2. **Invalid Captcha**
- **Code:** 400 BAD REQUEST
- **Condition:** Captcha validation failed or IP address mismatch
- **Content:**
```json
"Invalid Captcha , Please enter correct captcha"
```

3. **Invalid Credentials**
- **Code:** 400 BAD REQUEST
- **Condition:** Username or password is incorrect
- **Content:**
```json
"Invalid username and password"
```

**Validation Flow:**
1. Validates request body (userName, password must not be null)
2. Validates captcha with IP address using captcha service
3. If captcha valid, checks user credentials
4. Returns appropriate success or error message

**Example cURL:**
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -H "Origin: http://localhost:4200" \
  -d '{
    "userName": "john.doe",
    "password": "SecurePassword123",
    "captcha": "ABC123",
    "ipAddress": "192.168.1.100"
  }'
```

---

## Authentication Flow

### Complete Authentication Sequence

1. **Generate Captcha**
   ```
   POST /captcha
   Body: { "ipAddress": "192.168.1.100" }
   Response: { "id": "...", "captcha": "ABC123", "ipAddress": "192.168.1.100" }
   ```

2. **Display Captcha to User**
   - Frontend displays the captcha value to user
   - User enters captcha along with credentials

3. **Submit Login Request**
   ```
   POST /login
   Body: {
     "userName": "john.doe",
     "password": "SecurePassword123",
     "captcha": "ABC123",
     "ipAddress": "192.168.1.100"
   }
   ```

4. **Backend Validation**
   - Validates captcha exists and matches IP address
   - Checks username and password
   - Returns authentication result

---

## Security Features

### IP-Based Security
- All requests are tracked by IP address
- Captcha is bound to specific IP address
- Prevents captcha replay attacks from different IPs

### Attack Prevention
- Monitors continuous non-blocking requests
- Automatically blocks suspicious patterns
- Removes users exhibiting attack behavior

### CORS Protection
- Restricted to http://localhost:4200
- Prevents unauthorized cross-origin access

---

## Error Handling

### HTTP Status Codes
| Code | Description |
|------|-------------|
| 200 | Success |
| 400 | Bad Request - Invalid input or validation failure |

### Common Error Scenarios

1. **Missing Required Fields**
   - Status: 400 BAD REQUEST
   - Trigger: Null or missing userName, password, or ipAddress

2. **Captcha Validation Failure**
   - Status: 400 BAD REQUEST
   - Message: "Invalid Captcha , Please enter correct captcha"
   - Trigger: Captcha not found, expired, or IP mismatch

3. **Authentication Failure**
   - Status: 400 BAD REQUEST
   - Message: "Invalid username and password"
   - Trigger: Incorrect credentials

---

## Data Models

### User Request Object
```json
{
  "userName": "string",
  "password": "string",
  "captcha": "string",
  "ipAddress": "string"
}
```

### Captcha Object
```json
{
  "id": "string (MongoDB ObjectId)",
  "captcha": "string",
  "ipAddress": "string"
}
```

---

## Frontend Integration Example

### Angular Service Example
```typescript
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  // Generate Captcha
  generateCaptcha(ipAddress: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.baseUrl}/captcha`,
      { ipAddress },
      { headers }
    );
  }

  // Validate Captcha
  validateCaptcha(captcha: string, ipAddress: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/captcha`, {
      params: { captcha, ipAddress }
    });
  }

  // Login
  login(userName: string, password: string, captcha: string, ipAddress: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.baseUrl}/login`,
      { userName, password, captcha, ipAddress },
      { headers, responseType: 'text' }
    );
  }
}
```

### JavaScript Fetch API Example
```javascript
// Generate Captcha
async function generateCaptcha(ipAddress) {
  const response = await fetch('http://localhost:8080/captcha', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ ipAddress })
  });
  return await response.json();
}

// Login
async function login(userName, password, captcha, ipAddress) {
  const response = await fetch('http://localhost:8080/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ userName, password, captcha, ipAddress })
  });
  return await response.text();
}
```

---

## Testing

### Test Scenarios

#### 1. Successful Login Flow
```bash
# Step 1: Create Captcha
curl -X POST http://localhost:8080/captcha \
  -H "Content-Type: application/json" \
  -d '{"captcha":"TEST123","ipAddress":"127.0.0.1"}'

# Step 2: Login with Valid Credentials
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "userName":"validUser",
    "password":"validPass",
    "captcha":"TEST123",
    "ipAddress":"127.0.0.1"
  }'
```

#### 2. Invalid Captcha
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "userName":"validUser",
    "password":"validPass",
    "captcha":"WRONG",
    "ipAddress":"127.0.0.1"
  }'
# Expected: "Invalid Captcha , Please enter correct captcha"
```

#### 3. Invalid Credentials
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "userName":"wrongUser",
    "password":"wrongPass",
    "captcha":"TEST123",
    "ipAddress":"127.0.0.1"
  }'
# Expected: "Invalid username and password"
```

---

## Notes

### Database Requirements
- MongoDB must be running on localhost:27017
- Database name: demo
- Collection: captcha

### IP Address Handling
- IP address should be obtained from client request
- Server should validate IP matches between captcha generation and login
- Consider proxy/load balancer scenarios for production deployment

### Security Considerations
- Implement captcha expiration (time-based)
- Consider rate limiting per IP address
- Add password encryption (currently plain text)
- Implement session management after successful login
- Add HTTPS in production
- Consider JWT tokens for stateless authentication

### Production Recommendations
- Add API versioning (/v1/captcha, /v1/login)
- Implement comprehensive logging
- Add request/response validation
- Use environment-specific configuration
- Implement proper error response format
- Add API documentation (Swagger/OpenAPI)
- Implement captcha cleanup job (remove expired captchas)
