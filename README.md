# BCMEA Expo



## How to run

Prerequisite

* JDK 17+
* Maven 4.0.0
* Spring Boot 3.1.8

```
mvn install
```

Run

```
mvn spring-boot:run
```

## Api Documantation

- Admin APIs endpoint
  ```
  /api/v1/organizers/signup
  ```
  Request Body
  ```
  {
  "email":"test@gmail.com",
  "password":"123",
  "roleEnum":"ADMIN"	
  }
  ```
