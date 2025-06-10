# Java Exception Handling - Complete Interview Notes

## 1. CORE EXCEPTION HANDLING CONCEPTS

### What is an Exception?
- An **exception** is an unwanted or unexpected event that occurs during program execution
- It disrupts the normal flow of program instructions
- Represents an error condition that a program might want to catch and handle
- Examples: `NullPointerException`, `FileNotFoundException`, `ArrayIndexOutOfBoundsException`

### Difference Between Errors and Exceptions

| **Errors** | **Exceptions** |
|------------|----------------|
| Serious problems that applications shouldn't catch | Problems that applications can catch and handle |
| Usually irrecoverable | Can be recovered from |
| Examples: `OutOfMemoryError`, `StackOverflowError` | Examples: `IOException`, `SQLException` |
| Occur at JVM level | Occur at application level |
| Cannot be handled by normal exception handling | Can be handled using try-catch blocks |

### Exception Hierarchy in Java

```
java.lang.Object
    └── java.lang.Throwable
        ├── java.lang.Error
        │   ├── OutOfMemoryError
        │   ├── StackOverflowError
        │   └── VirtualMachineError
        └── java.lang.Exception
            ├── IOException (Checked)
            ├── SQLException (Checked)
            ├── ClassNotFoundException (Checked)
            └── RuntimeException (Unchecked)
                ├── NullPointerException
                ├── ArrayIndexOutOfBoundsException
                ├── IllegalArgumentException
                └── NumberFormatException
```

### Checked vs Unchecked Exceptions

**Checked Exceptions:**
- Must be declared in method signature using `throws` keyword
- Must be handled using try-catch or declared to be thrown
- Checked at compile-time
- Examples: `IOException`, `SQLException`, `ClassNotFoundException`

**Unchecked Exceptions:**
- Do not need to be declared or caught
- Subclasses of `RuntimeException`
- Checked at runtime
- Examples: `NullPointerException`, `ArrayIndexOutOfBoundsException`

### Handled vs Unhandled Exceptions
- **Handled**: Exceptions caught and processed using try-catch blocks
- **Unhandled**: Exceptions not caught, causing program termination with stack trace

### Runtime vs Compile-time Exceptions
- **Compile-time**: Checked exceptions that must be handled before compilation
- **Runtime**: Unchecked exceptions that occur during program execution

### Exception Propagation
- When an exception occurs, it propagates up the call stack
- Each method in the call stack gets a chance to handle it
- If no method handles it, the program terminates
- Exception moves from called method to calling method

```
main() → methodA() → methodB() → methodC() → Exception occurs here

methodC does not handle it → propagates to methodB
methodB does not handle it → propagates to methodA
methodA does not handle it → propagates to main
main does not handle it → JVM handles → program terminates
```
- ex with java code output:
```
Exception in thread "main" java.lang.ArithmeticException: / by zero
	at ExceptionPropagationDemo.methodC(ExceptionPropagationDemo.java:4)
	at ExceptionPropagationDemo.methodB(ExceptionPropagationDemo.java:8)
	at ExceptionPropagationDemo.methodA(ExceptionPropagationDemo.java:12)
	at ExceptionPropagationDemo.main(ExceptionPropagationDemo.java:16)
```

### Throwable Class
- Root class for all exceptions and errors
- Provides common methods like `getMessage()`, `printStackTrace()`, `getCause()`
- Two main subclasses: `Error` and `Exception`

### Important Classes and Methods

**Key Classes:**
- `Throwable` - Root class
- `Exception` - Base for all exceptions
- `RuntimeException` - Base for unchecked exceptions
- `Error` - Base for all errors

**Important Methods:**
- `getMessage()` - Returns exception message
- `printStackTrace()` - Prints stack trace
- `getCause()` - Returns cause of exception
- `toString()` - String representation of exception

## 2. EXCEPTION HANDLING MECHANISMS

