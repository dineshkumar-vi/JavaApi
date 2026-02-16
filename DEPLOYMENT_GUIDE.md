# Capital Markets API - Deployment Guide

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Development Environment Setup](#development-environment-setup)
3. [Building the Application](#building-the-application)
4. [Running the Application](#running-the-application)
5. [Production Deployment](#production-deployment)
6. [Docker Deployment](#docker-deployment)
7. [Cloud Deployment](#cloud-deployment)
8. [Monitoring & Maintenance](#monitoring--maintenance)
9. [Troubleshooting](#troubleshooting)

---

## Prerequisites

### Software Requirements

#### Required
- **Java Development Kit (JDK):** 1.8 or higher
- **MongoDB:** 4.0 or higher
- **Maven:** 3.6+ (or use included Maven Wrapper)

#### Optional
- **Docker:** 19.03+ (for containerized deployment)
- **Docker Compose:** 1.25+ (for multi-container setup)
- **Git:** For version control

### Hardware Requirements

#### Development Environment
- **CPU:** 2+ cores
- **RAM:** 4GB minimum, 8GB recommended
- **Disk:** 2GB free space

#### Production Environment
- **CPU:** 4+ cores
- **RAM:** 8GB minimum, 16GB recommended
- **Disk:** 10GB free space (depends on data volume)
- **Network:** Stable internet connection

---

## Development Environment Setup

### Step 1: Install Java JDK

#### macOS
```bash
# Using Homebrew
brew install openjdk@8

# Set JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)' >> ~/.zshrc
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-8-jdk

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
echo 'export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64' >> ~/.bashrc
```

#### Windows
1. Download JDK 8 from Oracle website
2. Install JDK
3. Set JAVA_HOME environment variable
4. Add %JAVA_HOME%\bin to PATH

#### Verify Installation
```bash
java -version
# Should show: java version "1.8.x"
```

### Step 2: Install MongoDB

#### macOS
```bash
# Using Homebrew
brew tap mongodb/brew
brew install mongodb-community@4.0

# Start MongoDB
brew services start mongodb-community@4.0

# Verify MongoDB is running
mongo --eval "db.version()"
```

#### Linux (Ubuntu/Debian)
```bash
# Import MongoDB public key
wget -qO - https://www.mongodb.org/static/pgp/server-4.0.asc | sudo apt-key add -

# Create list file
echo "deb [ arch=amd64 ] https://repo.mongodb.org/apt/ubuntu bionic/mongodb-org/4.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-4.0.list

# Install MongoDB
sudo apt update
sudo apt install -y mongodb-org

# Start MongoDB
sudo systemctl start mongod
sudo systemctl enable mongod

# Verify
mongo --eval "db.version()"
```

#### Windows
1. Download MongoDB Community Server from mongodb.com
2. Run installer
3. Configure as Windows Service
4. Start MongoDB service

#### Verify MongoDB
```bash
# Connect to MongoDB
mongo

# Check connection
> db.version()
> exit
```

### Step 3: Clone the Repository
```bash
git clone <repository-url>
cd JavaApi
```

### Step 4: Configure Application

#### Edit application.properties
```bash
# Location: src/main/resources/application.properties
nano src/main/resources/application.properties
```

#### Configuration Options
```properties
# MongoDB Connection
spring.data.mongodb.uri=mongodb://localhost:27017/demo

# Server Port (optional, default: 8080)
server.port=8080

# Logging Level (optional)
logging.level.com.example=DEBUG
```

---

## Building the Application

### Using Maven Wrapper (Recommended)

#### Clean and Build
```bash
./mvnw clean install
```

#### Build without Tests
```bash
./mvnw clean install -DskipTests
```

#### Package as JAR
```bash
./mvnw clean package
```

### Using Maven

#### If Maven is installed globally
```bash
mvn clean install
mvn clean package
```

### Build Output
- **Location:** `target/demo-0.0.1-SNAPSHOT.jar`
- **Size:** Approximately 30-40MB
- **Type:** Executable JAR (Spring Boot fat JAR)

### Verify Build
```bash
ls -lh target/demo-0.0.1-SNAPSHOT.jar
```

---

## Running the Application

### Method 1: Using Maven Wrapper
```bash
./mvnw spring-boot:run
```

### Method 2: Running Packaged JAR
```bash
# Build first
./mvnw clean package

# Run JAR
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Method 3: Using IDE
1. Import project as Maven project
2. Run `DemoApplication.java` main method
3. Application starts on port 8080

### Verify Application is Running

#### Check Logs
Look for:
```
Started DemoApplication in X seconds
```

#### Test Endpoints
```bash
# Test Captcha API
curl -X POST http://localhost:8080/captcha \
  -H "Content-Type: application/json" \
  -d '{"captcha":"TEST","ipAddress":"127.0.0.1"}'

# Expected: JSON response with captcha data
```

### Application URLs
- **API Base:** http://localhost:8080
- **Health Check:** Create custom endpoint if needed
- **MongoDB:** mongodb://localhost:27017/demo

---

## Production Deployment

### Step 1: Prepare Production Configuration

#### Create production properties
```bash
# Create: src/main/resources/application-prod.properties
```

```properties
# Production MongoDB
spring.data.mongodb.uri=mongodb://prod-mongodb-host:27017/capitalmarkets

# Production Port
server.port=8080

# Logging
logging.level.com.example=INFO
logging.file.name=/var/log/capitalmarkets/app.log

# Security
server.ssl.enabled=true
server.ssl.key-store=/path/to/keystore.jks
server.ssl.key-store-password=${KEYSTORE_PASSWORD}

# CORS (Update for production domain)
cors.allowed.origins=https://yourdomain.com
```

### Step 2: Build Production JAR
```bash
./mvnw clean package -DskipTests -Pprod
```

### Step 3: Deploy to Server

#### Transfer JAR to Server
```bash
scp target/demo-0.0.1-SNAPSHOT.jar user@production-server:/opt/capitalmarkets/
```

#### Create Systemd Service (Linux)

Create `/etc/systemd/system/capitalmarkets.service`:
```ini
[Unit]
Description=Capital Markets API
After=syslog.target network.target

[Service]
SuccessExitStatus=143
User=capitalmarkets
Group=capitalmarkets

Type=simple

WorkingDirectory=/opt/capitalmarkets
ExecStart=/usr/bin/java -jar /opt/capitalmarkets/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

StandardOutput=journal
StandardError=journal
SyslogIdentifier=capitalmarkets

[Install]
WantedBy=multi-user.target
```

#### Start Service
```bash
# Reload systemd
sudo systemctl daemon-reload

# Enable service
sudo systemctl enable capitalmarkets

# Start service
sudo systemctl start capitalmarkets

# Check status
sudo systemctl status capitalmarkets

# View logs
sudo journalctl -u capitalmarkets -f
```

### Step 4: Configure Reverse Proxy (Nginx)

#### Install Nginx
```bash
sudo apt install nginx
```

#### Create Nginx Configuration
Create `/etc/nginx/sites-available/capitalmarkets`:
```nginx
server {
    listen 80;
    server_name api.yourdomain.com;

    # Redirect HTTP to HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name api.yourdomain.com;

    # SSL Certificates
    ssl_certificate /etc/letsencrypt/live/api.yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/api.yourdomain.com/privkey.pem;

    # SSL Configuration
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    # Proxy to Spring Boot
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # CORS Headers
    add_header Access-Control-Allow-Origin "https://yourdomain.com" always;
    add_header Access-Control-Allow-Methods "GET, POST, OPTIONS" always;
    add_header Access-Control-Allow-Headers "Content-Type" always;
}
```

#### Enable Site
```bash
sudo ln -s /etc/nginx/sites-available/capitalmarkets /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

---

## Docker Deployment

### Step 1: Create Dockerfile

Create `Dockerfile` in project root:
```dockerfile
FROM openjdk:8-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build application
RUN ./mvnw package -DskipTests

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
```

### Step 2: Create docker-compose.yml

Create `docker-compose.yml`:
```yaml
version: '3.8'

services:
  mongodb:
    image: mongo:4.0
    container_name: capitalmarkets-mongodb
    ports:
      - "27017:27017"
    volumes:
      - mongodb-data:/data/db
    environment:
      MONGO_INITDB_DATABASE: demo
    networks:
      - capitalmarkets-network

  api:
    build: .
    container_name: capitalmarkets-api
    ports:
      - "8080:8080"
    environment:
      SPRING_DATA_MONGODB_URI: mongodb://mongodb:27017/demo
    depends_on:
      - mongodb
    networks:
      - capitalmarkets-network

volumes:
  mongodb-data:

networks:
  capitalmarkets-network:
    driver: bridge
```

### Step 3: Build and Run

```bash
# Build and start containers
docker-compose up -d

# View logs
docker-compose logs -f

# Stop containers
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### Alternative: Using Pre-built JAR

Create `Dockerfile`:
```dockerfile
FROM openjdk:8-jre-alpine

WORKDIR /app

COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
# Build JAR first
./mvnw clean package

# Build Docker image
docker build -t capitalmarkets-api .

# Run with MongoDB
docker run -d --name capitalmarkets-mongodb mongo:4.0

docker run -d \
  --name capitalmarkets-api \
  --link capitalmarkets-mongodb:mongodb \
  -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/demo \
  capitalmarkets-api
```

---

## Cloud Deployment

### AWS Deployment

#### Using Elastic Beanstalk

1. **Install EB CLI**
```bash
pip install awsebcli
```

2. **Initialize EB**
```bash
eb init -p java-8 capitalmarkets-api
```

3. **Create Environment**
```bash
eb create capitalmarkets-prod
```

4. **Deploy**
```bash
eb deploy
```

#### Using EC2

1. Launch EC2 instance (Ubuntu 20.04)
2. Install Java and MongoDB
3. Follow production deployment steps above
4. Configure security groups (open ports 22, 80, 443, 8080)

### Azure Deployment

#### Using Azure App Service

1. **Install Azure CLI**
```bash
curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash
```

2. **Login**
```bash
az login
```

3. **Create Resource Group**
```bash
az group create --name capitalmarkets-rg --location eastus
```

4. **Create App Service Plan**
```bash
az appservice plan create --name capitalmarkets-plan --resource-group capitalmarkets-rg --sku B1 --is-linux
```

5. **Create Web App**
```bash
az webapp create --resource-group capitalmarkets-rg --plan capitalmarkets-plan --name capitalmarkets-api --runtime "JAVA|8-jre8"
```

6. **Deploy JAR**
```bash
az webapp deploy --resource-group capitalmarkets-rg --name capitalmarkets-api --src-path target/demo-0.0.1-SNAPSHOT.jar --type jar
```

### Google Cloud Platform (GCP)

#### Using App Engine

1. **Install gcloud SDK**
2. **Create app.yaml**
```yaml
runtime: java11
```

3. **Deploy**
```bash
gcloud app deploy
```

### Heroku Deployment

1. **Create Procfile**
```
web: java -Dserver.port=$PORT -jar target/demo-0.0.1-SNAPSHOT.jar
```

2. **Deploy**
```bash
heroku create capitalmarkets-api
git push heroku master
```

---

## Monitoring & Maintenance

### Health Checks

#### Add Spring Boot Actuator

Update `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Configure in `application.properties`:
```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
```

Endpoints:
- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics

### Logging

#### Configure Logback

Create `src/main/resources/logback-spring.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>/var/log/capitalmarkets/app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>/var/log/capitalmarkets/app.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

### Database Backups

#### MongoDB Backup Script
```bash
#!/bin/bash
# backup-mongodb.sh

BACKUP_DIR="/backup/mongodb"
DATE=$(date +%Y%m%d_%H%M%S)

mongodump --uri="mongodb://localhost:27017/demo" --out="$BACKUP_DIR/$DATE"

# Compress
tar -czf "$BACKUP_DIR/demo_$DATE.tar.gz" -C "$BACKUP_DIR" "$DATE"

# Remove uncompressed backup
rm -rf "$BACKUP_DIR/$DATE"

# Keep only last 7 days
find "$BACKUP_DIR" -name "*.tar.gz" -mtime +7 -delete
```

Schedule with cron:
```bash
0 2 * * * /path/to/backup-mongodb.sh
```

---

## Troubleshooting

### Application Won't Start

#### Check Java Version
```bash
java -version
```
Solution: Ensure Java 1.8 or higher

#### Check MongoDB Connection
```bash
mongo --eval "db.version()"
```
Solution: Start MongoDB service

#### Check Port Availability
```bash
lsof -i :8080
```
Solution: Kill process or use different port

### Database Connection Errors

#### Error: "Connection refused"
- Check MongoDB is running
- Verify connection string in application.properties
- Check firewall rules

#### Error: "Authentication failed"
- Update MongoDB URI with credentials
- Check user permissions

### Memory Issues

#### OutOfMemoryError
Increase heap size:
```bash
java -Xmx2048m -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Performance Issues

#### Slow Response Times
- Check MongoDB indexes
- Monitor database query performance
- Increase server resources
- Enable connection pooling

### Logging Issues

#### No Logs Generated
- Check log file permissions
- Verify logging configuration
- Check disk space

### Docker Issues

#### Container Won't Start
```bash
# Check logs
docker logs capitalmarkets-api

# Check container status
docker ps -a
```

#### Network Issues
```bash
# Check network
docker network inspect capitalmarkets-network
```

---

## Security Checklist

- [ ] Change default MongoDB port
- [ ] Enable MongoDB authentication
- [ ] Use environment variables for sensitive data
- [ ] Enable HTTPS/SSL
- [ ] Configure firewall rules
- [ ] Implement rate limiting
- [ ] Enable security headers
- [ ] Regular security updates
- [ ] Implement password encryption
- [ ] Add JWT token authentication
- [ ] Enable audit logging
- [ ] Configure CORS properly
- [ ] Implement API versioning
- [ ] Set up monitoring alerts

---

## Maintenance Schedule

### Daily
- Check application logs
- Monitor system resources
- Verify API availability

### Weekly
- Review security logs
- Check disk space
- Update dependencies (if needed)

### Monthly
- Database backup verification
- Performance optimization review
- Security audit

### Quarterly
- Update dependencies
- Review and update documentation
- Disaster recovery testing
