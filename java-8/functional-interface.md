I'll refactor and restructure the provided documentation about Functional Interfaces and Java 8 features into a comprehensive markdown format.

# Java 8 Functional Programming Guide

A comprehensive overview of functional programming concepts in Java 8, including functional interfaces, lambda expressions, method references, and built-in functional interfaces. This guide provides practical examples and clear explanations of core concepts.

---

## Table of Contents

| #  | Topic                                                       |
|----|-------------------------------------------------------------|
| **Fundamentals of Functional Interfaces** |                       |
| 1  | [What is a Functional Interface?](#what-is-a-functional-interface) |
| 2  | [Default Methods in Interfaces](#default-methods-in-interfaces) |
| 3  | [Static Methods in Interfaces](#static-methods-in-interfaces) |
| 4  | [Private Methods in Interfaces](#private-methods-in-interfaces) |
| 5  | [Interface vs Abstract Class](#interface-vs-abstract-class) |



## What is a Functional Interface?

A functional interface is a special type of interface introduced in Java 8 that contains exactly one abstract method.

### Key Characteristics:

* Annotated with `@FunctionalInterface` (optional but recommended)
* Contains only one abstract method
* Can have any number of default and static methods.(since java 9+ you can have private methods.)
* Can include methods from the `java.lang.Object` class 
* Supports functional programming paradigms in Java

### Implementation Methods:

There are four ways to provide implementation for functional interfaces:

1. By writing a separate implementation class
2. By writing an anonymous inner class
3. By writing lambda expressions
4. By writing method references

### Example: Basic Functional Interface

```java
@FunctionalInterface
interface I {
    void m1();  // Single abstract method
    
    // Object class methods don't count against the single abstract method rule
    String toString();
    int hashCode();

    private void ex() {  }
}

// Implementation using a separate class
class Impl implements I {
    public void m1() {
        System.out.println("m1 method-Impl");
    }
}

public class Test {
    public static void main(String[] args) {
        I obj = new Impl();
        obj.m1();  // Output: m1 method-Impl
    }
}
```

> Note: `toString()` and `hashCode()` methods are being implemented by `java.lang.Object` class, so they don't violate the single abstract method rule.



> `toString()` and `hashCode()` are methods from `Object`, so **they do not count** toward the abstract method count of a functional interface. The compiler ignores them when validating `@FunctionalInterface`.

### Example: Functional Interface with Inheritance

```java
@FunctionalInterface
interface I {
    void m1();
}

@FunctionalInterface
interface J extends I {
    // No new abstract method - J inherits m1() from I
}

public class Test {
    public static void main(String[] args) {
        // Creating an instance of interface J using anonymous inner class
        J obj = new J() {
            @Override
            public void m1() {
                System.out.println("m1-anonymous");
            }
        };

        obj.m1();  // Output: m1-anonymous
    }
}
```

[Back to Top](#table-of-contents)

## Default Methods in Interfaces

Default methods were introduced in Java 8 to enable interface evolution without breaking existing implementations. They provide a default implementation that can be overridden by implementing classes.

### Key Characteristics:

* Defined with the `default` keyword
* Implicitly `public` (no need to specify the modifier)
* Provide implementation directly within the interface
* Can be overridden by implementing classes
* Help solve the "multiple inheritance of behavior" problem

### Example: Basic Default Method

```java
interface Printable {
    // Abstract method
    void print(String message);
    
    // Default method
    default void printDefault() {
        System.out.println("Default implementation");
    }
}

class PrintImpl implements Printable {
    @Override
    public void print(String message) {
        System.out.println("Message: " + message);
    }
    
    // Optional: Override the default method
    @Override
    public void printDefault() {
        System.out.println("Overridden default implementation");
    }
}
```

### Example: Multiple Inheritance with Default Methods

```java
interface Square {
    default void cal(int x) {
        System.out.println("The Square Of A Given Number: " + x * x);
    }
}

interface Cube {
    default void cal(int x) {
        System.out.println("The Cube Of A Given Number: " + x * x * x);
    }
}

class A implements Square, Cube {
    // Must override to resolve method conflict
    @Override
    public void cal(int x) {
        System.out.println("This is implementation");
        Square.super.cal(50);  // Calling Square's default method
    }
}

public class Main {
    public static void main(String[] args) {
        Cube obj = new A();
        obj.cal(100);  
        
        /* Output:
           This is implementation
           The Square Of A Given Number: 2500
        */
    }
}
```

[Back to Top](#table-of-contents)

## Static Methods in Interfaces

Static methods in interfaces were introduced in Java 8 to provide utility methods that are relevant to the interface but don't require an instance.

### Key Characteristics:

* Defined with the `static` keyword
* Implicitly `public` (though you can explicitly declare them as such)
* Cannot be overridden by implementing classes
* Accessed using the interface name, not through an instance

### Example: Static Methods in Interface

```java
interface I {
    static void m3() {
        System.out.println("This is static m3 method of I interface");
    }
}

class A implements I {
    // This is NOT overriding the interface's static method
    // It's a completely separate method
    static void m3() {
        System.out.println("This is static m3 method of class A");
    }
}

public class Test {
    public static void main(String[] args) {
        I.m3();  // Output: This is static m3 method of I interface
        A.m3();  // Output: This is static m3 method of class A
    }
}
```

> Note: Static methods in interfaces cannot be overridden in the implementing class. If you define a method with the same signature, it will be treated as a new method in the class.


## Private Methods in Interfaces

Private methods in interfaces were introduced in Java 9 to improve code reusability within interfaces.

### Key Characteristics:

* Can be either `private` or `private static`
* Help avoid code duplication in default and static methods
* Cannot be accessed outside the interface
* Enhance encapsulation within interfaces

### Example: Private Methods in Interface

```java
public interface CustomInterface {
    void abstractMethod();  // Abstract method

    default void defaultMethod() {
        // Calling private helper method to increase reusability
        privateMethod();
        System.out.println("Default method");
    }

    static void staticMethod() {
        // Calling private static helper method
        privateStaticMethod();
        System.out.println("Static method");
    }

    // Private helper method for default methods
    private void privateMethod() {
        System.out.println("Private method");
    }

    // Private static helper method for static methods
    private static void privateStaticMethod() {
        System.out.println("Private static method");
    }
}

public class Main implements CustomInterface {
    public static void main(String[] args) {
        CustomInterface customInterface = new Main();
        customInterface.defaultMethod();  
        /*
           Output:
           Private method
           Default method
        */
        
        customInterface.abstractMethod();  // Output: Abstract method
        
        CustomInterface.staticMethod();
        /*
           Output:
           Private static method
           Static method
        */
    }

    @Override
    public void abstractMethod() {
        System.out.println("Abstract method");
    }
}
```

[Back to Top](#table-of-contents)

## Interface vs Abstract Class

Understanding the differences between interfaces and abstract classes is crucial for proper design decisions in Java.

### Comparison Table:

| Feature | Interface | Abstract Class |
|---------|-----------|----------------|
| Variables | Always `public static final` | Can be `private`, `public`, `static`, `protected`, `final` |
| Variable initialization | Only declaration, no initialization | Can initialize variables |
| Blocks/Initializers | Cannot write blocks (instance/static) | Can include both static and instance initializers |
| Constructors | Cannot write constructors | Can write constructors |
| Object creation | Cannot create objects, only reference variables | Cannot instantiate directly, but can be extended |
| State representation | Doesn't represent object state | May represent object state |
| Methods | Abstract, default, static, private (Java 9+) | Abstract and concrete methods |
| Default methods | Supported | Not applicable (uses regular methods) |
| Multiple inheritance | Supported | Not supported |
| Object class methods | Cannot directly override | Can override |



[Back to Top](#table-of-contents)

