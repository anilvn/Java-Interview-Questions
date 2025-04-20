# Java Number Programs 
---

This documentation provides a comprehensive collection of Java programs for working with numbers. Each program includes detailed explanations, implementation code, and sample outputs to help you understand the concepts and logic behind numerical operations in Java.

---
### Additional Resources

If you found this guide helpful, you might also be interested in my other Spring Framework resources:
- [Core Java & Java-8 Interview Questions](https://github.com/anilvn/Java-Interview-Questions/tree/main)
- [Spring Boot Interview Questions](https://github.com/anilvn/spring-boot-interview-questions/tree/main)
- [Microservices with Spring Cloud Tutorials](https://javatechonline.com/microservices-tutorial/)

Feel free to star and fork these repositories if you find them useful!


---

## Table of Contents

| # | Program |
|---|---------|
| **Basic Number Operations** |
| 1 | [Find Factorial Using Recursion](#find-factorial-using-recursion) |
| 2 | [Find Sum of Natural Numbers](#find-sum-of-natural-numbers) |
| 3 | [Reverse a Number](#reverse-a-number) |
| 4 | [Count the Number of Digits in a Number](#count-the-number-of-digits-in-a-number) |
| 5 | [Check if a Number is Positive or Negative](#check-if-a-number-is-positive-or-negative) |
| 6 | [Swap Two Numbers Without Using a Temp Variable](#swap-two-numbers-without-using-a-temp-variable) |
| **Number Properties** |
| 7 | [Check Prime Number](#check-if-a-number-is-prime) |
| 8 | [Find Prime Numbers Within a Range](#find-prime-numbers-within-a-range) |
| 9 | [Check Armstrong Number](#check-armstrong-number) |
| 10 | [Check Palindrome Number](#check-palindrome-number) |
| 11 | [Check if a Given Number is Perfect Square](#check-if-a-given-number-is-perfect-square) |
| 12 | [Find Strong Number](#find-strong-number) |
| **Mathematical Algorithms** |
| 13 | [Find the Fibonacci Series](#find-the-fibonacci-series) |
| 14 | [Find GCD of Two Numbers](#find-gcd-of-two-numbers) |
| 15 | [Find LCM of Two Numbers](#find-lcm-of-two-numbers) |
| **Array Operations** |
| 16 | [Find the Largest of Three Numbers](#find-the-largest-of-three-numbers) |
| 17 | [Find the Second Largest Number in an Array](#find-the-second-largest-number-in-an-array) |

## Find Factorial Using Recursion

A factorial is the product of all positive integers less than or equal to a given positive integer. This program implements factorial calculation using recursion.

**Key Concepts:**
* **Factorial** notation is represented as n! where n is a non-negative integer
* **Base case** for recursion is 0! = 1 or 1! = 1
* **Recursive case** is n! = n × (n-1)!

```java
public class FactorialRecursion {
    public static void main(String[] args) {
        int num = 6;
        long factorial = calculateFactorial(num);
        System.out.println("Factorial of " + num + " = " + factorial);
    }
    
    public static long calculateFactorial(int n) {
        // Base case: factorial of 0 or 1 is 1
        if (n == 0 || n == 1) {
            return 1;
        }
        // Recursive case: n! = n * (n-1)!
        else {
            return n * calculateFactorial(n - 1);
        }
    }
}
```

**Sample Output:**
```
Factorial of 6 = 720
```

[Back to Top](#table-of-contents)

## Find Sum of Natural Numbers

This program calculates the sum of first n natural numbers using both iterative and recursive approaches.

**Key Concepts:**
* **Natural numbers** are positive integers starting from 1
* Sum can be calculated using the formula **n(n+1)/2**
* Both **iterative** and **recursive** solutions are demonstrated

```java
public class SumOfNaturalNumbers {
    public static void main(String[] args) {
        int num = 100;
        
        // Using iteration
        int sumIterative = 0;
        for (int i = 1; i <= num; i++) {
            sumIterative += i;
        }
        
        // Using formula
        int sumFormula = (num * (num + 1)) / 2;
        
        // Using recursion
        int sumRecursive = calculateSumRecursive(num);
        
        System.out.println("Sum of first " + num + " natural numbers (Iterative): " + sumIterative);
        System.out.println("Sum of first " + num + " natural numbers (Formula): " + sumFormula);
        System.out.println("Sum of first " + num + " natural numbers (Recursive): " + sumRecursive);
    }
    
    public static int calculateSumRecursive(int n) {
        // Base case
        if (n == 1) {
            return 1;
        }
        // Recursive case
        else {
            return n + calculateSumRecursive(n - 1);
        }
    }
}
```

**Sample Output:**
```
Sum of first 100 natural numbers (Iterative): 5050
Sum of first 100 natural numbers (Formula): 5050
Sum of first 100 natural numbers (Recursive): 5050
```

[Back to Top](#table-of-contents)

## Reverse a Number

This program reverses a given integer using arithmetic operations.

**Key Concepts:**
* Uses **modulus (%)** to extract the last digit
* Uses **division (/)** to remove the last digit
* Builds the reversed number by multiplying by 10 and adding extracted digits

```java
public class ReverseNumber {
    public static void main(String[] args) {
        int num = 12345;
        int reversed = 0;
        
        // Save the original number for output
        int original = num;
        
        // Reverse the number
        while (num != 0) {
            // Extract the last digit
            int digit = num % 10;
            
            // Append the digit to reversed
            reversed = reversed * 10 + digit;
            
            // Remove the last digit from num
            num /= 10;
        }
        
        System.out.println("Original number: " + original);
        System.out.println("Reversed number: " + reversed);
    }
}
```

**Sample Output:**
```
Original number: 12345
Reversed number: 54321
```

[Back to Top](#table-of-contents)

## Count the Number of Digits in a Number

This program counts the number of digits in a given integer using both iterative and logarithmic approaches.

**Key Concepts:**
* **Iterative approach** repeatedly divides by 10 until the number becomes 0
* **Logarithmic approach** uses `Math.log10()` to find the number of digits in one step
* Handles **negative numbers** by taking the absolute value

```java
public class CountDigits {
    public static void main(String[] args) {
        int num = 123456;
        
        // Iterative approach
        int count = 0;
        int temp = Math.abs(num);  // Handle negative numbers
        
        // Special case for 0
        if (num == 0) {
            count = 1;
        } else {
            while (temp != 0) {
                count++;
                temp /= 10;
            }
        }
        
        // Logarithmic approach (faster)
        int logCount = num == 0 ? 1 : (int) Math.log10(Math.abs(num)) + 1;
        
        System.out.println("Number: " + num);
        System.out.println("Number of digits (Iterative): " + count);
        System.out.println("Number of digits (Logarithmic): " + logCount);
    }
}
```

**Sample Output:**
```
Number: 123456
Number of digits (Iterative): 6
Number of digits (Logarithmic): 6
```

[Back to Top](#table-of-contents)

## Check if a Number is Positive or Negative

This program determines whether a given number is positive, negative, or zero.

**Key Concepts:**
* Simple **comparison operators** are used
* Handles **zero** as a special case

```java
public class CheckPositiveNegative {
    public static void main(String[] args) {
        // Test with different numbers
        checkNumber(15);
        checkNumber(-25);
        checkNumber(0);
    }
    
    public static void checkNumber(int num) {
        if (num > 0) {
            System.out.println(num + " is POSITIVE");
        } else if (num < 0) {
            System.out.println(num + " is NEGATIVE");
        } else {
            System.out.println(num + " is ZERO");
        }
    }
}
```

**Sample Output:**
```
15 is POSITIVE
-25 is NEGATIVE
0 is ZERO
```

[Back to Top](#table-of-contents)

## Swap Two Numbers Without Using a Temp Variable

This program demonstrates how to swap two numbers without using a temporary variable, using arithmetic operations and XOR.

**Key Concepts:**
* **Addition and subtraction** method
* **Multiplication and division** method (with precaution for zero values)
* **XOR (^)** bitwise operation method

```java
public class SwapWithoutTemp {
    public static void main(String[] args) {
        // Method 1: Using addition and subtraction
        int a = 10, b = 20;
        System.out.println("Before swapping: a = " + a + ", b = " + b);
        
        a = a + b;
        b = a - b;
        a = a - b;
        
        System.out.println("After swapping (method 1): a = " + a + ", b = " + b);
        
        // Method 2: Using XOR
        int x = 30, y = 40;
        System.out.println("\nBefore swapping: x = " + x + ", y = " + y);
        
        x = x ^ y;
        y = x ^ y;
        x = x ^ y;
        
        System.out.println("After swapping (method 2): x = " + x + ", y = " + y);
        
        // Method 3: Using multiplication and division (beware of zero values)
        int p = 5, q = 8;
        System.out.println("\nBefore swapping: p = " + p + ", q = " + q);
        
        p = p * q;
        q = p / q;
        p = p / q;
        
        System.out.println("After swapping (method 3): p = " + p + ", q = " + q);
    }
}
```

**Sample Output:**
```
Before swapping: a = 10, b = 20
After swapping (method 1): a = 20, b = 10

Before swapping: x = 30, y = 40
After swapping (method 2): x = 40, y = 30

Before swapping: p = 5, q = 8
After swapping (method 3): p = 8, q = 5
```

[Back to Top](#table-of-contents)

## Check if a Number is Prime

This program checks whether a given number is prime using optimized trial division.

**Key Concepts:**
* A **prime number** is a natural number greater than 1 that is not divisible by any other number except 1 and itself
* **Optimization**: Only check divisibility up to the square root of the number
* **Special cases**: 0 and 1 are not prime numbers

```java
public class CheckPrime {
    public static void main(String[] args) {
        checkPrimeNumber(7);
        checkPrimeNumber(15);
        checkPrimeNumber(1);
        checkPrimeNumber(2);
        checkPrimeNumber(97);
    }
    
    public static void checkPrimeNumber(int num) {
        boolean isPrime = true;
        
        // Check for special cases
        if (num <= 1) {
            isPrime = false;
        } else if (num == 2) {
            isPrime = true;
        } else if (num % 2 == 0) {
            isPrime = false;
        } else {
            // Check divisibility from 3 to sqrt(num)
            for (int i = 3; i <= Math.sqrt(num); i += 2) {
                if (num % i == 0) {
                    isPrime = false;
                    break;
                }
            }
        }
        
        if (isPrime) {
            System.out.println(num + " is a prime number");
        } else {
            System.out.println(num + " is not a prime number");
        }
    }
}
```

**Sample Output:**
```
7 is a prime number
15 is not a prime number
1 is not a prime number
2 is a prime number
97 is a prime number
```

[Back to Top](#table-of-contents)

## Find Prime Numbers Within a Range

This program finds and displays all prime numbers within a specified range using the Sieve of Eratosthenes algorithm for efficiency.

**Key Concepts:**
* **Sieve of Eratosthenes** algorithm for efficiently finding primes
* **Time complexity**: O(n log log n), which is more efficient than naive trial division
* Handles ranges with different starting and ending values

```java
public class PrimeNumbersInRange {
    public static void main(String[] args) {
        int start = 10;
        int end = 50;
        
        System.out.println("Prime numbers between " + start + " and " + end + " are:");
        findPrimesInRange(start, end);
    }
    
    public static void findPrimesInRange(int start, int end) {
        // Make sure start is at least 2
        start = Math.max(start, 2);
        
        // Apply Sieve of Eratosthenes algorithm
        boolean[] isComposite = new boolean[end + 1];
        
        // Mark all multiples of each prime as composite
        for (int i = 2; i * i <= end; i++) {
            if (!isComposite[i]) {
                for (int j = i * i; j <= end; j += i) {
                    isComposite[j] = true;
                }
            }
        }
        
        // Print all primes in the range
        for (int i = start; i <= end; i++) {
            if (!isComposite[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }
}
```

**Sample Output:**
```
Prime numbers between 10 and 50 are:
11 13 17 19 23 29 31 37 41 43 47
```

[Back to Top](#table-of-contents)

## Check Armstrong Number

An Armstrong number (also known as a narcissistic number) is a number that is equal to the sum of its own digits each raised to the power of the number of digits. This program checks if a given number is an Armstrong number.

**Key Concepts:**
* **Armstrong number** (also called Narcissistic number)
* Calculation requires **counting digits** first
* Each digit is raised to the power of the **number of digits**

```java
public class ArmstrongNumber {
    public static void main(String[] args) {
        checkArmstrong(153);  // 1^3 + 5^3 + 3^3 = 1 + 125 + 27 = 153
        checkArmstrong(370);  // 3^3 + 7^3 + 0^3 = 27 + 343 + 0 = 370
        checkArmstrong(371);  // 3^3 + 7^3 + 1^3 = 27 + 343 + 1 = 371
        checkArmstrong(9474); // 9^4 + 4^4 + 7^4 + 4^4 = 6561 + 256 + 2401 + 256 = 9474
        checkArmstrong(9475); // Not an Armstrong number
    }
    
    public static void checkArmstrong(int num) {
        int originalNumber = num;
        int sum = 0;
        
        // Count digits
        int digits = 0;
        int temp = num;
        while (temp > 0) {
            temp /= 10;
            digits++;
        }
        
        // Calculate sum of each digit raised to the power of number of digits
        temp = num;
        while (temp > 0) {
            int remainder = temp % 10;
            sum += Math.pow(remainder, digits);
            temp /= 10;
        }
        
        // Check if the number is Armstrong
        if (originalNumber == sum) {
            System.out.println(originalNumber + " is an Armstrong number");
        } else {
            System.out.println(originalNumber + " is not an Armstrong number");
        }
    }
}
```

**Sample Output:**
```
153 is an Armstrong number
370 is an Armstrong number
371 is an Armstrong number
9474 is an Armstrong number
9475 is not an Armstrong number
```

[Back to Top](#table-of-contents)

## Check Palindrome Number

A palindrome number reads the same backward as forward. This program checks if a given number is a palindrome.

**Key Concepts:**
* Reverses the digits of the number
* Compares the **reversed number** with the **original number**
* Can be implemented using mathematical operations without converting to string

```java
public class PalindromeNumber {
    public static void main(String[] args) {
        checkPalindrome(121);
        checkPalindrome(12321);
        checkPalindrome(12345);
        checkPalindrome(1001);
    }
    
    public static void checkPalindrome(int num) {
        int originalNumber = num;
        int reversed = 0;
        
        // Reverse the number
        while (num > 0) {
            int digit = num % 10;
            reversed = reversed * 10 + digit;
            num /= 10;
        }
        
        // Check if palindrome
        if (originalNumber == reversed) {
            System.out.println(originalNumber + " is a palindrome number");
        } else {
            System.out.println(originalNumber + " is not a palindrome number");
        }
    }
}
```

**Sample Output:**
```
121 is a palindrome number
12321 is a palindrome number
12345 is not a palindrome number
1001 is a palindrome number
```

[Back to Top](#table-of-contents)

## Check if a Given Number is Perfect Square

This program checks whether a given number is a perfect square using different methods.

**Key Concepts:**
* A **perfect square** is an integer that is the square of another integer
* Square root method using `Math.sqrt()` followed by checking if the result is an integer
* Binary search method for checking without floating-point operations

```java
public class PerfectSquare {
    public static void main(String[] args) {
        checkPerfectSquare(16);
        checkPerfectSquare(25);
        checkPerfectSquare(15);
        checkPerfectSquare(100);
        checkPerfectSquare(0);
    }
    
    public static void checkPerfectSquare(int num) {
        // Method 1: Using Math.sqrt()
        double sqrt = Math.sqrt(num);
        boolean isPerfectSquare = (sqrt - Math.floor(sqrt)) == 0;
        
        // Method 2: Using binary search (alternative method without floating-point)
        boolean usingBinarySearch = isPerfectSquareBinarySearch(num);
        
        System.out.println(num + " is " + (isPerfectSquare ? "a" : "not a") + " perfect square");
        
        // Show the square root if it's a perfect square
        if (isPerfectSquare) {
            System.out.println("Square root of " + num + " is " + (int)sqrt);
        }
    }
    
    // Binary search method for checking perfect squares
    public static boolean isPerfectSquareBinarySearch(int num) {
        // Handle special cases
        if (num < 0) return false;
        if (num == 0 || num == 1) return true;
        
        long start = 1;
        long end = num / 2;
        
        while (start <= end) {
            long mid = start + (end - start) / 2;
            long square = mid * mid;
            
            if (square == num) {
                return true;
            } else if (square < num) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        
        return false;
    }
}
```

**Sample Output:**
```
16 is a perfect square
Square root of 16 is 4
25 is a perfect square
Square root of 25 is 5
15 is not a perfect square
100 is a perfect square
Square root of 100 is 10
0 is a perfect square
Square root of 0 is 0
```

[Back to Top](#table-of-contents)

## Find Strong Number

A strong number is a number whose sum of factorial of digits is equal to the original number. This program checks whether a given number is a strong number.

**Key Concepts:**
* **Strong number**: Sum of factorial of all digits equals the original number
* Example: 145 = 1! + 4! + 5! = 1 + 24 + 120 = 145

```java
public class StrongNumber {
    public static void main(String[] args) {
        checkStrongNumber(145);  // 1! + 4! + 5! = 1 + 24 + 120 = 145
        checkStrongNumber(40585); // 4! + 0! + 5! + 8! + 5! = 24 + 1 + 120 + 40320 + 120 = 40585
        checkStrongNumber(1);    // 1! = 1
        checkStrongNumber(2);    // 2! = 2 (not a strong number)
    }
    
    public static void checkStrongNumber(int num) {
        int originalNumber = num;
        int sum = 0;
        
        // Process each digit
        while (num > 0) {
            int digit = num % 10;
            sum += factorial(digit);
            num /= 10;
        }
        
        // Check if strong number
        if (sum == originalNumber) {
            System.out.println(originalNumber + " is a strong number");
        } else {
            System.out.println(originalNumber + " is not a strong number");
        }
    }
    
    // Helper method to calculate factorial
    public static int factorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        int fact = 1;
        for (int i = 2; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
}
```

**Sample Output:**
```
145 is a strong number
40585 is a strong number
1 is a strong number
2 is not a strong number
```

[Back to Top](#table-of-contents)

## Find the Fibonacci Series

This program generates the Fibonacci series up to a specified number of terms using different approaches.

**Key Concepts:**
* **Fibonacci series**: Each number is the sum of the two preceding ones
* Starting values: 0, 1
* Next values: F(n) = F(n-1) + F(n-2)
* Implemented using **iterative** and **recursive** approaches

```java
public class FibonacciSeries {
    public static void main(String[] args) {
        int terms = 10;
        
        System.out.println("Fibonacci Series (first " + terms + " terms):");
        
        // Method 1: Iterative approach
        System.out.print("Using iteration: ");
        printFibonacciIterative(terms);
        
        // Method 2: Recursive approach (inefficient for large values)
        System.out.print("\nUsing recursion: ");
        for (int i = 0; i < terms; i++) {
            System.out.print(fibonacciRecursive(i) + " ");
        }
        
        // Method 3: Using dynamic programming
        System.out.print("\nUsing dynamic programming: ");
        printFibonacciDP(terms);
    }
    
    // Iterative approach
    public static void printFibonacciIterative(int n) {
        int first = 0, second = 1;
        
        if (n < 1) return;
        
        System.out.print(first + " ");
        
        if (n < 2) return;
        
        System.out.print(second + " ");
        
        for (int i = 2; i < n; i++) {
            int next = first + second;
            System.out.print(next + " ");
            first = second;
            second = next;
        }
    }
    
    // Recursive approach (inefficient due to repeated calculations)
    public static int fibonacciRecursive(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }
    
    // Dynamic programming approach
    public static void printFibonacciDP(int n) {
        int[] fib = new int[n];
        
        if (n < 1) return;
        
        fib[0] = 0;
        System.out.print(fib[0] + " ");
        
        if (n < 2) return;
        
        fib[1] = 1;
        System.out.print(fib[1] + " ");
        
        for (int i = 2; i < n; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
            System.out.print(fib[i] + " ");
        }
    }
}
```

**Sample Output:**
```
Fibonacci Series (first 10 terms):
Using iteration: 0 1 1 2 3 5 8 13 21 34 
Using recursion: 0 1 1 2 3 5 8 13 21 34 
Using dynamic programming: 0 1 1 2 3 5 8 13 21 34 
```

[Back to Top](#table-of-contents)

## Find GCD of Two Numbers

This program calculates the Greatest Common Divisor (GCD) of two numbers using multiple approaches.

**Key Concepts:**
* **GCD** (Greatest Common Divisor) or **HCF** (Highest Common Factor) is the largest positive integer that divides each given number without a remainder
* **Euclidean algorithm** is an efficient method for finding GCD
* **Recursive** and **iterative** implementations are provided

```java
public class GCD {
    public static void main(String[] args) {
        int num1 = 48;
        int num2 = 18;
        
        // Method 1: Using Euclidean algorithm (iterative)
        int gcdIterative = findGCDIterative(num1, num2);
        
        // Method 2: Using Euclidean algorithm (recursive)
        int gcdRecursive = findGCDRecursive(num1, num2);
        
        // Method 3: Using brute force
        int gcdBruteForce = findGCDBruteForce(num1, num2);
        
        System.out.println("GCD of " + num1 + " and " + num2 + " (Iterative): " + gcdIterative);
        System.out.println("GCD of " + num1 + " and " + num2 + " (Recursive): " + gcdRecursive);
        System.out.println("GCD of " + num1 + " and " + num2 + " (Brute Force): " + gcdBruteForce);
    }
    
    // Euclidean algorithm (iterative)
    public static int findGCDIterative(int a, int b) {
        // Ensure positive numbers
        a = Math.abs(a);
        b = Math.abs(b);
        
        // GCD(0, b) = b; GCD(a, 0) = a
        if (a == 0) return b;
        if (b == 0) return a;
        
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        
        return a;
    }
    
    // Euclidean algorithm (recursive)
    public static int findGCDRecursive(int a, int b) {
        // Ensure positive numbers
        a = Math.abs(a);
        b = Math.abs(b);
        
        // Base case
        if (b == 0) {
            return a;
        }
        
        // Recursive case
        return findGCDRecursive(b, a % b);
    }
    
    // Brute force method
    public static int findGCDBruteForce(int a, int b) {
        // Ensure positive numbers
        a = Math.abs(a);
        b = Math.abs(b);
        
        // Find the smaller number
        int smaller = Math.min(a, b);
        
        // Find the GCD
        int gcd = 1;
        for (int i = 1; i <= smaller; i++) {
            if (a % i == 0 && b % i == 0) {
                gcd = i;
            }
        }
        
        return gcd;
    }
}
```

**Sample Output:**
```
GCD of 48 and 18 (Iterative): 6
GCD of 48 and 18 (Recursive): 6
GCD of 48 and 18 (Brute Force): 6
```

[Back to Top](#table-of-contents)

## Find LCM of Two Numbers

This program calculates the Least Common Multiple (LCM) of two numbers using the GCD approach.

**Key Concepts:**
* **LCM** (Least Common Multiple) is the smallest positive integer that is divisible by both numbers
* LCM can be calculated using the formula: **LCM(a, b) = (a × b) / GCD(a, b)**
* Utilizes the GCD function from the previous program

```java
public class LCM {
    public static void main(String[] args) {
        int num1 = 12;
        int num2 = 18;
        
        // Method 1: Using GCD
        int lcmUsingGCD = findLCMUsingGCD(num1, num2);
        
        // Method 2: Brute force approach
        int lcmBruteForce = findLCMBruteForce(num1, num2);
        
        System.out.println("LCM of " + num1 + " and " + num2 + " (Using GCD): " + lcmUsingGCD);
        System.out.println("LCM of " + num1 + " and " + num2 + " (Brute Force): " + lcmBruteForce);
    }
    
    // Find LCM using GCD
    public static int findLCMUsingGCD(int a, int b) {
        // LCM(a, b) = (a * b) / GCD(a, b)
        return Math.abs(a * b) / findGCD(a, b);
    }
    
    // Helper method to find GCD
    public static int findGCD(int a, int b) {
        if (b == 0) {
            return a;
        }
        return findGCD(b, a % b);
    }
    
    // Find LCM using brute force
    public static int findLCMBruteForce(int a, int b) {
        // Ensure positive numbers
        a = Math.abs(a);
        b = Math.abs(b);
        
        // LCM can't be less than either a or b
        int lcm = Math.max(a, b);
        
        // Keep incrementing lcm until it's divisible by both a and b
        while (true) {
            if (lcm % a == 0 && lcm % b == 0) {
                return lcm;
            }
            lcm++;
        }
    }
}
```

**Sample Output:**
```
LCM of 12 and 18 (Using GCD): 36
LCM of 12 and 18 (Brute Force): 36
```

[Back to Top](#table-of-contents)

## Find the Largest of Three Numbers

This program finds the largest among three given numbers using different approaches.

**Key Concepts:**
* Using **if-else** conditions for comparing multiple values
* Using the **Math.max()** function for cleaner code
* Using a **ternary operator** for concise comparison

```java
public class LargestOfThreeNumbers {
    public static void main(String[] args) {
        int num1 = 25;
        int num2 = 78;
        int num3 = 45;
        
        // Method 1: Using if-else statements
        int largestIfElse = findLargestUsingIfElse(num1, num2, num3);
        
        // Method 2: Using Math.max()
        int largestMathMax = findLargestUsingMathMax(num1, num2, num3);
        
        // Method 3: Using ternary operator
        int largestTernary = findLargestUsingTernary(num1, num2, num3);
        
        System.out.println("Numbers: " + num1 + ", " + num2 + ", " + num3);
        System.out.println("Largest number (using if-else): " + largestIfElse);
        System.out.println("Largest number (using Math.max): " + largestMathMax);
        System.out.println("Largest number (using ternary): " + largestTernary);
    }
    
    // Find largest using if-else statements
    public static int findLargestUsingIfElse(int a, int b, int c) {
        int largest;
        
        if (a >= b && a >= c) {
            largest = a;
        } else if (b >= a && b >= c) {
            largest = b;
        } else {
            largest = c;
        }
        
        return largest;
    }
    
    // Find largest using Math.max()
    public static int findLargestUsingMathMax(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }
    
    // Find largest using ternary operator
    public static int findLargestUsingTernary(int a, int b, int c) {
        int largest = (a >= b) ? ((a >= c) ? a : c) : ((b >= c) ? b : c);
        return largest;
    }
}
```

**Sample Output:**
```
Numbers: 25, 78, 45
Largest number (using if-else): 78
Largest number (using Math.max): 78
Largest number (using ternary): 78
```

[Back to Top](#table-of-contents)

## Find the Second Largest Number in an Array

This program finds the second largest number in an array using different approaches.

**Key Concepts:**
* **Sorting** approach (simple but less efficient for large arrays)
* **Single scan** approach (more efficient, O(n) time complexity)
* Handling **edge cases** like duplicate elements and arrays with fewer than two elements

```java
import java.util.Arrays;

public class SecondLargestInArray {
    public static void main(String[] args) {
        int[] arr1 = {12, 35, 1, 10, 34, 1};
        int[] arr2 = {10, 5, 10};
        int[] arr3 = {45, 51, 28, 75, 49, 42};
        
        // Method 1: Using sorting
        System.out.println("Using sorting approach:");
        findSecondLargestUsingSorting(arr1);
        findSecondLargestUsingSorting(arr2);
        findSecondLargestUsingSorting(arr3);
        
        // Method 2: Using single scan (more efficient)
        System.out.println("\nUsing single scan approach:");
        findSecondLargestUsingSingleScan(arr1);
        findSecondLargestUsingSingleScan(arr2);
        findSecondLargestUsingSingleScan(arr3);
    }
    
    // Find second largest using sorting
    public static void findSecondLargestUsingSorting(int[] arr) {
        // Check if array has at least 2 elements
        if (arr.length < 2) {
            System.out.println("Array should have at least 2 elements");
            return;
        }
        
        // Create a copy of the array to avoid modifying the original
        int[] sortedArr = Arrays.copyOf(arr, arr.length);
        Arrays.sort(sortedArr);
        
        // Find the second largest (need to handle duplicates)
        int largest = sortedArr[sortedArr.length - 1];
        
        // Traverse the array from right to find the first element not equal to the largest
        for (int i = sortedArr.length - 2; i >= 0; i--) {
            if (sortedArr[i] != largest) {
                System.out.println("Second largest element in " + Arrays.toString(arr) + " is: " + sortedArr[i]);
                return;
            }
        }
        
        // If all elements are the same
        System.out.println("No second largest element exists in " + Arrays.toString(arr));
    }
    
    // Find second largest using single scan (O(n) time complexity)
    public static void findSecondLargestUsingSingleScan(int[] arr) {
        // Check if array has at least 2 elements
        if (arr.length < 2) {
            System.out.println("Array should have at least 2 elements");
            return;
        }
        
        int first = Integer.MIN_VALUE;
        int second = Integer.MIN_VALUE;
        
        // Find the largest and second largest in a single scan
        for (int i = 0; i < arr.length; i++) {
            // If current element is greater than first, update both first and second
            if (arr[i] > first) {
                second = first;
                first = arr[i];
            } 
            // If current element is between first and second, update second
            else if (arr[i] > second && arr[i] != first) {
                second = arr[i];
            }
        }
        
        // Check if a second largest element was found
        if (second == Integer.MIN_VALUE) {
            System.out.println("No second largest element exists in " + Arrays.toString(arr));
        } else {
            System.out.println("Second largest element in " + Arrays.toString(arr) + " is: " + second);
        }
    }
}
```

**Sample Output:**
```
Using sorting approach:
Second largest element in [12, 35, 1, 10, 34, 1] is: 34
Second largest element in [10, 5, 10] is: 5
Second largest element in [45, 51, 28, 75, 49, 42] is: 51

Using single scan approach:
Second largest element in [12, 35, 1, 10, 34, 1] is: 34
Second largest element in [10, 5, 10] is: 5
Second largest element in [45, 51, 28, 75, 49, 42] is: 51
```

[Back to Top](#table-of-contents)