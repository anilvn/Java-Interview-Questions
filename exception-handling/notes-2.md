# Java Exception Handling - Complete Notes

## OutOfMemoryError vs StackOverflowError

### OutOfMemoryError
- **Type**: Error (not Exception)
- **Cause**: JVM runs out of heap memory
- **Common scenarios**:
  - Creating too many objects
  - Memory leaks
  - Large arrays or collections
- **Example**: `String[] huge = new String[Integer.MAX_VALUE];`
- **Recovery**: Usually fatal, difficult to recover from

### StackOverflowError
- **Type**: Error (not Exception)
- **Cause**: Call stack exceeds maximum depth
- **Common scenarios**:
  - Infinite recursion
  - Very deep method calls
- **Example**: Method calling itself without base condition
- **Recovery**: Usually fatal, indicates programming error

```java
// StackOverflowError example
public void infiniteRecursion() {
    infiniteRecursion(); // No base case
}
```

## Exception Chaining

**Definition**: Technique of catching one exception and throwing another while preserving the original exception information.

**Purpose**:
- Maintain exception history
- Provide better context
- Hide implementation details while preserving debugging info

```java
try {
    // some operation
} catch (IOException e) {
    throw new ServiceException("Service failed", e); // 'e' is the cause
}

// Accessing chained exceptions
catch (ServiceException e) {
    Throwable cause = e.getCause(); // Gets original IOException
}
```

**Key methods**:
- `initCause(Throwable cause)`
- `getCause()`

## ClassNotFoundException vs NoClassDefFoundError

### ClassNotFoundException
- **Type**: Checked Exception
- **When**: Runtime class loading fails
- **Cause**: Class not found in classpath during `Class.forName()` or similar
- **Example**: `Class.forName("com.example.MissingClass")`

### NoClassDefFoundError
- **Type**: Error
- **When**: Class was available at compile time but missing at runtime
- **Cause**: Class present during compilation but removed/corrupted in runtime classpath
- **Example**: Class A uses Class B, B was deleted after compilation

```java
// ClassNotFoundException
try {
    Class.forName("NonExistentClass");
} catch (ClassNotFoundException e) {
    // Handle gracefully
}

// NoClassDefFoundError - cannot be caught as easily
// Usually indicates deployment/classpath issues
```

## Try Block Without Catch/Finally

**Question**: Can we use only try blocks without catch and finally?

**Answer**: **NO** - This is a compilation error.

**Valid combinations**:
- `try-catch`
- `try-finally`
- `try-catch-finally`
- `try-with-resources` (automatic resource management)

```java
// INVALID - Compilation error
try {
    // code
}

// VALID options
try {
    // code
} catch (Exception e) {
    // handle
}

try {
    // code
} finally {
    // cleanup
}

// try-with-resources (valid)
try (FileReader fr = new FileReader("file.txt")) {
    // code
} // automatic resource closing
```

## Empty Catch Block

**Question**: Can we have an empty catch block?

**Answer**: **YES** - Syntactically valid but **NOT RECOMMENDED**.

```java
try {
    riskyOperation();
} catch (Exception e) {
    // Empty - compiles but poor practice
}

// Better approaches
try {
    riskyOperation();
} catch (Exception e) {
    logger.error("Operation failed", e);
    // or at minimum:
    e.printStackTrace();
}
```

**Problems with empty catch**:
- Silent failures
- Difficult debugging
- Masked errors

## Finally Block Execution

**Question**: Does finally block always execute?

**Answer**: **Almost always**, with exceptions.

### Finally ALWAYS executes when:
- Normal execution
- Exception thrown and caught
- Exception thrown and not caught
- Return statement in try/catch

### Finally DOES NOT execute when:
- `System.exit()` called
- JVM crashes
- Infinite loop in try/catch
- Thread interrupted/killed

```java
try {
    System.exit(0); // Finally will NOT execute
    return "try";
} catch (Exception e) {
    System.exit(0); // Finally will NOT execute
    return "catch";
} finally {
    System.out.println("This won't print if System.exit() called");
}
```

