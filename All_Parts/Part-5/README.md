# 🧩 Part 5: Optimize Database Connection Pooling

## ✅ Problem Statement

The system faces issues with **database connections under high concurrency**, especially during peak load.

### 💡 Goals:
- Use **HikariCP** for efficient connection pooling
- Implement **custom monitoring** to log:
  - 🕓 Long connection waits
  - 💤 Underutilized pools
- Avoid blindly increasing pool size — instead, **optimize based on patterns**

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3.2.4
- HikariCP
- H2 (in-memory DB)
- JUnit 5

---

## 🗂 Project Structure

Part-5-Connection-Pooling/
├── pom.xml
├── src/
│ ├── main/
│ │ └── java/
│ │ └── com/
│ │ └── part5/
│ │ ├── Application.java # Spring Boot entry
│ │ ├── config/
│ │ │ └── DataSourceConfig.java # HikariCP config
│ │ ├── controller/
│ │ │ └── UserController.java # /api/users/ping
│ │ ├── repository/
│ │ │ └── UserRepository.java # DB logic
│ │ └── monitor/
│ │ └── HikariPoolMonitor.java # Scheduled pool monitoring
│ └── test/
│ └── java/
│ └── com/
│ └── part5/
│ └── concurrency/
│ └── HighConcurrencyTest.java # Simulate 50 concurrent calls





---

✅ Testing Strategy
✔️ Manual Test
Run the app

Use Postman or JMeter to hit:
GET http://localhost:9090/api/users/ping

✔️ Automated Load Test
File: HighConcurrencyTest.java

Fires 50 threads via RestTemplate hitting /ping

Observability : 
Assertions validate successful response
| Metric                   | Logged by        |
| ------------------------ | ---------------- |
| Active Connections > 80% | `WARN` log       |
| Idle Connections > 70%   | `INFO` log       |
| Pool Not Initialized     | `INFO` retry log |

## 🔁 Flowchart

```mermaid
flowchart TD
    A[Client API Request] --> B[UserController]
    B --> C[UserRepository.query()]
    C --> D[HikariCP Pool]
    D --> E[H2 DB]

    subgraph Pool Monitor
      D --> F[HikariPoolMonitor (every 5s)]
    end

    F --> G{Log Conditions}
    G --> |Active > 80%| H[⚠️ High load warning]
    G --> |Idle > 70%| I[ℹ️ Underutilization log]


 HLD with Component Flow:

          +--------------------+
          |  Client (Browser / |
          |     Postman)       |
          +--------+-----------+
                   |
                   v
      +------------+--------------+
      |      UserController       |
      |  (/api/users/ping)       |
      +------------+-------------+
                   |
                   v
     +-------------+--------------+
     |         UserRepository     |
     |     (DB query logic)       |
     +-------------+--------------+
                   |
                   v
     +-------------+--------------+
     |   HikariCP Connection Pool |
     |  (Manages JDBC connections)|
     +-------------+--------------+
                   |
                   v
           +-------+-------+
           |     H2 DB     |
           +---------------+


   ----------------------- Monitoring Side ------------------------

                     (Scheduled every 5s)
                               |
                               v
       +-------------------------------+
       |       HikariPoolMonitor       |
       |  - logs active/idle status    |
       |  - detects underutilization   |
       |  - detects saturation         |
       +-------------------------------+
