# Java Core Concepts and Advanced Features

This document provides coding examples and descriptions for various core and advanced Java concepts.

## Object Creation in Java

### Ways to Create Objects

There are five primary ways to create objects in Java:

1.  **Using the `new` keyword:** This is the most common way to create an object. It allocates memory for the object and calls the constructor of the class.

    ```java
    class MyClass {
        public MyClass() {
            System.out.println("MyClass object created using new keyword");
        }
    }

    public class NewKeywordDemo {
        public static void main(String[] args) {
            MyClass obj = new MyClass();
        }
    }
    ```

2.  **Using the `clone()` method:** The `clone()` method of the `Object` class can create a copy of an existing object. The class must implement the `Cloneable` interface.

    ```java
    class MyClass implements Cloneable {
        int value;
        public MyClass(int value) {
            this.value = value;
        }
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    public class CloneDemo {
        public static void main(String[] args) throws CloneNotSupportedException {
            MyClass obj1 = new MyClass(10);
            MyClass obj2 = (MyClass) obj1.clone();
            System.out.println("obj1.value: " + obj1.value); // Output: 10
            System.out.println("obj2.value: " + obj2.value); // Output: 10
        }
    }
    ```

3.  **Using Deserialization:** Objects can be created by reading their state from a persistent storage (like a file) using deserialization. The class must implement the `Serializable` interface.

    ```java
    import java.io.*;

    class MyClass implements Serializable {
        int value;
        public MyClass(int value) {
            this.value = value;
        }
    }

    public class DeserializationDemo {
        public static void main(String[] args) throws IOException, ClassNotFoundException {
            // Serialization
            MyClass obj1 = new MyClass(20);
            FileOutputStream fileOut = new FileOutputStream("myclass.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(obj1);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in myclass.ser");

            // Deserialization
            FileInputStream fileIn = new FileInputStream("myclass.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            MyClass obj2 = (MyClass) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Deserialized object value: " + obj2.value); // Output: 20
        }
    }
    ```

4.  **Using Factory Methods:** Factory methods are static or non-static methods that return an instance of a class. They can provide more control over object creation.

    ```java
    class MyClass {
        private MyClass() {
            System.out.println("MyClass private constructor");
        }
        public static MyClass getInstance() {
            return new MyClass();
        }
    }

    public class FactoryMethodDemo {
        public static void main(String[] args) {
            MyClass obj = MyClass.getInstance();
        }
    }
    ```

5.  **Using Literals (Auto Boxing):** For wrapper classes (like `Integer`, `Boolean`, etc.), objects can be created using literals, which are then auto-boxed into objects.

    ```java
    public class AutoBoxingDemo {
        public static void main(String[] args) {
            Integer intObj = 100; // Autoboxing
            Boolean boolObj = true; // Autoboxing
            System.out.println("Integer object: " + intObj); // Output: 100
            System.out.println("Boolean object: " + boolObj); // Output: true
        }
    }
    ```

## Dynamic Input in Java

### Ways to Load Data Dynamically

Java provides several ways to load data dynamically:

1.  **Command Line Arguments:** Data can be passed as arguments when running the Java program.

    ```java
    public class CommandLineArgsDemo {
        public static void main(String[] args) {
            if (args.length > 0) {
                System.out.println("Command line arguments:");
                for (String arg : args) {
                    System.out.println(arg);
                }
            } else {
                System.out.println("No command line arguments provided.");
            }
        }
    }
    ```

2.  **`java.io.BufferedReader`:** Reads text from an input stream, buffering characters for efficient reading. Commonly used with `InputStreamReader` (for byte to character conversion) or `FileReader`.

    ```java
    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;

    public class BufferedReaderDemo {
        public static void main(String[] args) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter your name:");
            String name = reader.readLine();
            System.out.println("Your name is: " + name);
        }
    }
    ```