## System.exit(0) Impact

**Effect**: Immediately terminates JVM, bypassing finally blocks.

```java
try {
    System.out.println("Try block");
    System.exit(0); // JVM terminates here
} finally {
    System.out.println("Finally - WILL NOT EXECUTE");
}
```

**Note**: Only `System.exit()` prevents finally execution in normal circumstances.

## Checked Exceptions from Static Block

**Question**: Can we throw checked exceptions from static blocks?

**Answer**: **NO** - Static blocks cannot throw checked exceptions.

```java
// INVALID - Compilation error
static {
    throw new IOException(); // Checked exception not allowed
}

// VALID - Runtime exceptions allowed
static {
    throw new RuntimeException(); // Unchecked exception OK
}

// WORKAROUND - Wrap in runtime exception
static {
    try {
        // risky operation
    } catch (IOException e) {
        throw new ExceptionInInitializerError(e);
    }
}
```

**Reason**: No method signature to declare throws clause for static blocks.

## Exception from Main Method

**What happens**: Uncaught exceptions in main method terminate the program.

```java
public static void main(String[] args) throws IOException {
    throw new IOException("Main method exception");
    // Program terminates, stack trace printed to stderr
}
```

**Process**:
1. Exception propagates up call stack
2. If uncaught, JVM's default exception handler takes over
3. Stack trace printed to System.err
4. Program terminates with non-zero exit code

## JVM Exception Handling

**When exception occurs, JVM**:
1. Creates exception object
2. Looks for matching catch block in current method
3. If not found, propagates to calling method
4. Continues up call stack
5. If reaches top (main method) uncaught:
   - Prints stack trace
   - Terminates program

**Default Exception Handler**:
- Prints exception class name
- Prints exception message  
- Prints stack trace
- Terminates thread/program

## Stack Trace

**Definition**: Detailed report showing the sequence of method calls leading to an exception.

**Components**:
- Exception class and message
- Method names and line numbers
- File names
- Call sequence (most recent first)

```java
Exception in thread "main" java.lang.NullPointerException: Cannot invoke method
    at com.example.MyClass.method3(MyClass.java:15)
    at com.example.MyClass.method2(MyClass.java:10)
    at com.example.MyClass.method1(MyClass.java:5)
    at com.example.MyClass.main(MyClass.java:3)
```

**Usage**:
- Debugging
- Error tracking
- Understanding execution flow

## Runtime Exceptions

**Definition**: Unchecked exceptions that extend `RuntimeException`.

**Characteristics**:
- **Not required** to be caught or declared
- Usually indicate programming errors
- Can occur anywhere in code
- Checked at runtime, not compile time

### Common Runtime Exceptions:

**NullPointerException**
```java
String str = null;
int length = str.length(); // NPE
```

**ArrayIndexOutOfBoundsException**
```java
int[] arr = {1, 2, 3};
int value = arr[5]; // Index out of bounds
```

**IllegalArgumentException**
```java
Thread.sleep(-1000); // Negative argument not allowed
```

**NumberFormatException**
```java
int num = Integer.parseInt("abc"); // Invalid number format
```

**ClassCastException**
```java
Object obj = "Hello";
Integer num = (Integer) obj; // Invalid cast
```

**ArithmeticException**
```java
int result = 10 / 0; // Division by zero
```

### Runtime vs Checked Exceptions:

| Aspect | Runtime Exceptions | Checked Exceptions |
|--------|-------------------|-------------------|
| **Handling** | Optional | Mandatory |
| **Compilation** | No compile-time check | Must catch or declare |
| **Purpose** | Programming errors | Recoverable conditions |
| **Examples** | NPE, IllegalArgument | IOException, SQLException |
| **Base Class** | RuntimeException | Exception (not Runtime) |

**Best Practice**: Handle runtime exceptions when you can recover, but don't catch them just to ignore them.