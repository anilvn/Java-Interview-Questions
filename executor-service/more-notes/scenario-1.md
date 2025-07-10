# CompletableFuture Guide: E-Commerce Order Processing

## Problem Overview

In e-commerce applications, order processing involves multiple independent tasks that can benefit from parallel execution:

- **Order Validation**: Verify order details and business rules
- **Payment Processing**: Handle payment gateway transactions  
- **Inventory Updates**: Adjust stock levels and availability
- **Email Notifications**: Send confirmation emails to customers

The challenge is orchestrating these tasks to run concurrently while ensuring all complete successfully before finalizing the order.

## Solution Approaches

### Approach 1: Parallel Execution with allOf() (Recommended)

This approach runs all tasks in parallel and waits for completion using `CompletableFuture.allOf()`.

```java
public class OrderProcessor {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public void processOrder(Order order) {
        CompletableFuture<Void> validation = CompletableFuture
            .runAsync(() -> validateOrder(order), executor);
            
        CompletableFuture<Void> payment = CompletableFuture
            .runAsync(() -> processPayment(order), executor);
            
        CompletableFuture<Void> inventory = CompletableFuture
            .runAsync(() -> updateInventory(order), executor);
            
        CompletableFuture<Void> notification = CompletableFuture
            .runAsync(() -> sendConfirmationEmail(order), executor);

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(
            validation, payment, inventory, notification
        );

        allTasks.join(); // Block until all tasks complete
        
        System.out.println("Order processing completed successfully");
        finalizeOrder(order);
    }
}
```

**Advantages:**
- True parallelism with maximum performance
- Clean, readable code structure
- Built-in coordination mechanism
- Easy to extend with additional tasks

**Disadvantages:**
- Basic error handling requires additional work
- No built-in result aggregation for return values

### Approach 2: Sequential Task Chaining

When tasks have dependencies, use `thenRunAsync()` for sequential execution.

```java
public void processOrderSequentially(Order order) {
    CompletableFuture<Void> pipeline = CompletableFuture
        .runAsync(() -> validateOrder(order))
        .thenRunAsync(() -> processPayment(order))
        .thenRunAsync(() -> updateInventory(order))
        .thenRunAsync(() -> sendConfirmationEmail(order));

    pipeline.join();
}
```

**Use Case:** When payment must succeed before inventory updates, or when email should only send after payment confirmation.

**Trade-off:** Sequential execution sacrifices parallelism for dependency management.

### Approach 3: Result Collection and Aggregation

When tasks return meaningful results that need to be collected:

```java
public OrderResult processOrderWithResults(Order order) {
    CompletableFuture<String> validation = CompletableFuture
        .supplyAsync(() -> validateOrder(order));
        
    CompletableFuture<PaymentResult> payment = CompletableFuture
        .supplyAsync(() -> processPayment(order));
        
    CompletableFuture<InventoryResult> inventory = CompletableFuture
        .supplyAsync(() -> updateInventory(order));
        
    CompletableFuture<String> notification = CompletableFuture
        .supplyAsync(() -> sendConfirmationEmail(order));

    CompletableFuture<OrderResult> allResults = CompletableFuture
        .allOf(validation, payment, inventory, notification)
        .thenApply(v -> new OrderResult(
            validation.join(),
            payment.join(),
            inventory.join(),
            notification.join()
        ));

    return allResults.join();
}
```

## Enhanced Error Handling

### Individual Task Error Management

```java
public void processOrderWithErrorHandling(Order order) {
    CompletableFuture<Void> validation = CompletableFuture
        .runAsync(() -> validateOrder(order), executor)
        .exceptionally(ex -> {
            logError("Order validation failed", ex);
            handleValidationFailure(order, ex);
            return null;
        });

    CompletableFuture<Void> payment = CompletableFuture
        .runAsync(() -> processPayment(order), executor)
        .exceptionally(ex -> {
            logError("Payment processing failed", ex);
            initiateRefundProcess(order, ex);
            return null;
        });

    // You can add more tasks with similar patterns if needed

    CompletableFuture<Void> allTasks = CompletableFuture
        .allOf(validation, payment)
        .whenComplete((result, ex) -> {
            if (ex != null) {
                logError("Overall order processing failed", ex);
                handleOverallFailure(order, ex);
            } else {
                finalizeSuccessfulOrder(order);
            }
        });

    allTasks.join(); // Wait for completion
}
```
you **can and should throw exceptions directly** in your task methods (like `validateOrder()`, `processPayment()`) when something goes wrong. This allows the `CompletableFuture`'s `.exceptionally()` or `.handle()` blocks to catch and process them cleanly.
```java
public void validateOrder(Order order) {
    if (!isOrderValid(order)) {
        throw new InvalidRequestException("Order validation failed due to missing fields.");
    }
    // ... proceed with validation logic
}
```

## Timeout Management

### Individual Task Timeouts

```java
public void processOrderWithTimeouts(Order order) {
    CompletableFuture<Void> validation = CompletableFuture
        .runAsync(() -> validateOrder(order), executor)
        .orTimeout(5, TimeUnit.SECONDS)
        .exceptionally(ex -> {
            if (ex instanceof TimeoutException) {
                logError("Validation timed out", ex);
            }
            return null;
        });
        
    CompletableFuture<Void> payment = CompletableFuture
        .runAsync(() -> processPayment(order), executor)
        .orTimeout(30, TimeUnit.SECONDS)
        .exceptionally(ex -> {
            if (ex instanceof TimeoutException) {
                logError("Payment processing timed out", ex);
                initiatePaymentCancellation(order);
            }
            return null;
        });
        
    // Configure other tasks similarly...
}
```

