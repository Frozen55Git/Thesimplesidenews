# Troubleshooting Guide - User Registration Issues

## Problem Description
Users can register but cannot log in afterward. The system shows "Invalid credentials" even though the user appears to be registered.

## Root Causes and Solutions

### 1. Database Connection Issues

**Symptoms:**
- User registration appears successful but user cannot log in
- "Email already exists" when trying to register again
- No users visible in database

**Solutions:**

#### A. Verify Database Connection
```bash
# Test database connection
curl http://localhost:8080/test/health

# Check if users exist in database
curl http://localhost:8080/test/users
```

#### B. Check Database Configuration
Ensure your `application.yml` has correct database settings:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/thesimpleside_news
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

#### C. Set Environment Variables (Recommended)
Create a `.env` file or set environment variables:

```bash
# Windows
set SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/thesimpleside_news
set SPRING_DATASOURCE_USERNAME=postgres
set SPRING_DATASOURCE_PASSWORD=postgres

# Linux/Mac
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/thesimpleside_news
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
```

### 2. Database Setup Issues

**Symptoms:**
- Application starts but no tables are created
- Database connection errors

**Solutions:**

#### A. Create Database Manually
```sql
-- Connect to PostgreSQL as superuser
CREATE DATABASE thesimpleside_news;
```

#### B. Run Database Setup Script
```bash
# Windows
setup-database.bat

# Or manually
psql -U postgres -c "CREATE DATABASE thesimpleside_news;"
```

### 3. Transaction Management Issues

**Symptoms:**
- User appears to be saved but not persisted
- Inconsistent user data

**Solutions:**

The application has been updated with proper transaction management:
- Removed class-level `@Transactional` annotation
- Added method-level `@Transactional` annotations
- Added verification after user creation

### 4. Password Encoding Issues

**Symptoms:**
- User created but cannot authenticate
- Password mismatch errors

**Solutions:**

The application now properly encodes passwords using BCrypt:
- Passwords are encoded before saving
- Authentication uses the same encoder

### 5. User Entity Issues

**Symptoms:**
- Lombok warnings during compilation
- Builder pattern not working correctly

**Solutions:**

Fixed User entity with proper `@Builder.Default` annotations:
- Added `@Builder.Default` to boolean fields
- Ensured proper default values

## Testing Steps

### 1. Verify Application Health
```bash
curl http://localhost:8080/test/health
```

### 2. Check Default Users
```bash
curl http://localhost:8080/test/users
```

### 3. Test User Registration
1. Go to http://localhost:8080/register
2. Register a new user
3. Check if user exists: `curl http://localhost:8080/test/users/{email}`
4. Try to log in

### 4. Verify Database Tables
```sql
-- Connect to thesimpleside_news database
\c thesimpleside_news

-- Check if users table exists
\dt users

-- Check user records
SELECT id, email, first_name, last_name, role, enabled FROM users;
```

## Common Error Messages and Solutions

### "Invalid credentials"
- Check if user exists in database
- Verify password encoding
- Check user account status (enabled, locked, etc.)

### "Email already exists"
- User might be partially created
- Check database for duplicate entries
- Clear database and restart application

### "User not found"
- Database connection issue
- User not properly saved
- Transaction rollback

## Debugging Commands

### Check Application Logs
```bash
# Look for user creation logs
grep "Creating new user" application.log

# Look for database errors
grep "ERROR" application.log
```

### Database Queries
```sql
-- Check all users
SELECT * FROM users;

-- Check specific user
SELECT * FROM users WHERE email = 'test@example.com';

-- Check user count
SELECT COUNT(*) FROM users;
```

### Test API Endpoints
```bash
# Health check
curl http://localhost:8080/test/health

# List all users
curl http://localhost:8080/test/users

# Check specific user
curl http://localhost:8080/test/users/test@example.com

# Check email existence
curl http://localhost:8080/test/email-exists/test@example.com
```

## Prevention Measures

1. **Always use environment variables** for database configuration
2. **Test database connection** before starting application
3. **Monitor application logs** for errors
4. **Use the test endpoints** to verify data persistence
5. **Regular database backups** to prevent data loss

## Getting Help

If issues persist:
1. Check application logs for detailed error messages
2. Verify database connection and permissions
3. Test with the provided API endpoints
4. Ensure PostgreSQL is running and accessible
5. Check firewall and network connectivity 