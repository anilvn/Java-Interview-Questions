# Complete Guide to StringTokenizer and Modern Alternatives in Java

## What is StringTokenizer?

`StringTokenizer` is a **legacy utility class** from `java.util` package that was introduced in Java 1.0. Its primary purpose is to break down strings into smaller pieces (tokens) based on specified delimiter characters.

### Key Characteristics:
- **Legacy class**: Part of Java since version 1.0 but now considered outdated
- **Simple delimiter-based**: Only supports fixed character delimiters (no regex)
- **Iterator-like behavior**: Provides tokens one at a time through method calls
- **Mutable state**: Internal pointer moves forward with each `nextToken()` call
- **Memory efficient**: Doesn't create arrays or lists upfront

## Basic StringTokenizer Usage

### Simple Example with Single Delimiter

```java
import java.util.StringTokenizer;

public class StringTokenizerExample {
    public static void main(String[] args) {
        // Input string with comma delimiter
        String data = "apple,banana,cherry,orange";
        
        // Create tokenizer with comma as delimiter
        StringTokenizer tokenizer = new StringTokenizer(data, ",");
        
        // Process tokens one by one
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            System.out.println("Token: " + token);
        }
    }
}

/* Output:
Token: apple
Token: banana
Token: cherry
Token: orange
*/
```

### Multiple Delimiters Example

```java
import java.util.StringTokenizer;

public class MultipleDelimitersExample {
    public static void main(String[] args) {
        // String with multiple types of delimiters
        String data = "red,blue;green|yellow:purple";
        
        // Multiple delimiters: comma, semicolon, pipe, colon
        StringTokenizer tokenizer = new StringTokenizer(data, ",;|:");
        
        System.out.println("Tokens found:");
        while (tokenizer.hasMoreTokens()) {
            System.out.println("- " + tokenizer.nextToken());
        }
        
        // You can also count tokens before processing
        StringTokenizer counter = new StringTokenizer(data, ",;|:");
        System.out.println("\nTotal tokens: " + counter.countTokens());
    }
}

/* Output:
Tokens found:
- red
- blue
- green
- yellow
- purple

Total tokens: 5
*/
```

### StringTokenizer with Return Delimiters

```java
import java.util.StringTokenizer;

public class DelimitersReturnExample {
    public static void main(String[] args) {
        String data = "a,b;c";
        
        // Third parameter 'true' means return delimiters as tokens
        StringTokenizer tokenizer = new StringTokenizer(data, ",;", true);
        
        while (tokenizer.hasMoreTokens()) {
            System.out.println("'" + tokenizer.nextToken() + "'");
        }
    }
}

/* Output:
'a'
','
'b'
';'
'c'
*/
```

## Why StringTokenizer is Less Used Today

### Limitations:
1. **No Regular Expression Support**: Can only split on fixed characters
2. **Limited Flexibility**: Cannot handle complex patterns like "split on multiple spaces"
3. **Not Part of Collections Framework**: Doesn't implement `Iterable` or `Iterator`
4. **Legacy Design**: Predates modern Java features like streams and lambdas
5. **Stateful**: Once used, you need to create a new instance to reprocess

### Performance Considerations:
- **Memory**: More memory efficient than `String.split()` for large strings
- **Speed**: Slightly faster for simple delimiters but difference is negligible in most cases
- **Garbage Collection**: Creates fewer temporary objects

## Modern Alternatives

### 1. String.split() - Most Common Approach

```java
import java.util.Arrays;

public class StringSplitExample {
    public static void main(String[] args) {
        String data = "apple,banana,cherry";
        
        // Split returns String array
        String[] tokens = data.split(",");
        
        // Print all tokens
        System.out.println("All tokens: " + Arrays.toString(tokens));
        
        // Process each token
        for (String token : tokens) {
            System.out.println("Processing: " + token);
        }
        
        // With regex - split on one or more spaces
        String spaceData = "word1   word2 word3";
        String[] spaceTokens = spaceData.split("\\s+");
        System.out.println("Space tokens: " + Arrays.toString(spaceTokens));
    }
}

/* Output:
All tokens: [apple, banana, cherry]
Processing: apple
Processing: banana
Processing: cherry
Space tokens: [word1, word2, word3]
*/
```

