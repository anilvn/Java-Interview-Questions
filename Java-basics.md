# Java 8 Method References

Method references are a compact way to refer to methods of functional interfaces. They are essentially shorthand for lambda expressions when the lambda expression simply calls an existing method.

## Syntax

There are four types of method references:

1.  **Reference to a static method:** `ClassName::staticMethodName`
2.  **Reference to an instance method of a particular object:** `object::instanceMethodName`
3.  **Reference to an instance method of an arbitrary object of a particular type:** `ClassName::instanceMethodName`
4.  **Reference to a constructor:** `ClassName::new`

## Example 1: Reference to a Static Method

```java
interface Printable {
    void print(String message);
}

public class Main {
    public static void printMessage(String msg) {
        System.out.println(msg.toUpperCase());
    }

    public static void main(String[] args) {
        Printable printable = Main::printMessage; // Method reference to a static method
        printable.print("hello"); // Output: HELLO

        Printable printableLambda = (String msg) -> Main.printMessage(msg); // Equivalent lambda
        printableLambda.print("world"); // Output: WORLD
    }
}
