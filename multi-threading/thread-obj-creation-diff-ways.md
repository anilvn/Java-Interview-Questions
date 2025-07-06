# Complete Guide to Thread Creation in Java

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