# Universidad Tecnológica Nacional Facultad Regional Santa Fe Diseño de Sistemas de Información Trabajo Práctico Sistema de Registro de Aulas. 

# BackEnd (Business Logic Layer and Data Access Layer / Controller and Model) and FrontEnd (Presentation Layer or UI) for App 

# Index

1. [Documentation Diagrams from draw.io](#documentation-diagrams).
2. [Overview](#overview).
3. [Features](#features).
4. [Requirements](#requirements)
5. [Project Structure](#project-structure).
6. [Configuration Files](#configuration-files).
7. [Setup Instructions](#setup-instructions).
   1. [Import the Java Project](#import-the-java-project).
   2. [Start Backend Application](#start-backend-application).
8. [Frontend Setup](#frontend-setup).
9. [Development Tips](#development-tips).
10. [PostgreSQL Notes](#postgresql-notes).
11. [API Documentation](#api-documentation).
12. [App Ports](#app-ports).
13. [Class Diagrams](#class-diagrams).
14. [Useful Links](#useful-links).
15. [Build Docker Image](#build-docker-image).
16. [Mocking Data](#mocking-data).
17. [Further Reading](#further-reading).

## Documentation Diagrams from draw.io

### Etapa 3: Diagrama de Clases de Diseño Refinado

[Take a look](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&layers=1&nav=1&title=Etapa%203%3A%20Diagrama%20de%20Clases%20de%20Dise%C3%B1o%20Refinado.drawio#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1YVADOm4JgJKi42wlq5s7f9I0kilPRx8x%26export%3Ddownload).

### Etapa 4: Diagramas Entidad-Relación

[Take a look](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&layers=1&nav=1&title=Etapa%204%3A%20Diagramas%20Entidad-Relaci%C3%B3n.drawio#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1W3XM6aZN4Pi9KA2UBDgkHQ24t9sd53c9%26export%3Ddownload).
You can switch between diagrams.

### Etapa 5: Diagramas de Secuencia del CU13, CU14, CU15 y CU16

[Take a look](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&layers=1&nav=1&title=Etapa%205%3A%20Diagramas%20de%20Secuencia%20del%20CU13%2C%20CU14%2C%20CU15%20y%20CU16.drawio#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1A7j7BY0LKcJxpTA1UvUemNCPsQpTxded%26export%3Ddownload).
You can switch between diagrams.

### Etapa 6: Diagramas de Secuencia del CU01 y CU05

[Take a look](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&layers=1&nav=1&title=Etapa%206%3A%20Diagramas%20de%20Secuencia.drawio#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1Rdd4bfmVAIRpdIXVgMuDPwdoYZeVGevu%26export%3Ddownload).
You can switch between diagrams.

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
[SwaggerUI](http://localhost:8080/swagger-ui/index.html#).

---

## App Ports

- **pgAdmin4**: [http://localhost:8081/](http://localhost:8081/).
- **Angular Frontend**: [http://localhost:4200/](http://localhost:4200/).

---

## Class Diagrams

![Basic Class Diagram draw.io](![Etapa 3 Diagrama de Clases de Diseño Refinado](https://github.com/user-attachments/assets/c73963d3-2fea-47b1-ae4d-cccf3dd840a7)
)
![Class Diagram](https://github.com/user-attachments/assets/2b9e9c60-d534-4c34-9fb9-bbf6e4de7aef).

---

## Useful Links

- [Maven Documentation](https://maven.apache.org/guides/index.html).
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/).
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/reference/jpa.html).

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

- [Angular CLI](https://github.com/angular/angular-cli).
- [Node.js](https://nodejs.org/en/download/package-manager).
