# AtomicReference with ExecutorService: Comprehensive Guide

## Overview

`AtomicReference` is a thread-safe reference wrapper that provides atomic operations on object references. When combined with `ExecutorService`, it enables safe concurrent access to shared state across multiple threads without explicit synchronization.

## Core Concepts

### Why AtomicReference with ExecutorService?

- **Thread Safety**: Multiple threads can safely read/write shared references
- **Lock-Free Programming**: Avoid traditional synchronization overhead
- **Compare-and-Swap (CAS)**: Atomic updates based on expected values
- **Non-Blocking**: Operations don't block threads waiting for locks

## Scenario 1: Contract Management Service

### Basic Contract State Management

```java
@Service
public class ContractManagementService {
    
    // Thread-safe reference to current contract state
    private final AtomicReference<ContractState> currentState = 
        new AtomicReference<>(ContractState.DRAFT);
    
    // Thread-safe reference to active contract
    private final AtomicReference<Contract> activeContract = 
        new AtomicReference<>();
    
    // Thread-safe reference to contract metadata
    private final AtomicReference<ContractMetadata> metadata = 
        new AtomicReference<>(new ContractMetadata());
    
    private final ExecutorService executorService = 
        Executors.newFixedThreadPool(10);
    
    /**
     * Process contract transitions concurrently
     */
    public CompletableFuture<Boolean> processContractTransition(
            String contractId, ContractState targetState) {
        
        return CompletableFuture.supplyAsync(() -> {
            // Get current state atomically
            ContractState currentStateValue = currentState.get();
            
            // Validate transition
            if (!isValidTransition(currentStateValue, targetState)) {
                throw new IllegalStateException(
                    String.format("Invalid transition from %s to %s", 
                        currentStateValue, targetState));
            }
            
            // Attempt atomic state update with CAS
            boolean transitionSuccessful = currentState.compareAndSet(
                currentStateValue, targetState);
            
            if (transitionSuccessful) {
                logStateTransition(contractId, currentStateValue, targetState);
                notifyStateChange(contractId, targetState);
            }
            
            return transitionSuccessful;
            
        }, executorService).exceptionally(ex -> {
            logError("Contract transition failed", ex);
            return false;
        });
    }
    
    /**
     * Update contract with optimistic locking using AtomicReference
     */
    public CompletableFuture<Contract> updateContract(
            String contractId, Function<Contract, Contract> updateFunction) {
        
        return CompletableFuture.supplyAsync(() -> {
            Contract updatedContract = null;
            boolean updateSuccessful = false;
            int retryCount = 0;
            final int maxRetries = 3;
            
            // Retry loop for CAS operations
            while (!updateSuccessful && retryCount < maxRetries) {
                // Get current contract reference
                Contract currentContract = activeContract.get();
                
                if (currentContract == null || !currentContract.getId().equals(contractId)) {
                    throw new IllegalStateException("Contract not found: " + contractId);
                }
                
                // Apply update function to create new contract version
                updatedContract = updateFunction.apply(currentContract);
                
                // Atomic update with compare-and-set
                updateSuccessful = activeContract.compareAndSet(
                    currentContract, updatedContract);
                
                if (!updateSuccessful) {
                    retryCount++;
                    // Brief pause before retry to reduce contention
                    try {
                        Thread.sleep(10 * retryCount);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Update interrupted", e);
                    }
                }
            }
            
            if (!updateSuccessful) {
                throw new RuntimeException(
                    "Failed to update contract after " + maxRetries + " retries");
            }
            
            // Update metadata atomically
            updateMetadata(contractId, updatedContract);
            
            return updatedContract;
            
        }, executorService);
    }
    
    /**
     * Batch contract processing with atomic counters
     */
    public CompletableFuture<BatchProcessingResult> processBatchContracts(
            List<Contract> contracts) {
        
        // Atomic counters for tracking progress
        AtomicReference<BatchStats> stats = new AtomicReference<>(new BatchStats());
        
        List<CompletableFuture<ProcessingResult>> processingTasks = contracts.stream()
            .map(contract -> CompletableFuture.supplyAsync(() -> {
                try {
                    ProcessingResult result = processIndividualContract(contract);
                    
                    // Atomically update batch statistics
                    stats.updateAndGet(currentStats -> {
                        BatchStats newStats = new BatchStats(currentStats);
                        if (result.isSuccessful()) {
                            newStats.incrementSuccessCount();
                        } else {
                            newStats.incrementFailureCount();
                        }
                        newStats.addProcessingTime(result.getProcessingTimeMs());
                        return newStats;
                    });
                    
                    return result;
                    
                } catch (Exception ex) {
                    // Update failure count atomically
                    stats.updateAndGet(currentStats -> {
                        BatchStats newStats = new BatchStats(currentStats);
                        newStats.incrementFailureCount();
                        return newStats;
                    });
                    
                    return ProcessingResult.failure(contract.getId(), ex.getMessage());
                }
            }, executorService))
            .collect(Collectors.toList());
        
        return CompletableFuture.allOf(
                processingTasks.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                List<ProcessingResult> results = processingTasks.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
                
                return new BatchProcessingResult(results, stats.get());
            });
    }
    
    private void updateMetadata(String contractId, Contract contract) {
        metadata.updateAndGet(currentMetadata -> {
            ContractMetadata newMetadata = new ContractMetadata(currentMetadata);
            newMetadata.setLastUpdated(Instant.now());
            newMetadata.setVersion(currentMetadata.getVersion() + 1);
            newMetadata.setContractId(contractId);
            return newMetadata;
        });
    }
}

// Supporting classes
enum ContractState {
    DRAFT, UNDER_REVIEW, APPROVED, ACTIVE, SUSPENDED, TERMINATED
}

class Contract {
    private final String id;
    private final String content;
    private final Instant createdAt;
    private final ContractState state;
    
    // Constructor, getters, builder pattern, etc.
}

class ContractMetadata {
    private String contractId;
    private Instant lastUpdated;
    private int version;
    private Map<String, Object> properties;
    
    public ContractMetadata() {
        this.lastUpdated = Instant.now();
        this.version = 1;
        this.properties = new ConcurrentHashMap<>();
    }
    
    public ContractMetadata(ContractMetadata other) {
        this.contractId = other.contractId;
        this.lastUpdated = other.lastUpdated;
        this.version = other.version;
        this.properties = new ConcurrentHashMap<>(other.properties);
    }
    
    // Getters and setters
}

class BatchStats {
    private int successCount;
    private int failureCount;
    private long totalProcessingTimeMs;
    
    public BatchStats() {}
    
    public BatchStats(BatchStats other) {
        this.successCount = other.successCount;
        this.failureCount = other.failureCount;
        this.totalProcessingTimeMs = other.totalProcessingTimeMs;
    }
    
    public void incrementSuccessCount() { successCount++; }
    public void incrementFailureCount() { failureCount++; }
    public void addProcessingTime(long timeMs) { totalProcessingTimeMs += timeMs; }
    
    // Getters
}
```

## Scenario 2: API Client with Connection Management

### Thread-Safe API Client with Connection Pooling

