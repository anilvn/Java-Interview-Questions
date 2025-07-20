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

