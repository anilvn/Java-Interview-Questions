
---

### ✅ `Collectors.groupingBy()` Overloads Explained

Here are the key overloads of `groupingBy`:

---
```java
List<String> words = List.of("cat", "car", "dog", "deer");

Map<Character, String> orderedMap = words.stream()
    .collect(Collectors.toMap(
        word -> word.charAt(0),                  // key
        word -> word,                            // value
        (a, b) -> a + "/" + b,                   // merge on duplicate key
        LinkedHashMap::new                       // preserve insertion order
    ));

System.out.println(orderedMap);

```

### **1. Basic Grouping**

```java
public static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(Function<? super T, ? extends K> classifier)
```

**Groups elements into a `Map<K, List<T>>`** using a classifier function (grouping key).

#### ✅ Example:

```java
List<String> words = List.of("apple", "banana", "apricot", "blueberry");

Map<Character, List<String>> grouped = words.stream()
    .collect(Collectors.groupingBy(word -> word.charAt(0)));
```

📦 Output:

```java
{a=[apple, apricot], b=[banana, blueberry]}
```

---

### **2. Grouping With Downstream Collector**

```java
public static <T, K, D> Collector<T, ?, Map<K, D>> groupingBy(
    Function<? super T, ? extends K> classifier,
    Collector<? super T, ?, D> downstream)
```

Instead of collecting to `List<T>`, you can **customize** the aggregation — like using `counting()`.

#### ✅ Example:

```java
Map<Character, Long> frequencyMap = input.chars()
    .mapToObj(c -> (char) c)
    .collect(Collectors.groupingBy(
        Function.identity(),
        Collectors.counting()
    ));
```

📦 Output:

```java
{a=3, b=1, n=2}
```

---

### **3. Grouping With Map Type + Downstream Collector**

```java
public static <T, K, D, M extends Map<K, D>> Collector<T, ?, M> groupingBy(
    Function<? super T, ? extends K> classifier,
    Supplier<M> mapFactory,
    Collector<? super T, ?, D> downstream)
```

You can **control the map type** (e.g., use `LinkedHashMap` instead of `HashMap`) and the aggregation.

#### ✅ Example:

```java
LinkedHashMap<Character, Long> orderedCounts = str.chars()
    .mapToObj(c -> (char) c)
    .collect(Collectors.groupingBy(
        Function.identity(),
        LinkedHashMap::new,
        Collectors.counting()
    ));
```

📦 Output (in insertion order):

```java
{b=1, a=3, n=2}
```

---

### 📦 Another Example with Different Downstream Collector

```java
Map<Integer, Set<String>> lengthToWords = List.of("apple", "ant", "bat", "ball", "car")
    .stream()
    .collect(Collectors.groupingBy(
        String::length,
        TreeMap::new,
        Collectors.toSet()
    ));
```

📦 Output:

```java
{3=[bat, ant, car], 4=[ball], 5=[apple]}
```

---

### ✅ Summary Table

| Overload                                         | Purpose                                                 |
| ------------------------------------------------ | ------------------------------------------------------- |
| `groupingBy(Function)`                           | Group into `Map<K, List<T>>`                            |
| `groupingBy(Function, Collector)`                | Group with custom aggregation (e.g., counting, summing) |
| `groupingBy(Function, Supplier<Map>, Collector)` | Group with custom map type and aggregation              |

---

<br/><br/><br/>


---

## 🔹 1. **Basic Grouping By a Property**

Group a list of strings by their **first character**:

```java
List<String> names = List.of("Alice", "Adam", "Bob", "Brian");

Map<Character, List<String>> result = names.stream()
    .collect(Collectors.groupingBy(name -> name.charAt(0)));

System.out.println(result);
```

📦 **Output**:

```java
{A=[Alice, Adam], B=[Bob, Brian]}
```

---

## 🔹 2. **Grouping with Counting (Summary)**

Count how many times each word appears:

```java
List<String> words = List.of("apple", "banana", "apple", "orange");

Map<String, Long> countMap = words.stream()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

System.out.println(countMap);
```

📦 **Output**:

```java
{orange=1, banana=1, apple=2}
```

---

## 🔹 3. **Grouping with a Custom Map (Preserve Order)**

Use `LinkedHashMap` to preserve insertion order:

```java
Map<String, Long> orderedCount = words.stream()
    .collect(Collectors.groupingBy(
        Function.identity(),
        LinkedHashMap::new,
        Collectors.counting()
    ));

System.out.println(orderedCount);
```

📦 **Output**:

```java
{apple=2, banana=1, orange=1}
```

---

## 🔹 4. **Grouping with Averaging**

Group people by gender and calculate average age:

