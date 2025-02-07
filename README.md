# SpringForge - Spring Boot Project Starter

## 📌 Introduction

**SpringForge** is a Spring Boot starter template that provides a well-structured foundation for building secure and
scalable applications. It follows best practices for authentication, exception handling, and environment management.

### 🔥 Features

- **JWT-based Authentication** (Login & Secure APIs)
- **Centralized Exception Handling**
- **Layered Architecture** (Controller, Service, Repository)
- **Environment-based Configuration Management**
- **Integrated Logging & Monitoring (SLF4J + Logback)**
- **Modular and Scalable Codebase**

---

## 🚀 Project Setup

### **Prerequisites**

Make sure you have the following installed before setting up the project:

- **JDK 17+** (Recommended)
- **Maven 3.8+**
- **PostgreSQL/MySQL** (or any supported database)
- **Git**
- **IDE (IntelliJ IDEA, VS Code, Eclipse, etc.)**

### **Clone the Repository**

```sh
git clone https://github.com/your-repo/springforge.git
cd springforge
```

### **Database**

We are using h2 database. In future we are give the multiple database support.

# Jasypt Encryption and Decryption Guide for SpringForge

## Introduction

Jasypt (Java Simplified Encryption) is a library that allows you to easily encrypt and decrypt sensitive data, such as
passwords, for your Java applications. In this guide, we'll show you how to encrypt and decrypt values, such as
passwords, using the Jasypt plugin with Maven.

## Encrypting a Value

To encrypt a value (e.g., a password), you can use the following Maven command:

```bash
mvn jasypt:encrypt-value -Djasypt.encryptor.password="key-password" -Djasypt.plugin.value="password"
```

- **-Djasypt.encryptor.password="key-password":** The password used to encrypt the value. You should replace "
  springforge" with your secret password.
- **-Djasypt.plugin.value="password"**: The value you want to encrypt. In this case, "sa" represents the plain password
  or value you wish to encrypt.
  After running the command, you will receive the encrypted value, which can be used in your application securely.

Example of an encrypted value:

```bash
ENC(z8hD0b+9epGXZIp0RZ0Fy8Cn1f8akJ6Q5sdQHlB0EuHwSKuqyEM8/p0kUBp38ieO)
```

## Decrypting a Value

To decrypt an encrypted value, use the following Maven command:

```bash
mvn jasypt:decrypt-value -Djasypt.encryptor.password="springforge" -Djasypt.plugin.value="z8hD0b+9epGXZIp0RZ0Fy8Cn1f8akJ6Q5sdQHlB0EuHwSKuqyEM8/p0kUBp38ieO"
```

- **-Djasypt.encryptor.password="password"**: The password that was used to encrypt the value. You must use the same
  password for decryption.
- **-Djasypt.plugin.value="encryptor value"**: The encrypted value that you want to decrypt.

### Disable Devtools in Production
Ensure Devtools is only active in development by adding this in `application.properties` or `application.yml`:
```
spring:
  devtools:
    restart:
      enabled: true
    livereload:
      enabled: true
```

### **Build project command**

```bash
mvn clean install -Dspring.profiles.active=<environment> -Djasypt.encryptor.password=<password>
```
**Replace** <environment> with the desired Spring profile (**e.g.**, ```dev```, ```uat```, ```prod```) and <password> with the Jasypt encryption password.

The application will start on `http://localhost:8080`.

---

## 🛠 API Authentication (JWT-Based Login)

### **User Login**

**Endpoint:** `POST /api/auth/login`

**Request Body:**

```json
{
  "username": "springforge",
  "password": "Springforge@123"
}
```

**Response:**

