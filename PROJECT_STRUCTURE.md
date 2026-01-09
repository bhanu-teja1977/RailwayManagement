# Railway Management System - Project Structure

## Complete Directory Structure

```
RailwayManagement/
│
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── RailwayManagement/
│   │   │           ├── config/
│   │   │           │   └── SecurityConfig.java
│   │   │           │
│   │   │           ├── controller/
│   │   │           │   ├── AuthController.java
│   │   │           │   ├── BookingController.java
│   │   │           │   ├── DashboardController.java
│   │   │           │   ├── PaymentController.java
│   │   │           │   └── TrainController.java
│   │   │           │
│   │   │           ├── entity/
│   │   │           │   ├── AuditLog.java
│   │   │           │   ├── Booking.java
│   │   │           │   ├── Payment.java
│   │   │           │   ├── Train.java
│   │   │           │   └── User.java
│   │   │           │
│   │   │           ├── exception/
│   │   │           │   ├── BookingException.java
│   │   │           │   ├── PaymentException.java
│   │   │           │   ├── ResourceNotFoundException.java
│   │   │           │   └── TrainNotAvailableException.java
│   │   │           │
│   │   │           ├── repository/
│   │   │           │   ├── AuditLogRepository.java
│   │   │           │   ├── BookingRepository.java
│   │   │           │   ├── PaymentRepository.java
│   │   │           │   ├── TrainRepository.java
│   │   │           │   └── UserRepository.java
│   │   │           │
│   │   │           ├── service/
│   │   │           │   ├── AuditService.java
│   │   │           │   ├── BookingService.java
│   │   │           │   ├── CustomUserDetailsService.java
│   │   │           │   ├── PaymentService.java
│   │   │           │   ├── TrainService.java
│   │   │           │   └── UserService.java
│   │   │           │
│   │   │           └── RailwayManagementApplication.java
│   │   │
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/
│   │       │       ├── V1__create_tables.sql
│   │       │       └── V2__insert_seed_data.sql
│   │       │
│   │       ├── templates/
│   │       │   ├── admin/
│   │       │   │   ├── train-form.html
│   │       │   │   └── trains.html
│   │       │   │
│   │       │   ├── access-denied.html
│   │       │   ├── admin-dashboard.html
│   │       │   ├── booking-details.html
│   │       │   ├── booking-form.html
│   │       │   ├── bookings.html
│   │       │   ├── dashboard.html
│   │       │   ├── error.html
│   │       │   ├── layout.html
│   │       │   ├── login.html
│   │       │   ├── payment.html
│   │       │   ├── register.html
│   │       │   └── trains.html
│   │       │
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── RailwayManagement/
│
├── .gitattributes
├── .gitignore
├── database_schema.sql
├── mvnw
├── mvnw.cmd
├── pom.xml
├── PROJECT_STRUCTURE.md
└── README.md
```

## Package Details

### 1. Config Package (`com.RailwayManagement.config`)
Contains application configuration classes.

- **SecurityConfig.java**: Spring Security configuration with authentication, authorization, and password encoding

### 2. Controller Package (`com.RailwayManagement.controller`)
Contains Spring MVC controllers for handling HTTP requests.

- **AuthController.java**: Handles login, registration, and logout
- **BookingController.java**: Manages booking operations (create, view, cancel)
- **DashboardController.java**: Handles user and admin dashboards
- **PaymentController.java**: Processes payment transactions
- **TrainController.java**: Manages train operations (CRUD for admin, search for users)

### 3. Entity Package (`com.RailwayManagement.entity`)
Contains JPA entity classes mapped to database tables.

- **User.java**: User entity with roles (ADMIN/USER)
- **Train.java**: Train entity with schedule and fare information
- **Booking.java**: Booking entity with status tracking
- **Payment.java**: Payment entity with transaction details
- **AuditLog.java**: Audit log entity for tracking system operations

### 4. Exception Package (`com.RailwayManagement.exception`)
Contains custom exception classes.

- **ResourceNotFoundException.java**: Thrown when a resource is not found
- **BookingException.java**: Thrown for booking-related errors
- **PaymentException.java**: Thrown for payment-related errors
- **TrainNotAvailableException.java**: Thrown when train seats are not available

### 5. Repository Package (`com.RailwayManagement.repository`)
Contains Spring Data JPA repository interfaces.

- **UserRepository.java**: User data access operations
- **TrainRepository.java**: Train data access with custom queries
- **BookingRepository.java**: Booking data access with filtering
- **PaymentRepository.java**: Payment data access operations
- **AuditLogRepository.java**: Audit log data access with pagination

