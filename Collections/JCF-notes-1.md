## How LinkedHashMap Maintains Insertion Order

#### What Happens on `put(k, v)` in `LinkedHashMap`
1. **Hashing**: Hash of the key is computed → used to find the bucket in the array.
2. **Bucket Insertion**: The node is inserted into the correct index of the internal `table[]` array, like a regular `HashMap`.
3. **Doubly Linked List Update**: A **reference** to that same node is maintained in a doubly linked list to preserve order (insertion or access).
    - `table[]` handles **hash lookups**
    - The **linked list** handles **iteration order**


#### Key Points

* `LinkedHashMap` = `HashMap` + **doubly linked list**.
* Preserves **insertion order** by default.
* Can preserve **access order** if constructed with `accessOrder = true`.
* Extra memory overhead due to `before`/`after` pointers.
* Ideal for **LRU cache** implementations.

<br/>

Great question! Here's the **difference between `HashMap` and `IdentityHashMap` in Java** in a clear way:

| Aspect                                                         | `HashMap`                                         | `IdentityHashMap`                                                                              |
| -------------------------------------------------------------- | ------------------------------------------------- | ---------------------------------------------------------------------------------------------- |
| **Key comparison**                                             | Uses `.equals()` method to compare keys           | Uses `==` operator (reference equality)                                                        |
| **Hashing method**                                             | Uses `.hashCode()` of the key                     | Uses `System.identityHashCode()` (based on memory address)                                     |
| **Allows null keys**                                           | Yes, allows one null key                          | Yes, allows one null key                                                                       |
| **Ordering**                                                   | No guaranteed order                               | No guaranteed order                                                                            |
| **Typical use case**                                           | General purpose map                               | When you want keys compared by reference, e.g., caches or internal maps where identity matters |
| **Collision handling**                                         | Uses chaining/buckets based on hashCode           | Similar, but based on identity hashCode                                                        |
| **Behavior when keys have same content but different objects** | Treated as same key (because equals returns true) | Treated as different keys (because references differ)                                          |
| **Performance**                                                | Standard performance for hashing                  | Slightly faster for identity checks, but less common                                           |

---

### Simple example

```java
String a = new String("hello");
String b = new String("hello");

HashMap<String, Integer> hashMap = new HashMap<>();
hashMap.put(a, 1);
hashMap.put(b, 2);

System.out.println(hashMap.size());  // Output: 1 because a.equals(b) == true

IdentityHashMap<String, Integer> identityMap = new IdentityHashMap<>();
identityMap.put(a, 1);
identityMap.put(b, 2);

System.out.println(identityMap.size()); // Output: 2 because a != b (different references)
```

---

### Summary

* Use **`HashMap`** for normal cases where logical equality matters.
* Use **`IdentityHashMap`** when you want keys to be distinguished by *object identity*, not by content equality.




### 1. **HashMap (Strong References)**

* **What it is:** The standard implementation of a map in Java.
* **Reference Type:** Holds **strong references** to its keys and values.
* **Behavior:** As long as a key (and value) is referenced by the map, it will **not be garbage collected**.
* **Use case:** When you want the map to keep entries until you explicitly remove them.

---

### 2. **WeakHashMap (Weak References on Keys)**

* **What it is:** A special map implementation that holds **weak references** to its **keys**.
* **Reference Type:** Keys are referenced **weakly**; values are referenced strongly.
* **Behavior:**

  * If a key is **no longer referenced elsewhere** (outside the map), it becomes eligible for garbage collection.
  * When the garbage collector collects a key, the corresponding entry is **automatically removed** from the map.
* **Use case:** Useful for caches or metadata where entries should disappear when keys are no longer in use elsewhere.

---

### Summary Table

| Feature                           | HashMap (Strong)                      | WeakHashMap                                              |
| --------------------------------- | ------------------------------------- | -------------------------------------------------------- |
| **Key Reference Type**            | Strong                                | Weak                                                     |
| **Value Reference Type**          | Strong                                | Strong                                                   |
| **Garbage Collection of Entries** | Entries stay until explicitly removed | Entries removed automatically when keys are GC'ed        |
| **Typical Use Case**              | General-purpose map                   | Caches, listeners, or mappings tied to lifecycle of keys |
| **Thread Safety**                 | Not synchronized (like HashMap)       | Not synchronized                                         |

---

### Simple example illustrating difference:

```java
import java.util.HashMap;
import java.util.WeakHashMap;

public class WeakHashMapDemo {
    public static void main(String[] args) throws InterruptedException {
        Map<Object, String> hashMap = new HashMap<>();
        Map<Object, String> weakHashMap = new WeakHashMap<>();

        Object key1 = new Object();
        Object key2 = new Object();

        hashMap.put(key1, "HashMap Value");
        weakHashMap.put(key2, "WeakHashMap Value");

        System.out.println("Before GC:");
        System.out.println("HashMap contains key1? " + hashMap.containsKey(key1));       // true
        System.out.println("WeakHashMap contains key2? " + weakHashMap.containsKey(key2)); // true

        // Remove strong reference to key2
        key2 = null;

        // Suggest GC to run
        System.gc();
        Thread.sleep(1000);

        System.out.println("After GC:");
        System.out.println("HashMap contains key1? " + hashMap.containsKey(key1));       // true
        System.out.println("WeakHashMap contains key2? " + weakHashMap.containsKey(key2)); // false (key2 is GC'ed)
    }
}
```

**Output:**

```
Before GC:
HashMap contains key1? true
WeakHashMap contains key2? true

After GC:
HashMap contains key1? true
WeakHashMap contains key2? false
```

---

### Key takeaways:

* `HashMap` prevents keys from being GC'ed because it holds strong references.
* `WeakHashMap` lets keys be GC'ed when no other strong references exist, automatically cleaning up the entries.

