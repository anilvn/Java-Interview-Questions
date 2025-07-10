# Table of Contents for Java Multithreading Notes

Based on the provided document, here is a detailed table of contents for the Java multithreading content:

## 1. Main Thread Execution
- Program demonstrating main thread execution with static and non-static variables and methods
- Execution order of static and non-static initializations

## 2. User-Defined Threads
- Steps to create user-defined threads in Java
- Ways to define user-defined threads
  - Extending java.lang.Thread class
  - Implementing java.lang.Runnable interface
- Example programs for user-defined threads
- Thread naming and identification

## 3. Thread Chaining
- Program demonstrating thread chaining concept
- Method calling within thread execution context

## 4. Multiple Thread Execution
- Creating and executing multiple user-defined threads
- Thread execution using Thread class
- Thread execution using Runnable interface
- Thread scheduling and execution order

## 5. Thread Synchronization
- Concept of synchronization in Java
- Program demonstrating synchronization with Institute class example
- Synchronized methods vs synchronized blocks
- Thread access control for shared resources
- Effect of synchronization on thread execution

## 6. Thread Lifecycle
- Different states in thread lifecycle
- Program demonstrating thread lifecycle
- IllegalThreadStateException when starting a thread twice

## 7. Thread Control Methods
- Thread.sleep() method
  - Purpose and usage
  - Example programs with sleep()
- join() method
  - Purpose and usage
  - Example programs with join()
  - Creating deadlock scenarios using join()
- wait(), notify(), and notifyAll() methods
  - Inter-thread communication
  - Example program with banking transaction

## 8. Thread Priority
- Daemon vs Non-Daemon threads
- Low Priority Threads (LPT) vs High Priority Threads (HPT)
- isDaemon() method usage
- setDaemon() method for changing thread priority
- Relationship between thread types and lifecycle

## 9. Additional Thread Control Methods
- yield() method
  - Purpose and usage
  - Example with producer-consumer scenario
- interrupt() method
  - Purpose and usage
  - Handling thread interruption

## 10. Thread Groups
- Creating and managing thread groups
- Parent and child thread groups
- Thread group properties and methods
- Setting priority for thread groups
- Monitoring active threads in a group

## 11. Modern Thread Creation
- Using lambda expressions with Runnable interface
- Simplified thread creation in modern Java

Each section contains explanatory text and example code demonstrating the concepts, with annotations about the expected output and behavior of the programs.

---
# Java Multithreading: Comprehensive Notes

## 1. Main Thread Execution

The `Thread.currentThread()` method returns a reference to the currently executing thread. When Java application starts, the main thread is automatically created and begins execution.

```java
// Execution order for class with static and non-static elements
// Order: Static variables/blocks → Main method → Non-static variables/blocks → Constructor
class Test {
    static int a = m1();            // 1. Static variable initialization
    static { /* Static block */}    // 2. Static block execution
    int b = m2();                  // 4. Non-static variable initialization
    { /* Non-static block */ }      // 5. Non-static block execution
    Test() { /* Constructor */ }    // 6. Constructor execution
    
    public static void main() {     // 3. Main method execution
        Test t = new Test();        // Creates instance, triggering 4, 5, 6
    }
}
```

## 2. User-Defined Threads

There are two ways to create custom threads in Java:

### Extending Thread class
```java
class MyThread extends Thread {
    @Override
    public void run() {
        // Thread code goes here
    }
}

// Usage
MyThread thread = new MyThread();
thread.start(); // Not thread.run() - this would execute in the current thread
```

### Implementing Runnable interface
```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        // Thread code goes here
    }
}

// Usage
Thread thread = new Thread(new MyRunnable());
thread.start();
```

Modern Java allows for lambda expressions with Runnable:
```java
Thread thread = new Thread(() -> {
    // Thread code goes here
});
thread.start();
```

## 3. Thread Control Methods

### sleep()
Pauses thread execution for specified milliseconds:
```java
try {
    Thread.sleep(1000); // Sleep for 1 second
} catch (InterruptedException e) {
    e.printStackTrace();
}
```

