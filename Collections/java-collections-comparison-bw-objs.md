# Java Collections Framework: Key Differences and Concepts

A comprehensive guide explaining the key differences between important interfaces, classes, and concepts in the Java Collections Framework, with detailed explanations and code examples.

---

## Table of Contents

| # | Question |
|---|----------|
| **Collection Interfaces** |
| 1 | [Difference between Collection and Collections](#difference-between-collection-and-collections) |
| 2 | [Difference between Iterator and ListIterator](#difference-between-iterator-and-listiterator) |
| 3 | [Difference between Enumeration and Iterator interface](#difference-between-enumeration-and-iterator-interface) |
| 4 | [Comparable and Comparator Interface in Java](#comparable-and-comparator-interface-in-java) |
| **Collection Classes** |
| 5 | [Array and ArrayList vs LinkedList](#array-and-arraylist-vs-linkedlist) |
| 6 | [Differences between ArrayList and Vector](#differences-between-arraylist-and-vector) |
| 7 | [Difference between CopyOnWriteArrayList and ArrayList](#difference-between-copyonwritearraylist-and-arraylist) |
| **Collection Methods and Behavior** |
| 8 | [Difference between fail-fast and fail-safe iterator](#difference-between-fail-fast-and-fail-safe-iterator) |
| 9 | [Difference between containsKey(), keySet() and values() in HashMap](#difference-between-containskey-keyset-and-values-in-hashmap) |
| 10 | [Difference between peek(), poll() and remove() method of the Queue interface](#difference-between-peek-poll-and-remove-method-of-the-queue-interface) |
| 11 | [Difference between ArrayBlockingQueue & LinkedBlockingQueue in Java Concurrency](#difference-between-arrayblockingqueue--linkedblockingqueue-in-java-concurrency) |
| **Set Implementations** |
| 12 | [What is the difference between Set and Map?](#what-is-the-difference-between-set-and-map) |
| 13 | [What is difference between HashSet and LinkedHashSet?](#what-is-difference-between-hashset-and-linkedhashset) |
| 14 | [What is the difference between HashSet and TreeSet?](#what-is-the-difference-between-hashset-and-treeset) |
| 15 | [What is the difference between HashSet and HashMap?](#what-is-the-difference-between-hashset-and-hashmap) |
| **Map Implementations** |
| 16 | [What is difference between IdentityHashMap and HashMap in Java?](#what-is-difference-between-identityhashmap-and-hashmap-in-java) |
| 17 | [What is difference between HashMap and Hashtable?](#what-is-difference-between-HashMap-and-HashTable) |
| 18 | [What is the difference between HashMap and TreeMap?](#what-is-the-difference-between-hashmap-and-treemap) |
| 19 | [What is the difference between HashMap and ConcurrentHashMap?](#what-is-the-difference-between-hashmap-and-concurrenthashmap) |
| 20 | [What is the difference between HashMap and HashTable?](#what-is-the-difference-between-hashmap-and-hashtable) |
| **Other Collections** |
| 21 | [What is Queue and Stack, list their differences?](#what-is-queue-and-stack-list-their-differences) |

---

## Difference between Collection and Collections

**Collection** is an interface that forms the foundation of the Java Collections Framework, while **Collections** is a utility class that provides static methods for working with collections.

<!-- ### Collection Interface

- **Collection** is a root interface in the collection hierarchy
- It represents a group of objects known as elements
- Interfaces like List, Set, and Queue extend the Collection interface
- Defines basic operations like add(), remove(), contains(), etc. -->

```java
Collection<String> names = new ArrayList<>();
names.add("Alice");
names.add("Bob");
names.contains("Alice"); // returns true
```
<!-- 
### Collections Utility Class

- **Collections** is a utility class that contains only static methods
- Provides methods to sort, search, shuffle, and synchronize collection elements
- Cannot be instantiated as all methods are static
- Includes algorithms that operate on Collection instances -->

```java
List<String> names = new ArrayList<>();
names.add("Charlie");
names.add("Alice");
names.add("Bob");

// Using Collections utility methods
Collections.sort(names);                       // Sorts the list
Collections.binarySearch(names, "Alice");      // Searches the sorted list
Collections.shuffle(names);                    // Randomizes order
List<String> syncList = Collections.synchronizedList(names); // Creates thread-safe version
```

### Key Differences

| Collection | Collections |
|------------|-------------|
| Interface | Utility class |
| Can be instantiated through implementing classes | Cannot be instantiated, provides only static methods |
| Defines operations that can be performed on collections | Implements algorithms to operate on collections |
| Part of java.util package | Part of java.util package |
| Examples of implementations: ArrayList, HashSet, LinkedList | Examples of methods: sort(), binarySearch(), max() |

[Back to Top](#table-of-contents)

## Difference between fail-fast and fail-safe iterator

The Java Collections Framework provides two types of iterators that differ in how they handle concurrent modifications during iteration.

### Fail-Fast Iterators

<!-- - Throw `ConcurrentModificationException` if the underlying collection is modified while iterating
- Use a modification counter (`modCount`) to detect concurrent modifications
- More efficient as they don't create a copy of the collection
- Examples: Iterators from ArrayList, HashMap, HashSet, etc. -->

```java
List<String> list = new ArrayList<>();
list.add("One");
list.add("Two");
list.add("Three");

Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String element = iterator.next();
    // The following line will throw ConcurrentModificationException
    list.add("Four");  // Modifying collection during iteration
}
```

### Fail-Safe Iterators

<!-- - Do not throw exceptions when the underlying collection is modified during iteration
- Work on a clone or snapshot of the original collection
- Less efficient as they need to create a copy of the collection
- Examples: Iterators from CopyOnWriteArrayList, ConcurrentHashMap, etc. -->

```java
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
list.add("One");
list.add("Two");
list.add("Three");

Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String element = iterator.next();
    // This will not throw an exception
    list.add("Four");  // Modification is allowed
    // But iterator won't see the new element because it works on a snapshot
}
```

### Key Differences

| Fail-Fast | Fail-Safe |
|-----------|-----------|
| Throws ConcurrentModificationException | Doesn't throw exceptions |
| Works directly on the original collection | Works on a clone or snapshot of the collection |
| More memory efficient | Less memory efficient |
| Immediately detects modifications | May not reflect recent modifications |
| Used by most non-concurrent collections | Used by concurrent collections |
| Examples: Iterators from ArrayList, HashMap, HashSet, etc. |Examples: Iterators from CopyOnWriteArrayList, ConcurrentHashMap, etc.|

[Back to Top](#table-of-contents)

## Difference between Iterator and ListIterator

Both **Iterator** and **ListIterator** are interfaces used for traversing collections in Java, but ListIterator provides more functionality specifically for List implementations.

### Iterator

- Can be used with any Collection (List, Set, Queue, etc.)
- Allows forward traversal only
- Supports remove operation
- Cannot add or replace elements

```java
List<String> list = new ArrayList<>();
list.add("Apple");
list.add("Banana");
list.add("Cherry");

Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String fruit = iterator.next();
    System.out.println(fruit);
    // Can remove elements
    if (fruit.equals("Banana")) {
        iterator.remove();
    }
}
```

### ListIterator

- Can be used only with List implementations
- Allows both forward and backward traversal
- Supports remove, add, and replace operations
- Provides information about element indices

```java
List<String> list = new ArrayList<>();
list.add("Apple");
list.add("Banana");
list.add("Cherry");

ListIterator<String> listIterator = list.listIterator();
// Forward traversal
while (listIterator.hasNext()) {
    int index = listIterator.nextIndex();
    String fruit = listIterator.next();
    System.out.println(index + ": " + fruit);
    
    // Adding a new element
    if (fruit.equals("Banana")) {
        listIterator.add("Blueberry");
    }
    
    // Replacing an element
    if (fruit.equals("Cherry")) {
        listIterator.set("Cranberry");
    }
}

// Backward traversal
while (listIterator.hasPrevious()) {
    System.out.println(listIterator.previous());
}
```

### Key Differences

| Feature | Iterator | ListIterator |
|---------|----------|-------------|
| Collection type | Works with any Collection | Works only with List |
| Traversal direction | Forward only | Both forward and backward |
| Operations | next(), hasNext(), remove() | All Iterator methods plus previous(), hasPrevious(), add(), set(), nextIndex(), previousIndex() |
| Add elements | Not supported | Supported with add() |
| Replace elements | Not supported | Supported with set() |
| Index information | Not available | Available with nextIndex() and previousIndex() |

[Back to Top](#table-of-contents)

## Difference between Enumeration and Iterator interface

**Enumeration** and **Iterator** are both interfaces used for traversing collections, but Enumeration is considered legacy and has been largely replaced by Iterator.

### Enumeration Interface

- Legacy interface introduced in JDK 1.0
- Used primarily with legacy classes like Vector and Hashtable
- Read-only access to collection elements
- Less functionality compared to Iterator
- Found in the java.util package

```java
Vector<String> vector = new Vector<>();
vector.add("One");
vector.add("Two");
vector.add("Three");

Enumeration<String> enumeration = vector.elements();
while (enumeration.hasMoreElements()) {
    String element = enumeration.nextElement();
    System.out.println(element);
    // Cannot remove elements during traversal
}
```

### Iterator Interface

- Introduced in Java Collections Framework (JDK 1.2)
- Used with all modern collection classes
- Allows removal of elements during traversal
- More flexible and feature-rich
- Found in the java.util package

```java
ArrayList<String> list = new ArrayList<>();
list.add("One");
list.add("Two");
list.add("Three");

Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String element = iterator.next();
    System.out.println(element);
    // Can remove elements during traversal
    if (element.equals("Two")) {
        iterator.remove();
    }
}
```

### Key Differences

| Feature | Enumeration | Iterator |
|---------|-------------|----------|
| Introduction | JDK 1.0 (legacy) | JDK 1.2 (modern) |
| Method names | hasMoreElements(), nextElement() | hasNext(), next() |
| Element removal | Not supported | Supported with remove() |
| Safety | Not fail-fast | Fail-fast (throws ConcurrentModificationException) |
| Primary usage | Legacy classes (Vector, Hashtable) | All modern collections |
| Method count | 2 methods | 3 methods (including remove) |

[Back to Top](#table-of-contents)

## Comparable and Comparator Interface in Java

Java provides two interfaces for comparing objects: **Comparable** and **Comparator**. They serve similar purposes but are used in different contexts.

### Comparable Interface

- Internal comparison mechanism (natural ordering)
- Class itself must implement Comparable
- Single compareTo() method
- Can define only one way of comparing objects

```java
public class Student implements Comparable<Student> {
    private String name;
    private int age;
    
    // Constructor, getters, and setters
    
    // Natural ordering based on age
    @Override
    public int compareTo(Student other) {
        return this.age - other.age;  // Ascending order by age
    }
}

// Using natural ordering
List<Student> students = new ArrayList<>();
// Add students
Collections.sort(students);  // Uses compareTo method
```

### Comparator Interface

- External comparison mechanism
- Separate class implements Comparator
- Single compare() method
- Multiple comparators can be created for different comparison criteria

```java
// Student class (without implementing Comparable)
public class Student {
    private String name;
    private int age;
    
    // Constructor, getters, and setters
}

// Separate comparator classes
public class AgeComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return s1.getAge() - s2.getAge();
    }
}

public class NameComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return s1.getName().compareTo(s2.getName());
    }
}

// Using comparators
List<Student> students = new ArrayList<>();
// Add students
Collections.sort(students, new AgeComparator());   // Sort by age
Collections.sort(students, new NameComparator());  // Sort by name
```

### Key Differences

| Feature | Comparable | Comparator |
|---------|------------|------------|
| Package | java.lang | java.util |
| Method | compareTo(T o) | compare(T o1, T o2) |
| Implementation | Class itself implements it | Separate class implements it |
| Natural ordering | Yes (defines class's natural order) | No (external to the class being compared) |
| Multiple sort sequences | No (only one implementation per class) | Yes (can have multiple comparators) |
| Method signature | int compareTo(T o) | int compare(T o1, T o2) |
| Usage with sort | Collections.sort(list) | Collections.sort(list, comparator) |
| Control | Need access to class source code | Can sort classes without modifying them |

[Back to Top](#table-of-contents)

## Difference between containsKey(), keySet() and values() in HashMap

These three methods serve different purposes when working with a `HashMap` in Java:

### containsKey()

- Checks if a specific key exists in the HashMap
- Returns a boolean value (true if key exists, false otherwise)
- Efficient lookup operation with O(1) time complexity

```java
HashMap<Integer, String> map = new HashMap<>();
map.put(1, "One");
map.put(2, "Two");
map.put(3, "Three");

boolean hasKey = map.containsKey(2);  // true
boolean noKey = map.containsKey(4);   // false
```

### keySet()

- Returns a Set view of all keys in the HashMap
- Useful for iterating over all keys
- Returns java.util.Set containing all keys

```java
HashMap<Integer, String> map = new HashMap<>();
map.put(1, "One");
map.put(2, "Two");
map.put(3, "Three");

Set<Integer> keys = map.keySet();  // Contains [1, 2, 3]

// Iterating over keys
for (Integer key : keys) {
    System.out.println("Key: " + key + ", Value: " + map.get(key));
}
```

### values()

- Returns a Collection view of all values in the HashMap
- Useful for iterating over all values
- Returns java.util.Collection containing all values

```java
HashMap<Integer, String> map = new HashMap<>();
map.put(1, "One");
map.put(2, "Two");
map.put(3, "Three");

Collection<String> values = map.values();  // Contains ["One", "Two", "Three"]

// Iterating over values
for (String value : values) {
    System.out.println("Value: " + value);
}
```

### Key Differences

| Method | Return Type | Purpose | Common Use Case |
|--------|------------|---------|----------------|
| containsKey(Object key) | boolean | Checks if a key exists | Validation before operations |
| keySet() | Set<K> | Gets all keys | Iterating over keys or getting key count |
| values() | Collection<V> | Gets all values | Iterating over values or searching values |

[Back to Top](#table-of-contents)

## Difference between peek(), poll() and remove() method of the Queue interface

These three methods are used for retrieving elements from a Queue in Java, but they behave differently in terms of element removal and exception handling.

### peek()

- Retrieves but does not remove the head of the queue
- Returns null if the queue is empty
- Does not throw an exception for empty queues

```java
Queue<String> queue = new LinkedList<>();
queue.add("First");
queue.add("Second");

String element = queue.peek();  // Returns "First" but keeps it in queue
System.out.println(element);    // Output: First
System.out.println(queue.size()); // Output: 2 (element still in queue)

Queue<String> emptyQueue = new LinkedList<>();
String nullElement = emptyQueue.peek();  // Returns null
```

### poll()

- Retrieves and removes the head of the queue
- Returns null if the queue is empty
- Does not throw an exception for empty queues

```java
Queue<String> queue = new LinkedList<>();
queue.add("First");
queue.add("Second");

String element = queue.poll();  // Returns "First" and removes it
System.out.println(element);    // Output: First
System.out.println(queue.size()); // Output: 1 (element removed)

Queue<String> emptyQueue = new LinkedList<>();
String nullElement = emptyQueue.poll();  // Returns null
```

### remove()

- Retrieves and removes the head of the queue
- Throws NoSuchElementException if the queue is empty
- Use when you expect the queue to have elements

```java
Queue<String> queue = new LinkedList<>();
queue.add("First");
queue.add("Second");

String element = queue.remove();  // Returns "First" and removes it
System.out.println(element);      // Output: First
System.out.println(queue.size()); // Output: 1 (element removed)

Queue<String> emptyQueue = new LinkedList<>();
try {
    emptyQueue.remove();  // Throws NoSuchElementException
} catch (NoSuchElementException e) {
    System.out.println("Queue is empty!");
}
```

### Key Differences

| Method | Returns | Removes Element | Empty Queue Behavior | Use Case |
|--------|---------|----------------|---------------------|----------|
| peek() | Head element | No | Returns null | When you only want to examine the element |
| poll() | Head element | Yes | Returns null | When removal is needed but empty is valid |
| remove() | Head element | Yes | Throws NoSuchElementException | When queue must have elements |

[Back to Top](#table-of-contents)

## Array and ArrayList vs LinkedList

Arrays, ArrayLists, and LinkedLists are fundamental data structures in Java, each with distinct characteristics and use cases.

### Array

- Fixed-size, primitive data structure in Java
- Elements stored in contiguous memory locations
- Direct access by index with O(1) time complexity
- Cannot be resized after creation
- Can store primitives or objects

```java
// Declaration and initialization
int[] numbers = new int[5];  // Fixed size of 5
numbers[0] = 10;
numbers[1] = 20;

// Array literal
String[] fruits = {"Apple", "Banana", "Cherry"};

// Direct access
int value = numbers[1];  // O(1) access
```

### ArrayList

- Dynamic array implementation (resizable)
- Part of the Collections Framework (implements List interface)
- Random access with O(1) time complexity
- Automatic resizing when needed (amortized O(1) for add)
- Stores objects only (not primitives directly)

```java
// Declaration and initialization
ArrayList<Integer> numbers = new ArrayList<>();
numbers.add(10);  // Automatic resizing
numbers.add(20);
numbers.add(30);

// Direct access
int value = numbers.get(1);  // O(1) access

// Insertion at specific position
numbers.add(1, 15);  // O(n) operation (shifts elements)
```

### LinkedList

- Doubly-linked list implementation
- Part of the Collections Framework (implements List and Deque interfaces)
- No direct access to elements (requires traversal)
- Efficient insertions and deletions at any position
- Higher memory overhead due to node references

```java
// Declaration and initialization
LinkedList<Integer> numbers = new LinkedList<>();
numbers.add(10);
numbers.add(20);
numbers.add(30);

// Access requires traversal
int value = numbers.get(1);  // O(n) operation

// Efficient insertions
numbers.addFirst(5);   // O(1) operation
numbers.addLast(40);   // O(1) operation
numbers.add(1, 15);    // O(1) operation after finding position
```

### Key Differences

| Feature | Array | ArrayList | LinkedList |
|---------|-------|-----------|------------|
| Size | Fixed | Dynamic | Dynamic |
| Memory | Contiguous | Contiguous (with resize) | Non-contiguous (nodes) |
| Random Access | O(1) | O(1) | O(n) |
| Insertion/Deletion | Expensive (shift elements) | Expensive at start/middle O(n) | Efficient O(1) after finding position |
| Memory Overhead | Minimal | Moderate (capacity management) | High (next/prev pointers) |
| Type Support | Primitives and objects | Objects only | Objects only |
| Implements | Not a collection | List interface | List and Deque interfaces |
| Usage | When size is known and fixed | For random access and dynamic size | For frequent insertions/deletions |

### Performance Comparison

| Operation | Array | ArrayList | LinkedList |
|-----------|-------|-----------|------------|
| Access | O(1) | O(1) | O(n) |
| Search | O(n) | O(n) | O(n) |
| Insert at start | O(n) | O(n) | O(1) |
| Insert at end | O(1)* | Amortized O(1) | O(1) |
| Insert in middle | O(n) | O(n) | O(n)** |
| Delete at start | O(n) | O(n) | O(1) |
| Delete at end | O(1) | O(1) | O(1) |
| Delete in middle | O(n) | O(n) | O(n)** |

\* Assuming space is available (rare for arrays)  
\** O(1) after finding the position, but finding requires O(n)

[Back to Top](#table-of-contents)

## Differences between ArrayList and Vector

Both ArrayList and Vector are implementations of the List interface in Java, but they have significant differences in terms of synchronization, growth, and legacy status.

### ArrayList

- Not synchronized (not thread-safe)
- More efficient in most scenarios
- Introduced in JDK 1.2 as part of Collections Framework
- Grows by 50% of current size when full
- Iterator is fail-fast

```java
ArrayList<String> list = new ArrayList<>();
list.add("One");
list.add("Two");
list.add("Three");

// Not thread-safe
// Multiple threads can access simultaneously, potentially causing issues
```

### Vector

- Synchronized (thread-safe)
- Less efficient due to synchronization overhead
- Legacy class (present since JDK 1.0)
- Grows by 100% of current size when full
- Both Iterator and Enumeration are available

```java
Vector<String> vector = new Vector<>();
vector.add("One");
vector.add("Two");
vector.add("Three");

// Thread-safe
// All methods are synchronized
// Only one thread can access at a time

// Can use Enumeration (legacy)
Enumeration<String> enumeration = vector.elements();
while (enumeration.hasMoreElements()) {
    System.out.println(enumeration.nextElement());
}
```

### Key Differences

| Feature | ArrayList | Vector |
|---------|-----------|--------|
| Synchronization | Not synchronized | Synchronized (thread-safe) |
| Performance | Faster (no synchronization overhead) | Slower due to synchronization |
| Growth | Increases by 50% when full | Increases by 100% when full |
| Introduction | JDK 1.2 (Collections Framework) | JDK 1.0 (legacy component) |
| Iterator | Only Iterator (fail-fast) | Both Iterator and Enumeration |
| Default capacity | 10 | 10 |
| Best use case | Single-threaded environments | Legacy code or when synchronization needed |

### Thread Safety Example

```java
// For ArrayList, external synchronization is needed
List<String> synchronizedList = Collections.synchronizedList(new ArrayList<>());

synchronized (synchronizedList) {
    Iterator<String> iterator = synchronizedList.iterator();
    while (iterator.hasNext()) {
        System.out.println(iterator.next());
    }
}

// For Vector, methods are already synchronized
Vector<String> vector = new Vector<>();
// No need for external synchronization for individual operations
vector.add("One");

// But still need synchronization for compound operations
synchronized (vector) {
    Iterator<String> iterator = vector.iterator();
    while (iterator.hasNext()) {
        System.out.println(iterator.next());
    }
}
```

[Back to Top](#table-of-contents)

## Difference between CopyOnWriteArrayList and ArrayList

CopyOnWriteArrayList and ArrayList are both List implementations, but they are designed for different use cases, particularly regarding concurrency.

### ArrayList

- Not thread-safe
- Direct modifications to the backing array
- Faster for frequent modifications
- Uses fail-fast iterators
- Part of the original Collections Framework

```java
ArrayList<String> list = new ArrayList<>();
list.add("One");
list.add("Two");

Iterator<String> iterator = list.iterator();
list.add("Three");  // Modifying during iteration

// The next call to iterator.next() will throw ConcurrentModificationException
```

### CopyOnWriteArrayList

- Thread-safe without explicit synchronization
- Creates a fresh copy of the array for every modification
- Uses fail-safe iterators (snapshot view)
- Designed for concurrent access scenarios
- Part of the java.util.concurrent package

```java
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
list.add("One");
list.add("Two");

Iterator<String> iterator = list.iterator();
list.add("Three");  // Creates a new copy of the array

// Iterator still works on the original snapshot
// "Three" won't be visible to this iterator
while (iterator.hasNext()) {
    System.out.println(iterator.next());  // Only prints "One" and "Two"
}

// Need a new iterator to see "Three"
Iterator<String> newIterator = list.iterator();
```

### Key Differences

| Feature | ArrayList | CopyOnWriteArrayList |
|---------|-----------|----------------------|
| Thread safety | Not thread-safe | Thread-safe |
| Implementation approach | Modifies original array | Creates new copy on modification |
| Iterator behavior | Fail-fast (throws exception on concurrent modification) | Fail-safe (works on snapshot) |
| Performance for reads | Fast | Fast |
| Performance for writes | Fast | Slow (creates new array copy) |
| Memory usage | Efficient | Higher (needs to create copies) |
| Use case | High-modification, single-threaded environments | Read-heavy, concurrent environments |
| Package | java.util | java.util.concurrent |

### Performance Considerations

```java
// Performance example
long start, end;

// ArrayList write performance
ArrayList<Integer> arrayList = new ArrayList<>();
start = System.nanoTime();
for (int i = 0; i < 100000; i++) {
    arrayList.add(i);
}
end = System.nanoTime();
System.out.println("ArrayList add time: " + (end - start) + " ns");

// CopyOnWriteArrayList write performance
CopyOnWriteArrayList<Integer> copyList = new CopyOnWriteArrayList<>();
start = System.nanoTime();
for (int i = 0; i < 100000; i++) {
    copyList.add(i);  // Each add creates a new array copy
}
end = System.nanoTime();
System.out.println("CopyOnWriteArrayList add time: " + (end - start) + " ns");
// Will be significantly slower
```

[Back to Top](#table-of-contents)

## Difference between ArrayBlockingQueue & LinkedBlockingQueue in Java Concurrency

ArrayBlockingQueue and LinkedBlockingQueue are thread-safe queue implementations in the java.util.concurrent package, designed for producer-consumer scenarios.

### ArrayBlockingQueue

- Bounded, array-based implementation
- Fixed-size queue (capacity specified at creation)
- Elements stored in contiguous memory locations
- One lock for both producers and consumers (more contention)
- Typically better memory locality and less overhead

```java
// Creating a bounded queue with capacity of 3
ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(3);

// Producer
queue.put("Task 1");  // Blocks if queue is full
queue.offer("Task 2");  // Returns false if queue is full

// Consumer
String task = queue.take();  // Blocks if queue is empty
String anotherTask = queue.poll();  // Returns null if queue is empty
```

### LinkedBlockingQueue

- Optionally bounded, linked-node based implementation
- Optional capacity limit (unbounded by default)
- Elements stored as linked nodes
- Separate locks for producers and consumers (less contention)
- More memory overhead due to node objects

```java
// Creating an unbounded queue
LinkedBlockingQueue<String> unboundedQueue = new LinkedBlockingQueue<>();

// Creating a bounded queue with capacity of 3
LinkedBlockingQueue<String> boundedQueue = new LinkedBlockingQueue<>(3);

// Producer
boundedQueue.put("Task 1");  // Blocks if queue is full
boundedQueue.offer("Task 2", 500, TimeUnit.MILLISECONDS);  // Waits up to 500ms

// Consumer
String task = boundedQueue.take();  // Blocks if queue is empty
String anotherTask = boundedQueue.poll(1, TimeUnit.SECONDS);  // Waits up to 1s
```

### Key Differences

| Feature | ArrayBlockingQueue | LinkedBlockingQueue |
|---------|-------------------|---------------------|
| Implementation | Array-based | Linked-node based |
| Bounding | Always bounded | Optionally bounded (unbounded by default) |
| Locking | Single lock for all operations | Separate locks for put and take operations |
| Memory efficiency | Better (less overhead) | Worse (node objects and references) |
| Throughput | Lower under high contention | Higher under high contention |
| Size limits | Must specify capacity at creation | Optional capacity limit |
| Memory usage | Pre-allocates all memory | Grows as needed (up to capacity if specified) |
| Best use case | Known, fixed capacity needs | Dynamic sizing or high throughput needs |

### Performance Example

```java
// Producer-Consumer Example
class Producer implements Runnable {
    private BlockingQueue<Integer> queue;
    
    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                queue.put(i);
                System.out.println("Produced: " + i);
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private BlockingQueue<Integer> queue;
    
    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                Integer value = queue.take();
                System.out.println("Consumed: " + value);
                Thread.sleep(50);  // Consumer is slower than producer
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Usage
BlockingQueue<Integer> arrayQueue = new ArrayBlockingQueue<>(10);
// OR
BlockingQueue<Integer> linkedQueue = new LinkedBlockingQueue<>(10);

new Thread(new Producer(arrayQueue)).start();
new Thread(new Consumer(arrayQueue)).start();
```

[Back to Top](#table-of-contents)

## What is the difference between Set and Map?

`Set` and `Map` are two fundamental interfaces in Java's Collections Framework, but they serve different purposes and have distinct characteristics.

| Feature | Set | Map |
|---------|-----|-----|
| Purpose | Stores unique elements | Stores key-value pairs |
| Duplicates | No duplicates allowed | No duplicate keys (values can be duplicated) |
| Null values | Most implementations allow one null element | Most implementations allow one null key and multiple null values |
| Access | Direct access to elements | Access values through keys |
| Common implementations | HashSet, LinkedHashSet, TreeSet | HashMap, LinkedHashMap, TreeMap |
| Interface hierarchy | Extends Collection interface | Does not extend Collection interface |

### Code Example:

```java
// Set example
Set<String> languageSet = new HashSet<>();
languageSet.add("Java");
languageSet.add("Python");
languageSet.add("JavaScript");
languageSet.add("Java"); // Duplicate, will not be added

System.out.println(languageSet); // Output: [Java, Python, JavaScript]
System.out.println(languageSet.contains("Java")); // Output: true

// Map example
Map<String, String> countryCapitals = new HashMap<>();
countryCapitals.put("USA", "Washington D.C.");
countryCapitals.put("France", "Paris");
countryCapitals.put("Japan", "Tokyo");
countryCapitals.put("USA", "Washington"); // Overwrites previous value

System.out.println(countryCapitals); // Output: {USA=Washington, France=Paris, Japan=Tokyo}
System.out.println(countryCapitals.get("France")); // Output: Paris
```

[Back to Top](#table-of-contents)

## What is difference between HashSet and LinkedHashSet?

Both `HashSet` and `LinkedHashSet` implement the `Set` interface, but they differ in their internal implementation and behavior.

| Feature | HashSet | LinkedHashSet |
|---------|---------|---------------|
| Ordering | No guaranteed order | Maintains insertion order |
| Implementation | Backed by HashMap | Backed by LinkedHashMap |
| Performance | Slightly better performance | Slightly slower due to maintaining linked list |
| Memory overhead | Less memory overhead | Additional memory for maintaining order |
| Iteration performance | Less predictable iteration | More predictable iteration |
| Use case | When order doesn't matter | When insertion order needs to be preserved |
| Null elements | Allows one null element | Allows one null element |

### Code Example:

```java
// HashSet example - does not maintain insertion order
Set<String> hashSet = new HashSet<>();
hashSet.add("Banana");
hashSet.add("Apple");
hashSet.add("Orange");
hashSet.add("Grapes");

System.out.println("HashSet elements:");
for (String fruit : hashSet) {
    System.out.println(fruit);
}
// Output might be: Apple, Grapes, Orange, Banana (order not guaranteed)

// LinkedHashSet example - maintains insertion order
Set<String> linkedHashSet = new LinkedHashSet<>();
linkedHashSet.add("Banana");
linkedHashSet.add("Apple");
linkedHashSet.add("Orange");
linkedHashSet.add("Grapes");

System.out.println("\nLinkedHashSet elements:");
for (String fruit : linkedHashSet) {
    System.out.println(fruit);
}
// Output will be: Banana, Apple, Orange, Grapes (insertion order maintained)
```

[Back to Top](#table-of-contents)

## What is the difference between HashSet and TreeSet?

`HashSet` and `TreeSet` are both implementations of the `Set` interface, but they have significant differences in behavior and performance characteristics.

| Feature | HashSet | TreeSet |
|---------|---------|---------|
| Ordering | No guaranteed order | Sorted natural order or by comparator |
| Implementation | Backed by HashMap | Backed by TreeMap (Red-Black Tree) |
| Performance for add/remove/contains | O(1) constant time operations | O(log n) time operations |
| Memory usage | Less memory overhead | More memory overhead |
| Null elements | Allows one null element | Doesn't allow null elements (Java 7+) |
| Use case | Fast operations, order not important | When sorted order is required |
| Interface | Implements Set | Implements NavigableSet and SortedSet |
| Additional operations | Basic Set operations | Range view, first/last element, navigation |

### Code Example:

```java
// HashSet example
Set<Integer> hashSet = new HashSet<>();
hashSet.add(5);
hashSet.add(3);
hashSet.add(9);
hashSet.add(1);
hashSet.add(7);

System.out.println("HashSet elements:");
for (Integer num : hashSet) {
    System.out.print(num + " ");
}
// Output might be: 1 3 5 7 9 (order not guaranteed)

// TreeSet example
Set<Integer> treeSet = new TreeSet<>();
treeSet.add(5);
treeSet.add(3);
treeSet.add(9);
treeSet.add(1);
treeSet.add(7);

System.out.println("\nTreeSet elements:");
for (Integer num : treeSet) {
    System.out.print(num + " ");
}
// Output will be: 1 3 5 7 9 (always sorted)

// TreeSet additional operations
TreeSet<Integer> navigableTreeSet = new TreeSet<>();
navigableTreeSet.addAll(Arrays.asList(1, 3, 5, 7, 9));

System.out.println("\nFirst element: " + navigableTreeSet.first()); // 1
System.out.println("Last element: " + navigableTreeSet.last()); // 9
System.out.println("Lower than 5: " + navigableTreeSet.lower(5)); // 3
System.out.println("Higher than 5: " + navigableTreeSet.higher(5)); // 7
System.out.println("Subset [3, 7]: " + navigableTreeSet.subSet(3, true, 7, true)); // [3, 5, 7]
```

[Back to Top](#table-of-contents)

## What is the difference between HashSet and HashMap?

`HashSet` and `HashMap` are fundamentally different collection types, although `HashSet` is internally backed by a `HashMap`.

| Feature | HashSet | HashMap |
|---------|---------|---------|
| Interface | Implements Set interface | Implements Map interface |
| Purpose | Stores unique elements | Stores key-value pairs |
| Internal structure | Internally uses HashMap | Direct hash table implementation |
| Implementation | Uses HashMap with dummy values | Direct key-value storage |
| What it stores | Single elements | Key-value pairs |
| Duplicate handling | No duplicates allowed | No duplicate keys (values can be duplicated) |
| Methods | add(), remove(), contains() | put(), get(), remove(), containsKey() |
| Performance | O(1) for basic operations | O(1) for basic operations |
| Memory usage | More overhead per element | More efficient for key-value data |
| Iteration | Iterates over elements | Iterates over keys, values, or entries |

### Code Example:

```java
// HashSet example
Set<String> hashSet = new HashSet<>();
hashSet.add("Java");
hashSet.add("Python");
hashSet.add("C++");
hashSet.add("JavaScript");

System.out.println("HashSet contains Java: " + hashSet.contains("Java")); // true
hashSet.remove("Python");
System.out.println("HashSet after removal: " + hashSet); // [Java, C++, JavaScript]

// HashMap example
Map<String, Integer> hashMap = new HashMap<>();
hashMap.put("Java", 1995);
hashMap.put("Python", 1991);
hashMap.put("C++", 1983);
hashMap.put("JavaScript", 1995);

System.out.println("HashMap value for Java: " + hashMap.get("Java")); // 1995
hashMap.remove("Python");
System.out.println("HashMap after removal: " + hashMap); // {Java=1995, C++=1983, JavaScript=1995}

// Iterating over HashSet
System.out.println("\nIterating HashSet:");
for (String language : hashSet) {
    System.out.println(language);
}

// Iterating over HashMap
System.out.println("\nIterating HashMap keys:");
for (String key : hashMap.keySet()) {
    System.out.println(key);
}

System.out.println("\nIterating HashMap values:");
for (Integer value : hashMap.values()) {
    System.out.println(value);
}

System.out.println("\nIterating HashMap entries:");
for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
    System.out.println(entry.getKey() + " was created in " + entry.getValue());
}
```
[Back to Top](#table-of-contents)

## What is difference between IdentityHashMap and HashMap in Java?

`IdentityHashMap` and `HashMap` are both map implementations in Java, but they differ in how they determine object equality.

| Feature | HashMap | IdentityHashMap |
|---------|---------|-----------------|
| Key comparison | Uses equals() method | Uses reference equality (==) |
| Hash calculation | Uses hashCode() method | Uses System.identityHashCode() |
| Performance | Generally faster for most operations | May be faster when comparing references |
| Use case | General purpose map implementation | When reference equality is needed |
| Null keys | Allows one null key | Allows null keys |
| Null values | Allows multiple null values | Allows multiple null values |
| Implementation | Standard hash table | Array-based implementation (non-standard) |
| Object instances | Different objects with same content are equal | Different objects are always considered different |

### Code Example:

```java
// HashMap example
Map<String, String> hashMap = new HashMap<>();
String key1 = new String("key");
String key2 = new String("key");

hashMap.put(key1, "value1");
hashMap.put(key2, "value2");

System.out.println("HashMap size: " + hashMap.size()); // Output: 1
System.out.println("HashMap value: " + hashMap.get(key1)); // Output: value2
System.out.println("key1.equals(key2): " + key1.equals(key2)); // Output: true

// IdentityHashMap example
Map<String, String> identityHashMap = new IdentityHashMap<>();
String idKey1 = new String("key");
String idKey2 = new String("key");

identityHashMap.put(idKey1, "value1");
identityHashMap.put(idKey2, "value2");

System.out.println("IdentityHashMap size: " + identityHashMap.size()); // Output: 2
System.out.println("IdentityHashMap value for key1: " + identityHashMap.get(idKey1)); // Output: value1
System.out.println("IdentityHashMap value for key2: " + identityHashMap.get(idKey2)); // Output: value2
System.out.println("idKey1 == idKey2: " + (idKey1 == idKey2)); // Output: false
```

[Back to Top](#table-of-contents)

## What is difference between HashMap and Hashtable?

`HashMap` and `Hashtable` both implement the `Map` interface, but they have several important differences in behavior and implementation.

| Feature | HashMap | Hashtable |
|---------|---------|-----------|
| Synchronization | Not synchronized (not thread-safe) | Synchronized (thread-safe) |
| Performance | Generally faster (no synchronization overhead) | Slower due to synchronization |
| Null keys/values | Allows one null key and multiple null values | Does not allow null keys or values |
| Iteration | Fail-fast iterator | Enumerator (older, not fail-fast) |
| Legacy status | Modern implementation (Java 1.2+) | Legacy class (since Java 1.0) |
| Inheritance | Extends AbstractMap | Extends Dictionary |
| Initial capacity | Default 16 with 0.75 load factor | Default 11 with 0.75 load factor |
| Modern alternative | Use HashMap or ConcurrentHashMap | Use ConcurrentHashMap instead |

### Code Example:

```java
// HashMap example
HashMap<String, Integer> hashMap = new HashMap<>();
hashMap.put("One", 1);
hashMap.put(null, 100);  // HashMap allows null keys
hashMap.put("Null", null);  // HashMap allows null values

// HashTable example
Hashtable<String, Integer> hashTable = new Hashtable<>();
hashTable.put("One", 1);
// hashTable.put(null, 100);  // Would throw NullPointerException
// hashTable.put("Null", null);  // Would throw NullPointerException

// Thread-safety demonstration
// HashMap needs external synchronization for thread safety
Map<String, Integer> threadSafeHashMap = Collections.synchronizedMap(new HashMap<>());

// Iterating over HashMap
Iterator<String> hashMapIterator = hashMap.keySet().iterator();
while (hashMapIterator.hasNext()) {
    String key = hashMapIterator.next();
    // Safe to modify hashMap here using iterator.remove()
}

// Iterating over Hashtable (both ways)
// 1. Using Iterator
Iterator<String> hashTableIterator = hashTable.keySet().iterator();
while (hashTableIterator.hasNext()) {
    String key = hashTableIterator.next();
}

// 2. Using legacy Enumeration
Enumeration<String> keys = hashTable.keys();
while (keys.hasMoreElements()) {
    String key = keys.nextElement();
}

// Recommended alternative to Hashtable
Map<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();
concurrentHashMap.put("One", 1);
concurrentHashMap.put("Two", 2);
// concurrentHashMap.put(null, 0);  // Would throw NullPointerException
// concurrentHashMap.put("Three", null);  // Would throw NullPointerException
```

[Back to Top](#table-of-contents)

## What is the difference between HashMap and TreeMap?

`HashMap` and `TreeMap` both implement the `Map` interface but have different characteristics in terms of ordering, performance, and functionality.

| Feature | HashMap | TreeMap |
|---------|---------|---------|
| Ordering | No guaranteed order | Sorted by natural order or comparator |
| Implementation | Hash table based | Red-Black Tree based |
| Performance (get/put/remove) | O(1) average case | O(log n) |
| Performance (iteration) | O(capacity + size) | O(n) |
| Memory usage | Less memory overhead | More memory overhead |
| Null keys | Allows one null key | Does not allow null keys if natural ordering used |
| Interface | Implements Map | Implements NavigableMap and SortedMap |
| Additional operations | Basic Map operations | Range views, first/last entry, navigation |
| Use case | Fast access, insertion, deletion | When sorted order is required |

### Code Example:

```java
// HashMap example
Map<String, Integer> hashMap = new HashMap<>();
hashMap.put("Banana", 2);
hashMap.put("Apple", 5);
hashMap.put("Orange", 3);
hashMap.put("Grapes", 4);

System.out.println("HashMap entries:");
for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
// Output might be in any order (e.g., Apple: 5, Grapes: 4, Orange: 3, Banana: 2)

// TreeMap example
Map<String, Integer> treeMap = new TreeMap<>();
treeMap.put("Banana", 2);
treeMap.put("Apple", 5);
treeMap.put("Orange", 3);
treeMap.put("Grapes", 4);

System.out.println("\nTreeMap entries (sorted by key):");
for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
// Output will be sorted by key: Apple: 5, Banana: 2, Grapes: 4, Orange: 3

// TreeMap additional operations
TreeMap<String, Integer> navigableTreeMap = new TreeMap<>();
navigableTreeMap.putAll(treeMap);

System.out.println("\nFirst entry: " + navigableTreeMap.firstEntry()); // Apple=5
System.out.println("Last entry: " + navigableTreeMap.lastEntry()); // Orange=3
System.out.println("Lower entry than 'Grapes': " + navigableTreeMap.lowerEntry("Grapes")); // Banana=2
System.out.println("Higher entry than 'Grapes': " + navigableTreeMap.higherEntry("Grapes")); // Orange=3
System.out.println("SubMap from 'Apple' to 'Orange': " + navigableTreeMap.subMap("Apple", true, "Orange", true));
// {Apple=5, Banana=2, Grapes=4, Orange=3}
```

[Back to Top](#table-of-contents)

## What is the difference between HashMap and ConcurrentHashMap?

`HashMap` and `ConcurrentHashMap` are both map implementations, but they differ significantly in terms of thread safety and concurrent access.

| Feature | HashMap | ConcurrentHashMap |
|---------|---------|-------------------|
| Thread safety | Not thread-safe | Thread-safe |
| Concurrency | No concurrency support | High concurrency with segment locking |
| Null values | Allows one null key and multiple null values | Does not allow null keys or values |
| Performance (single thread) | Faster in single-threaded environment | Slightly slower due to concurrency overhead |
| Performance (multi-thread) | Poor (requires external synchronization) | Excellent for concurrent access |
| Implementation (Java 8+) | Traditional hash table | Array of nodes + Red-Black Tree for collisions |
| Lock granularity | N/A (no locking) | Segment-level locking (Java 7), node-level locking (Java 8+) |
| Iteration | Fail-fast iterator | Weakly consistent iterator |
| Atomic operations | None | putIfAbsent(), remove(), replace() |

### Code Example:

```java
// HashMap example - Not thread-safe
Map<String, Integer> hashMap = new HashMap<>();
hashMap.put("One", 1);
hashMap.put("Two", 2);
hashMap.put(null, 0);
hashMap.put("Three", null);

// Using HashMap in a thread-safe way
Map<String, Integer> synchronizedHashMap = Collections.synchronizedMap(new HashMap<>());
// Must synchronize when iterating
synchronized (synchronizedHashMap) {
    for (Map.Entry<String, Integer> entry : synchronizedHashMap.entrySet()) {
        // Safe iteration
    }
}

// ConcurrentHashMap example - Thread-safe
Map<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();
concurrentHashMap.put("One", 1);
concurrentHashMap.put("Two", 2);
// concurrentHashMap.put(null, 0);  // Would throw NullPointerException
// concurrentHashMap.put("Three", null);  // Would throw NullPointerException

// No need to synchronize when iterating
for (Map.Entry<String, Integer> entry : concurrentHashMap.entrySet()) {
    // Safe iteration without explicit synchronization
}

// Atomic operations example
concurrentHashMap.putIfAbsent("Three", 3);  // Only puts if key doesn't exist
concurrentHashMap.replace("One", 1, 10);    // Replaces only if current value matches
concurrentHashMap.remove("Two", 2);         // Removes only if value matches

// Bulk operations (Java 8+)
concurrentHashMap.forEach(2, (key, value) -> 
    System.out.println(key + "=" + value));

// Parallel processing (Java 8+)
int sum = concurrentHashMap.reduceValues(2, value -> value);
```

[Back to Top](#table-of-contents)

## What is Queue and Stack, list their differences?

`Queue` and `Stack` are fundamental data structures that store elements but differ in their access patterns and operations.

| Feature | Queue | Stack |
|---------|-------|-------|
| Interface/Class | Interface in Java | Legacy class in Java (extends Vector) |
| Modern implementation | LinkedList, ArrayDeque, PriorityQueue | Recommended: ArrayDeque |
| Access pattern | FIFO (First In, First Out) | LIFO (Last In, First Out) |
| Main operations | offer(), poll(), peek() | push(), pop(), peek() |
| Add element | offer(e) / add(e) | push(e) |
| Remove element | poll() / remove() | pop() |
| Examine element | peek() / element() | peek() |
| Use cases | Breadth-first search, scheduling, buffering | Depth-first search, expression evaluation, undo mechanisms |
| Null elements | Implementation dependent | Implementation dependent |
| Legacy status | Modern interface | Stack class is considered legacy |

### Code Example:

```java
// Queue examples
Queue<String> queue = new LinkedList<>();

// Adding elements to queue
queue.offer("First");
queue.offer("Second");
queue.offer("Third");

System.out.println("Queue: " + queue);  // Output: [First, Second, Third]

// Removing elements from queue (FIFO)
String firstElement = queue.poll();  // Removes and returns "First"
System.out.println("Removed: " + firstElement);
System.out.println("Queue after removal: " + queue);  // Output: [Second, Third]

// Examining the next element without removal
String nextElement = queue.peek();  // Returns "Second" without removing
System.out.println("Next element: " + nextElement);

// Stack examples
Stack<String> stack = new Stack<>();  // Legacy class

// Adding elements to stack
stack.push("First");
stack.push("Second");
stack.push("Third");

System.out.println("Stack: " + stack);  // Output: [First, Second, Third]

// Removing elements from stack (LIFO)
String topElement = stack.pop();  // Removes and returns "Third"
System.out.println("Removed: " + topElement);
System.out.println("Stack after removal: " + stack);  // Output: [First, Second]

// Examining the top element without removal
String top = stack.peek();  // Returns "Second" without removing
System.out.println("Top element: " + top);

// Modern alternative to Stack (recommended)
Deque<String> modernStack = new ArrayDeque<>();
modernStack.push("First");
modernStack.push("Second");
modernStack.push("Third");

System.out.println("Modern Stack: " + modernStack);
System.out.println("Popped: " + modernStack.pop());  // Removes and returns "Third"
```

### Additional Queue Implementations:

```java
// Priority Queue - elements ordered by natural ordering or Comparator
Queue<Integer> priorityQueue = new PriorityQueue<>();
priorityQueue.offer(5);
priorityQueue.offer(1);
priorityQueue.offer(3);
System.out.println("Priority Queue: " + priorityQueue);  // Internal structure may differ
System.out.println("Polling from PriorityQueue: " + priorityQueue.poll());  // Returns smallest element (1)

// ArrayDeque as a Queue
Queue<String> arrayDequeQueue = new ArrayDeque<>();
arrayDequeQueue.offer("First");
arrayDequeQueue.offer("Second");
arrayDequeQueue.offer("Third");
System.out.println("ArrayDeque as Queue: " + arrayDequeQueue);
```

[Back to Top](#table-of-contents)

