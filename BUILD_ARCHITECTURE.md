# AuraOS Build Architecture

```mermaid
graph TD
    subgraph "Root Project (AuraOS)"
        A[settings.gradle.kts] -->|defines| B[Plugin Management]
        A -->|defines| C[Dependency Resolution]
        A -->|includes| D[app]
        A -->|includes| E[oracle-drive-integration]
        A -->|includes| F[buildSrc]
        
        B -->|manages| G[Android Gradle Plugin]
        B -->|manages| H[Kotlin Plugin]
        B -->|manages| I[KSP Plugin]
        
        C -->|resolves from| J[Google Maven]
        C -->|resolves from| K[Maven Central]
        C -->|resolves from| L[JetBrains Maven]
    end

    subgraph "oracle-drive-integration Module"
        M[build.gradle.kts] -->|applies| N[com.android.library]
        M -->|applies| O[org.jetbrains.kotlin.android]
        M -->|applies| P[com.google.devtools.ksp]
        
        M -->|depends on| Q[AndroidX Core]
        M -->|depends on| R[AndroidX Lifecycle]
        M -->|depends on| S[Jetpack Compose]
        M -->|depends on| T[Hilt]
        M -->|depends on| U[Coroutines]
    end

    subgraph "Build Configuration"
        V[Gradle Version] --> "8.14.3"
        W[AGP Version] --> "8.11.1"
        X[Kotlin Version] --> "2.2.0"
        Y[Java Version] --> "24"
        Z[Build Tools] --> "34.0.0"
    end

    style Root fill:#f9f,stroke:#333,stroke-width:2px
    style oracle-drive-integration fill:#bbf,stroke:#333,stroke-width:2px
    style BuildConfiguration fill:#bfb,stroke:#333,stroke-width:2px
```

## Key Configuration Details

### Root Project

- **Gradle Version**: 8.14.3
- **Build Tools**: 34.0.0
- **Java Version**: 17
- **Kotlin Version**: 2.2.0
- **AGP Version**: 8.11.1

### oracle-drive-integration Module

- **Type**: Android Library (com.android.library)
- **Plugins**:
    - Kotlin Android
    - KSP (Kotlin Symbol Processing)
- **Dependencies**:
    - AndroidX Core KTX
    - Lifecycle Runtime KTX
    - Jetpack Compose (BOM)
    - Hilt for DI
    - Kotlin Coroutines

### Repository Configuration

- Google Maven
- Maven Central
- JetBrains Maven
- JitPack
- Custom Compose Repositories

### Build Features

- Type-safe Project Accessors
- Stable Configuration Cache
- Version Catalogs (libs.versions.toml)
- Kotlin DSL for build configuration