### Try-Catch-Finally Block Structure

```java
try {
    // Code that might throw exception
} catch (SpecificException e) {
    // Handle specific exception
} catch (Exception e) {
    // Handle general exception
} finally {
    // Always executed (cleanup code)
}
```

### When to Use Each Block

**Try:**
- Wrap code that might throw exceptions
- Contains risky operations

**Catch:**
- Handle specific exceptions
- Provide recovery logic
- Log errors

**Finally:**
- Cleanup resources (close files, connections)
- Always executes (except System.exit())
- Use for guaranteed cleanup

**Finalize:**
- Deprecated method from Object class
- Called by garbage collector (unreliable)
- Don't use for cleanup - use try-with-resources instead

### Exception in Finally Block
- If exception occurs in finally block, it suppresses the original exception
- Original exception becomes suppressed exception
- Finally block exception becomes the main exception
### Java Program Example:

```java
public class FinallyExceptionDemo {

    public static void main(String[] args) {
        try {
            methodWithException();
        } catch (Exception e) {
            System.out.println("Main exception: " + e);
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("Suppressed exception: " + suppressed);
            }
        }
    }

    static void methodWithException() throws Exception {
        try {
            System.out.println("Inside try block");
            throw new Exception("Exception from try block");
        } finally {
            System.out.println("Inside finally block");
            throw new Exception("Exception from finally block");
        }
    }
}
```

🧾 Output:

```
Inside try block
Inside finally block
Main exception: java.lang.Exception: Exception from finally block
Suppressed exception: java.lang.Exception: Exception from try block
```

🎯 Explanation:

* In try: we throw Exception("Exception from try block")
* In finally: we throw Exception("Exception from finally block")
* The finally block's exception becomes the primary exception.
* The try block's exception becomes a suppressed exception.
  - A suppressed exception is an exception that was thrown but not allowed to propagate because another exception took priority.

📚 When is this important?

* When debugging — the stack trace may hide the root cause.
* In AutoCloseable (like try-with-resources), suppressed exceptions may happen when closing the resource fails, and the main logic also throws.

---

### Return Statement in Finally Block
- Finally block can contain return statement
- It will override return values from try/catch blocks
- **Bad practice** - avoid return in finally blocks

```java
public int method() {
    try {
        return 1;
    } finally {
        return 2; // This value will be returned (2)
    }
}
```

### Throw vs Throws

| **throw** | **throws** |
|-----------|------------|
| Used to explicitly throw an exception | Used to declare exceptions in method signature |
| Used inside method body | Used in method declaration |
| Can throw only one exception at a time | Can declare multiple exceptions |
| `throw new Exception("message");` | `public void method() throws IOException, SQLException` |

### Multiple Catch Blocks
- Can have multiple catch blocks for different exception types
- Must be ordered from specific to general
- Only one catch block executes

```java
try {
    // risky code
} catch (FileNotFoundException e) {
    // Handle file not found
} catch (IOException e) {
    // Handle other IO exceptions
} catch (Exception e) {
    // Handle any other exception
}
```

### Nested Try-Catch Blocks
- Try-catch blocks can be nested inside other try-catch blocks
- Inner exceptions are handled first
- Useful for handling exceptions at different levels

```java
try {
    try {
        // Inner risky code
    } catch (SpecificException e) {
        // Handle inner exception
    }
} catch (Exception e) {
    // Handle outer exception
}
```

### Try-with-Resources
- Automatic resource management
- Resources must implement `AutoCloseable` interface
- Resources are automatically closed after try block

```java
try (FileInputStream fis = new FileInputStream("file.txt");
     BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
    // Use resources
} catch (IOException e) {
    // Handle exception
}
// Resources automatically closed here
```

### Statements Between Try and Catch
- **NO** - Cannot have statements between try and catch blocks
- Catch block must immediately follow try block
- Finally block can come after catch blocks

## 3. ADVANCED EXCEPTION HANDLING