```java
@Component
public class ThreadSafeApiClient {
    
    // Atomic reference to current connection pool
    private final AtomicReference<ConnectionPool> connectionPool = 
        new AtomicReference<>();
    
    // Atomic reference to API configuration
    private final AtomicReference<ApiConfiguration> apiConfig = 
        new AtomicReference<>();
    
    // Atomic reference to circuit breaker state
    private final AtomicReference<CircuitBreakerState> circuitBreakerState = 
        new AtomicReference<>(new CircuitBreakerState());
    
    // Atomic reference to request statistics
    private final AtomicReference<RequestStatistics> requestStats = 
        new AtomicReference<>(new RequestStatistics());
    
    private final ExecutorService apiExecutor = 
        Executors.newFixedThreadPool(20, 
            new ThreadFactoryBuilder()
                .setNameFormat("api-client-%d")
                .setDaemon(true)
                .build());
    
    @PostConstruct
    public void initialize() {
        // Initialize with default configuration
        ApiConfiguration defaultConfig = ApiConfiguration.defaultConfig();
        apiConfig.set(defaultConfig);
        
        // Initialize connection pool
        ConnectionPool pool = new ConnectionPool(defaultConfig);
        connectionPool.set(pool);
    }
    
    /**
     * Execute API request with automatic retry and circuit breaker
     */
    public <T> CompletableFuture<ApiResponse<T>> executeRequest(
            ApiRequest request, Class<T> responseType) {
        
        return CompletableFuture.supplyAsync(() -> {
            // Check circuit breaker state
            CircuitBreakerState currentState = circuitBreakerState.get();
            if (currentState.isOpen()) {
                throw new CircuitBreakerOpenException(
                    "Circuit breaker is OPEN, request rejected");
            }
            
            ApiResponse<T> response = null;
            Exception lastException = null;
            
            // Get current configuration atomically
            ApiConfiguration config = apiConfig.get();
            int maxRetries = config.getMaxRetries();
            
            for (int attempt = 0; attempt <= maxRetries; attempt++) {
                try {
                    // Execute request with current connection pool
                    response = executeWithConnectionPool(request, responseType);
                    
                    // Update success statistics atomically
                    updateRequestStatistics(true, response.getResponseTimeMs());
                    
                    // Reset circuit breaker on success
                    resetCircuitBreakerOnSuccess();
                    
                    return response;
                    
                } catch (Exception ex) {
                    lastException = ex;
                    
                    // Update failure statistics
                    updateRequestStatistics(false, 0);
                    
                    // Update circuit breaker state
                    updateCircuitBreakerOnFailure();
                    
                    if (attempt < maxRetries && isRetryableException(ex)) {
                        // Wait before retry with exponential backoff
                        waitForRetry(attempt, config.getRetryDelayMs());
                    }
                }
            }
            
            throw new ApiException("Request failed after " + maxRetries + " retries", 
                lastException);
                
        }, apiExecutor);
    }
    
    /**
     * Update API configuration atomically
     */
    public CompletableFuture<Void> updateConfiguration(ApiConfiguration newConfig) {
        return CompletableFuture.runAsync(() -> {
            ApiConfiguration oldConfig = apiConfig.get();
            
            // Atomic configuration update
            if (apiConfig.compareAndSet(oldConfig, newConfig)) {
                // If connection pool settings changed, recreate pool
                if (connectionPoolSettingsChanged(oldConfig, newConfig)) {
                    recreateConnectionPool(newConfig);
                }
                
                logConfigurationChange(oldConfig, newConfig);
            }
        }, apiExecutor);
    }
    
    /**
     * Recreate connection pool with new configuration
     */
    private void recreateConnectionPool(ApiConfiguration newConfig) {
        ConnectionPool oldPool = connectionPool.get();
        ConnectionPool newPool = new ConnectionPool(newConfig);
        
        // Atomic pool replacement
        if (connectionPool.compareAndSet(oldPool, newPool)) {
            // Gracefully shutdown old pool
            CompletableFuture.runAsync(() -> {
                try {
                    oldPool.shutdown(30, TimeUnit.SECONDS);
                } catch (Exception ex) {
                    logError("Error shutting down old connection pool", ex);
                }
            }, apiExecutor);
        }
    }
    
    /**
     * Update request statistics atomically
     */
    private void updateRequestStatistics(boolean success, long responseTimeMs) {
        requestStats.updateAndGet(currentStats -> {
            RequestStatistics newStats = new RequestStatistics(currentStats);
            if (success) {
                newStats.incrementSuccessCount();
                newStats.addResponseTime(responseTimeMs);
            } else {
                newStats.incrementFailureCount();
            }
            newStats.updateLastRequestTime();
            return newStats;
        });
    }
    
    /**
     * Update circuit breaker state on failure
     */
    private void updateCircuitBreakerOnFailure() {
        circuitBreakerState.updateAndGet(currentState -> {
            CircuitBreakerState newState = new CircuitBreakerState(currentState);
            newState.recordFailure();
            
            // Check if threshold exceeded
            ApiConfiguration config = apiConfig.get();
            if (newState.getFailureCount() >= config.getCircuitBreakerThreshold()) {
                newState.openCircuit();
            }
            
            return newState;
        });
    }
    
    /**
     * Reset circuit breaker on successful request
     */
    private void resetCircuitBreakerOnSuccess() {
        circuitBreakerState.updateAndGet(currentState -> {
            if (currentState.isHalfOpen() || currentState.getFailureCount() > 0) {
                CircuitBreakerState newState = new CircuitBreakerState();
                newState.closeCircuit();
                return newState;
            }
            return currentState;
        });
    }
    
    /**
     * Get current API statistics
     */
    public ApiStatistics getCurrentStatistics() {
        RequestStatistics stats = requestStats.get();
        CircuitBreakerState cbState = circuitBreakerState.get();
        ApiConfiguration config = apiConfig.get();
        
        return new ApiStatistics(stats, cbState, config);
    }
    
    /**
     * Health check method
     */
    public CompletableFuture<HealthStatus> checkHealth() {
        return CompletableFuture.supplyAsync(() -> {
            RequestStatistics stats = requestStats.get();
            CircuitBreakerState cbState = circuitBreakerState.get();
            ConnectionPool pool = connectionPool.get();
            
            HealthStatus.Builder builder = HealthStatus.builder();
            
            // Check circuit breaker
            if (cbState.isOpen()) {
                builder.unhealthy("Circuit breaker is OPEN");
            }
            
            // Check connection pool
            if (pool.getActiveConnections() == 0) {
                builder.degraded("No active connections");
            }
            
            // Check error rate
            double errorRate = stats.getErrorRate();
            if (errorRate > 0.1) { // 10% error rate threshold
                builder.degraded(String.format("High error rate: %.2f%%", errorRate * 100));
            }
            
            return builder.build();
            
        }, apiExecutor);
    }
    
    @PreDestroy
    public void shutdown() {
        try {
            // Shutdown connection pool
            ConnectionPool pool = connectionPool.get();
            if (pool != null) {
                pool.shutdown(30, TimeUnit.SECONDS);
            }
            
            // Shutdown executor
            apiExecutor.shutdown();
            if (!apiExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                apiExecutor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            apiExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

// Supporting classes for API Client
class ApiConfiguration {
    private final String baseUrl;
    private final int maxRetries;
    private final long retryDelayMs;
    private final int connectionPoolSize;
    private final int circuitBreakerThreshold;
    private final long timeoutMs;
    
    // Constructor, getters, builder pattern
    
    public static ApiConfiguration defaultConfig() {
        return new ApiConfiguration.Builder()
            .baseUrl("https://api.example.com")
            .maxRetries(3)
            .retryDelayMs(1000)
            .connectionPoolSize(10)
            .circuitBreakerThreshold(5)
            .timeoutMs(30000)
            .build();
    }
}

class CircuitBreakerState {
    private int failureCount;
    private long lastFailureTime;
    private CircuitState state;
    
    public CircuitBreakerState() {
        this.failureCount = 0;
        this.lastFailureTime = 0;
        this.state = CircuitState.CLOSED;
    }
    
    public CircuitBreakerState(CircuitBreakerState other) {
        this.failureCount = other.failureCount;
        this.lastFailureTime = other.lastFailureTime;
        this.state = other.state;
    }
    
    public void recordFailure() {
        this.failureCount++;
        this.lastFailureTime = System.currentTimeMillis();
    }
    
    public void openCircuit() {
        this.state = CircuitState.OPEN;
    }
    
    public void closeCircuit() {
        this.failureCount = 0;
        this.state = CircuitState.CLOSED;
    }
    
    public boolean isOpen() {
        if (state == CircuitState.OPEN) {
            // Check if enough time has passed to try half-open
            long timeSinceLastFailure = System.currentTimeMillis() - lastFailureTime;
            if (timeSinceLastFailure > 60000) { // 60 seconds
                state = CircuitState.HALF_OPEN;
                return false;
            }
            return true;
        }
        return false;
    }
    
    public boolean isHalfOpen() {
        return state == CircuitState.HALF_OPEN;
    }
    
    // Getters
}

class RequestStatistics {
    private long successCount;
    private long failureCount;
    private long totalResponseTime;
    private long lastRequestTime;
    
    public RequestStatistics() {
        this.lastRequestTime = System.currentTimeMillis();
    }
    
    public RequestStatistics(RequestStatistics other) {
        this.successCount = other.successCount;
        this.failureCount = other.failureCount;
        this.totalResponseTime = other.totalResponseTime;
        this.lastRequestTime = other.lastRequestTime;
    }
    
    public void incrementSuccessCount() { successCount++; }
    public void incrementFailureCount() { failureCount++; }
    public void addResponseTime(long timeMs) { totalResponseTime += timeMs; }
    public void updateLastRequestTime() { lastRequestTime = System.currentTimeMillis(); }
    
    public double getErrorRate() {
        long total = successCount + failureCount;
        return total == 0 ? 0.0 : (double) failureCount / total;
    }
    
    public double getAverageResponseTime() {
        return successCount == 0 ? 0.0 : (double) totalResponseTime / successCount;
    }
    
    // Getters
}
```

## Scenario 3: Caching Service with AtomicReference

### Thread-Safe Cache Implementation

