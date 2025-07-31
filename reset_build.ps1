# Reset build script for Genesis-OS
Write-Host "Cleaning build directories..."

# Remove build directories
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue .gradle
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue buildSrc\.gradle
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue buildSrc\build
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue .gradle-kotlin
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue build\.gradle-kotlin

Write-Host "Build directories cleaned."
Write-Host "You can now run the build again with: .\gradlew.bat build"
