```java
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FirstNonRepeatedChar {

    public static Character findFirstNonRepeatedChar(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }

        // 1. Count character frequencies and maintain insertion order
        // LinkedHashMap is crucial here to preserve the order of characters
        LinkedHashMap<Character, Long> charCounts = str.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        LinkedHashMap::new, // Use LinkedHashMap to maintain insertion order
                        Collectors.counting()
                ));

        // 2. Find the first character with a count of 1
        return charCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(LinkedHashMap.Entry::getKey)
                .findFirst() // Get the first one that satisfies the condition
                .orElse(null); // Return null if no non-repeated character is found
    }
}

```

- approach-2 
```java
        // 1. Count character frequencies and maintain insertion order
        // LinkedHashMap is crucial here to preserve the order of characters
        String str = str.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        LinkedHashMap::new, // Use LinkedHashMap to maintain insertion order
                        Collectors.counting()
                )).entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 1)
                .map(LinkedHashMap.Entry::getKey)
                .findFirst() // Get the first one that satisfies the condition
                .orElse("not found"); // Return null if no non-repeated character is found

```


- approach-3- use the accumulator pattern

```java
    public static String findFirstNonRepeatedChar(String input) {
        Map<Character, Integer> charCounts = input.chars()
                .mapToObj(c -> (char) c)
                .collect(
                        LinkedHashMap::new,  //  () ->  new LinkedHashMap<Character, Integer>(),
                        (map, ch) -> map.put(ch, map.getOrDefault(ch, 0) + 1),
                        Map::putAll   //(map1, map2) -> map1.putAll(map2)
                );
  /*
   *  Even though combiner (3rd arg) isn’t used in sequential mode, 
   *  it must still be non-null — otherwise, you'll get a NullPointerException.
   */
        return charCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(entry -> String.valueOf(entry.getKey()))
                .findFirst()
                .orElse("not found");
    }

    //-equalent code for 3rd param - combiner
    (map1, map2) -> {
        for (Map.Entry<Character, Integer> entry : map2.entrySet()) {
            map1.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }
```
 - accumulator internals
```java
.collect(
    Supplier<R> supplier,              // 1. Creates the result container (e.g. new HashMap)
    BiConsumer<R, ? super T> accumulator, // 2. Adds a single stream element to the result
    BiConsumer<R, R> combiner             // 3. Merges two result containers (used in parallel streams)
)
```


- simple code to understand accumulator pattern ineternal working about how it can be process elements

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {

        String input = "abac";

        Map<Character, Integer> charCounts = input.chars()
            // .parallel()
            .mapToObj(c -> {
                char ch = (char) c;
                System.out.println("Stream element: " + ch);
                return ch;
            })
            .collect(
                () -> {
                    System.out.println("Created new LinkedHashMap");
                    return new LinkedHashMap<Character, Integer>();
                },
                (map, ch) -> {
                    System.out.println("Processing char: " + ch + " | Current map: " + map);
                    map.put(ch, map.getOrDefault(ch, 0) + 1);
                    System.out.println("Updated map: " + map);
                },
                (map1, map2) -> {
                    System.out.println("Merging maps: " + map1 + " + " + map2);
                    map1.putAll(map2);
                }
            );

        System.out.println("Final map: " + charCounts);
    }
}
```
 - without `.parallel()`:
```
Created new LinkedHashMap
Stream element: a
Processing char: a | Current map: {}
Updated map: {a=1}
Stream element: b
Processing char: b | Current map: {a=1}
Updated map: {a=1, b=1}
Stream element: a
Processing char: a | Current map: {a=1, b=1}
Updated map: {a=2, b=1}
Stream element: c
Processing char: c | Current map: {a=2, b=1}
Updated map: {a=2, b=1, c=1}
Final map: {a=2, b=1, c=1}
```
 - with `.parallel()`:
```
Created new LinkedHashMap
Created new LinkedHashMap
Stream element: a
Stream element: b
Processing char: b | Current map: {}
Updated map: {b=1}
Created new LinkedHashMap
Stream element: a
Processing char: a | Current map: {}
Updated map: {a=1}
Processing char: a | Current map: {}
Updated map: {a=1}
Merging maps: {a=1} + {b=1}
Created new LinkedHashMap
Stream element: c
Processing char: c | Current map: {}
Updated map: {c=1}
Merging maps: {a=1} + {c=1}
Merging maps: {a=1, b=1} + {a=1, c=1}
Final map: {a=1, b=1, c=1}
```
- you need to do the same like opreatios for linked list linked hashset





