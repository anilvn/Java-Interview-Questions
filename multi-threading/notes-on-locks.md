

## 🔒 Java Locks in Multithreading - Summary Notes

### 1. **Monitor Lock (synchronized keyword)**

* Implicit lock per object.
* One thread can acquire per object at a time.
* If multiple objects are used, threads may bypass the lock.

### 2. **ReentrantLock**

* Explicit lock (not bound to object).
* Must call `lock()` and `unlock()` manually.
* Works across multiple objects as long as same lock object is shared.

### 3. **ReadWriteLock**

* Separates **Read (Shared Lock)** and **Write (Exclusive Lock)**.
* Multiple readers allowed if no writer.
* Only one writer allowed, no reader during write.

### 4. **StampedLock**

* Enhances `ReadWriteLock` with **Optimistic Reads**.
* Supports:

  * `readLock()`
  * `writeLock()`
  * `tryOptimisticRead()` + `validate(stamp)`

### 5. **Semaphore**

* Controls **number of threads** entering a critical section.
* Use-case: Connection pool, printers, etc.
* Allows **N threads** to acquire lock simultaneously.

### 6. **Condition (Alternative to wait/notify)**

* Used with custom locks (e.g., ReentrantLock).
* `await()` → like `wait()`
* `signal()` → like `notify()`
* `signalAll()` → like `notifyAll()`

---

## 🔁 Shared vs Exclusive Lock – Table

| Lock Type       | Allows Multiple Threads? | Allows Write? | Used In                           |
| --------------- | ------------------------ | ------------- | --------------------------------- |
| Shared Lock     | ✅ Yes (multiple reads)   | ❌ No          | ReadWriteLock - readLock()        |
| Exclusive Lock  | ❌ No (only 1 thread)     | ✅ Yes         | ReadWriteLock - writeLock()       |
| Optimistic Lock | ✅ Yes (without locking)  | ✅ If valid    | StampedLock - tryOptimisticRead() |

---

## 🔄 When to Use Which Lock

| Use Case                                                 | Lock Type       |
| -------------------------------------------------------- | --------------- |
| Simple object-level thread safety                        | `synchronized`  |
| Locking across multiple objects                          | `ReentrantLock` |
| Many reads, few writes (high read concurrency)           | `ReadWriteLock` |
| Optimistic concurrency, with minimal lock overhead       | `StampedLock`   |
| Limit concurrent access (e.g., 2 threads for 2 printers) | `Semaphore`     |
| Thread communication (no monitor lock)                   | `Condition`     |

---

## 🔧 Code Snippets

### ReentrantLock (Object-Independent Lock)

```java
Lock lock = new ReentrantLock();

void producer() {
    lock.lock();
    try {
        // critical section
    } finally {
        lock.unlock();
    }
}
```

---

### ReadWriteLock

```java
ReadWriteLock lock = new ReentrantReadWriteLock();

// Reader
lock.readLock().lock();
try {
    // read only
} finally {
    lock.readLock().unlock();
}

// Writer
lock.writeLock().lock();
try {
    // write logic
} finally {
    lock.writeLock().unlock();
}
```

---

### StampedLock - Optimistic Read

```java
StampedLock lock = new StampedLock();

long stamp = lock.tryOptimisticRead();
// read operation

if (!lock.validate(stamp)) {
    stamp = lock.readLock();
    try {
        // fallback read
    } finally {
        lock.unlockRead(stamp);
    }
}
```

---

### Semaphore (Allow N threads at a time)

```java
Semaphore semaphore = new Semaphore(2); // allow 2 threads

semaphore.acquire();
try {
    // critical section
} finally {
    semaphore.release();
}
```

---

### Condition (Like wait/notify but for ReentrantLock)

```java
Lock lock = new ReentrantLock();
Condition condition = lock.newCondition();

lock.lock();
try {
    condition.await();  // like wait()
    // after being signaled
} finally {
    lock.unlock();
}

// In another thread
lock.lock();
try {
    condition.signal();  // like notify()
} finally {
    lock.unlock();
}
```

---

Let me know if you want this in a **PDF format**, or to export to **Notion / Word**.
Also, do you want to cover `CountDownLatch`, `CyclicBarrier`, or `Phaser` next?


<br/><br/><br/><br/>


Here is a **clean and formatted version** of all the examples from the **"Concept && Coding"** YouTube video — covering `ReentrantLock`, `ReadWriteLock`, `StampedLock` (including optimistic lock), and `Semaphore` in Java.

---

# ✅ **Java Locking Examples (Formatted)**

---

