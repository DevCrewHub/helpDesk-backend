# HelpDeskPro

HelpDeskPro is a Spring Boot-based help desk ticketing system designed to manage customer and agent interactions efficiently. It supports user authentication, role-based access, JWT-secured endpoints, department management, and a full ticket assignment workflow.

## Features
- User registration (Customer/Agent roles)
- User login with JWT authentication
- Role-based access control (Admin, Customer, Agent)
- Secure password storage (BCrypt)
- RESTful API endpoints
- Department management by admin
- Agent signup with department selection by name
- Ticket creation by customer (pending status)
- Admin can view and assign pending tickets to agents
- Agent can view tickets assigned to them

## Technologies Used
- Java 17+
- Spring Boot
- Spring Security (JWT)
- Hibernate/JPA
- Lombok
- MySQL

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
   The application uses MySQL as its database. Configure the following properties in `src/main/resources/application.properties` for your DB settings (MySQL):
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/helpdesk_db
    spring.datasource.username=root
    spring.datasource.password=your_password
    ```
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

### Ticket Workflow

#### 1. Customer Creates a Ticket
- **Endpoint:** `POST /api/customer/tickets`
- **Headers:**
  - `Authorization: Bearer <customer-jwt-token>`
  - `Content-Type: application/json`
- **Request Body:**
  ```json
  {
    "title": "Printer not working",
    "description": "The office printer is jammed.",
    "priority": "MEDIUM",
    "dueDate": "2024-12-31T10:00:00.000Z",
    "departmentName": "IT Support"
  }
  ```
- **Response:** Ticket with status `PENDING` and no assigned agent.

#### 2. Admin Views All Tickets
- **Endpoint:** `GET /api/admin/tickets`
- **Headers:**
  - `Authorization: Bearer <admin-jwt-token>`
- **Response:** List of all tickets in the system (regardless of status).

#### 3. Admin Views Pending Tickets
- **Endpoint:** `GET /api/admin/tickets/pending`
- **Headers:**
  - `Authorization: Bearer <admin-jwt-token>`
- **Response:** List of all tickets with status `PENDING`.

#### 4. Admin Assigns Ticket to Agent
- **Endpoint:** `PUT /api/admin/tickets/{ticketId}/assign?agentId={agentId}`
- **Headers:**
  - `Authorization: Bearer <admin-jwt-token>`
- **Response:** Updated ticket with status `ASSIGNED` and assigned agent info.
- **Note:** The agent must belong to the same department as the ticket.

#### 5. Agent Views Assigned Tickets
- **Endpoint:** `GET /api/agent/tickets`
- **Headers:**
  - `Authorization: Bearer <agent-jwt-token>`
- **Response:** List of tickets assigned to the logged-in agent.

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
│   ├── enums/            # Enum types (UserRole, TicketStatus, etc.)
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
