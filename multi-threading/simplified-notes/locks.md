# 🔒 Comprehensive Java Locks Guide - Theory & Code

## 📋 Table of Contents
1. [Basic Locks](#basic-locks)
2. [Advanced Locks](#advanced-locks)
3. [Synchronization Utilities](#synchronization-utilities)
4. [Lock Comparison Table](#lock-comparison-table)
5. [When to Use Which Lock](#when-to-use-which-lock)
6. [Complete Code Examples](#complete-code-examples)

---

## 🔐 Basic Locks

### 1. **Intrinsic Lock (synchronized keyword)**
- **Built-in** lock mechanism in Java
- **Implicit** acquisition and release
- **Object-level** or **class-level** locking
- **Non-interruptible** - thread waits indefinitely

```java
synchronized void method() {
    // critical section
}

synchronized(this) {
    // critical section
}
```

### 2. **ReentrantLock**
- **Explicit** lock with manual control
- **Reentrant** - same thread can acquire multiple times
- **Fair/Unfair** scheduling options
- **Interruptible** - can be interrupted while waiting

```java
ReentrantLock lock = new ReentrantLock(true); // fair lock
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}
```

### 3. **ReadWriteLock**
- **Separate** read and write locks
- **Multiple readers** can access simultaneously
- **Exclusive writer** access
- **Improved performance** for read-heavy workloads

```java
ReadWriteLock rwLock = new ReentrantReadWriteLock();
rwLock.readLock().lock();   // shared lock
rwLock.writeLock().lock();  // exclusive lock
```

---

## 🚀 Advanced Locks

### 4. **StampedLock (Java 8+)**
- **Three modes**: Read, Write, Optimistic Read
- **Non-blocking** optimistic reads
- **Better performance** than ReadWriteLock
- **Stamp-based** validation mechanism

```java
StampedLock lock = new StampedLock();
long stamp = lock.tryOptimisticRead();
// read data
if (!lock.validate(stamp)) {
    // fallback to read lock
}
```

### 5. **Semaphore**
- **Resource counting** mechanism
- **Controls access** to limited resources
- **Permits-based** system
- **Useful for** connection pools, rate limiting

```java
Semaphore semaphore = new Semaphore(3); // 3 permits
semaphore.acquire();
try {
    // use resource
} finally {
    semaphore.release();
}
```

### 6. **Condition Variables**
- **Alternative** to wait/notify
- **Multiple conditions** per lock
- **More flexible** thread communication
- **Works with** ReentrantLock

```java
Lock lock = new ReentrantLock();
Condition condition = lock.newCondition();
condition.await();  // like wait()
condition.signal(); // like notify()
```

---

## 🛠️ Synchronization Utilities

### 7. **CountDownLatch**
- **One-time** synchronization barrier
- **Count decrements** from N to 0
- **Threads wait** until count reaches 0
- **Use case**: Wait for multiple tasks completion

```java
CountDownLatch latch = new CountDownLatch(3);
latch.countDown(); // decrement count
latch.await();     // wait for count to reach 0
```

### 8. **CyclicBarrier**
- **Reusable** synchronization barrier
- **Threads wait** at barrier point
- **Barrier breaks** when all threads arrive
- **Use case**: Parallel processing phases

```java
CyclicBarrier barrier = new CyclicBarrier(3);
barrier.await(); // wait for all threads
```

### 9. **Phaser (Java 7+)**
- **Flexible** synchronization barrier
- **Dynamic** number of parties
- **Multiple phases** support
- **Advanced** alternative to CyclicBarrier

```java
Phaser phaser = new Phaser(3);
phaser.arriveAndAwaitAdvance(); // wait for phase completion
```

### 10. **Exchanger**
- **Two-thread** data exchange
- **Synchronous** exchange point
- **Bidirectional** communication
- **Use case**: Producer-consumer pairs

```java
Exchanger<String> exchanger = new Exchanger<>();
String data = exchanger.exchange("myData");
```

---

## 📊 Lock Comparison Table

| Lock Type | Fairness | Interruptible | Timeout | Condition | Performance |
|-----------|----------|---------------|---------|-----------|-------------|
| synchronized | No | No | No | Built-in | Good |
| ReentrantLock | Yes/No | Yes | Yes | Multiple | Good |
| ReadWriteLock | Yes/No | Yes | Yes | Multiple | Better (reads) |
| StampedLock | No | Yes | Yes | No | Best |
| Semaphore | Yes/No | Yes | Yes | No | Good |

---

## 🎯 When to Use Which Lock

| Scenario | Recommended Lock | Reason |
|----------|------------------|---------|
| Simple synchronization | `synchronized` | Built-in, easy to use |
| Need fairness/timeout | `ReentrantLock` | Advanced features |
| Read-heavy workload | `ReadWriteLock` | Multiple readers |
| High-performance reads | `StampedLock` | Optimistic reads |
| Resource limiting | `Semaphore` | Permit-based access |
| Task coordination | `CountDownLatch` | One-time barrier |
| Parallel phases | `CyclicBarrier` | Reusable barrier |
| Complex synchronization | `Phaser` | Flexible phases |
| Data exchange | `Exchanger` | Two-thread exchange |

---

## 💡 Best Practices

### ✅ Do's
- Always use try-finally with explicit locks
- Use fair locks for avoiding starvation
- Prefer ReadWriteLock for read-heavy operations
- Use StampedLock for high-performance scenarios
- Choose appropriate synchronization utility

### ❌ Don'ts
- Don't forget to unlock in finally block
- Don't use synchronized with explicit locks
- Don't hold locks longer than necessary
- Don't ignore interrupted exceptions
- Don't use wrong lock type for scenario

---

## 🔧 Quick Reference Code Snippets

### Basic ReentrantLock Pattern
```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}
```

### ReadWriteLock Pattern
```java
ReadWriteLock rwLock = new ReentrantReadWriteLock();
// Read operation
rwLock.readLock().lock();
try {
    // read data
} finally {
    rwLock.readLock().unlock();
}
```

### StampedLock Optimistic Read
```java
StampedLock lock = new StampedLock();
long stamp = lock.tryOptimisticRead();
// read operation
if (!lock.validate(stamp)) {
    // fallback to read lock
}
```

### Semaphore Resource Control
```java
Semaphore semaphore = new Semaphore(permits);
semaphore.acquire();
try {
    // use resource
} finally {
    semaphore.release();
}
```

---

# 🎪 Complete Code Examples

## 1. ReentrantLock with Fairness

```java
import java.util.concurrent.locks.ReentrantLock;

public class FairLockExample {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();
        
        // Create multiple threads
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> resource.accessResource(), "Thread-" + i);
            thread.start();
        }
    }
}

class SharedResource {
    private final ReentrantLock lock = new ReentrantLock(true); // Fair lock
    
    public void accessResource() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " acquired lock");
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + " releasing lock");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}
```

## 2. ReadWriteLock for Cache Implementation

```java
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheExample {
    public static void main(String[] args) {
        Cache cache = new Cache();
        
        // Reader threads
        for (int i = 0; i < 3; i++) {
            Thread reader = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    System.out.println("Read: " + cache.get("key" + j));
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                }
            }, "Reader-" + i);
            reader.start();
        }
        
        // Writer thread
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                cache.put("key" + i, "value" + i);
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
        }, "Writer");
        writer.start();
    }
}

class Cache {
    private final Map<String, String> cache = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public String get(String key) {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " reading key: " + key);
            Thread.sleep(100);
            return cache.get(key);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void put(String key, String value) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " writing key: " + key);
            cache.put(key, value);
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
```

## 3. StampedLock Optimistic Read Example

```java
import java.util.concurrent.locks.StampedLock;

public class OptimisticReadExample {
    public static void main(String[] args) {
        Point point = new Point(10, 20);
        
        // Reader thread with optimistic read
        Thread reader = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("Distance: " + point.distanceFromOrigin());
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            }
        }, "Reader");
        
        // Writer thread
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                point.move(i, i);
                try { Thread.sleep(300); } catch (InterruptedException e) {}
            }
        }, "Writer");
        
        reader.start();
        writer.start();
    }
}

class Point {
    private double x, y;
    private final StampedLock lock = new StampedLock();
    
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double distanceFromOrigin() {
        long stamp = lock.tryOptimisticRead();
        double curX = x, curY = y;
        
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                curX = x;
                curY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        
        return Math.sqrt(curX * curX + curY * curY);
    }
    
    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
            System.out.println("Point moved to (" + x + ", " + y + ")");
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
```

## 4. Semaphore Connection Pool

```java
import java.util.concurrent.Semaphore;

public class ConnectionPoolExample {
    public static void main(String[] args) {
        ConnectionPool pool = new ConnectionPool(3);
        
        // Create multiple clients
        for (int i = 0; i < 10; i++) {
            Thread client = new Thread(() -> {
                try {
                    Connection conn = pool.getConnection();
                    // Use connection
                    Thread.sleep(2000);
                    pool.releaseConnection(conn);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Client-" + i);
            client.start();
        }
    }
}

class ConnectionPool {
    private final Semaphore available;
    private final Connection[] connections;
    private final boolean[] used;
    
    public ConnectionPool(int size) {
        available = new Semaphore(size, true);
        connections = new Connection[size];
        used = new boolean[size];
        
        for (int i = 0; i < size; i++) {
            connections[i] = new Connection("Connection-" + i);
        }
    }
    
    public Connection getConnection() throws InterruptedException {
        available.acquire();
        return getNextAvailableConnection();
    }
    
    public void releaseConnection(Connection connection) {
        if (markAsUnused(connection)) {
            available.release();
        }
    }
    
    private synchronized Connection getNextAvailableConnection() {
        for (int i = 0; i < used.length; i++) {
            if (!used[i]) {
                used[i] = true;
                System.out.println(Thread.currentThread().getName() + 
                    " acquired " + connections[i].getName());
                return connections[i];
            }
        }
        return null;
    }
    
    private synchronized boolean markAsUnused(Connection connection) {
        for (int i = 0; i < connections.length; i++) {
            if (connections[i] == connection) {
                if (used[i]) {
                    used[i] = false;
                    System.out.println(Thread.currentThread().getName() + 
                        " released " + connection.getName());
                    return true;
                }
            }
        }
        return false;
    }
}

class Connection {
    private final String name;
    
    public Connection(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
```

## 5. CountDownLatch Task Coordination

```java
import java.util.concurrent.CountDownLatch;

public class TaskCoordinationExample {
    public static void main(String[] args) throws InterruptedException {
        int numTasks = 5;
        CountDownLatch latch = new CountDownLatch(numTasks);
        
        // Start multiple tasks
        for (int i = 0; i < numTasks; i++) {
            Thread task = new Thread(new Task(latch, i), "Task-" + i);
            task.start();
        }
        
        System.out.println("Waiting for all tasks to complete...");
        latch.await(); // Wait for all tasks to complete
        System.out.println("All tasks completed!");
    }
}

class Task implements Runnable {
    private final CountDownLatch latch;
    private final int taskId;
    
    public Task(CountDownLatch latch, int taskId) {
        this.latch = latch;
        this.taskId = taskId;
    }
    
    @Override
    public void run() {
        try {
            // Simulate task work
            Thread.sleep((taskId + 1) * 1000);
            System.out.println("Task " + taskId + " completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            latch.countDown(); // Signal completion
        }
    }
}
```

## 6. CyclicBarrier Parallel Processing

```java
import java.util.concurrent.CyclicBarrier;

public class ParallelProcessingExample {
    public static void main(String[] args) {
        int numThreads = 4;
        CyclicBarrier barrier = new CyclicBarrier(numThreads, () -> {
            System.out.println("All threads finished phase, starting next phase");
        });
        
        for (int i = 0; i < numThreads; i++) {
            Thread worker = new Thread(new Worker(barrier, i), "Worker-" + i);
            worker.start();
        }
    }
}

class Worker implements Runnable {
    private final CyclicBarrier barrier;
    private final int workerId;
    
    public Worker(CyclicBarrier barrier, int workerId) {
        this.barrier = barrier;
        this.workerId = workerId;
    }
    
    @Override
    public void run() {
        try {
            for (int phase = 1; phase <= 3; phase++) {
                // Do work for current phase
                System.out.println("Worker " + workerId + " working on phase " + phase);
                Thread.sleep((workerId + 1) * 500);
                
                System.out.println("Worker " + workerId + " finished phase " + phase);
                barrier.await(); // Wait for all workers to finish phase
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

## 7. Exchanger Data Exchange

```java
import java.util.concurrent.Exchanger;

public class DataExchangeExample {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    String data = "Data-" + i;
                    System.out.println("Producer sending: " + data);
                    String received = exchanger.exchange(data);
                    System.out.println("Producer received: " + received);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");
        
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    String ack = "ACK-" + i;
                    String received = exchanger.exchange(ack);
                    System.out.println("Consumer received: " + received);
                    System.out.println("Consumer sending: " + ack);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");
        
        producer.start();
        consumer.start();
    }
}
```

---

## 🎯 Key Takeaways

1. **Choose the right lock** for your use case
2. **Always use try-finally** with explicit locks
3. **Consider fairness** for avoiding starvation
4. **Use ReadWriteLock** for read-heavy operations
5. **StampedLock** offers best performance for read-heavy scenarios
6. **Semaphore** is perfect for resource limiting
7. **CountDownLatch** for one-time coordination
8. **CyclicBarrier** for reusable synchronization points
9. **Exchanger** for bidirectional data exchange

Remember: The key to effective multithreading is choosing the right synchronization mechanism for your specific use case!