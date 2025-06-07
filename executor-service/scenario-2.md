# Stock Market Live Data Aggregation with CompletableFuture

## Problem Overview

A financial application needs to:
1. **Retrieve stock prices** from three different APIs (e.g., Yahoo Finance, Alpha Vantage, IEX Cloud)
2. **Compute the average** of all retrieved prices
3. **Handle failures gracefully** by using default values for failed API calls
4. **Ensure fault tolerance** - system should work even if 1-2 APIs fail
5. **Optimize for speed** - parallel execution for better performance

## Core Requirements Analysis

- **Parallel Execution**: All API calls should run concurrently
- **Fault Tolerance**: Failed APIs should not break the entire system
- **Default Value Strategy**: Use fallback values for failed API calls
- **Result Aggregation**: Combine all results to compute average
- **Timeout Handling**: Prevent hanging on slow/unresponsive APIs

## Solution Approaches

### Solution 1: Individual Exception Handling with allOf() (Recommended)

This approach handles each API call independently and uses default values for failures.

```java
@Service
public class StockPriceAggregator {
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final RestTemplate restTemplate = new RestTemplate();
    
    public CompletableFuture<Double> getAverageStockPrice(String symbol) {
        // Create individual API call futures with exception handling
        CompletableFuture<Double> yahooPrice = CompletableFuture
            .supplyAsync(() -> fetchFromYahooFinance(symbol), executor)
            .orTimeout(5, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                logError("Yahoo Finance API failed", ex);
                return getDefaultPrice(symbol, "YAHOO");
            });
            
        CompletableFuture<Double> alphaVantagePrice = CompletableFuture
            .supplyAsync(() -> fetchFromAlphaVantage(symbol), executor)
            .orTimeout(5, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                logError("Alpha Vantage API failed", ex);
                return getDefaultPrice(symbol, "ALPHAVANTAGE");
            });
            
        CompletableFuture<Double> iexPrice = CompletableFuture
            .supplyAsync(() -> fetchFromIEXCloud(symbol), executor)
            .orTimeout(5, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                logError("IEX Cloud API failed", ex);
                return getDefaultPrice(symbol, "IEX");
            });
         
         // strategy-1
         // Combine results and calculate average
        CompletableFuture<Double> averagePriceFuture = apiyahooPrice
                .thenCombine(alphaVantagePrice, Double::sum)
                .thenCombine(iexPrice, Double::sum)
                .thenApply(total -> total / 3);

        Double averagePrice = averagePriceFuture.get();
        System.out.println("Average Stock Price: " + averagePrice);
        
        // strategy-2
        // Combine all results and calculate average
        return CompletableFuture.allOf(yahooPrice, alphaVantagePrice, iexPrice)
            .thenApply(v -> {
                Double yahoo = yahooPrice.join();
                Double alpha = alphaVantagePrice.join();
                Double iex = iexPrice.join();
                
                double average = (yahoo + alpha + iex) / 3.0;
                logInfo(String.format("Calculated average price for %s: %.2f (Yahoo: %.2f, Alpha: %.2f, IEX: %.2f)", 
                    symbol, average, yahoo, alpha, iex));
                
                return average;
            });
    }
    
    private Double fetchFromYahooFinance(String symbol) {
        // Simulate API call with potential failure
        if (Math.random() < 0.1) { // 10% failure rate
            throw new RuntimeException("Yahoo Finance API temporarily unavailable");
        }
        return simulateApiCall("Yahoo", symbol);
    }
    
    private Double getDefaultPrice(String symbol, String source) {
        // Could use cached price, historical average, or market estimate
        double defaultPrice = 100.0 + (Math.random() * 50); // Simplified default
        logWarning(String.format("Using default price %.2f for %s from %s", defaultPrice, symbol, source));
        return defaultPrice;
    }
}
```

**Advantages:**
- **Fault tolerant**: Each API failure is handled independently
- **Parallel execution**: All APIs called simultaneously
- **Clean error handling**: Individual exception management
- **Timeout protection**: Prevents hanging on slow APIs
- **Always returns result**: Never fails completely due to individual API failures

**Disadvantages:**
- **May mask failures**: Default values might hide underlying issues
- **Average quality degradation**: Default values may skew results

### Solution 2: Collect Successful Results Only

This approach only uses successful API calls and excludes failed ones from average calculation.

