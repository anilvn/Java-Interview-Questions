# Java 8 Functional Programming Guide - Part 2


## Table of Contents

| #  | Topic                                                       |
|----|-------------------------------------------------------------|
| **Built-in Functional Interfaces** |                             |
| 1 | [Function Interface](#function-interface) |
| 2 | [Consumer Interface](#consumer-interface) |
| 3 | [Predicate Interface](#predicate-interface) |
| 4 | [Supplier Interface](#supplier-interface) |
| 5 | [BiFunction Interface](#bifunction-interface) |
| 6 | [BiConsumer Interface](#biconsumer-interface) |
| 7 | [BiPredicate Interface](#bipredicate-interface) |

## Function Interface

The `Function<T, R>` interface represents a function that accepts one argument and produces a result.

### Key Characteristics:

* Takes one input of type T
* Returns an output of type R
* Has the functional method `R apply(T t)`
* Can be composed with other functions

### Example: Basic Function Interface Usage

```java
import java.util.function.Function;

class FunctionImpl implements Function<String, Integer> {
    @Override
    public Integer apply(String s) {
        return s.length();
    }
}

public class FunctionDemo {
    public static void main(String[] args) {
        // Using a class that implements Function
        Function<String, Integer> lengthFunction1 = new FunctionImpl();
        System.out.println(lengthFunction1.apply("Hello"));  // Output: 5
        
        // Using lambda expression
        Function<String, Integer> lengthFunction2 = s -> s.length();
        System.out.println(lengthFunction2.apply("World"));  // Output: 5
        
        // Function composition with andThen
        Function<Integer, Integer> multiplyByTwo = x -> x * 2;
        Function<String, Integer> lengthAndMultiply = lengthFunction2.andThen(multiplyByTwo);
        System.out.println(lengthAndMultiply.apply("Hello"));  // Output: 10 (5 * 2)
        
        // Function composition with compose
        Function<String, String> toUpperCase = s -> s.toUpperCase();
        Function<String, Integer> upperCaseLength = lengthFunction2.compose(toUpperCase);
        System.out.println(upperCaseLength.apply("Hello"));  // Output: 5
    }
}
```

### Example: Basic Function Interface with Method Reference Usage

```java
import java.util.function.Function;

public class MethodRefExamples {

    public static void main(String[] args) {
        // 1. Instance method of a particular object
        String prefix = "Hello ";
        Function<String, String> addPrefix = prefix::concat;  // s -> prefix.concat(s)
        System.out.println(addPrefix.apply("World"));  // Hello World

        // 2. Static method reference
        Function<String, Integer> parseInt = Integer::parseInt; // parses String to Integer
        System.out.println(parseInt.apply("123"));  // 123

        // 3. Instance method of parameter type
        Function<String, String> toUpperCase = String::toUpperCase;  // converts to uppercase
        System.out.println(toUpperCase.apply("java"));  // JAVA

        // 4. Composing functions: first toUpperCase, then length
        Function<String, Integer> length = String::length;  // length of string

        // compose: lengthFunction.compose(toUpperCase) means toUpperCase runs first, then length
        Function<String, Integer> upperCaseLength = length.compose(toUpperCase);
        System.out.println(upperCaseLength.apply("hello"));  // 5

        // 5. andThen: toUpperCase.andThen(length) means toUpperCase runs first, then length
        Function<String, Integer> upperCaseLength2 = toUpperCase.andThen(length);
        System.out.println(upperCaseLength2.apply("world"));  // 5
    }
}
```
[Back to Top](#table-of-contents)

## Consumer Interface

The `Consumer<T>` interface represents an operation that accepts a single input argument and returns no result.

### Key Characteristics:

* Takes one input of type T
* Returns no result (void)
* Has the functional method `void accept(T t)`
* Can be chained using `andThen()`

### Example: Consumer Interface Usage

```java
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.List;

class ConsumerImpl implements Consumer<String> {
    @Override
    public void accept(String input) {
        System.out.println(input);
    }
}

public class ConsumerDemo {
    public static void main(String[] args) {
        // Using a class that implements Consumer
        Consumer<String> consumer1 = new ConsumerImpl();
        consumer1.accept("Hello Consumer");  // Output: Hello Consumer
        
        // Using lambda expression
        Consumer<String> consumer2 = input -> System.out.println("Processing: " + input);
        consumer2.accept("Test");  // Output: Processing: Test
        
        // Consumer chaining with andThen
        Consumer<String> consumerChain = consumer1.andThen(consumer2);
        consumerChain.accept("Chained");  
        /*
           Output:
           Chained
           Processing: Chained
        */
        
        // Using Consumer with Collection
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        
        // forEach accepts a Consumer
        names.forEach(name -> System.out.println("Name: " + name));
        /*
           Output:
           Name: Alice
           Name: Bob
           Name: Charlie
        */
    }
}
```
### Example: Basic Consumer Interface with Method Reference Usage
```java
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.List;

public class ConsumerMethodRefExamples {

    public static void main(String[] args) {
        // 1. Consumer using instance method of a particular object
        String prefix = "Output: ";
        Consumer<String> printWithPrefix = prefix::concatAndPrint; 
        // Note: String class doesn't have concatAndPrint, so let's define our own object below

        Printer printer = new Printer();
        Consumer<String> printerConsumer = printer::print;  // instance method of Printer object
        printerConsumer.accept("Hello World!");  // prints: Printer: Hello World!

        // 2. Consumer using static method reference
        Consumer<String> sysOut = System.out::println;  // static method println(String) of PrintStream
        sysOut.accept("Printing with System.out.println");

        // 3. Consumer using instance method of parameter type
        Consumer<String> toLowerCasePrinter = s -> System.out.println(s.toLowerCase());
        toLowerCasePrinter.accept("LOWER CASE EXAMPLE");

        // Using method reference for the same: (no direct method reference here for println(toLowerCase))
        // But if you want, you can split steps:
        Consumer<String> print = System.out::println;
        Function<String, String> toLower = String::toLowerCase;
        // Combine manually:
        Consumer<String> printLower = s -> print.accept(toLower.apply(s));
        printLower.accept("Mixed Case Text");
    }

    // Helper class for example 1
    static class Printer {
        public void print(String s) {
            System.out.println("Printer: " + s);
        }
    }
}
```

[Back to Top](#table-of-contents)

## Predicate Interface

The `Predicate<T>` interface represents a predicate (boolean-valued function) of one argument.

### Key Characteristics:

* Takes one input of type T
* Returns boolean result
* Has the functional method `boolean test(T t)`
* Can be combined using logical operations: `and()`, `or()`, `negate()`

### Example: Predicate Interface Usage

```java
import java.util.function.Predicate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    @Override
    public String toString() {
        return name + " (" + age + ")";
    }
}

public class PredicateDemo {
    public static void main(String[] args) {
        // Simple predicate
        Predicate<Integer> isPositive = num -> num > 0;
        System.out.println(isPositive.test(5));   // Output: true
        System.out.println(isPositive.test(-5));  // Output: false
        
        // Predicate with complex object
        Predicate<Person> isAdult = person -> person.getAge() >= 18;
        Person person1 = new Person("Alice", 20);
        Person person2 = new Person("Bob", 16);
        
        System.out.println(isAdult.test(person1));  // Output: true
        System.out.println(isAdult.test(person2));  // Output: false
        
        // Combining predicates
        Predicate<Person> nameStartsWithA = person -> person.getName().startsWith("A");
        
        // AND operation
        Predicate<Person> isAdultAndNameStartsWithA = isAdult.and(nameStartsWithA);
        System.out.println(isAdultAndNameStartsWithA.test(person1));  // Output: true
        
        // OR operation
        Predicate<Person> isAdultOrNameStartsWithA = isAdult.or(nameStartsWithA);
        System.out.println(isAdultOrNameStartsWithA.test(person2));  // Output: false
        
        // NEGATE operation
        Predicate<Person> isNotAdult = isAdult.negate();
        System.out.println(isNotAdult.test(person2));  // Output: true
        
        // Using with streams
        List<Person> persons = Arrays.asList(
            new Person("Alice", 20),
            new Person("Bob", 16),
            new Person("Charlie", 25),
            new Person("Anna", 17)
        );
        
        List<Person> adults = persons.stream()
            .filter(isAdult)
            .collect(Collectors.toList());
            
        System.out.println("Adults: " + adults);
        // Output: Adults: [Alice (20), Charlie (25)]
    }
}
```

[Back to Top](#table-of-contents)


## Supplier Interface

The `Supplier<T>` functional interface represents a supplier of results without taking any input arguments. It is defined in the `java.util.function` package.

Key characteristics:
- Takes no parameters
- Returns a result of type T
- Contains a single abstract method `get()`
- Often used in lazy evaluation and factory patterns

```java
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
```

### Basic Supplier Example

*A simple example showing how to implement and use a Supplier:*

```java
import java.util.function.Supplier;

public class SupplierDemo {
    public static void main(String[] args) {
        // Using a lambda expression
        Supplier<String> messageSupplier = () -> "Hello, World!";
        System.out.println(messageSupplier.get()); // Output: Hello, World!
        
        // Using traditional implementation
        Supplier<String> traditionalSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello from traditional implementation!";
            }
        };
        System.out.println(traditionalSupplier.get()); // Output: Hello from traditional implementation!
    }
}
```

### Supplier with Objects

*Example showing how to create a Supplier that returns a new object:*

```java
import java.util.function.Supplier;

class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    
    @Override
    public String toString() {
        return "Person: " + name + ", " + age;
    }
}

public class PersonSupplierDemo {
    public static void main(String[] args) {
        // Supplier that creates a new Person object
        Supplier<Person> personSupplier = () -> new Person("Ramesh", 30);
        
        Person p = personSupplier.get();
        System.out.println("Person Detail: " + p.getName() + ", " + p.getAge());
        // Output: Person Detail: Ramesh, 30
    }
}
```

### Supplier with Collections

*Using a Supplier to generate a list of products:*

```java
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

class Product {
    private int id;
    private String name;
    private float price;
    
    public Product(int id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    
    // Getters and toString() method
    // ...
}

public class CollectionSupplierDemo {
    public static void main(String[] args) {
        Supplier<List<Product>> productListSupplier = productSupplier();
        List<Product> productList = productListSupplier.get();
        
        // Process the product list
        productList.forEach(System.out::println);
    }
    
    private static Supplier<List<Product>> productSupplier() {
        return () -> {
            List<Product> productsList = new ArrayList<>();
            productsList.add(new Product(1, "HP Laptop", 25000f));
            productsList.add(new Product(2, "Dell Laptop", 30000f));
            productsList.add(new Product(3, "Lenovo Laptop", 28000f));
            productsList.add(new Product(4, "Sony Laptop", 28000f));
            productsList.add(new Product(5, "Apple Laptop", 90000f));
            return productsList;
        };
    }
}
```

### Real-world Use Cases for Supplier

- **Lazy initialization**: Create objects only when needed
- **Factory patterns**: Generate new instances with specific configurations
- **Default values**: Provide fallback values when data is missing
- **Caching**: Compute values once and reuse them
- **Testing**: Generate test data on demand

[Back to Top](#table-of-contents)

---

## BiFunction Interface

The `BiFunction<T, U, R>` functional interface represents a function that accepts two arguments and produces a result. It is defined in the `java.util.function` package.

Key characteristics:
- Takes two parameters of types T and U
- Returns a result of type R
- Contains a single abstract method `apply(T t, U u)`
- Useful for operations requiring two inputs

```java
@FunctionalInterface
public interface BiFunction<T, U, R> {
    R apply(T t, U u);
}
```

### Basic BiFunction Example

*A simple example demonstrating addition and multiplication using BiFunction:*

```java
import java.util.function.BiFunction;

public class BiFunctionDemo {
    public static void main(String[] args) {
        // Addition BiFunction
        BiFunction<Integer, Integer, Integer> addition = (a, b) -> a + b;
        System.out.println("10 + 20 = " + addition.apply(10, 20));  // Output: 10 + 20 = 30
        
        // Multiplication BiFunction
        BiFunction<Integer, Integer, Integer> multiplication = (a, b) -> a * b;
        System.out.println("10 * 20 = " + multiplication.apply(10, 20));  // Output: 10 * 20 = 200
    }
}
```

### Using andThen() with BiFunction

*Example showing how to compose a BiFunction with a Function:*

```java
import java.util.function.BiFunction;
import java.util.function.Function;

public class BiFunctionCompositionDemo {
    public static void main(String[] args) {
        // BiFunction to add two numbers
        BiFunction<Integer, Integer, Integer> addition = (a, b) -> a + b;
        
        // Function to square a number
        Function<Integer, Integer> square = number -> number * number;
        
        // First add two numbers, then square the result
        Integer result = addition.andThen(square).apply(10, 20);
        System.out.println("(10 + 20)² = " + result);  // Output: (10 + 20)² = 900
    }
}
```

### Traditional Implementation

*Example showing traditional implementation vs lambda expression:*

```java
import java.util.function.BiFunction;

public class BiFunctionTraditionalDemo implements BiFunction<Integer, Integer, Integer> {
    // Traditional way of implementation
    @Override
    public Integer apply(Integer t, Integer u) {
        return t + u;
    }
    
    public static void main(String[] args) {
        // Using the traditional implementation
        BiFunction<Integer, Integer, Integer> traditional = new BiFunctionTraditionalDemo();
        System.out.println("Traditional: " + traditional.apply(10, 20));  // Output: Traditional: 30
        
        // Using lambda expressions
        BiFunction<Integer, Integer, Integer> lambdaPlus = (t, u) -> t + u;
        BiFunction<Integer, Integer, Integer> lambdaMultiply = (t, u) -> t * u;
        
        System.out.println("Lambda addition: " + lambdaPlus.apply(10, 20));  // Output: Lambda addition: 30
        System.out.println("Lambda multiplication: " + lambdaMultiply.apply(10, 20));  // Output: Lambda multiplication: 200
    }
}
```

### Real-world Use Cases for BiFunction

- **Mathematical operations**: Perform calculations with two inputs
- **String manipulation**: Combine or process two strings
- **Object merging**: Combine two objects into a new object
- **Data transformation**: Convert data from two sources into a new format
- **Key-value processing**: Process entries in a map

[Back to Top](#table-of-contents)

---

## BiConsumer Interface

The `BiConsumer<T, U>` functional interface represents an operation that takes two input arguments and returns no result. It is defined in the `java.util.function` package.

Key characteristics:
- Takes two parameters of types T and U
- Returns no result (void)
- Contains a single abstract method `accept(T t, U u)`
- Useful for operations that process two inputs but don't return a value

```java
@FunctionalInterface
public interface BiConsumer<T, U> {
    void accept(T t, U u);
}
```

### Basic BiConsumer Example

*A simple example showing addition and subtraction operations using BiConsumer:*

```java
import java.util.function.BiConsumer;

public class BiConsumerDemo {
    public static void main(String[] args) {
        // BiConsumer for addition
        BiConsumer<Integer, Integer> addition = (a, b) -> 
            System.out.println("Addition: " + a + " + " + b + " = " + (a + b));
        
        // BiConsumer for subtraction
        BiConsumer<Integer, Integer> subtraction = (a, b) -> 
            System.out.println("Subtraction: " + a + " - " + b + " = " + (a - b));
        
        // Using the BiConsumers
        addition.accept(10, 20);    // Output: Addition: 10 + 20 = 30
        subtraction.accept(20, 10); // Output: Subtraction: 20 - 10 = 10
    }
}
```

### Traditional vs Lambda Implementation

*Example showing traditional implementation compared to lambda expression:*

```java
import java.util.function.BiConsumer;

public class BiConsumerTraditionalDemo {
    public static void main(String[] args) {
        // Traditional way using anonymous inner class
        BiConsumer<Integer, Integer> traditional = new BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer a, Integer b) {
                System.out.println("Traditional: " + a + " + " + b + " = " + (a + b));
            }
        };
        
        // Using lambda expression
        BiConsumer<Integer, Integer> lambda = (a, b) -> 
            System.out.println("Lambda: " + a + " + " + b + " = " + (a + b));
            
        // Using the implementations
        traditional.accept(10, 20); // Output: Traditional: 10 + 20 = 30
        lambda.accept(10, 20);      // Output: Lambda: 10 + 20 = 30
    }
}
```

### Using BiConsumer with Map

*Example demonstrating how to use BiConsumer with a Map:*

```java
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class BiConsumerMapDemo {
    public static void main(String[] args) {
        // Create a Map
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 85);
        scores.put("Charlie", 90);
        
        // BiConsumer to process map entries
        BiConsumer<String, Integer> printEntry = (name, score) -> 
            System.out.println(name + " scored " + score + " points");
        
        // Process map entries using BiConsumer
        scores.forEach(printEntry);
        
        /* Output:
         * Bob scored 85 points
         * Alice scored 95 points
         * Charlie scored 90 points
         */
    }
}
```

### Using andThen() Method

*Example showing how to chain BiConsumer operations:*

```java
import java.util.function.BiConsumer;

public class BiConsumerAndThenDemo {
    public static void main(String[] args) {
        // First BiConsumer - adds two numbers and prints result
        BiConsumer<Integer, Integer> addAndPrint = (a, b) -> 
            System.out.println("Addition: " + a + " + " + b + " = " + (a + b));
        
        // Second BiConsumer - multiplies two numbers and prints result
        BiConsumer<Integer, Integer> multiplyAndPrint = (a, b) -> 
            System.out.println("Multiplication: " + a + " * " + b + " = " + (a * b));
        
        // Chain the BiConsumers
        BiConsumer<Integer, Integer> combined = addAndPrint.andThen(multiplyAndPrint);
        
        // Use the combined BiConsumer
        combined.accept(10, 20);
        
        /* Output:
         * Addition: 10 + 20 = 30
         * Multiplication: 10 * 20 = 200
         */
    }
}
```

### Real-world Use Cases for BiConsumer

- **Map processing**: Process key-value pairs in maps
- **Form validation**: Validate form fields with their error messages
- **Configuration settings**: Process setting names and values
- **Database operations**: Process column names and values
- **Logging**: Log parameter names and values

[Back to Top](#table-of-contents)

---

## BiPredicate Interface

The `BiPredicate<T, U>` functional interface represents a predicate (boolean-valued function) of two arguments. It is defined in the `java.util.function` package.

Key characteristics:
- Takes two parameters of types T and U
- Returns a boolean result
- Contains a single abstract method `test(T t, U u)`
- Useful for testing conditions involving two inputs

```java
@FunctionalInterface
public interface BiPredicate<T, U> {
    boolean test(T t, U u);
}
```

### Basic BiPredicate Example

*A simple example showing string comparison using BiPredicate:*

```java
import java.util.function.BiPredicate;

public class BiPredicateDemo {
    public static void main(String[] args) {
        // BiPredicate to check if two strings are equal
        BiPredicate<String, String> areEqual = (s1, s2) -> s1.equals(s2);
        
        // Test the BiPredicate
        System.out.println("'java' equals 'java': " + areEqual.test("java", "java"));  
        // Output: 'java' equals 'java': true
        
        System.out.println("'java' equals 'Java': " + areEqual.test("java", "Java"));  
        // Output: 'java' equals 'Java': false
    }
}
```

### Using Logical Operations

*Example demonstrating the use of and(), or(), and negate() methods:*

```java
import java.util.function.BiPredicate;

public class BiPredicateLogicalDemo {
    public static void main(String[] args) {
        // BiPredicate to check if x > y
        BiPredicate<Integer, Integer> isGreaterThan = (x, y) -> x > y;
        
        // BiPredicate to check if x == y
        BiPredicate<Integer, Integer> isEqual = (x, y) -> x.equals(y);
        
        // BiPredicate to check if x and y are both even
        BiPredicate<Integer, Integer> bothEven = (x, y) -> x % 2 == 0 && y % 2 == 0;
        
        // Test with AND operation
        boolean result1 = isGreaterThan.and(isEqual).test(20, 10);
        System.out.println("20 > 10 AND 20 == 10: " + result1);  // Output: false
        
        // Test with OR operation
        boolean result2 = isGreaterThan.or(isEqual).test(20, 10);
        System.out.println("20 > 10 OR 20 == 10: " + result2);   // Output: true
        
        // Test with combination of operations
        boolean result3 = isGreaterThan.and(bothEven).test(20, 10);
        System.out.println("20 > 10 AND both are even: " + result3);  // Output: true
    }
}
```

### Traditional vs Lambda Implementation

*Example showing traditional implementation compared to lambda expression:*

```java
import java.util.function.BiPredicate;

public class BiPredicateTraditionalDemo {
    public static void main(String[] args) {
        // Traditional way using anonymous inner class
        BiPredicate<String, String> traditional = new BiPredicate<String, String>() {
            @Override
            public boolean test(String s1, String s2) {
                return s1.equals(s2);
            }
        };
        
        // Using lambda expression
        BiPredicate<String, String> lambda = (s1, s2) -> s1.equals(s2);
        
        // Test both implementations
        System.out.println("Traditional: " + traditional.test("ramesh", "ramesh"));  // Output: true
        System.out.println("Lambda: " + lambda.test("java guides", "Java Guides"));  // Output: false
    }
}
```

### Complex Condition Examples

*Examples demonstrating more complex conditions with BiPredicate:*

```java
import java.util.function.BiPredicate;

public class BiPredicateComplexDemo {
    public static void main(String[] args) {
        // Check if sum is greater than a threshold
        BiPredicate<Integer, Integer> sumGreaterThan50 = (x, y) -> (x + y) > 50;
        
        // Check if the first string contains the second
        BiPredicate<String, String> containsString = (text, substring) -> text.contains(substring);
        
        // Check if two numbers are divisible by each other
        BiPredicate<Integer, Integer> areDivisible = (x, y) -> y != 0 && x % y == 0;
        
        // Test the BiPredicates
        System.out.println("30 + 25 > 50: " + sumGreaterThan50.test(30, 25));  // Output: true
        System.out.println("'Java Programming' contains 'gram': " + 
                          containsString.test("Java Programming", "gram"));  // Output: true
        System.out.println("10 is divisible by 2: " + areDivisible.test(10, 2));  // Output: true
        System.out.println("10 is divisible by 3: " + areDivisible.test(10, 3));  // Output: false
    }
}
```

### BiPredicate with Objects

*Example showing how to use BiPredicate with custom objects:*

```java
import java.util.function.BiPredicate;

class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
}

public class BiPredicateObjectDemo {
    public static void main(String[] args) {
        Person person1 = new Person("Alice", 25);
        Person person2 = new Person("Bob", 30);
        
        // Check if two persons have the same name
        BiPredicate<Person, Person> sameName = (p1, p2) -> p1.getName().equals(p2.getName());
        
        // Check if the age difference is less than 10 years
        BiPredicate<Person, Person> ageCloseBy = (p1, p2) -> Math.abs(p1.getAge() - p2.getAge()) < 10;
        
        // Test the BiPredicates
        System.out.println("Same name: " + sameName.test(person1, person2));  // Output: false
        System.out.println("Age difference < 10: " + ageCloseBy.test(person1, person2));  // Output: true
    }
}
```

### Real-world Use Cases for BiPredicate

- **Validation**: Validate pairs of values against business rules
- **Comparison**: Compare two objects or values
- **Authorization**: Check if a user has permission for a specific resource
- **Data filtering**: Filter data based on two conditions
- **Rule evaluation**: Evaluate complex business rules involving two parameters

[Back to Top](#table-of-contents)