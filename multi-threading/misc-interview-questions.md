
<br/><br/><br/>

- diff bw sleep()  vs wait()
- producer, consumer pattern
- Thread creation in various ways 

<br/><br/>


##  `sleep()` vs `wait()`

| Feature                          | `sleep()`                           | `wait()`                                                    |
| -------------------------------- | ----------------------------------- | ----------------------------------------------------------- |
| **Defined in**                   | `Thread` class                      | `Object` class                                              |
| **Purpose**                      | Pause current thread for a duration | Wait for a condition to be met (usually in synchronization) |
| **Used for**                     | Delays, timeouts                    | Inter-thread communication/synchronization                  |
| **Requires synchronized block?** | ❌ No                                | ✅ Yes                                                       |
| **Releases lock?**               | ❌ No — retains any locks held       | ✅ Yes — releases the monitor lock                           |
| **How it resumes**               | Automatically after time expires    | Must be `notified()` by another thread                      |
| **Throws Exception**             | `InterruptedException`              | `InterruptedException`                                      |
| **Thread state**                 | `TIMED_WAITING`                     | `WAITING` or `TIMED_WAITING` (if `wait(timeout)`)           |

---

##  `sleep()` – Pauses current thread

### 🔹 Example:

```java
System.out.println("Sleeping...");
Thread.sleep(2000); // sleeps for 2 seconds
System.out.println("Awake now.");
```

### 🔹 Key Points:

* Doesn’t release any monitor/lock it holds.
* Useful for retry mechanisms, polling, or artificial delay.

---

## `wait()` – Wait for a condition (used with `notify()`/`notifyAll()`)

### 🔹 Example:

```java
synchronized (lock) {
    System.out.println("Waiting...");
    lock.wait(); // releases lock and waits
    System.out.println("Notified and resumed.");
}
```

In another thread:

```java
synchronized (lock) {
    lock.notify(); // wakes one thread waiting on the lock
}
```

### 🔹 Key Points:

* Releases the lock.
* Must be used inside a `synchronized` block.
* Used when one thread needs to **pause until another thread signals it**.

---

##  Analogy:

| Situation | Description                                                                     |
| --------- | ------------------------------------------------------------------------------- |
| `sleep()` | You set an alarm for 5 minutes and take a nap. No one can wake you earlier.     |
| `wait()`  | You wait for someone to tap your shoulder and say "go ahead". You’re listening. |

---

##  When to Use What

| Scenario                                                           | Use                                 |
| ------------------------------------------------------------------ | ----------------------------------- |
| You want a thread to pause for a fixed time                        | `sleep()`                           |
| You want a thread to pause until a condition or signal is received | `wait()` + `notify()`/`notifyAll()` |



<br/><br/><br/><br/>
Absolutely! A **Producer-Consumer** example is one of the best ways to understand how `wait()` and `notify()` work in **real multithreaded coordination**.

---

## 🧵 Producer-Consumer Problem using `wait()` and `notify()`

### 👇 Scenario:

* A **Producer** thread puts data into a shared buffer.
* A **Consumer** thread takes data from the buffer.
* If the buffer is **full**, the producer must wait.
* If the buffer is **empty**, the consumer must wait.

We’ll simulate this with a **bounded buffer (queue of size 1)** for simplicity.

---

### ✅ Complete Java Example:

```java
import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerExample {

    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer(1);

        Thread producer = new Thread(() -> {
            int value = 1;
            while (true) {
                buffer.produce(value++);
                sleep(1000); // delay for demonstration
            }
        });

        Thread consumer = new Thread(() -> {
            while (true) {
                buffer.consume();
                sleep(1500);
            }
        });

        producer.start();
        consumer.start();
    }

    // Helper method to sleep safely
    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Shared buffer class
    static class SharedBuffer {
        private final Queue<Integer> queue = new LinkedList<>();
        private final int capacity;

        public SharedBuffer(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void produce(int value) {
            while (queue.size() == capacity) {
                try {
                    System.out.println("Buffer full, producer waiting...");
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            queue.offer(value);
            System.out.println("Produced: " + value);
            notify(); // Wake up consumer
        }

        public synchronized void consume() {
            while (queue.isEmpty()) {
                try {
                    System.out.println("Buffer empty, consumer waiting...");
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            int value = queue.poll();
            System.out.println("Consumed: " + value);
            notify(); // Wake up producer
        }
    }
}
```

---

### 🔍 Output Sample

```plaintext
Produced: 1
Consumed: 1
Produced: 2
Consumed: 2
Produced: 3
...
```

