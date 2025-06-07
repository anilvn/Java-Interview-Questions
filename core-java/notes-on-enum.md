# Java Enums - Complete Notes

## What are Enums?

1. Special type of class that represents a group of constants
2. Declared using the `enum` keyword
3. All enum constants are implicitly `public`, `static`, and `final`
4. Cannot use `new` to instantiate an enum
5. Can implement interfaces but cannot extend other classes
6. Can contain constructors, methods, and fields
7. Constructor must be private (implicitly if not specified)
8. Can contain abstract methods, but all constants must implement them
9. Can have instance-specific behavior by providing method implementations in constant definitions
10. Implicitly extends `java.lang.Enum`
11. Has special methods like `values()`, `valueOf()`, `ordinal()`, and `name()`
12. Can be used in switch statements
13. Thread-safe singleton implementation
14. Can be used with `EnumSet` and `EnumMap` for efficient operations

## Advantages of Using Enums

1. **Type safety**: Restricts variable to have only predefined values
2. **Readable code**: Improves clarity with named constants
3. **Singleton-like behavior**: Each enum constant is a singleton, ideal for shared constants
4. **Can have fields and methods**: Enums can have constructors, fields, and methods for complex behavior
5. **Switch-case friendly**: Works seamlessly with `switch` statements
6. **Built-in serialization support**: Enums are serializable by default

## Limitations of Enums

1. **Cannot extend classes**: Enums implicitly extend `java.lang.Enum`, so they can't extend any other class
2. **Fixed constants**: Enum constants are fixed at compile time—no dynamic addition
3. **Not suitable for all use cases**: Enums are ideal for fixed sets; dynamic or frequently changing values aren't a good fit
4. **Cannot clone**: Enum instances can't be cloned
5. **Limited flexibility**: Overengineering for simple constants if not used wisely

## Method Overriding in Enums

### Can enums override methods from `java.lang.Enum`?
**No**, enums **cannot override** methods from `java.lang.Enum` because it's a **final class**.

### Can enums override methods from `java.lang.Object`?
**Yes**, enums in Java **can override methods from `java.lang.Object`**, such as:
- `toString()`
- `equals()`
- `hashCode()`
- `clone()` (though enums can't be cloned)
- `finalize()` (deprecated)

Example:
```java
enum Color {
    RED, GREEN, BLUE;

    @Override
    public String toString() {
        return "Color: " + name();
    }
}
```

## Serialization and Deserialization

1. Enums are **serializable by default**
2. Use `ObjectOutputStream` and `ObjectInputStream` for serialization and deserialization
3. In JSON (e.g., Jackson), just use `@JsonProperty` or default mapping works out-of-the-box

## Examples

### Example 1: Basic Enum Usage

```java
import java.util.EnumSet;
import java.util.EnumMap;
import java.util.Map;

public class EnumExample {

    // Define enum Day with constants
    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public static void main(String[] args) {
        // 1. values() - Get all enum constants
        System.out.println("All days:");
        for (Day day : Day.values()) {
            System.out.print(day + " ");
        }
        System.out.println("\n");

        // 2. valueOf() - Convert string to enum constant
        Day d = Day.valueOf("MONDAY");
        System.out.println("valueOf(\"MONDAY\") returns: " + d);  // valueOf("MONDAY") returns: MONDAY

        // 3. ordinal() - Get index of enum constant
        System.out.println("Ordinal of MONDAY: " + Day.MONDAY.ordinal()); // 0
        System.out.println("Ordinal of SUNDAY: " + Day.SUNDAY.ordinal()); // 6

        // 4. name() - Get exact name of enum constant
        System.out.println("Name of Day.FRIDAY: " + Day.FRIDAY.name());

        System.out.println();

        // Using EnumSet for weekend days
        EnumSet<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);
        System.out.println("Weekend days using EnumSet:");
        for (Day day : weekend) {
            System.out.print(day + " ");
        }
        System.out.println("\n");

        // Using EnumMap to map days to tasks
        EnumMap<Day, String> tasks = new EnumMap<>(Day.class);
        tasks.put(Day.MONDAY, "Write report");
        tasks.put(Day.FRIDAY, "Submit report");

        System.out.println("Tasks mapped to days using EnumMap:");
        for (Map.Entry<Day, String> entry : tasks.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```

### Example 2: Enum with Abstract Methods

```java 
enum Operation {
    ADD {
        @Override
        public int apply(int a, int b) {
            return a + b;
        }
    },
    SUBTRACT {
        @Override
        public int apply(int a, int b) {
            return a - b;
        }
    },
    MULTIPLY {
        @Override
        public int apply(int a, int b) {
            return a * b;
        }
    };

    // Abstract method
    public abstract int apply(int a, int b);
}

//main
public class Main {
    public static void main(String[] args) {
        int a = 5, b = 3;

        System.out.println(Operation.ADD.apply(a, b));        // 8
        System.out.println(Operation.SUBTRACT.apply(a, b));   // 2
        System.out.println(Operation.MULTIPLY.apply(a, b));   // 15
    }
}
```





### Example: Main Method in Different Structures

```java
class A {
    public static void main(String[] s) {
        System.out.println("A-main");
    }
}

abstract class B {
    public static void main(String[] s) {
        System.out.println("B-main");
    }
}

enum C {
    ;  // Empty enum body
    public static void main(String[] s) {
        System.out.println("C-main");
    }
}

interface K {
    public static void main(String[] s) {
        System.out.println("K-main");
    }
}
```

> All of these classes can be compiled and their main methods can be executed.

[Back to Top](#table-of-contents)