@echo off
echo Testing TheSimpleSide News Database Connection...
echo.

REM Wait for application to start
echo Waiting for application to start...
timeout /t 10 /nobreak >nul

REM Test health endpoint
echo Testing application health...
curl -s http://localhost:8080/test/health
if %errorlevel% neq 0 (
    echo ERROR: Application is not responding. Please ensure the application is running.
    pause
    exit /b 1
)

echo.
echo Application is running!
echo.

REM Test users endpoint
echo Testing database connection...
curl -s http://localhost:8080/test/users
if %errorlevel% neq 0 (
    echo ERROR: Cannot retrieve users from database.
    pause
    exit /b 1
)

echo.
echo Database connection successful!
echo.

echo Test completed successfully.
echo You can now:
echo 1. Register a new user at: http://localhost:8080/register
echo 2. Login with existing users:
echo    - admin@thesimpleside.com / admin123
echo    - user@thesimpleside.com / user123
echo    - premium@thesimpleside.com / premium123
echo.
pause 