### code example for flatMap
```java
List<Customer> customers = List.of(
    new Customer("Alice", List.of(new Order("A1"), new Order("A2"))),
    new Customer("Bob", List.of(new Order("B1"))),
    new Customer("Charlie", List.of(new Order("C1"), new Order("C2"), new Order("C3")))
);

List<String> allOrderIds = customers.stream()
    .flatMap(customer -> customer.getOrders().stream())
    .map(Order::getOrderId)
    .collect(Collectors.toList());

System.out.println(allOrderIds);
// Output: [A1, A2, B1, C1, C2, C3]
```
<br/>

```java
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Employee> employees = List.of(
            new Employee("Alice", List.of(new Address("Hyderabad","50003"), new Address("Delhi","10010"))),
            new Employee("Bob", List.of(new Address("Mumbai","12133"), new Address("Hyderabad","53355"))),
            new Employee("Charlie", List.of(new Address("Delhi","41515"), new Address("Bangalore","53535")))
        );

        List<String> distinctCityNames = employees.stream()
            .flatMap(emp -> emp.getAddresses().stream())     // Stream<Address>
            .map(Address::getCity)                           // Stream<String>
            .distinct()                                      // Remove duplicates
            .collect(Collectors.toList());

        System.out.println(distinctCityNames);
        // Output: [Hyderabad, Delhi, Mumbai, Bangalore]
    }
}
```