```java
@Service
public class AtomicCacheService<K, V> {
    
    // Atomic reference to cache storage
    private final AtomicReference<ConcurrentHashMap<K, CacheEntry<V>>> cache = 
        new AtomicReference<>(new ConcurrentHashMap<>());
    
    // Atomic reference to cache statistics
    private final AtomicReference<CacheStatistics> statistics = 
        new AtomicReference<>(new CacheStatistics());
    
    // Atomic reference to cache configuration
    private final AtomicReference<CacheConfiguration> configuration = 
        new AtomicReference<>(CacheConfiguration.defaultConfig());
    
    private final ExecutorService cleanupExecutor = 
        Executors.newSingleThreadScheduledExecutor(
            new ThreadFactoryBuilder()
                .setNameFormat("cache-cleanup-%d")
                .setDaemon(true)
                .build());
    
    /**
     * Get value from cache with automatic loading
     */
    public CompletableFuture<V> get(K key, Function<K, V> loader) {
        return CompletableFuture.supplyAsync(() -> {
            ConcurrentHashMap<K, CacheEntry<V>> currentCache = cache.get();
            CacheEntry<V> entry = currentCache.get(key);
            
            // Check if entry exists and is not expired
            if (entry != null && !entry.isExpired(configuration.get().getTtlMs())) {
                // Update hit statistics
                updateStatistics(true);
                return entry.getValue();
            }
            
            // Cache miss or expired entry - load value
            updateStatistics(false);
            
            try {
                V value = loader.apply(key);
                
                // Create new cache entry
                CacheEntry<V> newEntry = new CacheEntry<>(value, System.currentTimeMillis());
                
                // Atomic cache update
                updateCache(key, newEntry);
                
                return value;
                
            } catch (Exception ex) {
                throw new CacheLoadException("Failed to load value for key: " + key, ex);
            }
            
        }, cleanupExecutor);
    }
    
    /**
     * Put value in cache atomically
     */
    public CompletableFuture<Void> put(K key, V value) {
        return CompletableFuture.runAsync(() -> {
            CacheEntry<V> entry = new CacheEntry<>(value, System.currentTimeMillis());
            updateCache(key, entry);
        }, cleanupExecutor);
    }
    
    /**
     * Update cache with new entry
     */
    private void updateCache(K key, CacheEntry<V> entry) {
        boolean updated = false;
        int retryCount = 0;
        final int maxRetries = 3;
        
        while (!updated && retryCount < maxRetries) {
            ConcurrentHashMap<K, CacheEntry<V>> currentCache = cache.get();
            ConcurrentHashMap<K, CacheEntry<V>> newCache = new ConcurrentHashMap<>(currentCache);
            
            // Add/update entry
            newCache.put(key, entry);
            
            // Check cache size limits
            CacheConfiguration config = configuration.get();
            if (newCache.size() > config.getMaxSize()) {
                evictOldestEntries(newCache, config.getMaxSize());
            }
            
            // Atomic cache replacement
            updated = cache.compareAndSet(currentCache, newCache);
            
            if (!updated) {
                retryCount++;
            }
        }
        
        if (!updated) {
            throw new RuntimeException("Failed to update cache after " + maxRetries + " retries");
        }
    }
    
    /**
     * Remove expired entries from cache
     */
    public CompletableFuture<Integer> cleanupExpiredEntries() {
        return CompletableFuture.supplyAsync(() -> {
            CacheConfiguration config = configuration.get();
            long ttlMs = config.getTtlMs();
            int removedCount = 0;
            
            boolean cleaned = false;
            int retryCount = 0;
            final int maxRetries = 3;
            
            while (!cleaned && retryCount < maxRetries) {
                ConcurrentHashMap<K, CacheEntry<V>> currentCache = cache.get();
                ConcurrentHashMap<K, CacheEntry<V>> newCache = new ConcurrentHashMap<>();
                
                // Copy non-expired entries
                for (Map.Entry<K, CacheEntry<V>> entry : currentCache.entrySet()) {
                    if (!entry.getValue().isExpired(ttlMs)) {
                        newCache.put(entry.getKey(), entry.getValue());
                    } else {
                        removedCount++;
                    }
                }
                
                // Atomic cache replacement
                cleaned = cache.compareAndSet(currentCache, newCache);
                
                if (!cleaned) {
                    retryCount++;
                    removedCount = 0; // Reset count for retry
                }
            }
            
            if (cleaned && removedCount > 0) {
                logInfo("Cleaned up " + removedCount + " expired cache entries");
            }
            
            return removedCount;
            
        }, cleanupExecutor);
    }
    
    /**
     * Update cache configuration atomically
     */
    public CompletableFuture<Void> updateConfiguration(CacheConfiguration newConfig) {
        return CompletableFuture.runAsync(() -> {
            CacheConfiguration oldConfig = configuration.get();
            
            if (configuration.compareAndSet(oldConfig, newConfig)) {
                logInfo("Cache configuration updated");
                
                // If max size decreased, trigger cleanup
                if (newConfig.getMaxSize() < oldConfig.getMaxSize()) {
                    CompletableFuture.runAsync(this::enforceMaxSize, cleanupExecutor);
                }
            }
        }, cleanupExecutor);
    }
    
    /**
     * Enforce maximum cache size
     */
    private void enforceMaxSize() {
        CacheConfiguration config = configuration.get();
        int maxSize = config.getMaxSize();
        
        boolean resized = false;
        int retryCount = 0;
        final int maxRetries = 3;
        
        while (!resized && retryCount < maxRetries) {
            ConcurrentHashMap<K, CacheEntry<V>> currentCache = cache.get();
            
            if (currentCache.size() <= maxSize) {
                return; // Already within limits
            }
            
            ConcurrentHashMap<K, CacheEntry<V>> newCache = new ConcurrentHashMap<>(currentCache);
            evictOldestEntries(newCache, maxSize);
            
            resized = cache.compareAndSet(currentCache, newCache);
            
            if (!resized) {
                retryCount++;
            }
        }
    }
    
    /**
     * Evict oldest entries to maintain size limit
     */
    private void evictOldestEntries(ConcurrentHashMap<K, CacheEntry<V>> cache, int maxSize) {
        if (cache.size() <= maxSize) {
            return;
        }
        
        // Sort entries by timestamp (oldest first)
        List<Map.Entry<K, CacheEntry<V>>> sortedEntries = cache.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(
                Comparator.comparing(CacheEntry::getTimestamp)))
            .collect(Collectors.toList());
        
        // Remove oldest entries
        int entriesToRemove = cache.size() - maxSize;
        for (int i = 0; i < entriesToRemove && i < sortedEntries.size(); i++) {
            cache.remove(sortedEntries.get(i).getKey());
        }
    }
    
    /**
     * Update cache statistics atomically
     */
    private void updateStatistics(boolean hit) {
        statistics.updateAndGet(currentStats -> {
            CacheStatistics newStats = new CacheStatistics(currentStats);
            if (hit) {
                newStats.incrementHits();
            } else {
                newStats.incrementMisses();
            }
            return newStats;
        });
    }
    
    /**
     * Get current cache statistics
     */
    public CacheStatistics getStatistics() {
        CacheStatistics stats = statistics.get();
        ConcurrentHashMap<K, CacheEntry<V>> currentCache = cache.get();
        
        return new CacheStatistics.Builder(stats)
            .currentSize(currentCache.size())
            .build();
    }
    
    /**
     * Clear cache completely
     */
    public CompletableFuture<Void> clear() {
        return CompletableFuture.runAsync(() -> {
            cache.set(new ConcurrentHashMap<>());
            statistics.set(new CacheStatistics());
            logInfo("Cache cleared");
        }, cleanupExecutor);
    }
}

// Supporting classes for Cache
class CacheEntry<V> {
    private final V value;
    private final long timestamp;
    
    public CacheEntry(V value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }
    
    public boolean isExpired(long ttlMs) {
        return System.currentTimeMillis() - timestamp > ttlMs;
    }
    
    // Getters
}

class CacheConfiguration {
    private final int maxSize;
    private final long ttlMs;
    private final boolean enableAutoCleanup;
    private final long cleanupIntervalMs;
    
    // Constructor, getters, builder
    
    public static CacheConfiguration defaultConfig() {
        return new CacheConfiguration.Builder()
            .maxSize(1000)
            .ttlMs(TimeUnit.HOURS.toMillis(1))
            .enableAutoCleanup(true)
            .cleanupIntervalMs(TimeUnit.MINUTES.toMillis(10))
            .build();
    }
}

class CacheStatistics {
    private long hits;
    private long misses;
    private int currentSize;
    
    public CacheStatistics() {}
    
    public CacheStatistics(CacheStatistics other) {
        this.hits = other.hits;
        this.misses = other.misses;
        this.currentSize = other.currentSize;
    }
    
    public void incrementHits() { hits++; }
    public void incrementMisses() { misses++; }
    
    public double getHitRate() {
        long total = hits + misses;
        return total == 0 ? 0.0 : (double) hits / total;
    }
    
    // Getters and Builder
}
```

## Advanced Usage Patterns

### Pattern 1: State Machine with AtomicReference

```java
public class OrderStateMachine {
    private final AtomicReference<OrderState> currentState = 
        new AtomicReference<>(OrderState.CREATED);
    
    private final AtomicReference<Map<OrderState, Set<OrderState>>> transitions = 
        new AtomicReference<>(buildTransitionMatrix());
    
    public CompletableFuture<Boolean> transitionTo(OrderState targetState) {
        return CompletableFuture.supplyAsync(() -> {
            OrderState current = currentState.get();
            Map<OrderState, Set<OrderState>> transitionMatrix = transitions.get();
            
            // Validate transition
            if (!transitionMatrix.get(current).contains(targetState)) {

```
<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>


# AtomicReference with ExecutorService - Part 2: Advanced Contract Processing & API Services

## Scenario 4: Advanced Contract Executor Service

### Comprehensive Contract Processing Engine