### Global Timeout Strategy

```java
public void processOrderWithGlobalTimeout(Order order) {
    CompletableFuture<Void> allTasks = CompletableFuture.allOf(
        validation, payment, inventory, notification
    );
    
    CompletableFuture<Void> result = allTasks
        .orTimeout(60, TimeUnit.SECONDS)
        .exceptionally(ex -> {
            if (ex instanceof TimeoutException) {
                logError("Overall order processing timed out", ex);
                cancelOrderProcessing(order);
                alertOperationsTeam(order);
            }
            return null;
        });
        
    result.join();
}
```

### Advanced Timeout with Watchdog

```java
private final ScheduledExecutorService scheduler = 
    Executors.newScheduledThreadPool(1);

public void processOrderWithWatchdog(Order order) {
    CompletableFuture<Void> allTasks = CompletableFuture.allOf(
        validation, payment, inventory, notification
    );
    
    ScheduledFuture<?> watchdog = scheduler.schedule(() -> {
        if (!allTasks.isDone()) {
            logError("Order processing watchdog triggered");
            escalateToManagement(order);
        }
    }, 90, TimeUnit.SECONDS);
    
    allTasks.whenComplete((result, ex) -> {
        watchdog.cancel(false); // Cancel watchdog on completion
        if (ex == null) {
            finalizeOrder(order);
        }
    });
    
    allTasks.join();
}
```

## Complete Production-Ready Example

```java
@Service
public class OrderProcessingService {
    private final ExecutorService executor;
    private final ScheduledExecutorService scheduler;
    
    public OrderProcessingService() {
        this.executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
        );
        this.scheduler = Executors.newScheduledThreadPool(2);
    }
    
    public CompletableFuture<OrderProcessingResult> processOrderAsync(Order order) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Create individual task futures
                CompletableFuture<ValidationResult> validation = createValidationTask(order);
                CompletableFuture<PaymentResult> payment = createPaymentTask(order);
                CompletableFuture<InventoryResult> inventory = createInventoryTask(order);
                CompletableFuture<NotificationResult> notification = createNotificationTask(order);
                
                // Wait for all tasks to complete
                CompletableFuture<Void> allTasks = CompletableFuture.allOf(
                    validation, payment, inventory, notification
                );
                
                // Set up global timeout
                allTasks.orTimeout(120, TimeUnit.SECONDS).join();
                
                // Collect results
                return new OrderProcessingResult(
                    validation.join(),
                    payment.join(),
                    inventory.join(),
                    notification.join()
                );
                
            } catch (Exception ex) {
                throw new OrderProcessingException("Order processing failed", ex);
            }
        }, executor);
    }
    
    private CompletableFuture<ValidationResult> createValidationTask(Order order) {
        return CompletableFuture
            .supplyAsync(() -> validateOrder(order), executor)
            .orTimeout(10, TimeUnit.SECONDS)
            .exceptionally(ex -> handleValidationError(order, ex));
    }
    
    private CompletableFuture<PaymentResult> createPaymentTask(Order order) {
        return CompletableFuture
            .supplyAsync(() -> processPayment(order), executor)
            .orTimeout(45, TimeUnit.SECONDS)
            .exceptionally(ex -> handlePaymentError(order, ex));
    }
    
    // Additional helper methods...
    
    @PreDestroy
    public void shutdown() {
        executor.shutdown();
        scheduler.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

## Comparison Summary

| Approach | Parallelism | Error Handling | Result Collection | Complexity | Best For |
|----------|-------------|----------------|-------------------|------------|----------|
| `allOf()` with `runAsync()` | ✅ Full | ⚠️ Manual | ❌ No | Low | Fire-and-forget tasks |
| `allOf()` with `supplyAsync()` | ✅ Full | ⚠️ Manual | ✅ Yes | Medium | Tasks returning results |
| Sequential chaining | ❌ None | ✅ Built-in | ✅ Yes | Low | Dependent tasks |
| Enhanced with timeouts | ✅ Full | ✅ Comprehensive | ✅ Yes | High | Production systems |

## Best Practices

1. **Use appropriate thread pools**: Size pools based on task characteristics (CPU-bound vs I/O-bound)
2. **Implement proper timeout strategies**: Set reasonable timeouts for individual tasks and overall processing
3. **Handle exceptions gracefully**: Use `exceptionally()`, `handle()`, or `whenComplete()` for error management
4. **Clean up resources**: Always shut down executors properly to prevent resource leaks
5. **Monitor and log**: Include comprehensive logging for debugging and monitoring
6. **Consider circuit breakers**: For external service calls, implement circuit breaker patterns
7. **Test timeout scenarios**: Verify behavior under various failure and timeout conditions

## Recommended Solution

For most e-commerce order processing scenarios, **Approach 1 with enhanced error handling and timeouts** provides the best balance of performance, maintainability, and robustness. It enables true parallelism while providing the flexibility to handle complex error scenarios and timeout requirements that are essential in production environments.