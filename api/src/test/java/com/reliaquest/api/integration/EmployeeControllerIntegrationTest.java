package com.reliaquest.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.model.EmployeeInput;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Integration: Get all employees")
    void testGetAllEmployees() throws Exception {
        mockMvc.perform(get("/api/v1/employees").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    @DisplayName("Integration: Create employee - success")
    void testCreateEmployee_Success() throws Exception {
        EmployeeInput input = new EmployeeInput("Swapnil Patil", 50000, 30, "Developer");
        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Swapnil Patil"));
    }

    @Test
    @DisplayName("Integration: Get employee by id - not found")
    void testGetEmployeeById_NotFound() throws Exception {
        String randomId = UUID.randomUUID().toString();
        mockMvc.perform(get("/api/v1/employees/" + randomId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Integration: Get top 10 highest earning employee names")
    void testGetTop10HighestEarningEmployeeNames() throws Exception {
        mockMvc.perform(get("/api/v1/employees/top10HighestEarningEmployeeNames")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @DisplayName("Integration: Delete employee by id")
    void testDeleteEmployeeById() throws Exception {
        // First create an employee to ensure the id exists
        EmployeeInput input = new EmployeeInput("Swapnil Patil", 60000, 28, "QA Engineer");
        String response = mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String id = objectMapper.readTree(response).get("id").asText();

        // Delete the created employee
        mockMvc.perform(delete("/api/v1/employees/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Swapnil Patil"));
    }

    @Test
    @DisplayName("Integration: Get highest salary of employees (with known data)")
    void testGetHighestSalaryOfEmployees_WithKnownData() throws Exception {
        // Create two employees with different salaries
        EmployeeInput input1 = new EmployeeInput("Amay", 70000, 32, "Engineer");
        EmployeeInput input2 = new EmployeeInput("Sharma", 90000, 40, "Manager");
        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input2)))
                .andExpect(status().isCreated());

        // Now check response for highest salary
        mockMvc.perform(get("/api/v1/employees/highestSalary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