```java
public class StockPriceAggregatorV2 {
    
    public CompletableFuture<Double> getAverageStockPrice(String symbol) {
        List<CompletableFuture<Optional<Double>>> apiCalls = Arrays.asList(
            createApiCall(() -> fetchFromYahooFinance(symbol), "Yahoo"),
            createApiCall(() -> fetchFromAlphaVantage(symbol), "AlphaVantage"),
            createApiCall(() -> fetchFromIEXCloud(symbol), "IEX")
        );
        
        return CompletableFuture.allOf(apiCalls.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                List<Double> successfulPrices = apiCalls.stream()
                    .map(CompletableFuture::join)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
                
                if (successfulPrices.isEmpty()) {
                    throw new RuntimeException("All API calls failed for symbol: " + symbol);
                }
                
                double average = successfulPrices.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
                
                logInfo(String.format("Calculated average from %d successful calls: %.2f", 
                    successfulPrices.size(), average));
                
                return average;
            });
    }
    
    private CompletableFuture<Optional<Double>> createApiCall(Supplier<Double> apiCall, String source) {
        return CompletableFuture
            .supplyAsync(apiCall, executor)
            .orTimeout(5, TimeUnit.SECONDS)
            .handle((result, ex) -> {
                if (ex != null) {
                    logError(source + " API failed", ex);
                    return Optional.<Double>empty();
                } else {
                    return Optional.of(result);
                }
            });
    }
}
```

**Advantages:**
- **Pure data**: Only uses actual API responses
- **Flexible averaging**: Adapts to number of successful calls
- **Clear failure tracking**: Explicitly tracks which APIs failed

**Disadvantages:**
- **Potential complete failure**: If all APIs fail, system fails
- **Variable reliability**: Quality depends on number of successful calls

### Solution 3: Weighted Average with Quality Scoring

This approach assigns quality scores to different data sources and calculates weighted averages.

```java
public class WeightedStockPriceAggregator {
    
    private static final Map<String, Double> API_WEIGHTS = Map.of(
        "YAHOO", 0.4,
        "ALPHAVANTAGE", 0.35,
        "IEX", 0.25
    );
    
    public CompletableFuture<WeightedAverageResult> getWeightedAveragePrice(String symbol) {
        CompletableFuture<PriceResult> yahooResult = createWeightedApiCall(
            () -> fetchFromYahooFinance(symbol), "YAHOO", symbol);
            
        CompletableFuture<PriceResult> alphaResult = createWeightedApiCall(
            () -> fetchFromAlphaVantage(symbol), "ALPHAVANTAGE", symbol);
            
        CompletableFuture<PriceResult> iexResult = createWeightedApiCall(
            () -> fetchFromIEXCloud(symbol), "IEX", symbol);
        
        return CompletableFuture.allOf(yahooResult, alphaResult, iexResult)
            .thenApply(v -> {
                List<PriceResult> results = Arrays.asList(
                    yahooResult.join(),
                    alphaResult.join(),
                    iexResult.join()
                );
                
                return calculateWeightedAverage(results, symbol);
            });
    }
    
    private CompletableFuture<PriceResult> createWeightedApiCall(
            Supplier<Double> apiCall, String source, String symbol) {
        
        return CompletableFuture
            .supplyAsync(apiCall, executor)
            .orTimeout(5, TimeUnit.SECONDS)
            .handle((price, ex) -> {
                if (ex != null) {
                    logError(source + " API failed", ex);
                    return new PriceResult(source, getDefaultPrice(symbol, source), 
                        API_WEIGHTS.get(source), false);
                } else {
                    return new PriceResult(source, price, API_WEIGHTS.get(source), true);
                }
            });
    }
    
    private WeightedAverageResult calculateWeightedAverage(List<PriceResult> results, String symbol) {
        double totalWeightedPrice = 0.0;
        double totalWeight = 0.0;
        int successfulCalls = 0;
        
        for (PriceResult result : results) {
            totalWeightedPrice += result.getPrice() * result.getWeight();
            totalWeight += result.getWeight();
            if (result.isSuccessful()) {
                successfulCalls++;
            }
        }
        
        double weightedAverage = totalWeightedPrice / totalWeight;
        double confidenceScore = calculateConfidenceScore(results);
        
        return new WeightedAverageResult(symbol, weightedAverage, confidenceScore, 
            successfulCalls, results);
    }
    
    private double calculateConfidenceScore(List<PriceResult> results) {
        double successfulWeight = results.stream()
            .filter(PriceResult::isSuccessful)
            .mapToDouble(PriceResult::getWeight)
            .sum();
        return successfulWeight; // Confidence based on successful API weights
    }
}

// Supporting classes
class PriceResult {
    private final String source;
    private final Double price;
    private final Double weight;
    private final boolean successful;
    
    // Constructor, getters, etc.
}

class WeightedAverageResult {
    private final String symbol;
    private final Double averagePrice;
    private final Double confidenceScore;
    private final Integer successfulApiCalls;
    private final List<PriceResult> individualResults;
    
    // Constructor, getters, etc.
}
```

