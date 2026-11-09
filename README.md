# Student Registration and Management System

A web application built with Spring MVC, Thymeleaf, Java and PostgreSQL. Users register students through an HTML form. The data is saved in PostgreSQL using JDBC, and all registered students are shown on a separate page.

## Technologies Used

- Java 17
- Spring Boot 4.1.2 (Spring MVC)
- Thymeleaf
- JDBC (PreparedStatement, no JPA or Hibernate)
- PostgreSQL
- Maven

## Project Structure

```
src/main/java/org/example/studentmanagement
├── StudentManagementApplication.java
├── config
│   └── DatabaseConfig.java
├── controller
│   └── StudentController.java
├── model
│   └── Student.java
├── repository
│   └── StudentRepository.java
└── service
    └── StudentService.java

src/main/resources
├── application.properties
└── templates
    ├── register.html
    └── students.html
```

## Database Setup

1. Create the database in pgAdmin:

```sql
CREATE DATABASE student_management;
```

2. Connect to `student_management` and create the table:

```sql
CREATE TABLE students (
    id        SERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    age       INTEGER NOT NULL CHECK (age > 18),
    email     VARCHAR(150) NOT NULL UNIQUE
);
```

The form collects first name and last name separately. The repository joins them into `full_name` before saving.

## How to Run the Application

1. Install Java 17 or newer and PostgreSQL.
2. Create the database and table (see above).
3. Set these environment variables (the password is never stored in the code):
    - `DB_USERNAME` = your PostgreSQL username
    - `DB_PASSWORD` = your PostgreSQL password
4. Run `StudentManagementApplication` in IntelliJ, or run `mvn spring-boot:run`.
5. Open `http://localhost:8081/student/register` in a browser.

## IoC, Bean Creation and Dependency Injection

**Inversion of Control (IoC):** In this project I do not create the main objects myself with `new`. Spring's IoC container creates them and manages them.

**Bean creation:** When the application starts, Spring scans the project and finds classes marked with `@Controller`, `@Service` and `@Repository`. It creates one object (a bean) for each of them. The `DataSource` bean is created in `DatabaseConfig` using `@Configuration` and `@Bean`.

**Dependency Injection:** Each class needs the layer below it, and Spring passes it in through the constructor:

- `StudentRepository` needs a `DataSource`. It is created as a bean in `DatabaseConfig`, with the values read from `application.properties`.
- `StudentService` needs a `StudentRepository`.
- `StudentController` needs a `StudentService`.

Spring builds them in that order and connects them. The controller and service never use `new` to create the objects they depend on.

The request flow is: Browser, then Controller, then Service, then Repository, then PostgreSQL.

## Controller Handler Mappings

`StudentController` uses `@RequestMapping("/student")` as the common start of every URL.

| HTTP Method | Endpoint | What it does | View returned |
|---|---|---|---|
| GET | `/student/register` | Shows the empty registration form | `register.html` |
| POST | `/student/register` | Receives the form with `@ModelAttribute`, validates it, saves the student | `register.html` |
| GET | `/student/all` | Gets all students and passes them to the page with `Model` | `students.html` |

`@GetMapping` and `@PostMapping` choose the method by the HTTP method. This lets `/student/register` show the form with GET and save it with POST. One controller returns different views depending on the request.

## Screenshots

### Registration page
![Registration page](screenshots/register.png)

### Student list
![Student list](screenshots/students.png)

### Saved records in the database
![Database records](screenshots/database.png)