# Order Processing System

Two Spring Boot 3 / Java 21 services, PostgreSQL, NGINX reverse proxy and Docker Compose.

## Architecture
- NGINX is the only published application port (`localhost:8080`).
- `/api/orders/**` routes to Service A, which persists orders in PostgreSQL.
- `/api/process/**` routes to two Service B replicas using NGINX `least_conn`.
- PostgreSQL data is persisted in the `postgres_data` Docker volume.

## Run everything
Requires Docker Desktop with Docker Compose.

```powershell
Copy-Item .env.example .env
# Edit .env and set a strong POSTGRES_PASSWORD
docker compose up --build -d
docker compose ps
```

## Test with PowerShell

Create an order:
```powershell
$body = @{ productName = "Laptop"; quantity = 2 } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/orders -ContentType "application/json" -Body $body
```

List orders:
```powershell
Invoke-RestMethod http://localhost:8080/api/orders
```

Get order 1:
```powershell
Invoke-RestMethod http://localhost:8080/api/orders/1
```

Process order 1:
```powershell
$body = @{ orderId = 1 } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/process -ContentType "application/json" -Body $body
```

Health:
```powershell
Invoke-RestMethod http://localhost:8080/api/orders/health
Invoke-RestMethod http://localhost:8080/api/process/health
Invoke-WebRequest http://localhost:8080/nginx-health
```

## Useful commands
```powershell
docker compose logs -f
docker compose logs -f service-a
docker compose logs -f service-b-1
docker compose down
docker compose down -v # WARNING: deletes the PostgreSQL data volume
```

## Build executable JARs locally (Java 21 + Maven 3.9+)
From the repository root:
```powershell
mvn -pl service-a,service-b -am clean package -DskipTests
```
JAR outputs:
- `service-a/target/service-a-1.0.0.jar`
- `service-b/target/service-b-1.0.0.jar`

The Dockerfiles also build these JARs in a Maven build stage, so Maven does not need to be installed on the host when using `docker compose up --build`.

## Notes
This is a learning/demo project. Service B currently demonstrates processing and load balancing; it does not update Service A's database. The database schema is created/updated by Hibernate for simplicity. For production, use Flyway/Liquibase, secrets management, non-root containers, stricter validation and automated tests.
