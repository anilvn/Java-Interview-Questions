# Java Multithreading Exceptions - Simplified Notes

## Overview

When working with multithreading in Java, you'll encounter various exceptions. Understanding these exceptions helps you write robust concurrent applications and handle errors properly.

## Exception Categories

### 1. Thread Lifecycle Exceptions

#### InterruptedException (Checked)
- **When**: Thread is waiting, sleeping, or joining and gets interrupted
- **Common scenarios**: `Thread.sleep()`, `Object.wait()`, `Future.get()`
- **Must handle**: Yes, it's a checked exception

```java
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // Restore interrupt flag
    return; // Exit gracefully
}
```

#### IllegalThreadStateException (Unchecked)
- **When**: Starting a thread more than once
- **Prevention**: Check thread state before starting

```java
Thread t = new Thread(() -> System.out.println("Running"));
t.start();
// t.start(); // Would throw IllegalThreadStateException
```

#### IllegalMonitorStateException (Unchecked)
- **When**: Calling `wait()`, `notify()`, `notifyAll()` without owning the lock
- **Fix**: Always use these methods inside synchronized blocks

```java
synchronized(obj) {
    obj.wait(); // Correct - we own the monitor
}
// obj.wait(); // Wrong - would throw IllegalMonitorStateException
```

### 2. ExecutorService & Future Exceptions

#### ExecutionException (Checked)
- **When**: Task submitted to executor throws an exception
- **Handling**: Unwrap the cause to see the original exception

```java
Future<String> future = executor.submit(() -> {
    throw new RuntimeException("Task failed");
});

try {
    String result = future.get();
} catch (ExecutionException e) {
    Throwable cause = e.getCause(); // Get original exception
    System.out.println("Task failed: " + cause.getMessage());
}
```

#### TimeoutException (Checked)
- **When**: `Future.get(timeout, unit)` exceeds wait time
- **Handling**: Decide whether to cancel or wait longer

```java
try {
    String result = future.get(5, TimeUnit.SECONDS);
} catch (TimeoutException e) {
    System.out.println("Task timed out");
    future.cancel(true); // Cancel the task
}
```

#### RejectedExecutionException (Unchecked)
- **When**: Task rejected due to executor shutdown or thread pool saturation
- **Prevention**: Proper shutdown sequence and appropriate rejection policies

```java
ExecutorService executor = Executors.newFixedThreadPool(2);
executor.shutdown(); // Shutdown executor
executor.submit(() -> System.out.println("Hi")); // Throws RejectedExecutionException
```

### 3. CompletableFuture Exceptions

#### CompletionException (Unchecked)
- **When**: Async task in CompletableFuture chain fails
- **Handling**: Use `handle()` or `exceptionally()` methods

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> {
        throw new RuntimeException("Failed");
    })
    .exceptionally(throwable -> {
        System.out.println("Handled: " + throwable.getMessage());
        return "Default value";
    });
```

#### CancellationException (Unchecked)
- **When**: Accessing result of a cancelled Future
- **Check**: Use `isCancelled()` before getting result

```java
Future<String> future = executor.submit(() -> "Hello");
future.cancel(true);

if (!future.isCancelled()) {
    String result = future.get(); // Safe to call
}
```

### 4. Concurrency Utilities Exceptions

#### BrokenBarrierException (Checked)
- **When**: CyclicBarrier is broken or reset while threads are waiting
- **Handling**: Usually indicates a serious synchronization problem

```java
CyclicBarrier barrier = new CyclicBarrier(3);
try {
    barrier.await();
} catch (BrokenBarrierException e) {
    System.out.println("Barrier was broken");
}
```

## Common Patterns and Solutions

### 1. Proper InterruptedException Handling

```java
// WRONG - Swallowing the exception
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // Ignoring - BAD!
}

// CORRECT - Restore interrupt status
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // Restore flag
    System.out.println("Thread was interrupted");
    return; // Exit method gracefully
}
```

### 2. Handling ExecutorService Exceptions

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

// Submit task and handle exceptions
Future<String> future = executor.submit(() -> {
    if (Math.random() > 0.5) {
        throw new RuntimeException("Random failure");
    }
    return "Success";
});

try {
    // Get result with timeout
    String result = future.get(10, TimeUnit.SECONDS);
    System.out.println("Result: " + result);
    
} catch (ExecutionException e) {
    System.out.println("Task failed: " + e.getCause().getMessage());
    
} catch (TimeoutException e) {
    System.out.println("Task timed out");
    future.cancel(true); // Cancel the task
    
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    System.out.println("Thread interrupted");
    
} finally {
    // Always shutdown executor
    executor.shutdown();
    try {
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }
    } catch (InterruptedException e) {
        executor.shutdownNow();
        Thread.currentThread().interrupt();
    }
}
```

### 3. CompletableFuture Exception Handling

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> {
        if (Math.random() > 0.5) {
            throw new RuntimeException("Task failed");
        }
        return "Success";
    })
    .handle((result, exception) -> {
        if (exception != null) {
            System.out.println("Handled exception: " + exception.getMessage());
            return "Default value";
        }
        return result;
    });

String result = future.join(); // Won't throw exception due to handle()
```

## Exception Prevention Tips

### 1. Thread Safety
- Use `synchronized` blocks or `ReentrantLock`
- Use thread-safe collections (`ConcurrentHashMap`, `BlockingQueue`)
- Use atomic classes (`AtomicInteger`, `AtomicReference`)

### 2. Proper Resource Management
```java
// Always use try-finally or try-with-resources
ExecutorService executor = Executors.newFixedThreadPool(4);
try {
    // Submit tasks
} finally {
    executor.shutdown();
    // Wait for termination
}
```

### 3. Defensive Programming
```java
// Check thread state before operations
if (!Thread.currentThread().isInterrupted()) {
    // Perform operation
}

// Check Future state before accessing
if (future.isDone() && !future.isCancelled()) {
    String result = future.get();
}
```

## Quick Reference

| Exception | Type | Common Cause | Solution |
|-----------|------|--------------|----------|
| `InterruptedException` | Checked | Thread interrupted during wait/sleep | Restore interrupt flag, exit gracefully |
| `ExecutionException` | Checked | Task in executor failed | Check cause, handle appropriately |
| `TimeoutException` | Checked | Operation exceeded time limit | Cancel task or extend timeout |
| `RejectedExecutionException` | Unchecked | Executor shutdown or saturated | Proper shutdown sequence, configure rejection policy |
| `IllegalThreadStateException` | Unchecked | Thread started multiple times | Check thread state before starting |
| `CompletionException` | Unchecked | CompletableFuture task failed | Use `handle()` or `exceptionally()` |
| `CancellationException` | Unchecked | Accessing cancelled Future | Check `isCancelled()` before access |

## Best Practices Summary

1. **Always handle InterruptedException properly** - restore the interrupt flag
2. **Use proper exception handling** for Future.get() operations
3. **Implement graceful shutdown** for ExecutorService
4. **Use defensive programming** - check states before operations
5. **Log exceptions appropriately** for debugging
6. **Test concurrent code thoroughly** - exceptions often occur under specific timing conditions
7. **Use high-level concurrency utilities** - they handle many edge cases automatically

