# Java Streams with Custom Collectors - Study Notes

## Basic Stream Collection Examples



### 1. Grouping Strings by Their Length

```java
public static Map<Integer, List<String>> groupByLength(List<String> words) {
    return words.stream().collect(
        HashMap::new,
        (map, word) -> map.computeIfAbsent(word.length(), k -> new ArrayList<>()).add(word),
        Map::putAll
    );
}

// Sample call
System.out.println(groupByLength(Arrays.asList("apple", "bat", "cat", "apricot")));
```

**Output:**
```
{3=[bat, cat], 5=[apple], 7=[apricot]}
```

---

### 2. Counting Word Frequencies in a Sentence

```java
public static Map<String, Integer> countWordFrequency(String sentence) {
    return Arrays.stream(sentence.split("\\s+")).collect(
        HashMap::new,
        (map, word) -> map.put(word, map.getOrDefault(word, 0) + 1),
        Map::putAll
    );
}

// Sample call
System.out.println(countWordFrequency("apple bat cat apple bat"));
```

**Output:**
```
{apple=2, bat=2, cat=1}
```

---

### 3. Concatenating Strings by First Letter

```java
public static LinkedList<String> concatByFirstLetter(List<String> words) {
    return words.stream().collect(
        LinkedList::new,
        (list, word) -> {
            String newName = word.charAt(0) + "-" + word;
            list.add(newName);
        },
        List::addAll
    );
}

public static StringBuilder concatByFirstLetter(List<String> words) {
    return words.stream().collect(
        StringBuilder::new,
        (sb, word) -> {
            String newName = word.charAt(0) + "-" + word;
            sb.append(newName);
        },
        StringBuilder::append
    );
}


// Sample call
System.out.println(concatByFirstLetter(Arrays.asList("apple", "apricot", "banana", "blueberry")));
```

**Output:**
```
{a=apple, apricot, b=banana, blueberry}
```

---

### 4. Partition Numbers into Even and Odd

#### Version 1: Custom Collector with Explicit Initialization
```java
public static Map<Boolean, List<Integer>> partitionEvenOdd(List<Integer> numbers) {
    return numbers.stream().collect(
        () -> {
            Map<Boolean, List<Integer>> map = new HashMap<>();
            map.put(true, new ArrayList<>());
            map.put(false, new ArrayList<>());
            return map;
        },
        (map, num) -> map.get(num % 2 == 0).add(num),
        (map1, map2) -> {
            map1.get(true).addAll(map2.get(true));
            map1.get(false).addAll(map2.get(false));
        }
    );
}
```

#### Version 2: Alternative Custom Collector
```java
public static Map<Boolean, List<Integer>> partitionEvenOddCustom(List<Integer> numbers) {
    return numbers.stream().collect(
        () -> {
            Map<Boolean, List<Integer>> map = new HashMap<>();
            map.put(true, new ArrayList<>());  // true for even
            map.put(false, new ArrayList<>()); // false for odd
            return map;
        },
        (map, num) -> map.get(num % 2 == 0).add(num),
        (map1, map2) -> map2.putAll(map1) 
    );
}
```

#### Version 3: Using computeIfAbsent
```java
public static Map<Boolean, List<Integer>> partitionEvenOdd(List<Integer> numList) {
    return numList.stream().collect(
        HashMap::new,
        (map, num) -> {
            boolean isEven = num % 2 == 0;
            map.computeIfAbsent(isEven, k -> new ArrayList<>()).add(num);
        },
        Map::putAll
    );
}
```

#### Version 4: Using Built-in partitioningBy Collector
```java
public Map<Boolean, List<Integer>> partitionEvenOdd(List<Integer> numList) {
    return numList.stream()
                  .collect(Collectors.partitioningBy(num -> num % 2 == 0));
}
```

**Sample call:**
```java
System.out.println(partitionEvenOdd(Arrays.asList(1, 2, 3, 4, 5, 6)));
```

**Output:**
```
{false=[1, 3, 5], true=[2, 4, 6]}
```

---

### 5. Merging Maps

