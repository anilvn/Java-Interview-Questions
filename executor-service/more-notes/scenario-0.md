# Asynchronous API Aggregation - Study Notes

## Problem Overview
Combine data from multiple APIs asynchronously to create a unified customer profile while handling failures gracefully.

## Solution Approaches

### Approach 1: CompletableFuture with thenCombine()
**How it works:**
- Creates separate CompletableFuture for each API call
- Uses `thenCombine()` to merge results step by step
- Each future has error handling with `exceptionally()`
- Returns default values when APIs fail
```java
public static CustomerInfo getCombinedCustomerInfo() throws ExecutionException, InterruptedException {
    CompletableFuture<CustomerPersonalInfo> personalFuture = CompletableFuture.supplyAsync(() ->
            CustomerService.getCustomerPersonalInfo())
            .exceptionally(ex -> {
                System.out.println("Personal Info API failed: " + ex.getMessage());
                return new CustomerPersonalInfo("N/A", "N/A");
            });

    CompletableFuture<CustomerCreditCardInfo> creditFuture = CompletableFuture.supplyAsync(() ->
            CustomerService.getCustomerCreditCardInfo())
            .exceptionally(ex -> {
                System.out.println("Credit Card Info API failed: " + ex.getMessage());
                return new CustomerCreditCardInfo("N/A", "N/A");
            });

    CompletableFuture<CustomerTransactionInfo> transactionFuture = CompletableFuture.supplyAsync(() ->
            CustomerService.getCustomerTransactionInfo())
            .exceptionally(ex -> {
                System.out.println("Transaction Info API failed: " + ex.getMessage());
                return new CustomerTransactionInfo(0.0, "N/A");
            });

    return personalFuture.thenCombine(creditFuture, (personal, credit) ->
            new Object[] { personal, credit }
    ).thenCombine(transactionFuture, (array, transaction) ->
            new CustomerInfo(
                    (CustomerPersonalInfo) array[0],
                    (CustomerCreditCardInfo) array[1],
                    transaction
            )
    ).get();
}
```
**Pros:**
- All API calls execute in parallel
- Graceful error handling per API
- Clean chaining syntax
- Result available as single object

**Cons:**
- Complex nested combining logic
- Object array casting required
- Harder to extend for more APIs

### Approach 2: CompletableFuture with thenAccept()
**How it works:**
- Creates shared mutable object
- Each API result directly updates the shared object
- Uses `thenAccept()` to perform side effects
- Parallel execution with shared state modification
```java
// approach-2 for simillar kind of behavour as above.
        CompletableFuture<Void> nameFuture = CompletableFuture.supplyAsync(() -> getNameFromApi1())
                .exceptionally(ex -> {
                    System.out.println("API1 failed: " + ex.getMessage());
                    return "Unknown";
                })
                .thenAccept(customerInfo::setName); // safely set name

        CompletableFuture<Void> creditCardFuture = CompletableFuture.supplyAsync(() -> getCardFromApi2())
                .exceptionally(ex -> {
                    System.out.println("API2 failed: " + ex.getMessage());
                    return new CreditCardInfo("0000", "Default");
                })
                .thenAccept(customerInfo::setCreditCardInfo); // safely set credit card info
```
**Pros:**
- Simpler syntax for multiple APIs
- Direct object mutation
- Easy to add more API calls

**Cons:**
- Requires mutable shared object
- Thread safety concerns
- Side effects make testing harder
- No clear completion signal for all operations

## Better Alternative: CompletableFuture.allOf()

### Approach 3: Using allOf() (Recommended)
```java
public static CustomerInfo getCombinedCustomerInfo() {
    CompletableFuture<CustomerPersonalInfo> personalFuture = 
        CompletableFuture.supplyAsync(() -> CustomerService.getCustomerPersonalInfo())
        .exceptionally(ex -> new CustomerPersonalInfo("N/A", "N/A"));
    
    CompletableFuture<CustomerCreditCardInfo> creditFuture = 
        CompletableFuture.supplyAsync(() -> CustomerService.getCustomerCreditCardInfo())
        .exceptionally(ex -> new CustomerCreditCardInfo("N/A", "N/A"));
    
    CompletableFuture<CustomerTransactionInfo> transactionFuture = 
        CompletableFuture.supplyAsync(() -> CustomerService.getCustomerTransactionInfo())
        .exceptionally(ex -> new CustomerTransactionInfo(0.0, "N/A"));
    
    return CompletableFuture.allOf(personalFuture, creditFuture, transactionFuture)
        .thenApply(v -> new CustomerInfo(
            personalFuture.join(),
            creditFuture.join(),
            transactionFuture.join()
        )).join();
}
```

**Why it's better:**
- Cleaner and more readable
- Easier to extend for more APIs
- Clear completion semantics
- No complex chaining or casting

## Key Concepts
- **Parallel Execution:** All API calls run simultaneously
- **Error Isolation:** Each API failure is handled independently
- **Default Values:** Fallback data when services are unavailable
- **Non-blocking:** Main thread isn't blocked during API calls

## Real-World Practice Scenarios

### Scenario 1: E-commerce Product Page
**Problem:** Load product details from multiple services
- **API 1:** Product basic info (name, description)
- **API 2:** Pricing and discounts
- **API 3:** Inventory status
- **API 4:** Customer reviews summary
- **Challenge:** Page should load even if some services are down

### Scenario 2: Social Media Dashboard
**Problem:** Aggregate user's social media activity
- **API 1:** Twitter posts and metrics
- **API 2:** LinkedIn connections and updates
- **API 3:** Instagram photos and engagement
- **API 4:** Facebook posts and reactions
- **Challenge:** Show available data, hide unavailable sections

### Scenario 3: Financial Portfolio Dashboard
**Problem:** Build investment portfolio overview
- **API 1:** Stock prices from market data provider
- **API 2:** Account balances from bank
- **API 3:** Cryptocurrency holdings
- **API 4:** News and market analysis
- **Challenge:** Real-time updates with different API response times

### Scenario 4: Travel Booking System
**Problem:** Show comprehensive trip options
- **API 1:** Flight availability and prices
- **API 2:** Hotel room availability
- **API 3:** Car rental options
- **API 4:** Weather forecast for destination
- **Challenge:** Timeout handling for slow travel APIs

### Scenario 5: Healthcare Patient Portal
**Problem:** Patient medical summary dashboard
- **API 1:** Basic patient demographics
- **API 2:** Recent lab results
- **API 3:** Prescription history
- **API 4:** Upcoming appointments
- **Challenge:** HIPAA compliance and secure error handling

## Best Practices
1. **Always handle exceptions** with `exceptionally()`
2. **Use meaningful default values** for failed API calls
3. **Set appropriate timeouts** for API calls
4. **Consider using Circuit Breaker pattern** for frequently failing services
5. **Log failures** for monitoring and debugging
6. **Use thread pools** for better resource management

## When to Use Each Approach
- **Use allOf()** for 3+ independent API calls
- **Use thenCombine()** for 2 APIs with simple combination logic
- **Avoid thenAccept() approach** due to thread safety concerns