```java
@Service
public class ContractExecutorService {
    
    // Core atomic references for contract processing
    private final AtomicReference<ContractProcessingQueue> processingQueue = 
        new AtomicReference<>(new ContractProcessingQueue());
    
    private final AtomicReference<Map<String, ContractProcessor>> processors = 
        new AtomicReference<>(new ConcurrentHashMap<>());
    
    private final AtomicReference<ContractExecutionMetrics> executionMetrics = 
        new AtomicReference<>(new ContractExecutionMetrics());
    
    private final AtomicReference<ContractProcessingConfig> processingConfig = 
        new AtomicReference<>(ContractProcessingConfig.defaultConfig());
    
    // Atomic reference for workflow orchestration
    private final AtomicReference<WorkflowOrchestrator> workflowOrchestrator = 
        new AtomicReference<>(new WorkflowOrchestrator());
    
    // Atomic reference for resource allocation
    private final AtomicReference<ResourceAllocation> resourceAllocation = 
        new AtomicReference<>(new ResourceAllocation());
    
    // Multiple specialized executor services
    private final ExecutorService contractProcessingExecutor = 
        Executors.newFixedThreadPool(15, 
            new ThreadFactoryBuilder()
                .setNameFormat("contract-processor-%d")
                .setPriority(Thread.NORM_PRIORITY + 1)
                .build());
    
    private final ExecutorService workflowExecutor = 
        Executors.newFixedThreadPool(8,
            new ThreadFactoryBuilder()
                .setNameFormat("workflow-executor-%d")
                .build());
    
    private final ScheduledExecutorService scheduledExecutor = 
        Executors.newScheduledThreadPool(4,
            new ThreadFactoryBuilder()
                .setNameFormat("contract-scheduler-%d")
                .setDaemon(true)
                .build());
    
    @PostConstruct
    public void initialize() {
        // Start periodic metrics collection
        scheduledExecutor.scheduleAtFixedRate(
            this::collectAndUpdateMetrics, 0, 30, TimeUnit.SECONDS);
        
        // Start resource monitoring
        scheduledExecutor.scheduleAtFixedRate(
            this::monitorResourceUtilization, 0, 10, TimeUnit.SECONDS);
    }
    
    /**
     * Execute contract processing with full workflow orchestration
     */
    public CompletableFuture<ContractExecutionResult> executeContractProcessing(
            ContractExecutionRequest request) {
        
        return CompletableFuture.supplyAsync(() -> {
            String executionId = generateExecutionId();
            
            try {
                // Validate and prepare execution context
                ContractExecutionContext context = prepareExecutionContext(request, executionId);
                
                // Register execution in metrics
                registerExecution(executionId, context);
                
                // Execute multi-phase processing
                ContractExecutionResult result = executeProcessingPhases(context);
                
                // Update completion metrics
                updateCompletionMetrics(executionId, result);
                
                return result;
                
            } catch (Exception ex) {
                updateFailureMetrics(executionId, ex);
                throw new ContractExecutionException(
                    "Contract processing failed for execution: " + executionId, ex);
            }
            
        }, contractProcessingExecutor);
    }
    
    /**
     * Execute processing phases with dynamic resource allocation
     */
    private ContractExecutionResult executeProcessingPhases(ContractExecutionContext context) {
        String executionId = context.getExecutionId();
        List<ProcessingPhase> phases = context.getProcessingPhases();
        
        ContractExecutionResult.Builder resultBuilder = ContractExecutionResult.builder()
            .executionId(executionId)
            .startTime(Instant.now());
        
        // Phase 1: Validation and Preparation
        CompletableFuture<ValidationResult> validationFuture = 
            executeValidationPhase(context);
        
        // Phase 2: Content Processing (depends on validation)
        CompletableFuture<ProcessingResult> processingFuture = 
            validationFuture.thenCompose(validationResult -> {
                if (!validationResult.isValid()) {
                    throw new ValidationException("Contract validation failed: " + 
                        validationResult.getErrorMessages());
                }
                return executeContentProcessing(context);
            });
        
        // Phase 3: Workflow Execution (parallel with content processing)
        CompletableFuture<WorkflowResult> workflowFuture = 
            executeWorkflowPhase(context);
        
        // Phase 4: Integration and Finalization
        CompletableFuture<IntegrationResult> integrationFuture = 
            CompletableFuture.allOf(processingFuture, workflowFuture)
                .thenCompose(v -> executeIntegrationPhase(
                    context, 
                    processingFuture.join(), 
                    workflowFuture.join()));
        
        try {
            // Wait for all phases to complete
            IntegrationResult integrationResult = integrationFuture.get(
                context.getTimeoutMs(), TimeUnit.MILLISECONDS);
            
            return resultBuilder
                .validationResult(validationFuture.join())
                .processingResult(processingFuture.join())
                .workflowResult(workflowFuture.join())
                .integrationResult(integrationResult)
                .endTime(Instant.now())
                .status(ExecutionStatus.COMPLETED)
                .build();
                
        } catch (TimeoutException ex) {
            throw new ContractExecutionException("Execution timeout for: " + executionId, ex);
        } catch (Exception ex) {
            throw new ContractExecutionException("Execution failed for: " + executionId, ex);
        }
    }
    
    /**
     * Execute validation phase with parallel validators
     */
    private CompletableFuture<ValidationResult> executeValidationPhase(
            ContractExecutionContext context) {
        
        return CompletableFuture.supplyAsync(() -> {
            List<ContractValidator> validators = getActiveValidators();
            String contractId = context.getContractId();
            
            // Execute validators in parallel
            List<CompletableFuture<ValidationResult>> validationTasks = validators.stream()
                .map(validator -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return validator.validate(context);
                    } catch (Exception ex) {
                        return ValidationResult.failure(
                            validator.getName(), ex.getMessage());
                    }
                }, contractProcessingExecutor))
                .collect(Collectors.toList());
            
            // Aggregate validation results
            CompletableFuture<Void> allValidations = CompletableFuture.allOf(
                validationTasks.toArray(new CompletableFuture[0]));
            
            return allValidations.thenApply(v -> {
                List<ValidationResult> results = validationTasks.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
                
                return aggregateValidationResults(results);
            }).join();
            
        }, contractProcessingExecutor);
    }
    
    /**
     * Execute content processing with dynamic processor selection
     */
    private CompletableFuture<ProcessingResult> executeContentProcessing(
            ContractExecutionContext context) {
        
        return CompletableFuture.supplyAsync(() -> {
            String contractType = context.getContractType();
            
            // Get processor atomically with fallback
            ContractProcessor processor = getProcessorForType(contractType);
            
            // Allocate resources for processing
            ResourceAllocation allocation = allocateProcessingResources(context);
            
            try {
                // Update resource allocation atomically
                updateResourceAllocation(allocation);
                
                // Execute processing with resource constraints
                ProcessingOptions options = ProcessingOptions.builder()
                    .maxMemoryMb(allocation.getMaxMemoryMb())
                    .maxProcessingTimeMs(allocation.getMaxProcessingTimeMs())
                    .parallelismLevel(allocation.getParallelismLevel())
                    .build();
                
                ProcessingResult result = processor.process(context, options);
                
                // Update processing metrics atomically
                updateProcessingMetrics(context.getExecutionId(), result);
                
                return result;
                
            } finally {
                // Release allocated resources
                releaseProcessingResources(allocation);
            }
            
        }, contractProcessingExecutor);
    }
    
    /**
     * Execute workflow phase with orchestration
     */
    private CompletableFuture<WorkflowResult> executeWorkflowPhase(
            ContractExecutionContext context) {
        
        return CompletableFuture.supplyAsync(() -> {
            WorkflowOrchestrator orchestrator = workflowOrchestrator.get();
            String workflowId = context.getWorkflowId();
            
            // Create workflow execution context
            WorkflowExecutionContext workflowContext = WorkflowExecutionContext.builder()
                .workflowId(workflowId)
                .contractContext(context)
                .executorService(workflowExecutor)
                .build();
            
            try {
                // Execute workflow with orchestration
                WorkflowResult result = orchestrator.executeWorkflow(workflowContext);
                
                // Update workflow metrics atomically
                updateWorkflowMetrics(context.getExecutionId(), result);
                
                return result;
                
            } catch (WorkflowException ex) {
                throw new ContractExecutionException(
                    "Workflow execution failed for: " + workflowId, ex);
            }
            
        }, workflowExecutor);
    }
    
    /**
     * Dynamic processor selection with atomic updates
     */
    private ContractProcessor getProcessorForType(String contractType) {
        Map<String, ContractProcessor> currentProcessors = processors.get();
        ContractProcessor processor = currentProcessors.get(contractType);
        
        if (processor == null) {
            // Try to create and register new processor
            processor = createProcessorForType(contractType);
            
            if (processor != null) {
                // Atomic processor registration
                registerProcessor(contractType, processor);
            } else {
                // Fallback to default processor
                processor = currentProcessors.get("default");
                if (processor == null) {
                    throw new IllegalStateException(
                        "No processor available for contract type: " + contractType);
                }
            }
        }
        
        return processor;
    }
    
    /**
     * Register processor atomically
     */
    private void registerProcessor(String contractType, ContractProcessor processor) {
        boolean registered = false;
        int retryCount = 0;
        final int maxRetries = 3;
        
        while (!registered && retryCount < maxRetries) {
            Map<String, ContractProcessor> currentProcessors = processors.get();
            Map<String, ContractProcessor> newProcessors = 
                new ConcurrentHashMap<>(currentProcessors);
            
            newProcessors.put(contractType, processor);
            
            registered = processors.compareAndSet(currentProcessors, newProcessors);
            
            if (!registered) {
                retryCount++;
            }
        }
        
        if (!registered) {
            logError("Failed to register processor for type: " + contractType);
        } else {
            logInfo("Registered processor for contract type: " + contractType);
        }
    }
    
    /**
     * Resource allocation with atomic updates
     */
    private ResourceAllocation allocateProcessingResources(ContractExecutionContext context) {
        ResourceAllocation currentAllocation = resourceAllocation.get();
        
        // Calculate required resources based on context
        ResourceRequirement requirement = calculateResourceRequirement(context);
        
        // Try to allocate resources atomically
        ResourceAllocation newAllocation = null;
        boolean allocated = false;
        int retryCount = 0;
        final int maxRetries = 5;
        
        while (!allocated && retryCount < maxRetries) {
            if (currentAllocation.canAllocate(requirement)) {
                newAllocation = currentAllocation.allocate(requirement);
                allocated = resourceAllocation.compareAndSet(currentAllocation, newAllocation);
                
                if (!allocated) {
                    currentAllocation = resourceAllocation.get();
                    retryCount++;
                }
            } else {
                // Wait for resources to become available
                try {
                    Thread.sleep(100 * (retryCount + 1));
                    currentAllocation = resourceAllocation.get();
                    retryCount++;
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new ResourceAllocationException(
                        "Resource allocation interrupted", ex);
                }
            }
        }
        
        if (!allocated) {
            throw new ResourceAllocationException(
                "Failed to allocate resources after " + maxRetries + " retries");
        }
        
        return newAllocation;
    }
    
    /**
     * Update processing metrics atomically
     */
    private void updateProcessingMetrics(String executionId, ProcessingResult result) {
        executionMetrics.updateAndGet(currentMetrics -> {
            ContractExecutionMetrics newMetrics = new ContractExecutionMetrics(currentMetrics);
            
            newMetrics.recordProcessingCompletion(
                executionId, 
                result.getProcessingTimeMs(),
                result.isSuccessful());
            
            if (result.isSuccessful()) {
                newMetrics.incrementSuccessfulProcessing();
            } else {
                newMetrics.incrementFailedProcessing();
            }
            
            return newMetrics;
        });
    }
    
    /**
     * Batch contract execution with parallel processing
     */
    public CompletableFuture<BatchExecutionResult> executeBatchProcessing(
            List<ContractExecutionRequest> requests) {
        
        return CompletableFuture.supplyAsync(() -> {
            String batchId = generateBatchId();
            
            // Atomic batch statistics tracking
            AtomicReference<BatchExecutionStats> batchStats = 
                new AtomicReference<>(new BatchExecutionStats(batchId));
            
            // Create execution tasks
            List<CompletableFuture<ContractExecutionResult>> executionTasks = 
                requests.stream()
                    .map(request -> executeContractProcessing(request)
                        .whenComplete((result, throwable) -> {
                            // Update batch statistics atomically
                            batchStats.updateAndGet(currentStats -> {
                                BatchExecutionStats newStats = 
                                    new BatchExecutionStats(currentStats);
                                
                                if (throwable == null && result.isSuccessful()) {
                                    newStats.incrementSuccessCount();
                                } else {
                                    newStats.incrementFailureCount();
                                }
                                
                                newStats.updateProgress();
                                return newStats;
                            });
                        }))
                    .collect(Collectors.toList());
            
            // Wait for all executions to complete
            CompletableFuture<Void> allExecutions = CompletableFuture.allOf(
                executionTasks.toArray(new CompletableFuture[0]));
            
            return allExecutions.thenApply(v -> {
                List<ContractExecutionResult> results = executionTasks.stream()
                    .map(task -> {
                        try {
                            return task.join();
                        } catch (Exception ex) {
                            return ContractExecutionResult.failure(ex);
                        }
                    })
                    .collect(Collectors.toList());
                
                return new BatchExecutionResult(batchId, results, batchStats.get());
            }).join();
            
        }, contractProcessingExecutor);
    }
    
    /**
     * Get current execution metrics
     */
    public ExecutionMetricsSnapshot getExecutionMetrics() {
        ContractExecutionMetrics metrics = executionMetrics.get();
        ResourceAllocation allocation = resourceAllocation.get();
        ContractProcessingQueue queue = processingQueue.get();
        
        return ExecutionMetricsSnapshot.builder()
            .totalExecutions(metrics.getTotalExecutions())
            .successfulExecutions(metrics.getSuccessfulExecutions())
            .failedExecutions(metrics.getFailedExecutions())
            .averageProcessingTime(metrics.getAverageProcessingTime())
            .currentResourceUtilization(allocation.getUtilizationPercentage())
            .queueSize(queue.size())
            .activeProcessors(processors.get().size())
            .timestamp(Instant.now())
            .build();
    }
}

// Supporting classes for Contract Executor Service
class ContractExecutionContext {
    private final String executionId;
    private final String contractId;
    private final String contractType;
    private final String workflowId;
    private final long timeoutMs;
    private final List<ProcessingPhase> processingPhases;
    private final Map<String, Object> parameters;
    
    // Constructor, getters, builder pattern
}

class ContractExecutionMetrics {
    private long totalExecutions;
    private long successfulExecutions;
    private long failedExecutions;
    private long totalProcessingTime;
    private final Map<String, ExecutionStats> executionStats;
    
    public ContractExecutionMetrics() {
        this.executionStats = new ConcurrentHashMap<>();
    }
    
    public ContractExecutionMetrics(ContractExecutionMetrics other) {
        this.totalExecutions = other.totalExecutions;
        this.successfulExecutions = other.successfulExecutions;
        this.failedExecutions = other.failedExecutions;
        this.totalProcessingTime = other.totalProcessingTime;
        this.executionStats = new ConcurrentHashMap<>(other.executionStats);
    }
    
    public void recordProcessingCompletion(String executionId, long processingTime, boolean successful) {
        this.totalExecutions++;
        this.totalProcessingTime += processingTime;
        
        ExecutionStats stats = new ExecutionStats(executionId, processingTime, successful);
        this.executionStats.put(executionId, stats);
    }
    
    public void incrementSuccessfulProcessing() { successfulExecutions++; }
    public void incrementFailedProcessing() { failedExecutions++; }
    
    public double getAverageProcessingTime() {
        return totalExecutions == 0 ? 0.0 : (double) totalProcessingTime / totalExecutions;
    }
    
    // Getters and additional methods
}

class ResourceAllocation {
    private int maxMemoryMb;
    private int allocatedMemoryMb;
    private int maxConcurrentProcesses;
    private int activeConcurrentProcesses;
    private final Map<String, Integer> processMemoryAllocation;
    
    public ResourceAllocation() {
        this.maxMemoryMb = 8192; // 8GB default
        this.maxConcurrentProcesses = 20;
        this.processMemoryAllocation = new ConcurrentHashMap<>();
    }
    
    public ResourceAllocation(ResourceAllocation other) {
        this.maxMemoryMb = other.maxMemoryMb;
        this.allocatedMemoryMb = other.allocatedMemoryMb;
        this.maxConcurrentProcesses = other.maxConcurrentProcesses;
        this.activeConcurrentProcesses = other.activeConcurrentProcesses;
        this.processMemoryAllocation = new ConcurrentHashMap<>(other.processMemoryAllocation);
    }
    
    public boolean canAllocate(ResourceRequirement requirement) {
        return (allocatedMemoryMb + requirement.getMemoryMb() <= maxMemoryMb) &&
               (activeConcurrentProcesses < maxConcurrentProcesses);
    }
    
    public ResourceAllocation allocate(ResourceRequirement requirement) {
        ResourceAllocation newAllocation = new ResourceAllocation(this);
        newAllocation.allocatedMemoryMb += requirement.getMemoryMb();
        newAllocation.activeConcurrentProcesses++;
        newAllocation.processMemoryAllocation.put(
            requirement.getProcessId(), requirement.getMemoryMb());
        return newAllocation;
    }
    
    public double getUtilizationPercentage() {
        return (double) allocatedMemoryMb / maxMemoryMb * 100.0;
    }
    
    // Additional methods
}
```