---

### 🔄 What’s Happening:

* `wait()` is called when:

  * Producer sees buffer full.
  * Consumer sees buffer empty.
* `notify()` is called after:

  * Producer adds an item.
  * Consumer removes an item.
* `synchronized` ensures only one thread accesses the buffer at a time.
* `wait()` releases the lock and pauses the thread.
* `notify()` wakes a waiting thread (doesn’t release the lock immediately!).

---

### ✅ Key Concepts in Action

| Concept        | Where it's used                      |
| -------------- | ------------------------------------ |
| `wait()`       | Inside `produce()` and `consume()`   |
| `notify()`     | After producing or consuming an item |
| `synchronized` | On all buffer methods                |

---

Let me know if you'd like to extend this to a **`notifyAll()`**, **blocking queue**, or **semaphore-based** version!

<br/><br/>
<br/>

## Thread creation in various ways 

## 1. Extending Thread Class

### Creation Method
```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running: " + Thread.currentThread().getName());
        // Your thread logic here
    }
}
```

### Ways to Start/Run
```java
public class ThreadExample {
    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        
        // ✅ CORRECT WAY - Creates new thread
        t1.start();
        
        // ❌ WRONG WAY - Runs in main thread (no new thread created)
        // t1.run();
    }
}
```

### Notes
- **Advantage**: Simple and direct approach
- **Disadvantage**: Java doesn't support multiple inheritance, so you can't extend another class
- **When to use**: Simple threading scenarios where you don't need to extend another class

---

## 2. Implementing Runnable Interface

### Creation Method
```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable running: " + Thread.currentThread().getName());
        // Your thread logic here
    }
}
```

### Ways to Start/Run
```java
public class RunnableExample {
    public static void main(String[] args) {
        MyRunnable r = new MyRunnable();
        
        // ✅ CORRECT WAY - Creates new thread
        Thread t1 = new Thread(r);
        t1.start();
        
        // Alternative one-liner
        new Thread(new MyRunnable()).start();
        
        // ❌ WRONG WAYS - No new thread created
        // r.run(); // Runs in main thread
        // new Thread(r).run(); // Still runs in main thread
    }
}
```

### Notes
- **Advantage**: Better design - allows implementing multiple interfaces and extending other classes
- **Disadvantage**: Slightly more verbose than extending Thread
- **When to use**: Most common and recommended approach for thread creation

---

## 3. Using Lambda Expressions (Java 8+)

### Creation and Start
```java
public class LambdaThreadExample {
    public static void main(String[] args) {
        // Method 1: Create Runnable first
        Runnable r = () -> {
            System.out.println("Lambda thread: " + Thread.currentThread().getName());
        };
        new Thread(r).start();
        
        // Method 2: Inline lambda
        new Thread(() -> {
            System.out.println("Inline lambda: " + Thread.currentThread().getName());
            // Multiple statements allowed
            for (int i = 0; i < 3; i++) {
                System.out.println("Count: " + i);
            }
        }).start();
        
        // Method 3: Single statement lambda
        new Thread(() -> System.out.println("Single line lambda")).start();
    }
}
```

### Notes
- **Advantage**: Clean, concise syntax; functional programming style
- **Disadvantage**: Less readable for complex logic
- **When to use**: Simple threading tasks, modern Java applications

---

## 4. Using ExecutorService (Thread Pool)

### Creation and Execution
```java
import java.util.concurrent.*;

public class ExecutorExample {
    public static void main(String[] args) {
        // Fixed thread pool
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Submit Runnable tasks
        executor.submit(() -> {
            System.out.println("Task 1: " + Thread.currentThread().getName());
        });
        
        executor.submit(() -> {
            System.out.println("Task 2: " + Thread.currentThread().getName());
        });
        
        // Execute method (fire and forget)
        executor.execute(() -> {
            System.out.println("Task 3: " + Thread.currentThread().getName());
        });
        
        executor.shutdown(); // Important: Always shutdown
    }
}
```

### Different ExecutorService Types
```java
// Single thread executor
ExecutorService single = Executors.newSingleThreadExecutor();

// Cached thread pool (creates threads as needed)
ExecutorService cached = Executors.newCachedThreadPool();

// Scheduled thread pool
ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);
```

### Notes
- **Advantage**: Better resource management, thread reuse, easier task management
- **Disadvantage**: More complex than basic thread creation
- **When to use**: Production applications, multiple tasks, performance-critical applications

---

## 5. Using Callable and Future

