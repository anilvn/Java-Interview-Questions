# Java I/O Streams - Complete Study Notes

## 📁 I/O Streams Hierarchy

```
java.io Package
├── Byte Streams (Binary Data)
│   ├── Input Streams
│   │   ├── FileInputStream
│   │   ├── ByteArrayInputStream
│   │   ├── ObjectInputStream
│   │   ├── DataInputStream
│   │   └── SequenceInputStream
│   └── Output Streams
│       ├── FileOutputStream
│       ├── ByteArrayOutputStream
│       ├── ObjectOutputStream
│       └── DataOutputStream
└── Character Streams (Text Data)
    ├── Input Streams
    │   └── FileReader
    └── Output Streams
        └── FileWriter
```

---

## 🔧 **Section 1: Basic File Operations**

### File Class
**Definition**: Represents file and directory pathnames in the file system.

```java
import java.io.*;

public class FileBasics {
    public static void main(String[] args) throws IOException {
        // Create File object
        File f = new File("test.txt");
        
        // Check if file exists
        System.out.println("Exists: " + f.exists()); // false initially
        
        // Create new file
        System.out.println("Created: " + f.createNewFile()); // true
        
        // Check again
        System.out.println("Exists now: " + f.exists()); // true
    }
}
```

### Key File Methods Table
| Method | Description | Return Type |
|--------|-------------|-------------|
| `exists()` | Checks if file exists | boolean |
| `createNewFile()` | Creates empty file | boolean |
| `delete()` | Deletes file | boolean |
| `isFile()` | Checks if it's a file | boolean |
| `isDirectory()` | Checks if it's a directory | boolean |
| `length()` | Returns file size in bytes | long |

---

## 🔧 **Section 2: Byte Streams (Binary Data)**

### FileOutputStream
**Definition**: Writes raw bytes to files. Used for binary data like images, executables.

```java
import java.io.*;

public class ByteStreamWrite {
    public static void main(String[] args) throws IOException {
        // Create output stream (overwrites existing file)
        FileOutputStream fos = new FileOutputStream("test1.txt", false);
        
        // Write individual bytes
        fos.write(new byte[]{100, 97, 100}); // "dad" in ASCII
        fos.write(32); // space character
        
        // Write portion of byte array
        fos.write(new byte[]{65, 66, 67, 68, 69, 70}, 2, 4); // "CDEF"
        fos.write(32); // space
        
        fos.close(); // Always close streams
    }
}
```

### FileInputStream
**Definition**: Reads raw bytes from files.

```java
import java.io.*;

public class ByteStreamRead {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("test.txt");
        
        int byteData = 0;
        // Read byte by byte until end of file (-1)
        while ((byteData = fis.read()) != -1) {
            System.out.print((char) byteData); // Convert to character
        }
        
        fis.close(); // Always close streams
    }
}
```

### FileOutputStream Constructor Options
| Constructor | Description |
|-------------|-------------|
| `FileOutputStream(String name)` | Creates new file or overwrites existing |
| `FileOutputStream(String name, boolean append)` | append=true: adds to end, append=false: overwrites |
| `FileOutputStream(File file)` | Uses File object |

---

## 🔧 **Section 3: Object Serialization**

### ObjectOutputStream & ObjectInputStream
**Definition**: Serialization converts objects to byte streams for storage/transmission.

```java
import java.io.*;

// Class must implement Serializable
class Student implements Serializable {
    String name = "Anil";
    int sage = 29;
    double sfee = 2500;
    String scourse = "java";
    String insName = "NIT";
}

public class SerializationDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // SERIALIZATION (Object → File)
        FileOutputStream fos = new FileOutputStream("student.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        
        Student s = new Student();
        oos.writeObject(s); // Serialize object
        oos.close();
        
        // DESERIALIZATION (File → Object)
        FileInputStream fis = new FileInputStream("student.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        Object obj = ois.readObject(); // Deserialize
        Student s1 = (Student) obj;    // Cast back to Student
        
        System.out.println("Age: " + s1.sage); // Access deserialized data
        ois.close();
    }
}
```

### Serialization Requirements
✅ **Must implement `Serializable` interface**  
✅ **All fields must be serializable**  
✅ **Use `transient` keyword for fields you don't want serialized**  
✅ **Consider `serialVersionUID` for version control**

---

## 🔧 **Section 4: Data Streams (Primitive Types)**

### DataOutputStream & DataInputStream
**Definition**: Handles primitive data types (int, double, boolean, etc.) in binary format.