### Multi-Catch (Catching Multiple Exceptions)
- Single catch block can handle multiple exception types
- Available from Java 7+
- Exceptions must not have inheritance relationship

```java
try {
    // risky code
} catch (IOException | SQLException | ClassNotFoundException e) {
    // Handle multiple exceptions
    System.out.println(e.getMessage());
}
```
- Limitations of Multi-Catch in Java
```java
try {
            riskyOperation(); // May throw IOException or SQLException
        } catch (IOException | SQLException e) {
            // This block handles both IOException and SQLException
            // The exception variable 'e' is effectively final, so you cannot reassign it
            System.out.println("Exception caught: " + e.getMessage());

            // catch (IOException | FileNotFoundException e) { } // Compile error: FileNotFoundException is a subclass of IOException
            
            // e = new IOException(); // Not allowed — exception variables in multi-catch are effectively final

            // If you want type-specific handling, use instanceof checks (less preferred)
            if (e instanceof SQLException) {
                // Handle SQL-related logic
            }

        }
    }
```

### Custom Exceptions
- Create application-specific exceptions
- Extend `Exception` (checked) or `RuntimeException` (unchecked)
- Provide meaningful names and messages

```java
// Custom checked exception
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

// Custom unchecked exception
class InvalidAccountNumberException extends RuntimeException {
    public InvalidAccountNumberException(String message) {
        super(message);
    }
}
```

### Exception Performance Impact
- Exception handling has performance overhead
- Creating exceptions is expensive (stack trace creation)
- Use exceptions for exceptional conditions, not control flow
- Catching exceptions is relatively cheap
- Avoid exceptions in loops or frequently called methods

### Exceptions in Constructors
- Constructors can throw exceptions
- If constructor throws exception, object is not created
- Use to validate constructor parameters

```java
public class Person {
    public Person(String name) throws InvalidNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("Name cannot be null or empty");
        }
        this.name = name;
    }
}
```

### Exceptions in Static Blocks
- Static blocks can throw exceptions
- If exception occurs, class loading fails
- Wrapped in `ExceptionInInitializerError`

```java
class MyClass {
    static {
        if (someCondition) {
            throw new RuntimeException("Static block failed");
        }
    }
}
```

### Try Block Without Catch
- Legal to have try without catch if finally is present
- Also legal with try-with-resources
- Exception will propagate up if not caught

```java
// Valid - try with finally
try {
    // code
} finally {
    // cleanup
}

// Valid - try-with-resources
try (Resource r = new Resource()) {
    // use resource
}
```

### Unhandled Exceptions
- Program terminates with stack trace
- Exception propagates up to main method
- If main doesn't handle it, JVM terminates program
- Thread terminates if exception not handled in thread

### Exception Suppression
- When try-with-resources is used, close() exceptions are suppressed
- Primary exception from try block is thrown
- Suppressed exceptions accessible via `getSuppressed()` method

### Multiple Try-with-Resources
- Can declare multiple resources in single try-with-resources
- Resources closed in reverse order of declaration

```java
try (FileInputStream fis = new FileInputStream("input.txt");
     FileOutputStream fos = new FileOutputStream("output.txt");
     BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
    // Use all resources
}
// Resources closed in order: br, fos, fis
```

## 4. RETHROWING EXCEPTIONS

### What is Rethrowing?
- Catching an exception and throwing it again
- Allows intermediate processing before propagation
- Can throw same exception or wrap in different exception

### Basic Rethrowing
```java
public void method() throws IOException {
    try {
        // risky operation
    } catch (IOException e) {
        // Log the exception
        logger.error("IO Error occurred", e);
        throw e; // Rethrow same exception
    }
}
```

### Rethrowing Checked Exceptions
- From Java 7+, can rethrow checked exceptions without declaring them if:
  - Exception is caught in catch block
  - Exception is not modified in catch block
  - Try block can only throw that specific exception type

