# Java ExecutorService - Comprehensive Notes

## Table of Contents
1. [ExecutorService Overview](#executorservice-overview)
2. [Core Interface Methods](#core-interface-methods)
3. [Future and Task Handling](#future-and-task-handling)
4. [Thread Pool Implementations](#thread-pool-implementations)
5. [Executors Utility Class](#executors-utility-class)
6. [ForkJoinPool and Work Stealing](#forkjoinpool-and-work-stealing)
7. [ScheduledThreadPoolExecutor](#scheduledthreadpoolexecutor)
8. [Lifecycle Management](#lifecycle-management)
9. [Best Practices and Common Patterns](#best-practices-and-common-patterns)
10. [Performance Considerations](#performance-considerations)
11. [Common Pitfalls](#common-pitfalls)
12. [Testing ExecutorService](#testing-executorservice)

---

## ExecutorService Overview

ExecutorService is a high-level interface in Java's `java.util.concurrent` package that provides a framework for managing and controlling thread execution. It extends the `Executor` interface and provides methods for managing termination and producing `Future` objects for tracking progress of asynchronous tasks.

### Key Benefits
- **Thread Pool Management**: Manages a pool of worker threads efficiently
- **Task Submission**: Provides methods to submit tasks for execution
- **Lifecycle Management**: Controls the lifecycle of threads
- **Future Support**: Returns Future objects to track task completion
- **Resource Management**: Helps prevent resource leaks

---

## Core Interface Methods

### Task Submission Methods

```java
// Submit a Callable task
<T> Future<T> submit(Callable<T> task)

// Submit a Runnable task
Future<?> submit(Runnable task)

// Submit a Runnable with a specific result
<T> Future<T> submit(Runnable task, T result)

// Execute a Runnable (inherited from Executor)
void execute(Runnable command)
```

### Bulk Task Execution

```java
// Execute all tasks and return when all complete
<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)

// Execute all tasks with timeout
<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, 
                              long timeout, TimeUnit unit)

// Execute tasks and return result of first completed task
<T> T invokeAny(Collection<? extends Callable<T>> tasks)

// Execute tasks with timeout and return first completed result
<T> T invokeAny(Collection<? extends Callable<T>> tasks, 
                long timeout, TimeUnit unit)
```

---

## Future and Task Handling

### Callable vs Runnable

| Aspect      | Runnable                        | Callable                     |
|-------------|--------------------------------|------------------------------|
| Return type | void                           | V (generic)                  |
| Throws      | Cannot throw checked exceptions | Can throw checked exceptions |
| Method      | run()                          | call()                       |

### Future Methods

| Method                                          | Purpose                                                             |
|------------------------------------------------|---------------------------------------------------------------------|
| `boolean cancel(boolean mayInterruptIfRunning)` | Tries to cancel; returns false if task already completed            |
| `boolean isCancelled()`                         | true if task was cancelled before completion                        |
| `boolean isDone()`                              | true if task finished (success, exception, or cancelled)            |
| `V get()`                                       | Waits if needed, then returns result                                |
| `V get(long timeout, TimeUnit unit)`            | Waits up to timeout; throws `TimeoutException` if task not finished |

### Task Submission Examples

#### UseCase 1: Runnable (no result)
```java
Future<?> f1 = executor.submit(() -> System.out.println("Task1"));
// f1.get() returns null because Runnable doesn't return a value
```

#### UseCase 2: Runnable with return value
```java
List<Integer> output = new ArrayList<>();
Future<List<Integer>> f2 = executor.submit(() -> {
    output.add(100);
}, output);
List<Integer> result = f2.get();
System.out.println(result.get(0));  // prints 100
```

#### UseCase 3: Callable
```java
Future<List<Integer>> f3 = executor.submit(() -> {
    List<Integer> list = new ArrayList<>();
    list.add(200);
    return list;
});
List<Integer> result = f3.get();
System.out.println(result.get(0));  // prints 200
```

### Working with Future Objects

```java
Future<String> future = executor.submit(() -> {
    Thread.sleep(1000);
    return "Task completed";
});

// Check if task is done
if (future.isDone()) {
    System.out.println("Task completed");
}

// Get result (blocks until completion)
String result = future.get();

// Get result with timeout
try {
    String result = future.get(5, TimeUnit.SECONDS);
} catch (TimeoutException e) {
    System.out.println("Task timed out");
}

// Cancel task
boolean cancelled = future.cancel(true); // true = interrupt if running
```

### CompletableFuture (Java 8+)

CompletableFuture provides more powerful asynchronous programming capabilities:

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return "Hello World";
}, executor);

// Chain operations
future.thenApply(result -> result.toUpperCase())
      .thenAccept(System.out::println);
```

#### CompletableFuture Methods Comparison

| Method | Purpose | Synchronous/Asynchronous |
|--------|---------|-------------------------|
| `thenApply` | Transform result | Synchronous (same thread) |
| `thenApplyAsync` | Transform result | Asynchronous (different thread) |
| `thenCompose` | Chain dependent async tasks | Synchronous |
| `thenComposeAsync` | Chain dependent async tasks | Asynchronous |
| `thenAccept` | Consume result (void) | Synchronous |
| `thenAcceptAsync` | Consume result (void) | Asynchronous |
| `thenCombine` | Combine two futures | Synchronous |
| `thenCombineAsync` | Combine two futures | Asynchronous |

#### CompletableFuture Examples

**thenApply vs thenApplyAsync:**
```java
CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
    System.out.println("supplyAsync thread: " + Thread.currentThread().getName());
    return "Concept and ";
}, executor).thenApply(val -> {
    System.out.println("thenApply thread: " + Thread.currentThread().getName());
    return val + "Coding";
});
```

**thenCompose for dependent tasks:**
```java
CompletableFuture<String> cf = CompletableFuture
    .supplyAsync(() -> "Concept and ", executor)
    .thenCompose(val -> CompletableFuture.supplyAsync(() -> val + "Coding"));
```

**thenCombine for combining results:**
```java
CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> 10, executor);
CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> "k ", executor);

CompletableFuture<String> combined = cf1.thenCombine(cf2, (num, str) -> num + str);
System.out.println(combined.get()); // prints "10k "
```

---

## Thread Pool Implementations

### ThreadPoolExecutor

The most flexible implementation with configurable parameters:

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    corePoolSize,     // minimum number of threads
    maximumPoolSize,  // maximum number of threads
    keepAliveTime,    // idle thread timeout
    TimeUnit.SECONDS, // time unit for keepAliveTime
    new LinkedBlockingQueue<>(), // work queue
    Executors.defaultThreadFactory(), // thread factory
    new ThreadPoolExecutor.AbortPolicy() // rejection policy
);
```

### Thread Pool Configuration Guidelines

#### Choosing Pool Size

**CPU-Intensive Tasks:**
```java
int coreCount = Runtime.getRuntime().availableProcessors();
ExecutorService executor = Executors.newFixedThreadPool(coreCount);
```

**I/O-Intensive Tasks:**
```java
int poolSize = Runtime.getRuntime().availableProcessors() * 2;
ExecutorService executor = Executors.newFixedThreadPool(poolSize);
```

---

## Executors Utility Class

The `Executors` class provides factory methods for common thread pool configurations:

### Thread Pool Types Comparison

| Type | Factory Method | Min/Max Pool Size | Queue Type | Keep Alive | Best For | Disadvantages |
|------|---------------|-------------------|------------|------------|----------|---------------|
| **Fixed** | `newFixedThreadPool(n)` | Both = n | Unbounded LinkedBlockingQueue | Yes | Known, limited concurrent tasks | Limited concurrency under load |
| **Cached** | `newCachedThreadPool()` | 0 / Integer.MAX_VALUE | SynchronousQueue | 60 seconds | Short-lived, bursty tasks | Risk of too many threads |
| **Single** | `newSingleThreadExecutor()` | Always 1 | Unbounded LinkedBlockingQueue | Yes | Sequential processing | No concurrency |
| **WorkStealing** | `newWorkStealingPool()` | CPU cores (default) | Submission + Deques | N/A | Parallel recursive tasks | Complex, not for unrelated tasks |

### Fixed Thread Pool
```java
ExecutorService fixedPool = Executors.newFixedThreadPool(5);
fixedPool.submit(() -> System.out.println("Fixed pool task"));
```

### Cached Thread Pool
```java
ExecutorService cachedPool = Executors.newCachedThreadPool();
cachedPool.submit(() -> System.out.println("Cached pool task"));
```

### Single Thread Executor
```java
ExecutorService singleThread = Executors.newSingleThreadExecutor();
singleThread.submit(() -> System.out.println("Single thread task"));
```

---

## ForkJoinPool and Work Stealing

### Overview

ForkJoinPool is designed for work that can be broken into smaller tasks recursively (divide and conquer approach).

### Key Concepts

- **Fork**: Split task into subtasks
- **Join**: Wait for subtask completion and combine results
- **Work Stealing**: Idle threads steal work from busy threads' queues

### Work Stealing Flow

1. Submit big task → goes to Submission Queue
2. Thread picks the task
3. Task divided into subtasks using `.fork()`
   - One subtask stays with current thread
   - Other subtasks put into that thread's work stealing queue (deque)
4. Free threads check:
   - Their own work stealing queue
   - Submission queue
   - Try to steal from other threads' queues
5. Use `.join()` to wait and combine results
6. Default threads = available CPU cores (`Runtime.getRuntime().availableProcessors()`)
7. Can set fixed number of threads with:

  ```java
  new ForkJoinPool(int parallelism)
  ```

### ForkJoinPool Example

```java
import java.util.concurrent.*;

public class ForkJoinSumExample {
    public static void main(String[] args) throws Exception {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        Future<Integer> result = pool.submit(new ComputeSumTask(0, 100));
        System.out.println("Total Sum: " + result.get());
    }
}

class ComputeSumTask extends RecursiveTask<Integer> {
    int start, end;
    ComputeSumTask(int start, int end) {
        this.start = start; this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= 4) {
            int sum = 0;
            for (int i = start; i < end; i++) sum += i;
            return sum;
        } else {
            int mid = (start + end) / 2;
            ComputeSumTask left = new ComputeSumTask(start, mid);
            ComputeSumTask right = new ComputeSumTask(mid, end);
            left.fork(); // execute left in parallel
            int rightResult = right.compute(); // execute right directly
            int leftResult = left.join(); // wait for left
            return leftResult + rightResult;
        }
    }
}
```

### RecursiveTask vs RecursiveAction

| Class | Purpose | Usage |
|-------|---------|-------|
| `RecursiveTask<T>` | Returns a value | Use when subtasks return results |
| `RecursiveAction` | No return value | Use when subtasks perform actions only |

---
##  **Work stealing queue priority:**

- Check own work stealing queue
- Check submission queue
- Try to steal from other threads’ work stealing queues (from back)

---

##  **Custom vs Utility:**

| Custom ThreadPoolExecutor               | Executors utility methods             |
| --------------------------------------- | ------------------------------------- |
| Full control (core, max, queue, policy) | Simple, quick to use                  |
| Use when you need special configs       | Use when standard pools fit your need |

---

## **Final quick tips:**

* Fixed thread pool: stable, known concurrent tasks
* Cached thread pool: bursty, short-lived tasks
* Single thread: sequential execution
* Work stealing / ForkJoin: recursive, divide & conquer problems

## ScheduledThreadPoolExecutor

Used for scheduling tasks with delays or at fixed intervals.

### Factory Method
```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
```

### Scheduling Methods

| Method | Purpose | Execution Pattern |
|--------|---------|-------------------|
| `schedule(Runnable/Callable, delay, unit)` | Execute once after delay | One-time execution |
| `scheduleAtFixedRate(task, initialDelay, period, unit)` | Execute at fixed intervals | Based on start time |
| `scheduleWithFixedDelay(task, initialDelay, delay, unit)` | Execute with delay after completion | Based on completion time |

### scheduleAtFixedRate vs scheduleWithFixedDelay

| Type | Next Task Starts | Behavior if Task Takes Long |
|------|------------------|----------------------------|
| `scheduleAtFixedRate` | At fixed intervals from start | Next task waits in queue |
| `scheduleWithFixedDelay` | After previous task completes + delay | Always waits for delay after completion |

### Examples

**Schedule once:**
```java
scheduler.schedule(() -> System.out.println("Hello after 3s"), 3, TimeUnit.SECONDS);
```

**Fixed rate:**
```java
ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
    System.out.println("Task at fixed rate: " + Thread.currentThread().getName());
}, 1, 3, TimeUnit.SECONDS);

// Cancel after some time
Thread.sleep(10000);
future.cancel(true);
```

**Fixed delay:**
```java
scheduler.scheduleWithFixedDelay(() -> {
    System.out.println("Fixed delay task: " + Thread.currentThread().getName());
}, 1, 3, TimeUnit.SECONDS);
```

---

## Lifecycle Management

### Shutdown Methods Comparison

| Method                                          | Purpose                                         | Effect on Running Tasks | Effect on Queued Tasks | Return Type    |
| ----------------------------------------------- | ----------------------------------------------- | ----------------------- | ---------------------- | -------------- |
| `shutdown()`                                    | Shutdown gracefully                             | Continue execution      | Will be processed      | void           |
| `shutdownNow()`                                 | Shutdown immediately (forceful shutdown)        | Attempts to interrupt   | Returned as list       | List<Runnable> |
| `isShutdown()`                                  | Check if shutdown was called                    | No effect               | No effect              | boolean        |
| `isTerminated()`                                | Check if all tasks completed after shutdown     | No effect               | No effect              | boolean        |
| `awaitTermination(long timeout, TimeUnit unit)` | Wait for termination to complete within timeout | No effect               | No effect              | boolean        |


### Proper Shutdown Pattern

```java
ExecutorService executor = Executors.newFixedThreadPool(4);
try {
    // Submit tasks
    executor.submit(() -> {
        // Task logic
    });
} finally {
    // Proper shutdown
    executor.shutdown();
    try {
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            executor.shutdownNow();
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Executor did not terminate");
            }
        }
    } catch (InterruptedException e) {
        executor.shutdownNow();
        Thread.currentThread().interrupt();
    }
}
```

### Try-with-Resources (Java 19+)

```java
try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
    // Submit tasks
    executor.submit(() -> {
        // Task logic
    });
    // Automatic shutdown when leaving try block
}
```

### Shutdown Behavior Examples

**Task submission after shutdown:**
```java
ExecutorService pool = Executors.newFixedThreadPool(5);
pool.submit(() -> System.out.println("Task1 running"));

pool.shutdown();
pool.submit(() -> System.out.println("Task2 running")); // Throws RejectedExecutionException
```

**awaitTermination usage:**
```java
ExecutorService pool = Executors.newFixedThreadPool(5);
pool.submit(() -> {
    try {
        Thread.sleep(6000);
    } catch (InterruptedException e) {}
    System.out.println("Task completed");
});

pool.shutdown();
boolean terminated = pool.awaitTermination(3, TimeUnit.SECONDS);
System.out.println("Is executor terminated: " + terminated); // false if task takes > 3s
```

---

## Best Practices and Common Patterns

### 1. Always Shutdown ExecutorService

```java
ExecutorService executor = Executors.newFixedThreadPool(4);
try {
    // Submit tasks
} finally {
    executor.shutdown();
}
```

### 2. Handle Exceptions Properly

```java
Future<String> future = executor.submit(() -> {
    if (Math.random() > 0.5) {
        throw new RuntimeException("Random failure");
    }
    return "Success";
});

try {
    String result = future.get();
    System.out.println(result);
} catch (ExecutionException e) {
    Throwable cause = e.getCause();
    System.err.println("Task failed: " + cause.getMessage());
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    System.err.println("Task interrupted");
}

// Use Try-with-Resources (Java 19+)
try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
    // Submit tasks
    executor.submit(() -> {
        // Task logic
    });
    // Automatic shutdown when leaving try block
}
```

### 3. Producer-Consumer Pattern

```java
ExecutorService executor = Executors.newFixedThreadPool(4);
BlockingQueue<String> queue = new LinkedBlockingQueue<>();

// Producer
executor.submit(() -> {
    for (int i = 0; i < 10; i++) {
        queue.offer("Item " + i);
    }
});

// Consumer
executor.submit(() -> {
    while (true) {
        String item = queue.poll(1, TimeUnit.SECONDS);
        if (item != null) {
            System.out.println("Processing: " + item);
        }
    }
});
```

### 4. Parallel Processing

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
ExecutorService executor = Executors.newFixedThreadPool(4);

List<Future<Integer>> futures = numbers.stream()
    .map(num -> executor.submit(() -> num * num))
    .collect(Collectors.toList());

// Collect results
List<Integer> results = futures.stream()
    .map(future -> {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    })
    .collect(Collectors.toList());
```

### 5. Timeout Handling

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<String> future = executor.submit(() -> {
    Thread.sleep(5000); // Simulate long-running task
    return "Completed";
});

try {
    String result = future.get(2, TimeUnit.SECONDS);
    System.out.println(result);
} catch (TimeoutException e) {
    System.out.println("Task timed out, cancelling...");
    future.cancel(true);
} catch (Exception e) {
    System.err.println("Task failed: " + e.getMessage());
}
```

---

## Performance Considerations

### Queue Types Impact

| Queue Type | Characteristics | Use Case |
|------------|----------------|----------|
| `LinkedBlockingQueue` | Unbounded, can cause memory issues | When you need unlimited capacity |
| `ArrayBlockingQueue` | Bounded, blocks when full | When you want to limit memory usage |
| `SynchronousQueue` | Direct handoff, no storage | For cached thread pools |
| `PriorityBlockingQueue` | Task prioritization | When tasks have different priorities |

### Rejection Policies

```java
// Abort Policy (default) - throws RejectedExecutionException
new ThreadPoolExecutor.AbortPolicy()

// Caller Runs Policy - runs task in calling thread
new ThreadPoolExecutor.CallerRunsPolicy()

// Discard Policy - silently discards task
new ThreadPoolExecutor.DiscardPolicy()

// Discard Oldest Policy - discards oldest unhandled task
new ThreadPoolExecutor.DiscardOldestPolicy()
```

### Custom Thread Pool Configuration

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    2,                          // corePoolSize
    10,                         // maximumPoolSize
    60L,                        // keepAliveTime
    TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(100), // bounded queue
    r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("CustomWorker-" + t.getId());
        return t;
    }
);
```

---

## Common Pitfalls

### 1. Forgetting to Shutdown
```java
// BAD - Resource leak
ExecutorService executor = Executors.newFixedThreadPool(4);
executor.submit(() -> System.out.println("Task"));
// Missing shutdown - threads remain alive

// GOOD
ExecutorService executor = Executors.newFixedThreadPool(4);
try {
    executor.submit(() -> System.out.println("Task"));
} finally {
    executor.shutdown();
}
```

### 2. Ignoring InterruptedException
```java
// BAD
try {
    result = future.get();
} catch (InterruptedException e) {
    // Ignoring interruption
}

// GOOD
try {
    result = future.get();
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // Restore interrupt status
    throw new RuntimeException("Task interrupted", e);
}
```

### 3. Unbounded Queues
```java
// BAD - Can cause OutOfMemoryError
ExecutorService executor = new ThreadPoolExecutor(
    1, 1, 0L, TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue<>() // Unbounded
);

// GOOD - Bounded queue with proper rejection handling
ExecutorService executor = new ThreadPoolExecutor(
    1, 1, 0L, TimeUnit.MILLISECONDS,
    new ArrayBlockingQueue<>(100), // Bounded
    new ThreadPoolExecutor.CallerRunsPolicy()
);
```

### 4. Exception Handling Strategies

**UncaughtExceptionHandler:**
```java
ThreadFactory factory = r -> {
    Thread t = new Thread(r);
    t.setUncaughtExceptionHandler((thread, exception) -> {
        System.err.println("Uncaught exception in thread " + 
                          thread.getName() + ": " + exception.getMessage());
    });
    return t;
};

ExecutorService executor = Executors.newFixedThreadPool(4, factory);
```

**Wrapping Tasks:**
```java
public class SafeRunnable implements Runnable {
    private final Runnable task;
    
    public SafeRunnable(Runnable task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        try {
            task.run();
        } catch (Exception e) {
            System.err.println("Task failed: " + e.getMessage());
            // Log exception, send alerts, etc.
        }
    }
}

// Usage
executor.submit(new SafeRunnable(() -> {
    // Your task logic that might throw exceptions
}));
```

---

## Testing ExecutorService

### Unit Testing with CountDownLatch

```java
@Test
public void testConcurrentExecution() throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(2);
    CountDownLatch latch = new CountDownLatch(2);
    AtomicInteger counter = new AtomicInteger(0);
    
    executor.submit(() -> {
        counter.incrementAndGet();
        latch.countDown();
    });
    
    executor.submit(() -> {
        counter.incrementAndGet();
        latch.countDown();
    });
    
    latch.await(5, TimeUnit.SECONDS);
    assertEquals(2, counter.get());
    
    executor.shutdown();
}
```

### Using CompletableFuture for Testing

```java
@Test
public void testAsyncOperation() {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    
    CompletableFuture<String> future = CompletableFuture
        .supplyAsync(() -> "Hello", executor)
        .thenApply(s -> s + " World");
    
    assertEquals("Hello World", future.join());
    executor.shutdown();
}
```

---

## Summary

ExecutorService provides a powerful abstraction for managing concurrent execution in Java. Key takeaways:

### Quick Reference for Interviews

**Most Common Questions:**

1. **What's the difference between shutdown() and shutdownNow()?**
   - `shutdown()`: graceful, lets tasks complete
   - `shutdownNow()`: forceful, tries to interrupt

2. **Does awaitTermination() stop the executor?**
   - No! It only waits for shutdown to complete

3. **Difference between scheduleAtFixedRate and scheduleWithFixedDelay?**
   - `FixedRate`: next task runs based on clock
   - `FixedDelay`: next task runs after previous finishes + delay

4. **When to use which thread pool?**
   - `FixedThreadPool`: Known workload, predictable requirements
   - `CachedThreadPool`: Short-lived, bursty tasks
   - `SingleThreadExecutor`: Sequential execution required
   - `WorkStealingPool`: Parallel, recursive, divide-and-conquer problems

5. **Future vs CompletableFuture?**
   - `Future`: Basic async result handling
   - `CompletableFuture`: Advanced chaining, composition, and async operations

### Essential Best Practices

- Always properly shutdown ExecutorService to prevent resource leaks
- Choose appropriate implementation based on your use case
- Handle exceptions and interruptions properly
- Configure thread pools based on workload characteristics (CPU vs I/O intensive)
- Use Future/CompletableFuture objects to handle asynchronous results
- Test concurrent code thoroughly with appropriate synchronization primitives

The ExecutorService framework is essential for building robust, scalable concurrent applications in Java.