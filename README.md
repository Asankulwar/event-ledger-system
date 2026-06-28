# 📘 Event Ledger System

## 🚀 Overview
Event Ledger is a microservices-based system built with **Spring Boot**.  
It consists of:
- **Event Gateway** → Handles incoming events and routes them to the Account Service.
- **Account Service** → Manages account balances and transaction history.

---

## 🛠️ Tech Stack
- Java 17  
- Spring Boot  
- Maven  
- H2 Database (in-memory)  
- Docker & Docker Compose  
- Swagger/OpenAPI for API documentation  

---

## 📂 Project Structure
event-ledger/
├── event-gateway/
│   ├── src/
│   ├── target/event-gateway-0.0.1-SNAPSHOT.jar
│   └── Dockerfile
├── account-service/
│   ├── src/
│   ├── target/account-service-0.0.1-SNAPSHOT.jar
│   └── Dockerfile
└── docker-compose.yml

⚙️ Build & Run (Local
1. Build JARs: 
mvn clean package -DskipTests

2. Run services:
java -jar event-gateway/target/event-gateway-0.0.1-SNAPSHOT.jar
java -jar account-service/target/account-service-0.0.1-SNAPSHOT.jar

🐳 Build & Run (Docker)

1. Ensure Docker Desktop is running.

2. From project root:
docker compose up --build

3. Access services:
Event Gateway → http://localhost:8080/swagger-ui.html

Account Service → http://localhost:8081/swagger-ui.html


📡 API Endpoints
Event Gateway
POST /events → Submit event

GET /events/{id} → Retrieve event by ID

GET /events?account={accountId} → List events for account

GET /events/health → Health check

Account Service
POST /accounts/{accountId}/transactions → Apply transaction

GET /accounts/{accountId}/balance → Get balance

GET /accounts/{accountId} → Get account details

GET /health → Health check


Testing
Run integration tests:

mvn test
Event Gateway → EventGatewayIntegrationTest

Account Service → AccountServiceIntegrationTest

Notes
Implements resiliency patterns (circuit breaker, graceful degradation).

Independent H2 databases for each service.

Clear API contracts between Gateway and Account Service
