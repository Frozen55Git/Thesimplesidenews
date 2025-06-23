@echo off
echo Setting up PostgreSQL database for TheSimpleSide News...
echo.

REM Check if PostgreSQL is running
echo Checking PostgreSQL connection...
psql -U postgres -c "SELECT version();" >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Cannot connect to PostgreSQL. Please ensure:
    echo 1. PostgreSQL is installed and running
    echo 2. psql is in your PATH
    echo 3. You can connect as postgres user
    echo.
    pause
    exit /b 1
)

echo PostgreSQL connection successful!
echo.

REM Create database if it doesn't exist
echo Creating database 'thesimpleside_news'...
psql -U postgres -c "CREATE DATABASE thesimpleside_news;" >nul 2>&1
if %errorlevel% equ 0 (
    echo Database created successfully!
) else (
    echo Database already exists or creation failed.
)

echo.
echo Database setup completed!
echo.
echo Next steps:
echo 1. Set environment variables or update application.yml with your database credentials
echo 2. Run: mvn spring-boot:run
echo 3. Access the application at: http://localhost:8080
echo.
pause 