# Java HashMap - Comprehensive Documentation

## Table of Contents

| # | Topic |
|---|-------|
| **HashMap Fundamentals** | |
| 1 | [How HashMap works in Java](#how-hashmap-works-in-java) |
| 2 | [How does HashMap handle collisions in Java](#how-does-hashmap-handle-collisions-in-java) |
| 3 | [What happens when a duplicate key is put into a HashMap](#what-happens-when-a-duplicate-key-is-put-into-a-hashmap) |
| 4 | [What is the load factor and initial capacity in HashMap](#what-is-the-load-factor-and-initial-capacity-in-hashmap) |
| 5 | [How to design a good key for HashMap](#how-to-design-a-good-key-for-hashmap) |
| **HashMap Variants** | |
| 6 | [What is LinkedHashMap in Java](#what-is-linkedhashmap-in-java) |
| 7 | [How do WeakHashMap work](#how-do-weakhashmap-work) |
| 8 | [What is EnumMap in Java](#what-is-enummap-in-java) |
| 9 | [What is IdentityHashMap and when to use it](#what-is-identityhashmap-and-when-to-use-it) |
| **Concurrent Maps** | |
| 10 | [Why is ConcurrentHashMap faster than Hashtable in Java](#why-is-concurrenthashmap-faster-than-hashtable-in-java) |
| 11 | [How does ConcurrentHashMap work internally](#how-does-concurrenthashmap-work-internally) |
| **Comparisons** | |
| 12 | [What are the differences between HashMap and HashSet](#what-are-the-differences-between-hashmap-and-hashset) |
| **Performance and Best Practices** | |
| 13 | [What are the time complexities of HashMap operations](#what-are-the-time-complexities-of-hashmap-operations) |
| 14 | [How to synchronize HashMap in Java](#how-to-synchronize-hashmap-in-java) |
| 15 | [Common HashMap interview coding problems](#common-hashmap-interview-coding-problems) |

## How HashMap works in Java

HashMap in Java is a hash table-based implementation of the Map interface that stores key-value pairs. Here's how it works internally:

* **Internal Structure**: HashMap stores data in an array of nodes called **buckets** or **bins**
* **Hashing Process**:
  1. When you put a key-value pair, Java computes the key's **hashCode()**
  2. The hashCode is further processed to determine the index in the internal array
  3. The key-value pair is stored in a node at that index

```java
// Creating and using a HashMap
HashMap<String, Integer> userScores = new HashMap<>();

// Adding key-value pairs
userScores.put("Alice", 95);
userScores.put("Bob", 82);
userScores.put("Charlie", 90);

// Retrieving a value
int aliceScore = userScores.get("Alice"); // Returns 95

// Checking if a key exists
boolean hasKey = userScores.containsKey("David"); // Returns false

// Iterating through a HashMap
for (Map.Entry<String, Integer> entry : userScores.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// Sample output:
// Alice: 95
// Bob: 82
// Charlie: 90
```

* **Hash Calculation**: The index in the array is calculated using:
  ```
  index = hash(key) & (n-1)
  ```
  where n is the capacity (size of the internal array)

* **Data Structure Evolution**:
  * Before Java 8: Simple linked lists were used for collision resolution
  * Java 8 and later: Uses a combination of linked lists and balanced trees (Red-Black Trees)
    * Linked lists for small numbers of collisions (< 8 nodes)
    * Balanced trees for larger numbers (≥ 8 nodes) to improve worst-case performance

* **Null Support**: HashMap allows one null key and multiple null values

[Back to Top](#table-of-contents)

## How does HashMap handle collisions in Java

Collision occurs when two different keys produce the same hash code or when two different hash codes map to the same bucket. Java's HashMap handles collisions effectively using:

* **Chaining**: The primary collision resolution technique
* **Evolution of Collision Resolution**:

### Pre-Java 8 Approach:
* Used simple **linked lists** for each bucket
* All colliding elements were stored in a linked list at the bucket location
* Worst-case performance was O(n) for retrieval operations

### Java 8 and Later Approach:
* Uses a **hybrid approach** combining linked lists and balanced trees
* Linked lists are used initially for collisions
* When the number of collisions in a bucket exceeds a threshold (TREEIFY_THRESHOLD = 8), the linked list is converted to a **Red-Black Tree**
* If the number of elements falls below UNTREEIFY_THRESHOLD (6), it converts back to a linked list
* This improves worst-case performance from O(n) to O(log n) for buckets with many collisions

```java
// Internal representation (simplified)
public class HashMap<K,V> {
    // Node class representing entries in a linked list
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next; // Reference to next node in the linked list
        
        // Constructor and methods...
    }
    
    // TreeNode class used when converted to a Red-Black Tree
    static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
        TreeNode<K,V> parent;
        TreeNode<K,V> left;
        TreeNode<K,V> right;
        TreeNode<K,V> prev;
        boolean red;
        
        // Constructor and tree operations...
    }
    
    // Other fields and methods...
}
```

* **Collision Resolution Process**:
  1. Key's hashCode is computed
  2. Hash is used to find the bucket index
  3. If bucket is empty, new Node is inserted
  4. If bucket already contains nodes:
     * The key is compared with existing keys (using equals())
     * If key already exists, value is replaced
     * If key is new, it's added to the collision chain (linked list or tree)

* **Performance Consideration**: 
  * Good hash function and implementation of equals() and hashCode() in keys reduce collisions
  * The treeification of buckets helps maintain good performance even with many collisions

[Back to Top](#table-of-contents)

## What happens when a duplicate key is put into a HashMap

When you attempt to insert a key-value pair where the key already exists in the HashMap, the following occurs:

* **Value Replacement**: The new value **replaces** the old value associated with the key
* **Return Value**: The `put()` method returns the previous value associated with the key
* **HashMap Size**: The size of the HashMap remains unchanged since no new key is added

```java
HashMap<String, Integer> map = new HashMap<>();

// Initial put operations
map.put("apple", 10);
map.put("banana", 20);
System.out.println(map); // Output: {apple=10, banana=20}

// Duplicate key with new value
Integer previousValue = map.put("apple", 30);
System.out.println("Previous value: " + previousValue); // Output: Previous value: 10
System.out.println(map); // Output: {apple=30, banana=20}

// Size remains unchanged
System.out.println("Size: " + map.size()); // Output: Size: 2
```

* **Duplicate Detection Process**:
  1. HashMap calculates the hash code of the key
  2. It finds the appropriate bucket
  3. It traverses the nodes in the bucket (linked list or tree)
  4. If a node with the same key is found (using equals() method), its value is updated
  5. If no matching key is found, a new node is added

* **Alternative Methods**:
  * `putIfAbsent(K key, V value)`: Only inserts the key-value pair if the key isn't present or is mapped to null
  * `replace(K key, V value)`: Replaces the entry only if the key exists

```java
// Using putIfAbsent
map.putIfAbsent("apple", 40); // Won't change existing entry
System.out.println(map); // Output: {apple=30, banana=20}

map.putIfAbsent("cherry", 50); // Will add new entry
System.out.println(map); // Output: {apple=30, banana=20, cherry=50}
```

* **Important Note**: The key comparison uses both hashCode() and equals() methods, so proper implementation of these methods is crucial for correct behavior with duplicate keys

[Back to Top](#table-of-contents)

## What is the load factor and initial capacity in HashMap

Load factor and initial capacity are two critical parameters that influence the performance and memory usage of a HashMap in Java:

### Initial Capacity

* **Definition**: The number of buckets (or slots) in the hash table when it is created
* **Default Value**: 16 (2^4)
* **Purpose**: Determines the initial size of the internal array
* **Impact**: 
  * Higher initial capacity reduces the need for resizing but uses more memory
  * Lower initial capacity saves memory but may require more frequent resizing

### Load Factor

* **Definition**: The measure of how full the HashMap is allowed to get before it is resized
* **Default Value**: 0.75 (75% full)
* **Formula**: Load Factor = Number of entries / Number of buckets
* **Resize Trigger**: When (number of entries) > (capacity × load factor)
* **Impact**:
  * Higher load factor:
    * Less memory overhead
    * More collisions
    * Potentially slower lookup times
  * Lower load factor:
    * More memory overhead
    * Fewer collisions
    * Potentially faster lookup times

### Specifying Load Factor and Initial Capacity

```java
// Different ways to initialize HashMap with custom parameters

// Default constructor: capacity=16, load factor=0.75
HashMap<String, Integer> map1 = new HashMap<>();

// Custom initial capacity: capacity=32, load factor=0.75
HashMap<String, Integer> map2 = new HashMap<>(32);

// Custom capacity and load factor: capacity=32, load factor=0.6
HashMap<String, Integer> map3 = new HashMap<>(32, 0.6f);

// Creating from another map: capacity for holding all elements from the given map
HashMap<String, Integer> map4 = new HashMap<>(anotherMap);
```

### Resizing (Rehashing)

* **Process**:
  1. Create a new internal array with double the capacity
  2. Rehash all existing entries to their new positions
  3. Replace the old array with the new one
* **Cost**: O(n) time complexity where n is the number of entries
* **Capacity Growth**: Always grows to a power of 2 (16 → 32 → 64 → 128 ...)

### Performance Considerations

| Consideration | Description |
|---------------|-------------|
| Memory Usage | Higher capacity = More memory |
| Access Time | Lower load factor = Fewer collisions = Faster access |
| Resize Frequency | Higher load factor = Less frequent resizing operations |
| Initial Sizing | If expected size is known, set capacity = expected size / load factor |

* **Best Practice**: If you know the approximate number of entries in advance, initialize the HashMap with an appropriate capacity to avoid costly resize operations:

```java
// If you expect to store 100 entries
int expectedEntries = 100;
float loadFactor = 0.75f;
int initialCapacity = (int)(expectedEntries / loadFactor);

HashMap<String, Integer> map = new HashMap<>(initialCapacity, loadFactor);
```

[Back to Top](#table-of-contents)

## How to design a good key for HashMap

Designing effective keys for a HashMap is crucial for ensuring optimal performance and correct behavior. Here are the essential principles and best practices:

### Key Requirements

* **Immutability**: Keys should be immutable to prevent changes after insertion
* **Consistent hashCode()**: The hashCode() method must consistently return the same value for an object
* **Proper equals() Implementation**: The equals() method must correctly determine object equality
* **hashCode() and equals() Compatibility**: If two objects are equal, they must have the same hash code

### Best Practices for HashMap Keys

1. **Use Immutable Classes**:
   * Strings, Integer, Float, and other wrapper classes
   * Enums (ideal keys because of their uniqueness and immutability)
   * Custom immutable classes

2. **For Custom Classes as Keys**:
   * Make fields final to enforce immutability
   * Override both hashCode() and equals() methods
   * Include all fields that determine object identity in both methods
   * Use Objects.hash() for computing hash codes of multiple fields

```java
// Example of a well-designed key class
public final class PersonKey {
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    
    public PersonKey(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }
    
    // Getters only (no setters to maintain immutability)
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        PersonKey personKey = (PersonKey) o;
        return Objects.equals(firstName, personKey.firstName) &&
               Objects.equals(lastName, personKey.lastName) &&
               Objects.equals(dateOfBirth, personKey.dateOfBirth);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, dateOfBirth);
    }
}

// Using the custom key
HashMap<PersonKey, String> personData = new HashMap<>();
personData.put(new PersonKey("John", "Doe", LocalDate.of(1990, 5, 15)), "Employee ID: 12345");
```

3. **Common Pitfalls to Avoid**:

* **Mutable Keys**: If a key's hashCode changes after insertion, the entry may become "lost" in the HashMap
  
```java
// Problematic example with mutable key
public class MutableKey {
    private String value; // not final!
    
    // Constructor and other methods...
    
    public void setValue(String value) {
        this.value = value; // Allows changing the value after creation!
    }
    
    @Override
    public int hashCode() {
        return value.hashCode(); // Hash depends on mutable field!
    }
}

// This can lead to lost entries
HashMap<MutableKey, String> map = new HashMap<>();
MutableKey key = new MutableKey("initial");
map.put(key, "value");
key.setValue("changed"); // Changes the hash code!
String value = map.get(key); // Will likely return null!
```

* **Inconsistent hashCode() and equals()**: Can lead to duplicate entries or inability to retrieve entries

* **Poor Hash Distribution**: Keys that produce many identical hash codes will degrade performance

4. **Advanced Techniques**:

* **Cache Hash Codes**: For complex keys, compute the hash code once and store it
* **Use Fields with Good Distribution**: Include distinguishing fields in the hash code calculation
* **Consider Hash Sensitivity**: Some applications may need custom hashing strategies

### Performance Considerations

| Consideration | Impact |
|---------------|--------|
| Hash Distribution | Good distribution minimizes collisions |
| Computation Complexity | Simple hashCode() and equals() improve speed |
| Field Selection | Choose fields that provide good uniqueness |
| Object Size | Smaller keys may improve memory usage |

[Back to Top](#table-of-contents)

## What is LinkedHashMap in Java

LinkedHashMap is a specialized implementation of HashMap that maintains the insertion order (or access order) of entries. It combines the hash table structure of HashMap with a doubly-linked list running through all entries.

### Key Features

* **Ordered Iteration**: Maintains a predictable iteration order
* **Two Ordering Modes**:
  * **Insertion-order** (default): Entries are iterated in the order they were added
  * **Access-order**: Entries are iterated from least-recently accessed to most-recently accessed
* **HashMap Performance**: Maintains O(1) performance for basic operations
* **Additional Memory**: Uses more memory than HashMap due to the linked list pointers

### Implementation Details

* **Class Hierarchy**: `LinkedHashMap<K,V>` extends `HashMap<K,V>`
* **Internal Structure**: 
  * Hash table (from HashMap) for quick lookup
  * Doubly-linked list to maintain order
  * Each entry contains references to previous and next entries in the insertion/access order

```java
// Basic usage of LinkedHashMap with insertion order
LinkedHashMap<String, Integer> insertionOrderMap = new LinkedHashMap<>();
insertionOrderMap.put("apple", 10);
insertionOrderMap.put("banana", 20);
insertionOrderMap.put("orange", 30);

// Iterating shows insertion order
System.out.println("Insertion Order:");
for (Map.Entry<String, Integer> entry : insertionOrderMap.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// Output:
// Insertion Order:
// apple: 10
// banana: 20
// orange: 30

// Creating an access-order LinkedHashMap (LRU cache-like behavior)
LinkedHashMap<String, Integer> accessOrderMap = new LinkedHashMap<>(16, 0.75f, true);
accessOrderMap.put("apple", 10);
accessOrderMap.put("banana", 20);
accessOrderMap.put("orange", 30);

// Accessing entries
accessOrderMap.get("apple");  // Moves "apple" to the end of the linked list
accessOrderMap.get("orange"); // Moves "orange" to the end of the linked list

// Iterating shows access order (least recently accessed first)
System.out.println("\nAccess Order (after accessing apple and orange):");
for (Map.Entry<String, Integer> entry : accessOrderMap.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// Output:
// Access Order (after accessing apple and orange):
// banana: 20
// apple: 10
// orange: 30
```

### Common Use Cases

1. **Cache Implementation**: Access-ordered LinkedHashMap can implement a simple LRU (Least Recently Used) cache

```java
// LRU Cache implementation using LinkedHashMap
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;
    
    public LRUCache(int capacity) {
        // Initial capacity, load factor, access-order=true
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // Remove the oldest entry when size exceeds capacity
        return size() > capacity;
    }
}

// Using the LRU cache
LRUCache<String, String> cache = new LRUCache<>(3);
cache.put("A", "Value A");
cache.put("B", "Value B");
cache.put("C", "Value C");
System.out.println(cache); // {A=Value A, B=Value B, C=Value C}

// Access an entry
cache.get("A"); // Moves A to the end (most recently used)

// Add a new entry that causes the least recently used entry to be removed
cache.put("D", "Value D");
System.out.println(cache); // {C=Value C, A=Value A, D=Value D} (B was removed)
```

2. **Predictable Iteration**: When iteration order matters in business logic
3. **Order-Sensitive Algorithm Implementations**: Algorithms that require maintaining insertion order

### Comparison with HashMap and WeakHashMap

| Feature | HashMap | LinkedHashMap | WeakHashMap |
|---------|---------|---------------|-------------|
| Order Guarantee | No order | Insertion/Access order | No order |
| Key References | Strong references | Strong references | Weak references |
| Memory Usage | Lower | Higher (linked list pointers) | Varies (GC dependent) |
| Use Case | General purpose | Order-sensitive applications | Cache-like structures |

[Back to Top](#table-of-contents)

## How do WeakHashMap work

WeakHashMap is a specialized implementation of the Map interface that uses weak references for its keys. This unique characteristic allows entries to be automatically removed when their keys are no longer referenced elsewhere in the program.

### Key Concepts

* **Weak References**: References that do not prevent garbage collection of the referenced object
* **Automatic Cleanup**: Entries with keys that have been garbage collected are automatically removed
* **Garbage Collection Dependent**: Behavior depends on JVM garbage collection timing

### How WeakHashMap Works Internally

1. **Reference Types**:
   * Keys are stored as **WeakReferences**
   * Values are stored as strong references
   
2. **Garbage Collection Process**:
   * When a key object has no strong references elsewhere in the program, it becomes eligible for garbage collection
   * After garbage collection, the JVM notifies the Reference queue associated with the WeakHashMap
   * The WeakHashMap checks this queue during operations and removes entries whose keys have been garbage collected

3. **Internal Structure**:
   * Similar to HashMap but uses Entry objects that extend WeakReference
   * Maintains a ReferenceQueue for tracking garbage-collected keys

```java
// Simplified representation of the internal structure
public class WeakHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
    // Entry extends WeakReference and implements Map.Entry
    private static class Entry<K, V> extends WeakReference<Object> implements Map.Entry<K, V> {
        V value;
        final int hash;
        Entry<K, V> next;
        
        // Constructor and methods...
    }
    
    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();
    // Other fields and methods...
    
    // Method that expunges stale entries
    private void expungeStaleEntries() {
        // Process reference queue and remove entries whose keys were GC'd
    }
}
```

### Usage Example

```java
import java.util.WeakHashMap;

public class WeakHashMapDemo {
    public static void main(String[] args) {
        // Create a WeakHashMap
        WeakHashMap<Key, String> weakMap = new WeakHashMap<>();
        
        // Create a key that we'll maintain a strong reference to
        Key strongKey = new Key("StrongKey");
        
        // Create a key that will have no other strong references
        Key weakKey = new Key("WeakKey");
        
        // Add both to the map
        weakMap.put(strongKey, "Strong Value");
        weakMap.put(weakKey, "Weak Value");
        
        // Check initial state
        System.out.println("Initial map size: " + weakMap.size());  // Output: Initial map size: 2
        System.out.println("Map contains: " + weakMap);  // Contains both entries
        
        // Remove the strong reference to weakKey
        weakKey = null;
        
        // Force garbage collection (though this doesn't guarantee immediate collection)
        System.gc();
        
        // Sleep a bit to give GC time to work
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        // Check final state - weakKey entry should be gone
        System.out.println("Final map size: " + weakMap.size());  // Output: Final map size: 1
        System.out.println("Map contains: " + weakMap);  // Contains only strongKey entry
    }
    
    static class Key {
        private String id;
        
        public Key(String id) {
            this.id = id;
        }
        
        @Override
        public String toString() {
            return "Key[" + id + "]";
        }
    }
}
```

### Differences Between HashMap and WeakHashMap

| Feature | HashMap | WeakHashMap |
|---------|---------|-------------|
| Key References | Strong references | Weak references |
| Memory Management | Manually remove entries | Automatic cleanup of unused keys |
| Performance | Generally faster | Slightly slower due to reference management |
| Thread Safety | Not thread-safe | Not thread-safe |
| Entry Removal | Only when explicitly removed | Removed when keys are garbage collected |
| Null Keys | Supports one null key | Does not reliably support null keys |

### Common Use Cases

1. **Memory-Sensitive Caches**: Allows cached objects to be reclaimed when no longer needed
2. **Observer Patterns**: Maintaining references to listeners without preventing them from being garbage collected
3. **Temporary Associations**: When you need temporary mappings that shouldn't prevent garbage collection

### Limitations and Considerations

* **Non-Deterministic Cleanup**: You cannot predict exactly when unused entries will be removed
* **Performance Overhead**: The reference management adds some overhead
* **Not for Critical Data**: Don't use for data that must persist regardless of external references

[Back to Top](#table-of-contents)

## What is EnumMap in Java

EnumMap is a specialized Map implementation designed exclusively for use with enum type keys. It offers highly efficient performance and memory characteristics due to its internal array-based representation.

### Key Characteristics

* **Enum Keys Only**: Can only use enum constants as keys
* **High Performance**: Operations are much faster than HashMap for enum keys
* **Memory Efficient**: Uses far less memory than HashMap for enum keys
* **Null Key Restriction**: Does not allow null keys (throws NullPointerException)
* **Order Guarantee**: Iteration order matches the declaration order of the enum constants

### Internal Implementation

* **Array-Based**: Internally uses an array where the ordinal value of each enum constant serves as the array index
* **Direct Indexing**: No need for hashing or complex data structures
* **Constant-Time Performance**: All basic operations (get, put, containsKey) are O(1)

```java
// Simplified internal structure (conceptual)
public class EnumMap<K extends Enum<K>, V> extends AbstractMap<K, V> implements Map<K, V> {
    // The enum class
    private final Class<K> keyType;
    
    // Array to store values (index = enum ordinal)
    private Object[] vals;
    
    // Number of key-value mappings
    private int size = 0;
    
    // Other fields and methods...
}
```

### Usage Example

```java
import java.util.EnumMap;
import java.util.Map;

public class EnumMapExample {
    // Define an enum for days of the week
    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
    
    public static void main(String[] args) {
        // Create an EnumMap
        EnumMap<Day, String> schedule = new EnumMap<>(Day.class);
        
        // Add entries
        schedule.put(Day.MONDAY, "Work");
        schedule.put(Day.TUESDAY, "Meeting");
        schedule.put(Day.WEDNESDAY, "Gym");
        schedule.put(Day.THURSDAY, "Project deadline");
        schedule.put(Day.FRIDAY, "Happy hour");
        schedule.put(Day.SATURDAY, "Hiking");
        schedule.put(Day.SUNDAY, "Rest");
        
        // Retrieve a value
        System.out.println("Wednesday plan: " + schedule.get(Day.WEDNESDAY));
        
        // Iterate through entries (in enum definition order)
        System.out.println("\nWeekly Schedule:");
        for (Map.Entry<Day, String> entry : schedule.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        
        // Check if contains key
        boolean hasFriday = schedule.containsKey(Day.FRIDAY);
        System.out.println("\nHas Friday plan? " + hasFriday);
        
        // Remove an entry
        schedule.remove(Day.THURSDAY);
        System.out.println("\nAfter removing Thursday:");
        schedule.forEach((day, activity) -> System.out.println(day + ": " + activity));
    }
}

/* Output:
Wednesday plan: Gym

Weekly Schedule:
MONDAY: Work
TUESDAY: Meeting
WEDNESDAY: Gym
THURSDAY: Project deadline
FRIDAY: Happy hour
SATURDAY: Hiking
SUNDAY: Rest

Has Friday plan? true

After removing Thursday:
MONDAY: Work
TUESDAY: Meeting
WEDNESDAY: Gym
FRIDAY: Happy hour
SATURDAY: Hiking
SUNDAY: Rest
*/
```

### Performance Advantages

EnumMap offers significant performance advantages over HashMap when using enum keys:

1. **Constant-Time Operations**: All basic operations run in constant time regardless of map size
2. **No Collisions**: Each enum constant maps to a unique index, eliminating hash collisions
3. **Compact Memory Usage**: Only stores an array sized to the number of enum constants
4. **No Hash Calculation**: No need to compute hash codes

### Comparison with HashMap

| Feature | EnumMap | HashMap |
|---------|---------|---------|
| Key Type | Enum constants only | Any object |
| Performance | O(1) guaranteed | O(1) average case |
| Memory Usage | Very low | Higher |
| Iteration Order | Enum declaration order | Unspecified |
| Null Keys | Not allowed | Allowed (one) |
| Implementation | Array-based | Hash table |

### Common Use Cases

1. **State Machines**: Mapping enum states to actions or handlers
2. **Configuration Settings**: Storing settings where options are predefined enum values
3. **Strategy Pattern Implementation**: Mapping enum types to strategy implementations
4. **Game Development**: Storing properties or behaviors for limited sets of options
5. **Performance-Critical Code**: When maximum efficiency is required with enum keys

```java
// Example: Strategy pattern with EnumMap
enum CalculationType {
    ADD, SUBTRACT, MULTIPLY, DIVIDE
}

interface MathOperation {
    double apply(double a, double b);
}

public class CalculatorWithEnumMap {
    private static final Map<CalculationType, MathOperation> operations;
    
    static {
        operations = new EnumMap<>(CalculationType.class);
        operations.put(CalculationType.ADD, (a, b) -> a + b);
        operations.put(CalculationType.SUBTRACT, (a, b) -> a - b);
        operations.put(CalculationType.MULTIPLY, (a, b) -> a * b);
        operations.put(CalculationType.DIVIDE, (a, b) -> a / b);
    }
    
    public double calculate(CalculationType type, double a, double b) {
        return operations.get(type).apply(a, b);
    }
}
```

[Back to Top](#table-of-contents)

## What is IdentityHashMap and when to use it

IdentityHashMap is a specialized Map implementation that uses reference equality (==) instead of object equality (equals()) for comparing keys. This creates fundamentally different behavior compared to standard HashMap.

### Key Characteristics

* **Reference Equality**: Uses object identity (memory address) to compare keys, not equals() method
* **System.identityHashCode()**: Uses the system identity hash code instead of hashCode() method
* **Performance**: Generally faster for identity comparison but typically less useful than regular HashMap
* **Unusual Contract**: Violates the general Map contract by using reference equality
* **Not Synchronized**: Not thread-safe by default

### Internal Implementation

* **Open Addressing**: Uses linear probing instead of separate chaining for collision resolution
* **Array-Based**: Single array where keys and values are stored alternately
* **Special Handling**: Special provisions for null keys and removal markers

```java
// Internal structure (simplified conceptual overview)
public class IdentityHashMap<K,V> extends AbstractMap<K,V> implements Map<K,V> {
    // Single array storing alternating keys and values
    private transient Object[] table;
    
    // Uses System.identityHashCode() instead of hashCode()
    private static int hash(Object x) {
        return System.identityHashCode(x);
    }
    
    // Uses == instead of equals()
    private static boolean eq(Object x, Object y) {
        return x == y;
    }
    
    // Other fields and methods...
    // Other fields and methods...
}
```

### Usage Example

```java
import java.util.*;

public class IdentityHashMapExample {
    public static void main(String[] args) {
        // Create a regular HashMap
        Map<String, String> normalMap = new HashMap<>();
        
        // Create an IdentityHashMap
        Map<String, String> identityMap = new IdentityHashMap<>();
        
        // Create String objects with the same content but different references
        String key1 = new String("key");
        String key2 = new String("key");
        
        // Verify they have the same content but are different objects
        System.out.println("key1.equals(key2): " + key1.equals(key2)); // true
        System.out.println("key1 == key2: " + (key1 == key2));         // false
        
        // Add to regular HashMap
        normalMap.put(key1, "Value for key1");
        normalMap.put(key2, "Value for key2");
        
        // Add to IdentityHashMap
        identityMap.put(key1, "Value for key1");
        identityMap.put(key2, "Value for key2");
        
        // Check sizes
        System.out.println("HashMap size: " + normalMap.size());              // 1 - Because keys are equal by equals()
        System.out.println("IdentityHashMap size: " + identityMap.size());    // 2 - Because keys are different objects
        
        // Get values
        System.out.println("HashMap value for key1: " + normalMap.get(key1)); // Value for key2 (the last value put)
        System.out.println("IdentityHashMap value for key1: " + identityMap.get(key1)); // Value for key1
        
        // Check using a different reference with same content
        String key3 = new String("key");
        System.out.println("HashMap contains key3: " + normalMap.containsKey(key3));        // true
        System.out.println("IdentityHashMap contains key3: " + identityMap.containsKey(key3)); // false
    }
}

/* Output:
key1.equals(key2): true
key1 == key2: false
HashMap size: 1
IdentityHashMap size: 2
HashMap value for key1: Value for key2
IdentityHashMap value for key1: Value for key1
HashMap contains key3: true
IdentityHashMap contains key3: false
*/
```

### When to Use IdentityHashMap

IdentityHashMap is a specialized tool that should be used only in specific scenarios:

1. **Object Topology Algorithms**: When maintaining object graphs and need to track object references
   ```java
   // Example: Detecting cycles in an object graph
   Map<Object, Boolean> visited = new IdentityHashMap<>();
   
   public boolean hasCycle(Object obj) {
       if (visited.containsKey(obj)) {
           return true; // Already visited this exact object reference
       }
       visited.put(obj, Boolean.TRUE);
       // Traverse children/related objects...
       return false;
   }
   ```

2. **Serialization Frameworks**: When tracking object references during serialization
   ```java
   // Example: Maintaining reference identity during serialization
   Map<Object, Integer> objectIds = new IdentityHashMap<>();
   int nextId = 0;
   
   public int getObjectId(Object obj) {
       Integer id = objectIds.get(obj);
       if (id == null) {
           id = nextId++;
           objectIds.put(obj, id);
       }
       return id;
   }
   ```

3. **Proxy Pattern**: When mapping original objects to their proxies
   ```java
   Map<OriginalObject, ProxyObject> objectProxies = new IdentityHashMap<>();
   
   public ProxyObject getProxyFor(OriginalObject original) {
       return objectProxies.computeIfAbsent(original, this::createNewProxy);
   }
   ```

4. **Caching Transformations**: When caching transformations of objects where equals() is overridden

5. **Memory Management Systems**: Custom memory or resource management tracking

### Comparison with HashMap

| Feature | IdentityHashMap | HashMap |
|---------|-----------------|---------|
| Key Comparison | Reference equality (==) | Object equality (equals()) |
| Hash Function | System.identityHashCode() | Object's hashCode() |
| Collision Resolution | Open addressing | Separate chaining |
| Use Case | Object reference tracking | General purpose key-value storage |
| Memory Usage | Can be more efficient | Generally more memory-efficient for normal use |
| Map Contract | Violates standard contract | Follows standard contract |

### Limitations and Considerations

* **Unexpected Behavior**: Can lead to unexpected behavior if you forget it uses reference equality
* **Performance Overhead**: May have more collisions if many different objects hash to same identity hash code
* **Security Implications**: Identity hash codes might be more predictable than well-distributed custom hash codes

[Back to Top](#table-of-contents)

## Why is ConcurrentHashMap faster than Hashtable in Java

ConcurrentHashMap is significantly faster than Hashtable in many scenarios, especially in concurrent environments. Let's explore why:

### Key Performance Advantages

1. **Fine-Grained Locking**:
   * **Hashtable**: Uses a single lock for the entire table (coarse-grained locking)
   * **ConcurrentHashMap**: Uses segment/bucket-level locking or lock striping (before Java 8) and even finer-grained node-level locking (Java 8+)

2. **Read Operations**:
   * **Hashtable**: All operations, including reads, require acquiring the lock
   * **ConcurrentHashMap**: Read operations generally do not require locking, allowing multiple reads to proceed concurrently

3. **Write Concurrency**:
   * **Hashtable**: Only one thread can modify the map at a time
   * **ConcurrentHashMap**: Multiple threads can modify different segments/buckets concurrently

### Evolution of ConcurrentHashMap

#### Pre-Java 8 Implementation
* Used segment-level locking (lock striping)
* Default of 16 segments, each acting as a mini-Hashtable
* Only locked the segment being modified

#### Java 8 and Later Implementation
* Moved to node-level locking (even finer granularity)
* Uses CAS (Compare-And-Swap) operations for atomic updates
* Special handling for common cases like empty bins
* Tree bins (similar to HashMap) for better collision handling

### Performance Comparison Example

```java
import java.util.*;
import java.util.concurrent.*;

public class MapPerformanceComparison {
    private static final int THREAD_COUNT = 16;
    private static final int OPERATIONS_PER_THREAD = 100000;
    
    public static void main(String[] args) throws Exception {
        // Create the maps
        final Hashtable<UUID, UUID> hashtable = new Hashtable<>();
        final ConcurrentHashMap<UUID, UUID> concurrentMap = new ConcurrentHashMap<>();
        
        // Pre-populate with some data
        for (int i = 0; i < 10000; i++) {
            UUID key = UUID.randomUUID();
            UUID value = UUID.randomUUID();
            hashtable.put(key, value);
            concurrentMap.put(key, value);
        }
        
        // Test with read-heavy workload (95% reads, 5% writes)
        System.out.println("Read-heavy workload (95% reads, 5% writes):");
        testMap("Hashtable", hashtable, 0.95);
        testMap("ConcurrentHashMap", concurrentMap, 0.95);
        
        // Test with write-heavy workload (50% reads, 50% writes)
        System.out.println("\nWrite-heavy workload (50% reads, 50% writes):");
        testMap("Hashtable", hashtable, 0.5);
        testMap("ConcurrentHashMap", concurrentMap, 0.5);
    }
    
    private static void testMap(String mapName, Map<UUID, UUID> map, double readRatio) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    Random random = new Random();
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        UUID key = UUID.randomUUID();
                        
                        if (random.nextDouble() < readRatio) {
                            // Read operation
                            map.get(key);
                        } else {
                            // Write operation
                            map.put(key, UUID.randomUUID());
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        executor.shutdown();
        
        System.out.println(mapName + " took " + (endTime - startTime) + " ms");
    }
}

/* Sample output (times will vary based on system):
Read-heavy workload (95% reads, 5% writes):
Hashtable took 3542 ms
ConcurrentHashMap took 876 ms

Write-heavy workload (50% reads, 50% writes):
Hashtable took 5231 ms
ConcurrentHashMap took 1982 ms
*/
```

### Technical Reasons for Better Performance

1. **Lock Contention Reduction**:
   * Finer-grained locking reduces thread contention
   * Less time spent waiting for locks means higher throughput

2. **Modern Concurrency Techniques**:
   * Use of volatile variables for visibility
   * Compare-and-swap (CAS) operations for atomic updates
   * Optimistic locking strategies

3. **Implementation Improvements**:
   * More efficient internal data structures
   * Better handling of hash collisions
   * Specialized code paths for common operations

### Specific Scenarios Where ConcurrentHashMap Excels

| Scenario | Performance Advantage |
|----------|----------------------|
| High Read/Low Write | Nearly linear scalability with number of threads |
| Multiple Threads Accessing Different Keys | Lock striping/node locking allows true parallelism |
| High Concurrency | Better throughput as thread count increases |
| Size-based Operations | Specialized optimizations for operations like size() |

### When Hashtable Might Still Be Used

Despite ConcurrentHashMap's performance advantages, Hashtable might still be used in:

1. **Legacy Code**: Existing codebase that relies on Hashtable behavior
2. **Synchronization Guarantees**: When full synchronization is explicitly required
3. **Full Thread Safety**: When absolute thread safety for all methods is needed (including methods like elements(), keys() and values())

### Best Practices

* **Default Choice**: Use ConcurrentHashMap for concurrent scenarios
* **Initial Sizing**: Set initial capacity appropriately to avoid resizing
* **Avoid Unnecessary Synchronization**: Don't manually synchronize on ConcurrentHashMap
* **Bulk Operations**: Use specialized bulk methods (forEach, search, reduce) for better performance

[Back to Top](#table-of-contents)

## How does ConcurrentHashMap work internally

ConcurrentHashMap implements a highly optimized concurrent hash map that offers excellent performance in multi-threaded environments. Here's an in-depth look at its internal workings:

### Core Design Evolution

#### Pre-Java 8 Design (Java 5-7)
* **Segment-Based Architecture**:
  * Map divided into segments (default 16)
  * Each segment was effectively a Hashtable with its own lock
  * Concurrency level = number of segments

#### Java 8 and Later Design
* **Node-Based Architecture**:
  * Abandoned segment locks in favor of much finer-grained approach
  * Locks individual hash buckets when necessary
  * Uses synchronized blocks on the first node in each bucket
  * Employs CAS operations for atomic updates when possible

### Key Internal Components

* **Node Structure**:
  ```java
  static class Node<K,V> implements Map.Entry<K,V> {
      final int hash;
      final K key;
      volatile V value;  // Note the volatile keyword
      volatile Node<K,V> next;
      // Methods...
  }
  ```

* **Table Structure**:
  ```java
  // The array of nodes (hash buckets)
  transient volatile Node<K,V>[] table;
  
  // Used for resizing operations
  private transient volatile Node<K,V>[] nextTable;
  
  // Base counter value, primarily under CAS control
  private transient volatile long baseCount;
  ```

### Main Operations

#### 1. Get Operation

```java
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    int h = spread(key.hashCode());  // Compute hash
    
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {
        // Direct hit on first node in bucket
        if ((eh = e.hash) == h) {
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val;
        }
        // Handle cases like TreeBins or forwarding nodes
        else if (eh < 0)
            return (p = e.find(h, key)) != null ? p.val : null;
            
        // Search rest of the list
        while ((e = e.next) != null) {
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null;
}
```

* **No Locking Required**: Read operations proceed without acquiring locks
* **Volatile Fields**: Ensure visibility of latest changes across threads
* **Memory Consistency**: Guaranteed visibility through volatile reads

#### 2. Put Operation

```java
// Simplified for clarity
public V put(K key, V value) {
    return putVal(key, value, false);
}

final V putVal(K key, V value, boolean onlyIfAbsent) {
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        // Initialize table if needed
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
            
        // If bin is empty, try to CAS a new node
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))
                break;  // Success, no further action needed
        }
        // Handle resizing
        else if ((fh = f.hash) == MOVED)
            tab = helpTransfer(tab, f);
        else {
            V oldVal = null;
            // Lock the first node in the bin
            synchronized (f) {
                if (tabAt(tab, i) == f) {
                    if (fh >= 0) {
                        // Insert into linked list or update existing
                        // ...
                    }
                    else if (f instanceof TreeBin) {
                        // Insert into tree
                        // ...
                    }
                }
            }
            // Possible tree conversion if list gets too long
            // ...
        }
    }
    // Increment counter and possibly trigger resize
    addCount(1L, binCount);
    return null;
}
```

* **Optimistic Approach**: Try CAS first for empty buckets
* **Fine-Grained Locking**: Only locks the affected bucket, not the entire map
* **Tree Conversion**: Similar to HashMap, converts to tree structure for large buckets

#### 3. Resize Operation

* **Concurrent Resize**: Multiple threads can help with the resizing process
* **Transfer Method**: Splits the resize work among helping threads
* **ForwardingNode**: Special node placed in buckets being transferred to redirect operations

```java
// During resize, threads help transfer nodes from old to new table
private final void transfer(Node<K,V>[] tab, Node<K,V>[] nextTab) {
    // Each thread claims regions of the array to transfer
    // Uses stride length based on available CPUs
    
    // For each bucket:
    // 1. Claim bucket with CAS operation
    // 2. Process it and add to new table
    // 3. Place ForwardingNode in old table
    // 4. Move to next bucket
}
```

### Concurrency Mechanisms

1. **Compare-And-Swap (CAS)**: Uses Unsafe.compareAndSwapObject for atomic operations
   ```java
   // Example of CAS operation used internally
   static final <K,V> boolean casTabAt(Node<K,V>[] tab, int i, Node<K,V> c, Node<K,V> v) {
       return U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
   }
   ```

2. **Volatile Variables**: Ensures visibility of changes across threads
   ```java
   // Reads from volatile arrays with proper memory ordering
   static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab, int i) {
       return (Node<K,V>)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);
   }
   ```

3. **Synchronized Blocks**: Used for modifications to bucket lists
   ```java
   synchronized (f) {
       // Bucket updates here...
   }
   ```

4. **Special Node Types**:
   * **ForwardingNode**: Used during resize to mark buckets being transferred
   * **TreeBin**: Container for Red-Black tree nodes, implements specialized locking
   * **ReservationNode**: Placeholder used in computeIfAbsent and related methods

### Size Calculation

* **Counter Cells**: Uses striped counters to reduce contention
* **BaseCount**: Main counter, with per-thread counters used when contention occurs
* **CounterCell Array**: Dynamically grows based on contention level

```java
// Size is calculated by summing baseCount and all active counter cells
public int size() {
    long n = sumCount();
    return ((n < 0L) ? 0 :
            (n > (long)Integer.MAX_VALUE) ? Integer.MAX_VALUE :
            (int)n);
}

final long sumCount() {
    CounterCell[] as = counterCells; CounterCell a;
    long sum = baseCount;
    if (as != null) {
        for (int i = 0; i < as.length; ++i) {
            if ((a = as[i]) != null)
                sum += a.value;
        }
    }
    return sum;
}
```

### Performance Characteristics

| Operation | Performance | Concurrency Approach |
|-----------|-------------|---------------------|
| get() | O(1) average | Lock-free using volatile reads |
| put() | O(1) average | CAS for empty buckets, synchronized for collisions |
| remove() | O(1) average | Synchronized on first node in bucket |
| containsKey() | O(1) average | Lock-free using volatile reads |
| size() | O(n) counters | Striped counters to reduce contention |
| clear() | O(n) | Table-level lock |
| Iterator ops | O(capacity + size) | Weakly consistent (may not reflect concurrent changes) |

### Notable Optimizations

1. **Hash Spreading**: Improved distribution of hash codes
   ```java
   static final int spread(int h) {
       return (h ^ (h >>> 16)) & HASH_BITS;
   }
   ```

2. **Resizing Thresholds**: Dynamic thresholds for table resizing based on load
3. **Helper Threads**: Multiple threads can contribute to resize operations
4. **Specialized Bulk Operations**: Methods like forEach, search, reduce optimized for concurrent operation
5. **Contended Cell Padding**: CounterCells use padding to avoid false sharing

[Back to Top](#table-of-contents)

## What are the differences between HashMap and HashSet

HashMap and HashSet are both popular collection classes in Java, but they serve different purposes and have distinct characteristics. Here's a comprehensive comparison:

### Basic Differences

| Feature | HashMap | HashSet |
|---------|---------|---------|
| Interface | Implements Map interface | Implements Set interface |
| Purpose | Stores key-value pairs | Stores unique elements |
| Storage Type | Key-value mappings | Individual elements |
| Duplicate Elements | No duplicate keys, but values can be duplicated | No duplicate elements allowed |
| Null Elements | One null key allowed, multiple null values allowed | One null element allowed |
| Internal Structure | Hash table of Entry objects | Backed by a HashMap internally |
| Elements Maintained | Both keys and values | Only values |

### Implementation Relationship

* **HashSet's Internal Implementation**: HashSet is actually implemented using HashMap
  ```java
  // Simplified internal structure of HashSet
  public class HashSet<E> extends AbstractSet<E> implements Set<E> {
      private transient HashMap<E, Object> map;
      
      // Dummy value to associate with an element in the map
      private static final Object PRESENT = new Object();
      
      public HashSet() {
          map = new HashMap<>();
      }
      
      public boolean add(E e) {
          return map.put(e, PRESENT) == null;
      }
      
      public boolean contains(Object o) {
          return map.containsKey(o);
      }
      
      // Other methods delegate to the internal map
  }
  ```

### Usage Examples

**HashMap Example:**
```java
import java.util.HashMap;
import java.util.Map;

public class HashMapExample {
    public static void main(String[] args) {
        // Creating a HashMap
        HashMap<String, Integer> studentScores = new HashMap<>();
        
        // Adding key-value pairs
        studentScores.put("Alice", 95);
        studentScores.put("Bob", 87);
        studentScores.put("Charlie", 91);
        
        // Getting a value by key
        System.out.println("Alice's score: " + studentScores.get("Alice"));
        
        // Updating a value
        studentScores.put("Bob", 89);  // Updates Bob's score
        System.out.println("Bob's updated score: " + studentScores.get("Bob"));
        
        // Checking if a key exists
        System.out.println("Does David exist? " + studentScores.containsKey("David"));
        
        // Checking if a value exists
        System.out.println("Is there a score of 91? " + studentScores.containsValue(91));
        
        // Iterating through the HashMap
        System.out.println("\nAll student scores:");
        for (Map.Entry<String, Integer> entry : studentScores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        
        // Removing an entry
        studentScores.remove("Charlie");
        System.out.println("\nAfter removing Charlie, size is: " + studentScores.size());
    }
}
```

**HashSet Example:**
```java
import java.util.HashSet;

public class HashSetExample {
    public static void main(String[] args) {
        // Creating a HashSet
        HashSet<String> fruits = new HashSet<>();
        
        // Adding elements
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");
        
        // Adding a duplicate (will be ignored)
        boolean added = fruits.add("Apple");
        System.out.println("Was duplicate Apple added? " + added);  // false
        
        // Checking if an element exists
        System.out.println("Contains Mango? " + fruits.contains("Mango"));  // false
        
        // Iterating through the HashSet
        System.out.println("\nAll fruits:");
        for (String fruit : fruits) {
            System.out.println(fruit);
        }
        
        // Removing an element
        fruits.remove("Banana");
        System.out.println("\nAfter removing Banana, size is: " + fruits.size());
        
        // Creating a HashSet from another collection
        HashSet<String> moreFruits = new HashSet<>();
        moreFruits.add("Grape");
        moreFruits.add("Apple");  // Duplicate with first set
        
        // Union operation
        HashSet<String> allFruits = new HashSet<>(fruits);
        allFruits.addAll(moreFruits);
        System.out.println("\nCombined fruits (union): " + allFruits);
    }
}
```

### Detailed Comparison of Operations

| Operation | HashMap | HashSet |
|-----------|---------|---------|
| Add Element | `put(key, value)` | `add(element)` |
| Check Existence | `containsKey(key)` or `containsValue(value)` | `contains(element)` |
| Remove Element | `remove(key)` | `remove(element)` |
| Accessing Element | `get(key)` | No direct access method (use iterator) |
| Iteration | Iterate over keys, values, or entries | Iterate over elements |

### Performance Characteristics

| Aspect | HashMap | HashSet |
|--------|---------|---------|
| Time Complexity (avg) | O(1) for put/get/containsKey | O(1) for add/contains/remove |
| Time Complexity (worst) | O(n) with hash collisions | O(n) with hash collisions |
| Space Complexity | O(capacity + size) | O(capacity + size) |
| Memory Usage | Higher (stores key-value pairs) | Lower for same number of elements |
| Performance Overhead | Direct implementation | Additional overhead from HashMap delegation |

### Use Cases

**When to use HashMap:**
* When you need to store and retrieve values based on a key
* When you need to map objects to other objects
* When you need fast lookups by key
* For implementing caches, dictionaries, or mappings
* When tracking counts or frequencies of objects

**When to use HashSet:**
* When you need to store unique elements
* When you only care about the presence of an object, not retrieving values
* For removing duplicates from a collection
* For set operations (union, intersection, difference)
* When implementing mathematical set concepts

### Code Example: Using Both Together

```java
import java.util.*;

public class HashMapAndHashSetTogether {
    public static void main(String[] args) {
        // Scenario: Tracking unique visitors for different websites
        
        // Using HashMap to track visitor count per website
        HashMap<String, Integer> websiteVisitors = new HashMap<>();
        
        // Using HashSet to track unique visitors for each website
        HashMap<String, HashSet<String>> uniqueVisitors = new HashMap<>();
        
        // Record visits
        recordVisit(websiteVisitors, uniqueVisitors, "example.com", "user1");
        recordVisit(websiteVisitors, uniqueVisitors, "example.com", "user2");
        recordVisit(websiteVisitors, uniqueVisitors, "example.com", "user1"); // Duplicate
        recordVisit(websiteVisitors, uniqueVisitors, "site.org", "user1");
        recordVisit(websiteVisitors, uniqueVisitors, "site.org", "user3");
        
        // Print statistics
        System.out.println("Website visit statistics:");
        for (String website : websiteVisitors.keySet()) {
            int totalVisits = websiteVisitors.get(website);
            int uniqueVisitCount = uniqueVisitors.get(website).size();
            
            System.out.println(website + ":");
            System.out.println("  Total visits: " + totalVisits);
            System.out.println("  Unique visitors: " + uniqueVisitCount);
            System.out.println("  Visitor IDs: " + uniqueVisitors.get(website));
        }
    }
    
    private static void recordVisit(
            HashMap<String, Integer> visitCounts,
            HashMap<String, HashSet<String>> uniqueVisitors,
            String website, String userId) {
        
        // Update total visit count
        visitCounts.put(website, visitCounts.getOrDefault(website, 0) + 1);
        
        // Update unique visitors set
        if (!uniqueVisitors.containsKey(website)) {
            uniqueVisitors.put(website, new HashSet<>());
        }
        uniqueVisitors.get(website).add(userId);
    }
}

/* Sample output:
Website visit statistics:
example.com:
  Total visits: 3
  Unique visitors: 2
  Visitor IDs: [user1, user2]
site.org:
  Total visits: 2
  Unique visitors: 2
  Visitor IDs: [user1, user3]
*/
```

[Back to Top](#table-of-contents)

## What are the time complexities of HashMap operations

Understanding the time complexities of HashMap operations is crucial for writing efficient Java code. Here's a comprehensive analysis:

### Basic Operations Time Complexity

| Operation | Average Case | Worst Case | Notes |
|-----------|--------------|------------|-------|
| get(K key) | O(1) | O(n) | Constant time retrieval in average case |
| put(K key, V value) | O(1) | O(n) | Constant time insertion in average case |
| remove(Object key) | O(1) | O(n) | Constant time removal in average case |
| containsKey(Object key) | O(1) | O(n) | Same complexity as get() |
| containsValue(Object value) | O(n) | O(n) | Must traverse all entries |
| size() | O(1) | O(1) | Pre-calculated and stored as a field |
| isEmpty() | O(1) | O(1) | Simple check if size == 0 |

### Iterator Operations

| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| Creation (keySet/values/entrySet) | O(1) | Just creates iterator object |
| Iterator.next() | O(1) amortized | May need to scan for next valid entry |
| Iterator.hasNext() | O(1) | Simple check |
| Iterator.remove() | O(1) | Average case |
| Traversing all elements | O(capacity + size) | Depends on capacity and actual entries |

### Bulk Operations

| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| clear() | O(capacity) | Needs to clear all buckets |
| putAll(Map m) | O(m.size()) | If m.size() >> capacity, additional resize cost |
| clone() | O(capacity + size) | Needs to create new copy of all data structures |

### Factors Affecting Performance

1. **Load Factor**: Controls when resizing occurs (default: 0.75)
   * Higher load factor → More collisions → Worse performance but less memory
   * Lower load factor → Fewer collisions → Better performance but more memory

2. **Initial Capacity**: Initial size of the internal array
   * Too small → More resizing operations → Worse performance
   * Too large → Wasted memory → Good performance but inefficient space usage

3. **Hash Function Quality**: Critical for even distribution
   * Good hash function → Even distribution → Fewer collisions → Better performance
   * Poor hash function → Uneven distribution → More collisions → Worse performance

4. **Collision Resolution**: TreeNodes vs LinkedList
   * Java 8+ converts linked lists to trees when buckets exceed threshold (8 nodes)
   * Improves worst-case performance from O(n) to O(log n) for heavily collided buckets

### When HashMap Performance Degrades to O(n)

1. **Hash collisions**: When multiple keys hash to the same bucket
2. **Poor hashing function**: Causes more collisions
3. **Malicious inputs**: Specially crafted keys causing many collisions


### Code Examples with Performance Analysis
### Code Example - Basic HashMap Operations

```java
import java.util.HashMap;

public class HashMapTimeComplexity {
    public static void main(String[] args) {
        // Create a HashMap with default initial capacity (16) and load factor (0.75)
        HashMap<String, Integer> map = new HashMap<>();
        
        // put operation - O(1) average case
        map.put("one", 1);  // Adding a key-value pair
        map.put("two", 2);
        map.put("three", 3);
        
        // get operation - O(1) average case
        Integer value = map.get("one");  // Retrieving value for key "one"
        System.out.println("Value for key 'one': " + value);
        
        // containsKey operation - O(1) average case
        boolean hasKey = map.containsKey("two");
        System.out.println("Contains key 'two': " + hasKey);
        
        // containsValue operation - O(n) 
        boolean hasValue = map.containsValue(3);
        System.out.println("Contains value 3: " + hasValue);
        
        // remove operation - O(1) average case
        map.remove("three");
        System.out.println("After removing 'three': " + map);
        
        // size operation - O(1)
        System.out.println("Size of map: " + map.size());
    }
}
```

**Sample output:**
```
Value for key 'one': 1
Contains key 'two': true
Contains value 3: true
After removing 'three': {one=1, two=2}
Size of map: 2
```

### HashMap Resizing and Performance Impact

When a HashMap reaches its capacity × load factor, it resizes by:
1. Creating a new internal array (approximately double the size)
2. Rehashing all existing entries to place them in new buckets
3. This operation is O(n), but amortized over many operations

**Choosing initial capacity:**
```java
// Creating HashMap with specific initial capacity and load factor
HashMap<String, Integer> map = new HashMap<>(32, 0.8f);
```

[Back to Top](#table-of-contents)

## How to synchronize HashMap in Java?

Java's HashMap is not thread-safe, meaning that if multiple threads access and modify it concurrently without synchronization, it may result in unpredictable behavior or data corruption. Here are various methods to synchronize HashMap in Java.

### Methods for HashMap Synchronization

#### 1. Using Collections.synchronizedMap()

This is the most common approach to create a synchronized wrapper around an existing HashMap.

```java
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SynchronizedHashMapExample {
    public static void main(String[] args) {
        // Create a HashMap
        HashMap<String, Integer> hashMap = new HashMap<>();
        
        // Create a synchronized Map
        Map<String, Integer> synchronizedMap = Collections.synchronizedMap(hashMap);
        
        // Now synchronizedMap is thread-safe
        // All access to synchronizedMap must be done through this wrapper
        
        // Thread-safe operations
        synchronizedMap.put("key1", 1);
        synchronizedMap.get("key1");
        
        // NOTE: Iterator access requires additional synchronization
        synchronized (synchronizedMap) {
            for (Map.Entry<String, Integer> entry : synchronizedMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
                // Do not modify the map during iteration
            }
        }
    }
}
```

**Key Points:**
- All individual operations are synchronized
- **Important:** Iteration requires manual synchronization using the synchronized map as the lock object

#### 2. Using ConcurrentHashMap

ConcurrentHashMap provides better concurrency than synchronized HashMap. It allows multiple threads to read and write concurrently without locking the entire map.

```java
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapExample {
    public static void main(String[] args) {
        // Create a ConcurrentHashMap
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        
        // Thread-safe operations
        concurrentMap.put("key1", 1);
        concurrentMap.get("key1");
        
        // No explicit synchronization needed for iteration
        for (String key : concurrentMap.keySet()) {
            System.out.println(key + ": " + concurrentMap.get(key));
        }
        
        // Atomic operations
        concurrentMap.putIfAbsent("key2", 2);  // Atomic operation
        
        // Replace if matches the expected value
        concurrentMap.replace("key1", 1, 100);  // Replaces only if current value is 1
        
        System.out.println("After replace: " + concurrentMap.get("key1"));
    }
}
```

**Sample output:**
```
key1: 1
After replace: 100
```

#### 3. Using Synchronized Block

You can use explicit synchronization with an object lock.

```java
import java.util.HashMap;

public class SynchronizedBlockExample {
    private static final HashMap<String, Integer> map = new HashMap<>();
    private static final Object lock = new Object();
    
    public static void main(String[] args) {
        // Thread-safe access using synchronized block
        synchronized (lock) {
            map.put("key1", 1);
        }
        
        // Thread-safe retrieval
        synchronized (lock) {
            Integer value = map.get("key1");
            System.out.println("Value: " + value);
        }
        
        // Multiple operations in a single synchronized block
        synchronized (lock) {
            // All operations here are atomic as a group
            if (!map.containsKey("key2")) {
                map.put("key2", 2);
            }
            map.put("key3", 3);
        }
    }
}
```

### Comparison of Synchronization Methods

| Method | Pros | Cons | Use Case |
|--------|------|------|----------|
| **Collections.synchronizedMap()** | - Simple to implement<br>- Preserves original HashMap<br>- Full thread safety | - Performance bottleneck<br>- One lock for all operations<br>- Manual sync for iteration | - When you need basic thread safety<br>- When backward compatibility is important |
| **ConcurrentHashMap** | - Better concurrency<br>- No need to synchronize iterator<br>- Atomic operations<br>- Better performance | - Java 1.5+ only<br>- Not synchronized with external locks | - High concurrency applications<br>- Frequent reads/writes<br>- Modern applications |
| **Synchronized Block** | - Fine-grained control<br>- Can synchronize multiple operations | - Error-prone<br>- Requires careful implementation<br>- Risk of deadlocks | - Custom synchronization logic<br>- When synchronizing with other objects |

### Performance Considerations

1. **ConcurrentHashMap** is generally the best choice for multi-threaded applications
2. **synchronizedMap** has higher overhead due to lock contention
3. Remember that synchronization comes at a performance cost

```java
// Performance test example
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MapPerformanceTest {
    private static final int THREAD_COUNT = 10;
    private static final int OPERATIONS_PER_THREAD = 100000;
    
    public static void main(String[] args) throws InterruptedException {
        // Test with synchronized HashMap
        Map<Integer, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());
        long syncTime = testMap(syncMap, "SynchronizedMap");
        
        // Test with ConcurrentHashMap
        Map<Integer, Integer> concMap = new ConcurrentHashMap<>();
        long concTime = testMap(concMap, "ConcurrentHashMap");
        
        System.out.println("Performance ratio: " + (double)syncTime / concTime);
    }
    
    private static long testMap(final Map<Integer, Integer> map, String mapType) throws InterruptedException {
        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadNum = i;
            executor.submit(() -> {
                for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                    int key = threadNum * OPERATIONS_PER_THREAD + j;
                    map.put(key, key);
                    map.get(key);
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        long end = System.currentTimeMillis();
        System.out.println(mapType + " took " + (end - start) + " ms");
        return end - start;
    }
}
```

**Sample output:**
```
SynchronizedMap took 1243 ms
ConcurrentHashMap took 398 ms
Performance ratio: 3.1231
```

[Back to Top](#table-of-contents)
