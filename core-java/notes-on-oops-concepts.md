

## Table of Contents

| #   | Question                                                                 |
|-----|--------------------------------------------------------------------------|
| 1   | [What is Association in OOP?](#what-is-association-in-oop)              |
| 2   | [What is Aggregation in OOP?](#what-is-aggregation-in-oop)              |
| 3   | [What is Composition in OOP?](#what-is-composition-in-oop)              |
| 4   | [Differences between Association, Aggregation, and Composition](#differences-between-association-aggregation-and-composition) |
| 5   | [Differences between is-a & has-a relationship](#notes-on-enum)                                          |
| 6   | [Java Abstraction, Interfaces, Inheritance & Diamond Problem](#java-abstraction-interfaces-inheritance--diamond-problem) |
| 7   | [How are Abstraction and Loose Coupling Achieved?](#1-how-are-abstraction-and-loose-coupling-achieved) |
| 8   | [What is the Use of Interfaces?](#2-what-is-the-use-of-interfaces) |
| 9   | [What is Inheritance and Types of Inheritance?](#3-what-is-inheritance-and-types-of-inheritance) |
| 10  | [Why is Multiple Inheritance Not Possible in Java?](#4-why-is-multiple-inheritance-not-possible-in-java) |
| 11  | [What is the Diamond Problem and How Does Java Solve It?](#5-what-is-the-diamond-problem-and-how-does-java-solve-it) |
| 12  | [Java Multiple Inheritance: Code Examples](#no-error-in-this-program) |

---


## 1. Association

Represents a "uses-a" or "has-a" relationship.
Objects can be related without ownership.
May be unidirectional or bidirectional.
Objects can exist independently.
UML Notation: Plain line

### Example: One-to-Many Association (Doctor ↔ Patients)

```java
class Patient {
    private String name;

    Patient(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class Doctor {
    private String name;

    Doctor(String name) {
        this.name = name;
    }

    void treat(Patient patient) {
        System.out.println(name + " is treating patient " + patient.getName());
    }
}

public class AssociationDemo {
    public static void main(String[] args) {
        Doctor doctor = new Doctor("Dr. Sharma");
        Patient p1 = new Patient("John");
        Patient p2 = new Patient("Sara");

        // One-to-Many: A doctor can treat many patients
        doctor.treat(p1);
        doctor.treat(p2);
    }
}
```

Additional Examples:

* One-to-One: A person has one passport.
* Many-to-One: Many students belong to one school.
* Many-to-Many: Students enroll in many courses and vice versa.

---

## 2. Aggregation

Represents a "has-a" relationship.
Weak association – the child can exist independently.
Implemented via object reference.
Represented by a hollow diamond in UML diagrams.

### Example: Aggregation (Team has Players)

```java
import java.util.List;
import java.util.ArrayList;

class Player {
    private String name;

    Player(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class Team {
    private String teamName;
    private List<Player> players;

    Team(String teamName) {
        this.teamName = teamName;
        this.players = new ArrayList<>();
    }

    void addPlayer(Player player) {
        players.add(player); // Aggregation: Player is referenced but not owned
    }

    void showTeam() {
        System.out.println("Team: " + teamName);
        for (Player p : players) {
            System.out.println("- " + p.getName());
        }
    }
}

public class AggregationDemo {
    public static void main(String[] args) {
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");

        Team team = new Team("Warriors");
        team.addPlayer(p1);
        team.addPlayer(p2);

        team.showTeam();

        // Players continue to exist even if the Team is disbanded
    }
}
```

---

## 3. Composition

Composition is a strong form of aggregation, where one class owns another class and is responsible for its lifecycle.
Represents a "part-of" or strong has-a relationship.
Strong association – the child cannot exist without the parent.
Represented by a solid diamond in UML diagrams.

### Example: Composition (Car has Engine)

```java
class Engine {
    private String type;

    Engine(String type) {
        this.type = type;
    }

    void start() {
        System.out.println("Engine of type " + type + " started.");
    }
}

class Car {
    private Engine engine;

    Car() {
        engine = new Engine("V8"); // Composition: Engine created and owned by Car
    }

    void drive() {
        engine.start();
        System.out.println("Car is driving.");
    }
}

public class CompositionDemo {
    public static void main(String[] args) {
        Car car = new Car();
        car.drive();

        // If the Car is destroyed, the Engine is also destroyed
    }
}
```

---

## Differences between Association, Aggregation, and Composition

| Feature      | Association          | Aggregation                   | Composition                      |
| ------------ | -------------------- | ----------------------------- | -------------------------------- |
| Type         | General relationship | Weak "has-a"                  | Strong "has-a"                   |
| Dependency   | No dependency        | Child can exist independently | Child cannot exist independently |
| Lifecycle    | Independent          | Independent                   | Dependent                        |
| UML Notation | Plain Line           | Hollow diamond                | Solid diamond                    |
| Example      | Student ↔ School     | Department → Professor        | House → Room                     |

---
## Comparison between is-a & has-a Relationship

| Feature            | is-a Relationship         | has-a Relationship             |
| ------------------ | ------------------------- | ------------------------------ |
| Concept            | Inheritance               | Composition/Aggregation        |
| Implementation     | `extends` / `implements`  | Class references another class |
| Relationship       | Child is a type of Parent | One class contains another     |
| Example            | Dog **is an** Animal      | Car **has a** Engine           |
| Design Flexibility | Less flexible             | More flexible                  |

---





<br/>

## Java: Abstraction, Interfaces, Inheritance & Diamond Problem

## 1. How are Abstraction and Loose Coupling Achieved?

### Abstraction
**Definition**: Hiding implementation details while showing only essential features to the user.

**Achieved through**:
- **Abstract Classes**: Classes that cannot be instantiated and may contain abstract methods
- **Interfaces**: Contracts that define what a class must do, not how it does it
- **Access Modifiers**: Control visibility of class members

**Example**:
```java
// Abstract class providing abstraction
abstract class Vehicle {
    abstract void start();  // What to do (not how)
    
    public void stop() {    // Common implementation
        System.out.println("Vehicle stopped");
    }
}

class Car extends Vehicle {
    void start() {          // How to do it
        System.out.println("Car engine started");
    }
}
```

### Loose Coupling
**Definition**: Reducing dependencies between classes/modules so changes in one don't heavily impact others.

**Achieved through**:
- **Interfaces**: Program to interfaces, not implementations
- **Dependency Injection**: Provide dependencies from outside
- **Abstract Classes**: Use abstract types instead of concrete classes

**Example**:
```java
// Loose coupling with interfaces
interface PaymentProcessor {
    void processPayment(double amount);
}

class CreditCardProcessor implements PaymentProcessor {
    public void processPayment(double amount) {
        // Credit card logic
    }
}

class PayPalProcessor implements PaymentProcessor {
    public void processPayment(double amount) {
        // PayPal logic
    }
}

class OrderService {
    private PaymentProcessor processor;
    
    // Loosely coupled - can work with any payment processor
    public OrderService(PaymentProcessor processor) {
        this.processor = processor;
    }
    
    public void processOrder(double amount) {
        processor.processPayment(amount);
    }
}
```

## 2. What is the Use of Interfaces?

### Primary Uses:

1. **Achieve Multiple Inheritance**: Java classes can implement multiple interfaces
2. **Contract Definition**: Define what methods a class must implement
3. **Abstraction**: Hide implementation details
4. **Loose Coupling**: Reduce dependencies between classes
5. **Polymorphism**: Treat different objects uniformly
6. **API Design**: Define consistent APIs across different implementations

### Practical Examples:

**Example 1: Multiple Inheritance of Type**
**Example 2: Polymorphism**


## 3. What is Inheritance and Types of Inheritance?

### Inheritance
**Definition**: Mechanism where a new class (child/subclass) inherits properties and methods from an existing class (parent/superclass).

**Benefits**:
- Code reusability
- Method overriding
- Polymorphism
- Hierarchical classification

### Types of Inheritance:

#### 1. Single Inheritance
One class inherits from one parent class.
```java
class Animal {
    void eat() { System.out.println("Animal eats"); }
}

class Dog extends Animal {
    void bark() { System.out.println("Dog barks"); }
}
```

#### 2. Multilevel Inheritance
Chain of inheritance (grandparent → parent → child).
```java
class Animal {
    void breathe() { System.out.println("Breathing"); }
}

class Mammal extends Animal {
    void warmBlooded() { System.out.println("Warm blooded"); }
}

class Dog extends Mammal {
    void bark() { System.out.println("Barking"); }
}
```

#### 3. Hierarchical Inheritance
Multiple classes inherit from one parent class.
```java
class Animal {
    void move() { System.out.println("Animal moves"); }
}

class Dog extends Animal {
    void bark() { System.out.println("Dog barks"); }
}

class Cat extends Animal {
    void meow() { System.out.println("Cat meows"); }
}
```

#### 4. Multiple Inheritance (NOT supported in Java for classes)
One class inheriting from multiple parent classes.
```java
// NOT POSSIBLE in Java with classes
// class Child extends Parent1, Parent2 { } // COMPILATION ERROR

// But possible with interfaces
interface Interface1 { void method1(); }
interface Interface2 { void method2(); }
class Child implements Interface1, Interface2 {
    public void method1() { }
    public void method2() { }
}
```

#### 5. Hybrid Inheritance (NOT directly supported)
Combination of multiple inheritance types.

## 4. Why is Multiple Inheritance Not Possible in Java?

### The Ambiguity Problem

**Reasons**:
1. **Diamond Problem**: Ambiguity when same method exists in multiple parent classes
2. **Complexity**: Makes the language complex and error-prone
3. **Compilation Issues**: Compiler cannot decide which method to inherit

**Example of the Problem**:
```java
// This is NOT valid Java code - just for illustration
class A {
    void display() { System.out.println("A"); }
}

class B extends A {
    void display() { System.out.println("B"); }
}

class C extends A {
    void display() { System.out.println("C"); }
}

// If this were allowed (IT'S NOT):
class D extends B, C {  // COMPILATION ERROR
    // Which display() method should D inherit?
    // B's display() or C's display()?
    // This creates ambiguity!
}
```

**Java's Solution**: Use interfaces for multiple inheritance of type, not implementation.

## 5. What is the Diamond Problem and How Does Java Solve It?

### Diamond Problem
**Definition**: Occurs when a class inherits from two classes that have a common base class, creating ambiguity about which version of inherited methods to use.

**Visual Representation**:
```
     A
   /   \
  B     C
   \   /
     D
```

### The Problem Scenario:
```java
// Hypothetical scenario (NOT valid Java)
class A {
    void method() { System.out.println("A"); }
}

class B extends A {
    void method() { System.out.println("B"); }
}

class C extends A {
    void method() { System.out.println("C"); }
}

// IF multiple inheritance were allowed:
class D extends B, C {  // NOT ALLOWED
    // Which method() should be inherited?
    // Ambiguity: B's method() or C's method()?
}
```

### Java's Solutions:

#### 1. No Multiple Inheritance for Classes
Java simply doesn't allow multiple class inheritance.

#### 2. Multiple Inheritance with Interfaces

When diamond problem occurs with interfaces:
 - Interface Default Methods (Java 8+)

```java
interface A {
    default void method() { System.out.println("A"); }
}

interface B extends A {
    default void method() { System.out.println("B"); }
}

interface C extends A {
    default void method() { System.out.println("C"); }
}

class D implements B, C {
    // Must resolve the conflict explicitly
    public void method() {
        B.super.method(); // Explicitly call B's method
        // or C.super.method(); // or C's method
        // or provide own implementation
    }
}
```
### Key Points:
- **Classes**: No multiple inheritance allowed - prevents diamond problem
- **Interfaces**: Multiple inheritance allowed, but conflicts must be resolved explicitly
- **Compiler Enforcement**: Java compiler forces explicit resolution of ambiguous situations
- **Clean Design**: Encourages better design patterns and loose coupling


## no error in this program

```java

interface EarthLike {
    void Rotation();  // Abstract method (no default keyword)
}

interface MarsLike {
    void Rotation();  // Same abstract method signature
}

class SatelliteBody implements EarthLike, MarsLike {
    @Override
    public void Rotation() {
        System.out.println("Resolved ambiguity: Satellite rotates uniquely.");
    }
}

public class Main {
    public static void main(String[] args) {
        SatelliteBody satellite = new SatelliteBody();
        satellite.Rotation();  // Output: Resolved ambiguity: Satellite rotates uniquely.
    }
}

```
## code will shows error here

```java
class A {
    void method() { System.out.println("A"); }
}

abstract class B extends A {
    abstract void method2();
}

abstract class C extends A {
    abstract void method2();
}

class D extends B, C {  // ❌ NOT ALLOWED
    // Which method() should be inherited?
}


```


## valid code only
```java
abstract class B {
    void common() {
        System.out.println("B");
    }
}

interface C {
    void extra();
}

class D extends B implements C {
    public void extra() {
        System.out.println("C's method");
    }
}

```

```java
interface MyInterface {
    static void staticMethod() {
        System.out.println("Static method in interface");
    }
}

class MyClass implements MyInterface {
    // No access to staticMethod() directly
}

public class Test {
    public static void main(String[] args) {
        // MyClass.staticMethod();  ❌ Compile-time error
        // new MyClass().staticMethod(); ❌ Compile-time error

        MyInterface.staticMethod(); // ✅ Correct usage
    }
}

```

```java
// Parent class with a static method
class Parent {
    static void greet() { // Static method in Parent
        System.out.println("Hello from Parent");
    }
}

// Child class that hides the static method of Parent
class Child extends Parent {
    static void greet() { // Static method in Child (hides Parent's method, doesn't override it)
        System.out.println("Hello from Child");
    }
}

public class Test {
    public static void main(String[] args) {
        Parent p = new Child(); // Reference is of type Parent, object is of type Child
        p.greet(); // Calls Parent.greet() because static methods are resolved at compile-time by reference type
                  // This is method hiding, not overriding. So output will be: Hello from Parent
    }
}

```