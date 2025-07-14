# University Management Web Application

This is a full-stack web application for managing university entities such as students, teachers, subjects, and grades. The application includes role-based access control with JSON Web Tokens (JWT) and a frontend built with Angular.

---

## 🛠 Technologies Used

### Backend
- Java + Spring Boot
- Spring Security + JWT
- PostgreSQL
- Maven

### Frontend
- Angular
- Angular Router
- CSS
- JWT for frontend auth

---

## 👤 User Roles

- **Student**
  - View their subjects
  - View grades for subjects

- **Teacher (Admin)**
  - Add new subjects
  - Assign grades to students

---

## ✨ Key Features

### Backend
- RESTful API with organized controllers
- JWT-secured endpoints via Spring Security
- PostgreSQL database integration
- Domain model includes: `Student`, `Teacher`, `Subject`, `Grade`
- Data validation and exception handling

### Frontend
- Component-based architecture
- Routing with guards
- Reactive form validation
- Services for all HTTP communication
- Search, sort, and UI interaction events

---

## 🚀 How to Run the Application

### 1. Clone the Repository

```bash
git clone https://github.com/Bartek-17/Teacher-Student-WebApp.git
cd Teacher-Student-WebApp
```

---

### 2. Run the Backend (Spring Boot)

1. Navigate to backend folder:
   ```bash
   cd Backend
   ```

2. Create PostgreSQL database (example: `university_db`).

3. Configure database in `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/university_db
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password
   jwt.secret=your_jwt_secret_key
   ```

4. Build and run the backend:
   ```bash
   ./mvnw spring-boot:run
   ```

   Or if Maven is globally installed:
   ```bash
   mvn spring-boot:run
   ```

Backend will be running at: `http://localhost:8080`

---

### 3. Run the Frontend (Angular)

1. Open a new terminal and navigate to frontend:
   ```bash
   cd Frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the Angular development server:
   ```bash
   ng serve
   ```

Frontend will be running at: `http://localhost:4200`

---

## 🖼 Screenshots

### Login Page
![Login Page](screenshots/login.png)

### Student Dashboard
![Student Dashboard](screenshots/student-dashboard.png)

### Teacher Dashboard
![Teacher Dashboard](screenshots/teacher-dashboard.png)

### Subject List
![Subject List](screenshots/subject-list.png)

### Grade Assignment Form
![Grade Form](screenshots/grade-form.png)

> Add your screenshots to the `screenshots/` folder and update the file names above as needed.

