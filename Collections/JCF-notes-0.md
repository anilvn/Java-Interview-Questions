# Java Collections - Simplified Notes

## 1. LIST INTERFACE

### ArrayList
**Class Signature:** `class ArrayList<E> implements List<E>`

**Basic Info:**
- Resizable array implementation
- Maintains insertion order
- Allows duplicates and null values

**Why Used:**
- Fast random access by index
- Dynamic sizing
- Good for frequent read operations

**Internal Working:**
- Uses Object[] array internally
- Default capacity: 10
- Grows by 50% when full (newSize = oldSize + oldSize/2)

**Performance:**
- Access: O(1)
- Search: O(n)
- Insert/Delete at end: O(1) amortized
- Insert/Delete at middle: O(n)

**Key Methods:**
- `add(element)`, `add(index, element)`
- `get(index)`, `set(index, element)`
- `remove(index)`, `remove(object)`
- `size()`, `isEmpty()`

**Similar Structures:**
- Vector (synchronized version)
- LinkedList (different performance characteristics)

---

### LinkedList
**Class Signature:** `class LinkedList<E> implements List<E>, Deque<E>`

**Basic Info:**
- Doubly-linked list implementation
- Maintains insertion order
- Allows duplicates and null values

**Why Used:**
- Efficient insertion/deletion at any position
- Implements both List and Deque
- Good for frequent modifications

**Internal Working:**
- Uses Node objects with prev/next pointers
- No capacity limit (only memory)

**Performance:**
- Access: O(n)
- Search: O(n)
- Insert/Delete at ends: O(1)
- Insert/Delete at middle: O(1) if node reference available

**Key Methods:**
- `addFirst()`, `addLast()`
- `removeFirst()`, `removeLast()`
- `getFirst()`, `getLast()`
- Standard List methods

**Similar Structures:**
- ArrayList (array-based alternative)
- ArrayDeque (array-based deque)

---

### Vector
**Class Signature:** `class Vector<E> implements List<E>`

**Basic Info:**
- Legacy synchronized resizable array
- Thread-safe version of ArrayList
- Maintains insertion order

**Why Used:**
- Thread safety required
- Legacy code compatibility

**Performance:**
- Same as ArrayList but slower due to synchronization
- Growth: doubles in size (100% increase)

**Key Methods:**
- Same as ArrayList
- Additional: `elements()` returns Enumeration

---

## 2. SET INTERFACE

### HashSet
**Class Signature:** `class HashSet<E> implements Set<E>`

**Basic Info:**
- Hash table implementation
- No duplicates allowed
- One null value allowed
- No ordering guarantee

**Why Used:**
- Fast lookup, insertion, deletion
- Eliminate duplicates
- Membership testing

**Internal Working:**
- Uses HashMap internally
- Default capacity: 16, load factor: 0.75
- Uses hashCode() and equals() for uniqueness

**Performance:**
- Add/Remove/Contains: O(1) average, O(n) worst case

**Key Methods:**
- `add(element)`, `remove(element)`
- `contains(element)`
- `size()`, `isEmpty()`

**Similar Structures:**
- LinkedHashSet (maintains insertion order)
- TreeSet (sorted order)

---

### LinkedHashSet
**Class Signature:** `class LinkedHashSet<E> extends HashSet<E>`

**Basic Info:**
- Hash table + linked list implementation
- Maintains insertion order
- No duplicates allowed

**Why Used:**
- Need HashSet performance with predictable iteration order
- Ordered unique elements

**Performance:**
- Slightly slower than HashSet due to linked list overhead
- Same time complexity: O(1) for basic operations

---

### TreeSet
**Class Signature:** `class TreeSet<E> implements NavigableSet<E>`

**Basic Info:**
- Red-Black tree (self-balancing BST)
- Elements stored in sorted order
- No duplicates allowed
- No null values

**Why Used:**
- Need sorted unique elements
- Range operations
- Navigation methods

