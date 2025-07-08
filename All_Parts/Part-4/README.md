# 🧵 Part-4: Fix Deadlock in a Multi-Threaded Environment

## ✅ Problem Statement

The system is experiencing **deadlocks** caused by improper locking, which occur sporadically under high concurrency. The challenge also includes the presence of **third-party libraries** that utilize their own locking mechanisms — making it risky to use basic `synchronized` blocks.

### Key Requirements:
- Analyze and fix the deadlock.
- Ensure thread-safe operations across shared resources.
- Avoid breaking third-party lock usage.
- Simulate high-concurrency access to validate the fix.

---

## 📁 Project Structure

Part-4-Deadlock-Fix/
├── pom.xml
├── src/
│ ├── main/
│ │ └── java/
│ │ └── deadlock/
│ │ ├── DeadlockSafeWorker.java # Worker using timed ReentrantLocks
│ │ ├── LockManager.java # Shared lock manager (modular & reusable)
│ │ └── DeadlockSafeSimulator.java # Main simulator for high concurrency test
│ └── test/
│ └── java/
│ └── deadlock/
│ └── HighConcurrencyTest.java # JUnit 5 test simulating many users


Flowchart  : 
# DeadlockSafeSimulator Diagram
+------------------------+
|  DeadlockSafeSimulator |
+-----------+------------+
            |
    +-------+-------+
    |               |
+---v---+       +---v---+
|Worker1|       |Worker2|
|methodA()      |methodB()
+---+---+       +---+---+
    |               |
    | calls         | calls
    |               |
+---v---+       +---v---+
|Lock A |       |Lock B |
+---+---+       +---+---+
    |               |
    | tryLock()     | tryLock()
    |               |
    +------>Shared Section<------+
                |
            Releases Locks


## 📐 High-Level Design (HLD)

- The simulator launches multiple threads executing worker methods.
- Each worker method tries to acquire **two locks** using `ReentrantLock.tryLock(timeout)`.
- All locking logic is handled in a reusable `LockManager` to ensure **consistency** and **modularity**.
- If locks can't be acquired within timeout, the thread backs off — **avoiding deadlock**.

---

## 🔧 Low-Level Design (LLD)

### 🔹 `LockManager.java`
- Manages two `ReentrantLock` instances (`lockA`, `lockB`).
- Provides `tryAcquireLocks(String method)` to acquire both locks with timeout.

### 🔹 `DeadlockSafeWorker.java`
- Calls LockManager methods (like `doWorkWithLocksAthenB()` or `doWorkWithLocksBthenA()`).
- Simulates real-world jobs accessing shared resources.
- Logs successful access to shared section.

### 🔹 `DeadlockSafeSimulator.java`
- Starts multiple threads running workers.
- Validates that under high concurrency, **no thread is blocked indefinitely**.

---
