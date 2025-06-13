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
  <strong>Let's connect professionally:</strong>
  <a href="https://www.linkedin.com/in/anil-valsa/" target="_blank" rel="noopener noreferrer">Anil Valsa on LinkedIn</a>
</p>

---

Java 8 introduced a paradigm shift in Java programming with functional programming concepts, lambdas, streams API, and new date-time API. This document provides comprehensive solutions to common Java 8 programming questions frequently asked in interviews.

## Additional Resources

If you found this guide helpful, you might also be interested in my other Spring Framework resources:
- [Core Java & Java-8 Interview Questions](https://github.com/anilvn/Java-Interview-Questions/tree/main)
- [Spring Boot Interview Questions](https://github.com/anilvn/spring-boot-interview-questions/tree/main)
- [Microservices with Spring Cloud Tutorials](https://javatechonline.com/microservices-tutorial/)

Feel free to star and fork these repositories if you find them useful!

---

## Table of Contents

| # | Question | Category |
|---|----------|----------|
| **Basic Operations** |
| 1 | [Find the Frequency of Each Character in a Given String](#1-find-the-frequency-of-each-character-in-a-given-string) | String Processing |
| 2 | [Find the Maximum Number and Minimum Number in a List](#2-find-the-maximum-number-and-minimum-number-in-a-list) | Collections |
| 3 | [Find the Second Largest Number in the List of Integers](#3-find-the-second-largest-number-in-the-list-of-integers) | Collections |
| 4 | [Find the Age of a Person if the Birthday Date has Given](#4-find-the-age-of-a-person-if-the-birthday-date-has-given) | Date/Time |
| 5 | [Find the Sum of All Digits of a Number](#5-find-the-sum-of-all-digits-of-a-number) | Number Operations |
| 6 | [Print Even Numbers from a List](#6-print-even-numbers-from-a-list) | Filtering |
| 7 | [Remove Duplicate Elements from a List](#7-remove-duplicate-elements-from-a-list) | Collections |
| 8 | [Retrieve Last Element of a List of Strings](#8-retrieve-last-element-of-a-list-of-strings) | Collections |
| 9 | [Reverse Each Word of String](#9-reverse-each-word-of-string) | String Processing |
| 10 | [Sort List of Strings Alphabetically](#10-sort-list-of-strings-alphabetically) | Sorting |
| **String Manipulation** |
| 11 | [Count Characters in a String](#11-count-characters-in-a-string) | String Processing |
| 12 | [Find First Repeated Character in a String](#12-find-first-repeated-character-in-a-string) | String Processing |
| 13 | [Reverse a String](#13-reverse-a-string) | String Processing |
| **Stream Operations** |
| 14 | [Concatenate Two Streams](#14-concatenate-two-streams) | Stream Operations |
| 15 | [Find the Minimum and Maximum Number of a Stream](#15-find-the-minimum-and-maximum-number-of-a-stream) | Stream Operations |
| **Number Operations** |
| 16 | [Find Prime Number](#16-find-prime-number) | Mathematical |
| **Advanced Concepts** |
| 17 | [Group Elements by a Property](#17-group-elements-by-a-property) | Collectors |
| 18 | [Convert List to Map](#18-convert-list-to-map) | Collectors |
| 19 | [Parallel Processing with Streams](#19-parallel-processing-with-streams) | Performance |
| 20 | [Working with Optional](#20-working-with-optional) | Optional API |
| **Fundamentals** |
| 21 | [Basic Collections](#basic-collections) | Collections |
| 22 | [String Operations](#string-operations) | String Processing |

---

## Basic Collections

```java        
// LinkedList collection
List<String> linkedList = Stream.of("apple", "banana", "cherry")
    .collect(LinkedList::new,
            LinkedList::add,
            LinkedList::addAll);
System.out.println("LinkedList: " + linkedList);

// HashSet collection (eliminates duplicates)
Set<String> hashSet = Stream.of("apple", "banana", "cherry", "apple")
    .collect(HashSet::new,
            HashSet::add,
            HashSet::addAll);
System.out.println("HashSet: " + hashSet);

// TreeSet collection (sorted, no duplicates)
Set<String> treeSet = Stream.of("cherry", "apple", "banana")
    .collect(TreeSet::new,
            TreeSet::add,
            TreeSet::addAll);
System.out.println("TreeSet: " + treeSet);

// Vector collection
Vector<String> vector = Stream.of("apple", "banana", "cherry")
    .collect(Vector::new,
            Vector::add,
            Vector::addAll);
System.out.println("Vector: " + vector);

// ArrayDeque collection
Deque<String> deque = Stream.of("apple", "banana", "cherry")
    .collect(ArrayDeque::new,
            ArrayDeque::add,
            ArrayDeque::addAll);
System.out.println("ArrayDeque: " + deque);

// Collect with transformation
List<String> upperCaseList = Stream.of("apple", "banana", "cherry")
    .collect(ArrayList::new,
            (list, item) -> list.add(item.toUpperCase()),
            ArrayList::addAll);
System.out.println("Uppercase list: " + upperCaseList);

// Priority queue (maintains order based on natural ordering)
PriorityQueue<Integer> pq = Stream.of(5, 2, 8, 1, 9, 3)
    .collect(PriorityQueue::new,
            PriorityQueue::add,
            PriorityQueue::addAll);
System.out.println("Priority Queue: " + pq);

// Collect only if condition is met
List<Integer> conditionalList = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    .collect(ArrayList::new,
            (list, num) -> {
                if (num % 2 == 0 && num > 4) {
                    list.add(num);
                }
            },
            ArrayList::addAll);
System.out.println("Conditional collection (even > 4): " + conditionalList);
```

**Description**: Shows how to collect stream elements into different collection types using the three-parameter collect method.

### Approach 2 - Using Standard Collectors

```java
// Simpler approaches using built-in collectors
List<String> list = Stream.of("apple", "banana", "cherry")
    .collect(Collectors.toList());

Set<String> set = Stream.of("apple", "banana", "cherry", "apple")
    .collect(Collectors.toSet());

Map<String, Integer> lengthMap = Stream.of("apple", "banana", "cherry")
    .collect(Collectors.toMap(Function.identity(), String::length));
```

---

## String Operations

```java   
// Concatenating strings
String concatenated = Stream.of("Hello", " ", "World", "!")
    .collect(StringBuilder::new,
            StringBuilder::append,
            StringBuilder::append)
    .toString();
System.out.println("Concatenated: " + concatenated);

// Joining with custom separator
String joined = Stream.of("apple", "banana", "cherry")
    .collect(
        // 1. Supplier: creates a new, empty StringBuilder
        StringBuilder::new,
        // 2. Accumulator: for each element in the stream
        (sb, s) -> {
            if (sb.length() > 0)         // If the StringBuilder is not empty,
                sb.append(", ");         // add a separator ", " before adding next word.
            sb.append(s);                // Append the current string (s) to the StringBuilder.
        },
        // 3. Combiner: used only in parallel streams to merge two partial StringBuilders
        (sb1, sb2) -> {
            if (sb1.length() > 0 && sb2.length() > 0) // If both builders have content,
                sb1.append(", ");                     // add a comma before merging
            sb1.append(sb2);                          // append sb2 content into sb1
        }
    ).toString(); // Convert final StringBuilder to String
```

**Description**: Demonstrates string building and joining with custom separators using StringBuilder.

### Approach 2 - Using Collectors.joining()

```java
// Much simpler approach using built-in joining collector
String joined = Stream.of("apple", "banana", "cherry")
    .collect(Collectors.joining(", "));

// With prefix and suffix
String formatted = Stream.of("apple", "banana", "cherry")
    .collect(Collectors.joining(", ", "[", "]"));
```

---

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

### Approach 2 - Using accumulator and merge operator

```java
// Count character frequencies
Map<Character, Integer> charFreq = "hello world".chars()
    .mapToObj(c -> (char) c)
    .filter(c -> c != ' ')
    .collect(HashMap::new,
            (map, ch) -> map.merge(ch, 1, Integer::sum),
            (map1, map2) -> {
                map2.forEach((k, v) -> map1.merge(k, v, Integer::sum));
                return map1;
            });
System.out.println("Character frequencies: " + charFreq);
```

**Description**: Counts frequency of characters in a string using HashMap with merge operations.

### Approach 3 - Using Traditional Loop with Stream Enhancement

```java
// For very large strings, a hybrid approach might be more memory efficient
Map<Character, Integer> freqMap = new HashMap<>();
input.chars()
     .forEach(c -> freqMap.merge((char) c, 1, Integer::sum));
```

[Back to Top](#table-of-contents)

---

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

### Approach 2 - Using IntSummaryStatistics

```java
IntSummaryStatistics stats = numbers.stream()
    .mapToInt(Integer::intValue)
    .summaryStatistics();

System.out.println("Min: " + stats.getMin());
System.out.println("Max: " + stats.getMax());
System.out.println("Average: " + stats.getAverage());
System.out.println("Count: " + stats.getCount());
System.out.println("Sum: " + stats.getSum());
```

[Back to Top](#table-of-contents)

---

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

### Approach 2 - Using TreeSet for Natural Ordering

```java
// Using TreeSet to automatically handle duplicates and sorting
TreeSet<Integer> sortedSet = numbers.stream()
    .collect(Collectors.toCollection(TreeSet::new));

if (sortedSet.size() >= 2) {
    Integer largest = sortedSet.last();
    sortedSet.remove(largest);
    Integer secondLargest = sortedSet.last();
    System.out.println("Second largest: " + secondLargest);
}
```

[Back to Top](#table-of-contents)

---

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

### Approach 2 - Using ChronoUnit for Simple Year Calculation

```java
import java.time.temporal.ChronoUnit;

// Simple age calculation in years only
long ageInYears = ChronoUnit.YEARS.between(birthDate, LocalDate.now());
System.out.println("Age: " + ageInYears + " years");

// Age in days
long ageInDays = ChronoUnit.DAYS.between(birthDate, LocalDate.now());
System.out.println("Age in days: " + ageInDays);
```

[Back to Top](#table-of-contents)

---

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

### Approach 3 - Using Recursive Approach with Streams

```java
// Functional approach using recursion
private static int sumDigitsRecursive(int number) {
    return number == 0 ? 0 : 
           (number % 10) + sumDigitsRecursive(number / 10);
}

// Or using a more mathematical approach
private static int sumDigitsMath(int number) {
    return String.valueOf(Math.abs(number))
            .chars()
            .map(c -> c - '0')  // Convert char to digit
            .sum();
}
```

[Back to Top](#table-of-contents)

---

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

### Approach 2 - Using Predicate for Reusability

```java
// Define reusable predicates
Predicate<Integer> isEven = n -> n % 2 == 0;
Predicate<Integer> isPositive = n -> n > 0;

// Use predicates
List<Integer> positiveEvenNumbers = numbers.stream()
    .filter(isEven.and(isPositive))
    .collect(Collectors.toList());
```

[Back to Top](#table-of-contents)

---

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

### Approach 2 - Using LinkedHashSet to Preserve Order

```java
// Using LinkedHashSet to maintain insertion order
List<Integer> distinctPreserveOrder = numbersWithDuplicates.stream()
    .collect(Collectors.toCollection(LinkedHashSet::new))
    .stream()
    .collect(Collectors.toList());

// Or more concisely
Set<Integer> uniqueSet = new LinkedHashSet<>(numbersWithDuplicates);
List<Integer> distinctList = new ArrayList<>(uniqueSet);
```

[Back to Top](#table-of-contents)

---

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

### Approach 2 - Using Collections.reverse()

```java
// For very large lists, this might be more efficient
Optional<String> lastElement = fruits.stream()
    .collect(Collectors.collectingAndThen(
        Collectors.toList(),
        list -> {
            Collections.reverse(list);
            return list.stream().findFirst();
        }
    ));
```

[Back to Top](#table-of-contents)

---

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

### Approach 2 - Using Inline Lambda

```java
// More concise with inline lambda
String reversed = Arrays.stream(input.split(" "))
    .map(word -> new StringBuilder(word).reverse().toString())
    .collect(Collectors.joining(" "));

// Or using streams for character reversal
String reversed2 = Arrays.stream(input.split(" "))
    .map(word -> word.chars()
                    .mapToObj(c -> (char) c)
                    .collect(StringBuilder::new, 
                           (sb, c) -> sb.insert(0, c), 
                           StringBuilder::append)
                    .toString())
    .collect(Collectors.joining(" "));
```

[Back to Top](#table-of-contents)

---


<!-- ```java
// Sort by multiple criteria: length descending, then alphabetically
List<String> customSorted = fruits.stream()
    .sorted(Comparator.comparing(String::length, Comparator.reverseOrder());
```
- continued. -->
## 10. Sort List of Strings Alphabetically

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

Using Java 8 to group collections by properties using Collectors.groupingBy():

```java
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Person {
    private String name;
    private int age;
    private String city;
    
    public Person(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }
    
    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getCity() { return city; }
    
    @Override
    public String toString() {
        return String.format("%s (%d, %s)", name, age, city);
    }
}

public class GroupingElements {
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
            new Person("Alice", 30, "New York"),
            new Person("Bob", 25, "London"),
            new Person("Charlie", 30, "New York"),
            new Person("David", 25, "Paris"),
            new Person("Eve", 35, "London")
        );
        
        // Group by city
        Map<String, List<Person>> groupedByCity = people.stream()
                                                      .collect(Collectors.groupingBy(Person::getCity));
        
        System.out.println("Grouped by city:");
        groupedByCity.forEach((city, persons) -> 
            System.out.println(city + ": " + persons));
        
        // Group by age
        Map<Integer, List<Person>> groupedByAge = people.stream()
                                                      .collect(Collectors.groupingBy(Person::getAge));
        
        System.out.println("\nGrouped by age:");
        groupedByAge.forEach((age, persons) -> 
            System.out.println(age + ": " + persons));
        
        // Group by age range (custom classifier)
        Map<String, List<Person>> groupedByAgeRange = people.stream()
                                                          .collect(Collectors.groupingBy(person -> {
                                                              if (person.getAge() < 30) return "Young";
                                                              else if (person.getAge() < 40) return "Middle";
                                                              else return "Senior";
                                                          }));
        
        System.out.println("\nGrouped by age range:");
        groupedByAgeRange.forEach((range, persons) -> 
            System.out.println(range + ": " + persons));
        
        // Count elements in each group
        Map<String, Long> countByCity = people.stream()
                                           .collect(Collectors.groupingBy(
                                               Person::getCity,
                                               Collectors.counting()
                                           ));
        
        System.out.println("\nCount by city:");
        countByCity.forEach((city, count) -> 
            System.out.println(city + ": " + count));
    }
}
```

**Key Concepts:**
- `Collectors.groupingBy()` groups stream elements by a classifier function
- Classifier function extracts the grouping key from each element
- Results in a `Map<Key, List<Value>>` where key is the grouping criteria
- Can use method references like `Person::getCity` or custom lambda expressions
- Secondary collectors like `Collectors.counting()` can aggregate grouped data
- Complex grouping logic can be implemented with custom classifier functions

[Back to Top](#table-of-contents)

## 18. Convert List to Map

Converting lists to maps using Java 8 streams:

```java
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class Employee {
    private int id;
    private String name;
    private double salary;
    
    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public double getSalary() { return salary; }
    
    @Override
    public String toString() {
        return String.format("Employee{id=%d, name='%s', salary=%.2f}", id, name, salary);
    }
}

public class ListToMap {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee(1, "Alice", 50000),
            new Employee(2, "Bob", 60000),
            new Employee(3, "Charlie", 55000),
            new Employee(4, "David", 65000)
        );
        
        // Convert to Map: ID -> Employee
        Map<Integer, Employee> employeeMap = employees.stream()
                                                   .collect(Collectors.toMap(
                                                       Employee::getId,
                                                       Function.identity()
                                                   ));
        
        System.out.println("ID -> Employee Map:");
        employeeMap.forEach((id, emp) -> System.out.println(id + " -> " + emp));
        
        // Convert to Map: Name -> Salary
        Map<String, Double> nameSalaryMap = employees.stream()
                                                  .collect(Collectors.toMap(
                                                      Employee::getName,
                                                      Employee::getSalary
                                                  ));
        
        System.out.println("\nName -> Salary Map:");
        nameSalaryMap.forEach((name, salary) -> 
            System.out.println(name + " -> $" + salary));
        
        // Handle duplicate keys with merge function
        List<String> words = Arrays.asList("apple", "banana", "apple", "cherry", "banana");
        
        Map<String, Integer> wordCount = words.stream()
                                           .collect(Collectors.toMap(
                                               Function.identity(),
                                               word -> 1,
                                               Integer::sum  // Merge function for duplicates
                                           ));
        
        System.out.println("\nWord count map:");
        wordCount.forEach((word, count) -> 
            System.out.println(word + " appears " + count + " times"));
        
        // Convert to concurrent map
        Map<Integer, String> concurrentMap = employees.parallelStream()
                                                   .collect(Collectors.toConcurrentMap(
                                                       Employee::getId,
                                                       Employee::getName
                                                   ));
        
        System.out.println("\nConcurrent Map (ID -> Name):");
        concurrentMap.forEach((id, name) -> System.out.println(id + " -> " + name));
    }
}
```

**Key Concepts:**
- `Collectors.toMap()` converts stream elements to a Map
- Takes key mapper and value mapper functions as parameters
- `Function.identity()` returns the element itself as the value
- Merge function handles duplicate keys (third parameter)
- `Collectors.toConcurrentMap()` creates thread-safe maps for parallel streams
- Duplicate keys without merge function will throw `IllegalStateException`

[Back to Top](#table-of-contents)

## 19. Parallel Processing with Streams

Using parallel streams for improved performance with large datasets:

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ParallelStreams {
    public static void main(String[] args) {
        // Create a large list of integers
        List<Integer> largeList = IntStream.range(1, 10_000_000)
                                       .boxed()
                                       .collect(java.util.stream.Collectors.toList());
        
        // Sequential processing
        long startTime = System.currentTimeMillis();
        long sequentialSum = largeList.stream()
                                   .mapToLong(Integer::longValue)
                                   .sum();
        long sequentialTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Sequential sum: " + sequentialSum);
        System.out.println("Sequential time: " + sequentialTime + " ms");
        
        // Parallel processing
        startTime = System.currentTimeMillis();
        long parallelSum = largeList.parallelStream()
                                 .mapToLong(Integer::longValue)
                                 .sum();
        long parallelTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Parallel sum: " + parallelSum);
        System.out.println("Parallel time: " + parallelTime + " ms");
        System.out.println("Speedup: " + (double)sequentialTime / parallelTime + "x");
        
        // Example with CPU-intensive operations
        demonstrateCPUIntensiveParallelism();
        
        // Showing when NOT to use parallel streams
        demonstrateWhenNotToUseParallel();
    }
    
    private static void demonstrateCPUIntensiveParallelism() {
        System.out.println("\n--- CPU Intensive Task ---");
        List<Integer> numbers = IntStream.range(1, 100_000)
                                      .boxed()
                                      .collect(java.util.stream.Collectors.toList());
        
        // CPU-intensive function
        java.util.function.Function<Integer, Double> expensiveOperation = n -> {
            double result = 0;
            for (int i = 0; i < 1000; i++) {
                result += Math.sqrt(n * i);
            }
            return result;
        };
        
        // Sequential
        long startTime = System.currentTimeMillis();
        double sequentialResult = numbers.stream()
                                      .mapToDouble(expensiveOperation::apply)
                                      .sum();
        long sequentialTime = System.currentTimeMillis() - startTime;
        
        // Parallel
        startTime = System.currentTimeMillis();
        double parallelResult = numbers.parallelStream()
                                    .mapToDouble(expensiveOperation::apply)
                                    .sum();
        long parallelTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Sequential time: " + sequentialTime + " ms");
        System.out.println("Parallel time: " + parallelTime + " ms");
        System.out.println("Results match: " + (Math.abs(sequentialResult - parallelResult) < 0.001));
    }
    
    private static void demonstrateWhenNotToUseParallel() {
        System.out.println("\n--- When NOT to use Parallel ---");
        List<Integer> smallList = Arrays.asList(1, 2, 3, 4, 5);
        
        // Small dataset - parallel overhead not worth it
        long startTime = System.currentTimeMillis();
        int sequentialSum = smallList.stream().mapToInt(Integer::intValue).sum();
        long sequentialTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int parallelSum = smallList.parallelStream().mapToInt(Integer::intValue).sum();
        long parallelTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Small list sequential time: " + sequentialTime + " ms");
        System.out.println("Small list parallel time: " + parallelTime + " ms");
        System.out.println("Parallel overhead not beneficial for small datasets");
    }
}
```

**Key Concepts:**
- `parallelStream()` creates a parallel stream that utilizes multiple CPU cores
- Best for CPU-intensive operations and large datasets
- Uses ForkJoinPool with default parallelism level equal to CPU cores
- Not always faster - overhead exists for thread management
- Avoid for small datasets or I/O operations
- Operations must be stateless and thread-safe
- Results may vary in order compared to sequential streams

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
