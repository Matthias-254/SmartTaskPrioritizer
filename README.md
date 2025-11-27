# Smart Task Prioritizer
Smart Task Prioritizer is a modern Spring Boot application that manages tasks and automatically assigns priority scores based on deadlines, urgency levels, task categories, and creation date.  
Designed to demonstrate the integration of multiple Spring technologies in a single, coherent system.

## Features

### Core Features
The application allows users to create, edit, delete, and complete tasks, while each task automatically receives a priority score (1–10) based on an intelligent scoring system that considers deadline proximity, urgency (LOW, MEDIUM, HIGH), category (School, Work, Personal, Hobby), and how long the task has been open; this priority is also visually represented with color-coded badges (green, orange, red) for a clear and intuitive user experience.

### Background Processing
Scheduled job recalculates all priorities every 10 seconds to ensures that priorities stay relevant as deadlines approach.  

### Security
The project includes a secure login system built with Spring Security, featuring protected task routes that require authentication and an in-memory user account (admin : admin) for easy testing and access control.

### Session State
The application stores the user’s preferred sorting order (either high-to-low or low-to-high priority) and keeps these preferences active throughout the entire session.

### REST API
JSON endpoints:
- `GET /api/tasks` → open tasks sorted by priority  
- `GET /api/tasks/{id}` → specific task  
Includes error handling for invalid or missing IDs.

### Validation
The application uses server-side validation powered by Spring Validation, ensuring reliable and secure form processing, including deadline checks enforced with @FutureOrPresent, and provides clear, user-friendly validation messages directly within the form interface.

### Frontend
The user interface is built using Thymeleaf templates styled with Bootstrap 5, featuring flash messages for create, update, and delete actions, a dedicated “Completed Tasks” page for better task organization, and custom error pages (404 and 500) to provide a polished and user-friendly experience.

## Priority Logic (1–10 Scoring System)
Each task receives a raw score determined by multiple weighted factors, including the deadline (0–10, where closer deadlines yield higher scores), urgency (0–4, with HIGH = 4, MEDIUM = 2, and LOW = 0), category (0–3, where SCHOOL = 3, WORK = 2, and PERSONAL = 1), and task age (0–4, giving older tasks gradually increasing priority). This combined raw score is then normalized into a final 1–10 priority value.

## Tech Stack

### Backend
- Spring Boot 3.x  
- Spring MVC  
- Spring Data JPA  
- Spring Security  
- Spring Validation  
- Spring Scheduling  
- H2 Database

## Running the Application

### Start the app
You can run the code via IntelliJ

### Access
- Web UI: `http://localhost:8080/tasks`  
- Login: `http://localhost:8080/login` (default: `admin/admin`)  
- REST API: `http://localhost:8080/api/tasks`

## References Used

### Spring Boot Documentation  
https://docs.spring.io/spring-boot/docs/current/reference/html/

### Spring MVC  
https://spring.io/guides/gs/serving-web-content/

### Spring Data JPA  
https://spring.io/guides/gs/accessing-data-jpa/

### Spring Validation  
https://www.baeldung.com/spring-boot-bean-validation

### Spring Security  
https://spring.io/guides/gs/securing-web/

### Spring Scheduling  
https://www.baeldung.com/spring-scheduled-tasks

### Thymeleaf  
https://www.thymeleaf.org/documentation.html

### Bootstrap  
https://getbootstrap.com/docs/
