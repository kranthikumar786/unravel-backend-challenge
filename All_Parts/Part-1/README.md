#  Part 1 – Distributed Session Handling with Redis

## 🔧 Problem

The system was experiencing **session inconsistency** due to improper in-memory session handling in a **distributed environment**.

###  Objective

- Centralize session handling across distributed instances.
- Ensure session consistency across all nodes.
- Make session storage atomic and reliable.

---

##  How the Code Resolves the Problem

###  Problem Areas

Previously, sessions were stored **in-memory**, which caused:

- Sessions to be inconsistent across multiple servers.
- No persistence of sessions between restarts.
- Race conditions due to non-atomic updates.

---

###  Fix Implemented in the Code

| Area                            | Description                                                                                   | File(s)                                      |
|---------------------------------|-----------------------------------------------------------------------------------------------|----------------------------------------------|
| **Centralized Session Store**   | Redis is used for session storage. Shared across all instances.                               | `RedisConfig.java`                           |
| **Atomic & Consistent Access**  | `RedisTemplate<String, byte[]>` handles session data atomically.                              | `RedisSessionManager.java`                   |
| **Loose Coupling via Spring**   | Beans are injected via Spring (`@Configuration`, `@Autowired`) for decoupled logic.           | `Application.java`, `RedisConfig.java`       |
| **Environment Configurable**    | Redis host/port fetched using `@Value`, with fallback defaults.                              | `application.properties`, `RedisConfig.java` |
| **Distributed-Ready**           | All services (server1, server2, etc.) use the same Redis config for session consistency.       | Singleton beans (`@Configuration`)           |
| **Testing Implemented**         | Includes both **unit tests** and **integration tests** with Redis.                            | `RedisSessionManagerTest`, `IntegrationTest` |

---

## ✅ Technology Stack

* Java 17
* Spring Boot 3.2.4
* Redis (via Lettuce)
* JUnit 5 (Testing)

---

## 📁 Project Structure

Part-1/
├── pom.xml
├── src/
│   ├── main/
│   │   └── java/
│   │       └── session/
│   │           ├── Application.java
│   │           ├── RedisConfig.java
│   │           ├── RedisSessionManager.java
│   │           └── SessionController.java
│   ├── main/resources/
│   │   └── application.properties
│   └── test/
│       └── java/
│           └── session/
│               ├── RedisSessionIntegrationTest.java
│               └── RedisSessionManagerTest.java

---
##  High-Level Design (HLD) 

                ┌────────────┐
                │  Client    │
                └────┬───────┘
                     │
              ┌──────▼────────────┐
              │ SessionController │
              └──────┬────────────┘
                     │
              ┌──────▼────────────────────┐
              │ RedisSessionManager       │
              └──────┬────────────────────┘
                     │
              ┌──────▼──────────┐
              │ RedisTemplate   │────► Redis Server (shared)
              └─────────────────┘



---

##  Achievements in Summary

| Problem                            | Solution Implemented                           |
|------------------------------------|------------------------------------------------|
| Inconsistent in-memory sessions    | Shared Redis-based session store               |
| Non-atomic updates                 | Redis atomic `set`, `get`, `delete` operations |
| Deployment across multiple servers | Singleton config and centralized data          |
| Tight coupling in config           | Externalized config with fallback via `@Value` |
| No test coverage                   | Unit + Integration tests                       |

---