#### Version 1: Using merge() method
```java
public static Map<String, Integer> mergeMaps(List<Map<String, Integer>> listOfMaps) {
    return listOfMaps.stream().collect(
        HashMap::new,
        (result, map) -> map.forEach((k, v) -> result.merge(k, v, Integer::sum)),
        Map::putAll
    );
    // map.merge(key, value, (oldVal, newVal) -> oldVal + newVal);
    // Map.merge(k, v, remappingFunction) 
    // remappingFunction.apply(existingValue, newValue)
    
}
```
```java
map.forEach((k, v) -> result.merge(k, v, Integer::sum)); // Summing values
result.merge(k, v, (oldVal, newVal) -> Math.min(oldVal, newVal));
result.merge(k, v, (oldVal, newVal) -> Math.max(oldVal, newVal));
result.merge(k, v, Integer::min);

result.merge(k, v, (oldVal, newVal) -> null); // Will remove the key
result.merge(k, v, (oldVal, newVal) -> oldVal );
result.merge(k, v, (oldVal, newVal) -> (oldVal + newVal) / 2);
result.merge(k, v, (oldVal, newVal) -> oldVal + ", " + newVal);

result.merge(k, v, (oldVal, newVal) -> {
    throw new IllegalStateException("Duplicate key: " + k);
});
```

#### Version 2: Using flatMap and toMap
```java
public static Map<String, Integer> mergeMaps(List<Map<String, Integer>> listOfMaps) {
    return listOfMaps.stream()
        .flatMap(map -> map.entrySet().stream())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            Integer::sum
        ));
}


public static Map<String, Integer> mergeMaps(List<Map<String, Integer>> listOfMaps) {
    return listOfMaps.stream()
        .flatMap(map -> map.entrySet().stream())
        .collect(Collectors.toMap(
            entry -> entry.getKey(),            // lambda
            entry -> entry.getValue(),          // lambda
            (v1, v2) -> v1 + v2                 // lambda for Integer::sum
        ));
}

```

**Sample call:**
```java
System.out.println(mergeMaps(List.of(
    Map.of("a", 1, "b", 2),
    Map.of("a", 3, "c", 4)
)));
```

**Output:**
```
{a=4, b=2, c=4}
```

---

## Advanced Map Types Usage Examples

### 1. Using `TreeMap` – Sorted Product Catalog by Name

```java
public static Map<String, Double> sortedProductCatalog(Map<String, Double> products) {
    return products.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (a, b) -> b,
            TreeMap::new
        ));
}

// Alternative version with Product objects
public static Map<String, Double> sortedProductCatalog(List<Product> products) {
    return products.stream().collect(
        TreeMap::new, // Supplier: TreeMap keeps keys sorted
        (map, product) -> map.put(product.getName(), product.getPrice()), // Accumulator
    //  (map, product) -> map.putIfAbsent(product.getName(), product.getPrice()), // Don't overwrite
        Map::putAll // Combiner
    );
}

// Advanced example: Grouping by date (descending), keeping the latest product per date
TreeMap<LocalDate, Product> filteredGroupedDescending = filteredProducts.stream()
    .collect(Collectors.toMap(
        Product::getCreatedDate,
        Function.identity(),
        (existing, replacement) -> replacement, // you can change logic here if needed
        () -> new TreeMap<>(Comparator.reverseOrder())
    ));

// Iterate and print
System.out.println("=== Products grouped by date (descending) ===");
for (Map.Entry<LocalDate, Product> entry : filteredGroupedDescending.entrySet()) {
    System.out.println(entry.getKey() + " => " + entry.getValue());
}
```

**Sample call:**
```java
Map<String, Double> input = Map.of(
    "Laptop", 1200.0,
    "Mobile", 800.0,
    "AirPods", 200.0
);
```

**Output:**
```
{AirPods=200.0, Laptop=1200.0, Mobile=800.0}
```

**Business Use Case**: Use `TreeMap` when **alphabetical ordering of product names** is required for consistent UI display.

---

### 2. Using `LinkedHashMap` – Preserve Insertion Order for Recently Added Records

```java
public static Map<String, String> recentLogsOrdered(List<String> logs) {
    return logs.stream().collect(Collectors.toMap(
        log -> log.split(":")[0],      // key = timestamp
        log -> log.split(":")[1],      // value = message
        (oldVal, newVal) -> newVal,
        LinkedHashMap::new
    ));
}
```

