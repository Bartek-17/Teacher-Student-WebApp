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

- **User (Student)**
  - View their subjects
  - View grades for subjects

- **Admin (Teacher)**
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

<img width="959" height="569" alt="Screenshot1" src="https://github.com/user-attachments/assets/d086fef5-c9ef-419b-8008-23bdbedd48f7" />

### User Dashboard

<img width="959" height="563" alt="Screenshot2" src="https://github.com/user-attachments/assets/f3f93127-c366-492b-92e8-302bace06e7f" />

### Teacher Dashboard

<img width="959" height="563" alt="Screenshot6" src="https://github.com/user-attachments/assets/91416f1e-be35-4aa8-ac69-3c299781e4dd" />


### Subject List

<img width="959" height="560" alt="Screenshot4" src="https://github.com/user-attachments/assets/fe8e0e65-ef01-4ed5-a1fe-84a881173ae5" />


### Grade Assignment Form

<img width="959" height="556" alt="Screenshot8" src="https://github.com/user-attachments/assets/60222216-a054-450a-aca4-42117bf41947" />