**Advantages:**
- **Quality awareness**: Weights reflect API reliability/accuracy
- **Confidence scoring**: Provides reliability metric
- **Sophisticated fallback**: Maintains quality even with partial failures

**Disadvantages:**
- **Complex configuration**: Requires weight tuning and maintenance
- **Higher complexity**: More code to maintain and debug

### Solution 4: Circuit Breaker Pattern with Fallback

This approach implements circuit breaker pattern for each API with intelligent fallback strategies.

```java
@Component
public class CircuitBreakerStockAggregator {
    
    private final Map<String, CircuitBreaker> circuitBreakers;
    private final StockPriceCache priceCache;
    
    public CircuitBreakerStockAggregator(StockPriceCache priceCache) {
        this.priceCache = priceCache;
        this.circuitBreakers = initializeCircuitBreakers();
    }
    
    public CompletableFuture<ResilientPriceResult> getResilientAveragePrice(String symbol) {
        CompletableFuture<Double> yahooPrice = createResilientApiCall(
            () -> fetchFromYahooFinance(symbol), "YAHOO", symbol);
            
        CompletableFuture<Double> alphaPrice = createResilientApiCall(
            () -> fetchFromAlphaVantage(symbol), "ALPHAVANTAGE", symbol);
            
        CompletableFuture<Double> iexPrice = createResilientApiCall(
            () -> fetchFromIEXCloud(symbol), "IEX", symbol);
        
        return CompletableFuture.allOf(yahooPrice, alphaPrice, iexPrice)
            .thenApply(v -> {
                List<Double> prices = Arrays.asList(
                    yahooPrice.join(),
                    alphaPrice.join(),
                    iexPrice.join()
                );
                
                double average = prices.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
                
                return new ResilientPriceResult(symbol, average, 
                    getCircuitBreakerStates(), prices);
            });
    }
    
    private CompletableFuture<Double> createResilientApiCall(
            Supplier<Double> apiCall, String source, String symbol) {
        
        CircuitBreaker circuitBreaker = circuitBreakers.get(source);
        
        return CompletableFuture
            .supplyAsync(() -> {
                if (circuitBreaker.isOpen()) {
                    logInfo(source + " circuit breaker is OPEN, using fallback");
                    return getFallbackPrice(symbol, source);
                }
                
                try {
                    Double price = apiCall.get();
                    circuitBreaker.recordSuccess();
                    priceCache.updatePrice(symbol, source, price);
                    return price;
                } catch (Exception ex) {
                    circuitBreaker.recordFailure();
                    logError(source + " API call failed", ex);
                    return getFallbackPrice(symbol, source);
                }
            }, executor)
            .orTimeout(3, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                circuitBreaker.recordFailure();
                return getFallbackPrice(symbol, source);
            });
    }
    
    private Double getFallbackPrice(String symbol, String source) {
        // Try cache first
        Optional<Double> cachedPrice = priceCache.getCachedPrice(symbol, source);
        if (cachedPrice.isPresent()) {
            logInfo("Using cached price for " + symbol + " from " + source);
            return cachedPrice.get();
        }
        
        // Try cross-source estimation
        Optional<Double> estimatedPrice = priceCache.getEstimatedPrice(symbol);
        if (estimatedPrice.isPresent()) {
            logInfo("Using estimated price for " + symbol);
            return estimatedPrice.get();
        }
        
        // Final fallback to market average
        double marketAverage = getMarketAveragePrice(symbol);
        logWarning("Using market average fallback for " + symbol);
        return marketAverage;
    }
    
    private Map<String, CircuitBreaker> initializeCircuitBreakers() {
        return Map.of(
            "YAHOO", new CircuitBreaker(5, 60000, 30000), // 5 failures, 60s timeout, 30s retry
            "ALPHAVANTAGE", new CircuitBreaker(3, 45000, 20000),
            "IEX", new CircuitBreaker(4, 50000, 25000)
        );
    }
}

// Circuit Breaker implementation
class CircuitBreaker {
    private final int failureThreshold;
    private final long timeoutMs;
    private final long retryAfterMs;
    private int failureCount = 0;
    private long lastFailureTime = 0;
    private CircuitState state = CircuitState.CLOSED;
    
    public synchronized boolean isOpen() {
        if (state == CircuitState.OPEN && 
            System.currentTimeMillis() - lastFailureTime > retryAfterMs) {
            state = CircuitState.HALF_OPEN;
            return false;
        }
        return state == CircuitState.OPEN;
    }
    
    public synchronized void recordSuccess() {
        failureCount = 0;
        state = CircuitState.CLOSED;
    }
    
    public synchronized void recordFailure() {
        failureCount++;
        lastFailureTime = System.currentTimeMillis();
        if (failureCount >= failureThreshold) {
            state = CircuitState.OPEN;
        }
    }
}

enum CircuitState { CLOSED, OPEN, HALF_OPEN }
```