## Scenario 5: Multi-Service API Client Framework

### Comprehensive API Service Management

```java
@Component
public class MultiServiceApiClientFramework {
    
    // Atomic references for service management
    private final AtomicReference<Map<String, ApiServiceClient>> serviceClients = 
        new AtomicReference<>(new ConcurrentHashMap<>());
    
    private final AtomicReference<ServiceRegistry> serviceRegistry = 
        new AtomicReference<>(new ServiceRegistry());
    
    private final AtomicReference<LoadBalancer> loadBalancer = 
        new AtomicReference<>(new RoundRobinLoadBalancer());
    
    private final AtomicReference<ApiGatewayMetrics> gatewayMetrics = 
        new AtomicReference<>(new ApiGatewayMetrics());
    
    // Atomic reference for global circuit breaker
    private final AtomicReference<GlobalCircuitBreaker> globalCircuitBreaker = 
        new AtomicReference<>(new GlobalCircuitBreaker());
    
    // Atomic reference for rate limiting
    private final AtomicReference<RateLimiter> rateLimiter = 
        new AtomicReference<>(new TokenBucketRateLimiter());
    
    // Multiple executor services for different API operations
    private final ExecutorService apiCallExecutor = 
        Executors.newFixedThreadPool(25,
            new ThreadFactoryBuilder()
                .setNameFormat("api-call-%d")
                .build());
    
    private final ExecutorService healthCheckExecutor = 
        Executors.newFixedThreadPool(5,
            new ThreadFactoryBuilder()
                .setNameFormat("health-check-%d")
                .setDaemon(true)
                .build());
    
    private final ScheduledExecutorService metricsExecutor = 
        Executors.newScheduledThreadPool(3,
            new ThreadFactoryBuilder()
                .setNameFormat("metrics-collector-%d")
                .setDaemon(true)
                .build());
    
    @PostConstruct
    public void initialize() {
        // Start periodic health checks
        metricsExecutor.scheduleAtFixedRate(
            this::performHealthChecks, 0, 30, TimeUnit.SECONDS);
        
        // Start metrics collection
        metricsExecutor.scheduleAtFixedRate(
            this::collectApiMetrics, 0, 10, TimeUnit.SECONDS);
        
        // Start circuit breaker monitoring
        metricsExecutor.scheduleAtFixedRate(
            this::monitorCircuitBreakers, 0, 5, TimeUnit.SECONDS);
    }
    
    /**
     * Execute API call with full service orchestration
     */
    public <T> CompletableFuture<ApiResponse<T>> executeApiCall(
            ApiCallRequest request, Class<T> responseType) {
        
        return CompletableFuture.supplyAsync(() -> {
            String requestId = generateRequestId();
            
            try {
                // Check rate limiting
                checkRateLimit(request);
                
                // Check global circuit breaker
                checkGlobalCircuitBreaker(request.getServiceName());
                
                // Select service client with load balancing
                ApiServiceClient client = selectServiceClient(request.getServiceName());
                
                // Record request start
                recordRequestStart(requestId, request);
                
                // Execute the API call
                ApiResponse<T> response = executeWithRetryAndFallback(
                    client, request, responseType, requestId);
                
                // Record successful response
                recordRequestSuccess(requestId, response);
                
                return response;
                
            } catch (Exception ex) {
                recordRequestFailure(requestId, ex);
                throw new ApiCallException("API call failed for request: " + requestId, ex);
            }
            
        }, apiCallExecutor);
    }
    
    /**
     * Execute API call with retry and fallback mechanisms
     */
    private <T> ApiResponse<T> executeWithRetryAndFallback(
            ApiServiceClient client, ApiCallRequest request, 
            Class<T> responseType, String requestId) {
        
        int maxRetries = client.getConfiguration().getMaxRetries();
        Exception lastException = null;
        
        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                // Check service-specific circuit breaker
                if (client.getCircuitBreaker().isOpen()) {
                    throw new CircuitBreakerOpenException(
                        "Service circuit breaker is open: " + client.getServiceName());
                }
                
                // Execute the actual API call
                ApiResponse<T> response = client.execute(request, responseType);
                
                // Update service metrics on success
                updateServiceMetrics(client.getServiceName(), true, response.getResponseTime());
                
                // Reset circuit breaker on success
                client.getCircuitBreaker().recordSuccess();
                
                return response;
                
            } catch (Exception ex) {
                lastException = ex;
                
                // Update service metrics on failure
                updateServiceMetrics(client.getServiceName(), false, 0);
                
                // Update circuit breaker on failure
                client.getCircuitBreaker().recordFailure();
                
                if (attempt < maxRetries && isRetryableException(ex)) {
                    // Wait with exponential backoff
                    waitForRetry(attempt, client.getConfiguration().getRetryDelayMs());
                } else if (attempt == maxRetries) {
                    // Try fallback service if available
                    return tryFallbackService(request, responseType, ex);
                }
            }
        }
        
        throw new ApiCallException("All retries exhausted", lastException);
    }
    
    /**
     * Select service client with load balancing
     */
    private ApiServiceClient selectServiceClient(String serviceName) {
        Map<String, ApiServiceClient> clients = serviceClients.get();
        ServiceRegistry registry = serviceRegistry.get();
        LoadBalancer balancer = loadBalancer.get();
        
        // Get available service instances
        List<ServiceInstance> instances = registry.getHealthyInstances(serviceName);
        
        if (instances.isEmpty()) {
            throw new ServiceUnavailableException("No healthy instances for service: " + serviceName);
        }
        
        // Use load balancer to select instance
        ServiceInstance selectedInstance = balancer.selectInstance(instances);
        
        // Get or create client for the selected instance
        String clientKey = serviceName + ":" + selectedInstance.getId();
        ApiServiceClient client = clients.get(clientKey);
        
        if (client == null) {
            client = createServiceClient(selectedInstance);
            registerServiceClient(clientKey, client);
        }
        
        return client;
    }
    
    /**
     * Register service client atomically
     */
    private void registerServiceClient(String clientKey, ApiServiceClient client) {
        boolean registered = false;
        int retryCount = 0;
        final int maxRetries = 3;
        
        while (!registered && retryCount < maxRetries) {
            Map<String, ApiServiceClient> currentClients = serviceClients.get();
            Map<String, ApiServiceClient> newClients = new ConcurrentHashMap<>(currentClients);
            
            newClients.put(clientKey, client);
            
            registered = serviceClients.compareAndSet(currentClients, newClients);
            
            if (!registered) {
                retryCount++;
            }
        }
        
        if (registered) {
            logInfo("Registered service client: " + clientKey);
        } else {
            logError("Failed to register service client: " + clientKey);
        }
    }
    
    /**
     * Update service metrics atomically
     */
    private void updateServiceMetrics(String serviceName, boolean success, long responseTime) {
        gatewayMetrics.updateAndGet(currentMetrics -> {
            ApiGatewayMetrics newMetrics = new ApiGatewayMetrics(currentMetrics);
            
            newMetrics.recordServiceCall(serviceName, success, responseTime);
            
            if (success) {
                newMetrics.incrementSuccessfulCalls(serviceName);
            } else {
                newMetrics.incrementFailedCalls(serviceName);
            }
            
            return newMetrics;
        });
    }
    
    /**
     * Batch API calls with parallel execution
     */
    public CompletableFuture<BatchApiResponse> executeBatchApiCalls(
            List<ApiCallRequest> requests) {
        
        return CompletableFuture.supplyAsync(() -> {
            String batchId = generateBatchId();
            
            // Atomic batch metrics tracking
            AtomicReference<BatchApiMetrics> batchMetrics = 
                new AtomicReference<>(new BatchApiMetrics(batchId));
            
            // Group requests by service for optimization
            Map<String, List<ApiCallRequest>> requestsByService = requests.stream()
                .collect(Collectors.groupingBy(ApiCallRequest::getServiceName));
            
            // Execute requests in parallel by service
            List<CompletableFuture<List<ApiResponse<Object>>>> serviceExecutionTasks = 
                requestsByService.entrySet().stream()
                    .map(entry -> executeServiceBatch(entry.getKey(), entry.getValue(), batchMetrics))
                    .collect(Collectors.toList());
            
            // Wait for all service batches to complete
            CompletableFuture<Void> allServiceBatches = CompletableFuture.allOf(
                serviceExecutionTasks.toArray(new CompletableFuture[0]));
            
            return allServiceBatches.thenApply(v -> {
                List<ApiResponse<Object>> allResponses = serviceExecutionTasks.stream()
                    .flatMap(task -> task.join().stream())
                    .collect(Collectors.toList());
                
                return new BatchApiResponse(batchId, allResponses, batchMetrics.get());
            }).join();
            
        }, apiCallExecutor);
    }
    
    /**
     * Execute batch of requests for a specific service
     */
    private CompletableFuture<List<ApiResponse<Object>>> executeServiceBatch(
            String serviceName, List<ApiCallRequest> requests, 
            AtomicReference<BatchApiMetrics> batchMetrics) {
        
        return CompletableFuture.supplyAsync(() -> {
            List<CompletableFuture<ApiResponse<Object>>> requestTasks = requests.stream()
                .map(request -> executeApiCall(request, Object.class)
                    .whenComplete((response, throwable) -> {
                        // Update batch metrics atomically
                        batchMetrics.updateAndGet(currentMetrics -> {
                            BatchApiMetrics newMetrics = new BatchApiMetrics(currentMetrics);
                            
                            if (throwable == null) {
                                newMetrics.incrementSuccessCount();
                            } else {
                                newMetrics.incrementFailureCount();
                            }
                            
                            newMetrics.updateProgress();
                            return newMetrics;
                        });
                    }))
                .collect(Collectors.toList());
            
            // Wait for all requests in this service batch
            CompletableFuture<Void> allRequests = CompletableFuture.allOf(
                requestTasks.toArray(new CompletableFuture[0]));
            
            return allRequests.thenApply(v -> 
                requestTasks.stream()
                    .map(task -> {
                        try {
                            return task.join();
                        } catch (Exception ex) {
                            return ApiResponse.failure(ex);
                        }
                    })
                    .collect(Collectors.toList())
            ).join();
            
        }, apiCallExecutor);
    }
    
    /**
     * Dynamic service discovery and registration
     */
    public CompletableFuture<Void> registerService(ServiceRegistration registration) {
        return CompletableFuture.runAsync(() -> {
            ServiceRegistry currentRegistry = serviceRegistry.get();
            
            // Create new registry with added service
            ServiceRegistry newRegistry = currentRegistry.addService(registration);
            
            // Atomic registry update
            if (serviceRegistry.compareAndSet(currentRegistry, newRegistry)) {
                logInfo("Registered new service: " + registration.getServiceName());
                
                // Create initial client for the new service
                ServiceInstance instance = registration.getServiceInstance();
                ApiServiceClient client = createServiceClient(instance);
                String clientKey = registration.getServiceName() + ":" + instance.getId();
                
                registerServiceClient(clientKey, client);
                
                // Trigger health check for new service
                CompletableFuture.runAsync(() -> 
                    performHealthCheck(registration.getServiceName()), healthCheckExecutor);
            }
        }, apiCallExecutor);
    }
    
    /**
     * Service health monitoring with automatic failover
     */
    private void performHealthChecks() {
        ServiceRegistry registry = serviceRegistry.get();
        Map<String, List<ServiceInstance>> services = registry.getAllServices();
        
        // Health check each service
        List<CompletableFuture<Void>> healthCheckTasks = services.entrySet().stream()
            .map(entry -> CompletableFuture.runAsync(() -> 
                performHealthCheck(entry.getKey()), healthCheckExecutor))
            .collect(Collectors.toList());
        
        // Wait for all health checks
        CompletableFuture.allOf(healthCheckTasks.toArray(new CompletableFuture[0]))
            .exceptionally(ex -> {
                logError("Health check batch failed", ex);
                return null;
            });
    }
    
    /**
     * Perform health check for a specific service
     */
    private void performHealthCheck(String serviceName) {
        ServiceRegistry registry = serviceRegistry.get();
        List<ServiceInstance> instances = registry.getInstances(serviceName);
        
        for (ServiceInstance instance : instances) {
            try {
                // Execute health check call
                ApiCallRequest healthRequest = ApiCallRequest.builder()
                    .serviceName(serviceName)
                    .endpoint("/health")
                    .method(HttpMethod.GET)
                    .timeout(5000) // 5 second timeout for health checks
                    .build();
                
                ApiServiceClient client = getOrCreateServiceClient(serviceName, instance);
                ApiResponse<HealthStatus> response = client.execute(healthRequest, HealthStatus.class);
                
                // Update instance health status
                updateInstanceHealth(serviceName, instance.getId(), 
                    response.isSuccessful() && response.getData().isHealthy());
                
            } catch (Exception ex) {
                // Mark instance as unhealthy
                updateInstanceHealth(serviceName, instance.getId(), false);
                logWarn("Health check failed for service: " + serviceName + 
                       ", instance: " + instance.getId(), ex);
            }
        }
    }
    
    /**
     * Update instance health status atomically
     */
    private void updateInstanceHealth(String serviceName, String instanceId, boolean healthy) {
        boolean updated = false;
        int retryCount = 0;
        final int maxRetries = 3;
        
        while (!updated && retryCount < maxRetries) {
            ServiceRegistry currentRegistry = serviceRegistry.get();
            ServiceRegistry newRegistry = currentRegistry.updateInstanceHealth(
                serviceName, instanceId, healthy);
            
            updated = serviceRegistry.compareAndSet(currentRegistry, newRegistry);
            
            if (!updated) {
                retryCount++;
            }
        }
        
        if (updated) {
            // If instance became unhealthy, remove its client
            if (!healthy) {
                removeServiceClient(serviceName + ":" + instanceId);
            }
        }
    }
    
    /**
     * Remove service client atomically
     */
    private void removeServiceClient(String clientKey) {
        boolean removed = false;
        int retryCount = 0;
        final int maxRetries = 3;
        
        while (!removed && retryCount < maxRetries) {
            Map<String, ApiServiceClient> currentClients = serviceClients.get();
            
            if (!currentClients.containsKey(clientKey)) {
                return; // Already removed
            }
            
            Map<String, ApiServiceClient> newClients = new ConcurrentHashMap<>(currentClients);
            ApiServiceClient removedClient = newClients.remove(clientKey);
            
            removed = serviceClients.compareAndSet(currentClients, newClients);
            
            if (removed && removedClient != null) {
                // Cleanup removed client
                CompletableFuture.runAsync(() -> {
                    try {
                        removedClient.shutdown();
                    } catch (Exception ex) {
                        logError("Error shutting down service client: " + clientKey, ex);
                    }
                }, apiCallExecutor);
            }
            
            if (!removed) {
                retryCount++;
            }
        }
    }
    
    /**
     * Circuit breaker monitoring and management
     */
    private void monitorCircuitBreakers() {
        Map<String, ApiServiceClient> clients = serviceClients.get();
        GlobalCircuitBreaker globalCB = globalCircuitBreaker.get();
        
        // Check individual service circuit breakers
        Map<String, CircuitBreakerStatus> serviceStatuses = new HashMap<>();
        
        for (Map.Entry<String, ApiServiceClient> entry : clients.entrySet()) {
            String clientKey = entry.getKey();
            ApiServiceClient client = entry.getValue();
            CircuitBreaker cb = client.getCircuitBreaker();
            
            CircuitBreakerStatus status = CircuitBreakerStatus.builder()
                .serviceName(client.getServiceName())
                .state(cb.getState())
                .failureCount(cb.getFailureCount())
                .lastFailureTime(cb.getLastFailureTime())
                .build();
            
            serviceStatuses.put(clientKey, status);
        }
        
        // Update global circuit breaker based on service states
        updateGlobalCircuitBreaker(serviceStatuses);
        
        // Log circuit breaker changes
        logCircuitBreakerStates(serviceStatuses);
    }
    
    /**
     * Update global circuit breaker atomically
     */
    private void updateGlobalCircuitBreaker(Map<String, CircuitBreakerStatus> serviceStatuses) {
        globalCircuitBreaker.updateAndGet(currentGlobalCB -> {
            GlobalCircuitBreaker newGlobalCB = new GlobalCircuitBreaker(currentGlobalCB);
            
            // Calculate global failure rate
            long totalFailures = serviceStatuses.values().stream()
                .mapToLong(CircuitBreakerStatus::getFailureCount)
                .sum();
            
            double globalFailureRate = calculateGlobalFailureRate(serviceStatuses);
            
            // Update global circuit breaker state
            if (globalFailureRate > 0.5) { // 50% failure rate threshold
                newGlobalCB.openCircuit("High global failure rate: " + globalFailureRate);
            } else if (globalFailureRate < 0.1) { // 10% recovery threshold
                newGlobalCB.closeCircuit();
            }
            
            newGlobalCB.updateMetrics(totalFailures, globalFailureRate);
            
            return newGlobalCB;
        });
    }
    
    /**
     * Rate limiting with token bucket algorithm
     */
    private void checkRateLimit(ApiCallRequest request) {
        RateLimiter limiter = rateLimiter.get();
        
        if (!limiter.tryAcquire(request.getServiceName())) {
            throw new RateLimitExceededException(
                "Rate limit exceeded for service: " + request.getServiceName());
        }
    }
    
    /**
     * Collect and aggregate API metrics
     */
    private void collectApiMetrics() {
        ApiGatewayMetrics currentMetrics = gatewayMetrics.get();
        Map<String, ApiServiceClient> clients = serviceClients.get();
        
        // Collect metrics from all service clients
        Map<String, ServiceMetrics> serviceMetrics = new HashMap<>();
        
        for (Map.Entry<String, ApiServiceClient> entry : clients.entrySet()) {
            String clientKey = entry.getKey();
            ApiServiceClient client = entry.getValue();
            
            ServiceMetrics metrics = client.getMetrics();
            serviceMetrics.put(clientKey, metrics);
        }
        
        // Update gateway metrics atomically
        gatewayMetrics.updateAndGet(oldMetrics -> {
            ApiGatewayMetrics newMetrics = new ApiGatewayMetrics(oldMetrics);
            newMetrics.aggregateServiceMetrics(serviceMetrics);
            newMetrics.updateTimestamp();
            return newMetrics;
        });
    }
    
    /**
     * Get comprehensive gateway status
     */
    public GatewayStatus getGatewayStatus() {
        ApiGatewayMetrics metrics = gatewayMetrics.get();
        ServiceRegistry registry = serviceRegistry.get();
        GlobalCircuitBreaker globalCB = globalCircuitBreaker.get();
        Map<String, ApiServiceClient> clients = serviceClients.get();
        
        return GatewayStatus.builder()
            .totalServices(registry.getServiceCount())
            .healthyServices(registry.getHealthyServiceCount())
            .totalClients(clients.size())
            .globalCircuitBreakerState(globalCB.getState())
            .totalRequests(metrics.getTotalRequests())
            .successfulRequests(metrics.getSuccessfulRequests())
            .failedRequests(metrics.getFailedRequests())
            .averageResponseTime(metrics.getAverageResponseTime())
            .requestsPerSecond(metrics.getRequestsPerSecond())
            .timestamp(Instant.now())
            .build();
    }
    
    /**
     * Graceful shutdown of all services
     */
    @PreDestroy
    public void shutdown() {
        try {
            // Stop accepting new requests
            logInfo("Shutting down API Gateway...");
            
            // Shutdown all service clients
            Map<String, ApiServiceClient> clients = serviceClients.get();
            List<CompletableFuture<Void>> shutdownTasks = clients.values().stream()
                .map(client -> CompletableFuture.runAsync(() -> {
                    try {
                        client.shutdown();
                    } catch (Exception ex) {
                        logError("Error shutting down client: " + client.getServiceName(), ex);
                    }
                }, apiCallExecutor))
                .collect(Collectors.toList());
            
            // Wait for all clients to shutdown
            CompletableFuture.allOf(shutdownTasks.toArray(new CompletableFuture[0]))
                .get(30, TimeUnit.SECONDS);
            
            // Shutdown executor services
            shutdownExecutorService(apiCallExecutor, "API Call Executor");
            shutdownExecutorService(healthCheckExecutor, "Health Check Executor");
            shutdownExecutorService(metricsExecutor, "Metrics Executor");
            
            logInfo("API Gateway shutdown completed");
            
        } catch (Exception ex) {
            logError("Error during gateway shutdown", ex);
        }
    }
    
    private void shutdownExecutorService(ExecutorService executor, String name) {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                logWarn(name + " did not terminate gracefully, forcing shutdown");
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            logWarn(name + " shutdown interrupted, forcing shutdown");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

// Supporting classes for Multi-Service API Framework
class ApiServiceClient {
    private final String serviceName;
    private final ServiceInstance serviceInstance;
    private final ApiClientConfiguration configuration;
    private final CircuitBreaker circuitBreaker;
    private final AtomicReference<ServiceMetrics> metrics;
    private final HttpClient httpClient;
    
    public ApiServiceClient(String serviceName, ServiceInstance instance, 
                           ApiClientConfiguration config) {
        this.serviceName = serviceName;
        this.serviceInstance = instance;
        this.configuration = config;
        this.circuitBreaker = new CircuitBreaker(config.getCircuitBreakerConfig());
        this.metrics = new AtomicReference<>(new ServiceMetrics());
        this.httpClient = createHttpClient(config);
    }
    
    public <T> ApiResponse<T> execute(ApiCallRequest request, Class<T> responseType) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Build HTTP request
            HttpRequest httpRequest = buildHttpRequest(request);
            
            // Execute HTTP call
            HttpResponse<String> httpResponse = httpClient.send(
                httpRequest, HttpResponse.BodyHandlers.ofString());
            
            // Process response
            T responseData = processResponse(httpResponse, responseType);
            
            // Update metrics
            long responseTime = System.currentTimeMillis() - startTime;
            updateMetrics(true, responseTime);
            
            return ApiResponse.success(responseData, responseTime, httpResponse.statusCode());
            
        } catch (Exception ex) {
            long responseTime = System.currentTimeMillis() - startTime;
            updateMetrics(false, responseTime);
            throw new ApiCallException("HTTP call failed", ex);
        }
    }
    
    private void updateMetrics(boolean success, long responseTime) {
        metrics.updateAndGet(currentMetrics -> {
            ServiceMetrics newMetrics = new ServiceMetrics(currentMetrics);
            newMetrics.recordCall(success, responseTime);
            return newMetrics;
        });
    }
    
    public ServiceMetrics getMetrics() {
        return metrics.get();
    }
    
    public CircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }
    
    public void shutdown() {
        // Cleanup HTTP client and resources
    }
    
    // Additional methods...
}

class ServiceRegistry {
    private final Map<String, List<ServiceInstance>> services;
    private final Map<String, Map<String, Boolean>> healthStatus;
    
    public ServiceRegistry() {
        this.services = new ConcurrentHashMap<>();
        this.healthStatus = new ConcurrentHashMap<>();
    }
    
    public ServiceRegistry(ServiceRegistry other) {
        this.services = new ConcurrentHashMap<>();
        this.healthStatus = new ConcurrentHashMap<>();
        
        // Deep copy services
        for (Map.Entry<String, List<ServiceInstance>> entry : other.services.entrySet()) {
            this.services.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        
        // Deep copy health status
        for (Map.Entry<String, Map<String, Boolean>> entry : other.healthStatus.entrySet()) {
            this.healthStatus.put(entry.getKey(), new ConcurrentHashMap<>(entry.getValue()));
        }
    }
    
    public ServiceRegistry addService(ServiceRegistration registration) {
        ServiceRegistry newRegistry = new ServiceRegistry(this);
        String serviceName = registration.getServiceName();
        ServiceInstance instance = registration.getServiceInstance();
        
        newRegistry.services.computeIfAbsent(serviceName, k -> new ArrayList<>())
            .add(instance);
        
        newRegistry.healthStatus.computeIfAbsent(serviceName, k -> new ConcurrentHashMap<>())
            .put(instance.getId(), true);
        
        return newRegistry;
    }
    
    public ServiceRegistry updateInstanceHealth(String serviceName, String instanceId, boolean healthy) {
        ServiceRegistry newRegistry = new ServiceRegistry(this);
        
        Map<String, Boolean> serviceHealth = newRegistry.healthStatus.get(serviceName);
        if (serviceHealth != null) {
            serviceHealth.put(instanceId, healthy);
        }
        
        return newRegistry;
    }
    
    public List<ServiceInstance> getHealthyInstances(String serviceName) {
        List<ServiceInstance> instances = services.get(serviceName);
        Map<String, Boolean> health = healthStatus.get(serviceName);
        
        if (instances == null || health == null) {
            return Collections.emptyList();
        }
        
        return instances.stream()
            .filter(instance -> health.getOrDefault(instance.getId(), false))
            .collect(Collectors.toList());
    }
    
    public List<ServiceInstance> getInstances(String serviceName) {
        return services.getOrDefault(serviceName, Collections.emptyList());
    }
    
    public Map<String, List<ServiceInstance>> getAllServices() {
        return new HashMap<>(services);
    }
    
    public int getServiceCount() {
        return services.size();
    }
    
    public int getHealthyServiceCount() {
        return (int) services.keySet().stream()
            .filter(serviceName -> !getHealthyInstances(serviceName).isEmpty())
            .count();
    }
}

class ApiGatewayMetrics {
    private long totalRequests;
    private long successfulRequests;
    private long failedRequests;
    private long totalResponseTime;
    private final Map<String, ServiceCallMetrics> serviceMetrics;
    private long lastUpdated;
    
    public ApiGatewayMetrics() {
        this.serviceMetrics = new ConcurrentHashMap<>();
        this.lastUpdated = System.currentTimeMillis();
    }
    
    public ApiGatewayMetrics(ApiGatewayMetrics other) {
        this.totalRequests = other.totalRequests;
        this.successfulRequests = other.successfulRequests;
        this.failedRequests = other.failedRequests;
        this.totalResponseTime = other.totalResponseTime;
        this.serviceMetrics = new ConcurrentHashMap<>(other.serviceMetrics);
        this.lastUpdated = other.lastUpdated;
    }
    
    public void recordServiceCall(String serviceName, boolean success, long responseTime) {
        this.totalRequests++;
        this.totalResponseTime += responseTime;
        
        if (success) {
            this.successfulRequests++;
        } else {
            this.failedRequests++;
        }
        
        // Update service-specific metrics
        serviceMetrics.computeIfAbsent(serviceName, k -> new ServiceCallMetrics())
            .recordCall(success, responseTime);
    }
    
    public void incrementSuccessfulCalls(String serviceName) {
        serviceMetrics.computeIfAbsent(serviceName, k -> new ServiceCallMetrics())
            .incrementSuccessful();
    }
    
    public void incrementFailedCalls(String serviceName) {
        serviceMetrics.computeIfAbsent(serviceName, k -> new ServiceCallMetrics())
            .incrementFailed();
    }
    
    public void aggregateServiceMetrics(Map<String, ServiceMetrics> newServiceMetrics) {
        for (Map.Entry<String, ServiceMetrics> entry : newServiceMetrics.entrySet()) {
            String serviceName = entry.getKey().split(":")[0]; // Extract service name from client key
            ServiceMetrics metrics = entry.getValue();
            
            ServiceCallMetrics callMetrics = serviceMetrics.computeIfAbsent(
                serviceName, k -> new ServiceCallMetrics());
            callMetrics.aggregate(metrics);
        }
    }
    
    public void updateTimestamp() {
        this.lastUpdated = System.currentTimeMillis();
    }
    
    public double getAverageResponseTime() {
        return totalRequests == 0 ? 0.0 : (double) totalResponseTime / totalRequests;
    }
    
    public double getRequestsPerSecond() {
        long timeWindowMs = System.currentTimeMillis() - lastUpdated;
        return timeWindowMs == 0 ? 0.0 : (double) totalRequests * 1000 / timeWindowMs;
    }
    
    // Getters
    public long getTotalRequests() { return totalRequests; }
    public long getSuccessfulRequests() { return successfulRequests; }
    public long getFailedRequests() { return failedRequests; }
}

class GlobalCircuitBreaker {
    private CircuitState state;
    private long lastFailureTime;
    private long totalFailures;
    private double globalFailureRate;
    private String reason;
    
    public GlobalCircuitBreaker() {
        this.state = CircuitState.CLOSED;
        this.lastFailureTime = 0;
        this.totalFailures = 0;
        this.globalFailureRate = 0.0;
    }
    
    public GlobalCircuitBreaker(GlobalCircuitBreaker other) {
        this.state = other.state;
        this.lastFailureTime = other.lastFailureTime;
        this.totalFailures = other.totalFailures;
        this.globalFailureRate = other.globalFailureRate;
        this.reason = other.reason;
    }
    
    public void openCircuit(String reason) {
        this.state = CircuitState.OPEN;
        this.lastFailureTime = System.currentTimeMillis();
        this.reason = reason;
    }
    
    public void closeCircuit() {
        this.state = CircuitState.CLOSED;
        this.reason = null;
    }
    
    public void updateMetrics(long totalFailures, double globalFailureRate) {
        this.totalFailures = totalFailures;
        this.globalFailureRate = globalFailureRate;
    }
    
    public boolean isOpen() {
        if (state == CircuitState.OPEN) {
            // Check if enough time has passed to try half-open
            long timeSinceLastFailure = System.currentTimeMillis() - lastFailureTime;
            if (timeSinceLastFailure > 120000) { // 2 minutes
                state = CircuitState.HALF_OPEN;
                return false;
            }
            return true;
        }
        return false;
    }
    
    // Getters
    public CircuitState getState() { return state; }
    public double getGlobalFailureRate() { return globalFailureRate; }
    public String getReason() { return reason; }
}

// Additional supporting classes
enum CircuitState {
    CLOSED, OPEN, HALF_OPEN
}

class ServiceInstance {
    private final String id;
    private final String host;
    private final int port;
    private final Map<String, String> metadata;
    
    // Constructor, getters
}

class ServiceRegistration {
    private final String serviceName;
    private final ServiceInstance serviceInstance;
    private final Map<String, Object> registrationMetadata;
    
    // Constructor, getters
}

class LoadBalancer {
    public ServiceInstance selectInstance(List<ServiceInstance> instances) {
        // Implementation varies by load balancing strategy
        return instances.get(0); // Simplified
    }
}

class RoundRobinLoadBalancer extends LoadBalancer {
    private final AtomicReference<Map<String, Integer>> serviceCounters = 
        new AtomicReference<>(new ConcurrentHashMap<>());
    
    @Override
    public ServiceInstance selectInstance(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            throw new IllegalArgumentException("No instances available");
        }
        
        String serviceKey = instances.get(0).getClass().getSimpleName();
        
        // Atomic counter update
        int index = serviceCounters.updateAndGet(currentCounters -> {
            Map<String, Integer> newCounters = new ConcurrentHashMap<>(currentCounters);
            int currentIndex = newCounters.getOrDefault(serviceKey, 0);
            int nextIndex = (currentIndex + 1) % instances.size();
            newCounters.put(serviceKey, nextIndex);
            return newCounters;
        }).get(serviceKey);
        
        return instances.get(index);
    }
}

## Best Practices and Performance Considerations

### 1. Atomic Operation Optimization

```java
// ✅ Good: Minimize work in atomic operations
public void updateCounterEfficiently() {
    counter.updateAndGet(current -> current + 1);
}

