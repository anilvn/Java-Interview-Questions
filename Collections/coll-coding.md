
**Business Use Case**: Although `TreeMap` provides natural ordering, use `LinkedHashMap` with sorting logic when **you want to rank records** but **preserve the order of ranks**.

---

Would you like similar examples for `NavigableMap`, `EnumMap`, or `WeakHashMap`? Each has strong real-world relevance when used in the right context.


Excellent! Let’s now explore **3 more specialized `Map` implementations**:

* ✅ `NavigableMap` (like `TreeMap` with navigation features)
* ✅ `EnumMap` (for enum-based keys; fast & memory-efficient)
* ✅ `WeakHashMap` (keys garbage-collected when no longer in use)

Each comes with:

* Method
* Sample call
* Sample output
* 1-line business justification

---

## ✅ 5. **Using `NavigableMap`** – Fetching Nearest Price Brackets

```java
public static NavigableMap<Double, String> buildPriceBrackets(Map<Double, String> brackets) {
    return new TreeMap<>(brackets);
}

// Sample call
NavigableMap<Double, String> priceMap = buildPriceBrackets(Map.of(
    100.0, "Basic",
    200.0, "Standard",
    500.0, "Premium"
));

System.out.println("Nearest plan for $250: " + priceMap.floorEntry(250.0));
System.out.println("Higher plan than $250: " + priceMap.higherEntry(250.0));
```

**Output:**

```
Nearest plan for $250: 200.0=Standard
Higher plan than $250: 500.0=Premium
```

**Business Use Case**: Use `NavigableMap` when you need **range-based lookup** (e.g., pricing tiers, score bands, salary brackets).

---

## ✅ 6. **Using `EnumMap`** – Fast Access by Enum Type (e.g., Status or Category)

```java
enum OrderStatus {
    NEW, PROCESSING, SHIPPED, DELIVERED
}

public static EnumMap<OrderStatus, Integer> countOrdersByStatus(List<OrderStatus> statuses) {
    EnumMap<OrderStatus, Integer> map = new EnumMap<>(OrderStatus.class);
    for (OrderStatus status : statuses) {
        map.merge(status, 1, Integer::sum);
    }
    return map;
}

// Sample call
List<OrderStatus> orders = Arrays.asList(
    OrderStatus.NEW, OrderStatus.PROCESSING, OrderStatus.NEW,
    OrderStatus.SHIPPED, OrderStatus.DELIVERED, OrderStatus.NEW
);
System.out.println(countOrdersByStatus(orders));
```

**Output:**

```
{NEW=3, PROCESSING=1, SHIPPED=1, DELIVERED=1}
```

**Business Use Case**: Use `EnumMap` for **fixed-key maps based on enum types**, like order status, roles, or states — it's **more performant** than `HashMap`.

---

## ✅ 7. **Using `WeakHashMap`** – Auto-Cleanup of Temporary Metadata

```java
public static Map<Object, String> createSessionMetadata() {
    Map<Object, String> sessionMap = new WeakHashMap<>();
    Object user1 = new Object();
    Object user2 = new Object();

    sessionMap.put(user1, "Session-1");
    sessionMap.put(user2, "Session-2");

    System.out.println("Before GC: " + sessionMap);

    user1 = null; // remove strong reference
    System.gc();  // suggest garbage collection
    try { Thread.sleep(100); } catch (InterruptedException e) {}

    System.out.println("After GC: " + sessionMap);
    return sessionMap;
}

// Sample call
createSessionMetadata();
```

**Output (may vary):**

```
Before GC: {java.lang.Object@1=Session-1, java.lang.Object@2=Session-2}
After GC: {java.lang.Object@2=Session-2}  // One key GC'd
```

**Business Use Case**: Use `WeakHashMap` for **cache or metadata storage** where keys should be garbage collected when no longer in use — helps avoid memory leaks.

---

### 🔚 Summary

| Map Type            | Business Case                                       |
| ------------------- | --------------------------------------------------- |
| `TreeMap`           | Sorted catalog, leaderboard, alphabetical display   |
| `LinkedHashMap`     | Preserve insertion order (logs, recent activity)    |
| `ConcurrentHashMap` | Thread-safe analytics/logging                       |
| `NavigableMap`      | Bracketed/ranged values like prices, age groups     |
| `EnumMap`           | Fast lookup by enum (status, category, state)       |
| `WeakHashMap`       | Temporary or auto-removable data (caches, sessions) |

---

Would you like to see these **wrapped as service methods**, or examples that integrate with **Spring Boot, REST APIs, or databases**?
