# Java Stream Collect Method - Study Notes

## Table of Contents

1. [Basic Collections](#1-basic-collections)
2. [String Operations](#2-string-operations)
3. [Parallel Processing Example](#3-parallel-processing-example)
4. [Mathematical Operations](#4-mathematical-operations)
5. [Frequency Counting](#5-frequency-counting)
6. [Advanced Examples](#6-advanced-examples)
7. [Conditional Collection](#7-conditional-collection)

---

## 1. Basic Collections

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
```

**Description**: Shows how to collect stream elements into different collection types using the three-parameter collect method.

---

## 2. String Operations

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

---

## 3. Parallel Processing Example

```java
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String result = Stream.of("apple", "banana", "cherry", "date")
            .parallel() // Makes the stream parallel
            .collect(
                StringBuilder::new,
                (sb, s) -> {
                    System.out.println(Thread.currentThread().getName() + " - Accumulator: " + s);
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(s);
                },
                (sb1, sb2) -> {
                    System.out.println(Thread.currentThread().getName() + " - Combiner:");
                    if (sb1.length() > 0 && sb2.length() > 0) sb1.append(", ");
                    sb1.append(sb2);
                }
            ).toString();

        System.out.println("\nFinal Result (Parallel): " + result);
    }
}
```

**Description**: Shows how the combiner function works in parallel streams by printing thread names during execution.

---

## 4. Mathematical Operations

```java   
// Sum using collect
Integer sum = Stream.of(1, 2, 3, 4, 5)
    .collect(() -> new int[1], // Supplier: array to hold sum
            (arr, val) -> arr[0] += val, // Accumulator
            (arr1, arr2) -> arr1[0] += arr2[0]) // Combiner
    [0];
System.out.println("Sum: " + sum);
```   

**Description**: Calculates sum using collect method with an array as the container.

---

## 5. Frequency Counting

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

---

## 6. Advanced Examples

```java
// Collect into multiple data structures simultaneously
class MultiCollector {
    List<String> list = new ArrayList<>();
    Set<String> set = new HashSet<>();
    int count = 0;
    
    void add(String item) {
        list.add(item);
        set.add(item);
        count++;
    }
    
    void combine(MultiCollector other) {
        list.addAll(other.list);
        set.addAll(other.set);
        count += other.count;
    }
    
    @Override
    public String toString() {
        return String.format("List: %s, Set: %s, Count: %d", list, set, count);
    }
}

MultiCollector multi = Stream.of("apple", "banana", "apple", "cherry")
    .collect(MultiCollector::new,
            MultiCollector::add,
            MultiCollector::combine);
System.out.println("Multi-collector: " + multi);

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
```

**Description**: Shows custom collector class, transformation during collection, and priority queue usage.

---

## 7. Conditional Collection

```java
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

**Description**: Demonstrates conditional collection where elements are added only if they meet specific criteria.

---

## Key Pattern

**Collect Method Structure:**
- **Supplier**: Creates the container (e.g., `ArrayList::new`)
- **Accumulator**: Adds elements to container (e.g., `List::add`)  
- **Combiner**: Merges containers for parallel processing (e.g., `List::addAll`)