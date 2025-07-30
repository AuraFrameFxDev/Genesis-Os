# Genesis-OS Module Guide

This document outlines the module structure and build configuration for the Genesis-OS project.

## Project Structure

```
Genesis-Os/
├── app/                    # Main application module
├── core/                   # Core modules with no UI dependencies
│   ├── common/            # Common utilities, extensions, and base classes
│   ├── data/              # Data layer (repositories, data sources)
│   ├── domain/            # Business logic and use cases
│   └── ui/                # Core UI components and theming
├── features/              # Feature modules (each feature in its own module)
│   ├── feature-a/         # Example feature module
│   └── feature-b/         # Another example feature module
├── data/                  # Data-related modules
│   ├── local/             # Local data sources (Room, DataStore)
│   ├── remote/            # Remote data sources (Retrofit)
│   └── repository/        # Repository implementations
└── ui/                    # UI components
    ├── components/        # Reusable UI components
    └── theme/             # Theme and styling
```

## Build Configuration

### Version Catalog

All dependencies are managed in `gradle/libs.versions.toml`. This file serves as a single source of
truth for all dependencies and their versions.

### Module Templates

- `feature-module-template.gradle.kts`: Template for feature modules
- `core-module-template.gradle.kts`: Template for core modules

### Common Tasks

- `./gradlew assemble`: Build all variants
- `./gradlew test`: Run all tests
- `./gradlew dokkaHtml`: Generate documentation
- `./gradlew dependencyUpdates`: Check for dependency updates

## Best Practices

1. **Module Dependencies**:
    - Feature modules can depend on core modules
    - Core modules should not depend on feature modules
    - Avoid circular dependencies between modules

2. **Code Organization**:
    - Follow the package-by-feature approach
    - Keep UI logic separate from business logic
    - Use dependency injection consistently

3. **Testing**:
    - Each module should have its own test directory
    - Use JUnit for unit tests
    - Use Espresso for UI tests
    - Use MockK for mocking in tests

## Build Optimization

- K2 compiler is enabled for faster compilation
- Configuration cache is enabled for faster builds
- Parallel execution is enabled for multi-module builds
- Build scan is configured for build analysis

## Documentation

- Use KDoc for public APIs
- Run `./gradlew dokkaHtml` to generate documentation
- Documentation is available in `build/dokka`

## CI/CD

GitHub Actions is configured for:

- Building and testing on every push
- Generating and publishing documentation
- Running static analysis
- Publishing releases
