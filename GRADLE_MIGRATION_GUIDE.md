# Gradle Migration Guide

## Overview
This project has been successfully migrated from Maven to Gradle and updated to use the latest versions of Java and Spring Boot.

## Version Updates

### Previous Versions
- Java: 1.8
- Spring Boot: 2.1.4.RELEASE
- Build Tool: Maven

### Current Versions
- Java: 21
- Spring Boot: 3.2.2
- Build Tool: Gradle 8.5

## New Dependencies Added

### PDF Processing
- Apache PDFBox 3.0.1 - For extracting text and data from PDF files
- PDFBox IO 3.0.1 - Supporting library for PDFBox

### Updated Dependencies
- Apache Commons Lang3: 3.14.0 (updated from 3.0)
- Apache HttpClient: 5.3.1 (updated from 4.5.8)
- Gson: 2.10.1 (updated from 2.8.5)

### Additional Dependencies
- Spring Boot Validation Starter - For input validation
- Lombok 1.18.30 - For reducing boilerplate code (optional)

## Building and Running the Project

### Using Gradle Wrapper (Recommended)

#### Build the project:
```bash
./gradlew build
```

#### Run the application:
```bash
./gradlew bootRun
```

#### Clean and rebuild:
```bash
./gradlew clean build
```

#### Run tests:
```bash
./gradlew test
```

### Direct Gradle Commands
If you have Gradle installed globally:
```bash
gradle build
gradle bootRun
gradle test
```

## Project Structure

```
JavaApi/
├── build.gradle                 # Gradle build configuration
├── settings.gradle              # Project settings
├── gradle.properties            # Gradle properties
├── gradlew                      # Gradle wrapper (Unix)
├── gradlew.bat                  # Gradle wrapper (Windows)
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/example/
    │   │       ├── DemoApplication.java
    │   │       ├── controllor/
    │   │       │   ├── CaptchaApi.java
    │   │       │   ├── LoginApi.java
    │   │       │   └── PriceController.java    # NEW
    │   │       ├── data/
    │   │       │   ├── User.java
    │   │       │   ├── Captcha.java
    │   │       │   └── PriceInfo.java          # NEW
    │   │       ├── repository/
    │   │       │   ├── UserRepo.java
    │   │       │   ├── CaptchaRepo.java
    │   │       │   └── PriceInfoRepository.java # NEW
    │   │       ├── service/
    │   │       │   ├── UserService.java
    │   │       │   ├── CaptchaService.java
    │   │       │   └── PriceService.java       # NEW
    │   │       ├── serviceimpl/
    │   │       │   ├── UserServiceImpl.java
    │   │       │   ├── CaptchaServiceImpl.java
    │   │       │   └── PriceServiceImpl.java   # NEW
    │   │       └── util/
    │   │           └── PdfPriceExtractor.java  # NEW
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/
```

## Migration Notes

### Removed Files
The following Maven files can be removed (they are no longer needed):
- pom.xml
- mvnw
- mvnw.cmd
- .mvn/ directory

### Configuration Changes
- MongoDB configuration remains the same in application.properties
- Added file upload configuration for PDF processing
- Added logging configuration for better debugging

## Compatibility Notes

### Spring Boot 3.x Changes
Spring Boot 3.x requires Java 17 or higher and includes:
- Jakarta EE namespace (javax.* → jakarta.*)
- Updated dependency coordinates
- Improved security features
- Better performance

### Java 21 Features Available
Your project can now use:
- Pattern matching for switch
- Record patterns
- Virtual threads
- Sequenced collections
- String templates (preview)

## Troubleshooting

### If build fails:
1. Ensure Java 21 is installed: `java -version`
2. Clean the build: `./gradlew clean`
3. Delete build/ directory and try again
4. Check MongoDB is running: `mongod --version`

### If tests fail:
1. Ensure MongoDB is running and accessible
2. Check application.properties for correct MongoDB URI
3. Run tests with verbose output: `./gradlew test --info`

## Next Steps

1. Remove Maven files if migration is successful:
   ```bash
   rm pom.xml mvnw mvnw.cmd
   rm -rf .mvn/
   ```

2. Update your IDE to use Gradle instead of Maven

3. Configure CI/CD pipelines to use Gradle commands

4. Review and update any documentation referencing Maven commands