### join()
Waits for another thread to complete before continuing:
```java
Thread thread = new Thread(() -> {
    // Some operations
});
thread.start();

try {
    thread.join(); // Current thread waits for thread to finish
} catch (InterruptedException e) {
    e.printStackTrace();
}
```

### yield()
Suggests that the current thread is willing to yield its execution:
```java
Thread.yield(); // Hint to scheduler that other threads can run
```

### interrupt()
Interrupts a thread, typically used to stop long-running or blocked threads:
```java
Thread thread = new Thread(() -> {
    try {
        Thread.sleep(10000);
    } catch (InterruptedException e) {
        // Handle interruption
    }
});
thread.start();
thread.interrupt(); // Interrupt the thread
```

## 4. Thread Synchronization

Synchronization controls access to shared resources, allowing only one thread to access at a time.

### Synchronized Methods
```java
public class Counter {
    private int count = 0;
    
    synchronized public void increment() {
        count++;
    }
    
    synchronized public int getCount() {
        return count;
    }
}
```

### Synchronized Blocks
```java
public void increment() {
    synchronized(this) {
        count++;
    }
}
```

## 5. Inter-Thread Communication

The `wait()`, `notify()` and `notifyAll()` methods enable communication between threads:

```java
class SharedResource {
    private boolean dataProduced = false;
    private int data;
    
    synchronized public void produce(int value) {
        while (dataProduced) {
            try {
                wait(); // Wait until consumer consumes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        data = value;
        dataProduced = true;
        notify(); // Notify consumer
    }
    
    synchronized public int consume() {
        while (!dataProduced) {
            try {
                wait(); // Wait until producer produces
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        dataProduced = false;
        notify(); // Notify producer
        return data;
    }
}
```

## 6. Thread Priority

Threads can be classified as daemon (low priority) or non-daemon (high priority):

```java
Thread thread = new Thread(() -> {
    // Code
});
thread.setDaemon(true); // Set as daemon thread
thread.start();

boolean isDaemon = thread.isDaemon(); // Check if thread is daemon
```

- Daemon threads automatically terminate when all non-daemon threads finish
- JVM will not wait for daemon threads to complete before exiting

## 7. Thread Groups

Thread groups organize and manage related threads:

```java
ThreadGroup parentGroup = new ThreadGroup("Parent");
ThreadGroup childGroup = new ThreadGroup(parentGroup, "Child");

Thread thread1 = new Thread(parentGroup, () -> {
    // Thread code
}, "Thread1");

parentGroup.setMaxPriority(7); // Set max priority for all threads in group
int activeCount = parentGroup.activeCount(); // Get number of active threads
```

## 8. Deadlock Prevention

Deadlocks occur when two or more threads are blocked forever, waiting for each other:

```java
// Potential deadlock scenario
Thread t1 = new Thread(() -> {
    synchronized(resourceA) {
        // Do something
        synchronized(resourceB) {
            // Do something
        }
    }
});

Thread t2 = new Thread(() -> {
    synchronized(resourceB) {
        // Do something
        synchronized(resourceA) {
            // Do something
        }
    }
});
```

Prevention techniques:
1. Lock ordering: Always acquire locks in the same order
2. Lock timeouts: Use `tryLock()` with timeout
3. Deadlock detection: Use tools to detect potential deadlocks

## Best Practices

1. Prefer implementing `Runnable` over extending `Thread`
2. Keep synchronization blocks as small as possible
3. Avoid calling thread control methods in synchronized blocks
4. Use thread pools instead of creating threads manually
5. Use high-level concurrency utilities from `java.util.concurrent` package
6. Handle InterruptedException properly
7. Be careful with thread priority - it's platform dependent
8. Avoid thread.stop(), thread.suspend(), thread.resume() - they are deprecated

## Common Issues

1. Race conditions: Multiple threads accessing shared data concurrently
2. Deadlocks: Two or more threads waiting for each other indefinitely
3. Starvation: Thread never gets access to resources
4. Livelock: Threads respond to each other but make no progress
5. Over-synchronization: Excessive locking reduces concurrency

Remember that thread scheduling is non-deterministic and depends on JVM implementation and operating system.