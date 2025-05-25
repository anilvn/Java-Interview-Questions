## Java Concepts - Questions & Answers

## Main Method Concepts

**1. Why is the main method static in Java?**
The main method is static because the JVM needs to call it without creating an instance of the class. Static methods belong to the class itself, not to any object.

**2. Can we run main() without the static keyword?**
No, the JVM specifically looks for `public static void main(String[] args)`. Without static, you'll get a runtime error.

**3. What if we write static public void instead of public static void?**
Both are valid. The order of access modifiers and static keyword doesn't matter in Java. `static public void main` works the same as `public static void main`.

**4. Can you declare the main method as final or private?**
- **Final**: Yes, but it's unnecessary since main isn't typically overridden
- **Private**: No, the JVM requires main to be public to access it

## Class Execution

**5. Can we run a Java class without a main method?**
- In standalone applications: No, you need main method to start execution
- In web applications or when called by other classes: Yes, other methods can be invoked

**6. Can you declare a class as final?**
Yes, final classes cannot be extended (inherited). Examples: String, Integer, all wrapper classes.

## Static vs Non-Static

**7. Can a static method reference a non-static variable?**
No, static methods cannot directly access non-static (instance) variables because static methods belong to the class, while instance variables belong to specific objects. You'll get a compilation error.

**8. Can we override static variables?**
No, static variables cannot be overridden. They can be hidden in subclasses, but this is called "variable hiding," not overriding. Each class has its own copy of static variables.

## Keywords and Identifiers

**9. Is 'main' a keyword in Java? What are other identifiers?**
No, 'main' is not a keyword - it's an identifier (method name). 

**Java Keywords include**: class, public, static, void, if, else, for, while, int, boolean, final, abstract, extends, implements, try, catch, etc.

**Identifiers are**: names given to variables, methods, classes, packages (like main, System, println, etc.)