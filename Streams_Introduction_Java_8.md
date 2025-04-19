# ✨ Java Stream Operations Overview ✨

## 📜 Introduction

Java Streams, introduced in Java 8, provide a powerful and declarative way to process sequences of elements. They allow for functional-style operations on collections, enabling concise and potentially parallelized data manipulation. Streams support two main types of operations: **Intermediate** and **Terminal**.

---
### Additional Resources

If you found this guide helpful, you might also be interested in my other Spring Framework resources:
- [Core Java & Java-8 Interview Questions](https://github.com/anilvn/Java-Interview-Questions/tree/main)
- [Spring Boot Interview Questions](https://github.com/anilvn/spring-boot-interview-questions/tree/main)
- [Microservices with Spring Cloud Tutorials](https://javatechonline.com/microservices-tutorial/)

Feel free to star and fork these repositories if you find them useful!

---

## ⚙️ Intermediate Operations

> Intermediate operations return another `Stream` as a result, allowing them to be chained together to form a processing pipeline.
> A key characteristic is that they are ***lazy***; they don't execute until a terminal operation is invoked on the stream pipeline.

**Common Examples:**

* `filter(Predicate<T>)`
* `map(Function<T, R>)`
* `flatMap(Function<T, Stream<R>>)`
* `mapMulti(BiConsumer<T, Consumer<R>>)` (Java 16+)
* `distinct()`
* `sorted()` / `sorted(Comparator<T>)`
* `peek(Consumer<T>)` (Mainly for debugging)
* `limit(long)`
* `skip(long)`
* `takeWhile(Predicate<T>)` (Java 9+)
* `dropWhile(Predicate<T>)` (Java 9+)

---

## 🏁 Terminal Operations

> Terminal operations produce a non-stream result, such as a primitive value, an object, a collection, or simply perform a side effect (like `forEach`).
> They trigger the ***eager*** execution of the entire stream pipeline (including all chained intermediate operations).
> A stream pipeline can have at most one terminal operation, which must be the final operation.

**Common Examples:**

* `forEach(Consumer<T>)` / `forEachOrdered(Consumer<T>)`
* `toArray()` / `toArray(IntFunction<A[]>)`
* `reduce(...)`
* `collect(...)`
* `toList()` (Java 16+)
* `min(Comparator<T>)` / `max(Comparator<T>)`
* `count()`
* `anyMatch(Predicate<T>)` / `allMatch(Predicate<T>)` / `noneMatch(Predicate<T>)`
* `findFirst()` / `findAny()`

---

## ⚖️ Intermediate vs. Terminal Operations

| Feature                  | Intermediate Operations                                  | Terminal Operations                                                    |
| :----------------------- | :------------------------------------------------------- | :--------------------------------------------------------------------- |
| **Return Type** | Returns a `Stream`.                                      | Returns a non-stream value (primitive, object, collection, `void`).    |
| **Chaining** | Can be chained together to form a pipeline.              | Cannot be chained after; terminates the pipeline.                      |
| **Pipeline Composition** | Pipeline can contain any number of intermediate ops.     | Pipeline can have max one terminal op, always at the end.              |
| **Execution (Laziness)** | **Lazy** (execution deferred until terminal op).         | **Eager** (triggers pipeline execution).                               |
| **Result** | Do not produce the final result themselves.              | Produce the final result or side effect of the pipeline.             |

---

## 🚀 Stream Creation Methods

Here are various ways to create a stream:

* ***Create an empty stream.***
    ```java
    import java.util.stream.Stream;
    // ... other necessary imports

    Stream<String> emptyStream = Stream.empty();
    ```

* ***Create a stream with a single element.***
    ```java
    Stream<String> singleElementStream = Stream.of("apple");
    ```

* ***Create a stream from a value that might be null (becomes empty stream if null).***
    ```java
    Stream<String> nullableStream = Stream.ofNullable(null); // Empty
    Stream<String> valueStream = Stream.ofNullable("value"); // Stream with "value"
    ```

* ***Create a stream from multiple elements.***
    ```java
    Stream<String> multiElementStream = Stream.of("apple", "banana", "cherry");
    ```

* ***Create a stream from an existing array.***
    ```java
    String[] array = {"a", "b", "c"};
    Stream<String> streamFromArray = Arrays.stream(array);
    ```

* ***Create a stream from a Collection (like List or Set).***
    ```java
    List<String> list = Arrays.asList("x", "y", "z");
    Stream<String> streamFromList = list.stream(); // Common way
    ```

* ***Create a stream using `Stream.builder()`.***
    ```java
    Stream<String> builtStream = Stream.<String>builder()
                                     .add("a").add("b").add("c")
                                     .build();
    ```

* ***Create an infinite sequential ordered stream (often limited).***
    ```java
    Stream<Integer> iteratedStream = Stream.iterate(1, n -> n + 1).limit(5);
    // Result: Stream containing [1, 2, 3, 4, 5]
    ```

* ***Create a finite sequential ordered stream using `iterate` with a predicate (Java 9+).***
    ```java
    Stream<Integer> boundedIteratedStream = Stream.iterate(1, n -> n < 10, n -> n + 2);
    // Result: Stream containing [1, 3, 5, 7, 9]
    ```

* ***Create an infinite sequential unordered stream using a Supplier (often limited).***
    ```java
    Stream<Double> generatedStream = Stream.generate(Math::random).limit(3);
    // Result: Stream containing 3 random doubles
    ```

* ***Concatenate two existing streams.***
    ```java
    Stream<String> stream1 = Stream.of("apple", "banana");
    Stream<String> stream2 = Stream.of("cherry", "date");
    Stream<String> concatenatedStream = Stream.concat(stream1, stream2);
    // Result: Stream containing ["apple", "banana", "cherry", "date"]
    ```

---

## ✨ Intermediate Operations Examples ✨

### `filter()`

* ***Keeps only elements matching the predicate (strings starting with "a").***
    ```java
    List<String> filtered = Stream.of("apple", "banana", "cherry")
                                 .filter(s -> s.startsWith("a"))
                                 .collect(Collectors.toList());
    // Result: ["apple"]
    ```

---

### `map()`

* ***Transforms each element (string to its length).***
    ```java
    List<Integer> lengths = Stream.of("apple", "banana", "cherry")
                                  .map(String::length)
                                  .collect(Collectors.toList());
    // Result: [5, 6, 6]
    ```

---

### `mapToInt()`, `mapToLong()`, `mapToDouble()`

* ***Transforms elements to a primitive stream (string to IntStream of lengths).***
    ```java
    IntStream intStream = Stream.of("apple", "banana", "cherry")
                               .mapToInt(String::length);
    // Result: IntStream containing 5, 6, 6
    ```

---

### `flatMap()`

* ***Transforms each element into a stream and flattens the results into one stream (list of lists to a single list).***
    ```java
    Stream<List<String>> streamOfLists = Stream.of(
        Arrays.asList("a", "b"),
        Arrays.asList("c", "d")
    );
    List<String> flatList = streamOfLists.flatMap(Collection::stream)
                                         .collect(Collectors.toList());
    // Result: ["a", "b", "c", "d"]
    ```

---

### `flatMapToInt()`, `flatMapToLong()`, `flatMapToDouble()`

* ***Flattens elements into a primitive stream (list of integer lists to a single IntStream).***
    ```java
    List<List<Integer>> nestedList = Arrays.asList(
        Arrays.asList(1, 2),
        Arrays.asList(3, 4)
    );
    IntStream flatIntStream = nestedList.stream()
                                       .flatMapToInt(list -> list.stream().mapToInt(Integer::intValue));
    // Result: IntStream containing 1, 2, 3, 4
    ```

---

### `mapMulti()` (Java 16+)

* ***Replaces each element with zero or more elements (maps each string to its uppercase and lowercase versions).***
    ```java
    List<String> resultMulti = new ArrayList<>();
    Stream.of("apple", "banana", "cherry").<String>mapMulti((s, consumer) -> {
        consumer.accept(s.toUpperCase());
        consumer.accept(s.toLowerCase());
    }).forEach(resultMulti::add);
    // Result: ["APPLE", "apple", "BANANA", "banana", "CHERRY", "cherry"]
    ```

---

### `distinct()`

* ***Removes duplicate elements based on `equals()`.***
    ```java
    List<String> distinct = Stream.of("apple", "banana", "apple")
                                  .distinct()
                                  .collect(Collectors.toList());
    // Result: ["apple", "banana"]
    ```

---

### `sorted()`

* ***Sorts elements according to their natural order.***
    ```java
    List<String> sorted = Stream.of("banana", "apple", "cherry")
                                .sorted()
                                .collect(Collectors.toList());
    // Result: ["apple", "banana", "cherry"]
    ```

* ***Sorts elements using a custom comparator (reverse alphabetical order).***
    ```java
    List<String> namesSort = Arrays.asList("Charlie", "Alice", "Bob");
    List<String> sortedNames = namesSort.stream()
                                       .sorted(Comparator.reverseOrder())
                                       .collect(Collectors.toList());
    // Result: ["Charlie", "Bob", "Alice"]
    ```

* ***Sorts elements using a custom comparator (by string length).***
    ```java
    List<String> sortedByLength = Stream.of("banana", "apple", "cherry")
                                       .sorted(Comparator.comparingInt(String::length))
                                       .collect(Collectors.toList());
    // Result: ["apple", "cherry", "banana"] or ["apple", "banana", "cherry"] (stability dependent)
    ```

---

### `peek()`

* ***Performs an action on each element as it flows through the stream (used mainly for debugging).***
    ```java
    List<String> peeked = Stream.of("apple", "banana", "cherry")
                               .peek(s -> System.out.println("Processing: " + s)) // Debugging action
                               .map(String::toUpperCase)
                               .collect(Collectors.toList());
    // Output during processing: Processing: apple, Processing: banana, Processing: cherry
    // Result: ["APPLE", "BANANA", "CHERRY"]
    ```

---

### `limit()`

* ***Truncates the stream to be no longer than the specified size.***
    ```java
    List<String> limited = Stream.of("a", "b", "c", "d", "e")
                                 .limit(3)
                                 .collect(Collectors.toList());
    // Result: ["a", "b", "c"]
    ```

---

### `skip()`

* ***Discards the first `n` elements of the stream.***
    ```java
    List<String> skipped = Stream.of("a", "b", "c", "d", "e")
                                .skip(2)
                                .collect(Collectors.toList());
    // Result: ["c", "d", "e"]
    ```

---

### `takeWhile()` (Java 9+)

* ***Takes elements from the start while the predicate is true, stops at the first false element.***
    ```java
    List<String> takenWhile = Stream.of("a", "b", "c", "d")
                                    .takeWhile(s -> s.compareTo("c") < 0) // Takes "a", "b"
                                    .collect(Collectors.toList());
    // Result: ["a", "b"]
    ```

---

### `dropWhile()` (Java 9+)

* ***Drops elements from the start while the predicate is true, keeps the rest starting from the first false element.***
    ```java
    List<String> droppedWhile = Stream.of("a", "b", "c", "d")
                                     .dropWhile(s -> s.compareTo("c") < 0) // Drops "a", "b"
                                     .collect(Collectors.toList());
    // Result: ["c", "d"]
    ```

---

## ✨ Terminal Operations Examples ✨

### `forEach()`

* ***Performs an action for each element (prints each element). Order not guaranteed in parallel streams.***
    ```java
    System.out.println("forEach example:");
    Stream.of("apple", "banana", "cherry").forEach(System.out::println);
    // Output: apple, banana, cherry (order may vary if parallel)
    ```

---

### `forEachOrdered()`

* ***Performs an action for each element, maintaining encounter order even in parallel streams.***
    ```java
    System.out.println("\nforEachOrdered example (parallel):");
    Stream.of("apple", "banana", "cherry").parallel().forEachOrdered(System.out::println);
    // Output: apple, banana, cherry (guaranteed order)
    ```

---

### `toArray()`

* ***Collects stream elements into an `Object` array.***
    ```java
    List<String> namesToArray1 = Arrays.asList("Alice", "Bob", "Charlie");
    Object[] nameArrayObj = namesToArray1.stream().toArray();
    System.out.println("\ntoArray (Object[]): " + Arrays.toString(nameArrayObj));
    // Output: [Alice, Bob, Charlie]
    ```

* ***Collects stream elements into a typed array using a generator.***
    ```java
    List<String> namesToArray2 = Arrays.asList("Alice", "Bob", "Charlie");
    String[] nameArrayTyped = namesToArray2.stream().toArray(String[]::new);
    System.out.println("toArray (String[]): " + Arrays.toString(nameArrayTyped));
    // Output: [Alice, Bob, Charlie]
    ```

---

### `reduce()`

* ***Combines stream elements into a single optional result using a binary operator (concatenates strings).***
    ```java
    Optional<String> concatenated = Stream.of("a", "b", "c")
                                         .reduce((s1, s2) -> s1 + s2);
    concatenated.ifPresent(s -> System.out.println("\nreduce (Optional): " + s));
    // Output: abc
    ```

* ***Combines stream elements using an identity value and a binary operator (sums integers).***
    ```java
    List<Integer> numbersReduce1 = Arrays.asList(1, 2, 3, 4, 5);
    int sumWithIdentity = numbersReduce1.stream()
                                       .reduce(0, Integer::sum); // Identity = 0
    System.out.println("reduce (Identity): " + sumWithIdentity);
    // Output: 15
    ```

* ***Combines stream elements using identity, accumulator, and combiner (calculates total length of names, parallel-safe).***
    ```java
    List<String> namesReduce = Arrays.asList("Alice", "Bob", "Charlie");
    int totalLength = namesReduce.stream()
                                 .reduce(0, // Identity
                                         (length, name) -> length + name.length(), // Accumulator
                                         Integer::sum); // Combiner
    System.out.println("reduce (parallel capable): " + totalLength);
    // Output: 15
    ```

---

### `collect()`

* ***Collects elements into a mutable container using supplier, accumulator, and combiner (creates an ArrayList).***
    ```java
    List<String> collectedManual = Stream.of("apple", "banana", "cherry")
                                        .collect(ArrayList::new, // Supplier
                                                 ArrayList::add,    // Accumulator
                                                 ArrayList::addAll); // Combiner
    System.out.println("\ncollect (manual): " + collectedManual);
    // Output: [apple, banana, cherry]
    ```

* ***Collects elements into a List using `Collectors.toList()`.***
    ```java
    List<String> namesCollectList = Arrays.asList("Alice", "Bob", "Charlie");
    List<String> nameList = namesCollectList.stream()
                                           .collect(Collectors.toList());
    System.out.println("collect (Collectors.toList): " + nameList);
    // Output: [Alice, Bob, Charlie]
    ```

* ***Collects elements into a Set using `Collectors.toSet()` (removes duplicates).***
    ```java
    List<String> namesCollectSet = Arrays.asList("Alice", "Bob", "Alice");
    Set<String> nameSet = namesCollectSet.stream()
                                        .collect(Collectors.toSet());
    System.out.println("collect (Collectors.toSet): " + nameSet);
    // Output: [Alice, Bob] (order may vary)
    ```

---

### `toList()` (Java 16+)

* ***Collects elements into an *unmodifiable* List.***
    ```java
    List<String> toListResult = Stream.of("apple", "banana", "cherry").toList();
    System.out.println("\ntoList(): " + toListResult);
    // Output: [apple, banana, cherry]
    // try { toListResult.add("date"); } catch(UnsupportedOperationException e) { System.out.println("List is unmodifiable"); }
    ```

---

### `min()`

* ***Finds the minimum element according to a comparator (shortest string).***
    ```java
    Optional<String> min = Stream.of("apple", "banana", "cherry")
                                .min(Comparator.comparingInt(String::length));
    min.ifPresent(s -> System.out.println("\nmin (by length): " + s));
    // Output: apple
    ```

---

### `max()`

* ***Finds the maximum element according to a comparator (longest string).***
    ```java
    Optional<String> max = Stream.of("apple", "banana", "cherry")
                                .max(Comparator.comparingInt(String::length));
    max.ifPresent(s -> System.out.println("max (by length): " + s));
    // Output: banana (or cherry, depending on internal stability)
    ```

---

### `count()`

* ***Counts the number of elements in the stream.***
    ```java
    long count = Stream.of("apple", "banana", "cherry").count();
    System.out.println("\ncount: " + count);
    // Output: 3
    ```

---

### `anyMatch()`

* ***Checks if at least one element matches the predicate.***
    ```java
    boolean hasApple = Stream.of("apple", "banana", "cherry")
                            .anyMatch(s -> s.equals("apple"));
    System.out.println("anyMatch ('apple'): " + hasApple);
    // Output: true
    ```

---

### `allMatch()`

* ***Checks if all elements match the predicate.***
    ```java
    boolean allStartWithA = Stream.of("apple", "apricot", "avocado")
                                 .allMatch(s -> s.startsWith("a"));
    System.out.println("allMatch ('a'): " + allStartWithA);
    // Output: true

    boolean allEndWithE = Stream.of("apple", "banana", "cherry")
                                 .allMatch(s -> s.endsWith("e"));
    System.out.println("allMatch ('e'): " + allEndWithE);
    // Output: false
    ```

---

### `noneMatch()`

* ***Checks if no elements match the predicate.***
    ```java
    boolean noneStartWithZ = Stream.of("apple", "banana", "cherry")
                                 .noneMatch(s -> s.startsWith("z"));
    System.out.println("noneMatch ('z'): " + noneStartWithZ);
    // Output: true
    ```

---

### `findFirst()`

* ***Finds the first element of the stream (if encounter order exists).***
    ```java
    Optional<String> first = Stream.of("apple", "banana", "cherry").findFirst();
    first.ifPresent(s -> System.out.println("\nfindFirst: " + s));
    // Output: apple
    ```

---

### `findAny()`

* ***Finds *any* element of the stream (useful for parallel streams where performance is key).***
    ```java
    Optional<String> any = Stream.of("apple", "banana", "cherry").parallel().findAny();
    any.ifPresent(s -> System.out.println("findAny (parallel): " + s));
    // Output: Could be apple, banana, or cherry
    ```

---

## 🔄 Mapping Objects Example (User to UserDTO)

* ***Transforms a list of `User` objects into a list of `UserDTO` objects using `map`.***
    ```java
    // Assume User and UserDTO classes exist as defined previously...
    List<User> users = List.of( /* ... users ... */ );

    System.out.println("\n--- DTO Mapping (Stream with map & collect) ---");
    List<UserDTO> usersDTO_stream = users.stream()
        .map(user -> new UserDTO(user.getId(), user.getUserName(), user.getEmail()))
        .collect(Collectors.toList());

    usersDTO_stream.forEach(System.out::println);
    ```

---

## 🔗 Chaining Operations Example

* ***Demonstrates chaining filter, map, and another filter before a terminal operation.***
    ```java
    List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5, 6);

    System.out.println("\n--- Chained Operations Example ---");
    intList.stream()
           .filter(a -> a % 2 == 0)     // Intermediate: [2, 4, 6]
           .map(a -> a + a)             // Intermediate: [4, 8, 12]
           .filter(a -> a > 5)          // Intermediate: [8, 12]
           .forEach(System.out::println); // Terminal: Prints 8, then 12
    ```

---

## ⏳ Laziness Example

* ***Illustrates that intermediate operations (`filter`, `map`) only execute when a terminal operation (`forEach`) is called.***
    ```java
    // Assume EmployeeLazy class exists as defined previously...
    List<EmployeeLazy> empListLazy = Arrays.asList( /* ... employees ... */ );

    System.out.println("\n--- Laziness Example ---");
    Stream<String> nameStream = empListLazy.stream()
        .filter(e -> { System.out.println("Filtering employee " + e.getId()); return e.getId() % 2 == 0; })
        .map(e -> { System.out.println("Mapping employee " + e.getName()); e.printName(); return e.getName(); });

    System.out.println("Pipeline defined. Adding terminal operation...");
    nameStream.forEach(name -> System.out.println("Final result: " + name)); // Execution happens now
    ```

---

## 🏢 Comprehensive Example: Employee Operations

This example demonstrates various stream operations on a list of `Employee` objects.

```java
import java.util.*;
import java.util.stream.Collectors;

// Employee class definition (as provided before) ...

public class EmployeeOperations {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            // ... employee data as provided before ...
            new Employee("Alice", 30, "Female", 50000, "New York", "IT", true),
            new Employee("Bob", 25, "Male", 60000, "San Francisco", "Finance", false),
            new Employee("Charlie", 35, "Male", 55000, "New York", "Marketing", true),
            new Employee("David", 40, "Male", 70000, "San Francisco", "IT", true),
            new Employee("Emma", 28, "Female", 75000, "Los Angeles", "HR", false),
            new Employee("Frank", 35, "Male", 55000, "New York", "IT", true)
        );

        System.out.println("--- Employee Operations ---");

        // 1. filter: Filters females
        System.out.println("\n1. Females:");
        employees.stream()
                 .filter(emp -> emp.getGender().equals("Female"))
                 .forEach(System.out::println); // Using forEach for direct output
        System.out.println("---");

        // 2. map: Get employee names
        System.out.println("\n2. Employee Names:");
        employees.stream()
                 .map(Employee::getName)
                 .forEach(System.out::println);
        System.out.println("---");

        // 3. flatMap & distinct: Get unique words from city names
        System.out.println("\n3. Unique Words in City Names:");
         employees.stream()
                  .map(Employee::getCity)
                  .flatMap(city -> Arrays.stream(city.split(" ")))
                  .distinct()
                  .forEach(System.out::println);
         System.out.println("---");

        // 4. map & distinct: Get unique department names
        System.out.println("\n4. Unique Department Names:");
        employees.stream()
                 .map(Employee::getDeptName)
                 .distinct()
                 .forEach(System.out::println);
        System.out.println("---");

        // 5. sorted: Sort employees by salary (ascending)
        System.out.println("\n5. Employees sorted by salary:");
        employees.stream()
                 .sorted(Comparator.comparingDouble(Employee::getSalary))
                 .forEach(System.out::println);
        System.out.println("---");

        // 6. sorted, reversed, limit: Get top 3 highest paid employees
        System.out.println("\n6. Top 3 highest paid employees:");
        employees.stream()
                 .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                 .limit(3)
                 .forEach(System.out::println);
        System.out.println("---");

        // 7. sorted, reversed, skip: Get employees *after* the top 3 highest paid
        System.out.println("\n7. Employees after top 3 highest paid:");
        employees.stream()
                 .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                 .skip(3)
                 .forEach(System.out::println);
        System.out.println("---");

        // 8. peek: Log names during filtering (Debugging example)
        System.out.println("\n8. Peeking at names while finding active IT employees:");
        List<Employee> activeITEmployees = employees.stream()
            .filter(emp -> emp.getDeptName().equals("IT"))
            .peek(emp -> System.out.println("Checking IT employee: " + emp.getName())) // Debug peek
            .filter(Employee::isActiveEmp)
            .collect(Collectors.toList()); // Collect results
        System.out.println("Active IT Employees found ("+ activeITEmployees.size() +"):");
        activeITEmployees.forEach(System.out::println);
        System.out.println("---");

        // Correct way to give a 10% raise (using map)
        System.out.println("\nGiving a 10% raise (using map - creates new objects):");
        List<Employee> employeesWithRaise = employees.stream()
            .map(emp -> new Employee(emp.getName(), emp.getAge(), emp.getGender(),
                                    emp.getSalary() * 1.10, emp.getCity(),
                                    emp.getDeptName(), emp.isActiveEmp()))
            .collect(Collectors.toList());
        employeesWithRaise.forEach(System.out::println);
    }
}
