# HelpDeskPro

HelpDeskPro is a Spring Boot-based help desk ticketing system designed to manage customer and agent interactions efficiently. It supports user authentication, role-based access, JWT-secured endpoints, and department management.

## Features
- User registration (Customer/Agent roles)
- User login with JWT authentication
- Role-based access control (Admin, Customer, Agent)
- Secure password storage (BCrypt)
- RESTful API endpoints
- **Department management by admin**
- **Agent signup with department selection by name**

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
- **Request Body for Customer:**
  ```json
  {
    "email": "user@test.com",
    "userName": "UserName",
    "fullName": "Full Name",
    "phoneNumber": "1234567890",
    "password": "password",
    "userRole": "CUSTOMER"
  }
  ```
- **Request Body for Agent (with departmentName):**
  ```json
  {
    "email": "agent@test.com",
    "userName": "agent1",
    "fullName": "Agent One",
    "phoneNumber": "1234567890",
    "password": "agentpass",
    "userRole": "AGENT",
    "departmentName": "IT Support"
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

### Department Management (Admin Only)

#### Create Department
- **Endpoint:** `POST /api/admin/departments`
- **Headers:**
  - `Authorization: Bearer <admin-jwt-token>`
  - `Content-Type: application/json`
- **Request Body:**
  ```json
  {
    "name": "IT Support"
  }
  ```
- **Response:**
  ```json
  {
    "id": 1,
    "name": "IT Support"
  }
  ```

#### Get All Departments
- **Endpoint:** `GET /api/admin/departments`
- **Headers:**
  - `Authorization: Bearer <admin-jwt-token>`

#### Update Department
- **Endpoint:** `PUT /api/admin/departments/{id}`
- **Headers:**
  - `Authorization: Bearer <admin-jwt-token>`
- **Request Body:**
  ```json
  {
    "name": "New Department Name"
  }
  ```

#### Delete Department
- **Endpoint:** `DELETE /api/admin/departments/{id}`
- **Headers:**
  - `Authorization: Bearer <admin-jwt-token>`

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
