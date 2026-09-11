# Sistema de Gestión de Devoluciones y Carga Masiva
Servicio Backend para la gestión, validación e ingesta masiva de solicitudes de devolución de dinero, desarrollado en Spring Boot y PostgreSQL.

---

## 🚀 Tecnologías Utilizadas
* **Java:** Version 21 LTS
* **Framework:** Spring Boot 3.4+ / 4.x
* **Base de Datos:** PostgreSQL 16
* **Migraciones DB:** Flyway
* **Persistencia:** Spring Data JPA / Hibernate
* **Librerías Auxiliares:** Lombok, Jackson
* **Pruebas:** JUnit 5, Mockito
* **Documentación API:** Springdoc OpenAPI / Swagger UI

---

## 🛠️ Arquitectura y Principios

- **Diseño por Capas:** Controller, Service, Repository, Domain / Model.
- **Validación de Reglas de Negocio:**
  - Validación de RUT chileno (Algoritmo Módulo 11).
  - Control de rangos de monto ($1 a $10.000.000 CLP).
  - Idempotencia mediante control de duplicidad en `referencia_banco`.
- **Transaccionalidad Batch:** Procesamiento atómico e informe de errores parciales por fila.

---

## 📋 Requisitos Previos

* JDK 21 instalado.
* Docker y Docker Compose (para la base de datos PostgreSQL).
* Apache Maven (o el wrapper `./mvnw` incluido).

---

## 🚀 Instrucciones de Ejecución

### 1. Iniciar la Base de Datos PostgreSQL (Docker)

```bash
docker-compose up -d