Sort employees by salary in ascending order using anonymous class# ✨ Java Stream Operations Overview ✨

## 📜 Introduction

Java Streams, introduced in Java 8, provide a powerful and declarative way to process sequences of elements. They allow for functional-style operations on collections, enabling concise and potentially parallelized data manipulation. Streams support two main types of operations: **Intermediate** and **Terminal**.

---

## ⚙️ Intermediate Operations

> Intermediate operations return another `Stream` as a result, allowing them to be chained together to form a processing pipeline.
> A key characteristic is that they are ***lazy***; they don't execute until a terminal operation is invoked on the stream pipeline.

**Common Examples:**
* `filter(Predicate<T>)`
* `map(Function<T, R>)`
* `flatMap(Function<T, Stream<R>>)`
* `mapMulti(BiConsumer<T, Consumer<R>>)` (Java 16+)
* `distinct()`
* `sorted()` / `sorted(Comparator<T>)`
* `peek(Consumer<T>)` (Mainly for debugging)
* `limit(long)`
* `skip(long)`
* `takeWhile(Predicate<T>)` (Java 9+)
* `dropWhile(Predicate<T>)` (Java 9+)

---

## 🏁 Terminal Operations

> Terminal operations produce a non-stream result, such as a primitive value, an object, a collection, or simply perform a side effect (like `forEach`).
> They trigger the ***eager*** execution of the entire stream pipeline (including all chained intermediate operations).
> A stream pipeline can have at most one terminal operation, which must be the final operation.

**Common Examples:**
* `forEach(Consumer<T>)` / `forEachOrdered(Consumer<T>)`
* `toArray()` / `toArray(IntFunction<A[]>)`
* `reduce(...)`
* `collect(...)`
* `toList()` (Java 16+)
* `min(Comparator<T>)` / `max(Comparator<T>)`
* `count()`
* `anyMatch(Predicate<T>)` / `allMatch(Predicate<T>)` / `noneMatch(Predicate<T>)`
* `findFirst()` / `findAny()`

---

## ⚖️ Intermediate vs. Terminal Operations

| Feature                  | Intermediate Operations                                  | Terminal Operations                                                    |
| :----------------------- | :------------------------------------------------------- | :--------------------------------------------------------------------- |
| **Return Type** | Returns a `Stream`.                                      | Returns a non-stream value (primitive, object, collection, `void`).    |
| **Chaining** | Can be chained together to form a pipeline.              | Cannot be chained after; terminates the pipeline.                      |
| **Pipeline Composition** | Pipeline can contain any number of intermediate ops.     | Pipeline can have max one terminal op, always at the end.              |
| **Execution (Laziness)** | **Lazy** (execution deferred until terminal op).         | **Eager** (triggers pipeline execution).                               |
| **Result** | Do not produce the final result themselves.              | Produce the final result or side effect of the pipeline.             |

---

## 🚀 Stream Creation Methods
*(This section remains the same as the previous version, detailing `Stream.of`, `Arrays.stream`, `collection.stream`, `iterate`, `generate`, etc.)*

---

## ✨ Intermediate Operations Examples ✨
*(This section remains the same as the previous version, showing isolated examples of `filter`, `map`, `sorted`, `limit`, etc.)*

---

## ✨ Terminal Operations Examples ✨
*(This section remains the same as the previous version, showing isolated examples of `forEach`, `collect`, `reduce`, `count`, etc.)*

---

## 🧩 Additional Stream Examples & Common Problems
*(This section remains the same as the previous version, showing specific problem solutions like second-smallest, common elements, character frequency, etc.)*

---

## 🏢 Comprehensive Employee Examples

This section demonstrates various stream operations applied specifically to a list of `Employee` objects, covering common data manipulation tasks.

### Employee Class Definition

