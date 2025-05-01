### readfile ex
```java
// byte strams.
import java.io.*;

public class Demo {
    public static void main(String[] args) throws IOException {
        /*File f = new File("test.txt")

        System.out.println(f.exists());//false //true
        System.out.println(f.createNewFile());//true //false
        System.out.println(f.exists());//true //true

        FileOutputStream fos = new FileOutputStream(f);
        FileOutputStream fos = new FileOutputStream("test1.txt");
        FileOutputStream fos = new FileOutputStream("test1.txt", true) ;*/
        
        FileOutputStream fos = new FileOutputStream("test1.txt", false);
        fos.write(new byte[]{100, 97, 100});
        fos.write(32);
        fos.write(new byte[]{65, 66, 67, 68, 69, 70}, 2, 4);
        fos.write(32);

    }
}

// char strams
package com.kit;

import java.io.FileInputStream;
import java.io.IOException;

public class Demo {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("test.txt");
        // File f1 = new File("C:\\Users\\admin\\Desktop\\Demo.java");          // Corrected path
        //                   ("C:/Users/admin/Desktop/Demo.java")              //  Alternatively you can
        int i = 0;
        while ((i = fis.read()) != -1) {
            System.out.print((char) i);
        }
        fis.close(); // Close the FileInputStream when done
    }
}
```
### wkg with File Decripter
```java
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

public class Demo {
    public static void main(String[] args) throws IOException {
        File f = new File("check.txt");
        FileOutputStream fos = new FileOutputStream(f);
        FileDescriptor fd = fos.getFD();
        FileOutputStream fos1 = new FileOutputStream(fd);
        fos1.write("hello naresh it".getBytes());
    }
}
```
###  OOS   &   OIS
```java

//  OOS   &   OIS
import java.io.*;

class Student implements Serializable {
    String name = "Anil";
    int sage = 29;
    double sfee = 2500;
    String scourse = "java";
    String insName = "NIT";

    static class SAndD {
        public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
            FileOutputStream fos = new FileOutputStream("des");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            Student s = new Student();
            oos.writeObject(s);
            oos.close();

            FileInputStream fis = new FileInputStream("des");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            Student s1 = (Student) o;
            System.out.println(s1.sage);
            ois.close();
        }
    }
}
```


### data streams
```java

// Data streams

import java.io.*;

public class Demo {
    public static void main(String[] args) throws IOException {
        File file = new File("test2.txt");
        FileOutputStream fos = new FileOutputStream(file);
        DataOutputStream dos = new DataOutputStream(fos);
        dos.write(100);
        dos.writeInt(200);
        dos.writeDouble(12.23d);
        dos.writeChar('a');
        dos.writeUTF("ramchandra");
        dos.writeBoolean(true);
        
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);

        System.out.println(dis.read());
        System.out.println(dis.readInt());
        System.out.println(dis.readDouble());
        System.out.println(dis.readChar());
        System.out.println(dis.readUTF());
        System.out.println(dis.readBoolean());

        dis.close(); // Close the streams when done
        dos.close();
    }
}

```


### Restricting  few feilds not to participate in Serialization Using Transient keyword.

``` java

import java.io.*;

public class Employee implements Serializable {
    int eid = 101;
    String ename = "ramchandar";
    int esal = 7000;
    int eage = 30;
    String epassword = "ram123java";

    //not participated in serialization.. will be null.
    transient String epassword = "ram123java"; 

    /*
    private void writeObject(ObjectOutputStream oos) throws Exception {
        System.out.println("private writeobject method");
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        System.out.println("private readobject method");
        ois.defaultReadObject();
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        System.out.println("private readobject method");
        ois.defaultReadObject();
        String s = (String) ois.readObject();
        System.out.println("s: " + s);
    }

    */

    private void writeObject(ObjectOutputStream oos) throws Exception {
        System.out.println("private writeObject method");
        oos.defaultWriteObject();
        oos.writeObject("sbiicicinitram123kitjava.netpassword");
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        System.out.println("private readobject method");
        ois.defaultReadObject();
        String s = (String) ois.readObject();
        String s1 = s.substring(11, 17); // 11 16
        String s2 = s.substring(20, 24); // 20 23
        epassword = s1 + "$2";
    }
}

```

