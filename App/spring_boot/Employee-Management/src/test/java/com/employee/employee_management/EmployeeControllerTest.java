package com.employee.employee_management;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    private static final String FILE_PATH = "/home/arido/Documents/Load_balancing/App/employees.json";
//    Path current_path = Paths.get("").toAbsolutePath();
//    private static final String FILE_PATH = "employees.json";
    private final Map<String, Employee> employees;

    public EmployeeControllerTest() {
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
    private String get_test_id(){
        String firstKey = "";
        for (Map.Entry<String, Employee> entry : this.employees.entrySet()) {
            firstKey = entry.getKey();
            break; // Exit loop after getting the first entry
        }
        return firstKey;
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGreetingEndpoint() throws Exception {
        mockMvc.perform(get("/greeting"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello world!"));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        String requestBody = "{\"name\":\"Big(O)\",\"city\":\"Mumbai\"}";

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Big(O)"))
                .andExpect(jsonPath("$.city").value("Mumbai"))
                .andExpect(jsonPath("$.employeeId").exists());
    }

    @Test
    public void testGetEmployee() throws Exception {
        String employeeId = this.get_test_id(); // Assuming employee with ID 1 exists

        mockMvc.perform(get("/employee/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(employeeId));
    }

    @Test
    public void testGetEmployeeWithNonExistingId() throws Exception {
        String nonExistingId = "1000"; // Assuming employee with ID 1000 does not exist

        mockMvc.perform(get("/employee/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        mockMvc.perform(get("/employees/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        String employeeId = this.get_test_id(); // Assuming employee with ID 1 exists
        String updatedEmployeeData = "{\"name\":\"New Name\",\"city\":\"New City\"}";

        mockMvc.perform(put("/employee/{id}", employeeId) // Include employeeId in the URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(employeeId))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.city").value("New City"));
    }


    @Test
    public void testUpdateEmployeeWithNonExistingId() throws Exception {
        String nonExistingId = "1000"; // Assuming employee with ID 1000 does not exist
        String updatedEmployeeData = "{\"name\":\"New Name\",\"city\":\"New City\"}";

        mockMvc.perform(put("/employee/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeData))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteEmployee() throws Exception {
//        String employeeId = "1"; // Assuming employee with ID 1 exists

        String employeeId = this.get_test_id();
        this.employees.remove(employeeId);
        mockMvc.perform(delete("/employee/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(employeeId));
    }

    @Test
    public void testDeleteEmployeeWithNonExistingId() throws Exception {
        String nonExistingId = "1000"; // Assuming employee with ID 1000 does not exist

        mockMvc.perform(delete("/employee/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }
}
