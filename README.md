# Universidad Tecnológica Nacional Facultad Regional Santa Fe Diseño de Sistemas de Información Trabajo Práctico Sistema de Registro de Aulas. BackEnd (Business Logic Layer and Data Access Layer / Controller and Model) and FrontEnd (Presentation Layer or UI) for App 

## Overview

<img src="https://upload.wikimedia.org/wikipedia/commons/7/79/Spring_Boot.svg" alt="Spring Boot Logo" width="250"/>

This project combines **Spring Boot** and **Docker Compose** to build a backend application that integrates **PostgreSQL** and **pgAdmin4**. It also includes support for **Angular** as the frontend. This README outlines the setup, build, and deployment process for this project.

---

## Features

- **Spring Boot** for backend logic.
- **Hibernate** and **Maven** for ORM and dependency management.
- **Docker Compose** for managing containers.
- **PostgreSQL** as the database.
- **Swagger UI** for API documentation.
- **Angular** for frontend development.

---

## Requirements

1. **Docker**: [Install Docker](https://www.docker.com/get-started/).
2. **Java**: Java 11 or later.
3. **Node.js**: Version 14.15 or later.
4. **npm**: Included with Node.js (verify installation using `npm -v`).

---

## Project Structure

```plaintext
src
├── main
│   ├── java
│   │   ├── dsitp.backend.project
│   │   │   ├── config
│   │   │   ├── controller
│   │   │   ├── domain
│   │   │   ├── integration
│   │   │   ├── mapper
│   │   │   ├── model
│   │   │   ├── repos
│   │   │   ├── rest
│   │   │   ├── service
│   │   │   ├── util
│   │   │   └── validation
│   │   └── ProjectApplication.java
│   ├── resources
│   │   ├── application.yml
│   │   └── docker-compose.yml
├── test
└── pom.xml
```

- **Controller**: REST endpoints using `@RestController`.
- **Domain**: Basic entities.
- **Model**: DTOs (Data Transfer Objects).
- **Repos**: DAO (Data Access Object) layer.
- **Service**: Business logic.
- **Util**: Utility classes.
- **Validation**: Input validation logic.

---

## Configuration Files

- `src/main/resources/application.yml`
- `pom.xml`
- `docker-compose.yml`

---

## Setup Instructions

### Import the Java Project

1. **Download the ZIP** from GitHub (Code -> Download ZIP).

#### In NetBeans:
- Navigate to: `File -> Import Project -> From ZIP`.

#### In IntelliJ IDEA:
- Navigate to: `File -> Open`, then select the ZIP file.

---

### Start Backend Application

1. **Build the application**:
   ```bash
   ./mvnw clean package
   ```

2. **Run with Docker Compose**:
   ```bash
   docker compose up
   ```
   _Tip_: To avoid issues, run `docker compose down` before restarting.

3. **Run Spring Boot Application**:
   ```bash
   ./mvnw spring-boot:run
   ```
   _or_
   ```bash
   java -Dspring.profiles.active=production -jar ./target/project-0.0.1-SNAPSHOT.jar
   ```

---

### Frontend Setup

<img src="https://angular.io/assets/images/logos/angular/angular.svg" alt="Angular Logo" width="250"/>

1. **Install Angular CLI**:
   ```bash
   npm install -g @angular/cli
   ```

2. **Install Modules**:
   From the project root, run:
   ```bash
   npm install
   ```

3. **Start the Angular Application**:
   ```bash
   ng serve
   ```
   _To use a custom port:_
   ```bash
   ng serve --port <port-number>
   ```

---

## Development Tips

- Use the `local` profile during development. Configure it by adding:
  ```bash
  -Dspring.profiles.active=local
  ```
  in your Run Configuration (VM Options).

- Create an `application-local.yml` file to override default settings for development.

---

## PostgreSQL Notes

<img src="https://upload.wikimedia.org/wikipedia/commons/2/29/Postgresql_elephant.svg" alt="PostgreSQL Logo" width="250"/>

- Use `ON DELETE CASCADE` to enable cascading deletions in relationships.
- Access PostgreSQL from the command line:
  ```bash
  psql -h localhost -p 5432 -U postgres -d project
  ```

---

## API Documentation

Access the Swagger UI for interactive API exploration:
[SwaggerUI](http://localhost:8080/swagger-ui/index.html#)

---

## App Ports

- **pgAdmin4**: [http://localhost:8081/](http://localhost:8081/)
- **Angular Frontend**: [http://localhost:4200/](http://localhost:4200/)

---

## Class Diagrams

![Actual Class Diagram](https://via.placeholder.com/600x400.png?text=Class+Diagram+Placeholder)
![Basic Class Diagram draw.io](https://github.com/user-attachments/assets/1b14c28b-6f66-4e96-966d-ba7008c1bef2)

---

## Useful Links

- [Maven Documentation](https://maven.apache.org/guides/index.html)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/reference/jpa.html)

---

## Build Docker Image

Create a production-ready Docker image:
```bash
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=isi.deso/tp-etapa-8-implementacion
```

---

## Mocking Data

When starting the app, Hibernate will automatically create tables and mock data. To prevent repeated mocking, set:
```properties
spring.sql.init.mode=never
```
in `application.properties`.

---

## Further Reading

- [Angular CLI](https://github.com/angular/angular-cli)
- [Node.js](https://nodejs.org/en/download/package-manager)
