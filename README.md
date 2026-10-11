# Smart Poultry Management - Backend API

Production-ready Spring Boot backend for the Smart Poultry Farm Management system.

## Tech Stack
- **Framework**: Spring Boot 3 / 4 (Java 17+)
- **Security**: Spring Security + JWT Authentication
- **Database**: MySQL + Spring Data JPA / Hibernate
- **Build Tool**: Maven (`mvnw`)
- **Deployment**: Render (Docker or Native Java)

## Quick Start (Local)

### Prerequisites
- Java 17 or higher
- MySQL running locally on port 3306

### Database Configuration
Ensure MySQL has a database named `poultry_db`:
```sql
CREATE DATABASE poultry_db;
```

### Run Locally
```bash
./mvnw clean spring-boot:run
```
Or on Windows:
```cmd
mvnw.cmd clean spring-boot:run
```

The server runs by default on `http://localhost:8080`.

---

## Deployment on Render

### Option 1: Docker (Recommended - Auto Detected)
This repository includes a multi-stage [Dockerfile](Dockerfile) and [render.yaml](render.yaml).

1. Log in to [Render](https://render.com) and click **New +** -> **Web Service**.
2. Connect your GitHub repository: `Abhinav-reddy01/smartPoultry-backend`.
3. Select **Docker** environment (or let Render auto-detect the `Dockerfile`).
4. In **Environment Variables**, set:
   - `PORT`: `8080` (or leave default assigned by Render)
   - `SPRING_DATASOURCE_URL`: `jdbc:mysql://<cloud-db-host>:3306/poultry_db?createDatabaseIfNotExist=true&useSSL=true&serverTimezone=UTC`
   - `SPRING_DATASOURCE_USERNAME`: `<your-db-username>`
   - `SPRING_DATASOURCE_PASSWORD`: `<your-db-password>`
   - `JWT_SECRET`: `<any-strong-random-key-at-least-32-chars>`

### Option 2: Native Java Environment
- **Environment**: Java
- **Build Command**: `./mvnw clean package -DskipTests`
- **Start Command**: `java -jar target/poultry-management-0.0.1-SNAPSHOT.jar`
- **Environment Variables**: Add `JAVA_VERSION=17` and the datasource variables above.

---

## Core API Endpoints

### Authentication
- `POST /api/auth/login` - Sign in (`email`/`username` & `password`)
- `POST /api/auth/register` - Create farm administrator account

### Farm Management
- `GET/POST/PUT/DELETE /api/customers` - Customer records
- `GET/POST/PUT/DELETE /api/sales` - Daily egg sales transactions
- `GET/POST/DELETE /api/stock` - Egg collection and damaged egg stock
- `GET /api/stock/summary` - Aggregated collected, damaged, sold, and available stock
- `GET/POST/DELETE /api/income` - Non-egg revenue
- `GET/POST/DELETE /api/expenses` - Farm operational expenses
- `GET/PUT /api/tax` - Configurable farm tax rates
- `GET /api/dashboard/summary` - Real-time financial & flock summary
- `GET /api/reports/daily?date=YYYY-MM-DD` - Daily financial report
- `GET /api/reports/monthly?month=YYYY-MM` - Monthly financial report
