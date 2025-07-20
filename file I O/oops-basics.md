

```java

/*
what is object?
real time world existing thing. has haracterstics
->  Behaviour       :     differentiate one object to another
->  Properties      :     hold the data accNo, accHolderName, pin
->  functionalities :     doing some operation on propertis
                          withdrawl()
                          deposit()
                          balance enquiry()
*/





class SuperClass {
    // Static variable
    static int staticVar = 10;
    // Instance variable
    int instanceVar = 20;

    // Static method
    static void staticMethod() {
        System.out.println("Static method in SuperClass");
    }

    // Instance method
    void instanceMethod() {
        System.out.println("Instance method in SuperClass");
    }
}
class SubClass extends SuperClass {
    
    // Static variable (hidden, not overridden)
    static int staticVar = 30;
    // Instance variable (hidden, not overridden)
    int instanceVar = 40;

    // Static method (hidden, not overridden)
    static void staticMethod() {
        System.out.println("Static method in SubClass");
    }

    // Instance method (overridden)
    @Override
    void instanceMethod() {
        System.out.println("Instance method in SubClass");
    }
}


public class Main {
    public static void main(String[] args) {
        // Upcasting
        SuperClass superClassRef = new SubClass();

        // Accessing static variable
        System.out.println("Static Variable: " + superClassRef.staticVar); // Output: 10 (SuperClass static variable)

        // Accessing instance variable
        System.out.println("Instance Variable: " + superClassRef.instanceVar); // Output: 20 (SuperClass instance variable)

        // Calling static method
        superClassRef.staticMethod(); // Output: Static method in SuperClass (SuperClass static method)

        // Calling instance method
        superClassRef.instanceMethod(); // Output: Instance method in SubClass (SubClass instance method)
        
        System.out.println("-------------------------------------------------------------------------------");
        
        
        
        
        
        SubClass subClassRef=(SubClass)superClassRef ;
        
                // Accessing static variable
        System.out.println("Static Variable: " + subClassRef.staticVar); // Output: 10 (SuperClass static variable)

        // Accessing instance variable
        System.out.println("Instance Variable: " + subClassRef.instanceVar); // Output: 20 (SuperClass instance variable)

        // Calling static method
        subClassRef.staticMethod(); // Output: Static method in SuperClass (SuperClass static method)

        // Calling instance method
        subClassRef.instanceMethod(); // Output: Instance method in SubClass (SubClass instance method)

        // Obj is created using the Subclass, later it get upcasted.
        // Access using subclass reference
        System.out.println("Static Variable (SubClass): " + subClassRef.staticVar); // Output: 30
        System.out.println("Instance Variable (SubClass): " + subClassRef.instanceVar); // Output: 40
        subClassRef.staticMethod(); // Output: Static method in SubClass
        subClassRef.instanceMethod(); // Output: Instance method in SubClass

        // Upcasting to superclass reference
        SuperClass superClassRef = subClassRef;

        // Access using superclass reference
        System.out.println("Static Variable (SuperClass): " + superClassRef.staticVar); // Output: 10
        System.out.println("Instance Variable (SuperClass): " + superClassRef.instanceVar); // Output: 20
        superClassRef.staticMethod(); // Output: Static method in SuperClass
        superClassRef.instanceMethod(); // Output: Instance method in SubClass
    }
}
        
    
```