### externalization.
```java


//externalization.

import java.io.*;

class ExDemo {
    public static void main(String[] args) throws IOException {
        File f = new File("emp.ext");
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        Employee e = new Employee(101, "ram", 7000.00, "dev", "amt");
        oos.writeObject(e);
        System.out.println(" completed");
    }

    /*
    @Override
    public void writeExternal(ObjectOutput oo) throws IOException {
        System.out.println("writeExternal method executing");
    }

    @Override
    public void readExternal(ObjectInput oi) throws IOException {
        System.out.println("readExternal method executing");
    }

    */

    @Override
    public void writeExternal(ObjectOutput oo) throws IOException {
        System.out.println("writeExternal method executing");
        oo.writeObject(ename);
        oo.writeObject(edeptname);
    }

    @Override
    public void readExternal(ObjectInput oi) throws IOException {
        System.out.println("readExternal method executing");
        ename = (String) oi.readObject();
    }

}


```

### wkg with serializable and inheritance.

```java

// wkg with serializable and inheritance.


import java.io.*;
class C implements Serializable {
            String s = "NIT";
}

class B implements Serializable {
        C obj3 = new C();
}

class A implements Serializable {
    B obj2 = new B();
}

public class SHDemo {
    public static void main(String[] args) throws IOException {
        File f = new File("test.txt");
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        A obj = new A();
        oos.writeObject(obj);
        System.out.println("serialization completed");
        
        File f1 = new File("test.txt");
        FileInputStream fis = new FileInputStream(f1);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object o = ois.readObject();
    }
}

```


### ByteArrayOutputStream
```java

/**             java.io.ByteArrayOutputStream
 * 
 *              It is useful for sending the same data from java application to multiple times at a time.
 */ 
import java.io.*;

public class BAOSDemo {
    public static void main(String[] args) throws IOException {
        FileOutputStream fos1 = new FileOutputStream("test1.txt");
        FileOutputStream fos2 = new FileOutputStream("test2.txt");
        FileOutputStream fos3 = new FileOutputStream("test3.txt");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(new byte[]{100, 97, 100, 32}); // d
        bos.write(new byte[]{109, 111, 109});
        
        bos.writeTo(fos1);
        bos.writeTo(fos2);
        bos.writeTo(fos3);
        
        System.out.println("process completed");
    }
}

```

### SequenceInputStream
```java

// wkg with SequenceInputStream

import java.io.*;

public class Student implements java.io.Serializable {
    private static final long serialVersionUID = 900L;
    int sid = 101;
    String sname = "varun";
    int sage = 25;
    String scourse = "java";
}

public class SISDemo {
    public static void main(String[] s) throws IOException {
        File f1 = new File("t1.txt");
        File f2 = new File("t2.txt");

        FileInputStream fis1 = new FileInputStream(f1);
        FileInputStream fis2 = new FileInputStream(f2);

        SequenceInputStream sis = new SequenceInputStream(fis2, fis1);
        int i = 0;
        while ((i = sis.read()) != -1) {
            System.out.print((char) i);
        }
    }
}
```

### wkg with File Writer
```java

//wkg with File Writer
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FWDemo {
    public static void main(String[] args) throws IOException {
        File f = new File("test.txt");
        FileWriter fw = new FileWriter(f);
        char[] c = {'n', 'i', 't'};
        fw.write(c);
        fw.write(32);
        fw.write("karthikit");
        fw.write(32);
        char[] c1 = {'a', 'b', 'c', 'd', 'e', 'f'};
        fw.write(c1, 2, 3);
        fw.write(32);
        fw.write("internationalization", 5, 6);
        fw.flush();
        fw.close();
    }
}
```
### wkg with File Reader
```java

//wkg with File Reader

c class FRDemo {
ublic static void main(String[] args) throws IOException{
File f = new File("test.txt");
FileReader fr = new FileReader(f);
int i =0;
while((i=fr.read()) !=- 1){
System.out.print((char)i);

System.out.println("\nprogram ends");


import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Test {
    public static void main(String[] args) { //throws F
        File f = new File("test.txt");
        try (FileReader fr = new FileReader(f)) {
            int i = 0;
            while ((i = fr.read()) != -1) {
                System.out.print((char) i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```



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