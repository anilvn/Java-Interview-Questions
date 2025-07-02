

---

```java
Integer min = numbers.stream().min(Integer::compare).get();                  // Minimum integer
Integer max = numbers.stream().max(Integer::compare).get();                  // Maximum integer
int sum = numbers.stream().reduce(0, Integer::sum);                          // Sum of integers

String minStr = names.stream().min(String::compareTo).get();                // Lexicographically smallest string
String maxStr = names.stream().max(String::compareTo).get();                // Lexicographically largest string
String combined = names.stream().reduce("", String::concat);                // Concatenate strings

LocalDate earliest = dates.stream().min(LocalDate::compareTo).get();        // Earliest date
LocalDate latest = dates.stream().max(LocalDate::compareTo).get();          // Latest date

Employee lowestPaid = employees.stream().min(Comparator.comparing(Employee::getSalary)).get();   // Employee with min salary
Employee highestPaid = employees.stream().max(Comparator.comparing(Employee::getSalary)).get();  // Employee with max salary


Double minD = doubles.stream().min(Double::compare).get();                         // Min double
Double maxD = doubles.stream().max(Double::compare).get();                         // Max double
double sumD = doubles.stream().reduce(0.0, Double::sum);                           // Sum double

Long minL = longs.stream().min(Long::compare).get();                               // Min long
Long maxL = longs.stream().max(Long::compare).get();                               // Max long
long sumL = longs.stream().reduce(0L, Long::sum);                                  // Sum long

BigInteger minBI = bigIntegers.stream().min(BigInteger::compareTo).get();          // Min BigInteger
BigInteger maxBI = bigIntegers.stream().max(BigInteger::compareTo).get();          // Max BigInteger
BigInteger sumBI = bigIntegers.stream().reduce(BigInteger.ZERO, BigInteger::add);  // Sum BigInteger

BigDecimal minBD = bigDecimals.stream().min(BigDecimal::compareTo).get();          // Min BigDecimal
BigDecimal maxBD = bigDecimals.stream().max(BigDecimal::compareTo).get();          // Max BigDecimal
BigDecimal sumBD = bigDecimals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);  // Sum BigDecimal

map1.forEach((k, v) -> result.merge(k, v, Integer::sum));                   // Merge map by summing values
map1.forEach((k, v) -> result.merge(k, v, Integer::max));                   // Merge map by keeping max value
map1.forEach((k, v) -> result.merge(k, v, (oldVal, newVal) -> newVal));     // Merge map by replacing with new value
map1.forEach((k, v) -> result.merge(k, v, (oldVal, newVal) -> oldVal));     // Merge map by keeping old value
map1.forEach((k, v) -> result.merge(k, v, (o, n) -> { throw new IllegalStateException("Duplicate key: " + k); })); // Throw on duplicate
```


```
/*
  🔍 ::compare vs ::compareTo — Stream Min/Max Cheat Sheet

  ✅ Integer::compare — static method (takes two args)
     - Use with primitive wrapper types like Integer, Long, Double
     - Signature: public static int compare(Integer a, Integer b)

     Example:
     Integer min = numbers.stream().min(Integer::compare).get();

  ✅ String::compareTo — instance method (one arg, called on the object)
     - Use with types that implement Comparable: String, BigDecimal, LocalDate, etc.
     - Signature: public int compareTo(T other)

     Example:
     String max = names.stream().max(String::compareTo).get();
     BigDecimal min = decimals.stream().min(BigDecimal::compareTo).get();

  ✅ Comparator.comparing(...) — for custom comparison (by field)
     - Use when comparing objects by a specific property
     - Signature: Comparator.comparing(Function<T,R> keyExtractor)

     Example:
     Employee top = employees.stream()
                             .max(Comparator.comparing(Employee::getSalary))
                             .get();

  ✅ Summary:
     - Use Class::compare ➝ for primitive wrapper static methods (Integer, Long, Double)
     - Use Class::compareTo ➝ for objects that implement Comparable
     - Use Comparator.comparing ➝ for custom field-based object comparison

  ✅ Golden Rule:
     - Two inputs? ➝ use ::compare (static)
     - One input?  ➝ use ::compareTo (instance)
*/


// Integer - use static compare
Stream.of(3, 1, 5).min(Integer::compare);

// String - use compareTo
Stream.of("cat", "apple", "banana").max(String::compareTo);

// BigDecimal - use compareTo
Stream.of(new BigDecimal("10.5"), new BigDecimal("8.1")).min(BigDecimal::compareTo);

// LocalDate - use compareTo
Stream.of(LocalDate.now(), LocalDate.of(2020, 1, 1)).min(LocalDate::compareTo);
```

<br/><br/>

```java
// 📊 IntSummaryStatistics collects count, sum, min, max, avg in one pass
IntSummaryStatistics stats = numbers.stream()
    .mapToInt(Integer::intValue)  // convert to IntStream
    .summaryStatistics();

// ✅ Access via: stats.getCount(), getSum(), getMin(), getMax(), getAverage()

// 🔁 For long values:
LongSummaryStatistics longStats = longs.stream()
    .mapToLong(Long::longValue)
    .summaryStatistics();

// 🔁 For double values:
DoubleSummaryStatistics doubleStats = doubles.stream()
    .mapToDouble(Double::doubleValue)
    .summaryStatistics();

// ❌ No summaryStatistics for BigDecimal/BigInteger — use reduce manually
BigDecimal sum = bigDecimals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
// BigDecimal avg = sum.divide(BigDecimal.valueOf(bigDecimals.size()), RoundingMode.HALF_UP);
```

---

### ✅ Full Example: Manual Summary for `BigDecimal`

```java
List<BigDecimal> decimals = List.of(
    new BigDecimal("10.50"),
    new BigDecimal("25.75"),
    new BigDecimal("8.25")
);

// 📌 Count
long count = decimals.size();

// 📌 Sum
BigDecimal sum = decimals.stream()
    .reduce(BigDecimal.ZERO, BigDecimal::add);

// 📌 Min
BigDecimal min = decimals.stream()
    .min(BigDecimal::compareTo)
    .orElse(BigDecimal.ZERO);  // or throw exception based on use case

/*
    if you use - .get() instead of  .orElse(BigDecimal.ZERO)
    -----------------------------------------------------------
    Exception in thread "main" java.util.NoSuchElementException: No value present
	at java.base/java.util.Optional.get(Optional.java:143)
	at javaBasics/javaBasics.MinExample.main(MinExample.java:35)
*/

// 📌 Max
BigDecimal max = decimals.stream()
    .max(BigDecimal::compareTo)
    .orElse(BigDecimal.ZERO);

// 📌 Average (with scale and rounding mode)
BigDecimal average = count > 0
    ? sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP)
    : BigDecimal.ZERO;
```

---

### 🧠 Notes:

* Use `.reduce(BigDecimal.ZERO, BigDecimal::add)` for summing.
* Use `.min(BigDecimal::compareTo)` / `.max(...)` for comparison.
* Use `BigDecimal.valueOf(count)` to ensure proper division.
* Always specify `scale` and `RoundingMode` when dividing BigDecimals.

---

### 🧪 Output (based on above list):

```java
count   = 3
sum     = 44.50
min     = 8.25
max     = 25.75
average = 14.83
```