**Advantages:**
- **Production-ready resilience**: Handles cascading failures
- **Intelligent fallbacks**: Multiple fallback strategies
- **Self-healing**: Circuit breaker auto-recovery
- **Performance protection**: Prevents hammering failed services

**Disadvantages:**
- **High complexity**: Significant implementation overhead
- **Configuration intensive**: Many parameters to tune
- **Monitoring requirements**: Needs comprehensive observability

### Solution 5: Reactive Streams with Timeout and Retry

This approach uses reactive patterns with built-in retry and timeout mechanisms.

```java
@Service
public class ReactiveStockAggregator {
    
    public CompletableFuture<Double> getAverageWithRetry(String symbol) {
        List<CompletableFuture<Double>> apiCalls = Arrays.asList(
            createRetryableApiCall(() -> fetchFromYahooFinance(symbol), "YAHOO", symbol),
            createRetryableApiCall(() -> fetchFromAlphaVantage(symbol), "ALPHAVANTAGE", symbol),
            createRetryableApiCall(() -> fetchFromIEXCloud(symbol), "IEX", symbol)
        );
        
        return CompletableFuture.allOf(apiCalls.toArray(new CompletableFuture[0]))
            .thenApply(v -> apiCalls.stream()
                .mapToDouble(CompletableFuture::join)
                .average()
                .orElse(0.0));
    }
    
    private CompletableFuture<Double> createRetryableApiCall(
            Supplier<Double> apiCall, String source, String symbol) {
        
        return CompletableFuture
            .supplyAsync(() -> executeWithRetry(apiCall, source, 3), executor)
            .orTimeout(10, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                logError("All retries failed for " + source, ex);
                return getDefaultPrice(symbol, source);
            });
    }
    
    private Double executeWithRetry(Supplier<Double> apiCall, String source, int maxRetries) {
        int attempt = 0;
        Exception lastException = null;
        
        while (attempt < maxRetries) {
            try {
                return apiCall.get();
            } catch (Exception ex) {
                lastException = ex;
                attempt++;
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(1000 * attempt); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupted during retry", ie);
                    }
                }
            }
        }
        
        throw new RuntimeException("Max retries exceeded for " + source, lastException);
    }
}
```

## Comprehensive Production Implementation