## 🔹 1. ReentrantLock Example

```java
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Thread th1 = new Thread(() -> resource.producer());
        Thread th2 = new Thread(() -> resource.producer());

        th1.start();
        th2.start();
    }
}

class SharedResource {
    boolean isAvailable = false;
    ReentrantLock lock = new ReentrantLock();

    public void producer() {
        try {
            lock.lock();
            System.out.println("Lock acquired by: " + Thread.currentThread().getName());
            isAvailable = true;
            Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println("Lock released by: " + Thread.currentThread().getName());
        }
    }
}
```

---

## 🔹 2. ReadWriteLock Example

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();
        ReadWriteLock lock = new ReentrantReadWriteLock();

        Thread th1 = new Thread(() -> resource.producer(lock));
        Thread th2 = new Thread(() -> resource.producer(lock));
        Thread th3 = new Thread(() -> resource.consumer(lock));

        th1.start();
        th2.start();
        th3.start();
    }
}

class SharedResource {
    boolean isAvailable = false;

    public void producer(ReadWriteLock lock) {
        try {
            lock.readLock().lock();
            System.out.println("Read Lock acquired by: " + Thread.currentThread().getName());
            isAvailable = true;
            Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
            System.out.println("Read Lock released by: " + Thread.currentThread().getName());
        }
    }

    public void consumer(ReadWriteLock lock) {
        try {
            lock.writeLock().lock();
            System.out.println("Write Lock acquired by: " + Thread.currentThread().getName());
            isAvailable = false;
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
            System.out.println("Write Lock released by: " + Thread.currentThread().getName());
        }
    }
}
```

---

## 🔹 3. StampedLock Example (Read & Write)

```java
import java.util.concurrent.locks.StampedLock;

public class Main {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Thread th1 = new Thread(() -> resource.producer());
        Thread th2 = new Thread(() -> resource.consumer());

        th1.start();
        th2.start();
    }
}

class SharedResource {
    boolean isAvailable = false;
    StampedLock lock = new StampedLock();

    public void producer() {
        long stamp = lock.readLock();
        try {
            System.out.println("Read Lock acquired by: " + Thread.currentThread().getName());
            isAvailable = true;
            Thread.sleep(6000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlockRead(stamp);
            System.out.println("Read Lock released by: " + Thread.currentThread().getName());
        }
    }

    public void consumer() {
        long stamp = lock.writeLock();
        try {
            System.out.println("Write Lock acquired by: " + Thread.currentThread().getName());
            isAvailable = false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamp);
            System.out.println("Write Lock released by: " + Thread.currentThread().getName());
        }
    }
}
```

---

## 🔹 4. StampedLock with Optimistic Read

```java
import java.util.concurrent.locks.StampedLock;

public class Main {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Thread th1 = new Thread(() -> resource.producer());
        Thread th2 = new Thread(() -> resource.consumer());

        th1.start();
        th2.start();
    }
}

class SharedResource {
    int a = 10;
    StampedLock lock = new StampedLock();

    public void producer() {
        long stamp = lock.tryOptimisticRead();
        try {
            System.out.println("Taken optimistic lock");
            a = 11;
            Thread.sleep(6000);
            if (lock.validate(stamp)) {
                System.out.println("Updated 'a' value successfully");
            } else {
                System.out.println("Rollback of work");
                a = 10; // rollback
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void consumer() {
        long stamp = lock.writeLock();
        try {
            System.out.println("Write lock acquired by: " + Thread.currentThread().getName());
            System.out.println("Performing write operation...");
            a = 9;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamp);
            System.out.println("Write lock released by: " + Thread.currentThread().getName());
        }
    }
}
```

---

## 🔹 5. Semaphore Example

```java
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Thread th1 = new Thread(() -> resource.producer());
        Thread th2 = new Thread(() -> resource.producer());
        Thread th3 = new Thread(() -> resource.producer());
        Thread th4 = new Thread(() -> resource.producer());

        th1.start();
        th2.start();
        th3.start();
        th4.start();
    }
}

class SharedResource {
    boolean isAvailable = false;
    Semaphore lock = new Semaphore(2); // Only 2 threads allowed at a time

    public void producer() {
        try {
            lock.acquire();
            System.out.println("Lock acquired by: " + Thread.currentThread().getName());
            isAvailable = true;
            Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.release();
            System.out.println("Lock released by: " + Thread.currentThread().getName());
        }
    }
}
```

---

Let me know if you'd like:

* A **GitHub repo** with all these examples,
* A **PDF version** for your interview notes,
* Or a side-by-side **comparison summary table** of all lock types.