**Internal Working:**
- Uses TreeMap internally
- Elements must implement Comparable or provide Comparator

**Performance:**
- Add/Remove/Contains: O(log n)

**Key Methods:**
- `first()`, `last()`
- `lower()`, `higher()`
- `subSet()`, `headSet()`, `tailSet()`

---

## 3. MAP INTERFACE

### HashMap
**Class Signature:** `class HashMap<K,V> implements Map<K,V>`

**Basic Info:**
- Hash table implementation
- Key-value pairs
- One null key, multiple null values allowed
- No ordering guarantee

**Why Used:**
- Fast key-based access
- Most commonly used Map
- Good general-purpose map

**Internal Working:**
- Array of buckets (Node/Entry objects)
- Uses hashCode() for bucket selection
- Handles collisions with chaining (linked list/tree)
- Converts to tree when bucket size > 8

**Performance:**
- Get/Put/Remove: O(1) average, O(n) worst case

**Key Methods:**
- `put(key, value)`, `get(key)`
- `remove(key)`, `containsKey(key)`
- `keySet()`, `values()`, `entrySet()`

**Similar Structures:**
- LinkedHashMap (maintains order)
- TreeMap (sorted keys)
- Hashtable (synchronized)

---

### LinkedHashMap
**Class Signature:** `class LinkedHashMap<K,V> extends HashMap<K,V>`

**Basic Info:**
- HashMap + doubly-linked list
- Maintains insertion or access order
- Slightly more memory overhead

**Why Used:**
- Predictable iteration order
- LRU cache implementation
- Ordered key-value pairs

**Performance:**
- Same as HashMap with slight overhead

---

### TreeMap
**Class Signature:** `class TreeMap<K,V> implements NavigableMap<K,V>`

**Basic Info:**
- Red-Black tree implementation
- Keys stored in sorted order
- No null keys allowed

**Why Used:**
- Sorted key-value pairs
- Range operations on keys
- Navigation methods

**Performance:**
- Get/Put/Remove: O(log n)

**Key Methods:**
- `firstKey()`, `lastKey()`
- `lowerKey()`, `higherKey()`
- `subMap()`, `headMap()`, `tailMap()`

---

### Hashtable
**Class Signature:** `class Hashtable<K,V> implements Map<K,V>`

**Basic Info:**
- Legacy synchronized hash table
- No null keys or values allowed
- Thread-safe

**Why Used:**
- Legacy code
- Thread safety required
- Simple synchronized map

**Performance:**
- Slower than HashMap due to synchronization

---

## 4. QUEUE INTERFACE

### ArrayDeque
**Class Signature:** `class ArrayDeque<E> implements Deque<E>`

**Basic Info:**
- Resizable array implementation of Deque
- Can be used as stack or queue
- No capacity restrictions

**Why Used:**
- Faster than LinkedList for queue operations
- Efficient double-ended queue
- Stack alternative to Stack class

**Performance:**
- Add/Remove at ends: O(1) amortized
- Random access: Not supported

**Key Methods:**
- `addFirst()`, `addLast()`
- `removeFirst()`, `removeLast()`
- `push()`, `pop()` (stack operations)

---

### PriorityQueue
**Class Signature:** `class PriorityQueue<E> implements Queue<E>`

**Basic Info:**
- Heap-based priority queue
- Elements ordered by priority
- Not thread-safe

**Why Used:**
- Process elements by priority
- Heap operations
- Scheduling algorithms

**Internal Working:**
- Uses binary heap (array-based)
- Elements must implement Comparable or provide Comparator

**Performance:**
- Add/Remove: O(log n)
- Peek: O(1)

**Key Methods:**
- `offer(element)`, `poll()`
- `peek()`, `remove()`

---

## 5. CONVERSION TO SYNCHRONIZED COLLECTIONS

### Collections.synchronizedXxx() Methods

**Purpose:** Convert non-thread-safe collections to thread-safe versions

