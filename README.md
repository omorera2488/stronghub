# StrongHub

## 📌 Información General
- **Nombre:** StrongHub
- **Backend:** Spring Boot 3.5.5
- **Gestión:** Maven (JDK 21)
- **Paquete raíz:** `com.bluelitelabs.stronghub`
- **Dominio ficticio:** `bluelite-labs.com`
- **Dominio de producto:** `stronghub.fit`

---

## 🗄️ Base de Datos
- **Motor:** PostgreSQL (multi-tenant con `gym_id` como clave principal).
- **Auditoría:** `created_at`, `updated_at`, `created_by`, `updated_by`, `deleted_at`.
- **Entidades clave:** `roles`, `users`, `gyms`, `members`, `trainers`, `plans`, `subscriptions`, `class_sessions`, `class_bookings`, `attendance`, `payments`.
- **Catálogos:** `subscription_status`, `payment_method`, `payment_status`, `booking_status`.
- **Seeds iniciales:** roles, usuarios, miembros, entrenadores, planes, subscripciones, clases, reservas, pagos, asistencia.

---

## ⚙️ Backend
- **Configuración:**
  - `server.servlet.context-path=/api/v1/`
  - PostgreSQL productivo, H2 para tests.
- **Dependencias:**
  - JPA, Web, Lombok, DevTools, PostgreSQL, OpenAPI/Swagger, H2 (test).
- **Implementado:**
  - Health checks: `/health`, `/health/db`
  - Swagger UI: `/api/v1/swagger-ui.html`

---

## 🚀 Roadmap
El roadmap MVP se encuentra en [`roadmap.md`](./roadmap.md):

```mermaid
gantt
    title StrongHub MVP Roadmap
    dateFormat  YYYY-MM-DD

    section Foundations
    Schema & ER           : a1, 2025-09-15, 10d
    OpenAPI               : a2, 2025-09-20, 7d

    section Core
    Members API           : b1, 2025-10-01, 10d
    Subscriptions         : b2, 2025-10-10, 10d
    Payments              : b3, 2025-10-20, 10d
    Classes & Bookings    : b4, 2025-10-25, 10d
    Attendance            : b5, 2025-11-01, 7d

    section UX & Auth
    PWA                   : c1, 2025-11-10, 20d
    JWT & Roles           : c2, 2025-11-20, 10d
🛠️ Instalación y Desarrollo
Prerrequisitos
JDK 21

Maven 3.9+

PostgreSQL 15+

Docker (opcional, para dev)

Pasos
Clonar el repositorio:

bash
Copiar código
git clone https://github.com/bluelitelabs/stronghub.git
cd stronghub
Configurar application.properties con credenciales de BD.

Ejecutar en local:

bash
Copiar código
mvn spring-boot:run
Acceder a Swagger: http://localhost:8080/api/v1/swagger-ui.html

🧪 Tests
BD de pruebas: H2 in-memory.

Ejecutar:

bash
Copiar código
mvn test
📦 CI/CD
Build + Tests + Lint en pipeline.

Contenedor dev con Docker Compose (Postgres + app).

Artefactos empaquetados vía Maven.

📚 Documentación
API: OpenAPI 3.0 disponible en /v3/api-docs.

Diagramas:

Clases

Modelo ER

Roadmap

👥 Autores
Equipo Bluelite Labs – 2025
