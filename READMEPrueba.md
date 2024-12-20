# Creando el archivo markdown con los estilos HTML embebidos

readme_content = """
# <span style="color: #0056b3;">Universidad Tecnológica Nacional</span> 
<span style="color: #0056b3;">Facultad Regional Santa Fe</span>  
**Diseño de Sistemas de Información**  
**Trabajo Práctico**  
### <span style="color: #ff6347;">Sistema de Registro de Aulas</span>  
_BackEnd (Business Logic Layer and Data Access Layer / Controller and Model)_ and FrontEnd (Presentation Layer or UI) for App  

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
