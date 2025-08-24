package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.api.model.EmployeeResponse;
import com.reliaquest.api.service.EmployeeService;
import com.reliaquest.api.translator.EmployeeTranslator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;

    @Mock
    private EmployeeTranslator employeeTranslator;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeResponse employee;
    private EmployeeInput employeeInput;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new EmployeeResponse();
        employee.setId(UUID.fromString("2f1a8de6-aeeb-4a99-b085-2b9cba532f44"));
        employee.setName("Swapnil Patil");
        employee.setSalary(100000);
        employee.setAge(30);
        employee.setTitle("Developer");
        employee.setEmail("swapnil@company.com");

        employeeInput = new EmployeeInput("Swapnil Patil", 100000, 30, "Developer");
    }

    @Test
    @DisplayName("Test getAllEmployees - Success")
    void testGetAllEmployees_Success() throws Exception {
        // When
        when(employeeService.fetchAllEmployees()).thenReturn(Arrays.asList(employee));

        // Then
        ResponseEntity<List<EmployeeResponse>> response = employeeController.getAllEmployees();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Test getAllEmployees - Empty List")
    void testGetAllEmployees_Empty() throws Exception {
        // When
        when(employeeService.fetchAllEmployees()).thenReturn(Collections.emptyList());

        // Then
        ResponseEntity<List<EmployeeResponse>> response = employeeController.getAllEmployees();

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Test getEmployeesByNameSearch - Success")
    void testGetEmployeesByNameSearch_Success() throws Exception {
        // When
        when(employeeService.getEmployeesByNameSearch("Patil")).thenReturn(Arrays.asList(employee));

        // Then
        ResponseEntity<List<EmployeeResponse>> response = employeeController.getEmployeesByNameSearch("Patil");

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Test getEmployeesByNameSearch - Empty List")
    void testGetEmployeesByNameSearch_Empty() throws Exception {
        // When
        when(employeeService.getEmployeesByNameSearch("Prerana")).thenReturn(Collections.emptyList());

        // Then
        ResponseEntity<List<EmployeeResponse>> response = employeeController.getEmployeesByNameSearch("Prerana");

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Test getEmployeeById - Success")
    void testGetEmployeeById_Success() throws Exception {
        // When
        when(employeeService.getEmployeeById("2f1a8de6-aeeb-4a99-b085-2b9cba532f44"))
                .thenReturn(employee);

        // Then
        ResponseEntity<EmployeeResponse> response =
                employeeController.getEmployeeById("2f1a8de6-aeeb-4a99-b085-2b9cba532f44");

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Swapnil Patil", response.getBody().getName());
    }

    @Test
    @DisplayName("Test getEmployeeById - Not Found")
    void testGetEmployeeById_NotFound() throws Exception {
        // When
        when(employeeService.getEmployeeById("uuid-999")).thenReturn(null);

        // Then
        ResponseEntity<EmployeeResponse> response = employeeController.getEmployeeById("uuid-999");

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Test getHighestSalaryOfEmployees - Success")
    void testGetHighestSalaryOfEmployees() throws Exception {
        // When
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(100000);

        // Then
        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100000, response.getBody());
    }

    @Test
    @DisplayName("Test getTopTenHighestEarningEmployeeNames - Success")
    void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        // When
        when(employeeService.getTop10HighestEarningEmployeeNames())
                .thenReturn(Arrays.asList("Swapnil Patil", "Prerana Patil"));

        // Then
        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    @DisplayName("Test createEmployee - Success")
    void testCreateEmployee_Success() throws Exception {
        // When
        when(employeeService.createEmployee(employeeInput)).thenReturn(employee);

        // Then
        ResponseEntity<EmployeeResponse> response = employeeController.createEmployee(employeeInput);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Swapnil Patil", response.getBody().getName());
    }

    @Test
    @DisplayName("Test deleteEmployeeById - Success")
    void testDeleteEmployeeById_Success() throws Exception {
        // When
        when(employeeService.deleteEmployeeById("2f1a8de6-aeeb-4a99-b085-2b9cba532f44"))
                .thenReturn("Swapnil Patil");

        // Then
        ResponseEntity<String> response = employeeController.deleteEmployeeById("2f1a8de6-aeeb-4a99-b085-2b9cba532f44");

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Swapnil Patil", response.getBody());
    }

    @Test
    @DisplayName("Test deleteEmployeeById - Not Found")
    void testDeleteEmployeeById_NotFound() throws Exception {
        // When
        when(employeeService.deleteEmployeeById("uuid-999")).thenReturn(null);

        // Then
        ResponseEntity<String> response = employeeController.deleteEmployeeById("uuid-999");

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
