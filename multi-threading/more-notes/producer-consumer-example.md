Great, Anil! You're using the **classic `wait()`/`notify()` approach**. Let's convert this to a **Java 8+ lambda-style implementation** by:

* **Replacing the `Producer` and `Consumer` classes** with lambda expressions
* Keeping the existing `SharedQueue` as-is (since `wait()`/`notify()` logic is inside it)

---

## ✅ ✅ Producer-Consumer (With Lambda + wait/notify `SharedQueue`)

```java
public class LambdaProducerConsumer {
    public static void main(String[] args) {
        SharedQueue queue = new SharedQueue();

        // ✅ Producer using lambda
        Runnable producer = () -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    queue.produce(i);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // ✅ Consumer using lambda
        Runnable consumer = () -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    queue.consume();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // ✅ Start threads
        new Thread(producer, "Producer-Thread").start();
        new Thread(consumer, "Consumer-Thread").start();
    }
}
```

---

### ✅ Reuse Your `SharedQueue` As-Is:

No change required in your original `SharedQueue` class:

```java
class SharedQueue {
    private int data;
    private boolean available = false;

    public synchronized void produce(int value) throws InterruptedException {
        while (available) {
            wait(); // wait until consumer consumes
        }
        data = value;
        System.out.println("Produced: " + data);
        available = true;
        notify(); // notify consumer
    }

    public synchronized int consume() throws InterruptedException {
        while (!available) {
            wait(); // wait until producer produces
        }
        System.out.println("Consumed: " + data);
        available = false;
        notify(); // notify producer
        return data;
    }
}
```

---

### ✅ Output:

```
Produced: 1
Consumed: 1
Produced: 2
Consumed: 2
...
```

---

### ✅ Benefits of Lambda Approach:

| Feature          | Benefit                                  |
| ---------------- | ---------------------------------------- |
| Lambdas used     | Cleaner and more concise code            |
| Thread naming    | Easier to debug and follow               |
| Less boilerplate | No need for separate `Thread` subclasses |

---

Let me know if you want to switch this example to a `BlockingQueue`-based version with lambdas too!

<br/><br/>
<br/>

## ✅ 1. Proper Producer-Consumer using `wait()` / `notify()`

```java
class SharedQueue {
    private int data;
    private boolean available = false;

    public synchronized void produce(int value) throws InterruptedException {
        while (available) {
            wait(); // wait until consumer consumes
        }
        data = value;
        System.out.println("Produced: " + data);
        available = true;
        notify(); // notify consumer
    }

    public synchronized int consume() throws InterruptedException {
        while (!available) {
            wait(); // wait until producer produces
        }
        System.out.println("Consumed: " + data);
        available = false;
        notify(); // notify producer
        return data;
    }
}

class Producer extends Thread {
    private SharedQueue queue;

    Producer(SharedQueue queue) {
        this.queue = queue;
    }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            try {
                queue.produce(i);
                Thread.sleep(500); // simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Consumer extends Thread {
    private SharedQueue queue;

    Consumer(SharedQueue queue) {
        this.queue = queue;
    }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            try {
                queue.consume();
                Thread.sleep(500); // simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        SharedQueue queue = new SharedQueue();
        new Producer(queue).start();
        new Consumer(queue).start();
    }
}
```

---

### ✅ Output (Orderly interaction):

```
Produced: 1
Consumed: 1
Produced: 2
Consumed: 2
...
```

💡 Threads cooperate using the shared lock — one waits while the other works.

---

## ❌ 2. Simulated Approach Using Only `Thread.sleep()` (No `wait/notify`) — for Contrast

```java
class WrongQueue {
    private int data;
    private boolean produced = false;

    public void produce(int value) {
        if (!produced) {
            data = value;
            System.out.println("Produced (Wrong): " + value);
            produced = true;
        }
    }

    public void consume() {
        if (produced) {
            System.out.println("Consumed (Wrong): " + data);
            produced = false;
        }
    }
}

class WrongProducer extends Thread {
    private WrongQueue queue;

    WrongProducer(WrongQueue queue) {
        this.queue = queue;
    }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            queue.produce(i);
            try {
                Thread.sleep(500); // no coordination!
            } catch (InterruptedException e) {}
        }
    }
}

class WrongConsumer extends Thread {
    private WrongQueue queue;

    WrongConsumer(WrongQueue queue) {
        this.queue = queue;
    }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            queue.consume();
            try {
                Thread.sleep(500); // no coordination!
            } catch (InterruptedException e) {}
        }
    }
}

public class WrongExample {
    public static void main(String[] args) {
        WrongQueue queue = new WrongQueue();
        new WrongProducer(queue).start();
        new WrongConsumer(queue).start();
    }
}
```

---

### ⚠️ Output (May lose data or consume before produce):

```
Produced (Wrong): 1
Produced (Wrong): 2
Consumed (Wrong): 2
...
```

💥 There's **no synchronization**, so data can be missed or out of order.

---

## ✅ Conclusion

| Feature         | `wait()/notify()`         | `Thread.sleep()`         |
| --------------- | ------------------------- | ------------------------ |
| Communication   | Yes (coordinated)         | No                       |
| Synchronization | Required (`synchronized`) | Not required             |
| Thread-safe     | ✅ Yes                     | ❌ No (race conditions)   |
| Correctness     | ✅ Correct                 | ❌ Can fail unpredictably |

---

Let me know if you'd like the same example using `BlockingQueue` or `ReentrantLock + Condition` (Java Concurrency API style).


<br/>
<br/><br/>


Absolutely, Anil! Let's rewrite the **Producer-Consumer** example using **lambda expressions** and Java 8 style, with **BlockingQueue** — which is the modern, clean, and recommended approach.

---

## ✅ Producer-Consumer Using Lambda + `BlockingQueue`

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);

        Runnable producer = () -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    queue.put(i);             // blocks if full
                    System.out.println("Produced: " + i);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Runnable consumer = () -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    int v = queue.take();     // blocks if empty
                    System.out.println("Consumed: " + v);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        new Thread(producer, "Producer").start();
        new Thread(consumer, "Consumer").start();
    }
}
```

---

### 🧾 Output (Sample)

```
Produced: 1
Consumed: 1
Produced: 2
Consumed: 2
...
```

---

## ✅ Highlights:

| Feature           | Implementation                            |
| ----------------- | ----------------------------------------- |
| Lambdas used      | For producer & consumer `Runnable`        |
| Thread-safe queue | `BlockingQueue` (no `wait/notify` needed) |
| Thread naming     | `Thread(producer, "Producer-Thread")`     |
| No manual sync    | High-level concurrency used               |
| Clean & modern    | Java 8+ style                             |

---

This is how you'd do Producer-Consumer in **real-world apps** — safe, concise, and reliable.

Would you like a version using `ExecutorService` with lambdas instead of manually starting threads?