```java
import java.io.*;

public class DataStreamDemo {
    public static void main(String[] args) throws IOException {
        File file = new File("data.txt");
        
        // WRITING DATA
        FileOutputStream fos = new FileOutputStream(file);
        DataOutputStream dos = new DataOutputStream(fos);
        
        dos.write(100);              // Write byte
        dos.writeInt(200);           // Write integer
        dos.writeDouble(12.23d);     // Write double
        dos.writeChar('a');          // Write character
        dos.writeUTF("ramchandra");  // Write UTF string
        dos.writeBoolean(true);      // Write boolean
        dos.close();
        
        // READING DATA (Must read in same order!)
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        
        System.out.println("Byte: " + dis.read());
        System.out.println("Int: " + dis.readInt());
        System.out.println("Double: " + dis.readDouble());
        System.out.println("Char: " + dis.readChar());
        System.out.println("String: " + dis.readUTF());
        System.out.println("Boolean: " + dis.readBoolean());
        
        dis.close();
    }
}
```

### DataStream Method Pairs
| Write Method | Read Method | Data Type |
|--------------|-------------|-----------|
| `writeInt()` | `readInt()` | int |
| `writeDouble()` | `readDouble()` | double |
| `writeChar()` | `readChar()` | char |
| `writeBoolean()` | `readBoolean()` | boolean |
| `writeUTF()` | `readUTF()` | String |

⚠️ **Important**: Must read data in the same order it was written!

---

## 🔧 **Section 5: ByteArrayOutputStream**

### ByteArrayOutputStream
**Definition**: Writes data to byte array in memory. Useful for sending same data to multiple destinations.

```java
import java.io.*;

public class ByteArrayDemo {
    public static void main(String[] args) throws IOException {
        // Create multiple output destinations
        FileOutputStream fos1 = new FileOutputStream("file1.txt");
        FileOutputStream fos2 = new FileOutputStream("file2.txt");
        FileOutputStream fos3 = new FileOutputStream("file3.txt");
        
        // Write to memory buffer first
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(new byte[]{100, 97, 100, 32}); // "dad "
        bos.write(new byte[]{109, 111, 109});    // "mom"
        
        // Send same data to multiple files
        bos.writeTo(fos1);
        bos.writeTo(fos2);
        bos.writeTo(fos3);
        
        System.out.println("Data written to 3 files!");
        
        // Close all streams
        bos.close();
        fos1.close();
        fos2.close();
        fos3.close();
    }
}
```

### ByteArrayOutputStream Advantages
✅ **Efficient**: Write once, send to multiple destinations  
✅ **Memory-based**: Fast operations in RAM  
✅ **Flexible**: Can convert to byte array or send to any OutputStream  

---

## 🔧 **Section 6: SequenceInputStream**

### SequenceInputStream
**Definition**: Reads from multiple input streams sequentially, treating them as one continuous stream.

```java
import java.io.*;

public class SequenceDemo {
    public static void main(String[] args) throws IOException {
        // Create two separate files first
        File f1 = new File("file1.txt");
        File f2 = new File("file2.txt");
        
        // Create input streams for both files
        FileInputStream fis1 = new FileInputStream(f1);
        FileInputStream fis2 = new FileInputStream(f2);
        
        // Combine them into one stream (reads fis2 first, then fis1)
        SequenceInputStream sis = new SequenceInputStream(fis2, fis1);
        
        int data = 0;
        while ((data = sis.read()) != -1) {
            System.out.print((char) data);
        }
        
        sis.close();
    }
}
```

### SequenceInputStream Use Cases
✅ **File concatenation**: Combine multiple files  
✅ **Data merging**: Merge data from different sources  
✅ **Sequential processing**: Process files in order  

---

## 🔧 **Section 7: Character Streams (Text Data)**

### FileWriter
**Definition**: Writes character data to files. Optimized for text content.

```java
import java.io.*;

public class FileWriterDemo {
    public static void main(String[] args) throws IOException {
        File f = new File("output.txt");
        FileWriter fw = new FileWriter(f);
        
        // Write character array
        char[] chars = {'n', 'i', 't'};
        fw.write(chars);
        fw.write(32); // space
        
        // Write string
        fw.write("karthikit");
        fw.write(32); // space
        
        // Write portion of character array
        char[] chars2 = {'a', 'b', 'c', 'd', 'e', 'f'};
        fw.write(chars2, 2, 3); // writes "cde"
        fw.write(32); // space
        
        // Write substring
        fw.write("internationalization", 5, 6); // writes "nation"
        
        fw.flush(); // Force write to disk
        fw.close();
    }
}
```

