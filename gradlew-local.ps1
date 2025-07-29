# Set up environment variables
$env:GRADLE_HOME = "$PSScriptRoot\.gradle-local\gradle-8.14.3"
$env:PATH = "$env:GRADLE_HOME\bin;$env:PATH"

# Run Gradle with the provided arguments
& "$PSScriptRoot\gradlew.bat" @args