3.  **`java.util.Scanner`:** A simple text scanner that can parse primitive types and strings using regular expressions. It can read from various input sources like `System.in`, files, etc.

    ```java
    import java.util.Scanner;

    public class ScannerDemo {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter an integer:");
            int number = scanner.nextInt();
            System.out.println("You entered: " + number);
            scanner.close();
        }
    }
    ```

4.  **`java.io.DataInputStream`:** Reads primitive data types and strings from an input stream in a machine-independent way.

    ```java
    import java.io.DataInputStream;
    import java.io.IOException;

    public class DataInputStreamDemo {
        public static void main(String[] args) throws IOException {
            DataInputStream dis = new DataInputStream(System.in);
            System.out.println("Enter an integer:");
            int num = Integer.parseInt(dis.readLine());
            System.out.println("You entered: " + num);
            dis.close();
        }
    }
    ```

5.  **`java.io.Console`:** Provides methods to access the system's console, if one is available. It can be used to read text and passwords.

    ```java
    import java.io.Console;
    import java.io.IOException;

    public class ConsoleInputDemo {
        public static void main(String[] args) throws IOException {
            Console console = System.console();
            if (console != null) {
                String username = console.readLine("Enter username: ");
                char[] password = console.readPassword("Enter password: ");
                System.out.println("Username: " + username);
                System.out.println("Password entered (length): " + password.length);
                // Be careful with storing or printing passwords directly
            } else {
                System.out.println("Console not available.");
            }
        }
    }
    ```

6.  **`System.getProperty()`:** Retrieves system properties, which can be set when running the JVM.

    ```java
    public class SystemPropertiesDemo {
        public static void main(String[] args) {
            String osName = System.getProperty("os.name");
            String javaVersion = System.getProperty("java.version");
            System.out.println("Operating System: " + osName);
            System.out.println("Java Version: " + javaVersion);
        }
    }
    ```

7.  **Swing/AWT/Applet (GUI):** For graphical applications, input can be taken through various UI components like text fields, etc. (Examples omitted for brevity).

## Cloning in Java

### What is Cloning?

Cloning is the process of creating an exact copy of an object. Java provides a mechanism for object cloning through the `Cloneable` interface and the `clone()` method.

### Types of Cloning

There are two main types of cloning in Java:

1.  **Shallow Cloning:** Creates a new instance of the object and copies the values of the fields from the original object to the new object. If a field is a reference to an object, only the reference is copied, meaning both the original and the cloned object will point to the same underlying object.

2.  **Deep Cloning:** Creates a new instance of the object and recursively copies all the objects referenced by the original object. This means that if a field is a reference to another object, a new copy of that object is also created, and so on. The original and the cloned object will have completely independent sets of objects.

### Steps to Implement Cloning

1.  **Implement the `Cloneable` interface:** This is a marker interface, indicating that the object can be cloned. If `clone()` is called on an object that does not implement `Cloneable`, it throws `CloneNotSupportedException`.
2.  **Override the `clone()` method:** The `clone()` method is protected in the `Object` class. To make it accessible and to customize the cloning behavior, you need to override it in your class. Call `super.clone()` within the overridden method.

### `Cloneable` Interface and `clone()` Method

* **`Cloneable` interface:** A marker interface in `java.lang` that signals to the `clone()` method that it is legal to make a field-by-field copy of instances of that class.
* **`clone()` method:** A protected method in the `Object` class. When overridden in a class that implements `Cloneable`, it should return a copy of the object.

### `CloneNotSupportedException`

This checked exception is thrown by the `clone()` method if an object's class does not implement the `Cloneable` interface.

### Shallow Clone Example

