# 🧮 SCCON – People Management

Java application **Spring Boot 3 / Java 17** following **Clean Architecture**, with REST endpoints for **CRUD of people**, **age calculation**, and **salary calculation**. Includes global exception handling, custom converters, and unit tests.

> **This README** contains **Mermaid** diagrams (render in GitHub and VS Code with the Mermaid extension).

---

## 📚 Table of Contents

- [Architecture](#-architecture)
- [Main Flows](#-main-flows)
- [Endpoints](#-endpoints)
- [Running](#-running)
- [Testing](#-testing)
- [Error Handling](#-error-handling)
- [Key Configurations](#-key-configurations)
- [Roadmap](#-roadmap)

---

## 🏗 Architecture

```
src/
 ├── main/
 │   ├── java/com/sccon/
 │   │   ├── domain/          # Entities, Enums, contracts (ports), services (business rules)
 │   │   ├── infrastructure/  # Technical adapters (repo, config, exception)
 │   │   └── presentation/    # REST Controllers, DTOs, converters
 │   └── resources/           # application.properties, OpenAPI
 └── test/                    # Unit and integration tests
```

---

## 🔄 Main Flows

### 1) **GET /person/{id}/salary?output=full**

- Retrieves salary based on hire date, applying base salary and increments.

### 2) **Salary Calculation**

- Base = R\$ 1558.00
- Every year: +18% + R\$ 500
- Rounds up to 2 decimal places
- `min` output divides result by minimum wage (R\$ 1302.00 – Feb/2023)

### 3) **Error Handling (Binding/Conversion)**

- Invalid parameter values throw `MethodArgumentTypeMismatchException` or `InvalidOutputException`, handled globally returning HTTP 400.

---

## 🔗 Endpoints

* Postman: directory: postman/postman_collection.json 

| Method | Endpoint                                      | Description          |                  |               |
|--------|-----------------------------------------------|----------------------| ---------------- |---------------|
| GET    | `/person`                                     | List all people      |
| GET    | `/person/{id}`                                | Get by ID            |
| POST   | `/person`                                     | Create person        |
| PUT    | `/person/{id}`                                | Update person        |
| PATCH  | `/person/{id}?salary=`                        | Update person salary |
| DELETE | `/person/{id}`                                | Delete person        |
| GET    | `/person/{id}/age?output={days,months,years}` | Calculate age        |
| GET    | `/person/{id}/salary?output={full, min}`      | Calculate salary     |

---

## ▶ Running

### Maven

```bash
mvn spring-boot:run
```

### Docker

```bash
mvn clean package
docker build -t sccon-people-management .
docker run -p 8080:8080 sccon-people-management
```

### Database (In-Memory)

```properties
> In-memory map for challenge execution.
```


---

## 🧪 Testing

- **JUnit 5** and **Mockito** and **ArchUnit**
- Coverage includes:
  - Service
  - Output converter
  - Controller (MockMvc)
  - Repository
  - Salary and age calculation edge cases
  - Architecture

```bash
mvn test
```

---

## 🛡 Error Handling

Example 400 Bad Request payload:

```json
{
  "timestamp": "2025-08-09T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid value for output. Accepted values: min | full",
  "path": "/person/1/salary"
}
```

---

### 👤 Author

**Fernando Marchesano Lerma** — Java/Spring Boot, Clean Architecture, testing, and best practices.

