
## Table of Contents
| #  | Topic                                                       |
|----|-------------------------------------------------------------|
| **Lambda Expressions** |                                         |
| 1  | [Understanding Lambda Expressions](#understanding-lambda-expressions) |
| 2  | [Blocked vs Unblocked Lambda](#blocked-vs-unblocked-lambda) |
| 3  | [Pure Functions](#pure-functions) |
| 4  | [Higher Order Functions](#higher-order-functions) |



## Understanding Lambda Expressions

Lambda expressions provide a concise way to represent anonymous functions that can be passed as arguments or stored in variables.

### Key Characteristics:

* Anonymous functions (unnamed functions/expressions)
* Provide implementation for functional interfaces
* Enable more concise and readable code
* Suitable for collection framework operations
* Support functional programming paradigms

### Basic Syntax:

```
(parameters) -> { statements }
```

### Example: Simple Lambda Expressions

```java
@FunctionalInterface
interface I {
    void m1();
}

interface J {
    void m2(int x);
}

public class Test {
    public static void main(String[] args) {
        // Simple lambda with no parameters
        I obj = () -> System.out.println("m1-lambda");
        obj.m1();  // Output: m1-lambda
        
        // Lambda with one parameter (with type)
        J obj2 = (int k) -> System.out.println("k: " + k);
        obj2.m2(123);  // Output: k: 123
        
        // Lambda with one parameter (type inference)
        J obj4 = x -> System.out.println("x: " + x);
        obj4.m2(123);  // Output: x: 123
    }
}
```

### Example: Lambda with Return Value

```java
@FunctionalInterface
interface Square {
    int area(int x);
}

public class Main {
    static void m1(Square obj) {
        System.out.println(obj.hashCode());
        System.out.println(obj.area(25));  // Output: 625
    }

    public static void main(String[] args) {
        // Lambda with return value
        Square s = (int value) -> {
            return value * value;
        };
        
        System.out.println(s.hashCode());
        System.out.println(s.area(10));  // Output: 100
        
        m1(s);
        
        // Passing lambda directly as an argument
        System.out.println(m1((int value) -> {
            return value * value;
        }));
    }
}
```

> Note: The `hashCode()` method is implemented by the anonymous class created from the lambda expression.

[Back to Top](#table-of-contents)

## Blocked vs Unblocked Lambda

Lambda expressions can be categorized as blocked or unblocked based on their syntax and format.

### Unblocked Lambda:

* Single expression without curly braces
* Implicit return for functions returning a value
* More concise syntax

### Blocked Lambda:

* Multiple statements enclosed in curly braces `{}`
* Explicit `return` statement for functions returning a value
* Can include variable declarations, multiple operations, etc.

### Example: Comparison of Both Styles

```java
@FunctionalInterface
interface Calculator {
    int calculate(int x, int y);
}

public class LambdaStyles {
    public static void main(String[] args) {
        // Unblocked lambda - single expression, implicit return
        Calculator add = (x, y) -> x + y;
        System.out.println("Addition: " + add.calculate(5, 3));  // Output: Addition: 8
        
        // Blocked lambda - multiple statements, explicit return
        Calculator multiply = (x, y) -> {
            int result = x * y;
            System.out.println("Computing product of " + x + " and " + y);
            return result;
        };
        System.out.println("Multiplication: " + multiply.calculate(5, 3));
        /*
           Output:
           Computing product of 5 and 3
           Multiplication: 15
        */
    }
}
```

[Back to Top](#table-of-contents)

## Pure Functions

Pure functions are a fundamental concept in functional programming that follow specific principles to ensure predictable behavior.

### Key Characteristics:

* No side effects: doesn't modify state outside its scope
* Output depends solely on input parameters
* For the same input, always returns the same output
* Makes code more predictable and testable

### Example: Pure Function

```java
@FunctionalInterface
interface MathOperation {
    int perform(int x, int y);
}

public class PureFunctionDemo {
    public static void main(String[] args) {
        // Pure function: result depends only on inputs
        MathOperation multiply = (int x, int y) -> x * y;
        
        System.out.println(multiply.perform(10, 20));  // Output: 200
        System.out.println(multiply.perform(10, 20));  // Output: 200 (always the same for same inputs)
    }
}
```

### Example: Impure Function

```java
@FunctionalInterface
interface I {
    String transform(int x, int y);
}

public class Test {
    static int a = 10;
    
    public static void main(String[] args) {
        int b = 20;
        
        // Impure function: modifies external state
        I obj = (int x, int y) -> {
            a = 20;  // Modifying static variable - side effect
            // b = 10;  // Would cause error: local variables must be final or effectively final
            return Integer.toString(x * y);
        };
        
        System.out.println(obj.transform(10, 20));  // Output: 200
        System.out.println("Modified a: " + a);     // Output: Modified a: 20
    }
}
```

[Back to Top](#table-of-contents)

## Higher Order Functions

Higher-order functions are functions that can take other functions as parameters or return functions as results.

### Key Characteristics:

* Take one or more functions as input parameters
* May return a function as output
* Enable composition and reuse of behavior
* Core concept in functional programming

### Example: Higher Order Function

```java
interface I {
    int m1();
}

@FunctionalInterface
interface J {
    int m2();
}

@FunctionalInterface
interface K {
    // Higher-order function: Takes two functional interfaces as parameters
    void m3(I obj1, J obj2);
}

class A {
    void m4() {
        System.out.println("m4()");
    }
}

public class Test {
    public static void main(String[] args) {
        // First function
        I obj1 = () -> {
            return 111;
        };
        
        // Second function
        J obj2 = () -> {
            return 222;
        };
        
        // Higher-order function that accepts two functions
        K obj3 = (I x, J y) -> {
            System.out.println(x.m1() + y.m2());  // Using the input functions
            A obj4 = new A();
            obj4.m4();
        };
        
        // Pass functions as arguments
        obj3.m3(obj1, obj2);
        
        /*
           Output:
           333
           m4()
        */
    }
}
```

[Back to Top](#table-of-contents)