**Available Methods:**
- `Collections.synchronizedList(list)`
- `Collections.synchronizedSet(set)`
- `Collections.synchronizedMap(map)`
- `Collections.synchronizedCollection(collection)`

**Important Notes:**
- Returns synchronized wrapper
- Manual synchronization needed for iteration
- Performance overhead due to synchronization

**Example Usage:**
```java
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());
Map<String, String> syncMap = Collections.synchronizedMap(new HashMap<>());

// Safe iteration requires manual synchronization
synchronized(syncList) {
    for(String item : syncList) {
        // process item
    }
}
```

**Key Points:**
- All methods are synchronized
- Better to use concurrent collections for high concurrency
- Legacy approach for thread safety

---

## 6. CONCURRENT COLLECTIONS

### ConcurrentHashMap
**Class Signature:** `class ConcurrentHashMap<K,V> implements ConcurrentMap<K,V>`

**Basic Info:**
- Thread-safe HashMap alternative
- Better performance than synchronized HashMap
- Allows concurrent reads and limited concurrent writes

**Why Used:**
- High-performance concurrent access
- Better scalability than Hashtable
- Modern thread-safe map

**Internal Working:**
- Segmentation (Java 7) / CAS operations (Java 8+)
- Lock-free reads
- Fine-grained locking for writes

**Performance:**
- Better than synchronized HashMap
- Scales well with concurrent access

**Key Methods:**
- Standard Map methods
- `putIfAbsent()`, `replace()`, `remove(key, value)`
- Atomic operations

---

### CopyOnWriteArrayList
**Class Signature:** `class CopyOnWriteArrayList<E> implements List<E>`

**Basic Info:**
- Thread-safe ArrayList alternative
- Copy-on-write semantics
- Optimized for read-heavy scenarios

**Why Used:**
- Frequent reads, infrequent writes
- Thread-safe iteration without synchronization
- Event listener lists

**Internal Working:**
- Creates new array copy for each modification
- Reads are lock-free
- Writes are expensive

**Performance:**
- Reads: O(1)
- Writes: O(n) due to copying

---

### BlockingQueue Implementations

#### ArrayBlockingQueue
- Fixed-size blocking queue
- Array-based implementation
- Useful for producer-consumer scenarios

#### LinkedBlockingQueue
- Optionally bounded blocking queue
- Linked node implementation
- Good for variable-size scenarios

**Common Methods:**
- `put()` - blocking insert
- `take()` - blocking remove
- `offer()` - non-blocking insert with timeout
- `poll()` - non-blocking remove with timeout

---

## QUICK COMPARISON SUMMARY

| Collection | Thread-Safe | Ordering | Duplicates | Null | Performance |
|------------|-------------|----------|------------|------|-------------|
| ArrayList | No | Insertion | Yes | Yes | Fast access |
| LinkedList | No | Insertion | Yes | Yes | Fast insert/delete |
| HashSet | No | None | No | One | Fast operations |
| TreeSet | No | Sorted | No | No | Log n operations |
| HashMap | No | None | Values only | Key+Values | Fast operations |
| TreeMap | No | Sorted keys | Values only | Values only | Log n operations |
| ConcurrentHashMap | Yes | None | Values only | No | Fast concurrent |
| CopyOnWriteArrayList | Yes | Insertion | Yes | Yes | Read-optimized |

## WHEN TO USE WHAT

- **ArrayList**: Default choice for lists, random access needed
- **LinkedList**: Frequent insertions/deletions in middle
- **HashSet**: Unique elements, fast lookup
- **TreeSet**: Unique sorted elements
- **HashMap**: Default choice for key-value pairs
- **TreeMap**: Sorted key-value pairs
- **ArrayDeque**: Queue/Stack operations
- **PriorityQueue**: Priority-based processing
- **ConcurrentHashMap**: Thread-safe map with good performance
- **CopyOnWriteArrayList**: Thread-safe list for read-heavy scenarios