# ExecutorService in Java - Complete Notes

## Overview

ExecutorService is a high-level interface in Java's `java.util.concurrent` package that provides a framework for managing and controlling thread execution. It extends the `Executor` interface and provides methods for managing termination and producing `Future` objects for tracking progress of asynchronous tasks.

## Key Benefits

- **Thread Pool Management**: Manages a pool of worker threads efficiently
- **Task Submission**: Provides methods to submit tasks for execution
- **Lifecycle Management**: Controls the lifecycle of threads
- **Future Support**: Returns Future objects to track task completion
- **Resource Management**: Helps prevent resource leaks

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

### Lifecycle Management

```java
// Shutdown gracefully
void shutdown()

// Shutdown immediately
List<Runnable> shutdownNow()

// Check if shutdown was called
boolean isShutdown()

// Check if all tasks completed after shutdown
boolean isTerminated()

// Wait for termination
boolean awaitTermination(long timeout, TimeUnit unit)
```

## Common Implementations

### 1. ThreadPoolExecutor

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

### 2. Factory Methods (Executors Class)

#### Fixed Thread Pool
```java
ExecutorService executor = Executors.newFixedThreadPool(4);
// Creates a pool with fixed number of threads
// Good for: Known workload with predictable thread requirements
```

#### Cached Thread Pool
```java
ExecutorService executor = Executors.newCachedThreadPool();
// Creates threads as needed, reuses existing threads
// Good for: Short-lived asynchronous tasks
```

#### Single Thread Executor
```java
ExecutorService executor = Executors.newSingleThreadExecutor();
// Uses a single worker thread
// Good for: Sequential task execution
```

#### Scheduled Thread Pool
```java
ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
// For scheduling tasks with delays or periodic execution
```

## Working with Future Objects

### Basic Future Operations

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

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return "Hello World";
}, executor);

// Chain operations
future.thenApply(result -> result.toUpperCase())
      .thenAccept(System.out::println);
```

## Best Practices

### 1. Always Shutdown ExecutorService

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

### 2. Use Try-with-Resources (Java 19+)

```java
try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
    // Submit tasks
    executor.submit(() -> {
        // Task logic
    });
    // Automatic shutdown when leaving try block
}
```

### 3. Handle Exceptions Properly

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
```

## Common Patterns

### 1. Producer-Consumer with ExecutorService

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

### 2. Parallel Processing

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

### 3. Timeout Handling

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

## Thread Pool Configuration Guidelines

### Choosing Pool Size

#### CPU-Intensive Tasks
```java
int coreCount = Runtime.getRuntime().availableProcessors();
ExecutorService executor = Executors.newFixedThreadPool(coreCount);
```

#### I/O-Intensive Tasks
```java
int poolSize = Runtime.getRuntime().availableProcessors() * 2;
ExecutorService executor = Executors.newFixedThreadPool(poolSize);
```

#### Custom Configuration
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

## Exception Handling Strategies

### 1. UncaughtExceptionHandler

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

### 2. Wrapping Tasks

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

## Performance Considerations

### 1. Queue Types Impact

- **LinkedBlockingQueue**: Unbounded, can cause memory issues
- **ArrayBlockingQueue**: Bounded, blocks when full
- **SynchronousQueue**: Direct handoff, no storage
- **PriorityBlockingQueue**: Task prioritization

### 2. Rejection Policies

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

## Testing ExecutorService Code

### 1. Unit Testing with CountDownLatch

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

### 2. Using CompletableFuture for Testing

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

## Summary

ExecutorService provides a powerful abstraction for managing concurrent execution in Java. Key takeaways:

- Always properly shutdown ExecutorService to prevent resource leaks
- Choose appropriate implementation based on your use case
- Handle exceptions and interruptions properly
- Configure thread pools based on workload characteristics
- Use Future objects to handle asynchronous results
- Consider using CompletableFuture for more complex async operations
- Test concurrent code thoroughly with appropriate synchronization primitives

The ExecutorService framework is essential for building robust, scalable concurrent applications in Java.