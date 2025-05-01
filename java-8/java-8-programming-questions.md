# Java 8 Programming Solutions

<p align="center">
  <a href="https://github.com/anilvn/spring-boot-interview-questions" target="_blank" rel="noopener noreferrer">
    <img src="https://img.shields.io/github/stars/anilvn/spring-boot-interview-questions?style=social" alt="GitHub Stars" />
  </a>
  &nbsp;
  <a href="https://www.linkedin.com/in/anil-valsa/" target="_blank" rel="noopener noreferrer">
    <img src="https://img.shields.io/badge/LinkedIn--blue?style=social&logo=linkedin" alt="LinkedIn" />
  </a>
</p>

<p align="center">
  ⭐ <strong>Please consider giving it a ⭐️ to show your support!</strong>
  <a href="https://github.com/anilvn/spring-boot-interview-questions" target="_blank" rel="noopener noreferrer">Click here</a>
  &nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <strong>Let’s connect professionally:</strong>
  <a href="https://www.linkedin.com/in/anil-valsa/" target="_blank" rel="noopener noreferrer">Anil Valsa on LinkedIn</a>
</p>

---

Java 8 introduced a paradigm shift in Java programming with functional programming concepts, lambdas, streams API, and new date-time API. This document provides comprehensive solutions to common Java 8 programming questions frequently asked in interviews.

### Additional Resources

