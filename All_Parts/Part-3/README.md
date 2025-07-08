# ⚙️ Part 3: Producer-Consumer with Priority Tasks

## 🧩 Problem Statement

> Implement the classic **Producer-Consumer** problem using threads, but with the following twist:
>
> - Consumers must process **different types of tasks**.
> - Tasks must have **varying priorities** (e.g., CRITICAL, INFO, DEBUG).
> - The system must **dynamically prioritize tasks** so that high-priority tasks are handled first.
> - Lower-priority tasks should not **starve** — fairness must be ensured.
> - **Thread pools** and **priority queues** must be used to simulate concurrent load.

---

## ✅ Solution Overview

| Requirement (from prompt) | ✅ Implemented |
|---------------------------|----------------|
| Multi-threaded producer-consumer setup | ✅ Yes |
| Different types of tasks | ✅ Yes (`LogTask.type`) |
| Varying priorities | ✅ Yes (`LogTask.priority`) |
| Dynamically prioritize tasks | ✅ Yes (`PriorityBlockingQueue + Comparator`) |
| Avoid starvation | ✅ Yes (compare by priority, then timestamp) |
| Scalable, modular design | ✅ Yes (well-separated layers) |

---

## 🗂 Project Structure

Part-3-Producer-Consumer/
├── pom.xml
├── src/
│ ├── main/
│ │ └── java/
│ │ ├── comparator/
│ │ │ └── LogTaskComparator.java # Comparator by priority + timestamp
│ │ ├── model/
│ │ │ └── LogTask.java # Task with type, priority, createdAt
│ │ ├── processor/
│ │ │ └── LogProcessor.java # Shared PriorityBlockingQueue logic
│ │ ├── runner/
│ │ │ ├── LogProducer.java # Producer thread
│ │ │ └── LogConsumer.java # Consumer thread
│ │ └── MainApp.java # Main app runner
│ └── test/
│ └── java/
│ └── processor/
│ └── ConcurrentLogProcessingTest.java # JUnit test for concurrency

---

## 🛠 Technologies Used

- Java 17
- JUnit 5 for testing
- `PriorityBlockingQueue` for priority-based ordering
- `ExecutorService` for thread pooling

---

## 📈 Flowchart

[Producer Thread(s)] --> [LogProcessor.submit(task)] --> [PriorityBlockingQueue]
↓
[Consumer Thread(s) consume()]
↓
[Task executed by priority]



---

## 🧱 Components

###  LogTask

```java
class LogTask {
  String type;          // INFO, DEBUG, etc.
  int priority;         // Lower = more critical
  Instant createdAt;    // For starvation prevention
}

 LogTaskComparator
Orders by:

priority (lowest first)

createdAt (older first to avoid starvation)


Comparator<LogTask> BY_PRIORITY_THEN_TIMESTAMP = (t1, t2) -> {
    int cmp = Integer.compare(t1.getPriority(), t2.getPriority());
    return cmp != 0 ? cmp : t1.getCreatedAt().compareTo(t2.getCreatedAt());
};


 LogProcessor
  Wraps a PriorityBlockingQueue<LogTask>

  Offers methods: submit() and consume()

 Producer & Consumer
  LogProducer: Creates dummy logs and submits them

  LogConsumer: Polls from queue and processes logs


 Test Case: ConcurrentLogProcessingTest
Simulates:
3 producers × 5 logs = 15 total tasks

2 consumers running concurrently

Asserts that tasks are processed in priority order without starvation

assertEquals(expectedSortedList, consumedList);

 Key Design Principles
------------------------------------------------------------------------------------
| Principle                | How It's Handled                                    |
| ------------------------ | --------------------------------------------------- |
| Prioritization           | `PriorityBlockingQueue` with custom `Comparator`    |
| Fairness (no starvation) | `createdAt` is used to break tie                    |
| Modular design           | Comparator, model, processor, threads are decoupled |
| Scalability              | Thread-safe, high-throughput ready design           |
---------------------------------------------------------------------------------------

High-Level Design : 


                           ┌────────────────────┐
                           │     MainApp.java   │
                           │ (Entry Point Class)│
                           └────────┬───────────┘
                                    │
     ┌──────────────────────────────┼──────────────────────────────┐
     ▼                              ▼                              ▼
┌───────────────┐         ┌──────────────────┐           ┌──────────────────┐
│ LogProducer   │         │ LogProcessor     │<────────┐ │ LogConsumer      │
│ (Thread)      │◄────────│ (Shared Queue)   │         │ │ (Thread)         │
└───────────────┘         │ - PriorityQueue  │         │ └──────────────────┘
     ▲   ▲                │ - submit(task)   │         │
     │   │                │ - consume()      │         │
     │   │                └──────────────────┘         │
     │   │                                             │
     │   └────── Produces LogTask objects ─────────────┘
     ▼
┌────────────────────┐
│ LogTask            │
│ - message          │
│ - type (INFO, ...) │
│ - priority (int)   │
│ - createdAt        │
└────────────────────┘

Comparator → LogTaskComparator (used by PriorityQueue):
    Order by: Priority → Created Time
