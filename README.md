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
- Agent can update ticket status (ASSIGNED → INPROGRESS → RESOLVED)
- Customer can update ticket status (any status → CLOSED, RESOLVED → ASSIGNED)
- Customer can update the priority of their open tickets

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

#### 6. Agent Updates Ticket Status
- **Endpoint:** `PUT /api/agent/tickets/{ticketId}/status?status={newStatus}`
- **Headers:**
  - `Authorization: Bearer <agent-jwt-token>`
- **Valid Status Values:** `INPROGRESS`, `RESOLVED`
- **Status Transitions:**
  - `ASSIGNED` → `INPROGRESS`
  - `INPROGRESS` → `RESOLVED`
  - `ASSIGNED` → `RESOLVED` (direct)

#### 7. Customer Updates Ticket Status
- **Endpoint:** `PUT /api/customer/tickets/{ticketId}/status?status={newStatus}`
- **Headers:**
  - `Authorization: Bearer <customer-jwt-token>`
- **Valid Status Values:** `CLOSED`, `ASSIGNED`
- **Status Transitions:**
  - Any status → `CLOSED` (customer can close anytime)
  - `RESOLVED` → `ASSIGNED` (only when ticket is resolved)

#### 8. Customer Views Created Tickets
- **Endpoint:** `GET /api/customer/ticketsCreated`
- **Headers:**
  - `Authorization: Bearer <customer-jwt-token>`
- **Response:** List of tickets created by the logged-in customer.

#### 9. Customer Updates Ticket Priority
- **Endpoint:** `PUT /api/customer/tickets/{ticketId}/priority?priority={newPriority}`
- **Headers:**
  - `Authorization: Bearer <customer-jwt-token>`
- **Valid Priority Values:** `LOW`, `MEDIUM`, `HIGH`
- **Note:** The ticket status must not be `RESOLVED` or `CLOSED`.

#### 10. Ticket Search by Title
- **Agent:**
  - **Endpoint:** `GET /api/agent/tickets/search/{title}`
  - **Headers:**
    - `Authorization: Bearer <agent-jwt-token>`
  - **Description:** Returns only the tickets assigned to the logged-in agent that contain the given title (case-insensitive, partial match).
  - **Example:**
    ```
    GET /api/agent/tickets/search/printer
    ```
- **Customer:**
  - **Endpoint:** `GET /api/customer/tickets/search/{title}`
  - **Headers:**
    - `Authorization: Bearer <customer-jwt-token>`
  - **Description:** Returns only the tickets created by the logged-in customer that contain the given title (case-insensitive, partial match).
  - **Example:**
    ```
    GET /api/customer/tickets/search/printer
    ```
- **Admin:**
  - **Endpoint:** `GET /api/admin/tickets/search/{title}`
  - **Headers:**
    - `Authorization: Bearer <admin-jwt-token>`
  - **Description:** Returns all tickets in the system that contain the given title (case-insensitive, partial match).
  - **Example:**
    ```
    GET /api/admin/tickets/search/printer
    ```

#### 11. Get Ticket by ID
- **Customer:**
  - **Endpoint:** `GET /api/customer/ticket/{id}`
  - **Headers:**
    - `Authorization: Bearer <customer-jwt-token>`
  - **Description:** Returns the ticket with the given ID if it was created by the logged-in customer. Returns 404 if not found or not accessible.
  - **Response Example:**
    ```json
    {
      "id": 1,
      "title": "Printer not working",
      "description": "The office printer is jammed.",
      "priority": "MEDIUM",
      "dueDate": "2024-12-31T10:00:00.000Z",
      "ticketStatus": "PENDING",
      "assignedAgent": { ... },
      "department": { ... }
    }
    ```
- **Agent:**
  - **Endpoint:** `GET /api/agent/ticket/{id}`
  - **Headers:**
    - `Authorization: Bearer <agent-jwt-token>`
  - **Description:** Returns the ticket with the given ID if it is assigned to the logged-in agent. Returns 404 if not found or not accessible.
- **Admin:**
  - **Endpoint:** `GET /api/admin/ticket/{id}`
  - **Headers:**
    - `Authorization: Bearer <admin-jwt-token>`
  - **Description:** Returns the ticket with the given ID if it exists in the system. Returns 404 if not found.

### Complete Ticket Status Flow
```
PENDING → ASSIGNED → INPROGRESS → RESOLVED → CLOSED
   ↓         ↓           ↓           ↓         ↓
Customer   Admin      Agent      Agent     Customer
Creates   Assigns    Starts     Completes  Closes
Ticket    to Agent   Working    Work       Ticket
```

### Secured Endpoints
- Use the JWT token in the `Authorization` header:
  ```
  Authorization: Bearer <token>
  ```
- Access role-based endpoints as per your user role.

### Admin Ticket Filtering Features

Admins can filter tickets using the following endpoints:

#### Filter Tickets by Priority
- **Endpoint:** `GET /api/admin/tickets/priority/{priority}`
- **Headers:**
  - `Authorization: Bearer <admin-jwt-token>`
- **Path Variable:**
  - `{priority}`: `LOW`, `MEDIUM`, or `HIGH`
- **Response:** List of tickets with the specified priority.

#### Filter Tickets by Status
- **Endpoint:** `GET /api/admin/tickets/status/{status}`
- **Headers:**
  - `Authorization: Bearer <admin-jwt-token>`
- **Path Variable:**
  - `{status}`: `PENDING`, `ASSIGNED`, `INPROGRESS`, `RESOLVED`, or `CLOSED`
- **Response:** List of tickets with the specified status.

#### Filter Tickets by Department Name
- **Endpoint:** `GET /api/admin/tickets/department/{name}`
- **Headers:**
  - `Authorization: Bearer <admin-jwt-token>`
- **Path Variable:**
  - `{name}`: Name of the department (e.g., `IT Support`)
- **Response:** List of tickets belonging to the specified department.

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