// ❌ Avoid: Heavy computation in atomic operations
public void updateCounterInefficiently() {
    counter.updateAndGet(current -> {
        // Heavy computation - should be done outside
        performExpensiveCalculation();
        return current + 1;
    });
}

// ✅ Better: Pre-compute heavy operations
public void updateCounterOptimized() {
    int computedValue = performExpensiveCalculation(); // Outside atomic operation
    counter.updateAndGet(current -> current + computedValue);
}
```

### 2. Retry Strategy for CAS Operations

```java
public boolean updateWithRetry(AtomicReference<ComplexObject> atomicRef, 
                              Function<ComplexObject, ComplexObject> updateFunction) {
    final int maxRetries = 5;
    final int baseDelayMs = 1;
    
    for (int attempt = 0; attempt < maxRetries; attempt++) {
        ComplexObject current = atomicRef.get();
        ComplexObject updated = updateFunction.apply(current);
        
        if (atomicRef.compareAndSet(current, updated)) {
            return true; // Success
        }
        
        // Exponential backoff for retries
        if (attempt < maxRetries - 1) {
            try {
                Thread.sleep(baseDelayMs << attempt); // 1, 2, 4, 8ms delays
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
    }
    
    return false; // Failed after all retries
}
```

### 3. Memory Management and Object Creation

```java
// ✅ Efficient: Reuse objects when possible
private final AtomicReference<Statistics> stats = new AtomicReference<>(new Statistics());

public void updateStatsEfficient(boolean success, long duration) {
    stats.updateAndGet(current -> {
        // Modify existing object instead of creating new one when possible
        if (current.canModifyInPlace()) {
            current.update(success, duration);
            return current;
        } else {
            return new Statistics(current).update(success, duration);
        }
    });
}

// 🔄 Alternative: Use copy constructor for immutable updates
public void updateStatsImmutable(boolean success, long duration) {
    stats.updateAndGet(current -> 
        new Statistics(current).update(success, duration));
}
```

### 4. Monitoring and Observability

```java
@Component
public class AtomicReferenceMonitor {
    private final MeterRegistry meterRegistry;
    private final Map<String, AtomicReference<?>> monitoredReferences = new ConcurrentHashMap<>();
    
    public <T> AtomicReference<T> createMonitoredReference(String name, T initialValue) {
        AtomicReference<T> reference = new AtomicReference<>(initialValue);
        monitoredReferences.put(name, reference);
        
        // Register metrics
        Gauge.builder("atomic.reference.updates")
            .tag("name", name)
            .register(meterRegistry, reference, ref -> getUpdateCount(name));
        
        return reference;
    }
    
    @Scheduled(fixedRate = 30000) // Every 30 seconds
    public void collectMetrics() {
        monitoredReferences.forEach((name, reference) -> {
            // Collect and report metrics
            logMetrics(name, reference);
        });
    }
}
```

## Conclusion

AtomicReference with ExecutorService provides a powerful combination for building highly concurrent, thread-safe applications. The key benefits include:

- **Lock-free concurrency**: No traditional synchronization overhead
- **Atomic state transitions**: Guaranteed consistency during updates
- **Scalable performance**: Better throughput under high contention
- **Composable operations**: Easy to build complex workflows

When implementing these patterns, remember to:
1. Keep atomic operations lightweight
2. Use appropriate retry strategies for CAS failures
3. Monitor performance and contention
4. Design for immutability when possible
5. Handle resource cleanup properly

These patterns are particularly effective in microservices architectures, high-throughput APIs, and real-time processing systems where thread safety and performance are critical.