```json
{
  "status": "success",
  "message": "Login successfully!",
  "data": {
    "username": "springforge",
    "email": "springforge@test.com",
    "roleEntities": [
      "ROLE_ADMIN"
    ],
    "type": "Bearer",
    "token": "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyYXZpbmRlciIsImlhdCI6MTczODU3NzQ0MCwiZXhwIjoxNzM4NTc3NTYwfQ.e-Kq3f9AH9Ymh_Q8N9F2sr2_LWYfs3C2Gi9q8oIAo_lc5UUjM3QpZY1lU93NcXa4stix0wJgzpRU7vr3XuTMNLLyIm1rh6dmtYo_75Mo1SKPv0k1CqvbTiP4oRpHsqI-tr9l5_r_iT566_RKLS9pICLKF-hcY9Yv8WyjF6nBntpypvTbz2x7pEtgbcnb-14J6Zh9ksY4wFmmeiIuAW2fyALOElC3UmnmmmdW7A9vW6wz69d_Zdckx3WF9dpq5DDCkRJOCUQ7y53MFQ7LOaw0PzVfLTy4qe8zuR1l9SIjc-WVGAtjbUIHlmZTZgLlHR5T7S-yEb_5-9K38nD-6S-nRw"
  }
}
```

Use this token in the `Authorization` header for protected endpoints:

```http
Authorization: Bearer <your_token>
```

---

## ⚠️ Exception Handling Format

All API errors return a structured JSON response.

**Example Response:**

```json
{
  "timestamp": 1738577400,
  "status": 401,
  "error": "Unauthorized",
  "exception": "org.springframework.security.authentication.BadCredentialsException",
  "message": "Bad credentials",
  "path": "/api/auth/signin"
}
```

---

## 📂 Project Structure

```
springforge/
│── src/
│   ├── main/
│   │   ├── java/com/example/springforge/
│   │   │   ├── config/         # Configuration files (CORS, Properties)
│   │   │   ├── constant/       # Constant
│   │   │   ├── controller/     # REST controllers
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── entity/         # JPA Entities
│   │   │   ├── exception/      # Centralized Exception Handling
│   │   │   ├── repository/     # Database Repositories
│   │   │   ├── security/       # JWT Security Implementation
│   │   │   ├── service/        # Business Logic Services
│   │   │   ├── util/           # Utility Classes
│   │   │   ├── SpringForgeApplication.java # Main Entry Point
│   │   ├── resources/
│   │   │   ├── application.yml  # Configuration Properties
│── test/                        # Unit & Integration Tests
│── pom.xml                       # Maven Build Configuration
│── README.md                     # Documentation
```

---

## 🛠 Built With

- **Spring Boot** - Backend Framework
- **Spring Security + JWT** - Authentication & Authorization
- **JPA + Hibernate** - ORM for Database Operations
- **SLF4J + Logback** - Logging & Monitoring
- **Maven** - Build Tool

---

# 🐳 Docker Setup for SpringForge

## Build the Docker Image

To build your Docker image, make sure you have the `Dockerfile` in the root of your project and then run the following
command:

```bash
docker build -t springforge .
```

## This will create a Docker image with the name springforge.

Run the Application with Docker
To run the SpringForge application inside a Docker container, use the following command:

```bash 
docker run -p 8080:8080 -e SPRING_PROFILE=dev -e JASYPT_PASSWORD=springforge springforge
```

This will expose your Spring Boot application on http://localhost:8080.

## Docker Compose Setup

If you want to manage your container using Docker Compose, you can use the following docker-compose.yml file.

```bash
version: '3.8'
services:
app:
image: springforge # Replace with your Docker image name
build: . # Optional, if you want to build the image from the Dockerfile
ports:

- "8080:8080"
  environment:
  SPRING_PROFILE: dev # Set your profile (dev, uat, prod)
  JASYPT_PASSWORD: springforge # Set your Jasypt password here
  restart: always # Automatically restart the container if it stops
```

## Steps to Run with Docker Compose

- Create the docker-compose.yml file in the root of your project.

- Run the following command to build and start the services:

```bash 
docker-compose up --build -d
```

This will:

- Build the Docker image (if build: . is included) or use the pre-built image (springforge).
- Expose your application on port 8080.
- Pass the environment variables (SPRING_PROFILE and JASYPT_PASSWORD) to your container.
  To stop and remove the containers:

```bash
docker-compose down
```

## 🎯 Future Enhancements

- Implement the refresh token
- Multiple database support
- Forget password with email verification
- After five wrong username & password block userEntity and unblock with email verification.
- Manage login session and logout from all devices allow multiple device login.
- Email verification for users

---

## 🤝 Contributing

We welcome contributions! Feel free to open an issue or submit a pull request.

---

## 📞 Contact

For any issues or feature requests, please open an issue in the GitHub repository.

Happy coding! 🚀

