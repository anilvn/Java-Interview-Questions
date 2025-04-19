# Java 8 Optional Class

## Introduction

As Java programmers, we often encounter `NullPointerException`. Java 8 introduced the `Optional` class in the `java.util` package to help avoid null checks and these exceptions. `Optional` is a container object that may or may not contain a non-null value. It essentially wraps an object to indicate the possible absence of a value.

---
### Additional Resources

If you found this guide helpful, you might also be interested in my other Spring Framework resources:
- [Core Java & Java-8 Interview Questions](https://github.com/anilvn/Java-Interview-Questions/tree/main)
- [Spring Boot Interview Questions](https://github.com/anilvn/spring-boot-interview-questions/tree/main)
- [Microservices with Spring Cloud Tutorials](https://javatechonline.com/microservices-tutorial/)

Feel free to star and fork these repositories if you find them useful!

---

## Why Use Optional?

* **Avoids NullPointerExceptions:** Enforces explicit handling of null values, reducing the risk of runtime errors.
* **Improves Code Readability:** Makes it clear when a variable might be absent.
* **Promotes Better Design:** Encourages developers to consider the absence of a value.

## Basic Concepts

`Optional` can be thought of as a box that might or might not contain an object. Instead of returning `null`, a method can return an `Optional`. This forces the caller to explicitly check if a value is present.

## Creating Optional Objects

There are several ways to create `Optional` objects:

### 1. `Optional.empty()`

   * Creates an empty `Optional` instance (i.e., it contains no value).

     ```java
     Optional<Object> emptyOptional = Optional.empty();
     System.out.println(emptyOptional); // Output: Optional.empty
     ```

### 2. `Optional.of(value)`

   * Creates an `Optional` with a non-null value.
   * Throws `NullPointerException` if the provided value is `null`.
     ```java
     String email = "test@example.com";
     Optional<String> emailOptional = Optional.of(email);
     System.out.println(emailOptional); // Output: Optional[test@example.com]

     String nullEmail = null;
     // Optional<String> nullOptional = Optional.of(nullEmail); // Throws NullPointerException
     ```

### 3. `Optional.ofNullable(value)`

   * Creates an `Optional` that can hold either a non-null value or `null`.
   * If the provided value is `null`, it creates an empty `Optional`.

     ```java
     String email = "test@example.com";
     Optional<String> emailOptional = Optional.ofNullable(email);
     System.out.println(emailOptional); // Output: Optional[test@example.com]

     String nullEmail = null;
     Optional<String> nullOptional = Optional.ofNullable(nullEmail);
     System.out.println(nullOptional); // Output: Optional.empty
     ```

## Working with Optional Values

### 1.  `isPresent()`

   * Checks if a value is present in the `Optional`.
   * Returns `true` if a value is present, `false` otherwise.

     ```java
     Optional<String> optionalValue = Optional.ofNullable("Hello");
     if (optionalValue.isPresent()) {
         System.out.println("Value is present: " + optionalValue.get());
     } else {
         System.out.println("Value is absent");
     }
     ```

### 2.  `isEmpty()` (Java 11+)

   * Checks if the `Optional` is empty.
   * Returns `true` if empty, `false` otherwise.
   * The opposite of `isPresent()`.

     ```java
     Optional<String> optionalValue = Optional.ofNullable(null);
     if (optionalValue.isEmpty()) {
         System.out.println("Value is absent");
     } else {
         System.out.println("Value is present: " + optionalValue.get());
     }
     ```

### 3.  `get()`

   * Retrieves the value from the `Optional`.
   * **Caution:** Throws `NoSuchElementException` if the `Optional` is empty.
   * **Avoid using `get()` without first checking with `isPresent()` or `isEmpty()`**.

     ```java
     Optional<String> optionalValue = Optional.ofNullable("World");
     if (optionalValue.isPresent()) {
         String value = optionalValue.get();
         System.out.println(value); // Output: World
     }

     Optional<String> emptyOptional = Optional.empty();
     // String value = emptyOptional.get(); // Throws NoSuchElementException
     ```

### 4.  `orElse(defaultValue)`

   * Provides a default value to return if the `Optional` is empty.
   * The `defaultValue` is always evaluated, even if the `Optional` has a value.

     ```java
     Optional<String> optionalValue = Optional.ofNullable(null);
     String value = optionalValue.orElse("Default Value");
     System.out.println(value); // Output: Default Value
     ```

### 5.  `orElseGet(supplier)`

   * Provides a `Supplier` that generates a default value if the `Optional` is empty.
   * The `Supplier` is only invoked if the `Optional` is empty.
   * **Use `orElseGet()` when calculating the default value is expensive (e.g., involves a database call) to avoid unnecessary computation.**

     ```java
     Optional<String> optionalValue = Optional.ofNullable(null);
     String value = optionalValue.orElseGet(() -> {
         // Perform expensive operation to get default value
         return "Default Value";
     });
     System.out.println(value); // Output: Default Value
     ```

### 6.  `orElseThrow(exceptionSupplier)`

   * Throws an exception if the `Optional` is empty.
   * The `exceptionSupplier` provides the exception to be thrown.

     ```java
     Optional<String> optionalValue = Optional.ofNullable(null);
     String value = optionalValue.orElseThrow(() -> new IllegalArgumentException("Value is missing"));
     // Throws IllegalArgumentException: Value is missing
     ```

### 7.  `ifPresent(consumer)`

   * Performs an action with the value if it's present.
   * Takes a `Consumer` functional interface.

     ```java
     Optional<String> optionalValue = Optional.ofNullable("Hello");
     optionalValue.ifPresent(value -> System.out.println("Value: " + value)); // Output: Value: Hello

     Optional<String> emptyOptional = Optional.empty();
     emptyOptional.ifPresent(value -> System.out.println("This won't be printed"));
     ```

### 8.  `ifPresentOrElse(consumer, runnable)` (Java 9+)

   * Performs one of two actions based on whether a value is present or not.
   * Takes a `Consumer` for the present case and a `Runnable` for the empty case.

     ```java
     Optional<String> optionalValue = Optional.ofNullable("Hello");
     optionalValue.ifPresentOrElse(
         value -> System.out.println("Value is present: " + value),
         () -> System.out.println("Value is absent")
     ); // Output: Value is present: Hello

     Optional<String> emptyOptional = Optional.ofNullable(null);
     emptyOptional.ifPresentOrElse(
         value -> System.out.println("Value is present: " + value),
         () -> System.out.println("Value is absent")
     ); // Output: Value is absent
     ```

### 9.  `filter(predicate)`

   * Filters the `Optional` value based on a `Predicate`.
   * If the value is present and matches the `Predicate`, it returns the `Optional`.
   * If the value is present but doesn't match, or if the `Optional` is empty, it returns an empty `Optional`.

     ```java
     Optional<String> optionalValue = Optional.ofNullable("  abc  ");
     Optional<String> filteredValue = optionalValue.filter(value -> value.contains("abc"));
     filteredValue.ifPresent(value -> System.out.println("Filtered Value: " + value)); // Output: Filtered Value:   abc

     Optional<String> emptyOptional = Optional.ofNullable("def");
     Optional<String> filteredEmpty = emptyOptional.filter(value -> value.contains("xyz"));
     filteredEmpty.ifPresent(value -> System.out.println("This won't be printed"));
     ```

### 10. `map(mapper)`

    * Transforms the value inside the `Optional` using a `Function`.
    * If the `Optional` is empty, it returns an empty `Optional`.

     ```java
     Optional<String> optionalValue = Optional.ofNullable("  abc  ");
     Optional<String> trimmedValue = optionalValue.map(String::trim);
     trimmedValue.ifPresent(value -> System.out.println("Trimmed Value: " + value)); // Output: Trimmed Value: abc

     Optional<String> emptyOptional = Optional.empty();
     Optional<Integer> mappedEmpty = emptyOptional.map(String::length);
     mappedEmpty.ifPresent(length -> System.out.println("This won't be printed"));
     ```

### 11. `flatMap(mapper)`

    * Similar to `map()`, but the `mapper` function returns an `Optional`.
    * Useful for chaining `Optional` operations where nested Optionals might result.
    * Flattens the result to avoid `Optional<Optional<U>>`.

## Code Examples and Best Practices

### Example: Handling Null Employee Data

```java
class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private Long phoneNo;

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }
}

// Without Optional (prone to NullPointerException)
Employee employee = getEmployee(); // May return null
if (employee != null) {
    String email = employee.getEmail();
    if (email != null) {
        String result = email.toLowerCase();
        System.out.println(result);
    }
}

// With Optional (safer and cleaner)
Optional<Employee> optionalEmployee = Optional.ofNullable(getEmployee());
optionalEmployee.map(Employee::getEmail)
                .map(String::toLowerCase)
                .ifPresent(System.out::println);

optionalEmployee.ifPresent(emp -> {
    Optional.ofNullable(emp.getId()).ifPresent(id -> {
        // Logic for handling employee id
    });
    Optional.ofNullable(emp.getEmail()).ifPresent(email -> {
        // Logic for handling employee email
    });
});
```


### Example: Retrieving a Customer by Email
```java
import java.util.List;
import java.util.Optional;

class Customer {
    private String email;

    public String getEmail() {
        return email;
    }

    public Customer(String email) {
        this.email = email;
    }
}

class EkartDataBase {
    private static List<Customer> customers = List.of(new Customer("test@test.com"));

    public static List<Customer> getALL() {
        return customers;
    }
}

public static Optional<Customer> getCustomerByEmailId(String email) {
    return EkartDataBase.getALL().stream()
                       .filter(customer -> customer.getEmail().equals(email))
                       .findAny();
}

// Usage
Optional<Customer> customer = getCustomerByEmailId("test@test.com");
customer.ifPresentOrElse(
    c -> System.out.println("Customer found: " + c.getEmail()),
    () -> System.out.println("No customer found with that email")
);

// Or throw an exception if not found
Customer foundCustomer = getCustomerByEmailId("invalid@test.com")
                           .orElseThrow(() -> new RuntimeException("No customer present with this email id"));
```

### Example: Spring REST Controller
```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class EmployeeController {

    private final EmployeeRepository repo; // Assume this is your repository

    public EmployeeController(EmployeeRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = repo.findById(id); // Assuming findById returns Optional

        return employee.map(e -> ResponseEntity.ok(e))
                       .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                           .body("Employee with ID " + id + " not found"));
    }

    @GetMapping("/employees/{id}/name")
    public ResponseEntity<?> getEmployeeNameById(@PathVariable Long id) {
        Optional<Employee> employee = repo.findById(id);

        return employee.flatMap(e -> Optional.ofNullable(e.getFirstName()))
                       .map(name -> ResponseEntity.ok(name.toUpperCase()))
                       .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                           .body("Employee with ID " + id + " not found, or name is null"));
    }
}
```