**Sample call:**
```java
List<String> logs = Arrays.asList(
    "2024-06-01:Started",
    "2024-06-02:Processing",
    "2024-06-03:Completed"
);
System.out.println(recentLogsOrdered(logs));
```

**Output:**
```
{2024-06-01=Started, 2024-06-02=Processing, 2024-06-03=Completed}
```

**Business Use Case**: Use `LinkedHashMap` when you need to **retain the order in which records/logs were received** (e.g., auditing, recent activity).

---

### 3. Using `ConcurrentHashMap` – Thread-Safe Word Counter in Real-Time Logging System

```java
public static Map<String, Integer> threadSafeWordCounter(List<String> sentences) {
    ConcurrentMap<String, Integer> wordCount = new ConcurrentHashMap<>();
    sentences.parallelStream()
        .flatMap(s -> Arrays.stream(s.split("\\s+")))
        .forEach(word -> wordCount.merge(word, 1, Integer::sum));
    return wordCount;
}
```

**Sample call:**
```java
List<String> lines = Arrays.asList("log started", "log processing", "log completed", "log started");
System.out.println(threadSafeWordCounter(lines));
```

**Output (order may vary):**
```
{log=4, started=2, processing=1, completed=1}
```

**Business Use Case**: Use `ConcurrentHashMap` when **multiple threads** may update the map concurrently (e.g., real-time analytics or logs).

---

### 4. Using `TreeMap` with Custom Sorting – Rank Students by Score Descending

#### **Ranking Students by Exam Scores**

**Scenario:** In an online education app, after students take a quiz, you want to show a **leaderboard**.

```java
Map<String, Integer> scores = Map.of(
    "Alice", 85,
    "Bob", 92,
    "Charlie", 78,
    "David", 92
);

Map<String, Integer> ranked = scores.entrySet().stream()
    // to compare by values, not on keys. 
    .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
    .collect(Collectors.toMap(
        Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

System.out.println(ranked);
```

**Output:**

```
Bob=92
David=92
Alice=85
Charlie=78
```

```java
// Alternative: Grouping products by date with TreeMap for sorted dates
Map<LocalDate, List<Product>> grouped = productsList.stream()
    .collect(Collectors.groupingBy(
        Product::getCreatedDate,         // group by createdDate
        TreeMap::new,                    // use TreeMap to keep dates sorted
        Collectors.toList()              // collect grouped products into a list
    ));
```

**Sample call:**
```java
Map<String, Integer> students = Map.of(
    "Alice", 85,
    "Bob", 92,
    "Charlie", 78
);
System.out.println(rankStudentsDescending(students));
```

**Output:**
```
{Bob=92, Alice=85, Charlie=78}
```

## Key Takeaways

- **HashMap**: Default choice for general-purpose key-value storage
- **TreeMap**: Use when you need sorted keys (natural or custom ordering)
- **LinkedHashMap**: Use when insertion order matters
- **ConcurrentHashMap**: Use for thread-safe operations in concurrent environments
- **Custom Collectors**: Provide fine-grained control over collection operations
- **Built-in Collectors**: Use when available (like `partitioningBy`, `groupingBy`) for cleaner code


```java
String removeDuplicateChars = Stream.of("a", "b", "a", "c")
    .collect(
        () -> {                    // ✅ supplier
            System.out.println("Creating StringBuilder");
            return new StringBuilder();
        },
        (sb, ch) -> {              // ❌ accumulator: no return
            if (sb.indexOf(ch) == -1) {
                sb.append(ch);
            }
        },
        (sb1, sb2) -> {            // ✅ combiner: must return
            sb1.append(sb2);
            return sb1;
        }
    ).toString();



public static String removeDuplicateChars(String input) {
    Set<Character> seen = new HashSet<>();

    StringBuilder result = input.chars()
        .mapToObj(c -> (char) c)
        .collect(
            StringBuilder::new,                          // supplier
            (sb, ch) -> {                                // accumulator
                if (seen.add(ch)) sb.append(ch);
            },
            StringBuilder::append                        // combiner
        );

    return result.toString();
}


public static String removeDuplicateChars(String input) {
    Set<Character> seen = new HashSet<>();
    StringBuilder result = new StringBuilder();

    input.chars()
         .mapToObj(c -> (char) c)
         .filter(seen::add) // only adds if not already in set
         .forEach(result::append);

    return result.toString();
}


public static String removeDuplicateChars(String input) {
    return input.chars()
        .mapToObj(c -> (char) c)
        .collect(
            Collector.of(
                LinkedHashMap<Character, Boolean>::new, // supplier
                (map, ch) -> map.putIfAbsent(ch, true), // accumulator
                (map1, map2) -> {                       // combiner
                    map2.forEach(map1::putIfAbsent);
                    return map1;
                },
                map -> {                               // finisher
                    StringBuilder sb = new StringBuilder();
                    map.keySet().forEach(sb::append);
                    return sb.toString();
                }
            )
        );
}
```

