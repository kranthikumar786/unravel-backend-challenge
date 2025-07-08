# 🧠 Part 2: Resolve Memory Management Issue

---

## ✅ Problem Statement (from PDF)

> "The system is experiencing memory leaks due to improper session data management. The memory issue arises sporadically, particularly under high user loads."

---

## ❌ Root Cause

### 🔍 Code Before Fix

```java
private static Map<String, byte[]> largeSessionData = new HashMap<>();

public static void addSessionData(String sessionId) {
    largeSessionData.put(sessionId, new byte[10 * 1024 * 1024]); // 10MB per session
}
```

* `static` in-memory map holds growing session data.
* No session expiry or cleanup.
* Results in high **heap pressure** → **OOM** under concurrent load.

---

## ✅ Resolution Strategy

### 1. 🔄 Replace In-Memory Map with Redis

* Redis is used to **offload session data** from the Java heap.
* Supports **distributed access** and **TTL**.

### 2. 🧼 Add TTL to Session Data (optional)

```java
redisTemplate.opsForValue().set(sessionId, data, Duration.ofMinutes(30));
```

---

## 📁 Project Structure
Part-2-Memory-Management-Fix/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   └── java/
│   │       └── session/
│   │           ├── Application.java               # Spring Boot main class
│   │           ├── MemoryManager.java             # Session memory handler
│   │           ├── RedisConfig.java               # Redis configuration class
│   │           └── MemoryController.java          # REST API controller
│   └── test/
│       └── java/
│           └── session/
│               ├── MemoryManagerTest.java        # Unit test for memory logic
│               └── RedisConfigTest.java          # Unit test for config
├── src/
│   └── main/
│       └── resources/
│           └── application.properties             # Spring config for Redis


## ✅ Technology Stack

* Java 17
* Spring Boot 3.2.4
* Redis (via Lettuce)
* JUnit 5 (Testing)

---

## 🧪 Test Coverage

* `POST /api/memory/add/{sessionId}` → adds 10MB dummy data
* `GET /api/memory/get/{sessionId}` → checks existence
* `DELETE /api/memory/remove/{sessionId}` → removes data from Redis

### Example (Postman or PowerShell):

```bash
Invoke-RestMethod -Uri http://localhost:8080/api/memory/add/kranthi -Method POST
Invoke-RestMethod -Uri http://localhost:8080/api/memory/get/kranthi -Method GET
Invoke-RestMethod -Uri http://localhost:8080/api/memory/remove/kranthi -Method DELETE
```

---

## 🧱 High-Level Design (HLD)

```
+-------------+     HTTP     +-------------------+      Redis
|   Client    |  <-------->  |  Spring Boot App  |  <-------------->
+-------------+              |   (MemoryController)     |
                             |  (MemoryManager + Redis) |
                             +-------------------+
```

---

## 🔧 Low-Level Design (LLD)

### RedisConfig.java

```java
@Bean
public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory("localhost", 6379);
}

@Bean
public RedisTemplate<String, byte[]> redisTemplate(...) { ... }
```

### MemoryManager.java

```java
public void addSessionData(String sessionId) {
    redisTemplate.opsForValue().set(sessionId, new byte[10 * 1024 * 1024]);
}
```

### MemoryController.java

```java
@PostMapping("/add/{sessionId}")
public String add(@PathVariable String sessionId) { ... }
```

---

## ✅ Benefits After Fix

* 🧹 Memory usage offloaded to Redis
* ✅ Supports scale-out in distributed environments
* 🚫 No memory leak
* 📈 Ready for high concurrency

---

## 📄 Future Enhancements

* Add TTL with `set(..., Duration.ofMinutes(30))`
* Add monitoring (Redis usage metrics)
* Move Redis configs to `application.properties`

---