```java
class Address {
    String street;
    public Address(String street) {
        this.street = street;
    }
}

class Person implements Cloneable {
    String name;
    Address address;
    public Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); // Shallow copy: only the reference to 'address' is copied
    }
}

public class ShallowCloneDemo {
    public static void main(String[] args) throws CloneNotSupportedException {
        Address addr1 = new Address("123 Main St");
        Person person1 = new Person("Alice", addr1);
        Person person2 = (Person) person1.clone();

        System.out.println("Person 1 Address: " + person1.address.street); // Output: 123 Main St
        System.out.println("Person 2 Address: " + person2.address.street); // Output: 123 Main St

        // Modifying the address of person2 will also affect person1
        person2.address.street = "456 Oak Ave";
        System.out.println("Person 1 Address (after change): " + person1.address.street); // Output: 456 Oak Ave
        System.out.println("Person 2 Address (after change): " + person2.address.street); // Output: 456 Oak Ave
    }
}
```

### `Deep Clone (Requires Custom Implementation)`
To perform a `deep clone`, you need to manually create new copies of the referenced objects within the `clone()` method.
```java
class Address implements Cloneable {
    String street;
    public Address(String street) {
        this.street = street;
    }
    @Override
    protected Address clone() throws CloneNotSupportedException {
        return (Address) super.clone();
    }
}

class Person implements Cloneable {
    String name;
    Address address;
    public Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }
    @Override
    protected Person clone() throws CloneNotSupportedException {
        Person clonedPerson = (Person) super.clone();
        clonedPerson.address = this.address.clone(); // Deep copy: create a new Address object
        return clonedPerson;
    }
}

public class DeepCloneDemo {
    public static void main(String[] args) throws CloneNotSupportedException {
        Address addr1 = new Address("123 Main St");
        Person person1 = new Person("Alice", addr1);
        Person person2 = person1.clone();

        System.out.println("Person 1 Address: " + person1.address.street); // Output: 123 Main St
        System.out.println("Person 2 Address: " + person2.address.street); // Output: 123 Main St

        // Modifying the address of person2 will NOT affect person1
        person2.address.street = "456 Oak Ave";
        System.out.println("Person 1 Address (after change): " + person1.address.street); // Output: 123 Main St
        System.out.println("Person 2 Address (after change): " + person2.address.street); // Output: 456 Oak Ave
    }
}
```

## Serialization and Deserialization

### What is Serialization and Deserialization?

**Serialization:** The process of converting the state of an object into a byte stream. This byte stream can be saved to a file, sent over a network, or stored in a database.

**Deserialization:** The process of reconstructing an object from a byte stream that was previously serialized.

### Different Streams Available

Java provides various streams for handling byte and character data:

#### Byte Streams (for binary data):

* `FileInputStream` (FIS): Reads bytes from a file.
* `FileOutputStream` (FOS): Writes bytes to a file.
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

### Advantages and Disadvantages of FIS, FOS, DIS, DOS, OIS, OOS

#### FileInputStream & FileOutputStream:

**Advantages:** Basic for reading and writing raw byte data to/from files.
**Disadvantages:** No built-in support for primitive data types or objects; requires manual handling of data formats.

#### DataInputStream & DataOutputStream:

**Advantages:** Provides methods for reading and writing primitive Java data types in a portable way.
**Disadvantages:** Only handles primitive types and strings; not for complex objects directly.

#### ObjectInputStream & ObjectOutputStream:

**Advantages:** Enables serialization and deserialization of entire Java objects, preserving their state and structure.
**Disadvantages:** Can be slower than basic byte streams; objects must implement `Serializable`; potential security risks if deserializing from untrusted sources.

### Working with FileDescriptor

`FileDescriptor` represents an underlying operating system file handle or socket. It is often used in conjunction with file input/output streams.
```java
import java.io.*;

public class FileDescriptorDemo {
    public static void main(String[] args) throws IOException {
        File file = new File("descriptor_test.txt");
        FileOutputStream fos = new FileOutputStream(file);
        FileDescriptor fd = fos.getFD();
        FileOutputStream fos1 = new FileOutputStream(fd); // Another stream to the same underlying file

        fos1.write("Hello from FileDescriptor!".getBytes());
        fos.close();
        fos
```