```java
record Person(String name, String gender, int age) {}

List<Person> people = List.of(
    new Person("Alice", "Female", 30),
    new Person("Bob", "Male", 25),
    new Person("Carol", "Female", 35)
);

Map<String, Double> avgAgeByGender = people.stream()
    .collect(Collectors.groupingBy(
        Person::gender,
        Collectors.averagingInt(Person::age)
    ));

System.out.println(avgAgeByGender);

// ----------------------------
        // First Approach: Step-by-step
        // ----------------------------
        Map<String, List<Person>> peopleByGender = people.stream()
            .collect(Collectors.groupingBy(Person::gender));

        Map<String, Double> avgAgeByGender1 = new HashMap<>();

        for (Map.Entry<String, List<Person>> entry : peopleByGender.entrySet()) {
            String gender = entry.getKey();
            List<Person> group = entry.getValue();

            int sum = group.stream().mapToInt(Person::age).sum();
            int count = group.size();
            double avg = sum / (double) count;

            avgAgeByGender1.put(gender, avg);
        }

        System.out.println("Step-by-step approach: " + avgAgeByGender1);

        // ----------------------------
        // Second Approach: Using groupingBy + collectingAndThen
        // ----------------------------
        Map<String, Double> avgAgeByGender2 = people.stream()
            .collect(Collectors.groupingBy(
                Person::gender,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> {
                        int sum = list.stream().mapToInt(Person::age).sum();
                        int count = list.size();
                        return sum / (double) count;
                    }
                )
            ));
```

📦 **Output**:

```java
{Female=32.5, Male=25.0}
```

---

## 🔹 5. **Grouping with Mapping**

Group names by age and collect only the **first letters**:

```java
Map<Integer, Set<Character>> initialsByAge = people.stream()
    .collect(Collectors.groupingBy(
        Person::age,
        Collectors.mapping(p -> p.name().charAt(0), Collectors.toSet())
    ));// ouput expected: City - set Of emp

System.out.println(initialsByAge);
```

📦 **Output**:

```java
{35=[C], 25=[B], 30=[A]}
```

---

## 🔹 6. **Grouping by Enum or Category**

```java
enum Status { ACTIVE, INACTIVE }

record User(String name, Status status) {}

List<User> users = List.of(
    new User("A", Status.ACTIVE),
    new User("B", Status.INACTIVE),
    new User("C", Status.ACTIVE)
);

Map<Status, List<User>> grouped = users.stream()
    .collect(Collectors.groupingBy(User::status));

System.out.println(grouped);
```

📦 **Output**:

```java
{INACTIVE=[User[name=B, status=INACTIVE]], ACTIVE=[User[name=A, status=ACTIVE], User[name=C, status=ACTIVE]]}
```

---

## 🔹 7. **Nested Grouping**

Group by gender → then by age:

```java
Map<String, Map<Integer, List<Person>>> nestedGroup = people.stream()
    .collect(Collectors.groupingBy(
        Person::gender,
        Collectors.groupingBy(Person::age)
    ));

System.out.println(nestedGroup);
```

📦 **Output**:

```java
{Female={30=[Person[name=Alice, gender=Female, age=30]], 35=[Person[name=Carol, gender=Female, age=35]]}, Male={25=[Person[name=Bob, gender=Male, age=25]]}}
```

---

## 🔹 8. **Partitioning (Only Two Groups)**

Split into adults and minors:

```java
Map<Boolean, List<Person>> partitioned = people.stream()
    .collect(Collectors.partitioningBy(p -> p.age() >= 18));

System.out.println(partitioned);
```

📦 **Output**:

```java
{true=[Person[name=Alice, gender=Female, age=30], Person[name=Bob, gender=Male, age=25], Person[name=Carol, gender=Female, age=35]], false=[]}
```

---

## 🔹 9. **Custom Classifier Function**

Group strings by word length range:

```java
Map<String, List<String>> byLengthCategory = List.of("a", "apple", "banana", "cat")
    .stream()
    .collect(Collectors.groupingBy(word -> {
        int len = word.length();
        if (len <= 3) return "SHORT";
        else if (len <= 6) return "MEDIUM";
        else return "LONG";
    }));

System.out.println(byLengthCategory);
```

📦 **Output**:

```java
{SHORT=[a, cat], MEDIUM=[apple], LONG=[banana]}
```

---

## 🔹 10. **Grouping and Reducing (Finding Min/Max)**

Find the oldest person per gender:

```java
Map<String, Optional<Person>> oldestByGender = people.stream()
    .collect(Collectors.groupingBy(
        Person::gender,
        Collectors.maxBy(Comparator.comparingInt(Person::age))
    ));

System.out.println(oldestByGender);

Optional<Person> oldestPerson = people.stream()
        .collect(Collectors.maxBy(Comparator.comparingInt(Person::age)));
```

📦 **Output**:

```java
{Female=Optional[Person[name=Carol, gender=Female, age=35]], Male=Optional[Person[name=Bob, gender=Male, age=25]]}
```

---

## 🧠 Summary Table

| Use Case              | Method Used                                    |
| --------------------- | ---------------------------------------------- |
| Group into lists      | `groupingBy(Function)`                         |
| Count items per group | `groupingBy(Function, counting())`             |
| Custom map type       | `groupingBy(Function, Supplier, Collector)`    |
| Average, sum, reduce  | `groupingBy(Function, averaging/summing/etc.)` |
| Map transformation    | `groupingBy(Function, mapping(...))`           |
| Nested groups         | `groupingBy(..., groupingBy(...))`             |
| Split into two groups | `partitioningBy(...)`                          |
| Top N, min/max        | `groupingBy(..., maxBy()/minBy())`             |

---

Let me know if you want a printable version or want these examples in a `.java` file.
