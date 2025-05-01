
## Table of Contents
| #  | Topic                                                       |
|----|-------------------------------------------------------------|
| **Method References** |                                         |
| 1 | [Method References Basics](#method-references-basics) |
| 2 | [Types of Method References](#types-of-method-references) |



## Method References Basics

Method references provide a way to refer to methods or constructors without invoking them, offering a more concise alternative to lambda expressions in certain scenarios.

### Key Characteristics:

* Provide implementation for functional interfaces
* Reference existing method implementations
* Create more readable and concise code
* Use the `::` operator for reference
* Internally create lambda expressions

### Example: Basic Method Reference

```java
@FunctionalInterface
interface Printable {
    void print(String message);
}

public class Main {
    public static void main(String[] args) {
        // Method reference to a static method
        Printable printable = System.out::println;
        printable.print("Hello");  // Output: Hello
        
        // Equivalent lambda expression
        Printable printableLambda = (String msg) -> System.out.println(msg);
        printableLambda.print("Hello");  // Output: Hello
    }
}
```

### Example: Method Reference to Static Method

```java
@FunctionalInterface
interface I {
    void m1();
}

class A {
    static public void test() {
        System.out.println("Implementation for m1-I");
    }
}

public class Test {
    public static void main(String[] args) {
        // Method reference to static method
        I obj = A::test;
        
        /* Equivalent lambda expression created internally:
           () -> { System.out.println("Implementation for m1-I"); }
        */
        
        obj.m1();  // Output: Implementation for m1-I
    }
}
```

[Back to Top](#table-of-contents)

## Types of Method References

Java 8 supports four types of method references, each with its specific use case and syntax.

### 1. Reference to a Static Method

```java
ClassName::staticMethodName
```

### 2. Reference to an Instance Method of a Particular Object

```java
objectName::instanceMethodName
```

### 3. Reference to an Instance Method of an Arbitrary Object of a Particular Type

```java
ClassName::instanceMethodName
```

### 4. Reference to a Constructor

```java
ClassName::new
```

### Example: Different Types of Method References

```java
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

class Person {
    private String name;
    
    public Person(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public static int compareByName(Person p1, Person p2) {
        return p1.getName().compareTo(p2.getName());
    }
}

public class MethodReferenceDemo {
    public static void main(String[] args) {
        // 1. Reference to a static method
        BiFunction<Person, Person, Integer> comparator = Person::compareByName;
        
        // 2. Reference to an instance method of a particular object
        Person person = new Person("John");
        Supplier<String> nameSupplier = person::getName;
        System.out.println(nameSupplier.get());  // Output: John
        
        // 3. Reference to an instance method of an arbitrary object
        Function<Person, String> nameFunction = Person::getName;
        System.out.println(nameFunction.apply(new Person("Alice")));  // Output: Alice
        
        // 4. Reference to a constructor
        Function<String, Person> personCreator = Person::new;
        Person newPerson = personCreator.apply("Bob");
        System.out.println(newPerson.getName());  // Output: Bob
    }
}
```

### Example: Using Method Reference with Functional Interface

```java
public interface FunctionalInterfaceDemo {
    void singleAbstMethod();
    
    default void printName() {
        System.out.println("Welcome to Code Decode");
    }
    
    default void printName2() {
        System.out.println("Welcome to Code Decode");
    }
}

class Test {
    public static void testImplementation() {
        System.out.println("This is test implementation of your abstract method");
    }
}

public class Main {
    public static void main(String[] args) {
        // Using method reference to implement the functional interface
        FunctionalInterfaceDemo functionalInterfaceDemo = Test::testImplementation;
        functionalInterfaceDemo.singleAbstMethod();  // Output: This is test implementation of your abstract method
        
        // Using lambda expression
        FunctionalInterfaceDemo f = () -> System.out.println("IMPLEMENTATION of SAM");
        f.singleAbstMethod();  // Output: IMPLEMENTATION of SAM
    }
}
```


[Back to Top](#table-of-contents)



