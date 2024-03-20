package com.employee.employee_management;

public class Employee {

    private String employeeId;
    private String name;
    private String city;

    public Employee() {
    }

    public Employee(String employeeId, String name, String city) {
        this.employeeId = employeeId;
        this.name = name;
        this.city = city;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
