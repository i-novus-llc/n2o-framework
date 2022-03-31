package net.n2oapp.framework.sandbox.cases.nesting_object_fields;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EmployeeService {
    private SortedMap<Integer, Employee> employees = new TreeMap<>();

    public EmployeeService() {
        Organization organization = new Organization("2", "org2");
        List<Department> departments = Arrays.asList(
                new Department("3", "department3")
        );
        employees.put(1, new Employee(1, "JOE", organization, departments));


        organization = new Organization("1", "org1");
        departments = Arrays.asList(
                new Department("1", "department1"),
                new Department("2", "department2")
        );
        employees.put(2, new Employee(2, "ANN", organization, departments));
    }

    public Object findAll() {
        return new ArrayList<>(employees.values());
    }

    public Employee findOne(Integer id) {
        return employees.get(id);
    }

    public Employee create(Employee employee) {
        employee.setId(employees.size() + 1);
        employees.put(employee.getId(), employee);
        return employee;
    }

    public void update(Employee employee) {
        if (employees.containsKey(employee.getId()))
            employees.put(employee.getId(), employee);
    }

    public void delete(Integer id) {
        employees.remove(id);
    }
}
