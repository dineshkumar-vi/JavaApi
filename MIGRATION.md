# Maven to Gradle Migration & Java/Spring Boot Upgrade

## Summary of Changes

This document outlines the migration of the project from Maven to Gradle and the upgrade to the latest Java and Spring Boot versions.

## Version Updates

### Before
- **Java**: 1.8
- **Spring Boot**: 2.1.4.RELEASE
- **Apache Commons Lang**: 3.0
- **Apache HttpClient**: 4.5.8
- **Gson**: 2.8.5
- **Build Tool**: Maven

### After
- **Java**: 17 (LTS)
- **Spring Boot**: 3.3.6
- **Apache Commons Lang**: 3.17.0
- **Apache HttpClient**: 5.4.1 (HttpClient 5)
- **Gson**: 2.11.0
- **Build Tool**: Gradle 8.11.1

## Files Created

### Gradle Build Files
1. **build.gradle** - Main build configuration file
2. **settings.gradle** - Project settings
3. **gradle.properties** - Gradle JVM and build properties
4. **gradlew** - Gradle wrapper script (Unix/Mac)
5. **gradlew.bat** - Gradle wrapper script (Windows)
6. **gradle/wrapper/gradle-wrapper.jar** - Gradle wrapper jar
7. **gradle/wrapper/gradle-wrapper.properties** - Gradle wrapper configuration

### Updated Files
1. **.gitignore** - Updated to include Gradle-specific ignores
2. **src/main/java/com/example/serviceimpl/UserServiceImpl.java** - Updated to use Apache HttpClient 5.x API

## Code Changes

### UserServiceImpl.java
Updated Apache HttpClient imports from version 4.x to 5.x:

**Before:**
```java
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
```

**After:**
```java
import java.nio.charset.StandardCharsets;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
```

**API Change:**
```java
// Before
String response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

// After
String response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
```

## Building the Project

### With Gradle

#### Clean and Build
```bash
./gradlew clean build
```

#### Run the Application
```bash
./gradlew bootRun
```

#### Run Tests
```bash
./gradlew test
```

#### Create Executable JAR
```bash
./gradlew bootJar
```
The JAR will be created in `build/libs/demo-0.0.1-SNAPSHOT.jar`

### Running the JAR
```bash
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

## Migration Notes

1. **Maven files preserved**: The original Maven files (pom.xml, mvnw, mvnw.cmd, .mvn/) are still present and can be removed if you're fully committed to Gradle.

2. **Spring Boot 3.x**: Spring Boot 3.x requires Java 17 or higher. This project now uses Java 17 (LTS).

3. **Jakarta EE**: Spring Boot 3.x uses Jakarta EE instead of javax packages. The current code doesn't use many javax imports, so no changes were needed.

4. **HttpClient 5.x**: The Apache HttpClient library was upgraded from 4.x to 5.x, which required package name changes (org.apache.http → org.apache.hc).

5. **MongoDB Compatibility**: Spring Boot 3.3.6 includes MongoDB driver updates that are backward compatible with MongoDB 3.6+.

## Next Steps

1. **Test the application** thoroughly to ensure all functionality works as expected
2. **Remove Maven files** if you want to fully commit to Gradle:
   ```bash
   rm pom.xml mvnw mvnw.cmd
   rm -rf .mvn
   ```
3. **Update CI/CD pipelines** to use Gradle instead of Maven
4. **Review and update any IDE configurations** (Eclipse, IntelliJ IDEA, VS Code)

## Troubleshooting

### If you encounter compilation errors:
```bash
./gradlew clean build --refresh-dependencies
```

### To see dependency tree:
```bash
./gradlew dependencies
```

### To check for dependency updates:
```bash
./gradlew dependencyUpdates
```

## Additional Resources

- [Spring Boot 3.3.6 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.3-Release-Notes)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [Apache HttpClient 5.x Migration Guide](https://hc.apache.org/httpcomponents-client-5.4.x/migration-guide/index.html)