### Creation and Execution
```java
import java.util.concurrent.*;

public class CallableExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        // Callable that returns a value
        Callable<String> task = () -> {
            Thread.sleep(2000); // Simulate work
            return "Result from: " + Thread.currentThread().getName();
        };
        
        // Submit and get Future
        Future<String> future = executor.submit(task);
        
        System.out.println("Task submitted...");
        
        // Get result (blocks until complete)
        String result = future.get();
        System.out.println("Result: " + result);
        
        executor.shutdown();
    }
}
```

### Advanced Callable Example
```java
public class AdvancedCallableExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // Multiple Callable tasks
        Callable<Integer> task1 = () -> {
            Thread.sleep(1000);
            return 42;
        };
        
        Callable<String> task2 = () -> {
            Thread.sleep(1500);
            return "Hello World";
        };
        
        // Submit both tasks
        Future<Integer> future1 = executor.submit(task1);
        Future<String> future2 = executor.submit(task2);
        
        // Get results
        Integer result1 = future1.get();
        String result2 = future2.get();
        
        System.out.println("Results: " + result1 + ", " + result2);
        executor.shutdown();
    }
}
```

### Notes
- **Advantage**: Can return values, exception handling, cancellation support
- **Disadvantage**: More complex than Runnable
- **When to use**: When you need return values from threads or exception handling

---

## 6. Anonymous Inner Classes

### Creation and Start
```java
public class AnonymousThreadExample {
    public static void main(String[] args) {
        // Anonymous Thread class
        Thread t1 = new Thread() {
            @Override
            public void run() {
                System.out.println("Anonymous Thread: " + Thread.currentThread().getName());
            }
        };
        t1.start();
        
        // Anonymous Runnable
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Anonymous Runnable: " + Thread.currentThread().getName());
            }
        });
        t2.start();
    }
}
```

### Notes
- **Advantage**: Quick for simple, one-off threads
- **Disadvantage**: Not reusable, can be verbose
- **When to use**: Simple, one-time threading tasks

---

## 7. CompletableFuture (Java 8+)

### Creation and Execution
```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureExample {
    public static void main(String[] args) throws Exception {
        // Async execution with no return value
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println("Async task: " + Thread.currentThread().getName());
        });
        
        // Async execution with return value
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            return "Result from: " + Thread.currentThread().getName();
        });
        
        // Chain operations
        CompletableFuture<String> chainedFuture = future2.thenApply(result -> {
            return result.toUpperCase();
        });
        
        // Get results
        future1.get(); // Wait for completion
        String result = chainedFuture.get();
        System.out.println("Final result: " + result);
    }
}
```

### Notes
- **Advantage**: Excellent for asynchronous programming, chaining operations
- **Disadvantage**: Complex for simple tasks
- **When to use**: Complex asynchronous workflows, reactive programming

---

## Summary Table

| Method | New Thread? | Return Value? | Inheritance Impact | Best Use Case |
|--------|-------------|---------------|-------------------|---------------|
| `extends Thread` | ✅ Yes | ❌ No | Can't extend other classes | Simple tasks |
| `implements Runnable` | ✅ Yes | ❌ No | Can extend other classes | Most common approach |
| Lambda Expression | ✅ Yes | ❌ No | No impact | Modern, concise code |
| ExecutorService | ✅ Yes | ✅ Yes (with Callable) | No impact | Production applications |
| Callable + Future | ✅ Yes | ✅ Yes | No impact | Tasks needing results |
| CompletableFuture | ✅ Yes | ✅ Yes | No impact | Complex async workflows |

## Thread Start Methods Comparison

| Method | Creates New Thread? | Notes |
|--------|-------------------|-------|
| `thread.start()` | ✅ Yes | **Correct way** - Creates new thread |
| `thread.run()` | ❌ No | **Wrong way** - Runs in current thread |
| `executor.submit()` | ✅ Yes | **Preferred** - Managed thread pool |
| `executor.execute()` | ✅ Yes | **Fire and forget** - No return value |

## Best Practices

1. **Always use `start()` method** to create new threads, never call `run()` directly
2. **Prefer `Runnable` over `Thread`** for better design flexibility
3. **Use `ExecutorService`** for production applications
4. **Always shutdown ExecutorService** to prevent resource leaks
5. **Use `Callable` and `Future`** when you need return values
6. **Consider `CompletableFuture`** for complex asynchronous operations
7. **Handle exceptions properly** in thread code
8. **Use thread-safe collections** when sharing data between threads