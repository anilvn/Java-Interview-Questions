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