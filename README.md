# Railway Management System

A comprehensive web-based Railway Management System built with Spring Boot 3.5.7, Java 21, MySQL, and Thymeleaf.

## Features

### User Features
- User registration and authentication
- Search trains by source, destination, and date
- Book train tickets
- View booking history
- Cancel bookings
- Secure payment processing
- Responsive UI with Bootstrap 5

### Admin Features
- Admin dashboard
- Add, edit, and delete trains
- View all bookings
- Manage train schedules
- Audit logging for all operations

## Technology Stack

- **Backend:** Java 21, Spring Boot 3.5.7
- **Database:** MySQL 8.0
- **ORM:** JPA/Hibernate
- **Security:** Spring Security 6
- **Frontend:** Thymeleaf, Bootstrap 5, Bootstrap Icons
- **Build Tool:** Maven
- **Database Migration:** Flyway

## Prerequisites

Before running this application, ensure you have the following installed:

- Java 21 or higher
- Maven 3.6+
- MySQL 8.0 or higher
- Git (optional)

## Database Setup

1. **Install MySQL** (if not already installed)

2. **Create the database:**

```sql
CREATE DATABASE railway_management;
```

3. **Create a MySQL user** (optional, or use root):

```sql
CREATE USER 'railway_user'@'localhost' IDENTIFIED BY 'Password_123';
GRANT ALL PRIVILEGES ON railway_management.* TO 'railway_user'@'localhost';
FLUSH PRIVILEGES;
```

## Installation & Running

### Method 1: Using Maven Wrapper (Recommended)

1. **Clone or navigate to the project directory:**

```bash
cd RailwayManagement
```

2. **Run the application:**

```bash
# On Windows
mvnw.cmd spring-boot:run

# On Linux/Mac
./mvnw spring-boot:run
```

### Method 2: Using Maven

1. **Navigate to the project directory:**

```bash
cd RailwayManagement
```

2. **Clean and build the project:**

```bash
mvn clean install
```

3. **Run the application:**

```bash
mvn spring-boot:run
```

### Method 3: Using JAR file

1. **Build the JAR file:**

```bash
mvn clean package
```

2. **Run the JAR:**

```bash
java -jar target/RailwayManagement-0.0.1-SNAPSHOT.jar
```

## Accessing the Application

Once the application is running, open your web browser and navigate to:

```
http://localhost:8080
```

## Default Credentials

### Admin Account
- **Email:** admin@railway.com
- **Password:** admin123

### User Account
- **Email:** user@railway.com
- **Password:** user123

## Application Configuration

The application configuration is located in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/railway_management
spring.datasource.username=root
spring.datasource.password=Password_123

# Server Configuration
server.port=8080
```

**Note:** Update the database credentials if you're using different values.

## Project Structure

```
RailwayManagement/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/RailwayManagement/
│   │   │       ├── config/              # Security & App Configuration
│   │   │       ├── controller/          # Web Controllers
│   │   │       ├── entity/              # JPA Entities
│   │   │       ├── exception/           # Custom Exceptions
│   │   │       ├── repository/          # Data Repositories
│   │   │       ├── service/             # Business Logic
│   │   │       └── RailwayManagementApplication.java
│   │   └── resources/
│   │       ├── db/migration/            # Flyway SQL Scripts
│   │       ├── templates/               # Thymeleaf Templates
│   │       └── application.properties   # Application Config
│   └── test/                            # Test Classes
├── pom.xml                              # Maven Dependencies
└── README.md                            # This file
```

## Database Schema

The application uses the following main tables:

- **users** - User accounts (admin/user)
- **trains** - Train information
- **bookings** - Booking records
- **payments** - Payment transactions
- **audit_logs** - System audit trail

Database schema is automatically created using Flyway migrations.

## API Endpoints

### Public Endpoints
- `GET /` - Home page (redirects to login)
- `GET /login` - Login page
- `POST /login` - Process login
- `GET /register` - Registration page
- `POST /register` - Process registration

### User Endpoints
- `GET /dashboard` - User dashboard
- `GET /trains` - Search trains
- `GET /user/bookings` - View bookings
- `POST /user/bookings/book/{trainId}` - Book a train
- `POST /user/bookings/cancel/{id}` - Cancel booking
- `GET /user/bookings/payment/{bookingId}` - Payment page
- `POST /user/bookings/payment/{bookingId}` - Process payment

### Admin Endpoints
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/trains` - Manage trains
- `GET /admin/trains/add` - Add train form
- `POST /admin/trains/add` - Create train
- `GET /admin/trains/edit/{id}` - Edit train form
- `POST /admin/trains/edit/{id}` - Update train
- `GET /admin/trains/delete/{id}` - Delete train

## Features in Detail

### User Registration & Authentication
- Secure password encryption using BCrypt
- Role-based access control (ADMIN/USER)
- Session management

### Train Management
- CRUD operations for trains
- Search by source, destination, and date
- Real-time seat availability

### Booking System
- Book multiple seats
- Automatic seat allocation
- Booking confirmation
- Cancellation with seat release

### Payment Processing
- Multiple payment modes (Credit Card, Debit Card, UPI, Net Banking, Wallet)
- Transaction ID generation
- Payment history

### Audit Logging
- Track all user actions
- IP address and user agent logging
- Timestamp for all operations

## Troubleshooting

### Database Connection Issues

If you encounter database connection errors:

1. Verify MySQL is running:
```bash
# Windows
net start MySQL80

# Linux
sudo systemctl start mysql
```

2. Check database credentials in `application.properties`

3. Ensure the database exists:
```sql
SHOW DATABASES;
```

### Port Already in Use

If port 8080 is already in use, change it in `application.properties`:

```properties
server.port=8081
```

### Flyway Migration Errors

If Flyway migration fails, you can reset the database:

```sql
DROP DATABASE railway_management;
CREATE DATABASE railway_management;
```

Then restart the application.

## Development

### Running in Development Mode

For development with auto-reload, use Spring Boot DevTools (already included):

```bash
mvn spring-boot:run
```

### Building for Production

```bash
mvn clean package -DskipTests
```

## Testing

Run tests using:

```bash
mvn test
```

## Security Notes

- All passwords are encrypted using BCrypt
- CSRF protection is enabled
- SQL injection prevention via JPA
- XSS protection via Thymeleaf escaping

## Future Enhancements

- Email notifications
- SMS alerts
- Real payment gateway integration
- Train route visualization
- Seat selection interface
- Multi-language support
- Mobile application
- Advanced reporting

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is created for educational purposes.

## Support

For issues and questions:
- Email: support@railway.com
- Phone: 1800-XXX-XXXX

## Authors

- Railway Management Team

## Acknowledgments

- Spring Boot Team
- Thymeleaf Team
- Bootstrap Team

---

**Note:** This is a demo application. For production use, implement additional security measures, proper payment gateway integration, and comprehensive testing.
