# TheSimpleSide News - Spring Boot Version

## Prerequisites
- Java 17 or higher
- Maven 3.8.x or higher
- PostgreSQL 15 or higher
- Git

## Setup Instructions

### 1. Database Setup
```sql
-- Connect to PostgreSQL and run:
CREATE DATABASE thesimpleside_news;
```

### 2. Environment Variables
Create a `.env` file in the project root with:
```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/thesimpleside_news
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET_KEY=your_jwt_secret_key
```

### 3. Build & Run
```bash
# Clean and install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
```

### 4. Access the Application
- Main application: http://localhost:8080
- API Documentation: http://localhost:8080/swagger-ui.html
- H2 Console (if enabled): http://localhost:8080/h2-console

## Development

### Project Structure
```
src/main/java/com/thesimpleside/news/
├── config/         # Configuration classes
├── controller/     # REST controllers
├── dto/           # Data Transfer Objects
├── exception/     # Custom exceptions
├── model/         # Entity classes
├── repository/    # JPA repositories
├── security/      # Security configurations
├── service/       # Business logic
└── util/          # Utility classes

src/main/resources/
├── static/        # Static resources (CSS, JS, images)
├── templates/     # Thymeleaf templates
└── application.yml # Application configuration
```

### Key Features
- Real-time stock tracking
- Portfolio management
- News aggregation
- Insider trading alerts
- User authentication
- Admin dashboard

### API Endpoints
- `/api/v1/auth/*` - Authentication endpoints
- `/api/v1/stocks/*` - Stock-related operations
- `/api/v1/news/*` - News operations
- `/api/v1/portfolio/*` - Portfolio management
- `/api/v1/alerts/*` - Alert system
- `/api/v1/admin/*` - Admin operations

## Testing
```bash
# Run tests
mvn test

# Run specific test class
mvn test -Dtest=StockServiceTest
```

## Production Deployment
1. Update application-prod.yml with production settings
2. Build the production JAR:
```bash
mvn clean package -Pprod
```
3. Run the JAR:
```bash
java -jar target/thesimpleside-news-0.1.0.jar --spring.profiles.active=prod
``` 