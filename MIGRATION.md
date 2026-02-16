# Migration to Gradle and Latest Versions

## Changes Made

### 1. Java Version Update
- **Old Version**: Java 1.8
- **New Version**: Java 17 (LTS)

### 2. Spring Boot Update
- **Old Version**: Spring Boot 2.1.4.RELEASE
- **New Version**: Spring Boot 3.2.2

### 3. Build Tool Migration
- **Old**: Maven (pom.xml)
- **New**: Gradle (build.gradle)

### 4. Dependencies Updated

| Dependency | Old Version | New Version |
|------------|-------------|-------------|
| commons-lang3 | 3.0 | 3.14.0 |
| httpclient | 4.5.8 | 4.5.14 |
| gson | 2.8.5 | 2.10.1 |

## How to Build and Run

### Prerequisites
- Java 17 or higher installed
- MongoDB running on localhost:27017

### Build the Project
```bash
./gradlew build
```

### Run the Application
```bash
./gradlew bootRun
```

### Run Tests
```bash
./gradlew test
```

### Create JAR
```bash
./gradlew bootJar
```
The JAR file will be created in `build/libs/demo-0.0.1-SNAPSHOT.jar`

## Important Notes

1. **HttpClient**: The project continues to use HttpClient 4.x (updated to 4.5.14 from 4.5.8). No code changes required for HttpClient.

2. **Spring Boot 3 Changes**: Spring Boot 3 requires Java 17 minimum. This project is configured for Java 17.

3. **Gradle Wrapper**: The project now includes Gradle wrapper (gradlew and gradlew.bat), so you don't need to install Gradle separately.

4. **Configuration Files**:
   - Maven files (pom.xml, mvnw, mvnw.cmd, .mvn/) can be removed if desired
   - Gradle files: build.gradle, settings.gradle, gradle.properties, gradlew, gradlew.bat, gradle/

## Testing the Migration

1. Clean any existing build artifacts:
   ```bash
   ./gradlew clean
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

4. Verify the application starts successfully and all endpoints work as expected.

## Rollback (if needed)

If you need to rollback to Maven:
1. The original `pom.xml` is still present in the project
2. Remove the Gradle files: `build.gradle`, `settings.gradle`, `gradle.properties`, `gradlew`, `gradlew.bat`, and `gradle/` directory
3. Use Maven commands: `./mvnw spring-boot:run`
