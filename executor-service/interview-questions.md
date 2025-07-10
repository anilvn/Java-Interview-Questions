# Java ExecutorService & Multithreading - Interview Questions

## ExecutorService Fundamentals

### Basic Concepts

**Q1: What is ExecutorService and how does it differ from creating threads directly?**
- ExecutorService provides thread pool management, lifecycle control, and task submission framework
- Benefits: Resource management, thread reuse, better performance, easier task management
- Direct thread creation: No reuse, manual lifecycle management, resource intensive

**Q2: What are the main methods of ExecutorService interface?**
- `submit()` - Submit tasks and get Future
- `execute()` - Execute Runnable tasks
- `invokeAll()` - Execute multiple tasks
- `invokeAny()` - Execute tasks, return first completed
- `shutdown()` - Graceful shutdown
- `shutdownNow()` - Immediate shutdown

**Q3: Explain the difference between execute() and submit() methods.**
- `execute()`: Takes Runnable, returns void, exceptions are handled by UncaughtExceptionHandler
- `submit()`: Takes Runnable/Callable, returns Future, exceptions wrapped in ExecutionException

### Thread Pool Types

**Q4: What are the different types of thread pools provided by Executors class?**
- `newFixedThreadPool(n)` - Fixed number of threads
- `newCachedThreadPool()` - Creates threads as needed, reuses existing
- `newSingleThreadExecutor()` - Single worker thread
- `newScheduledThreadPool(n)` - For scheduled/periodic tasks

**Q5: When would you use CachedThreadPool vs FixedThreadPool?**
- **CachedThreadPool**: Short-lived asynchronous tasks, unpredictable workload
- **FixedThreadPool**: Known workload, predictable resource requirements, long-running tasks

**Q6: What are the risks of using newCachedThreadPool()?**
- Can create unlimited threads leading to resource exhaustion
- No upper bound on thread creation
- Can cause OutOfMemoryError under high load

### Thread Pool Configuration

**Q7: Explain ThreadPoolExecutor constructor parameters.**
```java
ThreadPoolExecutor(
    int corePoolSize,           // Minimum threads
    int maximumPoolSize,        // Maximum threads
    long keepAliveTime,         // Idle thread timeout
    TimeUnit unit,              // Time unit
    BlockingQueue<Runnable> workQueue,  // Task queue
    ThreadFactory threadFactory,         // Thread creation
    RejectedExecutionHandler handler     // Rejection policy
)
```

**Q8: How do you determine the optimal thread pool size?**
- **CPU-intensive**: Number of CPU cores
- **I/O-intensive**: CPU cores × 2 (or higher based on I/O wait time)
- **Mixed workload**: Requires profiling and testing
- Formula: `Threads = CPU_cores × (1 + Wait_time/Service_time)`

**Q9: What are the different types of BlockingQueue used in thread pools?**
- `LinkedBlockingQueue` - Unbounded, can cause memory issues
- `ArrayBlockingQueue` - Bounded, blocks when full
- `SynchronousQueue` - Direct handoff, no storage
- `PriorityBlockingQueue` - Priority-based task execution

**Q10: Explain rejection policies in ThreadPoolExecutor.**
- `AbortPolicy` (default) - Throws RejectedExecutionException
- `CallerRunsPolicy` - Runs task in calling thread
- `DiscardPolicy` - Silently discards task
- `DiscardOldestPolicy` - Discards oldest unhandled task

## Future and Callable

**Q11: What is the difference between Runnable and Callable?**
- **Runnable**: `run()` method, returns void, cannot throw checked exceptions
- **Callable**: `call()` method, returns value, can throw checked exceptions

**Q12: How do you handle exceptions in Callable tasks?**
```java
Future<String> future = executor.submit(() -> {
    throw new RuntimeException("Task failed");
});

try {
    String result = future.get();
} catch (ExecutionException e) {
    Throwable cause = e.getCause(); // Original exception
}
```

**Q13: What happens when you call Future.get() without timeout?**
- Blocks indefinitely until task completes
- Can lead to thread starvation
- Better to use `get(timeout, unit)` for responsive applications

**Q14: How do you cancel a running task?**
```java
Future<?> future = executor.submit(longRunningTask);
boolean cancelled = future.cancel(true); // true = interrupt if running
```

**Q15: What is CompletableFuture and how does it differ from Future?**
- CompletableFuture: Composable, supports chaining, callbacks, exception handling
- Future: Basic async result container, limited functionality
- CompletableFuture allows non-blocking operations with `thenApply()`, `thenCompose()`

## Exception Handling

### InterruptedException

**Q16: What is InterruptedException and how should you handle it?**
- Thrown when thread is interrupted during blocking operations
- **Correct handling**: Restore interrupt flag and exit gracefully
```java
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // Restore flag
    return; // Exit method
}
```

**Q17: Why should you restore the interrupt flag after catching InterruptedException?**
- Preserves interrupt status for higher-level code
- Allows proper cleanup and shutdown procedures
- Maintains thread's interrupted state for other operations

**Q18: What's wrong with this code?**
```java
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // Log and continue - WRONG!
}
```
- Swallows interrupt signal
- Thread continues execution ignoring interruption
- Should restore interrupt flag: `Thread.currentThread().interrupt()`

### ExecutorService Exceptions

**Q19: What is ExecutionException and when does it occur?**
- Wraps exceptions thrown by Callable/Runnable tasks
- Occurs when calling `Future.get()` on failed task
- Use `getCause()` to access original exception

**Q20: When do you get RejectedExecutionException?**
- Executor is shutdown
- Thread pool is saturated and queue is full
- Task rejected by rejection policy