```java
public void method() { // No throws declaration needed
    try {
        methodThatThrowsIOException();
    } catch (Exception e) {
        doSomething();
        throw e; // Can rethrow without declaring
    }
}
```

### Catching and Rethrowing Different Exception
- Can catch one exception type and throw another
- Must declare the new exception type in throws clause
- Useful for exception translation

```java
public void method() throws ServiceException {
    try {
        databaseOperation();
    } catch (SQLException e) {
        throw new ServiceException("Database operation failed", e);
    }
}
```

### Preserving Stack Trace
- **Important**: Always preserve original stack trace when rethrowing

```java
// WRONG - loses original stack trace
catch (SQLException e) {
    throw new ServiceException("Database error");
}

// CORRECT - preserves stack trace
catch (SQLException e) {
    throw new ServiceException("Database error", e);
}
```
## example-01
```java
public void readFile(String fileName) throws IOException {
    try {
        Files.readAllLines(Path.of(fileName));
    } catch (IOException e) {
        System.out.println("Logging: Failed to read file: " + fileName);
        throw e;  // 👈 Rethrowing the same exception
    }
}
```
```java
public void readFile(String fileName) {
    try {
        Files.readAllLines(Path.of(fileName));
    } catch (IOException e) {
        // Wrap original exception in a custom one
        throw new RuntimeException("Unable to read file: " + fileName, e);  // 👈 wrapping
    }
}
```
## ✅ Conclusion

| Style                                  | Can trace root cause? | Stack trace shows?              | Safe to use? |
| -------------------------------------- | --------------------- | ------------------------------- | ------------ |
| `throw e;`                             | ✅ Yes                 | Original exception and location | ✅ Yes        |
| `throw new RuntimeException("msg", e)` | ✅ Yes                 | Your message + original cause   | ✅ Yes        |
| `throw new RuntimeException("msg")`    | ❌ No                  | Only your message               | ❌ No         |

---

### Throw e vs Throw new Exception(e)

| **throw e** | **throw new Exception(e)** |
|-------------|----------------------------|
| Rethrows exact same exception | Creates new exception with original as cause |
| Preserves original stack trace | New stack trace starts from this point |
| Same exception type | Potentially different exception type |
| `e.printStackTrace()` shows original trace | `e.getCause().printStackTrace()` for original |

```java
// throw e - preserves everything
try {
    riskyMethod();
} catch (IOException e) {
    throw e; // Same exception, same stack trace
}

// throw new Exception(e) - wraps exception
try {
    riskyMethod();
} catch (IOException e) {
    throw new RuntimeException("Wrapped IO exception", e);
}
```

### Comparision between different methods to print/log exception details 

