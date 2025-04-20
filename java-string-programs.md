# Java String Programs with Examples and Solutions

[![Click star if you like](https://img.shields.io/github/stars/javadevs/java-string-programs?style=social)](https://www.linkedin.com/in/javaexpert)

---

Java String handling is a fundamental skill for any Java developer. This comprehensive guide covers 30 essential string manipulation programs with complete code examples and detailed explanations of each solution.

## Additional Resources

This guide is part of a larger series on Java programming fundamentals. Check out these related resources:
- [Java Collections Framework](https://github.com/javadevs/java-collections)
- [Java Streams API Guide](https://github.com/javadevs/java-streams)
- [Java Concurrency Patterns](https://github.com/javadevs/java-concurrency)
- [Java Design Patterns](https://github.com/javadevs/java-design-patterns)

If you find this guide helpful, please consider giving it a star and sharing it with others!

---

## Table of Contents

| #  | Program                                                                                     |
|----|---------------------------------------------------------------------------------------------|
| **Basic String Operations**                                                                      |
| 1  | [Find the First Non-repeated Character in a String](#1-find-the-first-non-repeated-character-in-a-string) |
| 2  | [Check Palindrome String](#2-check-palindrome-string)                                       |
| 3  | [Find Duplicate Characters in a String](#3-find-duplicate-characters-in-a-string)          |
| 4  | [Find Duplicate Words in a String](#4-find-duplicate-words-in-a-string)                    |
| 5  | [Find All the Permutations of a String](#5-find-all-the-permutations-of-a-string)          |
| **Counting Operations**                                                                         |
| 6  | [Count Occurrences of Words in a String](#6-count-occurrences-of-words-in-a-string)        |
| 7  | [Count the Occurrences of Each Character](#7-count-the-occurrences-of-each-character)      |
| 8  | [Count Vowels and Consonants in a String](#8-count-vowels-and-consonants-in-a-string)      |
| 9  | [Count the Number of Duplicate Words in a String](#9-count-the-number-of-duplicate-words-in-a-string) |
| 10 | [Count Number of Words in Given String](#10-count-number-of-words-in-given-string)         |
| 11 | [Count the Number of Occurrences of Substring in a String](#11-count-the-number-of-occurrences-of-substring-in-a-string) |
| 12 | [Count the Occurrences of Each Character in String](#12-count-the-occurrences-of-each-character-in-string) |
| **String Manipulation**                                                                         |
| 13 | [Merge Two String Arrays](#13-merge-two-string-arrays)                                     |
| 14 | [Remove Duplicate Words from String](#14-remove-duplicate-words-from-string)              |
| 15 | [Reverse a String (5 ways)](#15-reverse-a-string-5-ways)                                   |
| 16 | [Reverse Each Word of a String](#16-reverse-each-word-of-a-string)                         |
| 17 | [Swap Two Strings](#17-swap-two-strings)                                                   |
| **String Validation**                                                                          |
| 18 | [Check if the String Contains Only Digits](#18-check-if-the-string-contains-only-digits)   |
| 19 | [Check if the String Contains Only Letters](#19-check-if-the-string-contains-only-letters) |
| 20 | [Check If the String Contains Only Letters or Digits](#20-check-if-the-string-contains-only-letters-or-digits) |
| **String Modification**                                                                        |
| 21 | [Remove All Whitespaces from a String](#21-remove-all-whitespaces-from-a-string)          |
| 22 | [Check if a String is Empty or Null](#22-check-if-a-string-is-empty-or-null)              |
| 23 | [Find Maximum Occurring Character in String](#23-find-maximum-occurring-character-in-string) |
| 24 | [Add Characters to a String](#24-add-characters-to-a-string)                              |
| **Miscellaneous**                                                                              |
| 25 | [Check if a Year is a Leap Year](#26-check-if-a-year-is-a-leap-year)                      |
| 26 | [Display Characters from A to Z using Loop](#27-display-characters-from-a-to-z-using-loop) |
| 27 | [Count Occurrences of Each Character Using HashMap](#28-count-occurrences-of-each-character-using-hashmap) |
<!-- | 25 | [Remove All Whitespaces from a String (Alternative Method)](#25-remove-all-whitespaces-from-a-string-alternative-method) | -->
---
## 1. Find the First Non-repeated Character in a String

This program finds the first non-repeated character in a given string. A non-repeated character is one that appears exactly once in the entire string.

```java
public class FirstNonRepeatedCharacter {
    public static void main(String[] args) {
        String str = "programming";
        System.out.println("The given string is: " + str);
        
        for (int i = 0; i < str.length(); i++) {
            boolean isUnique = true;
            for (int j = 0; j < str.length(); j++) {
                if (i != j && str.charAt(i) == str.charAt(j)) {
                    isUnique = false;
                    break;
                }
            }
            if (isUnique) {
                System.out.println("The first non-repeated character is: " + str.charAt(i));
                return;
            }
        }
        System.out.println("There are no non-repeated characters in the string.");
    }
}
```

**Output:**
```
The given string is: programming
The first non-repeated character is: p
```

**Using HashMap (More Efficient):**

```java
import java.util.HashMap;
import java.util.Map;

public class FirstNonRepeatedCharacterHashMap {
    public static void main(String[] args) {
        String str = "programming";
        System.out.println("The given string is: " + str);
        
        Map<Character, Integer> charCountMap = new HashMap<>();
        
        // Count occurrences of each character
        for (char c : str.toCharArray()) {
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }
        
        // Find first non-repeated character
        for (char c : str.toCharArray()) {
            if (charCountMap.get(c) == 1) {
                System.out.println("The first non-repeated character is: " + c);
                return;
            }
        }
        
        System.out.println("There are no non-repeated characters in the string.");
    }
}
```

**Output:**
```
The given string is: programming
The first non-repeated character is: p
```

[Back to Top](#table-of-contents)

## 2. Check Palindrome String

A palindrome is a string that reads the same backward as forward. This program checks if a given string is a palindrome.

```java
public class PalindromeCheck {
    public static void main(String[] args) {
        String str = "madam";
        String reversedStr = "";
        
        System.out.println("Original string: " + str);
        
        for (int i = str.length() - 1; i >= 0; i--) {
            reversedStr += str.charAt(i);
        }
        
        if (str.equals(reversedStr)) {
            System.out.println("The string is a palindrome.");
        } else {
            System.out.println("The string is not a palindrome.");
        }
    }
}
```

**More Efficient Approach:**

```java
public class PalindromeCheckEfficient {
    public static void main(String[] args) {
        String str = "madam";
        boolean isPalindrome = true;
        
        System.out.println("Original string: " + str);
        
        int length = str.length();
        for (int i = 0; i < length / 2; i++) {
            if (str.charAt(i) != str.charAt(length - i - 1)) {
                isPalindrome = false;
                break;
            }
        }
        
        if (isPalindrome) {
            System.out.println("The string is a palindrome.");
        } else {
            System.out.println("The string is not a palindrome.");
        }
    }
}
```

**Output:**
```
Original string: madam
The string is a palindrome.
```

[Back to Top](#table-of-contents)

## 3. Find Duplicate Characters in a String

This program finds and displays all duplicate characters in a given string.

```java
import java.util.HashMap;
import java.util.Map;

public class DuplicateCharacters {
    public static void main(String[] args) {
        String str = "programming";
        System.out.println("String: " + str);
        
        Map<Character, Integer> charCountMap = new HashMap<>();
        
        // Count each character
        for (char c : str.toCharArray()) {
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }
        
        System.out.println("Duplicate characters in the string:");
        boolean foundDuplicate = false;
        for (Map.Entry<Character, Integer> entry : charCountMap.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println("'" + entry.getKey() + "' appears " + entry.getValue() + " times");
                foundDuplicate = true;
            }
        }
        
        if (!foundDuplicate) {
            System.out.println("No duplicate characters found.");
        }
    }
}
```

**Output:**
```
String: programming
Duplicate characters in the string:
'r' appears 2 times
'g' appears 2 times
'm' appears 2 times
```

[Back to Top](#table-of-contents)

## 4. Find Duplicate Words in a String

This program identifies and displays all duplicate words in a given string.

```java
import java.util.HashMap;
import java.util.Map;

public class DuplicateWords {
    public static void main(String[] args) {
        String str = "Java is a programming language. Java is widely used for developing applications.";
        System.out.println("String: " + str);
        
        // Convert to lowercase and split by spaces and punctuation
        String[] words = str.toLowerCase().split("\\s+|\\p{Punct}");
        
        Map<String, Integer> wordCountMap = new HashMap<>();
        
        // Count occurrences of each word
        for (String word : words) {
            if (word.length() > 0) { // Skip empty strings
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }
        }
        
        System.out.println("Duplicate words in the string:");
        boolean foundDuplicate = false;
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println("'" + entry.getKey() + "' appears " + entry.getValue() + " times");
                foundDuplicate = true;
            }
        }
        
        if (!foundDuplicate) {
            System.out.println("No duplicate words found.");
        }
    }
}
```

**Output:**
```
String: Java is a programming language. Java is widely used for developing applications.
Duplicate words in the string:
'java' appears 2 times
'is' appears 2 times
```

[Back to Top](#table-of-contents)

## 5. Find All the Permutations of a String

This program generates and displays all possible permutations of a given string.

```java
public class StringPermutations {
    public static void main(String[] args) {
        String str = "ABC";
        System.out.println("String: " + str);
        System.out.println("All permutations of the string:");
        generatePermutations(str, 0, str.length() - 1);
    }
    
    private static void generatePermutations(String str, int left, int right) {
        if (left == right) {
            System.out.println(str);
            return;
        }
        
        for (int i = left; i <= right; i++) {
            str = swap(str, left, i);
            generatePermutations(str, left + 1, right);
            str = swap(str, left, i); // backtrack
        }
    }
    
    private static String swap(String str, int i, int j) {
        char[] charArray = str.toCharArray();
        char temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }
}
```

**Output:**
```
String: ABC
All permutations of the string:
ABC
ACB
BAC
BCA
CBA
CAB
```

[Back to Top](#table-of-contents)

## 6. Count Occurrences of Words in a String

This program counts how many times each word appears in a given string.

```java
import java.util.HashMap;
import java.util.Map;

public class CountWordOccurrences {
    public static void main(String[] args) {
        String str = "Java is a programming language. Java is widely used for enterprise applications.";
        System.out.println("String: " + str);
        
        // Convert to lowercase and split by spaces and punctuation
        String[] words = str.toLowerCase().split("\\s+|\\p{Punct}");
        
        Map<String, Integer> wordCountMap = new HashMap<>();
        
        // Count occurrences of each word
        for (String word : words) {
            if (word.length() > 0) { // Skip empty strings
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }
        }
        
        System.out.println("Word occurrences in the string:");
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            System.out.println("'" + entry.getKey() + "': " + entry.getValue() + " time(s)");
        }
    }
}
```

**Output:**
```
String: Java is a programming language. Java is widely used for enterprise applications.
Word occurrences in the string:
'a': 1 time(s)
'applications': 1 time(s)
'for': 1 time(s)
'is': 2 time(s)
'java': 2 time(s)
'language': 1 time(s)
'programming': 1 time(s)
'used': 1 time(s)
'widely': 1 time(s)
'enterprise': 1 time(s)
```

[Back to Top](#table-of-contents)

## 7. Count the Occurrences of Each Character

This program counts how many times each character appears in a given string.

```java
import java.util.HashMap;
import java.util.Map;

public class CountCharacterOccurrences {
    public static void main(String[] args) {
        String str = "programming";
        System.out.println("String: " + str);
        
        Map<Character, Integer> charCountMap = new HashMap<>();
        
        // Count occurrences of each character
        for (char c : str.toCharArray()) {
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }
        
        System.out.println("Character occurrences in the string:");
        for (Map.Entry<Character, Integer> entry : charCountMap.entrySet()) {
            System.out.println("'" + entry.getKey() + "': " + entry.getValue() + " time(s)");
        }
    }
}
```

**Output:**
```
String: programming
Character occurrences in the string:
'p': 1 time(s)
'r': 2 time(s)
'o': 1 time(s)
'g': 2 time(s)
'a': 1 time(s)
'm': 2 time(s)
'i': 1 time(s)
'n': 1 time(s)
```

[Back to Top](#table-of-contents)

## 8. Count Vowels and Consonants in a String

This program counts the number of vowels and consonants in a given string.

```java
public class CountVowelsConsonants {
    public static void main(String[] args) {
        String str = "programming";
        str = str.toLowerCase();
        System.out.println("String: " + str);
        
        int vowels = 0;
        int consonants = 0;
        
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            
            // Check if character is a letter
            if (ch >= 'a' && ch <= 'z') {
                // Check if character is a vowel
                if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                    vowels++;
                } else {
                    consonants++;
                }
            }
        }
        
        System.out.println("Vowels: " + vowels);
        System.out.println("Consonants: " + consonants);
    }
}
```

**Output:**
```
String: programming
Vowels: 3
Consonants: 8
```

[Back to Top](#table-of-contents)

## 9. Count the Number of Duplicate Words in a String

This program counts how many words appear more than once in a given string.

```java
import java.util.HashMap;
import java.util.Map;

public class CountDuplicateWords {
    public static void main(String[] args) {
        String str = "Java is a programming language. Java is widely used for developing applications.";
        System.out.println("String: " + str);
        
        // Convert to lowercase and split by spaces and punctuation
        String[] words = str.toLowerCase().split("\\s+|\\p{Punct}");
        
        Map<String, Integer> wordCountMap = new HashMap<>();
        
        // Count occurrences of each word
        for (String word : words) {
            if (word.length() > 0) { // Skip empty strings
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }
        }
        
        int duplicateWordCount = 0;
        System.out.println("Duplicate words in the string:");
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println("'" + entry.getKey() + "' appears " + entry.getValue() + " times");
                duplicateWordCount++;
            }
        }
        
        System.out.println("Total number of duplicate words: " + duplicateWordCount);
    }
}
```

**Output:**
```
String: Java is a programming language. Java is widely used for developing applications.
Duplicate words in the string:
'java' appears 2 times
'is' appears 2 times
Total number of duplicate words: 2
```

[Back to Top](#table-of-contents)

## 10. Count Number of Words in Given String

This program counts the total number of words in a given string.

```java
public class CountWords {
    public static void main(String[] args) {
        String str = "Java is a programming language. It is widely used.";
        System.out.println("String: " + str);
        
        // Method 1: Using split
        String[] words = str.split("\\s+");
        System.out.println("Number of words (Method 1): " + words.length);
        
        // Method 2: Manual counting
        int wordCount = 0;
        boolean isWord = false;
        
        for (int i = 0; i < str.length(); i++) {
            // If current character is a letter or digit
            if (Character.isLetterOrDigit(str.charAt(i))) {
                // If we haven't found a word yet, increment counter
                if (!isWord) {
                    wordCount++;
                    isWord = true;
                }
            } else {
                // If we found a delimiter, reset word flag
                isWord = false;
            }
        }
        
        System.out.println("Number of words (Method 2): " + wordCount);
    }
}
```

**Output:**
```
String: Java is a programming language. It is widely used.
Number of words (Method 1): 9
Number of words (Method 2): 9
```

[Back to Top](#table-of-contents)

## 11. Count the Number of Occurrences of Substring in a String

This program counts how many times a specific substring appears in a given string.

```java
public class CountSubstringOccurrences {
    public static void main(String[] args) {
        String str = "Java programming is fun. Java is a popular language. I love Java programming.";
        String subStr = "Java";
        
        System.out.println("String: " + str);
        System.out.println("Substring to search: " + subStr);
        
        // Method 1: Using indexOf
        int count = 0;
        int index = 0;
        
        while ((index = str.indexOf(subStr, index)) != -1) {
            count++;
            index += subStr.length();
        }
        
        System.out.println("The substring occurs " + count + " times.");
        
        // Method 2: Using split (counts occurrences + 1, so need to subtract 1)
        int countUsingRegex = str.split(subStr, -1).length - 1;
        System.out.println("The substring occurs (using regex): " + countUsingRegex + " times.");
    }
}
```

**Output:**
```
String: Java programming is fun. Java is a popular language. I love Java programming.
Substring to search: Java
The substring occurs 3 times.
The substring occurs (using regex): 3 times.
```

[Back to Top](#table-of-contents)

## 12. Count the Occurrences of Each Character in String

This program counts and displays the occurrences of each character in a given string.

```java
import java.util.HashMap;
import java.util.Map;

public class CountEachCharacter {
    public static void main(String[] args) {
        String str = "programming";
        System.out.println("String: " + str);
        
        Map<Character, Integer> charCountMap = new HashMap<>();
        
        // Count occurrences of each character
        for (char c : str.toCharArray()) {
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }
        
        System.out.println("Character occurrences:");
        for (Map.Entry<Character, Integer> entry : charCountMap.entrySet()) {
            System.out.println("'" + entry.getKey() + "': " + entry.getValue() + " time(s)");
        }
    }
}
```

**Output:**
```
String: programming
Character occurrences:
'p': 1 time(s)
'r': 2 time(s)
'o': 1 time(s)
'g': 2 time(s)
'a': 1 time(s)
'm': 2 time(s)
'i': 1 time(s)
'n': 1 time(s)
```

[Back to Top](#table-of-contents)

## 13. Merge Two String Arrays

This program merges two string arrays into a single array.

```java
import java.util.Arrays;

public class MergeStringArrays {
    public static void main(String[] args) {
        String[] array1 = {"Java", "Python", "C++"};
        String[] array2 = {"JavaScript", "Kotlin", "Swift"};
        
        System.out.println("First array: " + Arrays.toString(array1));
        System.out.println("Second array: " + Arrays.toString(array2));
        
        // Create a new array with the combined length
        String[] mergedArray = new String[array1.length + array2.length];
        
        // Copy elements from first array
        System.arraycopy(array1, 0, mergedArray, 0, array1.length);
        
        // Copy elements from second array
        System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);
        
        System.out.println("Merged array: " + Arrays.toString(mergedArray));
    }
}
```

**Using Java 8+ Stream API:**

```java
import java.util.Arrays;
import java.util.stream.Stream;

public class MergeStringArraysStream {
    public static void main(String[] args) {
        String[] array1 = {"Java", "Python", "C++"};
        String[] array2 = {"JavaScript", "Kotlin", "Swift"};
        
        System.out.println("First array: " + Arrays.toString(array1));
        System.out.println("Second array: " + Arrays.toString(array2));
        
        // Merge arrays using Stream API
        String[] mergedArray = Stream.concat(
                Arrays.stream(array1),
                Arrays.stream(array2)
            ).toArray(String[]::new);
        
        System.out.println("Merged array: " + Arrays.toString(mergedArray));
    }
}
```

**Output:**
```
First array: [Java, Python, C++]
Second array: [JavaScript, Kotlin, Swift]
Merged array: [Java, Python, C++, JavaScript, Kotlin, Swift]
```

[Back to Top](#table-of-contents)

## 14. Remove Duplicate Words from String

This program removes duplicate words from a given string.

```java
import java.util.LinkedHashSet;
import java.util.Set;

public class RemoveDuplicateWords {
    public static void main(String[] args) {
        String str = "Java is a programming language. Java is widely used for developing applications.";
        System.out.println("Original string: " + str);
        
        // Split the string into words
        String[] words = str.split("\\s+");
        
        // Use LinkedHashSet to maintain order while removing duplicates
        Set<String> uniqueWords = new LinkedHashSet<>();
        for (String word : words) {
            uniqueWords.add(word);
        }
        
        // Join the unique words back to form a string
        StringBuilder result = new StringBuilder();
        for (String word : uniqueWords) {
            result.append(word).append(" ");
        }
        
        System.out.println("After removing duplicate words: " + result.toString().trim());
    }
}
```

**Using Java 8+ Stream API:**

```java
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RemoveDuplicateWordsStream {
    public static void main(String[] args) {
        String str = "Java is a programming language. Java is widely used for developing applications.";
        System.out.println("Original string: " + str);
        
        // Using Stream API to remove duplicates
        String result = Arrays.stream(str.split("\\s+"))
                         .distinct()
                         .collect(Collectors.joining(" "));
        
        System.out.println("After removing duplicate words: " + result);
    }
}
```

**Output:**
```
Original string: Java is a programming language. Java is widely used for developing applications.
After removing duplicate words: Java is a programming language. widely used for developing applications.
```

[Back to Top](#table-of-contents)

## 15. Reverse a String (5 ways)

This program demonstrates five different ways to reverse a string in Java.

```java
public class ReverseString {
    public static void main(String[] args) {
        String original = "Hello, World!";
        System.out.println("Original string: " + original);
        
        // Method 1: Using StringBuilder/StringBuffer
        String reversed1 = new StringBuilder(original).reverse().toString();
        System.out.println("Method 1 (StringBuilder): " + reversed1);
        
        // Method 2: Using character array
        char[] charArray = original.toCharArray();
        String reversed2 = "";
        for (int i = charArray.length - 1; i >= 0; i--) {
            reversed2 += charArray[i];
        }
        System.out.println("Method 2 (Character Array): " + reversed2);
        
        // Method 3: Using byte array (for efficiency)
        byte[] strAsByteArray = original.getBytes();
        byte[] result = new byte[strAsByteArray.length];
        
        for (int i = 0; i < strAsByteArray.length; i++) {
            result[i] = strAsByteArray[strAsByteArray.length - i - 1];
        }
        String reversed3 = new String(result);
        System.out.println("Method 3 (Byte Array): " + reversed3);
        
        // Method 4: Using recursion
        System.out.println("Method 4 (Recursion): " + reverseRecursively(original));
        
        // Method 5: Using StringBuilder with custom logic
        StringBuilder reversed5 = new StringBuilder();
        for (int i = original.length() - 1; i >= 0; i--) {
            reversed5.append(original.charAt(i));
        }
        System.out.println("Method 5 (StringBuilder with loop): " + reversed5.toString());
    }
    
    // Recursive method to reverse a string
    private static String reverseRecursively(String str) {
        // Base case
        if (str.length() <= 1) {
            return str;
        }
        return reverseRecursively(str.substring(1)) + str.charAt(0);
    }
}
```

**Output:**
```
Original string: Hello, World!
Method 1 (StringBuilder): !dlroW ,olleH
Method 2 (Character Array): !dlroW ,olleH
Method 3 (Byte Array): !dlroW ,olleH
Method 4 (Recursion): !dlroW ,olleH
Method 5 (StringBuilder with loop): !dlroW ,olleH
```

[Back to Top](#table-of-contents)


### 16. Reverse Each Word of a String

This program reverses each word in a string while maintaining the original word order.

**Key concepts:**
- **String tokenization** to split the string into words
- **StringBuilder** for efficient string manipulation
- **String concatenation** to rebuild the result

```java
public class ReverseEachWord {
    public static void main(String[] args) {
        String input = "Java is a programming language";
        System.out.println("Original string: " + input);
        System.out.println("Reversed words: " + reverseEachWord(input));
    }
    
    public static String reverseEachWord(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            StringBuilder reversedWord = new StringBuilder(word);
            result.append(reversedWord.reverse()).append(" ");
        }
        
        // Remove the trailing space and return
        return result.toString().trim();
    }
}
```

**Output:**
```
Original string: Java is a programming language
Reversed words: avaJ si a gnimmargorp egaugnal
```

[Back to Top](#table-of-contents)


### 17. Swap Two Strings

This program demonstrates how to swap two strings without using a third string variable.

**Key concepts:**
- **String concatenation** for combining strings
- **String.substring()** method for extracting portions of strings

```java
public class SwapStrings {
    public static void main(String[] args) {
        String str1 = "Hello";
        String str2 = "World";
        
        System.out.println("Before swapping:");
        System.out.println("str1: " + str1);
        System.out.println("str2: " + str2);
        
        // Method 1: Using concatenation and substring
        str1 = str1 + str2;
        str2 = str1.substring(0, str1.length() - str2.length());
        str1 = str1.substring(str2.length());
        
        System.out.println("\nAfter swapping:");
        System.out.println("str1: " + str1);
        System.out.println("str2: " + str2);
        
        /* Alternative method using a temporary variable:
        String temp = str1;
        str1 = str2;
        str2 = temp;
        */
    }
}
```

**Output:**
```
Before swapping:
str1: Hello
str2: World

After swapping:
str1: World
str2: Hello
```

[Back to Top](#table-of-contents)

### 18. Check if the String Contains Only Digits


This program determines if a string consists exclusively of numeric digits.

**Key concepts:**
- **Character.isDigit()** method to check individual characters
- **String.chars()** method to create an IntStream of characters
- **Regular expressions** for pattern matching

```java
public class CheckOnlyDigits {
    public static void main(String[] args) {
        String numeric = "12345";
        String alphanumeric = "abc123";
        
        System.out.println("Using iteration method:");
        System.out.println(numeric + " contains only digits: " + containsOnlyDigitsIterative(numeric));
        System.out.println(alphanumeric + " contains only digits: " + containsOnlyDigitsIterative(alphanumeric));
        
        System.out.println("\nUsing Java 8 Stream API:");
        System.out.println(numeric + " contains only digits: " + containsOnlyDigitsStream(numeric));
        System.out.println(alphanumeric + " contains only digits: " + containsOnlyDigitsStream(alphanumeric));
        
        System.out.println("\nUsing Regular Expression:");
        System.out.println(numeric + " contains only digits: " + containsOnlyDigitsRegex(numeric));
        System.out.println(alphanumeric + " contains only digits: " + containsOnlyDigitsRegex(alphanumeric));
    }
    
    // Method 1: Using iteration
    public static boolean containsOnlyDigitsIterative(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        
        return true;
    }
    
    // Method 2: Using Java 8 Stream API
    public static boolean containsOnlyDigitsStream(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        
        return str.chars().allMatch(Character::isDigit);
    }
    
    // Method 3: Using Regular Expression
    public static boolean containsOnlyDigitsRegex(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        
        return str.matches("\\d+");
    }
}
```

**Output:**
```
Using iteration method:
12345 contains only digits: true
abc123 contains only digits: false

Using Java 8 Stream API:
12345 contains only digits: true
abc123 contains only digits: false

Using Regular Expression:
12345 contains only digits: true
abc123 contains only digits: false
```

[Back to Top](#table-of-contents)


### 19. Check if the String Contains Only Letters

This program verifies if a string contains only alphabetic characters (letters).

**Key concepts:**
- **Character.isLetter()** method to check if a character is a letter
- **String.chars()** for stream-based character processing
- **Regular expressions** with `matches()` method

```java
public class CheckOnlyLetters {
    public static void main(String[] args) {
        String onlyLetters = "HelloWorld";
        String mixed = "Hello123";
        
        System.out.println("Using iteration method:");
        System.out.println(onlyLetters + " contains only letters: " + containsOnlyLettersIterative(onlyLetters));
        System.out.println(mixed + " contains only letters: " + containsOnlyLettersIterative(mixed));
        
        System.out.println("\nUsing Java 8 Stream API:");
        System.out.println(onlyLetters + " contains only letters: " + containsOnlyLettersStream(onlyLetters));
        System.out.println(mixed + " contains only letters: " + containsOnlyLettersStream(mixed));
        
        System.out.println("\nUsing Regular Expression:");
        System.out.println(onlyLetters + " contains only letters: " + containsOnlyLettersRegex(onlyLetters));
        System.out.println(mixed + " contains only letters: " + containsOnlyLettersRegex(mixed));
    }
    
    // Method 1: Using iteration
    public static boolean containsOnlyLettersIterative(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        
        return true;
    }
    
    // Method 2: Using Java 8 Stream API
    public static boolean containsOnlyLettersStream(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        
        return str.chars().allMatch(Character::isLetter);
    }
    
    // Method 3: Using Regular Expression
    public static boolean containsOnlyLettersRegex(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        
        return str.matches("[a-zA-Z]+");
    }
}
```

**Output:**
```
Using iteration method:
HelloWorld contains only letters: true
Hello123 contains only letters: false

Using Java 8 Stream API:
HelloWorld contains only letters: true
Hello123 contains only letters: false

Using Regular Expression:
HelloWorld contains only letters: true
Hello123 contains only letters: false
```

[Back to Top](#table-of-contents)

### 20. Check If the String Contains Only Letters or Digits

This program checks whether a string contains only letters or digits (alphanumeric characters).

**Key concepts:**
- **Character.isLetterOrDigit()** method for checking alphanumeric characters
- **Stream operations** for functional processing
- **Regular expression pattern** for alphanumeric validation

```java
public class CheckOnlyLettersOrDigits {
    public static void main(String[] args) {
        String alphanumeric = "Hello123";
        String withSpecialChars = "Hello@123";
        
        System.out.println("Using iteration method:");
        System.out.println(alphanumeric + " contains only letters or digits: " + 
                containsOnlyLettersOrDigitsIterative(alphanumeric));
        System.out.println(withSpecialChars + " contains only letters or digits: " + 
                containsOnlyLettersOrDigitsIterative(withSpecialChars));
        
        System.out.println("\nUsing Java 8 Stream API:");
        System.out.println(alphanumeric + " contains only letters or digits: " + 
                containsOnlyLettersOrDigitsStream(alphanumeric));
        System.out.println(withSpecialChars + " contains only letters or digits: " + 
                containsOnlyLettersOrDigitsStream(withSpecialChars));
        
        System.out.println("\nUsing Regular Expression:");
        System.out.println(alphanumeric + " contains only letters or digits: " + 
                containsOnlyLettersOrDigitsRegex(alphanumeric));
        System.out.println(withSpecialChars + " contains only letters or digits: " + 
                containsOnlyLettersOrDigitsRegex(withSpecialChars));
    }
    
    // Method 1: Using iteration
    public static boolean containsOnlyLettersOrDigitsIterative(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetterOrDigit(str.charAt(i))) {
                return false;
            }
        }
        
        return true;
    }
    
    // Method 2: Using Java 8 Stream API
    public static boolean containsOnlyLettersOrDigitsStream(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        
        return str.chars().allMatch(Character::isLetterOrDigit);
    }
    
    // Method 3: Using Regular Expression
    public static boolean containsOnlyLettersOrDigitsRegex(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        
        return str.matches("[a-zA-Z0-9]+");
    }
}
```

**Output:**
```
Using iteration method:
Hello123 contains only letters or digits: true
Hello@123 contains only letters or digits: false

Using Java 8 Stream API:
Hello123 contains only letters or digits: true
Hello@123 contains only letters or digits: false

Using Regular Expression:
Hello123 contains only letters or digits: true
Hello@123 contains only letters or digits: false
```

[Back to Top](#table-of-contents)

### 21. Remove All Whitespaces from a String

This program demonstrates different ways to remove all whitespace characters from a string.

**Key concepts:**
- **String.replaceAll()** method with regex patterns
- **StringBuilder** for manual character processing
- **Stream API** for functional approach to character filtering

```java
public class RemoveWhitespaces {
    public static void main(String[] args) {
        String input = "  Java    Programming   Language  ";
        
        System.out.println("Original string: \"" + input + "\"");
        
        // Method 1: Using replaceAll with regex
        String result1 = input.replaceAll("\\s", "");
        System.out.println("After removing whitespaces (replaceAll): \"" + result1 + "\"");
        
        // Method 2: Using StringBuilder
        String result2 = removeWhitespacesWithStringBuilder(input);
        System.out.println("After removing whitespaces (StringBuilder): \"" + result2 + "\"");
        
        // Method 3: Using Java 8 Stream API
        String result3 = input.chars()
                .filter(c -> !Character.isWhitespace(c))
                .collect(StringBuilder::new, 
                         StringBuilder::appendCodePoint, 
                         StringBuilder::append)
                .toString();
        System.out.println("After removing whitespaces (Stream API): \"" + result3 + "\"");
    }
    
    public static String removeWhitespacesWithStringBuilder(String input) {
        if (input == null) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        
        return sb.toString();
    }
}
```

**Output:**
```
Original string: "  Java    Programming   Language  "
After removing whitespaces (replaceAll): "JavaProgrammingLanguage"
After removing whitespaces (StringBuilder): "JavaProgrammingLanguage"
After removing whitespaces (Stream API): "JavaProgrammingLanguage"
```

[Back to Top](#table-of-contents)


### 22. Check if a String is Empty or Null

This program demonstrates various ways to check if a string is empty, null, or contains only whitespace.

**Key concepts:**
- **Null check** to prevent NullPointerException
- **String.isEmpty()** for checking empty strings
- **String.isBlank()** (Java 11+) for checking blank strings
- **String.trim()** to remove surrounding whitespace

```java
public class CheckEmptyOrNull {
    public static void main(String[] args) {
        String nullString = null;
        String emptyString = "";
        String blankString = "    ";
        String nonEmptyString = "Hello";
        
        // Test each string with different methods
        System.out.println("--- Testing with isNullOrEmpty method ---");
        System.out.println("Null string is null or empty: " + isNullOrEmpty(nullString));
        System.out.println("Empty string is null or empty: " + isNullOrEmpty(emptyString));
        System.out.println("Blank string is null or empty: " + isNullOrEmpty(blankString));
        System.out.println("Non-empty string is null or empty: " + isNullOrEmpty(nonEmptyString));
        
        System.out.println("\n--- Testing with isNullOrBlank method ---");
        System.out.println("Null string is null or blank: " + isNullOrBlank(nullString));
        System.out.println("Empty string is null or blank: " + isNullOrBlank(emptyString));
        System.out.println("Blank string is null or blank: " + isNullOrBlank(blankString));
        System.out.println("Non-empty string is null or blank: " + isNullOrBlank(nonEmptyString));
    }
    
    // Method to check if a string is null or empty
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
    
    // Method to check if a string is null, empty, or contains only whitespace
    public static boolean isNullOrBlank(String str) {
        // For Java 11 and above, you can use: return str == null || str.isBlank();
        return str == null || str.trim().isEmpty();
    }
    
    // Java 11+ version of isNullOrBlank
    public static boolean isNullOrBlankJava11(String str) {
        return str == null || str.isBlank();
    }
}
```

**Output:**
```
--- Testing with isNullOrEmpty method ---
Null string is null or empty: true
Empty string is null or empty: true
Blank string is null or empty: false
Non-empty string is null or empty: false

--- Testing with isNullOrBlank method ---
Null string is null or blank: true
Empty string is null or blank: true
Blank string is null or blank: true
Non-empty string is null or blank: false
```

[Back to Top](#table-of-contents)

### 23. Find Maximum Occurring Character in String

This program finds the character that appears most frequently in a given string.

**Key concepts:**
- **HashMap** for counting character occurrences
- **Collections framework** for finding maximum values
- **Character frequency analysis**

```java
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MaxOccurringCharacter {
    public static void main(String[] args) {
        String str = "programming";
        System.out.println("String: " + str);
        System.out.println("Maximum occurring character: " + findMaxOccurringChar(str));
        
        str = "Java programming language";
        System.out.println("\nString: " + str);
        System.out.println("Maximum occurring character: " + findMaxOccurringChar(str));
        
        // Edge cases
        str = "";
        System.out.println("\nEmpty string result: " + findMaxOccurringChar(str));
        
        str = "aaaaabbbbbccccc"; // Multiple characters with same frequency
        System.out.println("\nString with multiple max chars: " + str);
        System.out.println("Maximum occurring character: " + findMaxOccurringChar(str));
    }
    
    public static char findMaxOccurringChar(String str) {
        // Handle edge cases
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }
        
        // Create a HashMap to store character counts
        HashMap<Character, Integer> charCountMap = new HashMap<>();
        
        // Convert the string to char array and count each character
        char[] charArray = str.toCharArray();
        for (char c : charArray) {
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }
        
        // Find the character with maximum count
        char maxChar = ' ';
        int maxCount = 0;
        
        for (Map.Entry<Character, Integer> entry : charCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxChar = entry.getKey();
            }
        }
        
        return maxChar;
    }
    
    // Alternative approach using Java 8 features
    public static char findMaxOccurringCharJava8(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }
        
        // Create character frequency map
        Map<Character, Long> frequencyMap = str.chars()
                .mapToObj(c -> (char) c)
                .collect(java.util.stream.Collectors.groupingBy(
                        c -> c, java.util.stream.Collectors.counting()));
        
        // Find max entry
        return Collections.max(frequencyMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
```

**Output:**
```
String: programming
Maximum occurring character: m

String: Java programming language
Maximum occurring character: a

Empty string result: Exception: Input string cannot be null or empty

String with multiple max chars: aaaaabbbbbccccc
Maximum occurring character: a
```

[Back to Top](#table-of-contents)

### 24. Add Characters to a String

This program demonstrates different ways to add characters to a string in Java.

**Key concepts:**
- **String immutability** in Java
- **StringBuilder and StringBuffer** for mutable string operations
- **String concatenation** methods

```java
public class AddCharactersToString {
    public static void main(String[] args) {
        // Initial string
        String original = "Hello";
        System.out.println("Original string: " + original);
        
        // Method 1: Using + operator (concatenation)
        String withPlusOperator = original + " World";
        System.out.println("\nAfter adding with + operator: " + withPlusOperator);
        
        // Method 2: Using concat() method
        String withConcat = original.concat(" Java");
        System.out.println("After adding with concat(): " + withConcat);
        
        // Method 3: Using StringBuilder (most efficient for multiple operations)
        StringBuilder sb = new StringBuilder(original);
        sb.append(" ").append("Programming");
        String withStringBuilder = sb.toString();
        System.out.println("After adding with StringBuilder: " + withStringBuilder);
        
        // Method 4: Insert at specific position using StringBuilder
        StringBuilder sb2 = new StringBuilder(original);
        sb2.insert(1, "i"); // Insert 'i' after the first character
        String withInsert = sb2.toString();
        System.out.println("After inserting 'i' at position 1: " + withInsert);
        
        // Method 5: Replace characters using StringBuilder
        StringBuilder sb3 = new StringBuilder(original);
        sb3.replace(1, 3, "app"); // Replace characters from index 1 to 2
        String withReplace = sb3.toString();
        System.out.println("After replacing characters at positions 1-2: " + withReplace);
        
        // Demonstration of different positions to add characters
        System.out.println("\nAdding at different positions:");
        System.out.println("Original: " + original);
        System.out.println("Prefix: " + addPrefix(original, "Java "));
        System.out.println("Suffix: " + addSuffix(original, " World"));
        System.out.println("At index 2: " + addAtPosition(original, "XX", 2));
    }
    
    // Add characters at the beginning (prefix)
    public static String addPrefix(String str, String prefix) {
        return prefix + str;
    }
    
    // Add characters at the end (suffix)
    public static String addSuffix(String str, String suffix) {
        return str + suffix;
    }
    
    // Add characters at a specific position
    public static String addAtPosition(String str, String toAdd, int position) {
        // Validate position
        if (position < 0 || position > str.length()) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
        
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, toAdd);
        return sb.toString();
    }
}
```

**Output:**
```
Original string: Hello

After adding with + operator: Hello World
After adding with concat(): Hello Java
After adding with StringBuilder: Hello Programming
After inserting 'i' at position 1: Hiello
After replacing characters at positions 1-2: Happlo

Adding at different positions:
Original: Hello
Prefix: Java Hello
Suffix: Hello World
At index 2: HeXXllo
```

[Back to Top](#table-of-contents)

### 26. Check if a Year is a Leap Year

This program determines whether a given year is a leap year according to the Gregorian calendar rules.

**Key concepts:**
- **Leap year rules** in the Gregorian calendar
- **Conditional logic** for implementing calendar rules
- **Java's Calendar and LocalDate APIs** for built-in leap year checks

```java
import java.time.LocalDate;
import java.time.Year;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class LeapYearChecker {
    public static void main(String[] args) {
        // Test different years
        int[] yearsToCheck = {1900, 2000, 2020, 2023, 2024, 2100};
        
        System.out.println("Checking leap years with custom implementation:");
        for (int year : yearsToCheck) {
            System.out.println(year + " is a leap year: " + isLeapYear(year));
        }
        
        System.out.println("\nChecking leap years with GregorianCalendar:");
        for (int year : yearsToCheck) {
            System.out.println(year + " is a leap year: " + isLeapYearUsingGregorianCalendar(year));
        }
        
        System.out.println("\nChecking leap years with Java 8 Year class:");
        for (int year : yearsToCheck) {
            System.out.println(year + " is a leap year: " + isLeapYearUsingYear(year));
        }
    }
    
    /**
     * Checks if a year is a leap year using custom logic.
     * Leap year rules:
     * 1. The year must be divisible by 4
     * 2. If the year is divisible by 100, it must also be divisible by 400
     */
    public static boolean isLeapYear(int year) {
        // If year is divisible by 400, it's a leap year
        if (year % 400 == 0) {
            return true;
        }
        
        // If year is divisible by 100 but not by 400, it's not a leap year
        if (year % 100 == 0) {
            return false;
        }
        
        // If year is divisible by 4, it's a leap year
        return year % 4 == 0;
        
        // One-line alternative:
        // return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
    }
    
    /**
     * Checks if a year is a leap year using Java's GregorianCalendar.
     */
    public static boolean isLeapYearUsingGregorianCalendar(int year) {
        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.isLeapYear(year);
    }
    
    /**
     * Checks if a year is a leap year using Java 8's Year class.
     */
    public static boolean isLeapYearUsingYear(int year) {
        return Year.isLeap(year);
    }
}
```

**Output:**
```
Checking leap years with custom implementation:
1900 is a leap year: false
2000 is a leap year: true
2020 is a leap year: true
2023 is a leap year: false
2024 is a leap year: true
2100 is a leap year: false

Checking leap years with GregorianCalendar:
1900 is a leap year: false
2000 is a leap year: true
2020 is a leap year: true
2023 is a leap year: false
2024 is a leap year: true
2100 is a leap year: false

Checking leap years with Java 8 Year class:
1900 is a leap year: false
2000 is a leap year: true
2020 is a leap year: true
2023 is a leap year: false
2024 is a leap year: true
2100 is a leap year: false
```

[Back to Top](#table-of-contents)

### 27. Display Characters from A to Z using Loop

This program demonstrates different approaches to print all uppercase letters from A to Z.

**Key concepts:**
- **ASCII values** for character representation
- **Character typecasting** from integers
- **Loop structures** for iteration
- **Java 8 Stream API** for functional approach

```java
import java.util.stream.IntStream;

public class DisplayAtoZ {
    public static void main(String[] args) {
        System.out.println("Using for loop with char:");
        displayAtoZWithCharLoop();
        
        System.out.println("\nUsing for loop with ASCII values:");
        displayAtoZWithASCII();
        
        System.out.println("\nUsing while loop:");
        displayAtoZWithWhileLoop();
        
        System.out.println("\nUsing Java 8 Stream API:");
        displayAtoZWithStreamAPI();
    }
    
    // Method 1: Using for loop with char
    public static void displayAtoZWithCharLoop() {
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            System.out.print(ch + " ");
        }
        System.out.println();
    }
    
    // Method 2: Using for loop with ASCII values
    public static void displayAtoZWithASCII() {
        // ASCII value of 'A' is 65 and 'Z' is 90
        for (int i = 65; i <= 90; i++) {
            System.out.print((char)i + " ");
        }
        System.out.println();
    }
    
    // Method 3: Using while loop
    public static void displayAtoZWithWhileLoop() {
        char ch = 'A';
        while (ch <= 'Z') {
            System.out.print(ch + " ");
            ch++;
        }
        System.out.println();
    }
    
    // Method 4: Using Java 8 Stream API
    public static void displayAtoZWithStreamAPI() {
        IntStream.rangeClosed(65, 90)
                .mapToObj(i -> (char)i)
                .forEach(ch -> System.out.print(ch + " "));
        System.out.println();
    }
}
```

**Output:**
```
Using for loop with char:
A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 

Using for loop with ASCII values:
A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 

Using while loop:
A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 

Using Java 8 Stream API:
A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 
```

[Back to Top](#table-of-contents)

### 28. Count Occurrences of Each Character Using HashMap

This program counts the occurrences of each character in a string using a HashMap data structure.

**Key concepts:**
- **HashMap** for storing character counts
- **Character manipulation** in strings
- **Stream API** for functional programming approach
- **Map entry iteration** for displaying results

```java

import java.util.HashMap;
import java.util.Map;

public class CharacterCounter {
    public static Map<Character, Integer> countCharacters(String input) {
        // Create a HashMap to store character counts
        Map<Character, Integer> charCountMap = new HashMap<>();
        
        // If the input is null or empty, return the empty map
        if (input == null || input.isEmpty()) {
            return charCountMap;
        }
        
        // Convert the string to a char array and count each character
        char[] chars = input.toCharArray();
        
        for (char c : chars) {
            // If the character is already in the map, increment its count
            // Otherwise, add it to the map with a count of 1
            charCountMap.put(c, charCountMap.getOrDefault(c, 0) + 1);
        }
        
        return charCountMap;
    }
    
    public static void main(String[] args) {
        String text = "hello world";
        Map<Character, Integer> result = countCharacters(text);
        
        // Print the character counts
        for (Map.Entry<Character, Integer> entry : result.entrySet()) {
            System.out.println("'" + entry.getKey() + "': " + entry.getValue());
        }
    }
}

```