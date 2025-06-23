# HelpDeskPro

HelpDeskPro is a Spring Boot-based help desk ticketing system designed to manage customer and agent interactions efficiently. It supports user authentication, role-based access, and JWT-secured endpoints.

## Features
- User registration (Customer/Agent roles)
- User login with JWT authentication
- Role-based access control (Admin, Customer, Agent)
- Secure password storage (BCrypt)
- RESTful API endpoints

## Technologies Used
- Java 17+
- Spring Boot
- Spring Security (JWT)
- Hibernate/JPA
- Lombok
- H2/MySQL (configurable)

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven

### Setup
1. **Clone the repository:**
   ```sh
   git clone https://github.com/DevCrewHub/helpDesk-backend
   cd HelpDeskPro
   ```
2. **Configure the database:**
   - Edit `src/main/resources/application.properties` for your DB settings (H2/MySQL).
3. **Build and run the application:**
   ```sh
   ./mvnw spring-boot:run
   # or on Windows
   mvnw.cmd spring-boot:run
   ```
4. **The app will start on** `http://localhost:8081` **(or your configured port).**

## API Usage

### Signup (Register)
- **Endpoint:** `POST /api/auth/signup`
- **Request Body:**
  ```json
  {
    "email": "user@test.com",
    "userName": "UserName",
    "fullName": "Full Name",
    "phoneNumber": "1234567890",
    "password": "password",
    "userRole": "CUSTOMER" // or "AGENT"
  }
  ```
- **Response:** `201 Created` with user info or error message.

### Login
- **Endpoint:** `POST /api/auth/login`
- **Request Body:**
  ```json
  {
    "username": "UserName",
    "password": "password"
  }
  ```
- **Response:**
  ```json
  {
    "jwt": "<token>",
    "userId": 1,
    "userRole": "CUSTOMER"
  }
  ```

### Secured Endpoints
- Use the JWT token in the `Authorization` header:
  ```
  Authorization: Bearer <token>
  ```
- Access role-based endpoints as per your user role.

## Project Structure
```
HelpDeskPro/
├── src/main/java/com/helpdesk/
│   ├── config/           # Security and JWT config
│   ├── controller/       # REST controllers
│   ├── dto/              # Data transfer objects
│   ├── entities/         # JPA entities
│   ├── enums/            # Enum types (UserRole)
│   ├── repositories/     # Spring Data JPA repositories
│   ├── services/         # Service layer
│   └── utils/            # Utility classes (JWT)
├── src/main/resources/
│   └── application.properties
└── ...
```

## Contribution Guidelines
1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request