**Q21: How do you handle TimeoutException in Future.get()?**
```java
try {
    String result = future.get(5, TimeUnit.SECONDS);
} catch (TimeoutException e) {
    future.cancel(true); // Cancel the task
    // Handle timeout scenario
}
```

**Q22: What is CompletionException in CompletableFuture?**
- Unchecked exception that wraps underlying exceptions
- Thrown by `join()` method
- Handle using `handle()`, `exceptionally()`, or `whenComplete()`

## Lifecycle Management

**Q23: Why is it important to shutdown ExecutorService?**
- Prevents resource leaks
- Non-daemon threads keep JVM alive
- Allows graceful completion of pending tasks

**Q24: What's the difference between shutdown() and shutdownNow()?**
- `shutdown()`: Graceful shutdown, waits for running tasks to complete
- `shutdownNow()`: Immediate shutdown, interrupts running tasks, returns pending tasks

**Q25: Write code for proper ExecutorService shutdown.**
```java
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
```

## Advanced Topics

**Q26: How do you implement a custom ThreadFactory?**
```java
ThreadFactory factory = r -> {
    Thread t = new Thread(r);
    t.setName("CustomWorker-" + t.getId());
    t.setDaemon(true);
    t.setUncaughtExceptionHandler((thread, ex) -> {
        System.err.println("Uncaught exception: " + ex.getMessage());
    });
    return t;
};
```

**Q27: What is the difference between submit() and invokeAll()?**
- `submit()`: Single task, returns Future immediately
- `invokeAll()`: Multiple tasks, returns List<Future>, waits for all to complete

**Q28: How do you handle partial failures in invokeAll()?**
```java
List<Future<String>> futures = executor.invokeAll(tasks);
for (Future<String> future : futures) {
    try {
        String result = future.get();
        // Process successful result
    } catch (ExecutionException e) {
        // Handle individual task failure
    }
}
```

**Q29: What is ForkJoinPool and when would you use it?**
- Specialized ExecutorService for divide-and-conquer algorithms
- Work-stealing algorithm for better load balancing
- Good for recursive tasks, parallel streams

**Q30: How do you implement timeout for a group of tasks?**
```java
List<Future<String>> futures = executor.invokeAll(tasks, 10, TimeUnit.SECONDS);
// Tasks that don't complete within 10 seconds are cancelled
```

## Practical Scenarios

**Q31: How would you implement a producer-consumer pattern with ExecutorService?**
```java
BlockingQueue<String> queue = new LinkedBlockingQueue<>();
ExecutorService executor = Executors.newFixedThreadPool(4);

// Producer
executor.submit(() -> {
    queue.offer("item");
});

// Consumer
executor.submit(() -> {
    String item = queue.take(); // Blocks until available
});
```

**Q32: How do you implement parallel processing of a list?**
```java
List<CompletableFuture<String>> futures = items.stream()
    .map(item -> CompletableFuture.supplyAsync(() -> process(item), executor))
    .collect(Collectors.toList());

List<String> results = futures.stream()
    .map(CompletableFuture::join)
    .collect(Collectors.toList());
```

**Q33: What are the common pitfalls in multithreading?**
- Forgetting to shutdown ExecutorService
- Swallowing InterruptedException
- Using unbounded queues leading to memory issues
- Not handling ExecutionException properly
- Creating too many threads for CPU-intensive tasks

**Q34: How do you test multithreaded code?**
- Use CountDownLatch for synchronization
- Test with different thread pool sizes
- Use stress testing with high concurrency
- Test timeout scenarios
- Verify proper exception handling

**Q35: Explain the difference between CancellationException and InterruptedException.**
- **CancellationException**: Future was cancelled, unchecked exception
- **InterruptedException**: Thread was interrupted during blocking operation, checked exception

## Performance and Best Practices

**Q36: How do you monitor thread pool performance?**
- Use ThreadPoolExecutor methods: `getActiveCount()`, `getCompletedTaskCount()`, `getPoolSize()`
- JMX monitoring
- Custom metrics collection
- Thread dumps analysis

**Q37: What causes thread pool starvation?**
- Tasks submitting other tasks to same pool
- Long-running tasks blocking worker threads
- Insufficient thread pool size
- Dependencies between tasks in same pool

**Q38: How do you avoid deadlocks in thread pools?**
- Avoid nested task submission to same pool
- Use timeout on blocking operations
- Proper lock ordering
- Use concurrent collections
- Minimize lock scope

**Q39: When would you use a custom RejectedExecutionHandler?**
- Logging rejected tasks
- Queuing tasks in external storage
- Implementing backpressure mechanisms
- Custom retry logic

**Q40: What is the difference between parallelStream() and ExecutorService?**
- **parallelStream()**: Uses common ForkJoinPool, automatic parallelization
- **ExecutorService**: Custom thread pool configuration, explicit task management
- parallelStream() easier but less control, ExecutorService more flexible

## Code Review Questions

**Q41: What's wrong with this shutdown code?**
```java
executor.shutdown();
executor.awaitTermination(1, TimeUnit.SECONDS);
```
**Issue**: Doesn't check return value of awaitTermination(), no shutdownNow() call

**Q42: Identify the problem:**
```java
for (int i = 0; i < 1000; i++) {
    ExecutorService exec = Executors.newSingleThreadExecutor();
    exec.submit(() -> doWork());
    // Missing shutdown - resource leak!
}
```

**Q43: What could go wrong here?**
```java
ExecutorService executor = Executors.newCachedThreadPool();
for (int i = 0; i < 1000000; i++) {
    executor.submit(() -> Thread.sleep(60000));
}
```
**Issue**: Could create millions of threads, leading to OutOfMemoryError

These questions cover the fundamental to advanced concepts of ExecutorService and multithreading exceptions. Practice implementing the code examples and understanding the underlying concepts for thorough preparation.