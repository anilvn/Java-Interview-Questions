
##  `sleep()` vs `wait()`

| Feature                          | `sleep()`                           | `wait()`                                                    |
| -------------------------------- | ----------------------------------- | ----------------------------------------------------------- |
| **Defined in**                   | `Thread` class                      | `Object` class                                              |
| **Purpose**                      | Pause current thread for a duration | Wait for a condition to be met (usually in synchronization) |
| **Used for**                     | Delays, timeouts                    | Inter-thread communication/synchronization                  |
| **Requires synchronized block?** | ❌ No                                | ✅ Yes                                                       |
| **Releases lock?**               | ❌ No — retains any locks held       | ✅ Yes — releases the monitor lock                           |
| **How it resumes**               | Automatically after time expires    | Must be `notified()` by another thread                      |
| **Throws Exception**             | `InterruptedException`              | `InterruptedException`                                      |
| **Thread state**                 | `TIMED_WAITING`                     | `WAITING` or `TIMED_WAITING` (if `wait(timeout)`)           |

---

##  `sleep()` – Pauses current thread

### 🔹 Example:

```java
System.out.println("Sleeping...");
Thread.sleep(2000); // sleeps for 2 seconds
System.out.println("Awake now.");
```

### 🔹 Key Points:

* Doesn’t release any monitor/lock it holds.
* Useful for retry mechanisms, polling, or artificial delay.

---

## `wait()` – Wait for a condition (used with `notify()`/`notifyAll()`)

### 🔹 Example:

```java
synchronized (lock) {
    System.out.println("Waiting...");
    lock.wait(); // releases lock and waits
    System.out.println("Notified and resumed.");
}
```

In another thread:

```java
synchronized (lock) {
    lock.notify(); // wakes one thread waiting on the lock
}
```

### 🔹 Key Points:

* Releases the lock.
* Must be used inside a `synchronized` block.
* Used when one thread needs to **pause until another thread signals it**.

---

##  Analogy:

| Situation | Description                                                                     |
| --------- | ------------------------------------------------------------------------------- |
| `sleep()` | You set an alarm for 5 minutes and take a nap. No one can wake you earlier.     |
| `wait()`  | You wait for someone to tap your shoulder and say "go ahead". You’re listening. |

---

##  When to Use What

| Scenario                                                           | Use                                 |
| ------------------------------------------------------------------ | ----------------------------------- |
| You want a thread to pause for a fixed time                        | `sleep()`                           |
| You want a thread to pause until a condition or signal is received | `wait()` + `notify()`/`notifyAll()` |



<br/><br/><br/><br/>
Absolutely! A **Producer-Consumer** example is one of the best ways to understand how `wait()` and `notify()` work in **real multithreaded coordination**.

---

## 🧵 Producer-Consumer Problem using `wait()` and `notify()`

### 👇 Scenario:

* A **Producer** thread puts data into a shared buffer.
* A **Consumer** thread takes data from the buffer.
* If the buffer is **full**, the producer must wait.
* If the buffer is **empty**, the consumer must wait.

We’ll simulate this with a **bounded buffer (queue of size 1)** for simplicity.

---

### ✅ Complete Java Example:

```java
import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerExample {

    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer(1);

        Thread producer = new Thread(() -> {
            int value = 1;
            while (true) {
                buffer.produce(value++);
                sleep(1000); // delay for demonstration
            }
        });

        Thread consumer = new Thread(() -> {
            while (true) {
                buffer.consume();
                sleep(1500);
            }
        });

        producer.start();
        consumer.start();
    }

    // Helper method to sleep safely
    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Shared buffer class
    static class SharedBuffer {
        private final Queue<Integer> queue = new LinkedList<>();
        private final int capacity;

        public SharedBuffer(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void produce(int value) {
            while (queue.size() == capacity) {
                try {
                    System.out.println("Buffer full, producer waiting...");
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            queue.offer(value);
            System.out.println("Produced: " + value);
            notify(); // Wake up consumer
        }

        public synchronized void consume() {
            while (queue.isEmpty()) {
                try {
                    System.out.println("Buffer empty, consumer waiting...");
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            int value = queue.poll();
            System.out.println("Consumed: " + value);
            notify(); // Wake up producer
        }
    }
}
```

---

### 🔍 Output Sample

```plaintext
Produced: 1
Consumed: 1
Produced: 2
Consumed: 2
Produced: 3
...
```

---

### 🔄 What’s Happening:

* `wait()` is called when:

  * Producer sees buffer full.
  * Consumer sees buffer empty.
* `notify()` is called after:

  * Producer adds an item.
  * Consumer removes an item.
* `synchronized` ensures only one thread accesses the buffer at a time.
* `wait()` releases the lock and pauses the thread.
* `notify()` wakes a waiting thread (doesn’t release the lock immediately!).

---

### ✅ Key Concepts in Action

| Concept        | Where it's used                      |
| -------------- | ------------------------------------ |
| `wait()`       | Inside `produce()` and `consume()`   |
| `notify()`     | After producing or consuming an item |
| `synchronized` | On all buffer methods                |

---

Let me know if you'd like to extend this to a **`notifyAll()`**, **blocking queue**, or **semaphore-based** version!