Absolutely! Let's explore more examples using **`Collector.of(...)` with a `finisher`**, which is useful when:

* You want to **transform** the result into a different type (e.g., from `Map` to `String`)
* You need **custom post-processing** (e.g., formatting, filtering, sorting)

---

## ✅ `Collector.of(...)` Structure Recap

```java
Collector.of(
    Supplier<A>,                   // Creates mutable container
    BiConsumer<A, T>,              // Accumulator
    BinaryOperator<A>,             // Combiner
    Function<A, R>                 // Finisher (returns final result)
)
```

Now let's walk through some practical examples:

---

## 1️⃣ **Remove duplicate characters and return String**

```java
public static String removeDuplicateChars(String input) {
    return input.chars()
        .mapToObj(c -> (char) c)
        .collect(
            Collector.of(
                LinkedHashSet::new,
                Set::add,
                (s1, s2) -> { s1.addAll(s2); return s1; },
                set -> {
                    StringBuilder sb = new StringBuilder();
                    set.forEach(sb::append);
                    return sb.toString();
                }
            )
        );
}
```

🧪 Output for `banana`: `"ban"`

---

## 2️⃣ **Group names by length and return a custom formatted string**

```java
public static String groupNamesByLength(List<String> names) {
    return names.stream()
        .collect(
            Collector.of(
                HashMap<Integer, List<String>>::new,
                (map, name) -> map.computeIfAbsent(name.length(), k -> new ArrayList<>()).add(name),
                (m1, m2) -> {
                    m2.forEach((k, v) -> m1.merge(k, v, (l1, l2) -> { l1.addAll(l2); return l1; }));
                    return m1;
                },
                map -> map.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining(", "))
            )
        );
}
```

🧪 Output for `["Tom", "Jerry", "Anna", "Bob"]`:

```
3=[Tom, Bob], 5=[Jerry], 4=[Anna]
```

---

## 3️⃣ **Concatenate uppercase names in sorted order**

```java
public static String concatSortedUppercase(List<String> names) {
    return names.stream()
        .collect(
            Collector.of(
                ArrayList<String>::new,
                List::add,
                (l1, l2) -> { l1.addAll(l2); return l1; },
                list -> list.stream()
                            .map(String::toUpperCase)
                            .sorted()
                            .collect(Collectors.joining(", "))
            )
        );
}
```

🧪 Input: `["john", "alice", "bob"]`
🧪 Output: `"ALICE, BOB, JOHN"`

---

## 4️⃣ **Count character frequency and return a formatted string**

```java
public static String charFrequency(String input) {
    return input.chars()
        .mapToObj(c -> (char) c)
        .collect(
            Collector.of(
                HashMap<Character, Integer>::new,
                (map, ch) -> map.merge(ch, 1, Integer::sum),
                (m1, m2) -> {
                    m2.forEach((k, v) -> m1.merge(k, v, Integer::sum));
                    return m1;
                },
                map -> map.entrySet().stream()
                          .map(e -> e.getKey() + ":" + e.getValue())
                          .collect(Collectors.joining(", "))
            )
        );
}
```

🧪 Input: `"banana"`
🧪 Output: `"b:1, a:3, n:2"`

---

## 🔁 Summary of Use-Cases for `finisher`

| Use Case                     | Collection Container  | Finisher Output          |
| ---------------------------- | --------------------- | ------------------------ |
| Unique chars to `String`     | `Set<Character>`      | `StringBuilder → String` |
| Grouping to formatted string | `Map<Integer, List>`  | `Map → String`           |
| Sorting + formatting         | `List<String>`        | `String`                 |
| Frequency map to string      | `Map<Character, Int>` | `String`                 |

---
