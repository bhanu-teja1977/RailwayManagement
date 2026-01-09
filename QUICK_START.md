# Railway Management System - Quick Start Guide

## Prerequisites Checklist

- [ ] Java 21 installed (`java -version`)
- [ ] Maven installed (`mvn -version`)
- [ ] MySQL 8.0 running
- [ ] MySQL root password is `Password_123`

## Setup Steps (5 minutes)

### Step 1: Create Database

Open MySQL command line or MySQL Workbench and run:

```sql
CREATE DATABASE railway_management;
```

### Step 2: Navigate to Project Directory

```bash
cd "d:\G Swaroop Teja\Java\RailwayManagement"
```

### Step 3: Run the Application

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

### Step 4: Access the Application

Open your browser and go to:
```
http://localhost:8080
```

## Login Credentials

### Admin Account
- **Email:** admin@railway.com
- **Password:** admin123
- **Access:** Full system management

### User Account
- **Email:** user@railway.com
- **Password:** user123
- **Access:** Book trains, view bookings

## What You Can Do

### As a User:
1. ✅ Register new account
2. ✅ Search trains by route and date
3. ✅ Book train tickets
4. ✅ Make payments
5. ✅ View booking history
6. ✅ Cancel bookings

### As an Admin:
1. ✅ View admin dashboard
2. ✅ Add new trains
3. ✅ Edit train details
4. ✅ Delete trains
5. ✅ View all bookings
6. ✅ Monitor system activity

## Sample Trains Available

| Train Number | Route | Departure | Fare |
|--------------|-------|-----------|------|
| 12345 | Hyderabad → Chennai | 08:00 AM | ₹850 |
| 67890 | Chennai → Bangalore | 10:30 AM | ₹650 |
| 11111 | Bangalore → Mumbai | 08:00 PM | ₹1200 |
| 22222 | Delhi → Kolkata | 04:00 PM | ₹1500 |
| 33333 | Mumbai → Pune | 06:00 AM | ₹450 |

## Troubleshooting

### Issue: Database connection failed
**Solution:** 
- Check if MySQL is running
- Verify credentials in `src/main/resources/application.properties`
- Ensure database `railway_management` exists

### Issue: Port 8080 already in use
**Solution:** 
- Change port in `application.properties`: `server.port=8081`
- Or stop the application using port 8080

### Issue: Flyway migration error
**Solution:**
```sql
DROP DATABASE railway_management;
CREATE DATABASE railway_management;
```
Then restart the application.

## Project Features

✅ **Complete CRUD Operations**
✅ **Spring Security Authentication**
✅ **Role-Based Access Control**
✅ **Responsive Bootstrap UI**
✅ **Database Migration with Flyway**
✅ **Audit Logging**
✅ **Payment Processing**
✅ **Real-time Seat Management**

## Technology Stack

- Java 21
- Spring Boot 3.5.7
- MySQL 8.0
- Thymeleaf
- Bootstrap 5
- Spring Security 6
- JPA/Hibernate
- Flyway
- Maven

## Next Steps

1. Try registering a new user account
2. Search for trains between cities
3. Book a train ticket
4. Process payment
5. View your bookings
6. Login as admin and manage trains

## Support

For detailed documentation, see:
- `README.md` - Complete setup guide
- `PROJECT_STRUCTURE.md` - Project architecture
- `database_schema.sql` - Database schema

---

**Happy Coding! 🚂**