### 2. Streams with split() - Modern Functional Approach

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamsSplitExample {
    public static void main(String[] args) {
        String data = "apple,banana,cherry,date";
        
        // Convert to List
        List<String> fruits = Arrays.stream(data.split(","))
                                   .collect(Collectors.toList());
        
        // Filter and transform
        List<String> longFruits = Arrays.stream(data.split(","))
                                        .filter(fruit -> fruit.length() > 5)
                                        .map(String::toUpperCase)
                                        .collect(Collectors.toList());
        
        System.out.println("All fruits: " + fruits);
        System.out.println("Long fruits (uppercase): " + longFruits);
        
        // Count tokens
        long tokenCount = Arrays.stream(data.split(",")).count();
        System.out.println("Token count: " + tokenCount);
    }
}

/* Output:
All fruits: [apple, banana, cherry, date]
Long fruits (uppercase): [BANANA, CHERRY]
Token count: 4
*/
```

### 3. Scanner - Great for Input Processing

```java
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ScannerExample {
    public static void main(String[] args) {
        String data = "apple banana cherry";
        
        // Default delimiter is whitespace
        Scanner scanner = new Scanner(data);
        List<String> tokens = new ArrayList<>();
        
        while (scanner.hasNext()) {
            tokens.add(scanner.next());
        }
        scanner.close();
        
        System.out.println("Tokens: " + tokens);
        
        // Custom delimiter
        String csvData = "item1,item2,item3";
        Scanner csvScanner = new Scanner(csvData);
        csvScanner.useDelimiter(",");
        
        System.out.println("CSV tokens:");
        while (csvScanner.hasNext()) {
            System.out.println("- " + csvScanner.next());
        }
        csvScanner.close();
    }
}

/* Output:
Tokens: [apple, banana, cherry]
CSV tokens:
- item1
- item2
- item3
*/
```

### 4. Pattern.split() - When You Need Regex Power

```java
import java.util.regex.Pattern;
import java.util.Arrays;

public class PatternSplitExample {
    public static void main(String[] args) {
        // Pre-compile pattern for better performance if used multiple times
        Pattern commaPattern = Pattern.compile(",");
        
        String data = "a,b,c,d";
        String[] tokens = commaPattern.split(data);
        
        System.out.println("Pattern split: " + Arrays.toString(tokens));
        
        // Complex regex example
        String complexData = "word1   word2\t\tword3\nword4";
        Pattern whitespacePattern = Pattern.compile("\\s+");
        String[] complexTokens = whitespacePattern.split(complexData);
        
        System.out.println("Complex split: " + Arrays.toString(complexTokens));
    }
}

/* Output:
Pattern split: [a, b, c, d]
Complex split: [word1, word2, word3, word4]
*/
```

## Comparison Table: StringTokenizer vs Modern Alternatives

| Feature | StringTokenizer | String.split() | Scanner | Pattern.split() | Streams + split() |
|---------|----------------|----------------|---------|-----------------|-------------------|
| **Regex Support** | ❌ No | ✅ Yes | ✅ Yes | ✅ Yes | ✅ Yes |
| **Memory Usage** | ✅ Low | ❌ Higher | ✅ Low | ❌ Higher | ❌ Higher |
| **Performance** | ✅ Fast | ✅ Fast | ✅ Fast | ✅ Very Fast* | ✅ Fast |
| **Ease of Use** | ⚠️ Moderate | ✅ Easy | ✅ Easy | ⚠️ Moderate | ✅ Easy |
| **Collections Integration** | ❌ No | ✅ Yes | ⚠️ Manual | ✅ Yes | ✅ Excellent |
| **Stream Processing** | ❌ No | ⚠️ Manual | ❌ No | ⚠️ Manual | ✅ Native |
| **Reusability** | ❌ Single Use | ✅ Reusable | ❌ Single Use | ✅ Reusable | ✅ Reusable |
| **Modern Java Features** | ❌ No | ⚠️ Limited | ⚠️ Limited | ⚠️ Limited | ✅ Full |
| **Learning Curve** | ⚠️ Moderate | ✅ Easy | ✅ Easy | ⚠️ Moderate | ⚠️ Moderate |
| **Best For** | Legacy code | General use | Input parsing | Regex heavy | Functional style |

*When pattern is pre-compiled and reused

## Performance Comparison with Code Examples

```java
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.Scanner;

public class PerformanceComparison {
    public static void main(String[] args) {
        String data = "apple,banana,cherry,date,elderberry";
        int iterations = 100000;
        
        // StringTokenizer approach
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            StringTokenizer tokenizer = new StringTokenizer(data, ",");
            while (tokenizer.hasMoreTokens()) {
                tokenizer.nextToken(); // Process token
            }
        }
        long tokenizerTime = System.nanoTime() - start;
        
