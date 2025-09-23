# Spring Boot Notes

- Java 21 + Spring Boot 3.x
- Modules: members, trainers, plans, subscriptions, classes, payments, attendance, auth
- Dependencies: spring-boot-starter-web, spring-boot-starter-data-jpa, spring-boot-starter-security, jjwt, postgresql
- Packaging: monolith modular + clear package by feature (ex: com.acme.gym.members)
- Security: JWT (access + refresh), roles ADMIN/TRAINER/MEMBER
- Multi-tenancy (optional): add `gym_id` to every table or schema-per-tenant
- Flyway/Liquibase for DB migrations
- MapStruct for DTO mapping
- OpenAPI/Swagger via springdoc-openapi-starter-webmvc-ui
- Test: JUnit 5 + Testcontainers (Postgres)
- Docker: docker-compose.yml with Postgres + app