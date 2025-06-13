# Java 8 Lambda Expressions - Complete Examples Guide

## Table of Contents
1. [Basic Lambda Expressions](#basic-lambda-expressions)
2. [Functional Interfaces](#functional-interfaces)
3. [Collection Operations](#collection-operations)
4. [Stream API with Lambdas](#stream-api-with-lambdas)
5. [Advanced Examples](#advanced-examples)

---

## Basic Lambda Expressions

### 1. Simple Runnable Example
```java
// Traditional approach
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello, World!");
    }
};

// Lambda approach
Runnable r2 = () -> System.out.println("Hello, Lambda!");
new Thread(r2).start();

// Even simpler - inline
new Thread(() -> System.out.println("Thread with Lambda!")).start();
```

### 2. Basic Mathematical Operations
```java
// Simple addition
BinaryOperator<Integer> add = (a, b) -> a + b;
System.out.println("5 + 3 = " + add.apply(5, 3)); // Output: 8

// Square operation  
UnaryOperator<Integer> square = x -> x * x;
System.out.println("5² = " + square.apply(5)); // Output: 25

// Multiplication
BinaryOperator<Integer> multiply = (a, b) -> a * b;
System.out.println("2 × 3 = " + multiply.apply(2, 3)); // Output: 6
```

---

## Functional Interfaces

### 3. Custom Functional Interface
```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);
}

public class LambdaExample {
    public static void main(String[] args) {
        Calculator add = (a, b) -> a + b;
        Calculator multiply = (a, b) -> a * b;
        Calculator subtract = (a, b) -> a - b;
        
        System.out.println("Addition: " + add.calculate(5, 3));        // 8
        System.out.println("Multiplication: " + multiply.calculate(5, 3)); // 15
        System.out.println("Subtraction: " + subtract.calculate(5, 3));    // 2
    }
}
```

### 4. Built-in Functional Interfaces

#### Predicate Examples
```java
// Check if number is even
Predicate<Integer> isEven = x -> x % 2 == 0;
System.out.println(isEven.test(4)); // true
System.out.println(isEven.test(5)); // false

// Check if string is empty
Predicate<String> isEmpty = s -> s.isEmpty();
System.out.println(isEmpty.test(""));     // true
System.out.println(isEmpty.test("Java")); // false

// Check if string starts with 'J'
Predicate<String> startsWithJ = s -> s.startsWith("J");
System.out.println(startsWithJ.test("Java"));   // true
System.out.println(startsWithJ.test("Python")); // false
```

#### Consumer Examples
```java
// Simple print consumer
Consumer<String> print = s -> System.out.println(s);
print.accept("Hello, World!"); // Output: Hello, World!

// Consumer with more logic
Consumer<String> printUpperCase = s -> System.out.println(s.toUpperCase());
printUpperCase.accept("java"); // Output: JAVA
```

#### Supplier Examples
```java
// Simple supplier
Supplier<String> supplier = () -> "Java 8";
System.out.println(supplier.get()); // Output: Java 8

// Random number supplier
Supplier<Integer> randomSupplier = () -> (int)(Math.random() * 100);
System.out.println("Random number: " + randomSupplier.get());
```

#### Function Examples
```java
// String length function
Function<String, Integer> length = s -> s.length();
System.out.println(length.apply("Lambda")); // Output: 6

// String to uppercase function
Function<String, String> toUpper = s -> s.toUpperCase();
System.out.println(toUpper.apply("java")); // Output: JAVA

// BiFunction example
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
System.out.println(add.apply(2, 3)); // Output: 5
```

---

## Collection Operations

### 5. List Iteration
```java
List<String> languages = Arrays.asList("Java", "Python", "JavaScript", "C++");

// Traditional for-each
for (String lang : languages) {
    System.out.println(lang);
}

// Lambda forEach
languages.forEach(lang -> System.out.println(lang));

// Method reference
languages.forEach(System.out::println);
```

### 6. List Sorting
```java
List<String> fruits = Arrays.asList("Banana", "Apple", "Orange", "Grapes");

// Sort alphabetically
fruits.sort((s1, s2) -> s1.compareTo(s2));
fruits.forEach(System.out::println);

// Sort by length
fruits.sort(Comparator.comparingInt(String::length));
fruits.forEach(System.out::println);

// Reverse alphabetical order
fruits.sort((s1, s2) -> s2.compareTo(s1));
fruits.forEach(System.out::println);
```

### 7. Map Operations
```java
Map<String, Integer> techVersions = new HashMap<>();
techVersions.put("Java", 8);
techVersions.put("Spring", 5);
techVersions.put("Hibernate", 6);

// Iterate through map
techVersions.forEach((key, value) -> 
    System.out.println(key + ": " + value));

// Filter map entries
techVersions.entrySet().stream()
    .filter(entry -> entry.getValue() > 5)
    .forEach(entry -> System.out.println(entry.getKey() + " = " + entry.getValue()));
```

---

## Stream API with Lambdas

### 8. Filtering Examples
```java
List<String> languages = Arrays.asList("Java", "JavaScript", "Python", "C++", "C#");

// Filter languages starting with 'J'
List<String> jLanguages = languages.stream()
    .filter(s -> s.startsWith("J"))
    .collect(Collectors.toList());
jLanguages.forEach(System.out::println); // Java, JavaScript

// Filter by length
List<String> shortNames = languages.stream()
    .filter(s -> s.length() <= 4)
    .collect(Collectors.toList());
shortNames.forEach(System.out::println); // Java, C++
```

### 9. Mapping Examples
```java
List<String> words = Arrays.asList("java", "spring", "lambda", "stream");

// Convert to uppercase
List<String> upperWords = words.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
upperWords.forEach(System.out::println);

// Get word lengths
List<Integer> lengths = words.stream()
    .map(String::length)
    .collect(Collectors.toList());
lengths.forEach(System.out::println);
```

### 10. Numeric Stream Operations
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Filter even numbers
List<Integer> evenNumbers = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
evenNumbers.forEach(System.out::println); // 2, 4, 6, 8, 10

// Square all numbers
List<Integer> squares = numbers.stream()
    .map(n -> n * n)
    .collect(Collectors.toList());
squares.forEach(System.out::println);

// Sum using reduce
int sum = numbers.stream()
    .reduce(0, (a, b) -> a + b);
System.out.println("Sum: " + sum); // Sum: 55

// Alternative sum using method reference
int sum2 = numbers.stream()
    .reduce(0, Integer::sum);
System.out.println("Sum: " + sum2); // Sum: 55
```

### 11. Stream Utility Operations
```java
List<Integer> duplicateNumbers = Arrays.asList(1, 2, 2, 3, 4, 4, 5, 5);

// Remove duplicates
duplicateNumbers.stream()
    .distinct()
    .forEach(System.out::println); // 1, 2, 3, 4, 5

// Count elements
long count = duplicateNumbers.stream()
    .distinct()
    .count();
System.out.println("Unique count: " + count); // 5

// Sort and display
duplicateNumbers.stream()
    .distinct()
    .sorted()
    .forEach(System.out::println);
```

### 12. Stream Matching Operations
```java
List<String> technologies = Arrays.asList("Java", "Spring", "Hibernate", "Maven");

// Check if any element matches
boolean containsJava = technologies.stream()
    .anyMatch(s -> s.equals("Java"));
System.out.println("Contains 'Java': " + containsJava); // true

// Check if all elements match a condition
boolean allLongerThan2 = technologies.stream()
    .allMatch(s -> s.length() > 2);
System.out.println("All longer than 2 chars: " + allLongerThan2); // true

// Check if none match
boolean noneStartsWithZ = technologies.stream()
    .noneMatch(s -> s.startsWith("Z"));
System.out.println("None starts with 'Z': " + noneStartsWithZ); // true
```

---

## Advanced Examples

### 13. Optional with Lambda
```java
// Creating and using Optional
Optional<String> optional = Optional.of("Java");
optional.ifPresent(s -> System.out.println("Value is present: " + s));

// Optional with filtering
Optional<String> filtered = optional.filter(s -> s.startsWith("J"));
filtered.ifPresent(System.out::println);

// Optional with mapping
Optional<Integer> length = optional.map(String::length);
length.ifPresent(l -> System.out.println("Length: " + l));
```

### 14. Complex Stream Pipeline
```java
List<Person> people = Arrays.asList(
    new Person("Alice", 25, "Engineer"),
    new Person("Bob", 30, "Manager"),
    new Person("Charlie", 35, "Engineer"),
    new Person("Diana", 28, "Designer")
);

// Complex filtering and mapping
List<String> engineerNames = people.stream()
    .filter(p -> p.getJob().equals("Engineer"))
    .filter(p -> p.getAge() > 20)
    .map(Person::getName)
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());

engineerNames.forEach(System.out::println);
```

### 15. Grouping and Collecting
```java
List<Student> students = Arrays.asList(
    new Student("John", "Math", 85),
    new Student("Jane", "Science", 92),
    new Student("Bob", "Math", 78),
    new Student("Alice", "Science", 88)
);

// Group by subject
Map<String, List<Student>> bySubject = students.stream()
    .collect(Collectors.groupingBy(Student::getSubject));

bySubject.forEach((subject, studentList) -> {
    System.out.println(subject + ": " + studentList.size() + " students");
});

// Average score by subject
Map<String, Double> avgScoreBySubject = students.stream()
    .collect(Collectors.groupingBy(
        Student::getSubject,
        Collectors.averagingDouble(Student::getScore)
    ));

avgScoreBySubject.forEach((subject, avg) -> 
    System.out.println(subject + " average: " + avg));
```

### 16. Parallel Stream Processing
```java
List<Integer> largeList = IntStream.rangeClosed(1, 1000000)
    .boxed()
    .collect(Collectors.toList());

// Sequential processing
long start = System.currentTimeMillis();
long sum1 = largeList.stream()
    .mapToLong(Integer::longValue)
    .sum();
long end = System.currentTimeMillis();
System.out.println("Sequential time: " + (end - start) + "ms");

// Parallel processing
start = System.currentTimeMillis();
long sum2 = largeList.parallelStream()
    .mapToLong(Integer::longValue)
    .sum();
end = System.currentTimeMillis();
System.out.println("Parallel time: " + (end - start) + "ms");
```

---

## Key Benefits of Lambda Expressions

1. **Concise Code**: Reduces boilerplate code significantly
2. **Functional Programming**: Enables functional programming paradigms in Java
3. **Better Collections**: Enhanced collection processing with Stream API
4. **Parallel Processing**: Easy parallel processing capabilities
5. **Method References**: Clean and readable method references

## Common Lambda Syntax Patterns

```java
// No parameters
() -> System.out.println("Hello")

// One parameter (parentheses optional)
x -> x * 2
(x) -> x * 2

// Multiple parameters
(x, y) -> x + y

// Block body
(x, y) -> {
    int result = x + y;
    return result;
}

// Method reference
String::toUpperCase
System.out::println
```

---

## More Stream Operations

### 17. Stream Matching Operations (Extended)
```java
List<Integer> numbers = Arrays.asList(2, 4, 6, 8, 10);

// Check if all numbers are even
boolean allEven = numbers.stream()
    .allMatch(n -> n % 2 == 0);
System.out.println("All even: " + allEven); // true

// Check if all numbers are greater than 5
boolean allGreaterThan5 = numbers.stream()
    .allMatch(n -> n > 5);
System.out.println("All greater than 5: " + allGreaterThan5); // false

List<String> languages = Arrays.asList("Java", "Spring", "Lambda");

// Check if none contains "Python"
boolean nonePython = languages.stream()
    .noneMatch(s -> s.equals("Python"));
System.out.println("Contains no 'Python': " + nonePython); // true

// Check if none starts with 'X'
boolean noneStartsWithX = languages.stream()
    .noneMatch(s -> s.startsWith("X"));
System.out.println("None starts with 'X': " + noneStartsWithX); // true
```

### 18. Finding Elements
```java
List<String> technologies = Arrays.asList("Java", "Spring", "Lambda", "Kafka");

// Find first element
Optional<String> first = technologies.stream()
    .findFirst();
first.ifPresent(System.out::println); // Java

// Find first element matching condition
Optional<String> firstStartingWithS = technologies.stream()
    .filter(s -> s.startsWith("S"))
    .findFirst();
firstStartingWithS.ifPresent(System.out::println); // Spring

// Find any element (useful in parallel streams)
Optional<String> any = technologies.stream()
    .findAny();
any.ifPresent(System.out::println);

// Find first element longer than 5 characters
Optional<String> longName = technologies.stream()
    .filter(s -> s.length() > 5)
    .findFirst();
longName.ifPresent(name -> System.out.println("Long name: " + name));
```

### 19. Numeric Stream Operations
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Sum using mapToInt
int sum = numbers.stream()
    .mapToInt(Integer::intValue)
    .sum();
System.out.println("Sum: " + sum); // Sum: 55

// Find maximum
int max = numbers.stream()
    .mapToInt(Integer::intValue)
    .max()
    .orElse(Integer.MIN_VALUE);
System.out.println("Max: " + max); // Max: 10

// Find minimum
int min = numbers.stream()
    .mapToInt(Integer::intValue)
    .min()
    .orElse(Integer.MAX_VALUE);
System.out.println("Min: " + min); // Min: 1

// Calculate average
double average = numbers.stream()
    .mapToInt(Integer::intValue)
    .average()
    .orElse(0.0);
System.out.println("Average: " + average); // Average: 5.5
```

### 20. String Operations
```java
List<String> words = Arrays.asList("Java", "Spring", "Lambda", "Kafka");

// Convert to lengths
words.stream()
    .mapToInt(String::length)
    .forEach(System.out::println); // 4, 6, 6, 5

// Join with comma
String joined = words.stream()
    .collect(Collectors.joining(", "));
System.out.println("Joined: " + joined); // Java, Spring, Lambda, Kafka

// Join without delimiter
String joinedNoDelimiter = words.stream()
    .collect(Collectors.joining());
System.out.println("No delimiter: " + joinedNoDelimiter); // JavaSpringLambdaKafka

// Join with prefix and suffix
String joinedWithBrackets = words.stream()
    .collect(Collectors.joining(", ", "[", "]"));
System.out.println("With brackets: " + joinedWithBrackets); // [Java, Spring, Lambda, Kafka]
```

### 21. Advanced Collectors
```java
List<String> items = Arrays.asList("Java", "Spring", "Lambda", "Java", "Spring");

// Group by frequency
Map<String, Long> frequency = items.stream()
    .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
frequency.forEach((k, v) -> System.out.println(k + ": " + v));
// Output: Java: 2, Spring: 2, Lambda: 1

// Collect to Set (removes duplicates)
Set<String> uniqueItems = items.stream()
    .collect(Collectors.toSet());
uniqueItems.forEach(System.out::println); // Java, Spring, Lambda

// Create map from list
List<String> technologies = Arrays.asList("Java", "Spring", "Lambda");
Map<String, Integer> techLengths = technologies.stream()
    .collect(Collectors.toMap(s -> s, String::length));
techLengths.forEach((k, v) -> System.out.println(k + ": " + v));
// Java: 4, Spring: 6, Lambda: 6

// Group by string length
Map<Integer, List<String>> groupedByLength = technologies.stream()
    .collect(Collectors.groupingBy(String::length));
groupedByLength.forEach((k, v) -> System.out.println(k + ": " + v));
// 4: [Java], 6: [Spring, Lambda]
```

### 22. Partitioning
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Partition by even/odd
Map<Boolean, List<Integer>> partitioned = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
partitioned.forEach((isEven, list) -> 
    System.out.println((isEven ? "Even" : "Odd") + ": " + list));
// Even: [2, 4, 6, 8, 10], Odd: [1, 3, 5, 7, 9]

// Partition strings by length
List<String> words = Arrays.asList("Java", "Go", "Python", "C", "JavaScript");
Map<Boolean, List<String>> partitionedWords = words.stream()
    .collect(Collectors.partitioningBy(s -> s.length() > 4));
partitionedWords.forEach((isLong, list) -> 
    System.out.println((isLong ? "Long" : "Short") + " words: " + list));
```

### 23. Statistical Operations
```java
List<Integer> scores = Arrays.asList(85, 92, 78, 96, 88, 73, 91, 87);

// Get comprehensive statistics
IntSummaryStatistics stats = scores.stream()
    .collect(Collectors.summarizingInt(Integer::intValue));

System.out.println("Count: " + stats.getCount());     // 8
System.out.println("Sum: " + stats.getSum());         // 690
System.out.println("Min: " + stats.getMin());         // 73
System.out.println("Max: " + stats.getMax());         // 96
System.out.println("Average: " + stats.getAverage()); // 86.25

// Alternative counting
long count = scores.stream()
    .collect(Collectors.counting());
System.out.println("Count using counting(): " + count); // 8
```

### 24. Stream Limiting and Skipping
```java
Stream<String> technologies = Stream.of("Java", "Spring", "Lambda", "Kafka", "Docker", "Kubernetes");

// Take first 3 elements
System.out.println("First 3:");
technologies.limit(3)
    .forEach(System.out::println); // Java, Spring, Lambda

// Skip first 2 elements
System.out.println("Skip first 2:");
Stream.of("Java", "Spring", "Lambda", "Kafka", "Docker", "Kubernetes")
    .skip(2)
    .forEach(System.out::println); // Lambda, Kafka, Docker, Kubernetes

// Combine skip and limit
System.out.println("Skip 2, take 3:");
Stream.of("Java", "Spring", "Lambda", "Kafka", "Docker", "Kubernetes")
    .skip(2)
    .limit(3)
    .forEach(System.out::println); // Lambda, Kafka, Docker
```

### 25. Stream Peek (Debugging)
```java
List<String> words = Arrays.asList("java", "spring", "lambda");

// Use peek for debugging
List<String> result = words.stream()
    .peek(s -> System.out.println("Original: " + s))
    .map(String::toUpperCase)
    .peek(s -> System.out.println("Uppercase: " + s))
    .filter(s -> s.length() > 4)
    .peek(s -> System.out.println("Filtered: " + s))
    .collect(Collectors.toList());

System.out.println("Final result: " + result);
```

### 26. Creating Streams
```java
// From array
String[] array = {"Java", "Spring", "Lambda"};
Stream<String> streamFromArray = Arrays.stream(array);
streamFromArray.forEach(System.out::println);

// From individual elements
Stream<String> streamFromElements = Stream.of("Java", "Spring", "Lambda");
streamFromElements.forEach(System.out::println);

// From range
IntStream.range(1, 6)
    .forEach(System.out::println); // 1, 2, 3, 4, 5

// From range (inclusive)
IntStream.rangeClosed(1, 5)
    .forEach(System.out::println); // 1, 2, 3, 4, 5

// Generate infinite stream
Stream.generate(() -> "Hello")
    .limit(3)
    .forEach(System.out::println); // Hello, Hello, Hello

// Iterate to create sequence
Stream.iterate(0, n -> n + 2)
    .limit(5)
    .forEach(System.out::println); // 0, 2, 4, 6, 8
```

### 27. Optional Operations (Extended)
```java
// Basic Optional usage
Optional<String> optional = Optional.of("Java");
optional.ifPresent(System.out::println); // Java

// Optional with null safety
Optional<String> emptyOptional = Optional.ofNullable(null);
emptyOptional.ifPresent(System.out::println); // Nothing printed

// Optional with default value
String value = emptyOptional.orElse("Default Value");
System.out.println(value); // Default Value

// Optional with supplier for default
String suppliedValue = emptyOptional.orElseGet(() -> "Supplied Default");
System.out.println(suppliedValue); // Supplied Default

// Optional chaining
Optional<String> result = Optional.of("java")
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase);
result.ifPresent(System.out::println); // JAVA

// Optional with exception
try {
    String mandatoryValue = emptyOptional.orElseThrow(() -> 
        new IllegalStateException("Value is required"));
} catch (IllegalStateException e) {
    System.out.println("Exception: " + e.getMessage());
}
```

### 28. Parallel Stream Processing
```java
List<Integer> largeNumbers = IntStream.rangeClosed(1, 1000000)
    .boxed()
    .collect(Collectors.toList());

// Sequential processing
long startTime = System.currentTimeMillis();
long sequentialSum = largeNumbers.stream()
    .filter(n -> n % 2 == 0)
    .mapToLong(Integer::longValue)
    .sum();
long sequentialTime = System.currentTimeMillis() - startTime;

// Parallel processing
startTime = System.currentTimeMillis();
long parallelSum = largeNumbers.parallelStream()
    .filter(n -> n % 2 == 0)
    .mapToLong(Integer::longValue)
    .sum();
long parallelTime = System.currentTimeMillis() - startTime;

System.out.println("Sequential sum: " + sequentialSum + " (Time: " + sequentialTime + "ms)");
System.out.println("Parallel sum: " + parallelSum + " (Time: " + parallelTime + "ms)");
```

### 29. Complex Real-World Examples
```java
// Employee management example
class Employee {
    private String name;
    private String department;
    private double salary;
    private int age;
    
    // Constructor and getters
    public Employee(String name, String department, double salary, int age) {
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.age = age;
    }
    
    // Getters...
}

List<Employee> employees = Arrays.asList(
    new Employee("Alice", "IT", 70000, 28),
    new Employee("Bob", "HR", 55000, 35),
    new Employee("Charlie", "IT", 80000, 32),
    new Employee("Diana", "Finance", 65000, 29),
    new Employee("Eve", "IT", 75000, 26)
);

// Find highest paid employee in IT
Optional<Employee> highestPaidIT = employees.stream()
    .filter(emp -> emp.getDepartment().equals("IT"))
    .max(Comparator.comparingDouble(Employee::getSalary));
highestPaidIT.ifPresent(emp -> 
    System.out.println("Highest paid IT: " + emp.getName() + " - $" + emp.getSalary()));

// Average salary by department
Map<String, Double> avgSalaryByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.averagingDouble(Employee::getSalary)
    ));
avgSalaryByDept.forEach((dept, avg) -> 
    System.out.println(dept + " average salary: $" + String.format("%.2f", avg)));

// Employees earning above average
double overallAverage = employees.stream()
    .mapToDouble(Employee::getSalary)
    .average()
    .orElse(0.0);

List<String> aboveAverageEmployees = employees.stream()
    .filter(emp -> emp.getSalary() > overallAverage)
    .map(Employee::getName)
    .sorted()
    .collect(Collectors.toList());

System.out.println("Employees earning above average ($" + 
    String.format("%.2f", overallAverage) + "): " + aboveAverageEmployees);
```

This guide covers the most common and useful lambda expression patterns in Java 8. Practice these examples to master functional programming concepts in Java!