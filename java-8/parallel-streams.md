
## Parallel Streams: Ordered vs Unordered — Key Notes

### 1. **What is a Parallel Stream?**

* A **parallel stream** splits the data source into multiple chunks.
* These chunks are processed **concurrently** on different CPU cores/threads.
* The goal is to improve performance by utilizing multi-core processors.

---

### 2. **Ordered vs Unordered Streams**

* **Ordered stream**: Preserves the **encounter order** of elements as defined by the source.
* **Unordered stream**: Elements can be processed and returned in **any order**; order is not guaranteed.

---

### 3. **Why Does Order Matter?**

* Some operations or business logic **depend on the original order** of data.
* Preserving order ensures **deterministic output**.
* Losing order can lead to **faster processing** but potentially **unpredictable results**.

---

### 4. **Default Behavior of Parallel Streams**

* By default, **parallel streams preserve encounter order** for **ordered data sources** (like Lists).
* However, **terminal operations** influence if the output respects order.

---

### 5. **Common Terminal Operations and Their Order Behavior**

| Terminal Operation        | Order Behavior in Parallel Streams                           | Notes                                            |
| ------------------------- | ------------------------------------------------------------ | ------------------------------------------------ |
| `forEach()`               | **Unordered**: elements may appear in any order              | Prioritizes performance; order is not guaranteed |
| `forEachOrdered()`        | **Ordered**: preserves encounter order                       | Slightly slower due to ordering constraints      |
| `collect()` (like toList) | Preserves order if source is ordered                         | Collectors maintain order                        |
| `reduce()`                | May or may not preserve order depending on accumulator logic | Need associative and stateless accumulator       |

---

### 6. **When to Use Ordered Parallel Streams**

* When the output **must match the input order** (e.g., timelines, sorted data, sequential logs).
* When you use operations that **depend on ordering** (like `limit()`, `skip()`).

---

### 7. **When to Use Unordered Parallel Streams**

* When the operation is **independent of order** (e.g., counting, summing, searching).
* When you want to **maximize performance** and ordering is irrelevant.
* When using unordered data sources like **HashSet** where order is inherently undefined.

---

### 8. **Performance Trade-offs**

| Aspect                | Ordered Parallel Stream                  | Unordered Parallel Stream          |
| --------------------- | ---------------------------------------- | ---------------------------------- |
| Speed                 | Slower due to ordering overhead          | Faster, less coordination overhead |
| Memory                | May use more memory for ordering buffers | Generally uses less memory         |
| Output Predictability | Predictable order                        | Non-deterministic order            |

---

### 9. **Summary**

* **Parallel streams** enable concurrent processing for better CPU utilization.
* **Order preservation** depends on:

  * Data source (ordered vs unordered)
  * Terminal operation (`forEach` vs `forEachOrdered`)
* Use **unordered** streams for speed when order doesn’t matter.
* Use **ordered** streams when processing depends on input sequence.

```java
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<String> plates = List.of("AP09", "TS07", "MH14", "KA05");

        plates.parallelStream()
              .forEach(plate -> System.out.println("Logging (unordered): " + plate));
        
        plates.parallelStream()
              .forEachOrdered(plate -> System.out.println("Logging (ordered): " + plate));

    }
}
```

```java
import java.util.*;
import java.util.stream.*;

class Transaction {
    private String customerName;
    private double amountUSD;

    public Transaction(String customerName, double amountUSD) {
        this.customerName = customerName;
        this.amountUSD = amountUSD;
    }

    public String getCustomerName() { return customerName; }
    public double getAmountUSD() { return amountUSD; }
}

public class Main {
    public static void main(String[] args) {
        List<Transaction> transactions = List.of(
            new Transaction("Alice", 50),
            new Transaction("Bob", 100),
            new Transaction("Charlie", 200),
            new Transaction("David", 80),
            new Transaction("Eve", 120)
        );

        double exchangeRate = 83.0; // USD to INR

        System.out.println("\n🔁 Unordered parallel processing (fast but unordered):");
        transactions.parallelStream()
            .map(tx -> {
                double inr = tx.getAmountUSD() * exchangeRate;
                return tx.getCustomerName() + ": ₹" + inr;
            })
            .filter(s -> s.contains("₹") && extractAmount(s) > 5000)
            .forEach(s -> System.out.println(Thread.currentThread().getName() + " → " + s));

        System.out.println("\n🧾 Ordered parallel processing (slower but ordered):");
        transactions.parallelStream()
            .map(tx -> {
                double inr = tx.getAmountUSD() * exchangeRate;
                return tx.getCustomerName() + ": ₹" + inr;
            })
            .filter(s -> s.contains("₹") && extractAmount(s) > 5000)
            .forEachOrdered(s -> System.out.println(s));
    }

    private static double extractAmount(String s) {
        return Double.parseDouble(s.split("₹")[1]);
    }
}
```

