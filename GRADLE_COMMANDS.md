# Gradle Commands Quick Reference

## Essential Commands

### Build Commands
```bash
# Clean build artifacts
./gradlew clean

# Compile the project
./gradlew build

# Build without running tests
./gradlew build -x test

# Clean and build
./gradlew clean build
```

### Run Commands
```bash
# Run the Spring Boot application
./gradlew bootRun

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Run tests
./gradlew test

# Run specific test class
./gradlew test --tests com.example.MyTestClass
```

### Package Commands
```bash
# Create executable JAR
./gradlew bootJar

# Create WAR file (if configured)
./gradlew bootWar

# View JAR contents
jar -tf build/libs/demo-0.0.1-SNAPSHOT.jar
```

### Information Commands
```bash
# List all available tasks
./gradlew tasks

# List all tasks (including sub-tasks)
./gradlew tasks --all

# Show project dependencies
./gradlew dependencies

# Show dependency tree
./gradlew dependencies --configuration runtimeClasspath

# Display project properties
./gradlew properties
```

### Maintenance Commands
```bash
# Refresh dependencies
./gradlew build --refresh-dependencies

# Clean Gradle cache
./gradlew clean cleanBuildCache

# Update Gradle wrapper
./gradlew wrapper --gradle-version 8.5

# Stop Gradle daemon
./gradlew --stop
```

## Maven to Gradle Command Mapping

| Maven Command | Gradle Equivalent |
|---------------|-------------------|
| `mvn clean` | `./gradlew clean` |
| `mvn compile` | `./gradlew classes` |
| `mvn test` | `./gradlew test` |
| `mvn package` | `./gradlew build` |
| `mvn install` | `./gradlew build` |
| `mvn spring-boot:run` | `./gradlew bootRun` |
| `mvn dependency:tree` | `./gradlew dependencies` |

## Common Options

```bash
# Run in offline mode
./gradlew build --offline

# Show stack traces on errors
./gradlew build --stacktrace

# Show full stack traces
./gradlew build --full-stacktrace

# Run with debug logging
./gradlew build --debug

# Run with info logging
./gradlew build --info

# Run in quiet mode
./gradlew build --quiet

# Continue build even if there are failures
./gradlew build --continue

# Show build scan
./gradlew build --scan
```

## Performance Tips

```bash
# Use parallel execution (already configured in gradle.properties)
./gradlew build --parallel

# Use build cache (already configured in gradle.properties)
./gradlew build --build-cache

# Run without daemon (for CI/CD)
./gradlew build --no-daemon

# Maximum workers
./gradlew build --max-workers=4
```

## Troubleshooting

```bash
# Clean everything and rebuild
./gradlew clean build --refresh-dependencies

# Debug dependency resolution
./gradlew dependencies --configuration compileClasspath

# Verify Java version
java -version

# Check Gradle version
./gradlew --version

# List build cache entries
./gradlew cleanBuildCache
```

## IDE Integration

### IntelliJ IDEA
1. File → New → Project from Existing Sources
2. Select the project directory
3. Choose "Gradle" as the external model
4. IntelliJ will auto-import the Gradle project

### Eclipse
1. Install "Buildship Gradle Integration" plugin
2. File → Import → Gradle → Existing Gradle Project
3. Select the project directory

### VS Code
1. Install "Gradle for Java" extension
2. Open the project folder
3. VS Code will detect the Gradle build

## Environment Variables

```bash
# Set Java home for Gradle
export JAVA_HOME=/path/to/java17

# Set Gradle options
export GRADLE_OPTS="-Xmx2048m -Dorg.gradle.daemon=true"
```

## Useful Gradle Properties

Add these to `gradle.properties` for customization:

```properties
# JVM arguments
org.gradle.jvmargs=-Xmx2048m -XX:MaxMetaspaceSize=512m

# Parallel execution
org.gradle.parallel=true

# Build cache
org.gradle.caching=true

# Daemon
org.gradle.daemon=true

# Configure on demand
org.gradle.configureondemand=true

# File encoding
org.gradle.jvmargs=-Dfile.encoding=UTF-8
```

## Running the JAR

After building with `./gradlew bootJar`:

```bash
# Run the generated JAR
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar

# Run with specific profile
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

# Run with custom port
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar --server.port=9090
```