* _First, let's define the `Employee` class used in these examples._
    ```java
    import java.util.Objects;

    class Employee {
        private String name;
        private int age;
        private String gender;
        private double salary;
        private String city;
        private String deptName;
        private boolean activeEmp;

        public Employee(String name, int age, String gender, double salary, String city, String deptName, boolean activeEmp) {
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.salary = salary;
            this.city = city;
            this.deptName = deptName;
            this.activeEmp = activeEmp;
        }

        // Standard Getters
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getGender() { return gender; }
        public double getSalary() { return salary; }
        public String getCity() { return city; }
        public String getDeptName() { return deptName; }
        public boolean isActiveEmp() { return activeEmp; }

        // Standard Setter for Salary (used in some examples)
        public void setSalary(double salary) { this.salary = salary; }

        @Override
        public String toString() {
            return "Employee{" + "name='" + name + '\'' + ", age=" + age + ", gender='" + gender + '\'' +
                   ", salary=" + salary + ", city='" + city + '\'' + ", deptName='" + deptName + '\'' +
                   ", activeEmp=" + activeEmp + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Employee employee = (Employee) o;
            return age == employee.age && Double.compare(employee.salary, salary) == 0 &&
                   activeEmp == employee.activeEmp && Objects.equals(name, employee.name) &&
                   Objects.equals(gender, employee.gender) && Objects.equals(city, employee.city) &&
                   Objects.equals(deptName, employee.deptName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age, gender, salary, city, deptName, activeEmp);
        }
    }
    ```

### Sample Employee Data

* _We'll use this list for the examples below._
    ```java
    import java.util.Arrays;
    import java.util.List;
    import java.util.ArrayList; // Needed for some examples later

    List<Employee> employeeList = Arrays.asList(
        new Employee("Alice", 30, "Female", 52000, "New York", "IT", true),
        new Employee("Bob", 25, "Male", 60000, "San Francisco", "Finance", false),
        new Employee("Charlie", 35, "Male", 55000, "New York", "Marketing", true),
        new Employee("David", 40, "Male", 70000, "San Francisco", "IT", true),
        new Employee("Emma", 28, "Female", 75000, "Los Angeles", "HR", false),
        new Employee("Frank", 35, "Male", 58000, "New York", "IT", true), // Duplicate name, same age as Charlie but diff salary
        new Employee("Grace", 28, "Female", 62000, "New York", "IT", true) // Same age as Emma
    );
    ```

### 🚶‍♂️ Iterating Over Employees

* _Iterating using an enhanced for-loop (traditional)._
    ```java
    System.out.println("--- Iterating with Enhanced For-Loop ---");
    for (Employee emp : employeeList) {
        System.out.println(emp);
        // Access specific fields: System.out.println(emp.getName());
    }
    ```

* _Iterating using `forEach` with a Lambda Expression._
    ```java
    System.out.println("\n--- Iterating with forEach + Lambda ---");
    employeeList.forEach(emp -> {
        System.out.println("Name: " + emp.getName() + ", Age: " + emp.getAge());
    });
    ```

* _Iterating using `forEach` with a Method Reference (prints the `toString()` representation)._
    ```java
    System.out.println("\n--- Iterating with forEach + Method Reference ---");
    employeeList.forEach(System.out::println);
    ```

