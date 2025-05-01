# Java Multithreading Comprehensive Notes


A comprehensive guide to Java multithreading concepts, implementation techniques, and best practices. This document covers thread creation, lifecycle, synchronization mechanisms, and inter-thread communication.

---

## Table of Contents

| # | Topic |
|---|-------|
| | **Thread Basics** |
| 1 | [What is the main thread execution with static & non-static variables and methods?](#what-is-the-main-thread-execution-with-static--non-static-variables-and-methods) |
| 2 | [How to create user-defined threads in Java?](#how-to-create-user-defined-threads-in-java) |
| 3 | [How many ways can we define user-defined threads?](#how-many-ways-can-we-define-user-defined-threads) |
| 4 | [What is Thread Chaining?](#what-is-thread-chaining) |
| | **Thread Management** |
| 5 | [Can we define multiple user-defined threads using Thread class and Runnable interface?](#can-we-define-multiple-user-defined-threads-using-thread-class-and-runnable-interface) |
| 6 | [How to handle multiple user-defined threads execution?](#how-to-handle-multiple-user-defined-threads-execution) |
| 7 | [How to develop a deadlock scenario using join()?](#how-to-develop-a-deadlock-scenario-using-join) |
| 8 | [What is the thread lifecycle?](#what-is-the-thread-lifecycle) |
| | **Thread Methods** |
| 9 | [What is sleep() in java.lang.Thread?](#what-is-sleep-in-javalangthread) |
| 10 | [How to use join() in multithreading?](#how-to-use-join-in-multithreading) |
| 11 | [What is the yield() method and when to use it?](#what-is-the-yield-method-and-when-to-use-it) |
| 12 | [How to use interrupt() method?](#how-to-use-interrupt-method) |
| | **Thread Synchronization** |
| 13 | [What is synchronization in Java?](#what-is-synchronization-in-java) |
| 14 | [How to implement synchronized methods and blocks?](#how-to-implement-synchronized-methods-and-blocks) |
| 15 | [What is inter-thread communication?](#what-is-inter-thread-communication) |
| | **Thread Classification** |
| 16 | [What are daemon and non-daemon threads?](#what-are-daemon-and-non-daemon-threads) |
| 17 | [How to use ThreadGroup in Java?](#how-to-use-threadgroup-in-java) |
| 18 | [How to implement Runnable using Lambda expressions?](#how-to-implement-runnable-using-lambda-expressions) |

## What is the main thread execution with static & non-static variables and methods?

In Java, the execution sequence of static and non-static variables and methods follows a specific order:

- **Static members** are initialized during class loading, before any instance is created
- **Non-static members** are initialized when an object is instantiated

### Execution order:

1. Static variables and static blocks are executed in the order they appear in the class
2. Main method executes
3. When creating a new object:
   - Non-static variables and blocks are executed in the order they appear
   - Constructor is executed last

### Example:

```java
class Test {
    // Static variable initialized with m1() call
    static int a = m1();

    // Static block
    static {
        System.out.println("sb: " + Thread.currentThread());
    }

    // Static method
    static int m1() {
        System.out.println("m1: " + Thread.currentThread());
        return 111;
    }

    // Non-static variable initialized with m2() call
    int b = m2();

    // Non-static block (instance initializer)
    {
        System.out.println("nsb: " + Thread.currentThread());
    }

    // Non-static method
    int m2() {
        System.out.println("m2: " + Thread.currentThread());
        return 222;
    }

    // Constructor
    Test() {
        System.out.println("con: " + Thread.currentThread());
    }

    public static void main(String[] args) {
        System.out.println("main: " + Thread.currentThread());
        Test t = new Test(); // Create a Test object
    }
}
```

**Output:**
```
m1: Thread[main, 5, main]
sb: Thread[main, 5, main]
main: Thread[main, 5, main]
m2: Thread[main, 5, main]
nsb: Thread[main, 5, main]
con: Thread[main, 5, main]
```

[Back to Top](#table-of-contents)

## How to create user-defined threads in Java?

Creating user-defined threads in Java involves the following steps:

1. Create a class that either extends `java.lang.Thread` or implements `java.lang.Runnable`
2. Override the `run()` method to define the thread's behavior
3. Create an instance of your thread class
4. Call the `start()` method to execute the thread

### Key Points about Thread Creation:

- **Important**: Always use `start()` method to execute a thread, not `run()`
- When `start()` is called:
  1. The thread object is registered with thread scheduler algorithms
  2. Control shifts from the main thread to the user-defined thread
  3. The user-defined thread executes its `run()` method

### Example of Thread Creation:

```java
public class Test extends Thread {
    @Override
    public void run() {
        System.out.println("run: " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        System.out.println("main: " + Thread.currentThread().getName());
        Test th = new Test();
        // th.run(); // Will NOT create a new thread, just runs in main thread!
        th.setName("NIT");
        th.start(); // This correctly creates and starts a new thread
    }
}
```

### Example with Loop to See Thread Behavior:

```java
class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++)
            System.out.println("run: " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        MyThread mt = new MyThread();
        mt.setName("NIT");
        mt.start();
        for (int i = 1; i <= 10; i++) {
            System.out.println("main: " + Thread.currentThread().getName());
        }
    }
}
```

**Output:** (Note: exact output will vary between runs due to thread scheduling)
```
main: main
run: NIT
main: main
run: NIT
main: main
run: NIT
run: NIT
run: NIT
run: NIT
main: main
main: main
...
```

[Back to Top](#table-of-contents)

## How many ways can we define user-defined threads?

In Java, there are two primary ways to create user-defined threads:

1. **Extending the `java.lang.Thread` class**
   - Direct inheritance from Thread class
   - Override the `run()` method
   - Advantages: Direct access to thread methods
   - Disadvantages: Limits inheritance options (Java doesn't support multiple inheritance)

2. **Implementing the `java.lang.Runnable` interface**
   - Implement the Runnable interface
   - Override the `run()` method
   - Pass the Runnable object to a Thread constructor
   - Advantages: Better for classes that need to extend another class
   - Better option for sharing the same task between multiple threads

### System Threads in Java JVM:

Java has several system threads that run automatically:
1. Main Thread
2. Finalizer (Garbage Collection)
3. Attach Listener
4. Signal Dispatcher
5. Reference Handler

### Example Using Thread Class:

```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running: " + Thread.currentThread().getName());
    }
    
    public static void main(String[] args) {
        MyThread thread = new MyThread();
        thread.setName("CustomThread");
        thread.start();
    }
}
```

### Example Using Runnable Interface:

```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable running: " + Thread.currentThread().getName());
    }
    
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.setName("RunnableThread");
        thread.start();
    }
}
```

[Back to Top](#table-of-contents)

## What is Thread Chaining?

**Thread chaining** refers to the execution of methods in the same thread context. When a thread executes a method, and that method calls another method, both methods execute in the same thread.

### Key Points about Thread Chaining:

- All method calls within a thread share the same thread context
- Methods executed by a thread will display the same thread name
- Thread chaining maintains the execution context across method calls
- It's important for understanding thread-safety and synchronization requirements

### Example:

```java
class Test extends Thread {
    @Override
    public void run() {
        System.out.println("run: " + Thread.currentThread().getName());
        this.m1(); // Method call chained within the same thread
    }

    public void m1() {
        System.out.println("m1: " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        Test t = new Test();
        System.out.println("main: " + Thread.currentThread().getName());
        t.setName("NIT");
        t.start();
    }
}
```

**Output:**
```
main: main
run: NIT
m1: NIT
```

The output shows that both `run()` and `m1()` methods execute in the same thread named "NIT", while the main method executes in the "main" thread. This demonstrates thread chaining - when run() calls m1(), the method call is executed in the same thread context.

[Back to Top](#table-of-contents)

## Can we define multiple user-defined threads using Thread class and Runnable interface?

Yes, we can define and execute multiple user-defined threads in Java using both the Thread class and Runnable interface.

### Key Points:

- Multiple threads can be created from the same Runnable object 
- Each thread can execute the same task concurrently
- Thread scheduling is managed by the JVM's thread scheduler
- Thread execution order is not guaranteed

### Example using Runnable Interface:

```java
class MyThread implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(i + ". run: " + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        MyThread mt1 = new MyThread(); // Single Runnable object
        MyThread mt2 = new MyThread(); // Another Runnable object

        Thread t1 = new Thread(mt1); // First thread with first Runnable
        Thread t2 = new Thread(mt2); // Second thread with second Runnable

        t1.start(); // Start first thread
        t2.start(); // Start second thread
    }
}
```

**Output:** (Note: actual output will vary due to thread scheduling)
```
1. run: Thread-0
1. run: Thread-1
2. run: Thread-0
2. run: Thread-1
3. run: Thread-0
3. run: Thread-1
4. run: Thread-0
4. run: Thread-1
5. run: Thread-0
5. run: Thread-1
6. run: Thread-0
6. run: Thread-1
...
```

We can also use a single Runnable object for multiple threads:

```java
MyThread sharedTask = new MyThread();
Thread t1 = new Thread(sharedTask);
Thread t2 = new Thread(sharedTask);
```

This approach is useful when you want multiple threads to execute the same task.

[Back to Top](#table-of-contents)

## How to handle multiple user-defined threads execution?

Handling multiple user-defined threads involves creating, starting, and managing the execution of several threads that run concurrently.

### Key Techniques:

1. **Creating named threads** - Better for debugging and logging
2. **Setting thread priorities** - Influence scheduling (but not guarantee execution order)
3. **Using sleep()** - Introduce pauses to give other threads chance to execute
4. **Using join()** - Wait for specific threads to complete
5. **Using synchronization** - Coordinate access to shared resources

### Example of Multiple Thread Execution:

```java
class Test extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(i + ". run: " + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        Test mt1 = new Test();
        mt1.setName("KIT"); // First thread named "KIT"

        Test mt2 = new Test();
        mt2.setName("NIT"); // Second thread named "NIT"

        mt1.start(); // Start first thread
        mt2.start(); // Start second thread
    }
}
```

**Output:** (Note: actual output will vary between runs)
```
1. run: KIT
1. run: NIT
2. run: KIT
2. run: NIT
3. run: KIT
3. run: NIT
4. run: KIT
4. run: NIT
5. run: KIT
5. run: NIT
6. run: KIT
6. run: NIT
7. run: KIT
8. run: KIT
7. run: NIT
9. run: KIT
8. run: NIT
10. run: KIT
9. run: NIT
10. run: NIT
```

### Advanced Control with Sleep:

To introduce more controlled execution, we can use `sleep()`:

```java
@Override
public void run() {
    for (int i = 1; i <= 10; i++) {
        System.out.println(i + ". run: " + Thread.currentThread().getName());
        try {
            Thread.sleep(100); // Pause for 100ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

This gives other threads more chance to execute, making the interleaving of thread execution more predictable.

[Back to Top](#table-of-contents)

## How to develop a deadlock scenario using join()?

A deadlock occurs when two or more threads are blocked forever, waiting for each other. Using the `join()` method, we can create a deadlock scenario where threads wait for each other indefinitely.

### Steps to Create a Deadlock with join():

1. Create two threads that have references to each other
2. Make each thread call `join()` on the other thread in its `run()` method
3. Both threads will wait for the other to complete, resulting in a deadlock

### Example:

```java
class FirstThread extends Thread {
    private SecondThread secondThread;

    public void setSecondThread(SecondThread secondThread) {
        this.secondThread = secondThread;
    }

    @Override
    public void run() {
        try {
            System.out.println("FirstThread is starting...");
            System.out.println("FirstThread is waiting for SecondThread to complete...");
            secondThread.join(); // FirstThread waits for SecondThread to complete
            System.out.println("FirstThread completed."); // This will never be reached
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class SecondThread extends Thread {
    private FirstThread firstThread;

    public void setFirstThread(FirstThread firstThread) {
        this.firstThread = firstThread;
    }

    @Override
    public void run() {
        try {
            System.out.println("SecondThread is starting...");
            System.out.println("SecondThread is waiting for FirstThread to complete...");
            firstThread.join(); // SecondThread waits for FirstThread to complete
            System.out.println("SecondThread completed."); // This will never be reached
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class DeadlockExample {
    public static void main(String[] args) {
        FirstThread firstThread = new FirstThread();
        SecondThread secondThread = new SecondThread();

        // Set references to each other to create deadlock scenario
        firstThread.setSecondThread(secondThread);
        secondThread.setFirstThread(firstThread);

        // Start both threads
        firstThread.start();
        secondThread.start();
    }
}
```

**Output:**
```
FirstThread is starting...
FirstThread is waiting for SecondThread to complete...
SecondThread is starting...
SecondThread is waiting for FirstThread to complete...
// Program hangs indefinitely - deadlock occurred
```

### Another Example (Self-Deadlock):

A thread can also create a deadlock by calling `join()` on itself:

```java
public class SelfDeadlockExample {
    public static void main(String[] args) {
        final Thread thread = new Thread(() -> {
            try {
                System.out.println("Thread is starting...");
                System.out.println("Thread is waiting for itself to complete...");
                Thread.currentThread().join(); // Thread waits for itself - instant deadlock!
                System.out.println("This will never be printed.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}
```

[Back to Top](#table-of-contents)

## What is the thread lifecycle?

The Java thread lifecycle consists of different states that a thread goes through from creation to termination.

### Thread States:

1. **New**: Thread object is created but not yet started
2. **Runnable**: Thread is ready to run, either running or waiting for resource allocation
3. **Blocked**: Thread is temporarily inactive, waiting for a monitor lock
4. **Waiting**: Thread is waiting indefinitely for another thread to perform a specific action
5. **Timed Waiting**: Thread is waiting for another thread with a specified waiting time
6. **Terminated**: Thread has completed execution

### Important Points about Thread Lifecycle:

- Once a thread is started, it cannot be restarted
- Attempting to call `start()` more than once will throw `IllegalThreadStateException`
- A thread can enter Waiting or Timed Waiting state using methods like `wait()`, `join()` or `sleep()`
- A thread enters Blocked state when trying to acquire a lock that is held by another thread

### Example Demonstrating Thread Lifecycle:

```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("run: " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        MyThread mt = new MyThread(); // New state
        mt.setName("NIT");
        mt.start(); // Runnable state
        // mt.start(); // IllegalThreadStateException - Cannot start the same thread twice
        
        // Get current thread state
        System.out.println("Thread state: " + mt.getState());
    }
}
```

### Comprehensive Example Showing Multiple States:

```java
public class ThreadLifecycleDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                // Thread enters TIMED_WAITING state
                Thread.sleep(1000);
                
                synchronized (Thread.currentThread()) {
                    Thread.currentThread().wait(2000); // Thread enters WAITING state
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        // Thread is in NEW state
        System.out.println("State after creation: " + thread.getState());
        
        thread.start();
        // Thread is in RUNNABLE state
        System.out.println("State after starting: " + thread.getState());
        
        Thread.sleep(500);
        // Thread should be in TIMED_WAITING state due to sleep()
        System.out.println("State during sleep: " + thread.getState());
        
        thread.join(); // Wait for thread to die
        // Thread is in TERMINATED state
        System.out.println("State after completion: " + thread.getState());
    }
}
```

**Output:**
```
State after creation: NEW
State after starting: RUNNABLE
State during sleep: TIMED_WAITING
State after completion: TERMINATED
```

[Back to Top](#table-of-contents)

## What is sleep() in java.lang.Thread?

The `sleep()` method in `java.lang.Thread` causes the currently executing thread to pause its execution for a specified period of time. This temporarily stops the execution of the current thread and allows other threads to execute.

### Key Features of sleep():

- **Static Method**: Called using the class name: `Thread.sleep()`
- **Overloaded Versions**: 
  - `sleep(long millis)` - Pauses for specified milliseconds
  - `sleep(long millis, int nanos)` - Adds nanosecond precision
- **Throws Exception**: Requires handling `java.lang.InterruptedException`
- **Does Not Release Locks**: If a thread holds any locks, it continues to hold them while sleeping
- **Not Precise**: The actual sleep time may be longer than requested due to system timers and scheduling

### Example:

```java
class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("run: " + Thread.currentThread().getName());
            try {
                Thread.sleep(10); // Sleep for 10 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MyThread mt = new MyThread();
        mt.setName("NIT");
        mt.start();
        for (int i = 1; i <= 10; i++) {
            System.out.println("main: " + Thread.currentThread().getName());
        }
    }
}
```

**Output:** (will vary between runs)
```
run: NIT
main: main
main: main
main: main
main: main
main: main
main: main
main: main
main: main
main: main
main: main
run: NIT
run: NIT
run: NIT
run: NIT
run: NIT
run: NIT
run: NIT
run: NIT
run: NIT
```

Since the NIT thread sleeps for 10ms after each iteration, the main thread often completes all its iterations before the NIT thread completes.

### Using sleep() with Different Threads:

```java
class ThreadDemo extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(getName() + ": " + i);
            try {
                Thread.sleep(1000); // Sleep for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        ThreadDemo t1 = new ThreadDemo();
        ThreadDemo t2 = new ThreadDemo();
        
        t1.setName("Thread-A");
        t2.setName("Thread-B");
        
        t1.start();
        t2.start();
    }
}
```

**Output:** (roughly alternating between threads with 1-second intervals)
```
Thread-A: 1
Thread-B: 1
Thread-A: 2
Thread-B: 2
Thread-A: 3
Thread-B: 3
Thread-A: 4
Thread-B: 4
Thread-A: 5
Thread-B: 5
```

[Back to Top](#table-of-contents)

## How to use join() in multithreading?

The `join()` method allows one thread to wait for the completion of another thread. It's used to establish dependencies between threads, ensuring that one thread finishes before another continues.

### Key Features of join():

- **Method Type**: Instance method called on a Thread object
- **Overloaded Versions**:
  - `join()` - Waits indefinitely until the thread dies
  - `join(long millis)` - Waits for specified milliseconds
  - `join(long millis, int nanos)` - Adds nanosecond precision
- **Throws Exception**: Requires handling `java.lang.InterruptedException`
- **Common Use Case**: Ensuring sequential execution of certain parts of code

### Example:

```java
public class Test extends Thread {
    static Thread mt = null;

    @Override
    public void run() {
        try {
            System.out.println("User thread: " + Thread.currentThread().getName());
            mt.join(); // Wait for main thread to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= 10; i++) {
            System.out.println("run: " + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Test t = new Test();
        t.setName("NIT");
        mt = Thread.currentThread(); // Store reference to main thread
        t.start();

        for (int i = 1; i <= 10; i++) {
            System.out.println("main: " + Thread.currentThread().getName());
            Thread.sleep(1000); // Sleep for 1 second
        }
    }
}
```

**Output:**
```
User thread: NIT
main: main
main: main
main: main
main: main
main: main
main: main
main: main
main: main
main: main
main: main
run: NIT
run: NIT
run: NIT
run: NIT
run: NIT
run: NIT
run: NIT
run: NIT
run: NIT
run: NIT
```

In this example, the NIT thread waits for the main thread to complete before executing its for loop.

### Practical Example - File Processing:

```java
public class JoinExampleFileProcessing {
    public static void main(String[] args) {
        Thread fileLoader = new Thread(() -> {
            System.out.println("Loading file...");
            try {
                Thread.sleep(3000); // Simulate file loading time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("File loaded successfully");
        });
        
        Thread fileProcessor = new Thread(() -> {
            try {
                fileLoader.join(); // Wait for file to be loaded
                System.out.println("Processing file...");
                Thread.sleep(2000); // Simulate processing time
                System.out.println("File processing complete");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        fileLoader.start();
        fileProcessor.start();
    }
}
```

**Output:**
```
Loading file...
File loaded successfully
Processing file...
File processing complete
```

[Back to Top](#table-of-contents)

## What is the yield() method and when to use it?

The `yield()` method in Java provides a hint to the thread scheduler that the current thread is willing to yield its current use of the processor. The scheduler can then select another thread for execution.

### Key Features of yield():

- **Static Method**: Called using `Thread.yield()`
- **No Parameters**: Simply indicates that the thread is willing to pause
- **No Guarantee**: The thread scheduler is free to ignore the hint
- **Doesn't Release Locks**: Similar to `sleep()`, a yielding thread keeps any locks it has
- **No Exception**: Unlike `sleep()` and `join()`, `yield()` doesn't throw InterruptedException

### When to Use yield():

- When you want to give other threads of the same priority a chance to execute
- For cooperative multitasking where threads voluntarily share CPU time
- To improve application responsiveness by allowing UI threads to run
- For testing thread scheduling behavior

### Example:

```java
public class YieldExample {
    public static void main(String[] args) {
        Thread producer = new Thread(new Producer(), "ProducerThread");
        Thread consumer = new Thread(new Consumer(), "ConsumerThread");

        producer.start();
        consumer.start();
    }
}

class Producer implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " producing item " + i);
            // Yielding control to allow other threads to execute
            Thread.yield();
        }
    }
}

class Consumer implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " consuming item " + i);
            // Yielding control to allow other threads to execute
            Thread.yield();
        }
    }
}
```

**Output:** (may vary due to thread scheduling)
```
ProducerThread producing item 0
ConsumerThread consuming item 0
ProducerThread producing item 1
ConsumerThread consuming item 1
ProducerThread producing item 2
ConsumerThread consuming item 2
ProducerThread producing item 3
ConsumerThread consuming item 3
ProducerThread producing item 4
ConsumerThread consuming item 4
```

### Important Notes about yield():

- The exact behavior depends on the JVM implementation and the underlying OS
- `yield()` is primarily used for testing and debugging
- For production code, other synchronization techniques are usually more appropriate
- In modern applications, thread scheduling is typically left to the JVM and OS

[Back to Top](#table-of-contents)

## How to use interrupt() method?

The `interrupt()` method in Java is used to interrupt a thread that is in a waiting, sleeping, or blocked state. It's a way to signal to a thread that it should stop what it's doing and potentially terminate.

### Key features:

* **Purpose**: Sends an interrupt signal to a thread or can say Called on a Thread object to interrupt that specific thread
* **Effect**: Sets the interrupt status flag of the target thread
* **Thread state impact**: If the thread is in a blocking operation (like `sleep()` or `wait()`), it will throw an `InterruptedException`
* **Usage**: Commonly used for graceful thread termination. It doesn't forcibly stop the thread but provides a mechanism for cooperative cancellation


### How interruption works:

1. Thread A calls `interrupt()` on Thread B
2. If Thread B is blocked in a method that throws InterruptedException, it will be woken up with that exception
3. If Thread B is not blocked, its interrupted status flag is set to true
4. Thread B can check its interrupted status using `Thread.interrupted()` or `isInterrupted()`

### Example:

```java
public class InterruptExample {
    public static void main(String[] args) {
        Thread workerThread = new Thread(new Worker(), "WorkerThread");
        workerThread.start();

        // Main thread sleeps for 2 seconds before interrupting worker
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Interrupt the worker thread
        System.out.println("Main thread is interrupting the worker thread");
        workerThread.interrupt();
    }
}

class Worker implements Runnable {
    @Override
    public void run() {
        try {
            // Simulate work with sleep
            System.out.println(Thread.currentThread().getName() + " is starting to work");
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " is working...");
                Thread.sleep(1000); // Will throw InterruptedException when interrupted
            }
            System.out.println(Thread.currentThread().getName() + " has finished work");
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted");
            // Handle interruption - typically by terminating the thread's execution
        }
    }
}
```

**Expected Output:**
```
WorkerThread is starting to work
WorkerThread is working...
WorkerThread is working...
Main thread is interrupting the worker thread
WorkerThread was interrupted
```

### Best Practices for Using interrupt():

1. **Handle the InterruptedException**: Always catch and handle the exception properly
2. **Clean up resources**: In the catch block, perform any necessary cleanup
3. **Use interrupt status**: Check `Thread.isInterrupted()` for long-running operations
4. **Reset interrupt status**: Use `Thread.interrupted()` if you need to clear and return the status

[Back to Top](#table-of-contents)

## What is synchronization in Java?

**Synchronization** is the process of controlling access to shared resources so that only one thread can access a resource at any given time. This prevents data inconsistency issues in multithreaded applications.

### Key Concepts:

* **Purpose**: Ensures data integrity and consistency in multithreaded environments
* **Mechanism**: Uses monitors (locks) to control thread access to synchronized code segments
* **Types**: Method synchronization and block synchronization
* **Cost**: Introduces performance overhead due to thread blocking
* **Core issue solved**: Race conditions where multiple threads modify shared data concurrently

### When to use synchronization:

* When multiple threads access and modify shared data
* When operations need to be atomic
* When thread safety is required for shared resources

### Consequences without synchronization:

* Data corruption
* Race conditions
* Unpredictable behavior
* Inconsistent states

[Back to Top](#table-of-contents)

## How to implement synchronized methods and blocks?

Java provides two primary ways to implement synchronization: **synchronized methods** and **synchronized blocks**.

### Synchronized Methods:

Adding the `synchronized` keyword to a method ensures that only one thread can execute the method at a time.

```java
class Institute {
    // Synchronized method - only one thread can access at a time
    synchronized public void classRoom(String facultyName) {
        for (int i = 1; i <= 5; i++) {
            System.out.println(i + ". Class Taking By " + facultyName);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

### Synchronized Blocks:

Synchronized blocks provide more fine-grained control by specifying exactly which object to lock.

```java
class Institute {
    public void classRoom(String facultyName) {
        // Only this block is synchronized
        synchronized(this) {
            for (int i = 1; i <= 5; i++) {
                System.out.println(i + ". Class Taking By " + facultyName);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        // This code is not synchronized
        System.out.println("Class completed by " + facultyName);
    }
}
```

### Complete Example with Multiple Threads:

```java
class Institute {
    synchronized public void classRoom(String facultyName) {
        for (int i = 1; i <= 5; i++) {
            System.out.println(i + ". Class Taking By " + facultyName);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Non-synchronized method - can run concurrently
    public void exam() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(i + ". Students Writing exam -- " + Thread.currentThread().getName());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class TeacherThread extends Thread {
    Institute institute;
    String facultyName;

    TeacherThread(Institute institute, String facultyName) {
        this.institute = institute;
        this.facultyName = facultyName;
    }

    @Override
    public void run() {
        institute.classRoom(facultyName);
    }
}

public class SynchronizationDemo {
    public static void main(String[] args) {
        Institute institute = new Institute();
        
        // Create two teacher threads
        TeacherThread teacher1 = new TeacherThread(institute, "Professor Smith");
        TeacherThread teacher2 = new TeacherThread(institute, "Dr. Johnson");
        
        // Start the exam (non-synchronized method)
        new Thread(() -> {
            institute.exam();
        }, "ExamThread").start();
        
        // Start teacher threads
        teacher1.start();
        teacher2.start();
    }
}
```

**Expected Output**: 
```
1. Class Taking By Professor Smith
1. Students Writing exam -- ExamThread
2. Students Writing exam -- ExamThread
3. Students Writing exam -- ExamThread
2. Class Taking By Professor Smith
4. Students Writing exam -- ExamThread
5. Students Writing exam -- ExamThread
3. Class Taking By Professor Smith
4. Class Taking By Professor Smith
5. Class Taking By Professor Smith
1. Class Taking By Dr. Johnson
2. Class Taking By Dr. Johnson
3. Class Taking By Dr. Johnson
4. Class Taking By Dr. Johnson
5. Class Taking By Dr. Johnson
```

### Important Rules for Synchronization:

1. **Static vs. Non-static**:
   * Static synchronized methods lock the class object
   * Non-static synchronized methods lock the instance object

2. **Synchronization Restrictions**:
   * If any thread is executing a synchronized method, other threads cannot execute ANY synchronized method in the same object, but can execute non-synchronized methods
   * Static synchronized methods do not interfere with non-static synchronized methods (they use different locks)

[Back to Top](#table-of-contents)

## What is inter-thread communication?

**Inter-thread communication** is a mechanism that allows synchronized threads to communicate with each other about the status of an event. Java provides `wait()`, `notify()`, and `notifyAll()` methods in the Object class for this purpose.

### Key Methods:

| Method | Description |
|--------|------------|
| `wait()` | Causes current thread to release the lock and wait until another thread invokes `notify()` or `notifyAll()` |
| `wait(long timeout)` | Waits for the specified amount of time or until notified |
| `notify()` | Wakes up a single thread waiting on this object's monitor |
| `notifyAll()` | Wakes up all threads waiting on this object's monitor |

### Important Notes:

* These methods must be called from within a synchronized block or method
* They are defined in the `Object` class, not the `Thread` class
* They throw `IllegalMonitorStateException` if the current thread doesn't own the monitor

### Example - Account Transaction:

```java
class Account {
    private double balance = 1000.00;

    synchronized public void withdraw(double amount) {
        System.out.println("Withdraw process starts");
        System.out.println("Current balance: " + balance);
        
        if (balance < amount) {
            System.out.println("Insufficient funds. Waiting for deposit...");
            try {
                wait(); // Thread goes into waiting state
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // After notification or if sufficient balance
        balance -= amount;
        System.out.println("Withdrawal complete. New balance: " + balance);
    }

    synchronized public void deposit(double amount) {
        System.out.println("Deposit process starts");
        System.out.println("Current balance: " + balance);
        
        balance += amount;
        System.out.println("Deposit complete. New balance: " + balance);
        
        notify(); // Notify the waiting thread (if any)
        // Use notifyAll() if multiple threads might be waiting
    }
}

public class InterThreadCommunicationDemo {
    public static void main(String[] args) {
        final Account account = new Account();
        
        // Withdrawal thread
        new Thread(() -> {
            account.withdraw(1500);
        }).start();
        
        // Sleep to ensure withdrawal thread starts first
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Deposit thread
        new Thread(() -> {
            account.deposit(1000);
        }).start();
    }
}
```

**Expected Output**:
```
Withdraw process starts
Current balance: 1000.0
Insufficient funds. Waiting for deposit...
Deposit process starts
Current balance: 1000.0
Deposit complete. New balance: 2000.0
Withdrawal complete. New balance: 500.0
```

### Common Use Cases:

* Producer-Consumer problems
* Thread coordination in complex applications
* Resource management scenarios
* Implementing custom thread pools

[Back to Top](#table-of-contents)

## What are daemon and non-daemon threads?

In Java, threads are classified into two categories: **daemon threads** and **non-daemon threads** (also called user threads).

### Comparison:

| Feature | Daemon Thread | Non-Daemon Thread |
|---------|--------------|-------------------|
| **Priority** | Low priority | High priority |
| **Purpose** | Background service provider | Main tasks of the application |
| **JVM Exit** | JVM exits when all non-daemon threads complete, regardless of daemon threads | JVM waits for all non-daemon threads to complete |
| **Examples** | Garbage collector, Signal dispatcher | Main thread, user-created threads |
| **Default** | By default all new threads inherit daemon status from their parent | Main thread is non-daemon by default |

### Methods for Daemon Thread Management:

* `setDaemon(boolean)` - Sets the thread as daemon or non-daemon
* `isDaemon()` - Checks if a thread is a daemon thread

### Example:

```java
class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(i + ". " + Thread.currentThread().getName() + 
                               " (Daemon: " + Thread.currentThread().isDaemon() + ")");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class DaemonThreadDemo {
    @Override
    protected void finalize() {
        System.out.println("Finalize method called by: " + 
                         Thread.currentThread().getName() + 
                         " (Daemon: " + Thread.currentThread().isDaemon() + ")");
    }
    
    public static void main(String[] args) throws InterruptedException {
        // Check main thread
        Thread mainThread = Thread.currentThread();
        System.out.println("Main thread: " + mainThread.getName() + 
                         " (Daemon: " + mainThread.isDaemon() + ")");
        
        // Create a regular thread
        MyThread regularThread = new MyThread();
        regularThread.setName("RegularThread");
        
        // Create a daemon thread
        MyThread daemonThread = new MyThread();
        daemonThread.setName("DaemonThread");
        daemonThread.setDaemon(true); // Set as daemon thread
        
        // Start both threads
        regularThread.start();
        daemonThread.start();
        
        // Main thread runs for a short time
        for (int i = 1; i <= 3; i++) {
            System.out.println(i + ". Main thread executing");
            Thread.sleep(200);
        }
        
        // Create an object for garbage collection
        new DaemonThreadDemo();
        System.gc();
        
        System.out.println("Main thread exiting");
        // When main and regularThread finish, JVM will exit even if daemonThread is still running
    }
}
```

**Expected Output**: (Note: The actual interleaving may vary)
```
Main thread: main (Daemon: false)
1. Main thread executing
1. RegularThread (Daemon: false)
1. DaemonThread (Daemon: true)
2. Main thread executing
2. RegularThread (Daemon: false)
2. DaemonThread (Daemon: true)
3. Main thread executing
3. RegularThread (Daemon: false)
3. DaemonThread (Daemon: true)
Main thread exiting
Finalize method called by: Finalizer (Daemon: true)
4. RegularThread (Daemon: false)
4. DaemonThread (Daemon: true)
...
10. RegularThread (Daemon: false)
```

Note that the daemon thread may be terminated when the JVM exits after the regular thread completes.

### Important Points:

* `setDaemon(true)` must be called before `start()`, otherwise it throws `IllegalThreadStateException`
* The Garbage Collector is a daemon thread
* Daemon threads are useful for background tasks that shouldn't prevent application termination

[Back to Top](#table-of-contents)

## How to use ThreadGroup in Java?

A **ThreadGroup** represents a set of threads that can be managed as a unit. Thread groups create a tree-like structure where every thread group except the initial system thread group has a parent.

### Key Methods:

| Method | Description |
|--------|-------------|
| `ThreadGroup(String name)` | Creates a new thread group |
| `ThreadGroup(ThreadGroup parent, String name)` | Creates a thread group with specified parent |
| `setMaxPriority(int pri)` | Sets the maximum priority for this thread group |
| `activeCount()` | Returns the number of active threads in this group |
| `list()` | Prints information about this thread group to the console |
| `isDestroyed()` | Tests if this thread group has been destroyed |
| `isDaemon()` | Tests if this thread group is a daemon thread group |

### Example:

```java
class GroupThread extends Thread {
    public GroupThread(ThreadGroup group, String name) {
        super(group, name);
    }
    
    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName() + 
                         " starting in thread group " + 
                         Thread.currentThread().getThreadGroup().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " ending");
    }
}

public class ThreadGroupDemo {
    public static void main(String[] args) throws InterruptedException {
        // Create parent thread group
        ThreadGroup parentGroup = new ThreadGroup("ParentGroup");
        
        // Create child thread group within the parent group
        ThreadGroup childGroup = new ThreadGroup(parentGroup, "ChildGroup");
        
        // Set max priority for the groups
        System.out.println("Default max priority of ParentGroup: " + parentGroup.getMaxPriority());
        parentGroup.setMaxPriority(7);
        System.out.println("Updated max priority of ParentGroup: " + parentGroup.getMaxPriority());
        
        // Create threads in different groups
        GroupThread thread1 = new GroupThread(parentGroup, "ParentThread1");
        GroupThread thread2 = new GroupThread(parentGroup, "ParentThread2");
        GroupThread thread3 = new GroupThread(childGroup, "ChildThread1");
        
        // Start threads
        thread1.start();
        thread2.start();
        thread3.start();
        
        // Display active counts
        System.out.println("Active threads in parent group: " + parentGroup.activeCount());
        System.out.println("Active thread groups in parent group: " + parentGroup.activeGroupCount());
        
        // List information about thread groups
        System.out.println("\nThread group information:");
        parentGroup.list();
        
        // Wait for threads to finish
        Thread.sleep(3000);
        
        System.out.println("\nAfter threads complete:");
        System.out.println("Active threads in parent group: " + parentGroup.activeCount());
    }
}
```

**Expected Output**:
```
Default max priority of ParentGroup: 10
Updated max priority of ParentGroup: 7
Thread ParentThread1 starting in thread group ParentGroup
Thread ChildThread1 starting in thread group ChildGroup
Thread ParentThread2 starting in thread group ParentGroup
Active threads in parent group: 3
Active thread groups in parent group: 1

Thread group information:
java.lang.ThreadGroup[name=ParentGroup,maxpri=7]
    Thread[ParentThread1,5,ParentGroup]
    Thread[ParentThread2,5,ParentGroup]
    java.lang.ThreadGroup[name=ChildGroup,maxpri=7]
        Thread[ChildThread1,5,ChildGroup]

Thread ParentThread1 ending
Thread ParentThread2 ending
Thread ChildThread1 ending

After threads complete:
Active threads in parent group: 0
```

### Practical Uses of ThreadGroups:

1. **Organizational tool** for managing related threads
2. **Access control** - setting permissions for entire groups
3. **Error handling** at the group level with `uncaughtException` handler
4. **Resource management** - prioritizing groups of threads
5. **Monitoring** active threads in specific areas of an application

### Best Practices:

* Use thread groups sparingly - modern Java often uses other concurrency frameworks
* Be aware that some methods like `stop()`, `suspend()`, and `resume()` are deprecated for thread groups
* ThreadGroups are most useful for monitoring and organizational purposes

[Back to Top](#table-of-contents)

## How to implement Runnable using Lambda expressions?

With the introduction of Lambda expressions in Java 8, implementing the `Runnable` interface has become much simpler and more concise.

### Traditional vs. Lambda Approaches:

#### 1. Traditional approach using anonymous class:

```java
// Traditional way
Thread thread = new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("Thread executing in anonymous class style");
    }
});
thread.start();
```

#### 2. Lambda expression approach:

```java
// Using Lambda expression
Thread thread = new Thread(() -> {
    System.out.println("Thread executing with lambda expression");
});
thread.start();
```

#### 3. Even more concise for simple operations:

```java
// Single statement lambda (no braces needed)
Thread thread = new Thread(() -> System.out.println("Concise lambda thread"));
thread.start();
```

### Complete Example:

```java
public class RunnableLambdaDemo {
    public static void main(String[] args) {
        System.out.println("Main thread: " + Thread.currentThread().getName());
        
        // Method 1: Traditional way with separate class
        Thread thread1 = new Thread(new TraditionalRunnable());
        thread1.start();
        
        // Method 2: Anonymous inner class
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread running with anonymous class");
            }
        });
        thread2.start();
        
        // Method 3: Lambda expression with multiple statements
        Thread thread3 = new Thread(() -> {
            System.out.println("Thread running with lambda expression");
            System.out.println("Lambda can contain multiple statements");
        });
        thread3.start();
        
        // Method 4: Concise lambda for single statement
        Thread thread4 = new Thread(() -> System.out.println("Concise lambda thread"));
        thread4.start();
        
        // Method 5: Using a method reference (if the method exists)
        Thread thread5 = new Thread(RunnableLambdaDemo::printMessage);
        thread5.start();
    }
    
    private static void printMessage() {
        System.out.println("Thread running with method reference");
    }
}

class TraditionalRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread running with traditional separate class");
    }
}
```

**Expected Output**:
```
Main thread: main
Thread running with traditional separate class
Thread running with anonymous class
Thread running with lambda expression
Lambda can contain multiple statements
Concise lambda thread
Thread running with method reference
```

### Benefits of Lambda Expressions for Runnable:

1. **Conciseness**: Significantly reduces boilerplate code
2. **Readability**: Code intention is clearer with less structural syntax
3. **Functional style**: Encourages functional programming paradigms
4. **Better encapsulation**: Variables from the enclosing scope can be accessed directly

### Practical Usage Scenarios:

1. **Simple background tasks**:
   ```java
   new Thread(() -> performBackgroundTask()).start();
   ```

2. **One-off thread creation**:
   ```java
   executor.submit(() -> processData(dataSet));
   ```

3. **Event handlers**:
   ```java
   button.addActionListener(e -> System.out.println("Button clicked"));
   ```

4. **Multiple operations in the thread**:
   ```java
   new Thread(() -> {
       loadData();
       processData();
       saveResults();
   }).start();
   ```

### Important Considerations:

* Lambda expressions can access effectively final variables from the enclosing scope
* For more complex threading scenarios, consider using the Executor framework instead of direct Thread creation
* Lambda expressions for Runnable are syntactic sugar - under the hood, Java still creates an anonymous class

[Back to Top](#table-of-contents)