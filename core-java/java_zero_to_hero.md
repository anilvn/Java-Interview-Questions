# Java Interview Questions Repository

This document contains a curated list of Java interview questions covering various topics from basic concepts to advanced features.

## Table of Contents

| #   | Question                                                                                                |
| --- | ------------------------------------------------------------------------------------------------------- |
| 1   | [Why is Java not 100% object-oriented?](#why-is-java-not-100-object-oriented)                          |
|     | **Java Basics** |
| 2   | [What are the features of Java?](#what-are-the-features-of-java)                                        |
| 3   | [What do you mean by platform independence?](#what-do-you-mean-by-platform-independence)                 |
| 4   | [What is the difference between JDK, JRE, and JVM?](#what-is-the-difference-between-jdk-jre-and-jvm)    |
| 5   | [What are the jobs of JVM?](#what-are-the-jobs-of-jvm)                                                  |
| 6   | [What are the memory areas available in Java?](#what-are-the-memory-areas-available-in-java)            |
| 7   | [What is garbage collection in Java? What methods are related to GC?](#what-is-garbage-collection-in-java-what-methods-are-related-to-gc) |
| 8   | [What is a package in Java?](#what-is-a-package-in-java)                                                |
| 9   | [What are the different data types in Java?](#what-are-the-different-data-types-in-java)                |
| 10  | [What are string literals?](#what-are-string-literals)                                                  |
| 11  | [What is the difference between declaring, defining, and initializing a value?](#what-is-the-difference-between-declaring-defining-and-initializing-a-value) |
| 12  | [What is the difference between a compiler, an interpreter, and bytecode?](#what-is-the-difference-between-a-compiler-an-interpreter-and-bytecode) |
| 13  | [How many different ways are there to take input in Java?](#how-many-different-ways-are-there-to-take-input-in-java) |
| 14  | [Can I import the same package/class twice? Will the JVM load the package twice at runtime?](#can-i-import-the-same-packageclass-twice-will-the-jvm-load-the-package-twice-at-runtime) |
|     | **Unicode and Identifiers** |
| 15  | [What is Unicode, and which Unicode system does Java use?](#what-is-unicode-and-which-unicode-system-does-java-use) |
| 16  | [Which non-Unicode characters can be used as the first character of an identifier?](#which-non-unicode-characters-can-be-used-as-the-first-character-of-an-identifier) |
|     | **Primitive Types and Wrapper Classes** |
| 17  | [What are primitive types and their wrapper classes in Java?](#what-are-primitive-types-and-their-wrapper-classes-in-java) |
| 18  | [What methods are inside primitive and wrapper classes?](#what-methods-are-inside-primitive-and-wrapper-classes) |
| 19  | [What is autoboxing and unboxing?](#what-is-autoboxing-and-unboxing)                                    |
|     | **Casting and Cloning** |
| 20  | [What is casting (implicit and explicit)?](#what-is-casting-implicit-and-explicit)                      |
| 21  | [Different types of cloning available in Java? Difference between deep cloning and shallow cloning?](#different-types-of-cloning-available-in-java-difference-between-deep-cloning-and-shallow-cloning) |
|     | **Strings and String Pool** |
| 22  | [Why is String not mutable, and how to develop a mutable String in Java?](#why-is-string-not-mutable-and-how-to-develop-a-mutable-string-in-java) |
| 23  | [What is the difference between String, StringBuilder, and StringBuffer?](#what-is-the-difference-between-string-stringbuilder-and-stringbuffer) |
| 24  | [What is the String constant pool?](#what-is-the-string-constant-pool)                                  |
| 25  | [String s_1 = "anil"; vs. String s_2 = new String("anil");](#string-s_1--anil-vs-string-s_2--new-stringanil) |
|     | **Operators and Comparisons** |
| 26  | [What is the difference between == and .equals?](#what-is-the-difference-between--and-equals)           |
| 27  | [How do you convert a String to an int (parse)?](#how-do-you-convert-a-string-to-an-int-parse)          |
|     | **Singleton and Static** |
| 28  | [What is a singleton class in Java?](#what-is-a-singleton-class-in-java)                                |
| 29  | [What is static in Java?](#what-is-static-in-java)                                                      |
| 30  | [Why is the main method static in Java?](#why-is-the-main-method-static-in-java)                        |
| 31  | [Can we run main() without the static keyword?](#can-we-run-main-without-the-static-keyword)            |
| 32  | [Can we run a Java class without a main method?](#can-we-run-a-java-class-without-a-main-method)        |
| 33  | [Can you declare the main method as final or private?](#can-you-declare-the-main-method-as-final-or-private) |
| 34  | [Does 'main' a keyword in Java? What are other identifiers?](#does-main-a-keyword-in-java-what-are-other-identifiers) |
| 35  | [What if we write static public void instead of public static void?](#what-if-we-write-static-public-void-instead-of-public-static-void) |
| 36  | [Can a static reference a non-static variable? What happens if it does?](#can-a-static-reference-a-non-static-variable-what-happens-if-it-does) |
| 37  | [Can we override static variables?](#can-we-override-static-variables)                                  |
| 38  | [Can you declare a class as final?](#can-you-declare-a-class-as-final)                                  |
|     | **Constructors** |
| 39  | [What is a constructor? Give an example.](#what-is-a-constructor-give-an-example)                       |
| 40  | [What is the main purpose of a constructor?](#what-is-the-main-purpose-of-a-constructor)                |
| 41  | [When is a constructor invoked?](#when-is-a-constructor-invoked)                                        |
| 42  | [What is a default constructor?](#what-is-a-default-constructor)                                        |
| 43  | [Can a constructor return any value?](#can-a-constructor-return-any-value)                              |
| 44  | [What is constructor overloading? (multiple constructors)](#what-is-constructor-overloading-multiple-constructors) |
| 45  | [What is the difference between a constructor and a method?](#what-is-the-difference-between-a-constructor-and-a-method) |
| 46  | [What is constructor chaining?](#what-is-constructor-chaining)                                          |
| 47  | [What is a copy constructor?](#what-is-a-copy-constructor)                                              |
| 48  | [What is a destructor?](#what-is-a-destructor)                                                          |
| 49  | [Can we declare a constructor as private or final?](#can-we-declare-a-constructor-as-private-or-final)  |
| 50  | [What are the this and super keywords? How are they used in constructors?](#what-are-the-this-and-super-keywords-how-are-they-used-in-constructors) |
| 51  | [Can we call a subclass constructor from a superclass constructor?](#can-we-call-a-subclass-constructor-from-a-superclass-constructor) |
| 52  | [Is it possible to inherit a constructor?](#is-it-possible-to-inherit-a-constructor)                    |
| 53  | [Is it possible to invoke a constructor of a class more than once for an object?](#is-it-possible-to-invoke-a-constructor-of-a-class-more-than-once-for-an-object) |
|     | **Final Keyword** |
| 54  | [What is the final keyword, and where can we use it?](#what-is-the-final-keyword-and-where-can-we-use-it) |
| 55  | [What is the difference between the final method and an abstract method?](#what-is-the-difference-between-the-final-method-and-an-abstract-method) |
| 56  | [Can we declare an interface as final?](#can-we-declare-an-interface-as-final)                          |
|     | **Overloading and Overriding** |
| 57  | [What is the difference between method overloading and overriding?](#what-is-the-difference-between-method-overloading-and-overriding) |
| 58  | [What are the rules associated with overloading and overriding?](#what-are-the-rules-associated-with-overloading-and-overriding) |
| 59  | [Can we change the scope of the overridden method in the subclass?](#can-we-change-the-scope-of-the-overridden-method-in-the-subclass) |
| 60  | [What is the covariant return type?](#what-is-the-covariant-return-type)                                |
| 61  | [Can we override private methods? | static methods](#can-we-override-private-methods--static-methods)    |
|     | **Access Modifiers and Visibility** |
| 62  | [What are visibility modifiers and access modifiers? What are the scopes associated with them?](#what-are-visibility-modifiers-and-access-modifiers-what-are-the-scopes-associated-with-them) |
| 63  | [What is the access modifier scope order (lower to higher)? What is default? Diff bw protected & default?](#what-is-the-access-modifier-scope-order-lower-to-higher-what-is-default-diff-bw-protected--default) |
| 64  | [Can a constructor be declared as private? Why is it needed?](#can-a-constructor-be-declared-as-private-why-is-it-needed) |
| 65  | [Can a protected method be accessed outside the package? If yes, how?](#can-a-protected-method-be-accessed-outside-the-package-if-yes-how) |
| 66  | [Can a class be declared as private or protected? Why or why not?](#can-a-class-be-declared-as-private-or-protected-why-or-why-not) |
| 67  | [How can we restrict subclassing in Java?](#how-can-we-restrict-subclassing-in-java)                    |
| 68  | [Can an abstract method be private? Why or why not?](#can-an-abstract-method-be-private-why-or-why-not) |
|     | **Abstract Classes** |
| 69  | [Can an abstract class have a constructor?](#can-an-abstract-class-have-a-constructor)                  |
| 70  | [How do you create an abstract class?](#how-do-you-create-an-abstract-class)                            |
| 71  | [Is at least one abstract method mandatory in an abstract class? (Concrete methods)](#is-at-least-one-abstract-method-mandatory-in-an-abstract-class-concrete-methods) |
| 72  | [Can we create an object of an abstract class using the new keyword?](#can-we-create-an-object-of-an-abstract-class-using-the-new-keyword) |
| 73  | [Can final methods be present in an abstract class?](#can-final-methods-be-present-in-an-abstract-class) |
| 74  | [Can we define an abstract class as final?](#can-we-define-an-abstract-class-as-final)                  |
| 75  | [Can an abstract class have a main method in Java?](#can-an-abstract-class-have-a-main-method-in-java)  |
|     | **Inheritance & Interfaces** |
| 76  | [What is inheritance, and what are the types of inheritance?](#what-is-inheritance-and-what-are-the-types-of-inheritance) |
| 77  | [What class do all classes inherit from in Java?](#what-class-do-all-classes-inherit-from-in-java)      |
| 78  | [Why is multiple inheritance not possible in Java? (ambiguity problem)](#why-is-multiple-inheritance-not-possible-in-java-ambiguity-problem) |
| 79  | [What is the use of interfaces?](#what-is-the-use-of-interfaces)                                        |
| 80  | [How are abstraction and loose coupling achieved?](#how-are-abstraction-and-loose-coupling-achieved)    |
| 81  | [Can an interface implement another interface in Java?](#can-an-interface-implement-another-interface-in-java) |
| 82  | [What is the diamond problem in Java? How does Java's approach with interfaces solve it?](#what-is-the-diamond-problem-in-java-how-does-javas-approach-with-interfaces-solve-it) |
| 83  | [What are marker interfaces? Can you provide examples?](#what-are-marker-interfaces-can-you-provide-examples) |
| 84  | [Can interfaces have static initializer blocks or constructors?](#can-interfaces-have-static-initializer-blocks-or-constructors) |
| 85  | [Can an interface have a static variable?](#can-an-interface-have-a-static-variable)                    |
| 86  | [Can an interface have static nested interfaces?](#can-an-interface-have-static-nested-interfaces)      |
| 87  | [What is the difference between Comparable and Comparator interfaces in Java?](#what-is-the-difference-between-comparable-and-comparator-interfaces-in-java) |
| 88  | [How can you achieve callback functionality using interfaces in Java?](#how-can-you-achieve-callback-functionality-using-interfaces-in-java) |
| 89  | [What is a functional interface?](#what-is-a-functional-interface)                                      |
| 90  | [What are default and static methods? What is their use?](#what-are-default-and-static-methods-what-is-their-use) |
| 91  | [Can a default method in an interface be overridden in an implementing class?](#can-a-default-method-in-an-interface-be-overridden-in-an-implementing-class) |
| 92  | [Can an interface have fields?](#can-an-interface-have-fields)                                          |
|     | **Polymorphism** |
| 93  | [What is polymorphism? (types)](#what-is-polymorphism-types)                                            |
|     | **Enum** |
| 94  | [What is an enum? How do you declare it in Java? What are the advantages and limitations?](#what-is-an-enum-how-do-you-declare-it-in-java-what-are-the-advantages-and-limitations) |
| 95  | [Can enums have methods in Java? | constructor](#can-enums-have-methods-in-java--constructor)             |
| 96  | [Can we create an enum object using new?](#can-we-create-an-enum-object-using-new)                      |
| 97  | [How do you access enum constants and methods in Java?](#how-do-you-access-enum-constants-and-methods-in-java) |
| 98  | [Can enums implement interfaces in Java?](#can-enums-implement-interfaces-in-java)                      |
| 99  | [Can enums have abstract methods in Java?](#can-enums-have-abstract-methods-in-java)                    |
| 100 | [Can enums override methods from the java.lang.Enum class in Java?](#can-enums-override-methods-from-the-javalangenum-class-in-java) |
| 101 | [How can you serialize and deserialize enums in Java?](#how-can-you-serialize-and-deserialize-enums-in-java) |
|     | **Encapsulation** |
| 102 | [What is encapsulation, and how is it implemented? What are its benefits?](#what-is-encapsulation-and-how-is-it-implemented-what-are-its-benefits) |
|     | **Collections and Data Structures** |
| 103 | [Explain the internal working of Array and ArrayList.](#explain-the-internal-working-of-array-and-arraylist) |
| 104 | [LinkedList, Circular Linked List implementations, and when to use what.](#linkedlist-circular-linked-list-implementations-and-when-to-use-what) |
| 105 | [What are Collection Mappings supported by JPA?](#what-are-collection-mappings-supported-by-jpa)        |
| 106 | [Comparable and Comparator differences.](#comparable-and-comparator-differences)                        |
| 107 | [Given an Employee class (id, name, salary): Sort by salary (then name if same) using Java 8.](#given-an-employee-class-id-name-salary-sort-by-salary-then-name-if-same-using-java-8) |
| 108 | [Given an Employee class: Store in a map ensuring no duplicates.](#given-an-employee-class-store-in-a-map-ensuring-no-duplicates) |
| 109 | [What is an iterator and why is it used? When to use, where is it present?](#what-is-an-iterator-and-why-is-it-used-when-to-use-where-is-it-present) |
| 110 | [How does HashMap work internally in Java?](#how-does-hashmap-work-internally-in-java)                 |
| 111 | [HashSet internal working.](#hashset-internal-working)                                                  |
| 112 | [HashMap vs ConcurrentHashMap.](#hashmap-vs-concurrenthashmap)                                          |
| 113 | [HashSet vs LinkedHashSet.](#hashset-vs-linkedhashset)                                                  |
| 114 | [ArrayList vs HashSet.](#arraylist-vs-hashset)                                                          |
| 115 | [Vector features.](#vector-features)                                                                    |
|     | **Multithreading and Concurrency** |
| 116 | [What is latching with respect to multithreading?](#what-is-latching-with-respect-to-multithreading)    |
| 117 | [What is synchronization in Java?](#what-is-synchronization-in-java)                                    |
| 118 | [How to achieve thread safety apart from using synchronization? (Semaphore & Mutex)](#how-to-achieve-thread-safety-apart-from-using-synchronization-semaphore--mutex) |
| 119 | [What is a deadlock situation in a Java program?](#what-is-a-deadlock-situation-in-a-java-program)      |
| 120 | [Why are wait(), notify(), and notifyAll() in Object class and not in Thread class?](#why-are-wait-notify-and-notifyall-in-object-class-and-not-in-thread-class) |
| 121 | [Explain ExecutorService framework. submit vs execute?](#explain-executorservice-framework-submit-vs-execute) |
| 122 | [What is the need for the Executor framework?](#what-is-the-need-for-the-executor-framework)            |
| 123 | [What is CompletableFuture? How is it different from Future?](#what-is-completablefuture-how-is-it-different-from-future) |
| 124 | [How to create a thread pool?](#how-to-create-a-thread-pool)                                            |
| 125 | [Use of Thread dump.](#use-of-thread-dump)                                                              |
| 126 | [Process vs Thread.](#process-vs-thread)                                                                |
| 127 | [How to analyze thread dump.](#how-to-analyze-thread-dump)                                              |
| 128 | [Callable and Future in ExecutorServices.](#callable-and-future-in-executorservices)                    |
| 129 | [Race Condition in threads.](#race-condition-in-threads)                                                |
|     | **Synchronization (Detailed)** |
| 130 | [Synchronization definition and purpose.](#synchronization-definition-and-purpose)                      |
| 131 | [What is a critical section?](#what-is-a-critical-section)                                              |
|     | **Memory and Initialization** |
| 132 | [What are the memory types, and what are the GC methods? (Revisit GC Section)](#what-are-the-memory-types-and-what-are-the-gc-methods-revisit-gc-section) |
| 133 | [Static loading and initialization phase.](#static-loading-and-initialization-phase)                    |
| 134 | [Invoking a constructor.](#invoking-a-constructor)                                                      |
| 135 | [Non-static loading and initialization phase.](#non-static-loading-and-initialization-phase)            |
|     | **Factory Methods** |
| 136 | [What are factory methods in Java?](#what-are-factory-methods-in-java)                                  |
|     | **Miscellaneous** |
| 137 | [What are this, super keywords? Using the same constructors? (Revisit Constructor Section)](#what-are-this-super-keywords-using-the-same-constructors-revisit-constructor-section) |
| 138 | [Implicit casting / upcasting vs Explicit casting / downcasting.](#implicit-casting--upcasting-vs-explicit-casting--downcasting) |
| 139 | [Using String... args vs. String[] args in the main method.](#using-string-args-vs-string-args-in-the-main-method) |
| 140 | [Steps to create an immutable class.](#steps-to-create-an-immutable-class)                              |
| 141 | [Static binding vs Dynamic binding.](#static-binding-vs-dynamic-binding)                                |
| 142 | [Transient keyword.](#transient-keyword)                                                                |
| 143 | [What is PostConstruct?](#what-is-postconstruct)                                                        |
|     | **Exception Handling** |
| 144 | [What is exception propagation in Java?](#what-is-exception-propagation-in-java)                        |
| 145 | [What is BufferUnderflowException?](#what-is-bufferunderflowexception)                                  |
| 146 | [Explain try-with-resources. Why don’t we need a finally block here?](#explain-try-with-resources-why-dont-we-need-a-finally-block-here) |
| 147 | [Checked Exception vs Unchecked Exception.](#checked-exception-vs-unchecked-exception)                  |
|     | **Java 8 and Functional Programming** |
| 148 | [What are Java 8 features?](#what-are-java-8-features)                                                  |
| 149 | [Explain the internal functioning of streams.](#explain-the-internal-functioning-of-streams)            |
| 150 | [How do you implement a functional interface?](#how-do-you-implement-a-functional-interface)            |
| 151 | [Random generation in streams.](#random-generation-in-streams)                                          |
|     | **Garbage Collection** |
| 152 | [What is Garbage Collection, and why is it needed in Java?](#what-is-garbage-collection-and-why-is-it-needed-in-java) |
| 153 | [How can we manually request Garbage Collection, and is it recommended?](#how-can-we-manually-request-garbage-collection-and-is-it-recommended) |
| 154 | [How does finalize() work, and is it guaranteed to execute? When does GC call finalize()?](#how-does-finalize-work-and-is-it-guaranteed-to-execute-when-does-gc-call-finalize) |
| 155 | [What are the different Garbage Collectors available in Java? Different ways to call GC?](#what-are-the-different-garbage-collectors-available-in-java-different-ways-to-call-gc) |
| 156 | [What are Strong, Soft, Weak, and Phantom References in Java?](#what-are-strong-soft-weak-and-phantom-references-in-java) |
| 157 | [How to make an object eligible for Garbage Collection?](#how-to-make-an-object-eligible-for-garbage-collection) |
| 158 | [Differentiate between Heap vs Stack Memory in Java? Different parts of the heap?](#differentiate-between-heap-vs-stack-memory-in-java-different-parts-of-the-heap) |
| 159 | [Which part of the memory is involved in Garbage Collection? Stack or Heap?](#which-part-of-the-memory-is-involved-in-garbage-collection-stack-or-heap) |
| 160 | [How do you identify minor and major garbage collection in Java?](#how-do-you-identify-minor-and-major-garbage-collection-in-java) |
| 161 | [What is the algorithm for garbage collection in Java?](#what-is-the-algorithm-for-garbage-collection-in-java) |
|     | **JVM Internals** |
| 162 | [(Refer to external resources/notes for JVM Internals)](#refer-to-external-resourcesnotes-for-jvm-internals) |
|     | **Generics** |
| 163 | [(Refer to external resources/notes for Generics)](#refer-to-external-resourcesnotes-for-generics)      |
|     | **Reflection API** |
| 164 | [(Refer to external resources/notes for Reflection API)](#refer-to-external-resourcesnotes-for-reflection-api) |
|     | **Spring Framework** |
| 165 | [Bean life cycle in Spring.](#bean-life-cycle-in-spring)                                                |
| 166 | [@Qualifier and @Required.](#qualifier-and-required)                                                    |
| 167 | [What are actuators, and which have you used?](#what-are-actuators-and-which-have-you-used)             |
| 168 | [How to handle global exceptions in Spring Boot.](#how-to-handle-global-exceptions-in-spring-boot)      |
| 169 | [How to access other .properties files in Spring Boot.](#how-to-access-other-properties-files-in-spring-boot) |
| 170 | [How to rename application.properties and use a custom properties file.](#how-to-rename-applicationproperties-and-use-a-custom-properties-file) |
| 171 | [What is advice in AOP?](#what-is-advice-in-aop)                                                        |
| 172 | [ApplicationContext vs WebApplicationContext.](#applicationcontext-vs-webapplicationcontext)            |
| 173 | [Scopes in Spring.](#scopes-in-spring)                                                                  |
| 174 | [Spring Data JPA flow.](#spring-data-jpa-flow)                                                          |
| 175 | [Spring design patterns.](#spring-design-patterns)                                                      |
| 176 | [How to create a composite key in Spring Data JPA.](#how-to-create-a-composite-key-in-spring-data-jpa)  |
| 177 | [What are pointcuts in AOP?](#what-are-pointcuts-in-aop)                                                |
|     | **Microservices** |
| 178 | [Microservices flow.](#microservices-flow)                                                              |
| 179 | [API Gateway internal working.](#api-gateway-internal-working)                                          |
| 180 | [How to communicate between microservices.](#how-to-communicate-between-microservices)                  |
| 181 | [In which area are you using AMQP?](#in-which-area-are-you-using-amqp)                                  |
|     | **SQL and Database** |
| 182 | [Use of index in MySQL.](#use-of-index-in-mysql)                                                        |
| 183 | [What is a trigger and how does it work?](#what-is-a-trigger-and-how-does-it-work)                      |
| 184 | [Drop vs Delete vs Truncate in MySQL.](#drop-vs-delete-vs-truncate-in-mysql)                            |
| 185 | [Self join in MySQL.](#self-join-in-mysql)                                                              |
| 186 | [What is an outer join?](#what-is-an-outer-join)                                                        |
| 187 | [Use of GROUP BY in MySQL.](#use-of-group-by-in-mysql)                                                  |
| 188 | [Functions vs Procedures in MySQL.](#functions-vs-procedures-in-mysql)                                  |
| 189 | [Primary key vs Composite key.](#primary-key-vs-composite-key)                                          |
|     | **Web Concepts** |
| 190 | [HTTPS verbs.](#https-verbs)                                                                            |
| 191 | [Post vs Put.](#post-vs-put)                                                                            |
|     | **System/Ops** |
| 192 | [How to delete files older than 30 days in Linux.](#how-to-delete-files-older-than-30-days-in-linux)    |
| 193 | [How to implement a scheduler lock.](#how-to-implement-a-scheduler-lock)                                |
|     | **General OOP/Java Concepts** |
| 194 | [Use of Abstraction.](#use-of-abstraction)                                                              |
| 195 | [Encapsulation vs Abstraction.](#encapsulation-vs-abstraction)                                          |
| 196 | [Setter vs Constructor for initialization.](#setter-vs-constructor-for-initialization)                  |
| 197 | ["12".equals(12) – What happened?](#12equals12--what-happened)                                          |


---

## Answers

### Why is Java not 100% object-oriented?
ans

### **Java Basics**
<br>

### What are the features of Java?
ans

### What do you mean by platform independence?
ans

### What is the difference between JDK, JRE, and JVM?
ans

### What are the jobs of JVM?
ans
*(Includes: Providing Runtime Environment, Loading/Linking/Initializing Classes, Bytecode Execution, Memory Management (heap, stack), Garbage Collection, Thread Management)*

### What are the memory areas available in Java?
ans

### What is garbage collection in Java? What methods are related to GC?
ans

### What is a package in Java?
ans

### What are the different data types in Java?
ans

### What are string literals?
ans

### What is the difference between declaring, defining, and initializing a value?
ans

### What is the difference between a compiler, an interpreter, and bytecode?
ans

### How many different ways are there to take input in Java?
ans

### Can I import the same package/class twice? Will the JVM load the package twice at runtime?
ans

### **Unicode and Identifiers**
<br>

### What is Unicode, and which Unicode system does Java use?
ans

### Which non-Unicode characters can be used as the first character of an identifier?
ans

### **Primitive Types and Wrapper Classes**
<br>

### What are primitive types and their wrapper classes in Java?
ans

### What methods are inside primitive and wrapper classes?
ans

### What is autoboxing and unboxing?
ans

### **Casting and Cloning**
<br>

### What is casting (implicit and explicit)?
ans

### Different types of cloning available in Java? Difference between deep cloning and shallow cloning?
ans

### **Strings and String Pool**
<br>

### Why is String not mutable, and how to develop a mutable String in Java?
ans

### What is the difference between String, StringBuilder, and StringBuffer?
ans

### What is the String constant pool?
ans

### String s_1 = "anil"; vs. String s_2 = new String("anil");
ans

### **Operators and Comparisons**
<br>

### What is the difference between == and .equals?
ans

### How do you convert a String to an int (parse)?
ans

### **Singleton and Static**
<br>

### What is a singleton class in Java?
ans

### What is static in Java?
ans

### Why is the main method static in Java?
ans

### Can we run main() without the static keyword?
ans

### Can we run a Java class without a main method?
ans

### Can you declare the main method as final or private?
ans

### Does 'main' a keyword in Java? What are other identifiers?
ans

### What if we write static public void instead of public static void?
ans

### Can a static reference a non-static variable? What happens if it does?
ans

### Can we override static variables?
ans

### Can you declare a class as final?
ans

### **Constructors**
<br>

### What is a constructor? Give an example.
ans

### What is the main purpose of a constructor?
ans

### When is a constructor invoked?
ans

### What is a default constructor?
ans

### Can a constructor return any value?
ans

### What is constructor overloading? (multiple constructors)
ans

### What is the difference between a constructor and a method?
ans

### What is constructor chaining?
ans

### What is a copy constructor?
ans

### What is a destructor?
ans

### Can we declare a constructor as private or final?
ans

### What are the this and super keywords? How are they used in constructors?
ans

### Can we call a subclass constructor from a superclass constructor?
ans

### Is it possible to inherit a constructor?
ans

### Is it possible to invoke a constructor of a class more than once for an object?
ans

### **Final Keyword**
<br>

### What is the final keyword, and where can we use it?
ans

### What is the difference between the final method and an abstract method?
ans

### Can we declare an interface as final?
ans

### **Overloading and Overriding**
<br>

### What is the difference between method overloading and overriding?
ans

### What are the rules associated with overloading and overriding?
ans

### Can we change the scope of the overridden method in the subclass?
ans

### What is the covariant return type?
ans

### Can we override private methods? | static methods
ans

### **Access Modifiers and Visibility**
<br>

### What are visibility modifiers and access modifiers? What are the scopes associated with them?
ans

### What is the access modifier scope order (lower to higher)? What is default? Diff bw protected & default?
ans

### Can a constructor be declared as private? Why is it needed?
ans

### Can a protected method be accessed outside the package? If yes, how?
ans

### Can a class be declared as private or protected? Why or why not?
ans

### How can we restrict subclassing in Java?
ans

### Can an abstract method be private? Why or why not?
ans

### **Abstract Classes**
<br>

### Can an abstract class have a constructor?
ans

### How do you create an abstract class?
ans

### Is at least one abstract method mandatory in an abstract class? (Concrete methods)
ans

### Can we create an object of an abstract class using the new keyword?
ans

### Can final methods be present in an abstract class?
ans

### Can we define an abstract class as final?
ans

### Can an abstract class have a main method in Java?
ans

### **Inheritance & Interfaces**
<br>

### What is inheritance, and what are the types of inheritance?
ans

### What class do all classes inherit from in Java?
ans

### Why is multiple inheritance not possible in Java? (ambiguity problem)
ans

### What is the use of interfaces?
ans

### How are abstraction and loose coupling achieved?
ans
*(Includes: Runtime polymorphism, high cohesion, is-a relationship)*

### Can an interface implement another interface in Java?
ans

### What is the diamond problem in Java? How does Java's approach with interfaces solve it?
ans

### What are marker interfaces? Can you provide examples?
ans

### Can interfaces have static initializer blocks or constructors?
ans

### Can an interface have a static variable?
ans

### Can an interface have static nested interfaces?
ans

### What is the difference between Comparable and Comparator interfaces in Java?
ans

### How can you achieve callback functionality using interfaces in Java?
ans

### What is a functional interface?
ans

### What are default and static methods? What is their use?
ans

### Can a default method in an interface be overridden in an implementing class?
ans

### Can an interface have fields?
ans

### **Polymorphism**
<br>

### What is polymorphism? (types)
ans

### **Enum**
<br>

### What is an enum? How do you declare it in Java? What are the advantages and limitations?
ans

### Can enums have methods in Java? | constructor
ans

### Can we create an enum object using new?
ans

### How do you access enum constants and methods in Java?
ans

### Can enums implement interfaces in Java?
ans

### Can enums have abstract methods in Java?
ans

### Can enums override methods from the java.lang.Enum class in Java?
ans

### How can you serialize and deserialize enums in Java?
ans

### **Encapsulation**
<br>

### What is encapsulation, and how is it implemented? What are its benefits?
ans

### **Collections and Data Structures**
<br>

### Explain the internal working of Array and ArrayList.
ans

### LinkedList, Circular Linked List implementations, and when to use what.
ans

### What are Collection Mappings supported by JPA?
ans

### Comparable and Comparator differences.
ans

### Given an Employee class (id, name, salary): Sort by salary (then name if same) using Java 8.
ans

### Given an Employee class: Store in a map ensuring no duplicates.
ans

### What is an iterator and why is it used? When to use, where is it present?
ans

### How does HashMap work internally in Java?
ans

### HashSet internal working.
ans

### HashMap vs ConcurrentHashMap.
ans

### HashSet vs LinkedHashSet.
ans

### ArrayList vs HashSet.
ans

### Vector features.
ans

### **Multithreading and Concurrency**
<br>

### What is latching with respect to multithreading?
ans

### What is synchronization in Java?
ans

### How to achieve thread safety apart from using synchronization? (Semaphore & Mutex)
ans

### What is a deadlock situation in a Java program?
ans

### Why are wait(), notify(), and notifyAll() in Object class and not in Thread class?
ans

### Explain ExecutorService framework. submit vs execute?
ans

### What is the need for the Executor framework?
ans

### What is CompletableFuture? How is it different from Future?
ans

### How to create a thread pool?
ans

### Use of Thread dump.
ans

### Process vs Thread.
ans

### How to analyze thread dump.
ans

### Callable and Future in ExecutorServices.
ans

### Race Condition in threads.
ans

### **Synchronization (Detailed)**
<br>

### Synchronization definition and purpose.
ans
*(Includes: handling resource accessibility, avoiding thread interference)*

### What is a critical section?
ans
*(Includes: synchronized keyword)*

### **Memory and Initialization**
<br>

### What are the memory types, and what are the GC methods? (Revisit GC Section)
ans

### Static loading and initialization phase.
ans

### Invoking a constructor.
ans

### Non-static loading and initialization phase.
ans

### **Factory Methods**
<br>

### What are factory methods in Java?
ans

### **Miscellaneous**
<br>

### What are this, super keywords? Using the same constructors? (Revisit Constructor Section)
ans

### Implicit casting / upcasting vs Explicit casting / downcasting.
ans
*(Includes: lower to higher, higher to lower, data loss)*

### Using String... args vs. String[] args in the main method.
ans

### Steps to create an immutable class.
ans

### Static binding vs Dynamic binding.
ans

### Transient keyword.
ans

### What is PostConstruct?
ans

### **Exception Handling**
<br>

### What is exception propagation in Java?
ans

### What is BufferUnderflowException?
ans

### Explain try-with-resources. Why don’t we need a finally block here?
ans

### Checked Exception vs Unchecked Exception.
ans

### **Java 8 and Functional Programming**
<br>

### What are Java 8 features?
ans

### Explain the internal functioning of streams.
ans

### How do you implement a functional interface?
ans

### Random generation in streams.
ans

### **Garbage Collection**
<br>

### What is Garbage Collection, and why is it needed in Java?
ans

### How can we manually request Garbage Collection, and is it recommended?
ans

### How does finalize() work, and is it guaranteed to execute? When does GC call finalize()?
ans

### What are the different Garbage Collectors available in Java? Different ways to call GC?
ans

### What are Strong, Soft, Weak, and Phantom References in Java?
ans

### How to make an object eligible for Garbage Collection?
ans

### Differentiate between Heap vs Stack Memory in Java? Different parts of the heap?
ans

### Which part of the memory is involved in Garbage Collection? Stack or Heap?
ans

### How do you identify minor and major garbage collection in Java?
ans

### What is the algorithm for garbage collection in Java?
ans

### **JVM Internals**
<br>

### (Refer to external resources/notes for JVM Internals)
ans
*(Note: This topic is broad; linking to external detailed notes/articles like the one provided is practical.)*

### **Generics**
<br>

### (Refer to external resources/notes for Generics)
ans
*(Note: This topic is broad; linking to external detailed notes/articles is practical.)*

### **Reflection API**
<br>

### (Refer to external resources/notes for Reflection API)
ans
*(Note: This topic is broad; linking to external detailed notes/articles like the one provided is practical.)*

### **Spring Framework**
<br>

### Bean life cycle in Spring.
ans

### @Qualifier and @Required.
ans

### What are actuators, and which have you used?
ans

### How to handle global exceptions in Spring Boot.
ans

### How to access other .properties files in Spring Boot.
ans

### How to rename application.properties and use a custom properties file.
ans

### What is advice in AOP?
ans

### ApplicationContext vs WebApplicationContext.
ans

### Scopes in Spring.
ans

### Spring Data JPA flow.
ans

### Spring design patterns.
ans

### How to create a composite key in Spring Data JPA.
ans

### What are pointcuts in AOP?
ans

### **Microservices**
<br>

### Microservices flow.
ans

### API Gateway internal working.
ans

### How to communicate between microservices.
ans

### In which area are you using AMQP?
ans

### **SQL and Database**
<br>

### Use of index in MySQL.
ans

### What is a trigger and how does it work?
ans

### Drop vs Delete vs Truncate in MySQL.
ans

### Self join in MySQL.
ans

### What is an outer join?
ans

### Use of GROUP BY in MySQL.
ans

### Functions vs Procedures in MySQL.
ans

### Primary key vs Composite key.
ans

### **Web Concepts**
<br>

### HTTPS verbs.
ans

### Post vs Put.
ans

### **System/Ops**
<br>

### How to delete files older than 30 days in Linux.
ans

### How to implement a scheduler lock.
ans

### **General OOP/Java Concepts**
<br>

### Use of Abstraction.
ans

### Encapsulation vs Abstraction.
ans

### Setter vs Constructor for initialization.
ans

### "12".equals(12) – What happened?
ans