# Java Array Programs with Output

---

A comprehensive guide to Java array manipulation programs with detailed explanations, code examples, and expected outputs. This document covers common array operations including reversing arrays, finding duplicates, sorting, and more.

---

## Table of Contents

| # | Question |
|---|----------|
| **Array Manipulation** |
| 1 | [Java Program to Reverse an Array Without Using Another Array](#java-program-to-reverse-an-array-without-using-another-array) |
| 2 | [Java Program to Find Duplicate Elements in an Array](#java-program-to-find-duplicate-elements-in-an-array) |
| 3 | [Java Program to Find Largest Number in an Array](#java-program-to-find-largest-number-in-an-array) |
| 4 | [Java Program to Check the Equality of Two Arrays](#java-program-to-check-the-equality-of-two-arrays) |
| 5 | [Java Program to Calculate Average Using Arrays](#java-program-to-calculate-average-using-arrays) |
| 6 | [Java Program to Sort the Array Elements in Descending Order](#java-program-to-sort-the-array-elements-in-descending-order) |
| **Additional Array Programs** |
| 7 | [Java Program to Find the Second Largest Element in an Array](#java-program-to-find-the-second-largest-element-in-an-array) |
| 8 | [Java Program to Remove Duplicates from an Array](#java-program-to-remove-duplicates-from-an-array) |
| 9 | [Java Program to Merge Two Arrays](#java-program-to-merge-two-arrays) |
| 10 | [Java Program to Find the Frequency of Each Element in an Array](#java-program-to-find-the-frequency-of-each-element-in-an-array) |

## Java Program to Reverse an Array Without Using Another Array

This program demonstrates how to reverse elements in an array in-place without using an additional array.

### Key Features:
* Uses the **two-pointer approach** to swap elements
* Performs the operation in-place (O(1) space complexity)
* Time complexity is O(n/2) which simplifies to O(n)

### Code Example:

```java
public class ReverseArray {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        
        System.out.println("Original Array:");
        printArray(arr);
        
        reverseArray(arr);
        
        System.out.println("\nReversed Array:");
        printArray(arr);
    }
    
    // Function to reverse the array in-place
    static void reverseArray(int[] arr) {
        int start = 0;
        int end = arr.length - 1;
        
        while (start < end) {
            // Swap elements at start and end indices
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            
            // Move pointers toward center
            start++;
            end--;
        }
    }
    
    // Utility function to print the array
    static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}
```

### Output:
```
Original Array:
1 2 3 4 5 

Reversed Array:
5 4 3 2 1
```

[Back to Top](#table-of-contents)

## Java Program to Find Duplicate Elements in an Array

This program identifies and displays duplicate elements within an array.

### Key Features:
* Uses a **nested loop approach** to compare each element with others
* Handles duplicate detection without additional data structures
* Includes logic to avoid reporting the same duplicate multiple times

### Code Example:

```java
public class FindDuplicates {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 2, 7, 8, 8, 3};
        
        System.out.println("Original Array:");
        printArray(arr);
        
        System.out.println("\nDuplicate elements in the array:");
        findDuplicates(arr);
    }
    
    // Function to find duplicates in the array
    static void findDuplicates(int[] arr) {
        boolean duplicateFound = false;
        
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] == arr[j]) {
                    System.out.println("Found duplicate: " + arr[i]);
                    duplicateFound = true;
                    break; // Break to avoid reporting the same duplicate multiple times
                }
            }
        }
        
        if (!duplicateFound) {
            System.out.println("No duplicates found in the array.");
        }
    }
    
    // Utility function to print the array
    static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}
```

### Output:
```
Original Array:
1 2 3 4 2 7 8 8 3 

Duplicate elements in the array:
Found duplicate: 2
Found duplicate: 3
Found duplicate: 8
```

[Back to Top](#table-of-contents)

## Java Program to Find Largest Number in an Array

This program demonstrates how to find the largest element in an array.

### Key Features:
* Uses a **single-pass** approach with O(n) time complexity
* Handles both positive and negative numbers
* Can be easily modified to find the minimum value

### Code Example:

```java
public class FindLargest {
    public static void main(String[] args) {
        int[] arr = {10, 324, 45, 90, 9808};
        
        System.out.println("Array elements:");
        printArray(arr);
        
        int max = findLargest(arr);
        
        System.out.println("\nLargest element in the array: " + max);
        
        // Example with negative numbers
        int[] arr2 = {-10, -324, -45, -90, -9808};
        System.out.println("\nArray with negative elements:");
        printArray(arr2);
        
        max = findLargest(arr2);
        System.out.println("\nLargest element in the array: " + max);
    }
    
    // Function to find the largest element
    static int findLargest(int[] arr) {
        // Initialize max with the first element
        int max = arr[0];
        
        // Compare with remaining elements
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        
        return max;
    }
    
    // Utility function to print the array
    static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}
```

### Output:
```
Array elements:
10 324 45 90 9808 

Largest element in the array: 9808

Array with negative elements:
-10 -324 -45 -90 -9808 

Largest element in the array: -10
```

[Back to Top](#table-of-contents)

## Java Program to Check the Equality of Two Arrays

This program demonstrates different ways to check if two arrays are equal.

### Key Features:
* Compares arrays using **manual element-by-element** comparison
* Alternative approach using **Arrays.equals()** method
* Handles arrays of different lengths

### Code Example:

```java
import java.util.Arrays;

public class ArrayEquality {
    public static void main(String[] args) {
        // Test Case 1: Equal arrays
        int[] arr1 = {1, 2, 3, 4, 5};
        int[] arr2 = {1, 2, 3, 4, 5};
        
        System.out.println("Array 1: " + Arrays.toString(arr1));
        System.out.println("Array 2: " + Arrays.toString(arr2));
        
        // Method 1: Manual comparison
        boolean isEqual = areEqual(arr1, arr2);
        System.out.println("\nUsing manual comparison - Arrays are equal: " + isEqual);
        
        // Method 2: Using Arrays.equals()
        boolean isEqualBuiltIn = Arrays.equals(arr1, arr2);
        System.out.println("Using Arrays.equals() - Arrays are equal: " + isEqualBuiltIn);
        
        // Test Case 2: Unequal arrays
        int[] arr3 = {1, 2, 3, 4, 5};
        int[] arr4 = {1, 2, 3, 5, 4};
        
        System.out.println("\nArray 3: " + Arrays.toString(arr3));
        System.out.println("Array 4: " + Arrays.toString(arr4));
        
        isEqual = areEqual(arr3, arr4);
        System.out.println("\nUsing manual comparison - Arrays are equal: " + isEqual);
        
        isEqualBuiltIn = Arrays.equals(arr3, arr4);
        System.out.println("Using Arrays.equals() - Arrays are equal: " + isEqualBuiltIn);
    }
    
    // Function to check equality of two arrays manually
    static boolean areEqual(int[] arr1, int[] arr2) {
        // Check if lengths are different
        if (arr1.length != arr2.length) {
            return false;
        }
        
        // Check each element
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        
        // If we reach here, arrays are equal
        return true;
    }
}
```

### Output:
```
Array 1: [1, 2, 3, 4, 5]
Array 2: [1, 2, 3, 4, 5]

Using manual comparison - Arrays are equal: true
Using Arrays.equals() - Arrays are equal: true

Array 3: [1, 2, 3, 4, 5]
Array 4: [1, 2, 3, 5, 4]

Using manual comparison - Arrays are equal: false
Using Arrays.equals() - Arrays are equal: false
```

[Back to Top](#table-of-contents)

## Java Program to Calculate Average Using Arrays

This program calculates the average of all elements in an array.

### Key Features:
* Handles both **integer and floating-point** calculations
* Uses efficient implementation with a single pass through the array
* Demonstrates proper handling of potential arithmetic overflow

### Code Example:

```java
public class ArrayAverage {
    public static void main(String[] args) {
        // Integer array
        int[] intArray = {10, 20, 30, 40, 50};
        
        System.out.println("Integer Array elements:");
        for (int num : intArray) {
            System.out.print(num + " ");
        }
        
        double intAverage = calculateAverage(intArray);
        System.out.println("\nAverage of integer array: " + intAverage);
        
        // Floating-point array
        double[] doubleArray = {10.5, 20.3, 30.7, 40.1, 50.9};
        
        System.out.println("\nDouble Array elements:");
        for (double num : doubleArray) {
            System.out.print(num + " ");
        }
        
        double doubleAverage = calculateAverage(doubleArray);
        System.out.println("\nAverage of double array: " + doubleAverage);
    }
    
    // Function to calculate average of integer array
    static double calculateAverage(int[] arr) {
        // Use long to prevent potential overflow
        long sum = 0;
        
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        
        // Cast to double for accurate division
        return (double) sum / arr.length;
    }
    
    // Function to calculate average of double array
    static double calculateAverage(double[] arr) {
        double sum = 0.0;
        
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        
        return sum / arr.length;
    }
}
```

### Output:
```
Integer Array elements:
10 20 30 40 50 
Average of integer array: 30.0

Double Array elements:
10.5 20.3 30.7 40.1 50.9 
Average of double array: 30.5
```

[Back to Top](#table-of-contents)

## Java Program to Sort the Array Elements in Descending Order

This program demonstrates different approaches to sort an array in descending order.

### Key Features:
* Implements **bubble sort** for manual sorting in descending order
* Uses Java's built-in **Arrays.sort()** with a custom comparator
* Demonstrates proper handling of primitive and wrapper types

### Code Example:

```java
import java.util.Arrays;
import java.util.Collections;

public class SortDescending {
    public static void main(String[] args) {
        // Test array
        int[] arr = {5, 1, 9, 3, 7, 6};
        
        System.out.println("Original Array:");
        printArray(arr);
        
        // Method 1: Using bubble sort (manual approach)
        int[] bubbleSorted = arr.clone();
        bubbleSortDescending(bubbleSorted);
        System.out.println("\nArray after bubble sort (descending):");
        printArray(bubbleSorted);
        
        // Method 2: Using Arrays.sort() with wrapper class
        Integer[] wrapperArr = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            wrapperArr[i] = arr[i];  // Autoboxing
        }
        
        Arrays.sort(wrapperArr, Collections.reverseOrder());
        System.out.println("\nArray after Arrays.sort() with Collections.reverseOrder():");
        for (Integer num : wrapperArr) {
            System.out.print(num + " ");
        }
    }
    
    // Function to sort in descending order using bubble sort
    static void bubbleSortDescending(int[] arr) {
        int n = arr.length;
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Swap if current element is smaller than next element
                if (arr[j] < arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
    
    // Utility function to print the array
    static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}
```

### Output:
```
Original Array:
5 1 9 3 7 6 

Array after bubble sort (descending):
9 7 6 5 3 1 

Array after Arrays.sort() with Collections.reverseOrder():
9 7 6 5 3 1
```

[Back to Top](#table-of-contents)

## Java Program to Find the Second Largest Element in an Array

This program demonstrates how to find the second largest element in an array using different approaches.

### Key Features:
* Implements a **single-pass** algorithm for efficiency
* Handles edge cases like duplicate values
* Demonstrates proper initialization of variables

### Code Example:

```java
import java.util.Arrays;

public class SecondLargest {
    public static void main(String[] args) {
        // Test array
        int[] arr = {12, 35, 1, 10, 34, 1};
        
        System.out.println("Array elements: " + Arrays.toString(arr));
        
        // Method 1: Simple approach
        int secondLargest = findSecondLargest(arr);
        System.out.println("Second largest element: " + secondLargest);
        
        // Method 2: Using sorting
        int secondLargestBySorting = findSecondLargestBySorting(arr);
        System.out.println("Second largest element (using sorting): " + secondLargestBySorting);
        
        // Test case with duplicate largest element
        int[] arr2 = {35, 35, 1, 10, 34, 1};
        System.out.println("\nArray with duplicate largest: " + Arrays.toString(arr2));
        System.out.println("Second largest element: " + findSecondLargest(arr2));
    }
    
    // Function to find second largest element using single traversal
    static int findSecondLargest(int[] arr) {
        if (arr.length < 2) {
            System.out.println("Array should have at least two elements");
            return Integer.MIN_VALUE; // Error value
        }
        
        int largest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;
        
        for (int i = 0; i < arr.length; i++) {
            // If current element is greater than largest
            if (arr[i] > largest) {
                secondLargest = largest;
                largest = arr[i];
            }
            // If current element is between largest and second largest
            else if (arr[i] > secondLargest && arr[i] != largest) {
                secondLargest = arr[i];
            }
        }
        
        if (secondLargest == Integer.MIN_VALUE) {
            System.out.println("No second largest element found");
            return Integer.MIN_VALUE;
        }
        
        return secondLargest;
    }
    
    // Function to find second largest using sorting
    static int findSecondLargestBySorting(int[] arr) {
        if (arr.length < 2) {
            System.out.println("Array should have at least two elements");
            return Integer.MIN_VALUE;
        }
        
        // Sort the array
        Arrays.sort(arr);
        
        // Start from the end to find the second largest
        for (int i = arr.length - 2; i >= 0; i--) {
            if (arr[i] != arr[arr.length - 1]) {
                return arr[i];
            }
        }
        
        System.out.println("No second largest element found");
        return Integer.MIN_VALUE;
    }
}
```

### Output:
```
Array elements: [12, 35, 1, 10, 34, 1]
Second largest element: 34
Second largest element (using sorting): 34

Array with duplicate largest: [35, 35, 1, 10, 34, 1]
Second largest element: 34
```

[Back to Top](#table-of-contents)

## Java Program to Remove Duplicates from an Array

This program demonstrates how to remove duplicate elements from an array.

### Key Features:
* Uses a **temporary array** to store unique elements
* Handles various data types through method overloading
* Preserves the original order of elements

### Code Example:

```java
import java.util.Arrays;
import java.util.LinkedHashSet;

public class RemoveDuplicates {
    public static void main(String[] args) {
        int[] arr = {1, 2, 2, 3, 4, 4, 5, 5, 6};
        
        System.out.println("Original Array: " + Arrays.toString(arr));
        
        // Method 1: Using a temporary array
        int[] uniqueArr = removeDuplicates(arr);
        System.out.println("After removing duplicates: " + Arrays.toString(uniqueArr));
        
        // Method 2: Using LinkedHashSet (preserves order)
        int[] uniqueArrSet = removeDuplicatesUsingSet(arr);
        System.out.println("After removing duplicates (using Set): " + Arrays.toString(uniqueArrSet));
        
        // Test with String array
        String[] strArr = {"Java", "Python", "Java", "C++", "Python", "JavaScript"};
        System.out.println("\nOriginal String Array: " + Arrays.toString(strArr));
        
        String[] uniqueStrArr = removeDuplicatesUsingSet(strArr);
        System.out.println("After removing duplicates: " + Arrays.toString(uniqueStrArr));
    }
    
    // Function to remove duplicates using temporary array
    static int[] removeDuplicates(int[] arr) {
        int n = arr.length;
        
        // If array is empty or has one element, return as is
        if (n <= 1) {
            return arr;
        }
        
        // Create a temporary array to store unique elements
        int[] temp = new int[n];
        int j = 0;  // Index for temporary array
        
        // Traverse the original array
        for (int i = 0; i < n - 1; i++) {
            // If current element is not equal to next element
            if (arr[i] != arr[i + 1]) {
                temp[j++] = arr[i];
            }
        }
        
        // Add the last element
        temp[j++] = arr[n - 1];
        
        // Create the result array with exact size
        int[] result = new int[j];
        System.arraycopy(temp, 0, result, 0, j);
        
        return result;
    }
    
    // Function to remove duplicates using LinkedHashSet (for int array)
    static int[] removeDuplicatesUsingSet(int[] arr) {
        // Create a LinkedHashSet to preserve order
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        
        // Add all elements to the set (duplicates will be ignored)
        for (int i : arr) {
            set.add(i);
        }
        
        // Create a new array from the set
        int[] result = new int[set.size()];
        int i = 0;
        
        for (Integer num : set) {
            result[i++] = num;
        }
        
        return result;
    }
    
    // Function to remove duplicates using LinkedHashSet (for String array)
    static String[] removeDuplicatesUsingSet(String[] arr) {
        // Create a LinkedHashSet to preserve order
        LinkedHashSet<String> set = new LinkedHashSet<>(Arrays.asList(arr));
        
        // Create a new array from the set
        return set.toArray(new String[0]);
    }
}
```

### Output:
```
Original Array: [1, 2, 2, 3, 4, 4, 5, 5, 6]
After removing duplicates: [1, 2, 3, 4, 5, 6]
After removing duplicates (using Set): [1, 2, 3, 4, 5, 6]

Original String Array: [Java, Python, Java, C++, Python, JavaScript]
After removing duplicates: [Java, Python, C++, JavaScript]
```

[Back to Top](#table-of-contents)

## Java Program to Merge Two Arrays

This program demonstrates different approaches to merge two arrays into a single array.

### Key Features:
* Uses **System.arraycopy()** for efficient copying
* Demonstrates merging arrays of different data types
* Shows handling of sorted and unsorted arrays

### Code Example:

```java
import java.util.Arrays;

public class MergeArrays {
    public static void main(String[] args) {
        // Test arrays
        int[] arr1 = {1, 2, 3, 4, 5};
        int[] arr2 = {6, 7, 8, 9, 10};
        
        System.out.println("First Array: " + Arrays.toString(arr1));
        System.out.println("Second Array: " + Arrays.toString(arr2));
        
        // Merge arrays
        int[] mergedArray = mergeArrays(arr1, arr2);
        System.out.println("Merged Array: " + Arrays.toString(mergedArray));
        
        // Test with different sized arrays
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {4, 5, 6, 7, 8};
        
        System.out.println("\nFirst Array: " + Arrays.toString(arr3));
        System.out.println("Second Array: " + Arrays.toString(arr4));
        
        int[] mergedArray2 = mergeArrays(arr3, arr4);
        System.out.println("Merged Array: " + Arrays.toString(mergedArray2));
        
        // Test with String arrays
        String[] strArr1 = {"Hello", "World"};
        String[] strArr2 = {"Java", "Programming"};
        
        System.out.println("\nFirst String Array: " + Arrays.toString(strArr1));
        System.out.println("Second String Array: " + Arrays.toString(strArr2));
        
        String[] mergedStrArray = mergeArrays(strArr1, strArr2);
        System.out.println("Merged String Array: " + Arrays.toString(mergedStrArray));
    }
    
    // Function to merge two integer arrays
    static int[] mergeArrays(int[] arr1, int[] arr2) {
        int len1 = arr1.length;
        int len2 = arr2.length;
        
        // Create a new array with combined length
        int[] result = new int[len1 + len2];
        
        // Copy elements from first array
        System.arraycopy(arr1, 0, result, 0, len1);
        
        // Copy elements from second array
        System.arraycopy(arr2, 0, result, len1, len2);
        
        return result;
    }
    
    // Function to merge two String arrays (method overloading)
    static String[] mergeArrays(String[] arr1, String[] arr2) {
        int len1 = arr1.length;
        int len2 = arr2.length;
        
        // Create a new array with combined length
        String[] result = new String[len1 + len2];
        
        // Copy elements from first array
        System.arraycopy(arr1, 0, result, 0, len1);
        
        // Copy elements from second array
        System.arraycopy(arr2, 0, result, len1, len2);
        
        return result;
    }
    
    // Additional method to merge two sorted arrays in sorted order
    static int[] mergeSortedArrays(int[] arr1, int[] arr2) {
        int len1 = arr1.length;
        int len2 = arr2.length;
        
        // Create a new array with combined length
        int[] result = new int[len1 + len2];
        
        int i = 0, j = 0, k = 0;
        
        // Compare elements from both arrays and add the smaller one to result
        while (i < len1 && j < len2) {
            if (arr1[i] <= arr2[j]) {
                result[k++] = arr1[i++];
            } else {
                result[k++] = arr2[j++];
            }
        }
        
        // Copy remaining elements from first array, if any
        while (i < len1) {
            result[k++] = arr1[i++];
        }
        
        // Copy remaining elements from second array, if any
        while (j < len2) {
            result[k++] = arr2[j++];
        }
        
        return result;
    }
}
```

### Output:
```
First Array: [1, 2, 3, 4, 5]
Second Array: [6, 7, 8, 9, 10]
Merged Array: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

First Array: [1, 2, 3]
Second Array: [4, 5, 6, 7, 8]
Merged Array: [1, 2, 3, 4, 5, 6, 7, 8]

First String Array: [Hello, World]
Second String Array: [Java, Programming]
Merged String Array: [Hello, World, Java, Programming]
```

[Back to Top](#table-of-contents)

## Java Program to Find the Frequency of Each Element in an Array

This program demonstrates how to count the frequency of each element in an array.

### Key Features:
* Uses a **frequency array** for integers within a limited range
* Implements a **HashMap** solution for any data type
* Handles duplicate elements efficiently

### Code Example:

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class ArrayElementFrequency {
    public static void main(String[] args) {
        // Test array
        int[] arr = {1, 2, 8, 3, 2, 2, 2, 5, 1};
        
        System.out.println("Original Array: " + Arrays.toString(arr));
        System.out.println("\nFrequency of each element:");
        
        // Using HashMap to count frequencies
        countFrequency(arr);
        
        // Additional example with a different array
        System.out.println("\n--- Another Example ---");
        int[] arr2 = {10, 20, 20, 10, 10, 20, 5, 20};
        System.out.println("Original Array: " + Arrays.toString(arr2));
        System.out.println("\nFrequency of each element:");
        countFrequency(arr2);
    }
    
    // Function to count frequency of each element
    static void countFrequency(int[] arr) {
        // Create HashMap to store element:frequency pairs
        HashMap<Integer, Integer> frequencyMap = new HashMap<>();
        
        // Count occurrences of each element
        for (int num : arr) {
            // If element exists in map, increment its count
            // Otherwise, add it with count 1
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // Print the frequency of each element
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            System.out.println("Element " + entry.getKey() + " occurs " + 
                               entry.getValue() + " time(s)");
        }
    }
}
```
### Output:
```yml
Original Array: [1, 2, 8, 3, 2, 2, 2, 5, 1]

Frequency of each element:
Element 1 occurs 2 time(s)
Element 2 occurs 4 time(s)
Element 3 occurs 1 time(s)
Element 5 occurs 1 time(s)
Element 8 occurs 1 time(s)

--- Another Example ---
Original Array: [10, 20, 20, 10, 10, 20, 5, 20]

Frequency of each element:
Element 5 occurs 1 time(s)
Element 20 occurs 4 time(s)
Element 10 occurs 3 time(s)

```