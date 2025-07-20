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
```

**Description**: Shows custom collector class, transformation during collection, and priority queue usage.


---

## Key Pattern

**Collect Method Structure:**
- **Supplier**: Creates the container (e.g., `ArrayList::new`)
- **Accumulator**: Adds elements to container (e.g., `List::add`)  
- **Combiner**: Merges containers for parallel processing (e.g., `List::addAll`)


<br/>
<br/><br/>

```java
// Simple POJO representing a JobSeeker
    class JobSeeker {
        private String name;
        private List<String> skills; 
        // gettter, setter, constructors
    }
```
```java
// List of job seekers (some match more, some fewer skills)
        List<JobSeeker> jobSeekers = List.of(
            new JobSeeker("Alice", List.of("Java", "Spring", "Docker")),             // matches 3
            new JobSeeker("Bob", List.of("Java", "Spring Boot")),                    // matches 1
            new JobSeeker("Charlie", List.of("Java", "Spring", "Docker", "Angular")),// matches 4
            new JobSeeker("David", List.of("C++", "Python"))                         // matches 0
        );
```

```java
        // List of required skills (size=5)
        List<String> reqSkills = List.of("Java", "Spring", "Docker", "Angular", "SQL");

        // jobseeker matched all skills- exact match.
        List<JobSeeker> filtered = jobSeekers.stream()
                .filter(js -> js.getSkills().containsAll(reqSkills))
                .collect(Collectors.toList());
```


```java
// Minimum number of matching skills required
        int minMatchedSkills = 3;

        // Filtering: keep candidates who have at least minMatchedSkills from reqSkills
        List<JobSeeker> filtered = jobSeekers.stream()
            .filter(js -> {
                long matched = js.getSkills().stream()
                        .filter(reqSkills::contains)
                        .count();
                return matched >= minMatchedSkills;
            })
            .collect(Collectors.toList());
        
        // Print the filtered candidates
        System.out.println("Job seekers matching at least " + minMatchedSkills + " skills:");
        filtered.forEach(js -> System.out.println(js.getName() + " → " + js.getSkills()));

```