#### output
```
🔁 Unordered parallel processing (fast but unordered):
ForkJoinPool.commonPool-worker-5 → Bob: ₹8300.0
ForkJoinPool.commonPool-worker-3 → Charlie: ₹16600.0
ForkJoinPool.commonPool-worker-7 → Eve: ₹9960.0

🧾 Ordered parallel processing (slower but ordered):
Bob: ₹8300.0
Charlie: ₹16600.0
Eve: ₹9960.0
```
<br/><br/>

## Parallel Streams: Common Pitfalls & Effects of Intermediate Operations on Ordering

### 1. **Common Pitfalls When Using Parallel Streams and Ordering**

### a) **Assuming Parallel Stream Always Preserves Order**

* By default, parallel streams on ordered sources preserve order **only if the terminal operation supports it**.
* Using `forEach()` in parallel **does not guarantee order**, even if the source is ordered.
* Always use `forEachOrdered()` when order matters in parallel processing.

### b) **Ignoring Side Effects**

* Parallel streams run operations concurrently, so **side effects** (e.g., modifying shared variables or collections) can cause data corruption or inconsistent results.
* Always avoid shared mutable state or use thread-safe structures.

### c) **Performance Degradation Due to Forced Ordering**

* Forcing order with `forEachOrdered()` or using ordered collectors can **significantly slow down** parallel streams.
* Sometimes a **sequential stream** is faster than a parallel stream forced to maintain order.

### d) **Using Stateful Intermediate Operations**

* Stateful operations like `distinct()`, `sorted()`, or `limit()` can introduce **synchronization overhead** in parallel streams.
* This may reduce or negate the performance benefits of parallelism.

### e) **Using Unordered Data Sources**

* Some collections (e.g., `HashSet`) are unordered.
* If order matters, converting to an ordered collection (e.g., `List`) before streaming is important.

---

### 2. **How Intermediate Operations Affect Ordering**

#### a) `sorted()`

* Enforces a **sort order** on the stream elements.
* Causes the stream to become **ordered**, regardless of the source's original order.
* Parallel streams with `sorted()` perform a parallel sort (like parallel merge sort), which involves coordination overhead.
* Can significantly affect performance if dataset is large.

### b) `unordered()`

* Declares that the stream's **encounter order does not matter**.
* Allows the framework to **optimize processing** without maintaining order.
* Can improve performance, especially on parallel streams, by relaxing ordering constraints.
* Useful when you know your data or processing logic does not require ordering (e.g., counting, summing).

### c) `distinct()`

* Removes duplicates and implicitly requires ordering or hashing.
* May cause the stream to become **stateful**.
* Can reduce performance in parallel streams due to synchronization or combining steps.

### d) `limit()` and `skip()`

* Dependent on order; to work correctly, stream must be ordered.
* In parallel streams, these cause additional overhead to maintain correctness.

---

### 3. **Summary Table**

| Operation            | Effect on Ordering          | Notes                                             |
| -------------------- | --------------------------- | ------------------------------------------------- |
| `sorted()`           | Enforces order              | Parallel sort overhead                            |
| `unordered()`        | Removes order constraints   | Allows better parallel performance                |
| `distinct()`         | Requires order/statefulness | Can reduce parallel efficiency                    |
| `limit()` / `skip()` | Requires order              | High overhead in parallel to maintain correctness |

---

### 4. **Best Practices**

* Use `unordered()` **only when order is truly irrelevant**.
* Avoid **stateful intermediate operations** on huge datasets in parallel streams if possible.
* Use **`forEachOrdered()`** sparingly; consider if ordering is needed at all.
* Test performance both with parallel and sequential streams, especially when ordering constraints exist.
* For **complex order-dependent processing**, consider sequential streams or custom thread-safe designs.

---