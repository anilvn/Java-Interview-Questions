That’s a **big chunk of notes** you pasted, and it covers a lot about:
✅ `Future` and its methods (`isDone()`, `isCancelled()`, `get()`, etc.)
✅ `Callable` vs `Runnable`
✅ `CompletableFuture` and methods like `thenApply`, `thenApplyAsync`, `thenCompose`, `thenAccept`, `thenCombine`, etc.

To make this useful for quick revision / interview prep, here’s a **clean, structured, commented summary** of what you wrote:

---

## ✅ ThreadPoolExecutor + Future

```java
public static void main(String[] args) {
    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
        1, 1, 1, TimeUnit.HOURS, new ArrayBlockingQueue<>(10),
        Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy()
    );

    // Submit a task
    Future<?> futureObj = poolExecutor.submit(() -> {
        System.out.println("Task is running in: " + Thread.currentThread().getName());
        // Simulate work
        Thread.sleep(7000);
    });

    // Check task status
    System.out.println("isDone: " + futureObj.isDone());

    try {
        // Wait up to 2 seconds for completion
        futureObj.get(2, TimeUnit.SECONDS);
    } catch (TimeoutException e) {
        System.out.println("TimeoutException: task is still running.");
    } catch (Exception e) {
        e.printStackTrace();
    }

    try {
        // Wait indefinitely for completion
        futureObj.get();
        System.out.println("isDone: " + futureObj.isDone());
        System.out.println("isCancelled: " + futureObj.isCancelled());
    } catch (Exception e) {
        e.printStackTrace();
    }

    poolExecutor.shutdown();
}
```