* _Iterating using a Stream and `forEach` (equivalent to list's `forEach`)._
    ```java
    System.out.println("\n--- Iterating with Stream + forEach ---");
    employeeList.stream().forEach(emp -> {
        System.out.println("Dept: " + emp.getDeptName() + ", Salary: " + emp.getSalary());
    });
    ```

---

### Collecting & Filtering Employee Data

* _Collect only the names of all employees into a new List._
    ```java
    import java.util.stream.Collectors;

    System.out.println("\n--- Collecting Employee Names ---");
    List<String> employeeNames = employeeList.stream()
                                          .map(Employee::getName) // Method reference for getter
                                          // .map(emp -> emp.getName()) // Equivalent lambda
                                          .collect(Collectors.toList());
    System.out.println(employeeNames);
    ```
    > _Output: `[Alice, Bob, Charlie, David, Emma, Frank, Grace]`_

* _Filter employees older than 30._
    ```java
    System.out.println("\n--- Filtering Employees Older Than 30 ---");
    List<Employee> olderEmployees = employeeList.stream()
                                             .filter(emp -> emp.getAge() > 30)
                                             .collect(Collectors.toList());
    olderEmployees.forEach(System.out::println);
    ```
    > _Output: Includes Charlie, David, Frank_

* _Find unique and duplicate employee names._
    ```java
    import java.util.Set;
    import java.util.HashSet;

    System.out.println("\n--- Finding Unique and Duplicate Names ---");
    Set<String> uniqueNames = new HashSet<>();
    Set<String> duplicateNames = employeeList.stream()
                                            .map(Employee::getName)
                                            .filter(name -> !uniqueNames.add(name)) // .add returns false if element already exists
                                            .collect(Collectors.toSet());

    System.out.println("All Unique Names Encountered: " + uniqueNames);
    System.out.println("Duplicate Names Found: " + duplicateNames);
    ```
    > _Output:_
    > _All Unique Names Encountered: `[Alice, Bob, Charlie, David, Emma, Frank, Grace]` (order may vary)_
    > _Duplicate Names Found: `[]` (In this specific dataset, names are unique)_
    > _Note: If 'Frank' was named 'Charlie', Duplicates would be `[Charlie]`._

---

### 🧑‍🤝‍🧑 Grouping Employees

* _Group employees by their Department._
    ```java
    import java.util.Map;

    System.out.println("\n--- Grouping Employees by Department ---");
    Map<String, List<Employee>> employeesByDept = employeeList.stream()
        .collect(Collectors.groupingBy(Employee::getDeptName));

    employeesByDept.forEach((dept, emps) -> {
        System.out.println("Department: " + dept);
        emps.forEach(emp -> System.out.println("  " + emp.getName()));
    });
    ```

* _Group employees by Active Status (Active vs. Inactive)._
    ```java
    System.out.println("\n--- Grouping Employees by Active Status ---");
    Map<Boolean, List<Employee>> employeesByActiveStatus = employeeList.stream()
        .collect(Collectors.groupingBy(Employee::isActiveEmp));

    employeesByActiveStatus.forEach((isActive, emps) -> {
        System.out.println("Active: " + isActive);
        emps.forEach(emp -> System.out.println("  " + emp.getName()));
    });
    ```

* _Count employees in each Department._
    ```java
    System.out.println("\n--- Counting Employees per Department ---");
    Map<String, Long> countByDept = employeeList.stream()
        .collect(Collectors.groupingBy(Employee::getDeptName, // Group by department
                                       Collectors.counting())); // Count elements in each group
    countByDept.forEach((dept, count) -> System.out.println(dept + ": " + count));
    ```
    > _Output:_
    > _HR: 1_
    > _Finance: 1_
    > _Marketing: 1_
    > _IT: 4_

* _Group employees by City._
    ```java
    System.out.println("\n--- Grouping Employees by City ---");
    Map<String, List<Employee>> employeesByCity = employeeList.stream()
        .collect(Collectors.groupingBy(Employee::getCity));

    employeesByCity.forEach((city, emps) -> {
        System.out.println("City: " + city);
        emps.forEach(emp -> System.out.println("  " + emp.getName()));
    });
    ```

---

### 📊 Sorting Employees

* _Sort employees by Name (Ascending)._
    ```java
    import java.util.Comparator;

    System.out.println("\n--- Sorting Employees by Name (Ascending) ---");
    List<Employee> sortedByNameAsc = employeeList.stream()
        .sorted(Comparator.comparing(Employee::getName))
        //  .sorted(Comparator.comparing(Employee::getName)).reversed()
       // .sorted( (e1,e2)-> (double) (e1.getsalary(),e2.getSalary()) )
       // .sorted((e1, e2) ->  Double.compare(e1.getSalary(),e2.getSalary());
      // .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))      // Equivalent lambda
        .collect(Collectors.toList());
    sortedByNameAsc.forEach(emp -> System.out.println(emp.getName()));
    ```


    * _Sort employees by salary in ascending order using anonymous class._
    ```java
List<Employee> sortedEmployees = employees.stream()
        .sorted(new Comparator<Employee>() {
        @Override
        public int compare(Employee e1, Employee e2) {
                return (int) (e1.getSalary() - e2.getSalary());
                }
            })
        .collect(Collectors.toList());
    ```


* _Sort employees by salary in ascending order using overridden compare method._
  ```java
employees.sort(new SalaryComparator());

List<Employee> sortedEmployees = employees.stream()
.sorted(new Comparator<Employee>() {
    @Override
    public int compare(Employee e1, Employee e2) {
        return Double.compare(e1.getSalary(), e2.getSalary());
    }
})
.collect(Collectors.toList());
 ```
* _Traditional approach for implementing the comparator interface method._
 ```java
class SalaryComparator implements Comparator<Employee> {
    @Override
    public int compare(Employee e1, Employee e2) {
        return Double.compare(e1.getSalary(), e2.getSalary());
    }
}
```

* _Sort employees by Salary (Descending)._
    ```java
    System.out.println("\n--- Sorting Employees by Salary (Descending) ---");
    List<Employee> sortedBySalaryDesc = employeeList.stream()
        .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
        // .sorted((e1, e2) -> Double.compare(e2.getSalary(), e1.getSalary())) // Equivalent lambda
        .collect(Collectors.toList());
    sortedBySalaryDesc.forEach(emp -> System.out.println(emp.getName() + ": " + emp.getSalary()));
    ```

* _Sort employees by Age (Ascending)._
    ```java
    System.out.println("\n--- Sorting Employees by Age (Ascending) ---");
    List<Employee> sortedByAgeAsc = employeeList.stream()
        .sorted(Comparator.comparingInt(Employee::getAge))
        .collect(Collectors.toList());
    sortedByAgeAsc.forEach(emp -> System.out.println(emp.getName() + ": " + emp.getAge()));
    ```

* _Collect employee Ages sorted Ascending._
    ```java
    System.out.println("\n--- Collecting Sorted Employee Ages (Ascending) ---");
    List<Integer> sortedAges = employeeList.stream()
                                        .map(Employee::getAge)
                                        .sorted()
                                        .collect(Collectors.toList());
    System.out.println(sortedAges);
    ```
    > _Output: `[25, 28, 28, 30, 35, 35, 40]`_

* _Multi-level sorting: Sort by City Descending, then by Name Ascending._
    ```java
    System.out.println("\n--- Sorting by City (Desc) then Name (Asc) ---");
    Comparator<Employee> cityDescNameAsc = Comparator.comparing(Employee::getCity).reversed()
                                                    .thenComparing(Employee::getName);
    List<Employee> multiSorted1 = employeeList.stream()
                                            .sorted(cityDescNameAsc)
                                            .collect(Collectors.toList());
    multiSorted1.forEach(emp -> System.out.println(emp.getCity() + " - " + emp.getName()));
    ```

* _Multi-level sorting: Sort by City Descending, then by Name Descending._
    ```java
    System.out.println("\n--- Sorting by City (Desc) then Name (Desc) ---");
    Comparator<Employee> cityDescNameDesc = Comparator.comparing(Employee::getCity).reversed()
                                                     .thenComparing(Employee::getName, Comparator.reverseOrder());
    List<Employee> multiSorted2 = employeeList.stream()
                                            .sorted(cityDescNameDesc)
                                            .collect(Collectors.toList());
    multiSorted2.forEach(emp -> System.out.println(emp.getCity() + " - " + emp.getName()));
    ```

* _Select the top 3 Employees with the highest Salaries._
    ```java
    System.out.println("\n--- Top 3 Highest Paid Employees ---");
    List<Employee> top3Salaries = employeeList.stream()
        .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
        .limit(3)
        .collect(Collectors.toList());
    top3Salaries.forEach(System.out::println);
    ```

* _Select Employees starting from the 4th highest Salary onwards._
    ```java
    System.out.println("\n--- Employees After Top 3 Paid ---");
    List<Employee> afterTop3Salaries = employeeList.stream()
        .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
        .skip(3) // Skip the first 3
        .collect(Collectors.toList());
    afterTop3Salaries.forEach(System.out::println);
    ```

* _Select the 2nd & 3rd youngest Employees._
    ```java
    System.out.println("\n--- 2nd and 3rd Youngest Employees ---");
    List<Employee> secondAndThirdYoungest = employeeList.stream()
        .sorted(Comparator.comparingInt(Employee::getAge))
        .skip(1) // Skip the youngest
        .limit(2) // Take the next two
        .collect(Collectors.toList());
    secondAndThirdYoungest.forEach(System.out::println);
    ```
    > _Output: Includes Emma (28) and Grace (28)_

---

### 📈 Employee Statistics

* _Find the Employee with the Maximum Salary._
    ```java
    System.out.println("\n--- Employee with Maximum Salary ---");
    Optional<Employee> highestPaidEmployee = employeeList.stream()
        .collect(Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary)));
        // Alternatively: .max(Comparator.comparingDouble(Employee::getSalary));
    highestPaidEmployee.ifPresent(System.out::println);
    ```
    > _Output: Emma's record_

* _Find the Employee with the Minimum Salary._
    ```java
    System.out.println("\n--- Employee with Minimum Salary ---");
    Optional<Employee> lowestPaidEmployee = employeeList.stream()
        .collect(Collectors.minBy(Comparator.comparingDouble(Employee::getSalary)));
        // Alternatively: .min(Comparator.comparingDouble(Employee::getSalary));
    lowestPaidEmployee.ifPresent(System.out::println);
    ```
    > _Output: Alice's record_

* _Calculate Summary Statistics for Employee Ages (Count, Sum, Min, Average, Max)._
    ```java
    import java.util.IntSummaryStatistics;

    System.out.println("\n--- Summary Statistics for Employee Ages ---");
    IntSummaryStatistics ageStats = employeeList.stream()
        .mapToInt(Employee::getAge) // Convert to IntStream
        .summaryStatistics(); // Calculate stats

    System.out.println("Count: " + ageStats.getCount());
    System.out.println("Sum: " + ageStats.getSum());
    System.out.println("Min: " + ageStats.getMin());
    System.out.println("Average: " + ageStats.getAverage());
    System.out.println("Max: " + ageStats.getMax());
    ```
    > _Output:_
    > _Count: 7_
    > _Sum: 221_
    > _Min: 25_
    > _Average: 31.57..._
    > _Max: 40_

* _Calculate Summary Statistics for Employee Salaries._
    ```java
    import java.util.DoubleSummaryStatistics;

    System.out.println("\n--- Summary Statistics for Employee Salaries ---");
    DoubleSummaryStatistics salaryStats = employeeList.stream()
        .mapToDouble(Employee::getSalary) // Convert to DoubleStream
        .summaryStatistics(); // Calculate stats

    System.out.println("Count: " + salaryStats.getCount());
    System.out.println("Sum: " + salaryStats.getSum());
    System.out.println("Min: " + salaryStats.getMin());
    System.out.println("Average: " + salaryStats.getAverage());
    System.out.println("Max: " + salaryStats.getMax());
    ```

* _Find the Highest Salary in each Department._
    ```java
    import java.util.function.BinaryOperator;

    System.out.println("\n--- Highest Salary per Department ---");
    Map<String, Optional<Employee>> highestSalaryByDept = employeeList.stream()
        .collect(Collectors.groupingBy(
            Employee::getDeptName,
            Collectors.reducing(BinaryOperator.maxBy(Comparator.comparingDouble(Employee::getSalary)))
        ));

    highestSalaryByDept.forEach((dept, empOpt) -> {
        System.out.print("Dept: " + dept + ", Highest Paid: ");
        empOpt.ifPresent(emp -> System.out.println(emp.getName() + " (" + emp.getSalary() + ")"));
    });
    ```

---

### ⚠️ Stream Re-use Caveat

* _Streams cannot be reused after a terminal operation is called. Attempting to do so results in an `IllegalStateException`._
    ```java
    Stream<String> namesStream = employeeList.stream().map(Employee::getName);
    System.out.println("\n--- Stream Re-use Attempt ---");
    System.out.println("First consumption (forEach):");
    namesStream.forEach(System.out::println); // Consumes the stream

    try {
        // Attempting to reuse the *same* stream instance
        long count = namesStream.count(); // This will fail
        System.out.println("Count (should not be reached): " + count);
    } catch (IllegalStateException e) {
        System.out.println("Error trying to reuse stream: " + e.getMessage());
    }
    ```
    > _Output: Will print names, then throw `IllegalStateException: stream has already been operated upon or closed`_

* _To perform multiple operations, either recreate the stream or use a `Supplier`._
    ```java
    import java.util.function.Supplier;

    // Option 1: Recreate the stream
    System.out.println("\n--- Recreating Stream ---");
    employeeList.stream().map(Employee::getName).forEach(System.out::println);
    long countRecreated = employeeList.stream().map(Employee::getName).count();
    System.out.println("Count (recreated): " + countRecreated);

    // Option 2: Use a Supplier
    System.out.println("\n--- Using Supplier for Stream ---");
    Supplier<Stream<Employee>> employeeStreamSupplier = () -> employeeList.stream();

    System.out.println("First consumption via supplier:");
    employeeStreamSupplier.get().map(Employee::getName).forEach(System.out::println); // Gets a new stream

    System.out.println("Second consumption via supplier:");
    long countSupplier = employeeStreamSupplier.get().count(); // Gets another new stream
    System.out.println("Count (supplier): " + countSupplier);
    ```

---

## ✅ Conclusion

Java Streams offer a flexible and expressive paradigm for data processing. Mastering the distinction between **lazy intermediate operations** and **eager terminal operations**, along with understanding how to chain them effectively, unlocks the power of the Stream API for writing clean, declarative, and efficient Java code. Common problems, especially when working with collections of objects like `Employee`, can often be solved elegantly using combinations of stream operations like `filter`, `map`, `reduce`, `collect` (especially `groupingBy`), `sorted`, `distinct`, and summary statistics methods. Remember the single-consumption nature of streams and plan accordingly for multiple operations.