### FileReader
**Definition**: Reads character data from files.

```java
import java.io.*;

public class FileReaderDemo {
    public static void main(String[] args) {
        File f = new File("input.txt");
        
        // Using try-with-resources for automatic closing
        try (FileReader fr = new FileReader(f)) {
            int charData = 0;
            while ((charData = fr.read()) != -1) {
                System.out.print((char) charData);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        }
        
        System.out.println("\nProgram completed");
    }
}
```

---

## 🔧 **Section 8: FileDescriptor**

### FileDescriptor
**Definition**: Handle to underlying machine-specific structure representing open file.

```java
import java.io.*;

public class FileDescriptorDemo {
    public static void main(String[] args) throws IOException {
        File f = new File("descriptor.txt");
        FileOutputStream fos = new FileOutputStream(f);
        
        // Get file descriptor
        FileDescriptor fd = fos.getFD();
        
        // Create another stream using same descriptor
        FileOutputStream fos1 = new FileOutputStream(fd);
        fos1.write("Hello from FileDescriptor".getBytes());
        
        fos.close();
        fos1.close();
    }
}
```

---

## 📊 **Stream Comparison Table**

| Stream Type | Data Type | Primary Use | Buffer Type | Performance |
|-------------|-----------|-------------|-------------|-------------|
| **FileInputStream/OutputStream** | Bytes | Binary files, images | Byte array | Fast |
| **FileReader/Writer** | Characters | Text files | Character array | Medium |
| **DataInputStream/OutputStream** | Primitives | Structured data | Type-specific | Fast |
| **ObjectInputStream/OutputStream** | Objects | Serialization | Object graph | Slower |
| **ByteArrayOutputStream** | Bytes | Memory buffer | Byte array | Very Fast |
| **SequenceInputStream** | Bytes | Multiple sources | Combined | Variable |

---

## 🎯 **Key Takeaways & Best Practices**

### ✅ **Always Remember**
- **Close streams**: Use try-with-resources or explicit close()
- **Handle exceptions**: IOException is checked exception
- **Choose right stream**: Byte streams for binary, Character streams for text
- **Buffer for performance**: Use BufferedInputStream/OutputStream for large files
- **Serialization requirements**: Implement Serializable interface

### ⚠️ **Common Pitfalls**
- **Not closing streams**: Causes resource leaks
- **Wrong stream type**: Using byte streams for text (encoding issues)
- **DataStream order**: Must read in same order as written
- **Forgetting flush()**: Data might not be written to disk

### 🚀 **Performance Tips**
- **Use buffered streams** for large files
- **ByteArrayOutputStream** for multiple destinations
- **Try-with-resources** for automatic cleanup
- **Avoid frequent small writes** - batch operations

---

## 📋 **Final Summary Cheat Sheet**

| Task | Best Stream Choice | Example |
|------|-------------------|---------|
| **Copy binary file** | FileInputStream/OutputStream | Images, executables |
| **Write text file** | FileWriter | Configuration files |
| **Read text file** | FileReader | Log files |
| **Save object** | ObjectOutputStream | User preferences |
| **Load object** | ObjectInputStream | Application state |
| **Write primitives** | DataOutputStream | Database records |
| **Read primitives** | DataInputStream | Binary protocols |
| **Multiple outputs** | ByteArrayOutputStream | Email attachments |
| **Combine files** | SequenceInputStream | Log file merging |

---

## 🔗 **Related Classes & Methods**

### Essential Methods to Remember
```java
// File operations
File.exists(), File.createNewFile(), File.delete()

// Stream operations
stream.read(), stream.write(), stream.close()
stream.available(), stream.skip()

// Character stream specific
writer.flush(), reader.ready()

// Data stream specific
dos.writeInt(), dis.readInt()
dos.writeUTF(), dis.readUTF()

// Object stream specific
oos.writeObject(), ois.readObject()
```

### Exception Handling
```java
try (FileInputStream fis = new FileInputStream("file.txt")) {
    // Stream operations
} catch (FileNotFoundException e) {
    // Handle file not found
} catch (IOException e) {
    // Handle other IO errors
}
```

---

*📚 **Study Tip**: Practice with small examples first, then combine concepts for complex file operations. Remember: Byte streams for binary data, Character streams for text data!*