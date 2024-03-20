package com.employee.employee_management;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


@RestController
public class EmployeeController {

    private static final String FILE_PATH = "/home/arido/Documents/Load_balancing/App/employees.json";
//    private static final String FILE_PATH = "employees.json";
    private final Map<String, Employee> employees;

    public EmployeeController() {
        this.employees = loadEmployeesFromFile();
    }

    private Map<String, Employee> loadEmployeesFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(FILE_PATH));
            return mapper.readValue(jsonData, new TypeReference<Map<String, Employee>>() {});
        } catch (IOException e) {
            // If file doesn't exist, return an empty map
            return new HashMap<>();
        }
    }

    private void saveEmployeesToFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(FILE_PATH), employees);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception if necessary
            System.out.println("test failed error"+e.toString());
        }
    }

    @GetMapping("/greeting")
    public String greeting() {
//        return this.employees.toString();
        return "Hello world!";
//        return res;
    }

    @PostMapping("/employee")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        employee.setEmployeeId(UUID.randomUUID().toString());
        employees.put(employee.getEmployeeId(), employee);
        saveEmployeesToFile(); // Save to JSON file
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable String id) {
        Employee employee = employees.get(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/employees/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(new ArrayList<>(employees.values()));
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @RequestBody Employee updatedEmployee) {
        Employee employee = employees.get(id);
        if (employee != null) {
            employee.setName(updatedEmployee.getName());
            employee.setCity(updatedEmployee.getCity());
            saveEmployeesToFile(); // Save to JSON file
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/employee/all")
    public ResponseEntity<String> deleteAllEmployees() {
        employees.clear();
        saveEmployeesToFile(); // Save to JSON file
        return ResponseEntity.ok("All employees deleted successfully");
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable String id) {
        Employee employee = employees.remove(id);
        if (employee != null) {
            saveEmployeesToFile(); // Save to JSON file
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
