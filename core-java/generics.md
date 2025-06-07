

### Example: Simple Generic Functional Interface

```java
@FunctionalInterface
interface I<T> {
    T m1(T x);
}

public class Main {
    public static void main(String[] args) {
        // Using the generic interface with Integer type
        I<Integer> obj1 = (x) -> {
            System.out.println("Lambda-Integer: " + x);
            return x;
        };
        System.out.println(obj1.m1(999));  
        /*
           Output:
           Lambda-Integer: 999
           999
        */

        // Using the same interface with String type
        I<String> obj2 = (String x) -> {
            System.out.println("Lambda-String: " + x);
            return "Hello " + x;
        };
        System.out.println(obj2.m1("world"));  
        /*
           Output:
           Lambda-String: world
           Hello world
        */
    }
}
```

### Example: Generic Class with Type Parameter

```java
public class Box<T> {
    private T data;

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}

public class Main {
    public static void main(String[] args) {
        // Using the generic Box with Integer type
        Box<Integer> obj = new Box<>();
        obj.setData(19);
        System.out.println(obj.getData());  // Output: 19
        
        // Using the same Box class with String type
        Box<String> strBox = new Box<>();
        strBox.setData("Hello Generic");
        System.out.println(strBox.getData());  // Output: Hello Generic
    }
}
```

[Back to Top](#table-of-contents)


## Generic Functional Interfaces

Generic functional interfaces allow for type-safe operations with different data types while maintaining the single abstract method constraint.

### Key Characteristics:

* Work with different data types
* Enforce type safety at compile time
* Improve code reusability
* Enhance readability
* Allow specifying types at instantiation

[Back to Top](#table-of-contents)