| **Method**              | **Description**                                                                                                                       | **Output Example**                                                                            | **Output**                 | **Includes Stack Trace?** | **Usefulness** | **Notes**                                       | **When to Use**                      |
| ----------------------- | ------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------- | -------------------------- | ------------------------- | -------------- | ----------------------------------------------- | ------------------------------------ |
| `printStackTrace()`     | Prints the stack trace of the exception to the **standard error stream**. Provides details like class, file, method, and line number. | Exception in thread "main" java.lang.ArithmeticException: / by zero at Main.main(Main.java:5) | Full stack trace to stderr | ✅ Yes                     | Very High      | Shows complete stack trace for debugging.       | Use during development to debug.     |
| `toString()`            | Returns a string with the exception class name and message.                                                                           | java.lang.ArithmeticException: / by zero                                                      | ExceptionClass: message    | ❌ No                      | Medium         | Quick, readable exception summary.              | Good for logging, not user display.  |
| `getMessage()`          | Returns only the error message.                                                                                                       | / by zero                                                                                     | Message only               | ❌ No                      | Low            | Simple message text only.                       | For user-friendly error messages.    |
| `getCause()`            | Returns the underlying cause of the exception, if present.                                                                            | java.lang.NullPointerException: Cause of the exception                                        | Cause of exception         | ❌ Partial                 | Medium         | Helps trace nested exceptions.                  | When analyzing wrapped exceptions.   |
| `getStackTrace()`       | Returns an array of stack trace elements (class, method, line number).                                                                | \[Ljava.lang.StackTraceElement;@15db9742                                                      | Stack trace as array       | Partial                   | Medium         | Raw stack trace data, needs formatting.         | When manually logging or formatting. |
| `getLocalizedMessage()` | Returns a localized message if supported by the exception.                                                                            | Arithmetic error occurred (localized message)                                                 | Localized error message    | ❌ No                      | Low-Medium     | Provides message in user's locale if supported. | For internationalized applications.  |

---
## 5. EXCEPTION HANDLING AND INHERITANCE

### Exception Handling in Method Overriding

**Rule**: Overriding method cannot throw broader checked exceptions than the parent method.

**Key Principles:**
1. Can throw **same** or **narrower** checked exceptions
2. Can throw **any unchecked** exceptions
3. Can throw **no exceptions** at all
4. Cannot throw **broader** or **new** checked exceptions

```java
class Parent {
    public void method() throws IOException {
        // parent method
    }
}

class Child extends Parent {
    // VALID - same exception
    public void method() throws IOException {
        // implementation
    }
    
    // VALID - narrower exception (FileNotFoundException extends IOException)
    public void method() throws FileNotFoundException {
        // implementation
    }
    
    // VALID - no exception
    public void method() {
        // implementation
    }
    
    // INVALID - broader exception (Exception is broader than IOException)
    // public void method() throws Exception { } // Compilation error
    
    // VALID - unchecked exceptions are allowed
    public void method() throws IOException, RuntimeException {
        // implementation
    }
}
```

### Can Subclass Throw New Checked Exception?

**NO** - Subclass cannot throw new checked exceptions not declared in parent method.

```java
class Parent {
    public void process() throws IOException {
        // parent method
    }
}

class Child extends Parent {
    // INVALID - SQLException is not thrown by parent
    // public void process() throws IOException, SQLException { } // Compilation error
    
    // VALID - Can add unchecked exceptions
    public void process() throws IOException, IllegalArgumentException {
        // implementation
    }
}
```

### Can Subclass Remove Checked Exceptions?

**YES** - Subclass can remove checked exceptions from throws clause.

```java
class Parent {
    public void method() throws IOException, SQLException {
        // parent method
    }
}

class Child extends Parent {
    // VALID - Remove all exceptions
    public void method() {
        // implementation without throwing exceptions
    }
    
    // VALID - Remove some exceptions
    public void method() throws IOException {
        // only throws IOException
    }
}
```

### Superclass Constructor Throwing Exception

When superclass constructor throws exception, subclass constructor must handle it.

```java
class Parent {
    public Parent(String name) throws InvalidNameException {
        if (name == null) throw new InvalidNameException("Name cannot be null");
    }
}

class Child extends Parent {
    // Must handle parent constructor exception
    public Child(String name) throws InvalidNameException {
        super(name); // Must be first statement
    }
    
    // OR handle it with try-catch
    public Child(String name) {
        try {
            super(name);
        } catch (InvalidNameException e) {
            // Handle exception - but super() must still be first statement
            // This approach won't work - super() must be first
        }
    }
    
    // Correct way to handle
    public Child(String name) throws InvalidNameException {
        super(validateName(name)); // Handle in helper method
    }
    
    private static String validateName(String name) throws InvalidNameException {
        if (name == null) throw new InvalidNameException("Name cannot be null");
        return name;
    }
}
```

### Exceptions and Polymorphism

When using polymorphism, the reference type determines which exceptions can be thrown at compile time.

```java
class Animal {
    public void makeSound() throws AnimalException {
        // parent method
    }
}

class Dog extends Animal {
    public void makeSound() throws DogException { // DogException extends AnimalException
        // dog implementation
    }
}

// Polymorphic usage
Animal animal = new Dog();
try {
    animal.makeSound(); // Must catch AnimalException (reference type)
} catch (AnimalException e) { // Cannot catch DogException directly
    // Handle exception
}
```

### Interface Not Throwing Declared Exception

If interface declares exception but implementing class doesn't throw it, that's perfectly valid.

```java
interface Service {
    void doSomething() throws ServiceException;
}

class SimpleService implements Service {
    // VALID - doesn't throw the declared exception
    public void doSomething() {
        System.out.println("Simple implementation without exceptions");
    }
}

// Usage
Service service = new SimpleService();
try {
    service.doSomething(); // Still must handle ServiceException due to interface
} catch (ServiceException e) {
    // This catch block is required even though SimpleService doesn't throw it
}
```

### Exception Handling with Abstract Classes

Abstract classes follow the same rules as regular inheritance for exception handling.

```java
abstract class AbstractProcessor {
    public abstract void process() throws ProcessingException;
    
    // Concrete method can have its own exception rules
    public void initialize() throws InitializationException {
        // implementation
    }
}

class ConcreteProcessor extends AbstractProcessor {
    // Must follow overriding rules
    public void process() throws ProcessingException {
        // Can throw same or narrower exceptions
    }
    
    // Can override concrete methods with same rules
    public void initialize() {
        // Can remove exceptions
    }
}
```

### Multiple Inheritance Levels

Exception handling rules apply at each level of inheritance.

```java
class GrandParent {
    public void method() throws IOException {
        // grandparent method
    }
}

class Parent extends GrandParent {
    public void method() throws FileNotFoundException { // Narrower than IOException
        // parent method
    }
}

class Child extends Parent {
    // Must respect Parent's throws clause, not GrandParent's
    public void method() throws FileNotFoundException {
        // Can throw FileNotFoundException or narrower
    }
    
    // INVALID - Cannot throw IOException (broader than parent's FileNotFoundException)
    // public void method() throws IOException { } // Compilation error
}
```

### Constructor Exceptions in Inheritance

Constructors have special rules for exception handling in inheritance.

```java
class Parent {
    public Parent() throws ParentException {
        // parent constructor
    }
    
    public Parent(String name) {
        // no exception
    }
}

class Child extends Parent {
    public Child() throws ParentException {
        super(); // Must handle parent constructor exception
    }
    
    public Child(String name) {
        super(name); // This parent constructor doesn't throw exception
    }
    
    // Can add additional exceptions
    public Child(int id) throws ParentException, ChildException {
        super();
        if (id < 0) throw new ChildException("Invalid ID");
    }
}
```

### Covariant Return Types and Exceptions

Covariant return types don't affect exception handling rules.

```java
class Parent {
    public Number getValue() throws DataException {
        return new Integer(10);
    }
}

class Child extends Parent {
    // Covariant return type with same exception rules
    public Integer getValue() throws DataException {
        return new Integer(20);
    }
    
    // Can narrow exceptions with covariant return types
    public Integer getValue() {
        return new Integer(30);
    }
}
```

### Overriding with Unchecked Exceptions

Subclass can always throw unchecked exceptions regardless of parent method signature.

```java
class Parent {
    public void method() {
        // no exceptions declared
    }
}

class Child extends Parent {
    // VALID - can throw unchecked exceptions
    public void method() throws RuntimeException, IllegalArgumentException {
        throw new IllegalArgumentException("Something went wrong");
    }
    
    // INVALID - cannot throw checked exceptions
    // public void method() throws IOException { } // Compilation error
}
```

### Super() and Exceptions

`super()` call must be first statement in constructor, affecting exception handling.

```java
class Parent {
    public Parent(String data) throws ValidationException {
        if (data == null) throw new ValidationException("Data cannot be null");
    }
}

class Child extends Parent {
    public Child(String data) throws ValidationException {
        super(data); // Must be first statement
        // Additional initialization after super()
    }
    
    // Cannot wrap super() in try-catch directly
    // public Child(String data) {
    //     try {
    //         super(data); // Compilation error - super() not first statement
    //     } catch (ValidationException e) {
    //         // handle
    //     }
    // }
    
    // Correct approach - handle before calling super()
    public Child(String data) throws ValidationException {
        super(validateData(data));
    }
    
    private static String validateData(String data) throws ValidationException {
        if (data == null) throw new ValidationException("Data cannot be null");
        return data;
    }
}
```

### Catch Block Handling Superclass Exception

Catch blocks can handle superclass exceptions, which will catch all subclass exceptions too.

```java
try {
    someMethod();
} catch (IOException e) { // Will catch IOException and all its subclasses
    // Handles FileNotFoundException, SocketException, etc.
    
    if (e instanceof FileNotFoundException) {
        // Handle specific subclass
    } else if (e instanceof SocketException) {
        // Handle another subclass
    }
}

// More specific to general order
try {
    someMethod();
} catch (FileNotFoundException e) {
    // Handle specific exception first
} catch (IOException e) {
    // Handle general IOException
} catch (Exception e) {
    // Handle any other exception
}
```

## INHERITANCE AND EXCEPTION HANDLING SUMMARY

### Key Rules to Remember:

1. **Method Overriding**: Can throw same, narrower, or no checked exceptions
2. **Cannot Add**: New or broader checked exceptions in overriding methods
3. **Unchecked Exceptions**: Always allowed in overriding methods
4. **Interfaces**: Same rules apply as method overriding
5. **Constructors**: Must handle superclass constructor exceptions
6. **super() Call**: Must be first statement, affecting exception handling strategy
7. **Polymorphism**: Reference type determines required exception handling
8. **Abstract Classes**: Follow same inheritance exception rules
9. **Multiple Levels**: Each level must respect its immediate parent's exception contract
10. **Covariant Returns**: Don't change exception handling rules

### Common Interview Scenarios:

- **"Can subclass throw SQLException if parent throws IOException?"** - NO (broader checked exception)
- **"Can subclass throw FileNotFoundException if parent throws IOException?"** - YES (narrower exception)
- **"Can subclass throw RuntimeException if parent throws IOException?"** - YES (unchecked exception)
- **"What if interface declares exception but implementation doesn't throw?"** - VALID (narrower is allowed)
- **"How to handle superclass constructor exceptions?"** - Declare in subclass constructor or handle before super() call

## KEY INTERVIEW TIPS

1. **Always mention exception hierarchy** when discussing types
2. **Emphasize resource management** - use try-with-resources
3. **Performance considerations** - don't use exceptions for control flow
4. **Stack trace preservation** is crucial when rethrowing
5. **Checked vs unchecked** - know when to use each
6. **Custom exceptions** should have meaningful names and messages
7. **Finally blocks** always execute except with System.exit()
8. **Multi-catch** reduces code duplication (Java 7+)
9. **Exception chaining** helps in debugging
10. **Avoid empty catch blocks** - at minimum log the exception
11. **Inheritance rules** - narrower or same exceptions only for checked exceptions
12. **Polymorphism** - reference type determines exception handling requirements

## COMMON PITFALLS TO AVOID

- Don't catch `Exception` or `Throwable` unless absolutely necessary
- Don't ignore exceptions (empty catch blocks)
- Don't use exceptions for normal program flow
- Don't forget to close resources (use try-with-resources)
- Don't lose original stack traces when rethrowing
- Don't put return statements in finally blocks
- Don't declare exceptions that are never thrown
- Don't try to throw broader checked exceptions in overriding methods
- Don't forget that unchecked exceptions can always be added in overriding methods
- Don't wrap super() calls in try-catch blocks directly