```java
@Service
@Slf4j
public class ProductionStockPriceAggregator {
    private final ExecutorService executor;
    private final StockPriceCache cache;
    private final MeterRegistry meterRegistry;
    
    public ProductionStockPriceAggregator(StockPriceCache cache, MeterRegistry meterRegistry) {
        this.executor = Executors.newFixedThreadPool(6, 
            new ThreadFactoryBuilder().setNameFormat("stock-api-%d").build());
        this.cache = cache;
        this.meterRegistry = meterRegistry;
    }
    
    public CompletableFuture<AggregatedStockPrice> getAggregatedPrice(String symbol) {
        Timer.Sample sample = Timer.start(meterRegistry);
        
        try {
            CompletableFuture<ApiResult> yahooResult = createMonitoredApiCall(
                () -> fetchFromYahooFinance(symbol), "yahoo", symbol);
                
            CompletableFuture<ApiResult> alphaResult = createMonitoredApiCall(
                () -> fetchFromAlphaVantage(symbol), "alphavantage", symbol);
                
            CompletableFuture<ApiResult> iexResult = createMonitoredApiCall(
                () -> fetchFromIEXCloud(symbol), "iex", symbol);
            
            return CompletableFuture.allOf(yahooResult, alphaResult, iexResult)
                .thenApply(v -> {
                    List<ApiResult> results = Arrays.asList(
                        yahooResult.join(),
                        alphaResult.join(),
                        iexResult.join()
                    );
                    
                    return aggregateResults(symbol, results);
                })
                .whenComplete((result, ex) -> {
                    sample.stop(Timer.builder("stock.aggregation.duration")
                        .tag("symbol", symbol)
                        .register(meterRegistry));
                        
                    if (ex != null) {
                        meterRegistry.counter("stock.aggregation.errors",
                            "symbol", symbol).increment();
                    }
                });
                
        } catch (Exception ex) {
            return CompletableFuture.failedFuture(ex);
        }
    }
    
    private CompletableFuture<ApiResult> createMonitoredApiCall(
            Supplier<Double> apiCall, String source, String symbol) {
        
        return CompletableFuture
            .supplyAsync(() -> {
                Timer.Sample apiTimer = Timer.start(meterRegistry);
                try {
                    Double price = apiCall.get();
                    cache.updatePrice(symbol, source, price);
                    meterRegistry.counter("stock.api.success", "source", source).increment();
                    return new ApiResult(source, price, true, null);
                } catch (Exception ex) {
                    meterRegistry.counter("stock.api.failure", "source", source).increment();
                    Double fallbackPrice = getFallbackPrice(symbol, source);
                    return new ApiResult(source, fallbackPrice, false, ex.getMessage());
                } finally {
                    apiTimer.stop(Timer.builder("stock.api.duration")
                        .tag("source", source)
                        .register(meterRegistry));
                }
            }, executor)
            .orTimeout(5, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                meterRegistry.counter("stock.api.timeout", "source", source).increment();
                Double fallbackPrice = getFallbackPrice(symbol, source);
                return new ApiResult(source, fallbackPrice, false, "Timeout: " + ex.getMessage());
            });
    }
    
    private AggregatedStockPrice aggregateResults(String symbol, List<ApiResult> results) {
        List<Double> prices = results.stream()
            .map(ApiResult::getPrice)
            .collect(Collectors.toList());
            
        double average = prices.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
            
        long successfulCalls = results.stream()
            .mapToLong(r -> r.isSuccessful() ? 1 : 0)
            .sum();
            
        double confidence = (double) successfulCalls / results.size();
        
        return new AggregatedStockPrice(symbol, average, confidence, 
            Instant.now(), results);
    }
    
    @PreDestroy
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

## Solution Comparison

| Solution | Fault Tolerance | Performance | Complexity | Production Ready | Best Use Case |
|----------|----------------|-------------|------------|-----------------|---------------|
| **Individual Exception Handling** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐ | **General purpose, reliable** |
| Successful Results Only | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐ | High data quality requirements |
| Weighted Average | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Different API reliability levels |
| Circuit Breaker | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | **High-volume production systems** |
| Reactive with Retry | ⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | Transient failure tolerance |

## Recommended Solution: Individual Exception Handling (Solution 1)

**Why Solution 1 is the best choice:**

### 1. **Optimal Balance**
- Simple enough to understand and maintain
- Robust enough for production use
- Excellent fault tolerance without over-engineering

### 2. **Performance Excellence**
- Full parallelism with concurrent API calls
- Fast failure handling with immediate fallbacks
- Timeout protection prevents hanging

### 3. **Reliability**
- **Always returns a result** - never fails completely
- **Graceful degradation** - works even if all APIs fail
- **Individual error isolation** - one API failure doesn't affect others

### 4. **Maintainability**
- Clean, readable code structure
- Easy to modify fallback strategies
- Simple debugging and monitoring

### 5. **Production Considerations**
```java
// Easy to add monitoring
.exceptionally(ex -> {
    metricsService.incrementApiFailure(source);
    alertService.notifyApiFailure(source, ex);
    return getDefaultPrice(symbol, source);
});

// Easy to customize timeouts per API
.orTimeout(getTimeoutForSource(source), TimeUnit.SECONDS)

// Easy to implement different fallback strategies
private Double getDefaultPrice(String symbol, String source) {
    return fallbackStrategies.get(source).getFallbackPrice(symbol);
}
```

## When to Choose Other Solutions

- **Solution 3 (Weighted Average)**: When you have different API reliability/accuracy levels and need sophisticated quality scoring
- **Solution 4 (Circuit Breaker)**: For high-volume systems where API failures can cascade and cause system-wide issues
- **Solution 2 (Successful Only)**: When data purity is more important than availability (research/analytics scenarios)
- **Solution 5 (Reactive/Retry)**: When dealing with frequently intermittent network issues

## Implementation Checklist

✅ **Parallel execution** for all API calls  
✅ **Individual timeout handling** per API  
✅ **Graceful error handling** with meaningful fallbacks  
✅ **Comprehensive logging** for debugging  
✅ **Metrics and monitoring** integration  
✅ **Resource cleanup** with proper executor shutdown  
✅ **Configuration externalization** for timeouts and fallbacks  
✅ **Unit and integration testing** for failure scenarios  

The recommended solution provides the best foundation that can be enhanced with additional patterns (caching, circuit breakers, etc.) as your system scales and requirements evolve.