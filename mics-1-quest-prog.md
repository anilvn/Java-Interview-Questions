Great set of questions! Let’s go through them **one by one** with clear answers and code where needed.

---

### ✅ **1. Without StringBuffer, how to reverse a string**

You can do this by converting the string to a char array and swapping from start to end:

```java
public class ReverseString {
    public static void main(String[] args) {
        String input = "hello";
        char[] chars = input.toCharArray();
        
        int left = 0;
        int right = chars.length - 1;
        
        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            
            left++;
            right--;
        }
        
        String reversed = new String(chars);
        System.out.println(reversed); // olleh
    }
}
```

---

### ✅ **2. What is metaspace in Java?**

* Metaspace is part of memory introduced in **Java 8** to replace **PermGen**.
* It stores **class metadata**, like class structure, methods, constant pool, etc.
* Unlike PermGen, **Metaspace is native memory** (outside the JVM heap) and can grow dynamically (limited by available system memory).
* Benefit: reduces out-of-memory errors related to class metadata.

---

### ✅ **3. How did you customize the annotation?**

Custom annotations in Java can be created to add metadata.
Example:

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Define annotation
@Retention(RetentionPolicy.RUNTIME) // keep at runtime
public @interface MyCustomAnnotation {
    String value();
}

// Use annotation
public class Demo {
    @MyCustomAnnotation("Custom message")
    public void show() {
        System.out.println("Method with custom annotation");
    }
}
```

You can process it via reflection or using tools like Spring AOP or AspectJ to add behavior when methods/classes are annotated.

---

### ✅ **4. Write a code to move all zero elements to the end using Java 8**

Example using streams + collectors:

```java
import java.util.*;
import java.util.stream.*;

public class MoveZeros {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(0, 1, 0, 3, 0, 5, 0, 6);

        List<Integer> result = Stream.concat(
                list.stream().filter(n -> n != 0),
                list.stream().filter(n -> n == 0)
        ).collect(Collectors.toList());

        System.out.println(result); // [1, 3, 5, 6, 0, 0, 0, 0]
    }
}
```

---

### ✅ **5. Print non-repetitive characters from a given String**

```java
import java.util.*;

public class NonRepetitiveChars {
    public static void main(String[] args) {
        String str = "programming";
        Map<Character, Long> freq = str.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()));

        freq.entrySet().stream()
            .filter(entry -> entry.getValue() == 1)
            .forEach(entry -> System.out.print(entry.getKey() + " "));
        
        // Output: p r a n
    }
}
```

*Uses LinkedHashMap to keep order.*

---

### ✅ **6. What is the purpose of view and what is the purpose of composite view?**

|          | View                                                                                                                               | Composite View                                                                                                 |
| -------- | ---------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------- |
| Purpose  | A **view** is a database object that shows a virtual table created by a query. Used for abstraction, security, and simplification. | A **composite view** combines multiple tables/views into a single view to show a unified result (like a join). |
| Use case | Hiding complex queries, exposing limited columns to users.                                                                         | Combine data from multiple sources; e.g., customer and order details in one view.                              |

Example:

* View: `CREATE VIEW ActiveUsers AS SELECT id, name FROM users WHERE active=1;`
* Composite view: `CREATE VIEW OrderDetails AS SELECT o.id, o.date, c.name FROM orders o JOIN customers c ON o.customer_id=c.id;`

---

If you'd like,
✅ I can also help you with **how to process custom annotation using reflection** or **Spring AOP** — let me know! 🚀
