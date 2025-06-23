# TheSimpleSide News - Spring Boot Version

A comprehensive financial news and stock tracking platform that helps users follow and analyze the investment activities of politicians, corporate insiders, and superinvestors.

## 🚀 Features

- **User Authentication & Authorization**: Secure database-based authentication with role-based access control
- **Real-time Stock Tracking**: Comprehensive stock data with live updates
- **Portfolio Management**: User-specific portfolios with performance tracking
- **News Aggregation**: Multi-source news with stock relationships
- **Alert System**: Personalized notifications for market events
- **Admin Dashboard**: Administrative tools for user and content management
- **Responsive UI**: Modern, mobile-friendly interface

## 🛠️ Prerequisites

- **Java 17** or higher
- **Maven 3.8.x** or higher
- **PostgreSQL 15** or higher
- **Git**

## 📋 Setup Instructions

### 1. Database Setup

```sql
-- Connect to PostgreSQL as superuser and run:
CREATE DATABASE thesimpleside_news;
```

### 2. Environment Configuration

Create a `.env` file in the project root with your database credentials:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/thesimpleside_news
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET_KEY=your_jwt_secret_key_here
```

### 3. Build & Run

```bash
# Clean and install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
```

### 4. Access the Application

- **Main application**: http://localhost:8080
- **Login page**: http://localhost:8080/login
- **Registration page**: http://localhost:8080/register
- **Dashboard**: http://localhost:8080/dashboard (after login)

## 👤 Default Users

The application automatically creates these default users on startup:

| Email | Password | Role | Description |
|-------|----------|------|-------------|
| admin@thesimpleside.com | admin123 | ADMIN | Full administrative access |
| user@thesimpleside.com | user123 | USER | Standard user access |
| premium@thesimpleside.com | premium123 | PREMIUM_USER | Premium features access |

## 🏗️ Project Structure

```
src/main/java/com/thesimpleside/news/
├── config/              # Configuration classes
│   ├── SecurityConfig.java
│   ├── WebMvcConfig.java
│   ├── WebSocketConfig.java
│   └── DataInitializer.java
├── controller/          # REST controllers
│   ├── HomeController.java
│   ├── AuthController.java
│   ├── StockController.java
│   └── TestController.java
├── dto/                # Data Transfer Objects
│   ├── UserRegistrationDto.java
│   ├── UserLoginDto.java
│   └── UserProfileDto.java
├── exception/          # Custom exceptions
├── model/             # Entity classes
│   ├── User.java
│   ├── Stock.java
│   ├── News.java
│   ├── Portfolio.java
│   ├── Alert.java
│   └── Role.java
├── repository/        # JPA repositories
│   ├── UserRepository.java
│   └── StockRepository.java
├── security/         # Security configurations
├── service/          # Business logic
│   ├── UserService.java
│   ├── StockService.java
│   └── impl/
│       ├── UserServiceImpl.java
│       └── StockServiceImpl.java
└── util/             # Utility classes

src/main/resources/
├── static/           # Static resources (CSS, JS, images)
├── templates/        # Thymeleaf templates
│   ├── auth/
│   │   ├── login.html
│   │   ├── register.html
│   │   └── profile.html
│   ├── dashboard.html
│   ├── index.html
│   └── layout/
└── application.yml   # Application configuration
```

## 🔐 Security Features

- **BCrypt Password Encoding**: Secure password hashing
- **Role-based Access Control**: USER, ADMIN, PREMIUM_USER roles
- **Session Management**: Secure session handling with remember-me functionality
- **CSRF Protection**: Cross-site request forgery protection
- **Input Validation**: Comprehensive form validation with error handling

## 🎨 Frontend Features

- **Bootstrap 5.3.2**: Modern, responsive UI framework
- **Thymeleaf**: Server-side templating with security integration
- **jQuery 3.7.1**: Dynamic client-side interactions
- **Bootstrap Icons**: Professional icon set
- **Responsive Design**: Mobile-first approach

## 📊 API Endpoints

### Authentication
- `POST /register` - User registration
- `POST /login` - User authentication
- `GET /logout` - User logout
- `GET /profile` - User profile (authenticated)

### Public
- `GET /` - Home page
- `GET /login` - Login page
- `GET /register` - Registration page

### Protected
- `GET /dashboard` - User dashboard (authenticated)
- `GET /profile` - User profile (authenticated)

### Admin Only
- `/admin/**` - Admin panel (ADMIN role required)

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn test jacoco:report
```

## 🚀 Production Deployment

1. **Update application-prod.yml** with production settings:
```yaml
spring:
  profiles: prod
  datasource:
    url: jdbc:postgresql://your-prod-db:5432/thesimpleside_news
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate
```

2. **Build the production JAR**:
```bash
mvn clean package -Pprod
```

3. **Run the JAR**:
```bash
java -jar target/thesimpleside-news-0.1.0.jar --spring.profiles.active=prod
```

## 🔧 Configuration

### Database Configuration
The application uses PostgreSQL with the following default settings:
- **Database**: thesimpleside_news
- **Username**: postgres
- **Password**: postgres
- **Port**: 5432

### Security Configuration
- **Password Encoding**: BCrypt with strength 10
- **Session Timeout**: 30 minutes
- **Remember Me**: 24 hours
- **JWT Secret**: Configurable via environment variable

## 🐛 Troubleshooting

### User Registration Issues

If you're experiencing problems where users can register but cannot log in, follow these steps:

#### 1. Database Setup
```bash
# Windows - Run the database setup script
setup-database.bat

# Or manually create the database
psql -U postgres -c "CREATE DATABASE thesimpleside_news;"
```

#### 2. Environment Variables
Set your database credentials as environment variables:

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

#### 3. Test Database Connection
```bash
# Test application health
curl http://localhost:8080/test/health

# Check existing users
curl http://localhost:8080/test/users

# Check specific user
curl http://localhost:8080/test/users/test@example.com
```

#### 4. Verify User Registration
1. Register a new user at http://localhost:8080/register
2. Check if user was saved: `curl http://localhost:8080/test/users/{email}`
3. Try logging in with the registered credentials

#### 5. Common Solutions
- **"Invalid credentials"**: Check if user exists in database
- **"Email already exists"**: User might be partially created, check database
- **No users in database**: Database connection or transaction issues

For detailed troubleshooting, see [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

### Common Issues

1. **Database Connection Error**:
   - Ensure PostgreSQL is running
   - Check database credentials in application.yml
   - Verify database exists: `thesimpleside_news`

2. **Port Already in Use**:
   - Change server port in application.yml
   - Kill existing process: `lsof -ti:8080 | xargs kill -9`

3. **Authentication Issues**:
   - Check user credentials
   - Verify user is enabled in database
   - Check role assignments

4. **User Registration Problems**:
   - Run `test-database.bat` to verify setup
   - Check application logs for errors
   - Verify database tables are created

### Logs
Enable debug logging by setting in application.yml:
```yaml
logging:
  level:
    com.thesimpleside: DEBUG
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
```

### Testing Commands
```bash
# Test application health
curl http://localhost:8080/test/health

# List all users
curl http://localhost:8080/test/users

# Check email existence
curl http://localhost:8080/test/email-exists/test@example.com
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review the troubleshooting section

---

**TheSimpleSide News** - Follow the smart money with real-time trade tracking of politicians, insiders, and superinvestors. 