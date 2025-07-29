@echo off
setlocal

:: Set JAVA_HOME to Java 24 installation
set "JAVA_HOME=C:\\Program Files\\Java\\jdk-24.0.2"

:: Add Java 24 to PATH
set "PATH=%JAVA_HOME%\\bin;%PATH%"

:: Verify Java version
echo Using Java version:
java -version

:: Run Gradle wrapper with the specified command
call .\\gradlew %*

endlocal
