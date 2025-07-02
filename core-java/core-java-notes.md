# Java Rules: Abstract Classes, Interfaces, Polymorphism, and More
<!-- | #   | Question Text                                                                                     |
|-----|---------------------------------------------------------------------------------------------------|
| **Abstract Classes**                                                                                   |
| 1   | [What is an abstract class and how is it declared?](#abstract-classes)                           |
| 2   | [Can abstract classes be instantiated directly?](#abstract-classes)                             |
| 3   | [What types of methods can abstract classes contain?](#abstract-classes)                        |
| 4   | [What are the rules for abstract methods in abstract classes?](#abstract-classes)               |
| 5   | [Can abstract classes have constructors and instance variables?](#abstract-classes)             |
| **Interfaces**                                                                                        |
| 6   | [What are the characteristics of interface methods and fields?](#interfaces)                    |
| 7   | [What are default and static methods in interfaces?](#interfaces)                               |
| 8   | [What is the purpose of private methods in interfaces?](#interfaces)                            |
| **Marker Interfaces**                                                                                 |
| 9   | [What are marker interfaces and their use cases?](#marker-interfaces)                           |
| 10  | [What are examples of marker interfaces in Java?](#marker-interfaces)                           |
| **Functional Interfaces**                                                                             |
| 11  | [What is a functional interface and how is it used?](#functional-interfaces)                    |
| 12  | [What are examples of functional interfaces in Java?](#functional-interfaces)                   |
| **Polymorphism**                                                                                      |
| 13  | [What is polymorphism and what are its types?](#polymorphism)                                   |
| 14  | [How does runtime polymorphism work in Java?](#polymorphism)                                    |
| **Method Overloading**                                                                                |
| 15  | [What is method overloading and how is it achieved?](#method-overloading)                       |
| 16  | [What are the rules for method overloading?](#method-overloading)                               |
| **Method Overriding**                                                                                 |
| 17  | [What is method overriding and how is it different from overloading?](#method-overriding)       |
| 18  | [What are the rules for method overriding?](#method-overriding)                                 |
| **Constructors**                                                                                      |
| 21  | [What are constructors and how are they used?](#constructors)                                   |
| 22  | [What are the rules for constructor overloading?](#constructors)                                |
| **final Keyword**                                                                                     |
| 23  | [What are the uses of the `final` keyword in Java?](#final-keyword)                             |
| 24  | [What are the rules for final variables, methods, and classes?](#final-keyword)                 |
| **transient Keyword**                                                                                 |
| 25  | [What is the purpose of the `transient` keyword?](#transient-keyword)                           |
| 26  | [What are the rules for using `transient` fields?](#transient-keyword)                          |
| **synchronized Keyword**                                                                              |
| 27  | [What is the `synchronized` keyword and how is it used?](#synchronized-keyword)                 |
| 28  | [What are the rules for synchronized methods and blocks?](#synchronized-keyword)                |
| **Exceptions**                                                                                        |
| 29  | [What is the hierarchy of exceptions in Java?](#exceptions)                                     |
| 30  | [What are the differences between checked and unchecked exceptions?](#exceptions)              |
| 31  | [What are the rules for try-catch-finally blocks?](#exceptions)                                 |
| 32  | [How are custom exceptions created in Java?](#exceptions)                                       |
| **Java 8 Features**                                                                                   |
| 33  | [What are lambda expressions and how are they used?](#java-8-features)                          |
| 34  | [What is the Stream API and how does it work?](#java-8-features)                                |
| 35  | [What are default methods in interfaces?](#java-8-features)                                    |
| 36  | [What are method references and their types?](#java-8-features)                                |
| 37  | [What is the purpose of the `Optional` class?](#java-8-features)                                |
| **Multithreading**                                                                                    |
| 38  | [What are the ways to create threads in Java?](#multithreading)                                 |
| 39  | [What are the rules for thread safety?](#multithreading)                                        |
| 40  | [What are the features of the Executors framework?](#multithreading)                           |
| **Advanced Exception Handling**                                                                      |
| 41  | [What are the best practices for exception handling?](#advanced-exception-handling)            |
| 42  | [What are assertions and how are they used?](#advanced-exception-handling)                     |
| **Generics**                                                                                         |
| 43  | [What are generics and their rules in Java?](#generics)                                        |
| 44  | [What are bounded and unbounded wildcards in generics?](#generics)                             |
| **Collections Framework**                                                                            |
| 45  | [What are the main interfaces and classes in the Collections Framework?](#collections-framework)|
| 46  | [What are the rules for using iterators with collections?](#collections-framework)             |
| **I/O and NIO**                                                                                      |
| 47  | [What are the main classes for I/O operations in Java?](#io-and-nio)                           |
| 48  | [What are the features of NIO in Java?](#io-and-nio)                                           |
| **Inner Classes**                                                                                    |
| 49  | [What are the types of inner classes and their rules?](#inner-classes)                         |
| 50  | [What are the rules for anonymous inner classes?](#inner-classes)                              |
| **Reflection**                                                                                       |
| 51  | [What is reflection and how is it used in Java?](#reflection)                                  |
| 52  | [What are the rules and limitations of reflection?](#reflection)                               |
| **Annotations**                                                                                      |
| 53  | [What are annotations and their types in Java?](#annotations)                                  |
| 54  | [What are meta-annotations and their purposes?](#annotations)                                  |
| **Java Memory Model**                                                                                |
| 55  | [What are the key concepts of the Java Memory Model?](#java-memory-model)                      |
| 56  | [What are the rules for thread safety in the Java Memory Model?](#java-memory-model)           |
| **Garbage Collection**                                                                               |
| 57  | [What are the types of references in Java and their impact on garbage collection?](#garbage-collection)|
| 58  | [What are the common garbage collection algorithms in Java?](#garbage-collection)              |
| **Java 9+ Features**                                                                                 |
| 59  | [What is the Java Module System and its features?](#java-9-features)                           |
| 60  | [What are the new features introduced in Java 9 and later versions?](#java-9-features)         | -->

## Abstract Classes

1. Can contain `static`, `final`, and `instance variables` with any `access modifier` (no restriction)
2. Can have constructors, static methods, and final methods
3. Abstract methods cannot be `private`, `static`, or `final`
4. Declared with the `abstract` keyword
5. Cannot be instantiated directly
6. May contain both abstract and concrete methods
7. Abstract methods have no implementation (no body) and must be overridden by subclasses
8. Subclasses must either implement all abstract methods or be declared abstract themselves 
9. Can extend only one abstract class (single inheritance)
10. Can implement interfaces
11. A class can extend only one abstract class but implement multiple interfaces

    - Abstract classes **can have instance variables**, and constructors help **initialize those variables**.

    - Even though **you can’t instantiate** an abstract class directly, its **constructor is called when a concrete subclass is instantiated**.

### 📌 Example:

```java
abstract class Animal {
	int typeOfAnimal = "Animal"; // instance variable with direct initialization
    String name;  // any access modfier -  (final also)

    // static & Non-Static blocks for var initilization.
    // static & Non-Static methods. - (private,final)

    // Constructor in abstract class
    Animal(String name) {
        this.name = name;
        System.out.println("Animal constructor called");
    }
}

class Dog extends Animal {
    Dog(String name) {
        super(name);
        System.out.println("Dog constructor called");
    }
}

public class Main {
    public static void main(String[] args) {
        Dog d = new Dog("Buddy");
    }
}
```

### 🧾 Output:

```
Animal constructor called
Dog constructor called
```

So, the constructor of the abstract class runs when the child class is created.

---

## Interfaces

1. All interface methods are implicitly `public` and `abstract` (before Java 8)
2. Cannot be instantiated.
3. All fields are implicitly `public`, `static`, and `final` (constants) 
4. Cannot contain instance variables (only constants)
5. Since Java 8, interfaces can have:
   - Default methods (with `default` keyword)
   - Static methods
      - `default` and `static` methods in interfaces are `implicitly public`.
      - `default` methods have a body and can be overridden.
6. `protected` or package-private methods are `not allowed` in interfaces.
7. Since Java 9, interfaces can have `private` methods. and for internal reuse. `ex:` private static method(){ }
8. No constructors allowed 
9. A class implements an interface using the `implements` keyword
10. A class must implement all methods of the interface unless it's abstract
11. An interface can extend multiple interfaces (multiple inheritance) 
<br/>

- void m1(); non-static method un-implmented her in interface, can be implemented in class.

```java
interface Animal {
    // Variables (implicitly public static final)
    int AGE = 10;

    // Static block ❌ Not allowed in interfaces
    // Instance block ❌ Not allowed

    // Static method & Default method (Java 8+)
    // Private method (Java 9+)

    // Abstract method 
    void eat(); // non-static method
}
```

#### Default Methods
1. Defined in interfaces with the `default` keyword
2. Provide implementation within the interface
3. Can be overridden by a default method in a sub-interface
4. If a class implements multiple interfaces with the same default method, it must override it
5. Cannot be marked as `final`, `static`, `abstract`, or `synchronized`
6. Can access static interface methods
<details>
<summary>code example for default methods in interface</summary>

```java
// ❌ Invalid default method declarations — commented to avoid compile errors
interface InvalidDefaults {

    // ❌ Cannot use final with default methods — they are meant to be overridden
    // final default void method1();        

    // ❌ Cannot use static with default methods — static and default don't go together
    // static default void method2();       

    // ❌ Cannot use abstract with default methods — default has a body, abstract doesn't
    // abstract default void method3();     

    // ❌ Cannot use synchronized — not allowed in interface methods
    // synchronized default void method4(); 
}


// ✅ Multiple interfaces with same default method — must resolve conflict
interface A {
    default void greet() {
        System.out.println("Hello from A");
    }
}

interface B {
    default void greet() {
        System.out.println("Hello from B");
    }
}

class MyClass implements A, B {
    // Must override to resolve conflict between A and B
    @Override
    public void greet() {
        System.out.println("Hello from MyClass");
    }
}


// ✅ Overriding default method in a sub-interface
interface Parent {
    default void show() {
        System.out.println("Parent show");
    }
}

interface Child extends Parent {
    @Override
    default void show() {
        System.out.println("Child show");
    }
}


// ✅ Main class to run all examples
public class DefaultMethodExamples {
    public static void main(String[] args) {
        System.out.println("=== Conflict Resolution ===");
        MyClass obj = new MyClass();
        obj.greet(); // Output: Hello from MyClass

        System.out.println("\n=== Sub-interface override ===");
        Child child = new Child() {}; // anonymous class implementation
        child.show(); // Output: Child show
    }
}
```
</details>


## Marker Interfaces

1. Empty interfaces with no methods or constants
2. Used to "mark" a class as having a certain capability
3. Provides runtime type information through instanceof operator
4. Examples include `Serializable`, `Cloneable`, and `Remote`
5. Cannot be annotated with `@FunctionalInterface`
6. Often replaced by annotations in modern Java
7. No method implementation requirements for implementing classes
<details>
<summary>example for Marker Interfaces with ID Card Analogy </summary>
<br/>

* Suppose you’re at a tech conference.
* Some attendees have a **"VIP" badge**.
* The badge has **no data or behavior**, just a **mark**.
* At the entrance, the guard checks:
  `"if (person instanceof VIP)"`
  → Let them in first.


```java
public interface VIP {} // marker interface

public class Person {}

public class Guest extends Person implements VIP {}
```

Now you can write logic like:

```java
if (guest instanceof VIP) {
    System.out.println("Give special treatment");
}
```
</details>

## Functional Interfaces

1. Contains exactly one abstract method
2. Can be annotated with `@FunctionalInterface` (optional but recommended)
3. Can contain `default` methods, `static` methods, and methods from `Object` class
4. Can be implemented using lambda expressions
5. Examples include `Runnable`, `Comparable`, `Consumer`, `Supplier`, `Function`
6. Can be used with method references
7. Cannot have more than one abstract method (will cause compilation error if annotated with `@FunctionalInterface`)
8. Compatible with Java's Stream API

```java
@FunctionalInterface
interface MyComparator<T> {
    int compare(T o1, T o2); // only abstract method

    boolean equals(Object obj); // from Object class — ignored
}
```

## Polymorphism

1. Ability of an object to take many forms
2. Achieved through inheritance and interfaces
3. Types:
   - Compile-time (method overloading)
   - Runtime (method overriding)
4. Method calls are resolved at runtime based on the object's actual type
5. Objects behave differently based on their actual runtime type
6. Allows reference variables of a superclass to refer to subclass objects
7. Enables the use of superclass type to refer to subclass object
8. Enables code reuse and flexibility
9. Polymorphic references can only access methods defined in the reference type
10. Type casting required to access subclass-specific methods

## Method Overloading

> **Method Overloading** is resolved at **compile-time**.                                                         
> It refers to the ability to define multiple methods with the same name but different parameter types or counts in the same class.

1. Multiple methods with the same name but different parameters in the same class
2. Differences can be:
   - Number of parameters
   - Data type of parameters
   - Order of parameters
3. Cannot overload methods that differ only by return type. Return type alone is not sufficient for overloading and Return type is optional.
4. Determined at compile time (static binding)
5. Access modifiers can be different for overloaded methods. (Access modifiers alone is not sufficient)
6. Exception declarations can be different
7. Can involve static and non-static methods
8. Constructors can be overloaded
9. Method resolution follows most specific parameter matching

## Method Overriding

> **Method Overriding** is resolved at **runtime**.                                                         
> It allows a subclass to provide a specific implementation of a method already defined in its superclass.

1. Redefining a superclass method in a subclass with the same signature
2. Method signature must be identical (name, parameters, return type)
3. Return type can be a subtype of the original return type (covariant return)
4. Access modifier cannot be more restrictive than the overridden method
5. Cannot throw broader exceptions than the overridden method
6. `@Override` annotation is recommended but optional
7. `final`, `static`, `private` methods cannot be overridden
   - `static` methods cannot be overridden (although they can be hidden)
   - `private` methods cannot be overridden
8. Determined at runtime (dynamic binding)
9. Can use `super` keyword to call the superclass version of the method

 **Example for Covariant Return type:**

```java
class Animal {}

class Dog extends Animal {}

class Parent {
    Animal getAnimal() {
        return new Animal();
    }
}

class Child extends Parent {
    Dog getAnimal() {
        return new Dog(); // Valid: Dog is a subclass of Animal
    }
}
```

In the above code:

* `Parent.getAnimal()` returns `Animal`
* `Child.getAnimal()` returns `Dog`, which is a subclass of `Animal`
* This is valid because of covariant return type

## Constructors

1. Have the same name as the class
2. No return type (not even void)
3. Are called during object creation with the `new` keyword
4. Can be overloaded (multiple constructors with different parameters)
5. If no constructor is provided, Java adds a default no-argument constructor
6. Can use `this()` to call another constructor in the same class (must be first statement)
7. In subclasses, can use `super()` to call parent constructor (must be first statement)
8. If parent class has no default constructor, subclass must explicitly call a parent constructor
9. Can have any access modifier (`public`, `protected`, `private`, default)
10. Private constructors prevent instantiation from outside the class
11. Constructors can `throw` exceptions
12. Static initialization blocks run before constructors
13. Instance initialization blocks run before constructors but after the parent constructor call
14. Cannot be inherited or overridden

```java
class Parent {
    Parent(int x) {   // No default (no-arg) constructor
        System.out.println("Parent constructor: " + x);
    }
}

class Child extends Parent {
    Child() {
        // Implicit call to super() here — but no Parent() exists, so compile error!
        System.out.println("Child constructor");
    }
}
```

## final keyword

1. **With variables**:
   - Makes the variable a constant (cannot be reassigned)
   - Must be initialized at declaration or in constructor
   - Local final variables can be initialized later but only once
    ``` java
      public class Test {
         public static void main(String[] args) {
        final int x;  // declared but not initialized
        x = 10;       // initialized later — valid
        // x = 20;    // ❌ Error: cannot assign a value again
        System.out.println(x); // 10
        }
     }
    ```
   - Final reference variables cannot refer to a different object, but the object's state can be modified
    ``` java
     final Person p = new Person("Alice");

        p.name = "Bob";      // ✅ Allowed: modifying internal state
        // p = new Person("Charlie"); // ❌ Not allowed: can't reassign final reference
    ```
   - Often used with `static` for class constants

2. **With methods**:
   - Cannot be overridden in subclasses
   - Can be overloaded
   - Promotes security and performance optimization
```java
public class Main {

    // final method - cannot be overridden
    public final void show(String msg) {
        System.out.println("Message: " + msg);
    }

    // overloaded method - same method name, different parameter
    public void show(int number) {
        System.out.println("Number: " + number);
    }

    public static void main(String[] args) {
        Main obj = new Main();

        // Calling overloaded methods
        obj.show("Hello, World!");  // Calls final method
        obj.show(123);              // Calls overloaded method
    }
}
```

3. **With classes**:
   - Cannot be extended/subclassed
   - All methods are implicitly final
   - Examples: `String`, `Integer`, and wrapper classes
   - Often used for immutable classes

4. **With parameters**:
   - Parameter cannot be reassigned within the method
   - Object state can still be modified

5. **With inner classes**:
   - Can access final local variables of the enclosing method
<details>
<summary>example</summary>

```java
public class Main {

    public void validExample() {
        System.out.println("=== Valid Example ===");
        
        final String greeting = "Hello";          // explicitly final
        int number = 100;                         // effectively final (not modified)

        class InnerClass {
            public void print() {
                System.out.println("Greeting: " + greeting);
                System.out.println("Number: " + number);
            }
        }

        InnerClass inner = new InnerClass();
        inner.print();
    }

    public void invalidExample() {
        System.out.println("\n=== Invalid Example ===");

        String name = "Anil";
        name = "Valsa";  // ❌ Now it's NOT effectively final

        class InnerClass {
            public void print() {
                // ❌ Compile-time error if uncommented
                // System.out.println("Name: " + name);
            }
        }

        InnerClass inner = new InnerClass();
        inner.print();
    }

    public static void main(String[] args) {
        Main obj = new Main();

        obj.validExample();

        // Uncommenting this will cause a compile error
        // obj.invalidExample();
    }
}
```
</details>

## volatile keyword

1. **Ensures visibility** of changes to a variable across **multiple threads**
2. Tells the JVM **not to cache the value** of the variable in threads' local memory
3. Guarantees **read/write goes directly to and from main memory**
4. Applied **only to instance variables** (not methods, classes, or local variables)
5. **Does not guarantee atomicity** — operations like `i++` are **not** thread-safe even if `i` is volatile
6. Can **prevent instruction reordering** (useful in implementing safe singleton patterns)
7. Commonly used in **double-checked locking** and **flags** (e.g., stopping a thread)
8. **Not a replacement for synchronized** — use it only when **atomicity is not required**
9. Helps **avoid stale data issues** in multi-threaded applications
10. Cannot be used with `final` (as final means constant and can't change, while volatile is about change visibility)

## transient keyword

1. Used to indicate that a field should not be serialized
2. Applies only to instance variables
3. Cannot be used with `static` fields (as static fields are not serialized anyway)
4. During deserialization, transient fields are initialized with default values
5. Cannot be applied to methods or classes
6. Often used for security-sensitive data or derived fields
7. Has no effect if the class doesn't implement `Serializable`
8. Can be used with custom serialization (readObject/writeObject methods)
9. Not reflected in XML serialization frameworks like JAXB
10. Custom serialization can still manually serialize transient fields if needed



<details>
<summary>serialize transient fields using Custom serialization</summary>

* `transient` fields are **not saved automatically** when an object is serialized.
* You can still save them by writing your own methods called `writeObject()` and `readObject()`.
* Inside these methods, you tell Java how to save and load the transient fields manually.
* This way, you control how the transient data is stored and restored.
</details>



## Comparison between Mutable vs Immutable Classes in Java

| Aspect                | Mutable Class                                            | Immutable Class                                                 |
| --------------------- | -------------------------------------------------------- | --------------------------------------------------------------- |
| **Definition**        | Object whose state **can be changed** after creation     | Object whose state **cannot be changed** after creation         |
| **Class Declaration** | Regular class                                            | Declared as `final` to prevent subclassing                      |
| **Field Declaration** | `private`, **non-final**                                 | `private final`                                                 |
| **Setters**           | ✅ Provided — used to modify fields                       | ❌ Not provided — no way to change internal state                |
| **Getters**           | ✅ Can expose direct fields (including mutable objects)   | ✅ Provided, but return **defensive copies** if field is mutable |
| **Constructor**       | Used to initialize fields, but values can change later   | All fields initialized once via constructor                     |
| **Thread Safety**     | ❌ Not thread-safe (must handle manually)                 | ✅ Thread-safe (due to immutability)                             |
| **Use Cases**         | When object state needs to change over time (e.g., DTOs) | For constants, safe sharing in concurrent environments          |
| **Example Classes**   | `StringBuilder`, `ArrayList`, `HashMap`, custom beans    | `String`, `Integer`, `LocalDate`, custom immutable classes      |

---


## synchronized keyword

1. **With methods**:
   - Entire method becomes a synchronized block on the object's monitor
   - For instance methods, synchronizes on `this` (current object)
   - For static methods, synchronizes on the Class object
   - Only one thread can execute any synchronized method of the object at a time
<details>
<summary>more notes on synchronizatoin with methods</summary>

* **Instance methods:**
  When you use `synchronized` on an instance method, the lock is on the current object (`this`).
  This means two threads can run the same method at the same time **if they are using different objects**.

* **Static methods:**
  When you use `synchronized` on a static method, the lock is on the **Class object** itself.
  This means only one thread can run any synchronized static method at a time, no matter which instance is used.
</details>

<br/>

2. **With blocks**:
   - Syntax: `synchronized(object) { ... }`
   - Acquires lock on the specified object
   - Provides more fine-grained control than synchronized methods
   - Can reduce contention by synchronizing only critical sections

3. **General rules**:
   - Provides mutual exclusion and visibility guarantees
   - Can cause deadlocks if locks acquired in inconsistent order
   - Does not make the reference to the synchronized object thread-safe
   - Synchronizing on non-final fields can be dangerous
   - Affects performance due to lock acquisition overhead
   - Reentrancy: a thread can acquire the same lock multiple times
   - Not inherited in subclass method overrides (must be explicitly declared)
## Multithreading

### Thread Creation
1. Extend `Thread` class or implement `Runnable` interface
2. Can't restart a thread once it has completed execution
3. Thread priorities are hints to the scheduler, not guarantees
4. Daemon threads are terminated when all user threads complete
5. `Thread.sleep()` keeps lock ownership but pauses execution
6. `Thread.join()` makes the current thread wait for another thread to die
7. Thread names should be unique for debugging
8. Thread groups are mostly obsolete

#### Difference Between `sleep()` and `wait()`:

| Aspect            | `Thread.sleep(milliseconds)`                              | `Object.wait()`                                                                    |
| ----------------- | --------------------------------------------------------- | ---------------------------------------------------------------------------------- |
| **Lock release**  | Does **NOT** release the lock.                            | **Releases** the lock on the object.                                               |
| **Usage context** | Can be called anywhere (doesn't require synchronization). | Must be called inside a synchronized block/method on the object you're waiting on. |
| **Thread state**  | Thread pauses for specified time but keeps the lock.      | Thread pauses and releases lock until notified.                                    |
| **Waking up**     | Wakes after the sleep time ends (or interrupted).         | Wakes when another thread calls `notify()` or `notifyAll()` on the same object.    |
| **Purpose**       | Pause execution temporarily.                              | Wait for a condition or signal from another thread.                                |

---

### Thread Safety
1. Always synchronize access to mutable shared data
2. Avoid unnecessary synchronization for better performance
3. Immutable objects are inherently thread-safe
4. Thread-local variables provide per-thread isolated storage
5. Volatile ensures visibility but doesn't guarantee atomicity
6. CAS (Compare-And-Swap) operations can avoid locking
7. High-level concurrency utilities preferred over raw synchronization

### Locks and Conditions
1. `ReentrantLock` provides same behavior as synchronized, plus additional features
2. Must explicitly unlock in finally blocks
3. `ReadWriteLock` allows concurrent reads but exclusive writes
4. Lock fairness setting determines if longest-waiting thread gets lock first
5. `Condition` objects can provide more fine-grained waiting/signaling
6. `tryLock()` attempts to get lock without blocking
7. `lockInterruptibly()` allows lock acquisition to be interrupted

### Executors Framework
1. Prefer executors over creating threads directly
2. Thread pools reuse threads to reduce overhead
3. `newFixedThreadPool` creates a fixed-size thread pool
4. `newCachedThreadPool` creates threads as needed, reuses idle threads
5. `newSingleThreadExecutor` guarantees sequential execution
6. `ScheduledExecutorService` handles delayed and periodic tasks
7. Always shut down executor services explicitly
8. Handle uncaught exceptions via `UncaughtExceptionHandler`

### Concurrent Collections
1. Thread-safe without external synchronization
2. `ConcurrentHashMap` provides better concurrency than `Hashtable`
3. `CopyOnWriteArrayList` optimizes for read-heavy scenarios
4. `BlockingQueue` supports producer-consumer pattern
5. `ConcurrentSkipListMap` is a concurrent sorted map
6. May not reflect the latest state of other threads
7. Size-related operations (size, isEmpty) may not be accurate in concurrent contexts
8. Iteration doesn't throw `ConcurrentModificationException`

## Exceptions

1. **Hierarchy**:
   - `Throwable` (root)
     - `Error`: serious problems, not usually caught
     - `Exception`: recoverable conditions
       - Checked exceptions: must be declared or caught
       - `RuntimeException` (unchecked): not required to be declared or caught

2. **Checked Exceptions**:
   - Must be handled with try-catch or declared with `throws` clause
   - Compile-time enforcement
   - Examples: `IOException`, `SQLException`

3. **Unchecked Exceptions (RuntimeExceptions)**:
   - Not required to be caught or declared
   - Examples: `NullPointerException`, `ArrayIndexOutOfBoundsException`

4. **Try-Catch-Finally**:
   - `try`: Contains code that might throw exceptions
   - `catch`: Handles specific exceptions
   - `finally`: Always executes regardless of exception (unless `System.exit()` is called)
   - Since Java 7: `try-with-resources` for automatic resource management

5. **Throw and Throws**:
   - `throw`: Used to explicitly throw an exception
   - `throws`: Declares that a method might throw certain exceptions

6. **Custom Exceptions**:
   - Should extend `Exception` for checked exceptions
   - Should extend `RuntimeException` for unchecked exceptions
   - Should include constructors that call the parent constructors

7. **Exception Chaining**:
   - `throw new SomeException("Message", originalException);`
   - Preserves the original cause

8. **Multi-catch** (Java 7+):
   - `catch (IOException | SQLException e) { ... }`
   - Exception types must not be in a subclass relationship

9. **Important rules**:
   - More specific exceptions must be caught before broader ones
   - Subclass methods cannot throw broader checked exceptions than superclass method
   - Finally block executes even if there's a return statement in try or catch
   - Never empty catch blocks in production code
   - Prefer specific exceptions over generic ones
   - Clean up resources in finally blocks or use try-with-resources

## Advanced Exception Handling

### Best Practices
1. Create specific exception types for specific error cases
2. Include relevant information in exception messages
3. Document all exceptions in Javadoc
4. Don't catch `Throwable` or `Exception` (too broad)
5. Release resources in finally blocks or use try-with-resources
6. Log exceptions with stack traces
7. Don't suppress exceptions without handling them
8. Preserve the original exception when wrapping (exception chaining)
9. Throw early, catch late
10. Convert checked exceptions to unchecked when appropriate

### Assertions
1. Used for internal invariants and assumptions
2. Disabled by default at runtime (enable with -ea flag)
3. Not for validating public method parameters or user input
4. Should not have side effects
5. Used for conditions that should never happen in correct code

### Java 7+ Exception Handling
1. Try-with-resources manages `AutoCloseable` resources automatically
2. Resources declared in try are closed in reverse order
3. Suppressed exceptions are accessible via `getSuppressed()`
4. Multi-catch reduces code duplication: `catch(IOException | SQLException ex)`
5. More specific exception classes must be caught before more general ones
6. Rethrowing precise exceptions preserves original exception types



## Java 8 Features

### Lambda Expressions
1. Syntax: `(parameters) -> expression` or `(parameters) -> { statements; }`
2. Can be used only with functional interfaces
3. Parameter types can be inferred by the compiler
4. Can access final or effectively final local variables
5. Cannot modify local variables (must be effectively final)
6. `this` refers to the enclosing class, not the lambda
7. Can throw exceptions, but must conform to the functional interface
8. Cannot specify return type explicitly

### Stream API
1. Streams do not store data; they process data from a source
2. Stream operations are either intermediate (return a stream) or terminal (produce a result)
3. Streams are lazy - evaluation only happens on terminal operations
4. Streams can be traversed only once
5. Operations are processed vertically (per element) not horizontally (per operation)
6. Parallel streams use the ForkJoinPool.commonPool() by default
7. Not suitable for side effects - focus on immutability
8. Collectors are used for mutable reduction operations
9. Ordered streams guarantee order; unordered streams may optimize operations




### Method References
1. Syntax: `Class::methodName` or `instance::methodName`
2. Four types: static methods, instance methods of particular objects, instance methods of arbitrary objects, constructors
3. Must match the functional interface method signature
4. Constructor references use `ClassName::new`
5. Can't be used if additional logic is needed

### Optional
1. Container object that may or may not contain a non-null value
2. Never return `null` from `Optional` methods; return `Optional.empty()`
3. Avoid `Optional.get()` without checking `isPresent()`
4. Prefer `orElse()`, `orElseGet()`, or `orElseThrow()`
5. Don't use as method parameter or field type, only as return type
6. `orElseGet()` is lazy; `orElse()` always evaluates its argument
7. `map()` transforms value if present, `flatMap()` for when the transformation returns an Optional
8. Cannot be serialized




## Generics

1. Provide compile-time type safety for collections
2. Declared using angle brackets: `class Box<T>`
3. Type erasure: generic type information is removed at runtime
4. Cannot create arrays of generic types: `new T[]` is illegal
5. Cannot use primitives as type parameters (use wrapper classes)
6. Wildcard `?` represents an unknown type
7. Upper bounded wildcard: `<? extends Type>` accepts Type or its subtypes
8. Lower bounded wildcard: `<? super Type>` accepts Type or its supertypes
9. Unbounded wildcard: `<?>` accepts any type
10. PECS principle: "Producer Extends, Consumer Super"
11. Type parameters can have multiple bounds: `<T extends A & B & C>`
12. Static members cannot refer to class type parameters
13. Generic methods can be defined with their own type parameters
14. Cannot catch or throw generic exception types

## Collections Framework 

1. All collections implement `Collection` interface (except Map)
2. Main interfaces: `List`, `Set`, `Queue`, and `Map`
3. `List`: ordered collection with duplicates allowed
4. `Set`: collection with no duplicates
5. `Queue`: collection designed for holding elements for processing
6. `Map`: key-value pairs with unique keys
7. `ArrayList`: dynamic array implementation of List
8. `LinkedList`: doubly-linked list implementation of List and Queue
9. `HashSet`: hash table implementation of Set
10. `TreeSet`: sorted Set implementation using Red-Black Tree
11. `HashMap`: hash table implementation of Map
12. `TreeMap`: sorted Map implementation using Red-Black Tree
13. Use `Iterator` for traversing collections
14. `Collections` utility class provides useful algorithms
15. Use generics with collections for type safety
16. `Comparable` interface for natural ordering
17. `Comparator` interface for custom ordering
18. Fail-fast iterators throw `ConcurrentModificationException` if collection is modified during iteration
19. Prefer concurrent collections for multithreaded access

## I/O and NIO

1. `InputStream` and `OutputStream` for byte-based I/O
2. `Reader` and `Writer` for character-based I/O
3. Always close streams in finally blocks or use try-with-resources
4. Buffered streams improve performance
5. `ObjectInputStream` and `ObjectOutputStream` for object serialization
6. NIO provides non-blocking I/O operations
7. `Channel` for connections to entities capable of I/O operations
8. `Buffer` for data container used in channel operations
9. `Selector` allows a single thread to manage multiple channels
10. `Path` represents file system paths
11. `Files` utility class for file operations
12. Memory-mapped files provide faster I/O for large files
13. `StandardCharsets` defines standard character encodings
14. File locking allows coordination between processes
15. Asynchronous I/O operations supported via CompletableFuture

## Inner Classes

1. Four types: static nested, inner (non-static nested), local, and anonymous
2. Inner classes have access to all members of enclosing class
3. Static nested classes can only access static members of enclosing class
4. Local classes are defined in a block and can access final or effectively final local variables
5. Anonymous classes create instances of unnamed classes that implement an interface or extend a class
6. Inner class instances have implicit reference to enclosing instance
7. Inner classes cannot declare static members (except constants)
8. Nested classes can be declared with any access modifier
9. Local and anonymous classes can only access final or effectively final local variables
10. Anonymous classes cannot have constructors (they are implied)

## Reflection

1. Allows examination and modification of runtime behavior
2. Main classes: `Class`, `Field`, `Method`, `Constructor`
3. `Class.forName()` loads a class by name
4. `.class` syntax gets a Class object for a type
5. `object.getClass()` returns the runtime class of an object
6. Access private members using `setAccessible(true)`
7. Can create new instances via reflection using `newInstance()`
8. Can invoke methods with `invoke()`
9. Can access and modify fields with `get()` and `set()`
10. Performance impact: reflection is slower than direct code
11. Security considerations: can break encapsulation
12. Used by many frameworks like Spring, Hibernate
13. Cannot access information lost in type erasure
14. Can examine annotations at runtime

## Annotations

1. Start with `@` symbol, followed by annotation name
2. Defined using `@interface` keyword
3. Three types: marker (no elements), single-element, and multi-element
4. Can have default values: `String value() default ""`
5. Meta-annotations: annotations that apply to other annotations
6. `@Retention` determines annotation lifetime (SOURCE, CLASS, RUNTIME)
7. `@Target` specifies where annotation can be applied
8. `@Documented` includes annotations in Javadoc
9. `@Inherited` allows subclasses to inherit annotations
10. `@Repeatable` allows multiple annotations of same type
11. Access at runtime via Reflection API
12. Cannot extend other annotations or implement interfaces
13. Element types limited to primitives, String, Class, enum, annotation, and arrays of these
14. Often used for code generation, dependency injection, validation

## Java Memory Model

1. Defines how threads interact through memory
2. Guarantees for properly synchronized code
3. Happens-before relationship ensures memory visibility
4. Volatile guarantees visibility but not atomicity
5. Final fields are safely published if constructor doesn't leak this reference
6. Double-checked locking without volatile is broken
7. Memory barriers ensure ordering of operations
8. Out-of-order execution can be performed by JVM for optimization
9. Thread confinement avoids concurrency issues
10. Thread-local storage provides isolated per-thread data
11. Immutable objects are always thread-safe
12. Synchronized methods/blocks establish happens-before relationships
13. Lock acquisition/release creates memory barriers
14. CAS (Compare And Swap) operations are atomic

## Garbage Collection

1. Automatic memory management system
2. Objects eligible for GC when no references point to them
3. System.gc() is only a suggestion, not guaranteed
4. Four types of references: strong, soft, weak, and phantom
5. Strong references prevent garbage collection
6. Soft references cleared before OutOfMemoryError
7. Weak references cleared during next GC cycle
8. Phantom references allow cleanup before reclamation
9. finalize() method called before garbage collection (deprecated)
10. Try to avoid finalization due to performance impact
11. Memory leaks can occur in Java (e.g., keeping unneeded references)
12. Common GC algorithms: Serial, Parallel, CMS, G1, ZGC
13. GC tuning through JVM parameters
14. Use memory profilers to detect memory issues

## Java 9+ Features

1. **Module System (Java 9)**:
   - Declared with module-info.java
   - Explicit exports/requires statements
   - Strong encapsulation beyond packages
   - Reliable configuration with version conflicts detection

2. **Reactive Streams (Java 9)**:
   - Flow API for asynchronous stream processing
   - Publisher/Subscriber/Processor interfaces
   - Backpressure support
   - Non-blocking handling of asynchronous data streams

3. **JShell (Java 9)**:
   - Interactive REPL (Read-Eval-Print Loop)
   - Evaluates declarations, statements, and expressions
   - Auto-completion and command history
   - Snippet saving and loading

4. **Private interface methods (Java 9)**:
   - Support code reuse within interfaces
   - Cannot be used by implementing classes
   - Can be static or instance methods
   - Help organize default method implementations

5. **var keyword (Java 10)**:
   - Local variable type inference
   - Only for local variables with initializers
   - Cannot use for method parameters, return types, fields
   - Type is still static, just inferred by compiler

6. **Switch Expressions (Java 12-14)**:
   - Can return values
   - No fall-through with arrow syntax
   - Multiple case labels
   - Yield statement for complex blocks

7. **Text Blocks (Java 15)**:
   - Multi-line string literals
   - No escape sequences for most cases
   - Preserves formatting with control over whitespace
   - Can still use string concatenation and interpolation

8. **Records (Java 16)**:
   - Immutable data classes
   - Automatic getters, equals, hashCode, toString
   - Can declare additional methods
   - Cannot extend other classes (implicitly extends Record)

9. **Sealed Classes (Java 17)**:
   - Restrict which classes can extend/implement
   - Permits clause lists allowed subtypes
   - Subclasses must be final, sealed, or non-sealed
   - Works with records and interfaces

10. **Pattern Matching (Java 16+)**:
    - Enhanced instanceof with variable binding
    - Pattern matching in switch statements
    - Guards in patterns (when clause)
    - Record patterns and deconstruction

<br/>


## Serialization and Deserialization


### What is Serialization and Deserialization?

**Serialization:** The process of converting the state of an object into a byte stream. This byte stream can be saved to a file, sent over a network, or stored in a database.

**Deserialization:** The process of reconstructing an object from a byte stream that was previously serialized.

### Different Streams Available

Java provides various streams for handling byte and character data:

#### Byte Streams (for binary data):

* `FileInputStream` (FIS): Reads bytes from a file.
* `FileOutputStream` (FOAS): Writes bytes to a file.
* `ByteArrayInputStream`: Reads bytes from a byte array.
* `ByteArrayOutputStream`: Writes bytes to a byte array.
* `DataInputStream` (DIS): Reads primitive data types and strings in a machine-independent way.
* `DataOutputStream` (DOS): Writes primitive data types and strings in a machine-independent way.
* `ObjectInputStream` (OIS): Deserializes primitive data and objects written by `ObjectOutputStream`.
* `ObjectOutputStream` (OOS): Serializes primitive data and objects.
* `SequenceInputStream`: Concatenates multiple input streams into a single input stream.

#### Character Streams (for text data):

* `FileReader`: Reads characters from a file. Internally uses `InputStreamReader` with the default character encoding.
* `FileWriter`: Writes characters to a file. Internally uses `OutputStreamWriter` with the default character encoding.
* `CharArrayReader`: Reads characters from a `char` array.
* `CharArrayWriter`: Writes characters to a `char` array.
* `BufferedReader`: Reads text from a character input stream, buffering characters for efficient reading.
* `BufferedWriter`: Writes text to a character output stream, buffering characters for efficient writing.
* `PrintWriter`: Prints formatted representations of objects to a text-output stream.

### Comparison of Byte and Character Streams

| Feature             | Byte Streams                     | Character Streams                  |
| ------------------- | -------------------------------- | ---------------------------------- |
| Data Type           | Bytes (8-bit)                    | Characters (16-bit Unicode)        |
| Primary Use         | Binary data (images, audio, video, etc.) | Text data                          |
| Classes (Examples)  | `FileInputStream`, `FileOutputStream`, `DataInputStream`, `DataOutputStream` | `FileReader`, `FileWriter`, `BufferedReader`, `BufferedWriter` |
| Internal Conversion | None                             | Byte to character (encoding/decoding) |

## Advantages and Disadvantages of FIS, FOS, DIS, DOS, OIS, OOS

#### FileInputStream & FileOutputStream:

**Advantages:** Basic for reading and writing raw byte data to/from files.
**Disadvantages:** No built-in support for primitive data types or objects; requires manual handling of data formats.

#### DataInputStream & DataOutputStream:

**Advantages:** Provides methods for reading and writing primitive Java data types in a portable way.
**Disadvantages:** Only handles primitive types and strings; not for complex objects directly.

#### ObjectInputStream & ObjectOutputStream:

**Advantages:** Enables serialization and deserialization of entire Java objects, preserving their state and structure.
**Disadvantages:** Can be slower than basic byte streams; objects must implement `Serializable`; potential security risks if deserializing from untrusted sources.



