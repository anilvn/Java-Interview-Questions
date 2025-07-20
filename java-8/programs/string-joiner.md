
```java
import java.util.*;
import java.util.stream.*;

public class SplitJoinSingleExample {
    public static void main(String[] args) {
        String data = "apple,banana,cherry";

        // Split by comma, then join using semicolon as delimiter
        String result = Arrays.stream(data.split(","))
                              .collect(Collectors.joining(";"));

        System.out.println(result); 
        // Output: apple;banana;cherry

        /*
         * ✏ Alternatives you can pass in Collectors.joining(...) :
         *
         * 1️⃣ Collectors.joining(",")      → apple,banana,cherry
         * 2️⃣ Collectors.joining("-")      → apple-banana-cherry
         * 3️⃣ Collectors.joining(" ")      → apple banana cherry
         * 4️⃣ Collectors.joining(" | ")    → apple | banana | cherry
         * 5️⃣ Collectors.joining("")       → applebananacherry (no separator)
         *
         * Advanced:
         * 6️⃣ Collectors.joining(",", "[", "]") 
         *     → adds prefix and suffix → [apple,banana,cherry]
         *
         * Signature of joining:
         *   - joining(CharSequence delimiter)
         *   - joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix)
         *
         * So you can customize the output to look however you want!
         */
    }
}
```


```java
import java.util.*;
import java.util.stream.*;

public class SplitExamples {
    public static void main(String[] args) {
        // ✅ Split by multiple spaces
        String data1 = "hello   world    from    chatgpt";
        List<String> list1 = Arrays.stream(data1.split("\\s+"))
                                   .collect(Collectors.toList());
        System.out.println(list1); 
        // Output: [hello, world, from, chatgpt]

        // ✅ Split by comma
        String data2 = "apple,banana,cherry";
        List<String> list2 = Arrays.stream(data2.split(","))
                                   .collect(Collectors.toList());
        System.out.println(list2); 
        // Output: [apple, banana, cherry]

        // ✅ Split by semicolon
        String data3 = "dog;cat;lion;tiger";
        List<String> list3 = Arrays.stream(data3.split(";"))
                                   .collect(Collectors.toList());
        System.out.println(list3); 
        // Output: [dog, cat, lion, tiger]

        // ✅ Split by colon
        String data4 = "user:password:email:phone";
        List<String> list4 = Arrays.stream(data4.split(":"))
                                   .collect(Collectors.toList());
        System.out.println(list4); 
        // Output: [user, password, email, phone]

        // ✅ Split by hash #
        String data5 = "red#green#blue#yellow";
        List<String> list5 = Arrays.stream(data5.split("#"))
                                   .collect(Collectors.toList());
        System.out.println(list5); 
        // Output: [red, green, blue, yellow]

        // ✅ Split by digit(s)
        String data6 = "part1section2chapter3";
        List<String> list6 = Arrays.stream(data6.split("\\d+"))
                                   .collect(Collectors.toList());
        System.out.println(list6); 
        // Output: [part, section, chapter, ]

        // ✅ Split by comma OR semicolon
        String data7 = "red,blue;green,yellow";
        List<String> list7 = Arrays.stream(data7.split("[,;]"))
                                   .collect(Collectors.toList());
        System.out.println(list7); 
        // Output: [red, blue, green, yellow]

        // ✅ Split by pipe |
        String data8 = "alpha|beta|gamma|delta";
        List<String> list8 = Arrays.stream(data8.split("\\|"))
                                   .collect(Collectors.toList());
        System.out.println(list8); 
        // Output: [alpha, beta, gamma, delta]

        // ✅ Split by dot .
        String data9 = "one.two.three.four";
        List<String> list9 = Arrays.stream(data9.split("\\."))
                                   .collect(Collectors.toList());
        System.out.println(list9); 
        // Output: [one, two, three, four]

        // ✅ Split by tab \t
        String data10 = "Tom\tJerry\tSpike";
        List<String> list10 = Arrays.stream(data10.split("\\t"))
                                    .collect(Collectors.toList());
        System.out.println(list10); 
        // Output: [Tom, Jerry, Spike]

        // ✅ Split by newline \n
        String data11 = "first line\nsecond line\nthird line";
        List<String> list11 = Arrays.stream(data11.split("\\n"))
                                    .collect(Collectors.toList());
        System.out.println(list11); 
        // Output: [first line, second line, third line]
    }
}
```