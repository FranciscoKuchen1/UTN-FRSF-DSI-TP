# DSITP Backend (Capa de Negocio/Logica y Capa de Acceso a Datos o Controller y Model) para App
Se usa Docker Compose (recomiendo descargar Docker Desktop, con el archivo docker-compose y el comando ```docker compose up -d``` (Ver indicaciones, en ingles) levantan una instancia de imagen de PostgreSQL) en su ```localhost:5433``` (ver en navegador ```http://localhost:5433``` o ```http://localhost:5433/project```, creo). Se usa Hibernate, Maven y muchas dependencias de Java. 
El paquete Resource es de Controllers (RestController). Domain son las entidades básicas, model los DTO, repository hacen de DAOs.

Por configuracion ver: ```project/src/main/resources``` (archivos .yml), ```project/pom.xml``` y ```project/docker-compose.yml```

Creo que en el mismo projecto podemos tener tanto archivos de Java como de Angular. 

Para investigar sobre Spring Boot recomiendo (Capitulo 2): https://howtodoinjava.com/spring-boot/spring-boot/

## Para importar proyecto de Java
Primero descargar ZIP desde GitHub (DAR A CODE Y HTTP/SSH/... -> ZIP)
### En NetBeans
File -> Import Project -> From ZIP

### En IntelliJ IDEA
File o Open -> Seleccionar ZIP descargado -> Open

### Para acceder a la UI de Swagger
Acceder a [SwaggerUI](http://localhost:8080/swagger-ui/index.html)

This app was created with Bootify.io - tips on working with the code [can be found here](https://bootify.io/next-steps/).

## Development

When starting the application `docker compose up` is called and the app will connect to the contained services.
[Docker](https://www.docker.com/get-started/) must be available on the current system.

During development it is recommended to use the profile `local`. In IntelliJ `-Dspring.profiles.active=local` can be
added in the VM options of the Run Configuration after enabling this property in "Modify options". Create your own
`application-local.yml` file to override settings for development.

Lombok must be supported by your IDE. For IntelliJ install the Lombok plugin and enable annotation processing -
[learn more](https://bootify.io/next-steps/spring-boot-with-lombok.html).

After starting the application it is accessible under `localhost:8080`.

## Build

The application can be built using the following command:

```
./mvnw clean package
```

Start your application with the following command - here with the profile `production`:

```
java -Dspring.profiles.active=production -jar ./target/project-0.0.1-SNAPSHOT.jar
```
o

```
./mvnw spring-boot:run
```

Si se quiere especificar el perfil de compilación usar:
```
-Dspring.profiles.active=
```

## Base de Datos en PostgreSQL:

Si se quiere la eliminación en casada, cambiar de:
```ON DELETE NO ACTION``` a ```ON DELETE CASCADE```


If required, a Docker image can be created with the Spring Boot plugin. Add `SPRING_PROFILES_ACTIVE=production` as
environment variable when running the container.

```
mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=dsitp.backend/project
```

## Further readings

* [Maven docs](https://maven.apache.org/guides/index.html)  
* [Spring Boot reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)  
* [Spring Data JPA reference](https://docs.spring.io/spring-data/jpa/reference/jpa.html)