        // String.split() approach
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            String[] tokens = data.split(",");
            for (String token : tokens) {
                // Process token
            }
        }
        long splitTime = System.nanoTime() - start;
        
        // Scanner approach
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Scanner scanner = new Scanner(data);
            scanner.useDelimiter(",");
            while (scanner.hasNext()) {
                scanner.next(); // Process token
            }
            scanner.close();
        }
        long scannerTime = System.nanoTime() - start;
        
        System.out.println("Performance Results (100,000 iterations):");
        System.out.println("StringTokenizer: " + tokenizerTime / 1_000_000 + " ms");
        System.out.println("String.split():  " + splitTime / 1_000_000 + " ms");
        System.out.println("Scanner:         " + scannerTime / 1_000_000 + " ms");
    }
}
```

## When to Use Each Approach

### Use StringTokenizer when:
- Working with **legacy code** that already uses it
- **Memory is critical** and you're processing very large strings
- You need **simple delimiter splitting** without regex
- **Performance is absolutely critical** for simple tokenization

### Use String.split() when:
- You need **regex support**
- You want to **convert to arrays/collections** easily
- You're doing **general-purpose string splitting**
- You want **readable, maintainable code**

### Use Scanner when:
- **Reading from input sources** (files, System.in)
- You need **type conversion** (nextInt(), nextDouble())
- Processing **structured input** with mixed data types
- You want **flexible delimiter handling**

### Use Pattern.split() when:
- You're **reusing the same regex pattern** multiple times
- You need **maximum performance** with complex patterns
- You're doing **heavy regex-based processing**

### Use Streams + split() when:
- You want **functional programming style**
- You need to **filter, map, or transform** tokens
- You're **chaining operations** together
- You want **modern, readable code**

## Complete Side-by-Side Example

```java
import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

public class CompleteSideBySideExample {
    public static void main(String[] args) {
        String data = "apple,banana,cherry,date";
        
        System.out.println("=== Input: " + data + " ===\n");
        
        // 1. StringTokenizer approach
        System.out.println("1. StringTokenizer:");
        StringTokenizer tokenizer = new StringTokenizer(data, ",");
        while (tokenizer.hasMoreTokens()) {
            System.out.println("  - " + tokenizer.nextToken());
        }
        
        // 2. String.split() approach
        System.out.println("\n2. String.split():");
        String[] tokens = data.split(",");
        for (String token : tokens) {
            System.out.println("  - " + token);
        }
        
        // 3. Scanner approach
        System.out.println("\n3. Scanner:");
        Scanner scanner = new Scanner(data);
        scanner.useDelimiter(",");
        while (scanner.hasNext()) {
            System.out.println("  - " + scanner.next());
        }
        scanner.close();
        
        // 4. Pattern.split() approach
        System.out.println("\n4. Pattern.split():");
        Pattern pattern = Pattern.compile(",");
        String[] patternTokens = pattern.split(data);
        for (String token : patternTokens) {
            System.out.println("  - " + token);
        }
        
        // 5. Streams approach
        System.out.println("\n5. Streams + split():");
        List<String> streamTokens = Arrays.stream(data.split(","))
                                          .collect(Collectors.toList());
        streamTokens.forEach(token -> System.out.println("  - " + token));
        
        // 6. Streams with processing
        System.out.println("\n6. Streams with filtering:");
        List<String> longFruits = Arrays.stream(data.split(","))
                                        .filter(fruit -> fruit.length() > 5)
                                        .map(String::toUpperCase)
                                        .collect(Collectors.toList());
        System.out.println("  Long fruits: " + longFruits);
    }
}
```

## Best Practices and Recommendations

### For Modern Java Development:
1. **Prefer `String.split()`** for most string tokenization needs
2. **Use Streams** when you need to process tokens further
3. **Use Scanner** for input parsing and type conversion
4. **Avoid StringTokenizer** unless working with legacy code or extreme performance requirements

### Memory and Performance Tips:
- **Pre-compile patterns** if using the same regex multiple times
- **Use StringBuilder** for reconstructing strings from tokens
- **Consider memory usage** when processing very large strings
- **Profile your specific use case** rather than assuming performance characteristics

### Code Readability:
- **Choose the approach** that makes your intent clearest
- **Use meaningful variable names** for tokens
- **Add comments** explaining complex regex patterns
- **Consider future maintainability** over micro-optimizations

This comprehensive guide should help you understand when and how to use StringTokenizer versus modern alternatives in Java, with practical examples and performance considerations for each approach.