### 6. Service Package (`com.RailwayManagement.service`)
Contains business logic layer.

- **UserService.java**: User management (registration, authentication)
- **TrainService.java**: Train management (CRUD, search, seat management)
- **BookingService.java**: Booking management (create, cancel, seat allocation)
- **PaymentService.java**: Payment processing and transaction management
- **AuditService.java**: Audit logging for all operations
- **CustomUserDetailsService.java**: Spring Security user details service

## Template Structure

### Public Templates
- **login.html**: User login page
- **register.html**: User registration page
- **error.html**: Global error page
- **access-denied.html**: Access denied page

### User Templates
- **dashboard.html**: User dashboard with recent bookings
- **trains.html**: Train search and listing page
- **bookings.html**: User booking history
- **booking-form.html**: Train booking form
- **booking-details.html**: Detailed booking information
- **payment.html**: Payment processing page

### Admin Templates
- **admin-dashboard.html**: Admin dashboard with statistics
- **admin/trains.html**: Train management page
- **admin/train-form.html**: Add/Edit train form

### Common Templates
- **layout.html**: Base layout template (not used in current implementation)

## Database Migration Files

Located in `src/main/resources/db/migration/`:

- **V1__create_tables.sql**: Creates all database tables and indexes
- **V2__insert_seed_data.sql**: Inserts demo users and trains

## Configuration Files

- **application.properties**: Application configuration (database, server, security)
- **pom.xml**: Maven dependencies and build configuration

## Key Features by Layer

### Presentation Layer (Controllers + Templates)
- User authentication and authorization
- Train search and booking
- Payment processing
- Admin train management
- Responsive Bootstrap UI

### Business Layer (Services)
- User registration with password encryption
- Train CRUD operations
- Booking with seat allocation
- Payment transaction management
- Comprehensive audit logging

### Data Layer (Repositories + Entities)
- JPA entity relationships
- Custom query methods
- Optimistic locking for concurrent updates
- Database indexing for performance

### Security Layer
- BCrypt password encryption
- Role-based access control
- CSRF protection
- Session management

## Technologies Used

| Layer | Technology |
|-------|-----------|
| Backend Framework | Spring Boot 3.5.7 |
| Language | Java 21 |
| Database | MySQL 8.0 |
| ORM | Hibernate/JPA |
| Security | Spring Security 6 |
| Template Engine | Thymeleaf |
| Frontend | Bootstrap 5 + Bootstrap Icons |
| Build Tool | Maven |
| Database Migration | Flyway |
| Validation | Jakarta Validation |

## Design Patterns Used

1. **MVC Pattern**: Separation of concerns (Model-View-Controller)
2. **Repository Pattern**: Data access abstraction
3. **Service Layer Pattern**: Business logic encapsulation
4. **DTO Pattern**: Data transfer between layers (implicit)
5. **Dependency Injection**: Spring IoC container
6. **Builder Pattern**: Lombok @Data annotation
7. **Singleton Pattern**: Spring beans

## Security Features

1. **Authentication**: Form-based login with Spring Security
2. **Authorization**: Role-based access control (ADMIN/USER)
3. **Password Encryption**: BCrypt hashing
4. **CSRF Protection**: Enabled for all state-changing operations
5. **Session Management**: Secure session handling
6. **SQL Injection Prevention**: JPA parameterized queries
7. **XSS Prevention**: Thymeleaf automatic escaping

## Database Relationships

```
users (1) -----> (N) bookings
trains (1) -----> (N) bookings
bookings (1) -----> (1) payments
users (1) -----> (N) audit_logs
```

## API Flow Examples

### User Booking Flow
1. User searches trains → `TrainController.searchTrains()`
2. User selects train → `BookingController.showBookingForm()`
3. User confirms booking → `BookingService.createBooking()`
4. System updates seats → `TrainService.updateSeatsAvailable()`
5. User makes payment → `PaymentService.processPayment()`
6. System logs action → `AuditService.logAction()`

### Admin Train Management Flow
1. Admin adds train → `TrainController.addTrain()`
2. Service validates → `TrainService.addTrain()`
3. Repository saves → `TrainRepository.save()`
4. System logs action → `AuditService.logAction()`

## Running the Application

See README.md for detailed setup and running instructions.

## Future Enhancements

- REST API endpoints
- Email notifications
- Real payment gateway integration
- Advanced reporting
- Mobile responsive improvements
- Seat selection UI
- Train route visualization
