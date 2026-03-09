# 🚀 Reddit Clone Backend

An **enterprise-grade social media backend** engineered with **Spring Boot 3.x** and **Java 8+**.  
This project demonstrates a **scalable, secure architecture** designed for high-concurrency environments, featuring **asymmetric authentication and modern backend best practices**.

---

## 🛠️ Tech Stack & Tools

| Category | Technologies |
|--------|-------------|
| **Core Framework** | Java 8+, Spring Boot 3.x, Spring MVC |
| **Security** | Spring Security, OAuth2, JWT (Asymmetric RSA Encryption) |
| **Persistence** | Spring Data JPA, Hibernate, MySQL |
| **Infrastructure** | Docker, Maven |
| **Development Workflow** | Agile/Scrum, JIRA |
| **Email Testing** | Mailtrap |
| **API Documentation** | Swagger UI / OpenAPI 3 |

---

# ✨ Key Features

### 🔐 Secure Asymmetric Authentication
- User authentication powered by **JWT with RSA (RS256)** encryption.
- Private key used for signing tokens and public key for verification.

### 👥 Community Management
- Create and manage **Subreddits (communities)**.
- Define custom rules and metadata.

### 📝 Content Engine
- Create posts and threaded comments.
- Implement **Upvote/Downvote logic**.

### 📧 Email Verification
- Account activation via **Spring Mail**.
- Email testing using **Mailtrap SMTP**.

### 📚 API Documentation
- All APIs documented using **Swagger UI / OpenAPI 3**.

---

# 🔐 JWT Key Generation

This project uses **RS256 (Asymmetric RSA)** for signing and verifying tokens.

You must generate a **key pair** before running the application.

### Generate Key using Java Keytool

Run the following command to create a `.jks` file:

```bash
keytool -genkeypair \
-alias redditclone \
-keyalg RSA \
-keystore src/main/resources/redditclone.jks \
-keysize 2048
```

⚠️ **Important:**  
Ensure that the **alias and password match the configuration in `application.properties`.**

---

# 🚀 Build and Run

## 1️⃣ Database Setup

Create a MySQL database:

```sql
CREATE DATABASE reddit_clone;
```

Update the database credentials in:

```
src/main/resources/application.properties
```

Example configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/reddit_clone
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

---

## 2️⃣ Build the Project

Compile and package the project using Maven:

```bash
mvn clean package -DskipTests
```

---

## 3️⃣ Run the Application

### Option 1 — Run with Maven

```bash
mvn spring-boot:run
```

### Option 2 — Run Executable JAR

```bash
java -jar target/reddit-clone-backend-0.0.1-SNAPSHOT.jar
```

---

# 📋 API Exploration

Once the application is running, open Swagger UI to explore all APIs:

```
http://localhost:8080/swagger-ui/index.html
```

Swagger provides:
- Endpoint documentation
- Request/response schema
- Interactive API testing

---

# 🏗️ Architectural Decisions

### 🔒 Security First
- Implemented **Asymmetric JWT Authentication**.
- Private key isolated for token signing.
- Public key used for verification.

### ⚙️ Scalable Backend Design
- Built with **Spring Boot Microservice-ready architecture**.
- Clean separation of layers: **Controller → Service → Repository**.

### 🧑‍💻 Agile Development
- Developed using **Agile/Scrum methodology**.
- Tasks and workflow tracked via **JIRA**.

---

# 📦 Future Enhancements

- Redis caching
- Kafka event-driven architecture
- Kubernetes deployment
- Rate limiting
- Notification system
