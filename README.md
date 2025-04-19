# ResamoPro

ResamoPro is an Android application built with modern Android development practices and architecture.

## Project Structure

The project follows a clean architecture approach with the following main components:

- `app/` - Main application module
- `data/` - Data layer implementation
- `domain/` - Domain layer with business logic and models

## Tech Stack

- **Language**: Kotlin
- **Architecture**: Clean Architecture
- **Dependency Injection**: Hilt
- **Firebase Integration**: 
  - Firebase Crashlytics
  - Google Services
- **Build System**: Gradle (Kotlin DSL)

## Prerequisites

- Android Studio - Version 2023.2.1 and above
- JDK 17 or higher
- Android SDK 34 (Android 14)
- Minimum SDK version: 21

## Building the Project

1. Clone the repository
2. Open the project in Android Studio
3. Sync the project with Gradle files
4. Build and run the application

## Running Tests and Code Coverage

### Running Unit Tests
To run all unit tests, use the following command:
```bash
./gradlew test
```

To run tests for a specific module:
```bash
./gradlew :app:test
./gradlew :domain:test
./gradlew :data:test
```

### Running Android Tests
To run all Android tests:
```bash
./gradlew connectedAndroidTest
```

### Generating Code Coverage Report
The project uses JaCoCo for code coverage. To generate a coverage report:

1. Run the tests with coverage:
```bash
./gradlew jacocoTestReport
```

2. The coverage report will be generated in:
   - For unit tests: `app/build/reports/jacoco/jacocoTestReport/html/index.html`
   - For Android tests: `app/build/reports/jacoco/connectedAndroidTest/html/index.html`

3. To view the report, open the `index.html` file in a web browser.

### Running Tests with Coverage in Android Studio
1. Right-click on the test directory or file
2. Select "Run 'Tests' with Coverage"
3. View the coverage results in the Coverage tool window

## Configuration

The project uses the following configuration files:

- `keystore.properties` - Contains signing configuration
- `version.properties` - Manages version information
- `gradle.properties` - Gradle configuration
- `local.properties` - Local development settings

## Author

Denis Oluka