If you found this guide helpful, you might also be interested in my other Spring Framework resources:
- [Core Java & Java-8 Interview Questions](https://github.com/anilvn/Java-Interview-Questions/tree/main)
- [Spring Boot Interview Questions](https://github.com/anilvn/spring-boot-interview-questions/tree/main)
- [Microservices with Spring Cloud Tutorials](https://javatechonline.com/microservices-tutorial/)

Feel free to star and fork these repositories if you find them useful!

---

## Table of Contents

| # | Question |
|---|----------|
| 1 | [Find the Frequency of Each Character in a Given String](#1-find-the-frequency-of-each-character-in-a-given-string) |
| 2 | [Find the Maximum Number and Minimum Number in a List](#2-find-the-maximum-number-and-minimum-number-in-a-list) |
| 3 | [Find the Second Largest Number in the List of Integers](#3-find-the-second-largest-number-in-the-list-of-integers) |
| 4 | [Find the Age of a Person if the Birthday Date has Given](#4-find-the-age-of-a-person-if-the-birthday-date-has-given) |
| 5 | [Find the Sum of All Digits of a Number](#5-find-the-sum-of-all-digits-of-a-number) |
| 6 | [Print Even Numbers from a List](#6-print-even-numbers-from-a-list) |
| 7 | [Remove Duplicate Elements from a List](#7-remove-duplicate-elements-from-a-list) |
| 8 | [Retrieve Last Element of a List of Strings](#8-retrieve-last-element-of-a-list-of-strings) |
| 9 | [Reverse Each Word of String](#9-reverse-each-word-of-string) |
| 10 | [Sort List of Strings Alphabetically](#10-sort-list-of-strings-alphabetically) |
| **String Manipulation** |
| 11 | [Count Characters in a String](#11-count-characters-in-a-string) |
| 12 | [Find First Repeated Character in a String](#12-find-first-repeated-character-in-a-string) |
| 13 | [Reverse a String](#13-reverse-a-string) |
| **Stream Operations** |
| 14 | [Concatenate Two Streams](#14-concatenate-two-streams) |
| 15 | [Find the Minimum and Maximum Number of a Stream](#15-find-the-minimum-and-maximum-number-of-a-stream) |
| **Number Operations** |
| 16 | [Find Prime Number](#16-find-prime-number) |
| **Additional Questions** |
| 17 | [Group Elements by a Property](#17-group-elements-by-a-property) |
| 18 | [Convert List to Map](#18-convert-list-to-map) |
| 19 | [Parallel Processing with Streams](#19-parallel-processing-with-streams) |
| 20 | [Working with Optional](#20-working-with-optional) |

## 1. Find the Frequency of Each Character in a Given String

In Java 8, we can use streams and collectors to find the frequency of each character in a string:

```java
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CharacterFrequency {
    public static void main(String[] args) {
        String input = "programming";
        
        // Using Stream API to count character frequency
        Map<Character, Long> frequencyMap = input.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        // Print the frequency of each character
        frequencyMap.forEach((character, count) -> 
            System.out.println(character + ": " + count));
    }
}
```

**Key Concepts:**
- `String.chars()` converts the string to an IntStream
- `mapToObj(c -> (char) c)` converts each int to Character
- `Collectors.groupingBy` with `Function.identity()` groups by the character itself
- `Collectors.counting()` counts occurrences in each group

[Back to Top](#table-of-contents)

## 2. Find the Maximum Number and Minimum Number in a List

Java 8 provides convenient stream methods to find minimum and maximum values:

```java
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

public class MinMaxInList {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(5, 9, 1, 3, 4, 8, 6, 7, 2);
        
        // Find maximum number
        Integer max = numbers.stream()
                .max(Integer::compare)
                .orElseThrow(() -> new IllegalStateException("No maximum value found"));
        System.out.println("Maximum number: " + max);
        
        // Find minimum number
        Integer min = numbers.stream()
                .min(Integer::compare)
                .orElseThrow(() -> new IllegalStateException("No minimum value found"));
        System.out.println("Minimum number: " + min);
        
        // Alternative using primitive streams
        int maxPrimitive = numbers.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow(() -> new IllegalStateException("No maximum value found"));
        
        int minPrimitive = numbers.stream()
                .mapToInt(Integer::intValue)
                .min()
                .orElseThrow(() -> new IllegalStateException("No minimum value found"));
    }
}
```

**Key Concepts:**
- `stream().max()` and `stream().min()` return `Optional` objects
- `Integer::compare` is a method reference used as comparator
- `orElseThrow()` handles empty collections
- For primitive streams, `mapToInt()` provides specialized methods like `max()` and `min()`

[Back to Top](#table-of-contents)

## 3. Find the Second Largest Number in the List of Integers

Finding the second largest number requires some careful handling of duplicates and edge cases:

```java
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SecondLargestNumber {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(5, 9, 1, 3, 9, 8, 6, 7, 2);
        
        // Method 1: Using sorted() and skip()
        Optional<Integer> secondLargest = numbers.stream()
                .sorted(Comparator.reverseOrder())
                .distinct()  // Remove duplicates
                .skip(1)     // Skip the largest
                .findFirst();
        
        secondLargest.ifPresent(num -> System.out.println("Second largest: " + num));
        
        // Method 2: Using limit() and min()
        Optional<Integer> secondLargest2 = numbers.stream()
                .distinct()  // Remove duplicates
                .sorted(Comparator.reverseOrder())
                .limit(2)    // Take top 2
                .min(Comparator.naturalOrder());  // Get the smaller of the two
        
        secondLargest2.ifPresent(num -> System.out.println("Second largest (method 2): " + num));
    }
}
```

**Key Concepts:**
- `distinct()` removes duplicate elements
- `sorted(Comparator.reverseOrder())` sorts in descending order
- `skip(1)` skips the first (largest) element
- `findFirst()` gets the second largest
- Alternative approach uses `limit(2)` and `min()` to find the smaller of the top two elements

[Back to Top](#table-of-contents)

## 4. Find the Age of a Person if the Birthday Date has Given

Java 8 introduced a much-improved date and time API that makes calculating age straightforward:

```java
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class CalculateAge {
    public static void main(String[] args) {
        // Birthday date in yyyy-MM-dd format
        String birthdayString = "1990-05-15";
        
        // Parse the birthday string to LocalDate
        LocalDate birthDate = LocalDate.parse(birthdayString, 
                                             DateTimeFormatter.ISO_LOCAL_DATE);
        
        // Get current date
        LocalDate currentDate = LocalDate.now();
        
        // Calculate age using Period
        Period period = Period.between(birthDate, currentDate);
        
        // Extract years, months, and days
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();
        
        System.out.println("Age: " + years + " years, " + 
                           months + " months, " + days + " days");
        
        // Alternative: Just calculate years
        int age = period.getYears();
        System.out.println("Age in years: " + age);
    }
}
```

**Key Concepts:**
- `LocalDate` class from `java.time` package represents dates
- `DateTimeFormatter` handles date string parsing
- `Period.between()` calculates the period between two dates
- `getYears()`, `getMonths()`, and `getDays()` extract components of the period

[Back to Top](#table-of-contents)

## 5. Find the Sum of All Digits of a Number

We can use Java 8 features to calculate the sum of digits in a number:

```java
import java.util.stream.IntStream;

public class SumOfDigits {
    public static void main(String[] args) {
        int number = 12345;
        
        // Method 1: Convert to string, then to IntStream
        int sum1 = String.valueOf(number)
                .chars()
                .map(Character::getNumericValue)
                .sum();
        System.out.println("Sum of digits (Method 1): " + sum1);
        
        // Method 2: Using traditional approach with streams
        int sum2 = computeDigitSum(number);
        System.out.println("Sum of digits (Method 2): " + sum2);
    }
    
    private static int computeDigitSum(int number) {
        return IntStream.iterate(number, n -> n > 0, n -> n / 10)
                .map(n -> n % 10)
                .sum();
    }
}
```

**Key Concepts:**
- Method 1 converts the number to string and processes its characters
- `chars()` returns an IntStream of character codes
- `Character::getNumericValue` converts character codes to numeric values
- Method 2 uses `IntStream.iterate()` to repeatedly divide by 10
- `map(n -> n % 10)` extracts each digit using modulo operation

[Back to Top](#table-of-contents)

## 6. Print Even Numbers from a List

Using Java 8 streams to filter and print even numbers:

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EvenNumbers {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Method 1: Filter and print directly
        System.out.println("Even numbers:");
        numbers.stream()
               .filter(n -> n % 2 == 0)
               .forEach(System.out::println);
        
        // Method 2: Collect into a new list
        List<Integer> evenNumbers = numbers.stream()
                                          .filter(n -> n % 2 == 0)
                                          .collect(Collectors.toList());
        System.out.println("Even numbers list: " + evenNumbers);
        
        // Method 3: Create an IntStream with range and filter
        System.out.println("Even numbers from 1 to 20:");
        IntStream.rangeClosed(1, 20)
                .filter(n -> n % 2 == 0)
                .forEach(System.out::println);
    }
}
```

**Key Concepts:**
- `filter(n -> n % 2 == 0)` keeps only elements divisible by 2
- `forEach(System.out::println)` prints each element
- `collect(Collectors.toList())` gathers results into a new list
- `IntStream.rangeClosed()` creates a stream of numbers in a given range

[Back to Top](#table-of-contents)

## 7. Remove Duplicate Elements from a List

Java 8 provides several ways to remove duplicates from a list:

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveDuplicates {
    public static void main(String[] args) {
        List<Integer> numbersWithDuplicates = Arrays.asList(1, 2, 3, 1, 4, 2, 5, 6, 3, 7);
        
        // Using Stream.distinct()
        List<Integer> distinctNumbers = numbersWithDuplicates.stream()
                                                           .distinct()
                                                           .collect(Collectors.toList());
        
        System.out.println("Original list: " + numbersWithDuplicates);
        System.out.println("List without duplicates: " + distinctNumbers);
        
        // String example
        List<String> stringsWithDuplicates = Arrays.asList(
                "apple", "banana", "apple", "orange", "banana", "mango");
        
        List<String> distinctStrings = stringsWithDuplicates.stream()
                                                         .distinct()
                                                         .collect(Collectors.toList());
        
        System.out.println("Original strings: " + stringsWithDuplicates);
        System.out.println("Distinct strings: " + distinctStrings);
    }
}
```

**Key Concepts:**
- `distinct()` method removes duplicate elements from the stream
- Elements are compared using their `equals()` method
- `collect(Collectors.toList())` gathers results into a new list
- Works for any type of object, not just primitives

[Back to Top](#table-of-contents)

## 8. Retrieve Last Element of a List of Strings

Retrieving the last element of a list using Java 8 features:

```java
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LastElement {
    public static void main(String[] args) {
        List<String> fruits = Arrays.asList("apple", "banana", "orange", "mango", "grape");
        
        // Method 1: Using streams with skip()
        Optional<String> lastElement = fruits.stream()
                                          .skip(fruits.size() - 1)
                                          .findFirst();
        
        lastElement.ifPresent(element -> 
            System.out.println("Last element (using stream): " + element));
        
        // Method 2: Direct access (traditional approach)
        if (!fruits.isEmpty()) {
            String last = fruits.get(fruits.size() - 1);
            System.out.println("Last element (direct access): " + last);
        }
        
        // Method 3: Using streams with reduce()
        Optional<String> lastElementReduce = fruits.stream()
                                               .reduce((first, second) -> second);
        
        lastElementReduce.ifPresent(element -> 
            System.out.println("Last element (using reduce): " + element));
    }
}
```

**Key Concepts:**
- Method 1: `skip(size - 1)` skips all but the last element, then `findFirst()` retrieves it
- Method 2: Traditional approach using `get(size - 1)`
- Method 3: `reduce()` keeps replacing the accumulated value, ending with the last element
- All methods handle empty lists safely

[Back to Top](#table-of-contents)

## 9. Reverse Each Word of String

Using Java 8 features to reverse each word in a string:

```java
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReverseWords {
    public static void main(String[] args) {
        String input = "Java 8 Stream API is powerful";
        
        // Method: Split by space, reverse each word, join back
        String reversed = Arrays.stream(input.split(" "))
                             .map(ReverseWords::reverseWord)
                             .collect(Collectors.joining(" "));
        
        System.out.println("Original: " + input);
        System.out.println("Reversed words: " + reversed);
    }
    
    private static String reverseWord(String word) {
        return new StringBuilder(word).reverse().toString();
    }
}
```

**Key Concepts:**
- `split(" ")` breaks the string into words
- `map(ReverseWords::reverseWord)` applies the reversal to each word
- `StringBuilder.reverse()` efficiently reverses a string
- `Collectors.joining(" ")` joins the words back with spaces

[Back to Top](#table-of-contents)

## 10. Sort List of Strings Alphabetically

Sorting strings alphabetically using Java 8 streams:

```java
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortStrings {
    public static void main(String[] args) {
        List<String> fruits = Arrays.asList("Banana", "Apple", "Mango", "Orange", "Grape", "apple");
        
        // Sort alphabetically (case sensitive)
        List<String> sortedCaseSensitive = fruits.stream()
                                              .sorted()
                                              .collect(Collectors.toList());
        
        System.out.println("Case-sensitive sort: " + sortedCaseSensitive);
        
        // Sort alphabetically (case insensitive)
        List<String> sortedCaseInsensitive = fruits.stream()
                                               .sorted(String.CASE_INSENSITIVE_ORDER)
                                               .collect(Collectors.toList());
        
        System.out.println("Case-insensitive sort: " + sortedCaseInsensitive);
        
        // Sort by length and then alphabetically
        List<String> sortedByLengthThenAlpha = fruits.stream()
                                                  .sorted(Comparator.comparing(String::length)
                                                          .thenComparing(String::compareToIgnoreCase))
                                                  .collect(Collectors.toList());
        
        System.out.println("Sort by length then alphabetically: " + sortedByLengthThenAlpha);
    }
}
```

**Key Concepts:**
- `sorted()` uses natural ordering (lexicographical for strings)
- `String.CASE_INSENSITIVE_ORDER` is a predefined case-insensitive comparator
- `Comparator.comparing(String::length)` sorts by string length
- `thenComparing()` provides a secondary sort criteria
- Results are collected into a new list

[Back to Top](#table-of-contents)

## 11. Count Characters in a String

Using Java 8 to count specific types of characters in a string:

```java
import java.util.function.Predicate;

public class CountCharacters {
    public static void main(String[] args) {
        String text = "Java 8 has 9 new features! Lambda & Stream API are useful.";
        
        // Count all characters
        long totalChars = text.chars().count();
        
        // Count letters
        long letterCount = text.chars()
                            .filter(Character::isLetter)
                            .count();
        
        // Count digits
        long digitCount = text.chars()
                           .filter(Character::isDigit)
                           .count();
        
        // Count spaces
        long spaceCount = text.chars()
                           .filter(Character::isWhitespace)
                           .count();
        
        // Count special characters (non-alphanumeric, non-whitespace)
        long specialCharCount = text.chars()
                                 .filter(c -> !Character.isLetterOrDigit(c) && !Character.isWhitespace(c))
                                 .count();
        
        System.out.println("Total characters: " + totalChars);
        System.out.println("Letter count: " + letterCount);
        System.out.println("Digit count: " + digitCount);
        System.out.println("Space count: " + spaceCount);
        System.out.println("Special character count: " + specialCharCount);
    }
}
```

**Key Concepts:**
- `String.chars()` returns an IntStream of character codes
- `Character` class provides useful methods like `isLetter()` and `isDigit()`
- `filter()` with appropriate predicates selects specific character types
- `count()` returns the number of elements in the stream
- Complex filters can be created using logical operators

[Back to Top](#table-of-contents)

## 12. Find First Repeated Character in a String

Finding the first repeated character in a string using Java 8:

```java
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FirstRepeatedCharacter {
    public static void main(String[] args) {
        String input = "programming";
        
        // Method 1: Using stream and Set
        Optional<Character> firstRepeated = findFirstRepeatedChar(input);
        
        firstRepeated.ifPresentOrElse(
            ch -> System.out.println("First repeated character: " + ch),
            () -> System.out.println("No repeated characters found.")
        );
        
        // Method 2: Using IntStream
        findFirstRepeatedCharAlternative(input);
    }
    
    private static Optional<Character> findFirstRepeatedChar(String str) {
        Set<Character> seen = new HashSet<>();
        
        return str.chars()
                 .mapToObj(c -> (char) c)
                 .filter(c -> !seen.add(c))  // If add() returns false, character was already in set
                 .findFirst();
    }
    
    private static void findFirstRepeatedCharAlternative(String str) {
        Set<Integer> seen = new HashSet<>();
        
        str.chars()
           .filter(c -> !seen.add(c))
           .mapToObj(c -> (char) c)
           .findFirst()
           .ifPresentOrElse(
               ch -> System.out.println("First repeated character (method 2): " + ch),
               () -> System.out.println("No repeated characters found.")
           );
    }
}
```

**Key Concepts:**
- Maintain a `HashSet` to track seen characters
- `seen.add(c)` returns false if the character is already in the set
- `filter(c -> !seen.add(c))` keeps only characters that are already seen
- `findFirst()` gets the first such character
- `ifPresentOrElse()` handles both present and absent cases

[Back to Top](#table-of-contents)

## 13. Reverse a String

Several approaches to reverse a string using Java 8:

```java
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReverseString {
    public static void main(String[] args) {
        String original = "Hello, Java 8!";
        
        // Method 1: Using StringBuilder (most efficient)
        String reversed1 = new StringBuilder(original).reverse().toString();
        System.out.println("Reversed (StringBuilder): " + reversed1);
        
        // Method 2: Using IntStream
        String reversed2 = IntStream.range(0, original.length())
                                 .map(i -> original.length() - i - 1)  // map to reversed indices
                                 .mapToObj(i -> original.charAt(i))     // get characters
                                 .map(String::valueOf)                  // convert to String
                                 .collect(Collectors.joining());        // join them
        
        System.out.println("Reversed (IntStream): " + reversed2);
        
        // Method 3: Using chars() and collect
        String reversed3 = original.chars()
                                .mapToObj(c -> (char) c)
                                .collect(StringBuilder::new, 
                                         (sb, c) -> sb.insert(0, c),  // insert at beginning
                                         StringBuilder::append)
                                .toString();
        
        System.out.println("Reversed (chars + collect): " + reversed3);
    }
}
```

**Key Concepts:**
- Method 1: `StringBuilder.reverse()` is the most efficient approach
- Method 2: Maps indices to get characters in reverse order
- Method 3: Uses a custom collector that inserts each character at the beginning
- Method 3 uses `StringBuilder` as the mutable collection container

[Back to Top](#table-of-contents)

## 14. Concatenate Two Streams

Concatenating streams using Java 8:

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConcatenateStreams {
    public static void main(String[] args) {
        // Create two streams of different types
        Stream<String> stream1 = Stream.of("a", "b", "c");
        Stream<String> stream2 = Stream.of("x", "y", "z");
        
        // Concatenate streams
        List<String> concatenated = Stream.concat(stream1, stream2)
                                       .collect(Collectors.toList());
        
        System.out.println("Concatenated stream: " + concatenated);
        
        // Example with numeric streams
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(4, 5, 6);
        
        List<Integer> mergedList = Stream.concat(
                list1.stream(),
                list2.stream()
            ).collect(Collectors.toList());
        
        System.out.println("Merged lists: " + mergedList);
        
        // Concatenating more than two streams
        Stream<String> stream3 = Stream.of("1", "2", "3");
        Stream<String> stream4 = Stream.of("4", "5", "6");
        Stream<String> stream5 = Stream.of("7", "8", "9");
        
        List<String> multiConcatenated = Stream.concat(
                Stream.concat(stream3, stream4),
                stream5
            ).collect(Collectors.toList());
        
        System.out.println("Multi-concatenated: " + multiConcatenated);
    }
}
```

**Key Concepts:**
- `Stream.concat()` is used to merge two streams
- Concatenated streams preserve the order of elements
- For more than two streams, `concat()` calls can be nested
- Stream elements can be of any type, as long as both streams have the same type
- Streams are consumed after use, so fresh streams must be created for each operation

[Back to Top](#table-of-contents)

## 15. Find the Minimum and Maximum Number of a Stream

Finding minimum and maximum values in a stream efficiently:

```java
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class MinMaxInStream {
    public static void main(String[] args) {
        // Using primitive IntStream
        IntStream numbers = IntStream.of(5, 3, 9, 1, 7, 4, 6, 8, 2);
        
        OptionalInt min = numbers.min();
        min.ifPresent(value -> System.out.println("Minimum: " + value));
        
        // Need to recreate the stream as it's already consumed
        IntStream numbers2 = IntStream.of(5, 3, 9, 1, 7, 4, 6, 8, 2);
        OptionalInt max = numbers2.max();
        max.ifPresent(value -> System.out.println("Maximum: " + value));
        
        // Using object stream
        List<Integer> numberList = Arrays.asList(5, 3, 9, 1, 7, 4, 6, 8, 2);
        
        Optional<Integer> minObj = numberList.stream().min(Integer::compare);
        minObj.ifPresent(value -> System.out.println("Minimum (object): " + value));
        
        Optional<Integer> maxObj = numberList.stream().max(Integer::compare);
        maxObj.ifPresent(value -> System.out.println("Maximum (object): " + value));
        
        // Get both min and max in one pass using reduce
        int[] result = numberList.stream()
                              .reduce(
                                  new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE},
                                  (acc, num) -> new int[]{
                                      Math.min(acc[0], num),  // Minimum
                                      Math.max(acc[1], num)   // Maximum
                                  },
                                  (acc1, acc2) -> new int[]{
                                      Math.min(acc1[0], acc2[0]),
                                      Math.max(acc1[1], acc2[1])
                                  }
                              );
        
        System.out.println("One-pass min: " + result[0] + ", max: " + result[1]);
    }
}
```

**Key Concepts:**
- `IntStream.min()` and `IntStream.max()` directly find minimum and maximum values
- For object streams, comparators must be provided: `min(Comparator)` and `max(Comparator)`
- `reduce()` allows finding both values in a single pass
- Streams can only be consumed once, so they must be recreated for multiple operations
- `Optional` is used to handle potentially empty streams

[Back to Top](#table-of-contents)

## 16. Find Prime Number

Using Java 8 to check and find prime numbers:

```java
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrimeNumbers {
    public static void main(String[] args) {
        int num = 17;
        
        // Check if a number is prime
        boolean isPrime = isPrime(num);
        System.out.println(num + " is prime: " + isPrime);
        
        // Find all prime numbers up to a limit
        int limit = 50;
        List<Integer> primes = findPrimesUpTo(limit);
        System.out.println("Prime numbers up to " + limit + ": " + primes);
        
        // Find first n prime numbers
        int n = 10;
        List<Integer> firstNPrimes = findFirstNPrimes(n);
        System.out.println("First " + n + " prime numbers: " + firstNPrimes);
    }
    
    // Check if a number is prime
    private static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        
        return IntStream.rangeClosed(2, (int) Math.sqrt(number))
                     .noneMatch(n -> number % n == 0);
    }
    
    // Find all prime numbers up to a limit
    private static List<Integer> findPrimesUpTo(int limit) {
        return IntStream.rangeClosed(2, limit)
                     .filter(PrimeNumbers::isPrime)
                     .boxed()
                     .collect(Collectors.toList());
    }
    
    // Find first n prime numbers
    private static List<Integer> findFirstNPrimes(int n) {
        return IntStream.iterate(2, i -> i + 1)
                     .filter(PrimeNumbers::isPrime)
                     .limit(n)
                     .boxed()
                     .collect(Collectors.toList());
    }
}
```

**Key Concepts:**
- `noneMatch(n -> number % n == 0)` checks that no divisors exist
- Only checking divisors up to the square root of the number improves efficiency
- `IntStream.rangeClosed()` creates a range of numbers to check
- `filter(PrimeNumbers::isPrime)` applies the primality test
- `boxed()` converts primitive int values to Integer objects
- `IntStream.iterate()` with `limit()` finds exactly n prime numbers

[Back to Top](#table-of-contents)

## 17. Group Elements by a Property

Using Java 8 streams to group elements by different properties:

```java
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupByExample {
    public static void main(String[] args) {
        // Example with Person objects
        List<Person> people = Arrays.asList(
            new Person("John", "Smith", 28, "IT"),
            new Person("Jane", "Doe", 32, "HR"),
            new Person("Steve", "Jones", 45, "IT"),
            new Person("Mary", "Johnson", 28, "Finance"),
            new Person("Mike", "Brown", 32, "IT"),
            new Person("Susan", "Miller", 45, "HR")
        );
        
        // Group by age
        Map<Integer, List<Person>> byAge = people.stream()
            .collect(Collectors.groupingBy(Person::getAge));
        
        System.out.println("Grouped by Age:");
        byAge.forEach((age, personList) -> 
            System.out.println(age + ": " + personList.stream()
                              .map(Person::getFirstName)
                              .collect(Collectors.joining(", "))));
        
        // Group by department
        Map<String, List<Person>> byDepartment = people.stream()
            .collect(Collectors.groupingBy(Person::getDepartment));
        
        System.out.println("\nGrouped by Department:");
        byDepartment.forEach((dept, personList) -> 
            System.out.println(dept + ": " + personList.stream()
                              .map(p -> p.getFirstName() + " " + p.getLastName())
                              .collect(Collectors.joining(", "))));
        
        // Group by department and count
        Map<String, Long> departmentCount = people.stream()
            .collect(Collectors.groupingBy(Person::getDepartment, Collectors.counting()));
        
        System.out.println("\nDepartment Counts:");
        departmentCount.forEach((dept, count) -> 
            System.out.println(dept + ": " + count));
    }
    
    static class Person {
        private String firstName;
        private String lastName;
        private int age;
        private String department;
        
        public Person(String firstName, String lastName, int age, String department) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.department = department;
        }
        
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public int getAge() { return age; }
        public String getDepartment() { return department; }
        
        @Override
        public String toString() {
            return firstName + " " + lastName + " (" + age + ", " + department + ")";
        }
    }
}
```

**Key Concepts:**
- `Collectors.groupingBy()` groups elements by a specified function
- The function can be a method reference (`Person::getAge`) or a lambda expression
- Downstream collectors can be added for further processing
- `Collectors.counting()` counts elements in each group
- Multiple-level grouping is possible with nested `groupingBy()` calls

[Back to Top](#table-of-contents)

## 18. Convert List to Map

Converting lists to maps using Java 8 streams:

```java
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListToMapExample {
    public static void main(String[] args) {
        // Example with simple string list
        List<String> fruits = Arrays.asList("apple", "banana", "orange", "mango");
        
        // Convert to Map with string length as value
        Map<String, Integer> fruitLengths = fruits.stream()
            .collect(Collectors.toMap(
                Function.identity(),  // Key is the fruit name
                String::length        // Value is the string length
            ));
        
        System.out.println("Fruit lengths: " + fruitLengths);
        
        // Example with Product objects
        List<Product> products = Arrays.asList(
            new Product(1, "Laptop", 999.99),
            new Product(2, "Phone", 599.99),
            new Product(3, "Tablet", 399.99),
            new Product(4, "Watch", 199.99)
        );
        
        // Convert to Map with ID as key and Product as value
        Map<Integer, Product> productMap = products.stream()
            .collect(Collectors.toMap(
                Product::getId,     // Key mapper
                Function.identity() // Value mapper (the product itself)
            ));
        
        System.out.println("\nProduct map by ID:");
        productMap.forEach((id, product) -> 
            System.out.println(id + " -> " + product));
        
        // Convert to map with name as key and price as value
        // Handle potential duplicate keys with a merge function
        Map<String, Double> productPrices = products.stream()
            .collect(Collectors.toMap(
                Product::getName,          // Key mapper
                Product::getPrice,         // Value mapper
                (existing, replacement) -> existing  // Merge function for duplicates
            ));
        
        System.out.println("\nProduct prices by name:");
        productPrices.forEach((name, price) -> 
            System.out.println(name + ": $" + price));
    }
    
    static class Product {
        private int id;
        private String name;
        private double price;
        
        public Product(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        
        @Override
        public String toString() {
            return name + " ($" + price + ")";
        }
    }
}
```

**Key Concepts:**
- `Collectors.toMap()` converts a stream to a Map
- First parameter is the key mapper function
- Second parameter is the value mapper function
- Third (optional) parameter is a merge function to handle duplicate keys
- `Function.identity()` is used when the element itself should be the key or value
- Method references can be used for extracting specific properties

[Back to Top](#table-of-contents)

## 19. Parallel Processing with Streams

Utilizing parallel streams for improved performance:

```java
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class ParallelStreamExample {
    public static void main(String[] args) {
        // Example 1: Sum of large range of numbers
        Instant start = Instant.now();
        
        long sequentialSum = LongStream.rangeClosed(1, 100_000_000)
                                    .sum();
        
        Instant finish = Instant.now();
        long timeElapsedSequential = Duration.between(start, finish).toMillis();
        
        System.out.println("Sequential sum: " + sequentialSum);
        System.out.println("Time taken with sequential stream: " + timeElapsedSequential + " ms");
        
        // Same operation with parallel stream
        start = Instant.now();
        
        long parallelSum = LongStream.rangeClosed(1, 100_000_000)
                                  .parallel()
                                  .sum();
        
        finish = Instant.now();
        long timeElapsedParallel = Duration.between(start, finish).toMillis();
        
        System.out.println("Parallel sum: " + parallelSum);
        System.out.println("Time taken with parallel stream: " + timeElapsedParallel + " ms");
        
        // Example 2: Complex calculations on list elements
        List<Integer> numbers = IntStream.rangeClosed(1, 10_000)
                                      .boxed()
                                      .toList();
        
        // Sequential stream
        start = Instant.now();
        
        long count = numbers.stream()
                         .filter(ParallelStreamExample::isPrime)
                         .count();
        
        finish = Instant.now();
        timeElapsedSequential = Duration.between(start, finish).toMillis();
        
        System.out.println("\nNumber of primes found (sequential): " + count);
        System.out.println("Time taken: " + timeElapsedSequential + " ms");
        
        // Parallel stream
        start = Instant.now();
        
        count = numbers.parallelStream()
                    .filter(ParallelStreamExample::isPrime)
                    .count();
        
        finish = Instant.now();
        timeElapsedParallel = Duration.between(start, finish).toMillis();
        
        System.out.println("Number of primes found (parallel): " + count);
        System.out.println("Time taken: " + timeElapsedParallel + " ms");
    }
    
    // Method to check if a number is prime
    private static boolean isPrime(int number) {
        if (number <= 1) return false;
        if (number <= 3) return true;
        if (number % 2 == 0 || number % 3 == 0) return false;
        
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
}
```

**Key Concepts:**
- `parallel()` converts a sequential stream to a parallel one
- `parallelStream()` creates a parallel stream directly from a collection
- Parallel streams use the fork/join framework and common pool
- Best performance gains come with:
  - Large data sets
  - Computationally intensive operations
  - Stateless, independent operations
- Not all operations benefit from parallelization due to coordination overhead
- Thread-safe accumulators (like `AtomicLong`) should be used when necessary

[Back to Top](#table-of-contents)

## 20. Working with Optional

Using Java 8's Optional to handle nullable values safely:

```java
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class OptionalExample {
    public static void main(String[] args) {
        // Creating Optional objects
        Optional<String> empty = Optional.empty();
        Optional<String> nonEmpty = Optional.of("Hello");
        
        // Optional.ofNullable - allows null value
        String nullableValue = null;
        Optional<String> nullable = Optional.ofNullable(nullableValue);
        Optional<String> nonNullable = Optional.ofNullable("Hello");
        
        // isPresent() and isEmpty() checks
        System.out.println("Empty Optional: " + empty.isPresent());      // false
        System.out.println("Empty Optional (isEmpty): " + empty.isEmpty()); // true
        System.out.println("Non-Empty Optional: " + nonEmpty.isPresent()); // true
        
        // ifPresent() consumer
        nonEmpty.ifPresent(value -> System.out.println("Value is present: " + value));
        
        // ifPresentOrElse() handle both cases
        nullable.ifPresentOrElse(
            value -> System.out.println("Value is present: " + value),
            () -> System.out.println("Value is not present")
        );
        
        // orElse vs orElseGet
        String fallbackValue = empty.orElse("Fallback");
        System.out.println("orElse result: " + fallbackValue);
        
        // orElseGet uses supplier - lazy evaluation
        String lazyFallback = empty.orElseGet(() -> {
            System.out.println("Computing fallback value...");
            return "Computed Fallback";
        });
        System.out.println("orElseGet result: " + lazyFallback);
        
        // orElseThrow
        try {
            String value = empty.orElseThrow(() -> new RuntimeException("Value not present"));
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
        
        // map() transformation
        String transformedValue = nonEmpty
            .map(String::toUpperCase)
            .orElse("No value");
        System.out.println("Transformed: " + transformedValue);
        
        // filter()
        Optional<String> filtered = nonEmpty
            .filter(value -> value.length() > 10);
        System.out.println("Filtered is present: " + filtered.isPresent());
        
        // flatMap() example with nested Optionals
        Optional<User> user = findUserById(1);
        
        // Without flatMap (verbose)
        if (user.isPresent()) {
            Optional<Address> address = user.get().getAddress();
            if (address.isPresent()) {
                Optional<Country> country = address.get().getCountry();
                country.ifPresent(c -> System.out.println("User's country: " + c.getName()));
            }
        }
        
        // With flatMap (cleaner)
        String userCountry = user
            .flatMap(User::getAddress)
            .flatMap(Address::getCountry)
            .map(Country::getName)
            .orElse("Unknown");
        
        System.out.println("User's country with flatMap: " + userCountry);
        
        // Stream from Optional
        Stream<String> streamFromOptional = nonEmpty.stream();
        streamFromOptional.forEach(System.out::println);
        
        // Multiple Optionals with Stream
        List<Optional<String>> listOfOptionals = Arrays.asList(
            Optional.empty(),
            Optional.of("Hello"),
            Optional.empty(),
            Optional.of("World")
        );
        
        // Filter out empty Optionals and get values
        List<String> filteredList = listOfOptionals.stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
        
        System.out.println("Filtered non-empty values: " + filteredList);
    }
    
    // Example classes for flatMap demonstration
    static class User {
        private final int id;
        private final String name;
        private final Optional<Address> address;
        
        User(int id, String name, Optional<Address> address) {
            this.id = id;
            this.name = name;
            this.address = address;
        }
        
        public Optional<Address> getAddress() {
            return address;
        }
    }
    
    static class Address {
        private final String street;
        private final Optional<Country> country;
        
        Address(String street, Optional<Country> country) {
            this.street = street;
            this.country = country;
        }
        
        public Optional<Country> getCountry() {
            return country;
        }
    }
    
    static class Country {
        private final String name;
        
        Country(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
    
    // Example method returning an Optional
    private static Optional<User> findUserById(int id) {
        // Simulate database lookup
        if (id == 1) {
            Country country = new Country("USA");
            Address address = new Address("123 Main St", Optional.of(country));
            return Optional.of(new User(1, "John", Optional.of(address)));
        }
        return Optional.empty();
    }
}
```

**Key Concepts:**
- `Optional` is a container that may or may not contain a value
- Created using `Optional.of()`, `Optional.empty()`, or `Optional.ofNullable()`
- `isPresent()` and `isEmpty()` check for value presence
- `ifPresent()` executes an action if value exists
- `orElse()` provides a default value if Optional is empty
- `orElseGet()` lazily provides a default via Supplier function
- `orElseThrow()` throws exception if value is absent
- `map()` transforms the contained value if present
- `flatMap()` helps with nested Optionals
- `filter()` conditionally keeps or empties an Optional
- `stream()` converts Optional to a Stream with 0 or 1 element

[Back to Top](#table-of-contents)


    