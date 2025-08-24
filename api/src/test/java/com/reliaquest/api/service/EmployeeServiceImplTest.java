package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.api.model.EmployeeResponse;
import com.reliaquest.api.translator.EmployeeTranslator;
import com.reliaquest.server.model.CreateMockEmployeeInput;
import com.reliaquest.server.model.DeleteMockEmployeeInput;
import com.reliaquest.server.model.MockEmployee;
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

class EmployeeServiceImplTest {
    @Mock
    private com.reliaquest.server.service.MockEmployeeService mockEmployeeService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EmployeeTranslator employeeTranslator;

    @Mock
    private EmployeeInput employeeInput;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private MockEmployee mockEmployee;

    private EmployeeResponse employee;
    private CreateMockEmployeeInput createMockEmployeeInput;
    private DeleteMockEmployeeInput deleteMockEmployeeInput;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockEmployee =
                new MockEmployee(UUID.randomUUID(), "Swapnil Patil", 100000, 30, "Developer", "swapnil@company.com");
        employeeInput = new EmployeeInput("Swapnil Patil", 100000, 30, "Developer");

        // Setting up the expected Employee object
        employee = new EmployeeResponse();
        employee.setId(UUID.fromString("7b9b1e3c-2123-4f91-9b0c-e801632b0e22"));
        employee.setName("Swapnil Patil");
        employee.setAge(30);
        employee.setTitle("Engineer");
        employee.setSalary(100000);
        employee.setEmail("swapnil@example.com");

        // Setting up CreateMockEmployeeInput
        createMockEmployeeInput = new CreateMockEmployeeInput();
        createMockEmployeeInput.setName("Swapnil Patil");
        createMockEmployeeInput.setSalary(100000);
        createMockEmployeeInput.setAge(30);
        createMockEmployeeInput.setTitle("Developer");

        deleteMockEmployeeInput = new DeleteMockEmployeeInput();
    }

    @Test
    @DisplayName("Test getAllEmployees - Success")
    void testGetAllEmployees_Success() {
        // When
        when(mockEmployeeService.getMockEmployees()).thenReturn(Collections.singletonList(mockEmployee));
        when(employeeTranslator.convertToEmployee(mockEmployee)).thenReturn(employee);

        // Then
        List<EmployeeResponse> result = employeeService.fetchAllEmployees();

        // Assertions
        assertEquals(1, result.size());
        assertEquals("Swapnil Patil", result.get(0).getName());
    }

    @Test
    @DisplayName("Test getAllEmployees - Empty")
    void testGetAllEmployees_Empty() {
        // When
        when(mockEmployeeService.getMockEmployees()).thenReturn(Collections.emptyList());

        // Then
        List<EmployeeResponse> result = employeeService.fetchAllEmployees();

        // Assertions
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test getEmployeesByNameSearch - Success")
    void testGetEmployeesByNameSearch_Success() {
        // When
        when(mockEmployeeService.getMockEmployees()).thenReturn(Collections.singletonList(mockEmployee));
        when(employeeTranslator.convertToEmployee(mockEmployee)).thenReturn(employee);

        // Then
        List<EmployeeResponse> result = employeeService.getEmployeesByNameSearch("Swapnil");

        // Assertions
        assertEquals(1, result.size());
        assertEquals("Swapnil Patil", result.get(0).getName());
    }

    @Test
    @DisplayName("Test getEmployeesByNameSearch - Empty")
    void testGetEmployeesByNameSearch_Empty() {
        // When
        when(mockEmployeeService.getMockEmployees()).thenReturn(Collections.emptyList());

        // Then
        List<EmployeeResponse> result = employeeService.getEmployeesByNameSearch("Borse");

        // Assertions
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test getEmployeeById - Success")
    void testGetEmployeeById_Success() {
        // Given
        UUID id = mockEmployee.getId();

        // When
        when(mockEmployeeService.getMockEmployees()).thenReturn(Collections.singletonList(mockEmployee));
        when(employeeTranslator.convertToEmployee(mockEmployee)).thenReturn(employee);

        // Then
        EmployeeResponse result = employeeService.getEmployeeById(id.toString());

        // Assertions
        assertNotNull(result);
        assertEquals("Swapnil Patil", result.getName());
    }

    @Test
    @DisplayName("Test getEmployeeById - Invalid UUID")
    void testGetEmployeeById_InvalidUUID() {
        // Then
        Exception ex =
                assertThrows(IllegalArgumentException.class, () -> employeeService.getEmployeeById("invalid-uuid"));

        // Assertions
        assertEquals("Invalid employee ID format", ex.getMessage());
    }

    @Test
    @DisplayName("Test getEmployeeById - Not Found")
    void testGetEmployeeById_NotFound() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(mockEmployeeService.getMockEmployees()).thenReturn(Collections.emptyList());

        // Then
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.getEmployeeById(id.toString());
        });

        // Assertions
        assertTrue(ex.getMessage().contains("Employee not found for ID"));
    }

    @Test
    @DisplayName("Test getHighestSalaryOfEmployees - Success")
    void testGetHighestSalaryOfEmployees() {
        // When
        when(mockEmployeeService.getMockEmployees()).thenReturn(Arrays.asList(mockEmployee));
        // Then
        Integer result = employeeService.getHighestSalaryOfEmployees();

        // Assertions
        assertEquals(100000, result);
    }

    @Test
    @DisplayName("Test getHighestSalaryOfEmployees - Empty")
    void testGetHighestSalaryOfEmployees_Empty() {
        // When
        when(mockEmployeeService.getMockEmployees()).thenReturn(Collections.emptyList());

        // Then
        Integer result = employeeService.getHighestSalaryOfEmployees();
        // Assertions
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Test getTop10HighestEarningEmployeeNames - Success")
    void testGetTop10HighestEarningEmployeeNames() {
        // GIven
        MockEmployee emp2 =
                new MockEmployee(UUID.randomUUID(), "Prerana Patil", 200000, 28, "Lead", "prerana@company.com");
        // When
        when(mockEmployeeService.getMockEmployees()).thenReturn(Arrays.asList(mockEmployee, emp2));
        // Then
        List<String> result = employeeService.getTop10HighestEarningEmployeeNames();

        // Assertions
        assertEquals(2, result.size());
        assertEquals("Prerana Patil", result.get(0));
        assertEquals("Swapnil Patil", result.get(1));
    }

    @Test
    @DisplayName("Test deleteEmployeeById - Success")
    void testDeleteEmployeeById_Success() {
        // Given
        UUID id = mockEmployee.getId();

        // When
        when(mockEmployeeService.getMockEmployees()).thenReturn(Collections.singletonList(mockEmployee));
        when(objectMapper.convertValue(mockEmployee, DeleteMockEmployeeInput.class))
                .thenReturn(deleteMockEmployeeInput);
        // Then
        String result = employeeService.deleteEmployeeById(id.toString());

        // Assertions
        assertEquals("Swapnil Patil", result);
    }

    @Test
    @DisplayName("Test deleteEmployeeById - Invalid UUID")
    void testDeleteEmployeeById_InvalidUUID() {
        // Then
        Exception ex =
                assertThrows(IllegalArgumentException.class, () -> employeeService.deleteEmployeeById("invalid-uuid"));

        // Assertions
        assertEquals("Invalid employee ID format", ex.getMessage());
    }

    @Test
    @DisplayName("Test deleteEmployeeById - Not Found")
    void testDeleteEmployeeById_NotFound() {
        // Given
        UUID id = UUID.randomUUID();
        // When
        when(mockEmployeeService.getMockEmployees()).thenReturn(Collections.emptyList());
        // Then
        Exception ex =
                assertThrows(IllegalArgumentException.class, () -> employeeService.deleteEmployeeById(id.toString()));

        // Assertions
        assertTrue(ex.getMessage().contains("Employee not found for ID"));
    }

    @Test
    @DisplayName("Test createEmployee - Success")
    void testCreateEmployee_Success() {
        // When
        when(objectMapper.convertValue(employeeInput, CreateMockEmployeeInput.class))
                .thenReturn(createMockEmployeeInput);
        when(mockEmployeeService.create(createMockEmployeeInput)).thenReturn(mockEmployee);
        when(employeeTranslator.convertToEmployee(mockEmployee)).thenReturn(employee);

        // Then
        EmployeeResponse result = employeeService.createEmployee(employeeInput);

        // Assertions
        assertNotNull(result);
        assertEquals("Swapnil Patil", result.getName());
    }

    @Test
    @DisplayName("Test createEmployee - Null Input")
    void testCreateEmployee_NullInput() {
        // Then
        Exception ex = assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(null));

        // Assertions
        assertEquals("Employee input is null", ex.getMessage());
    }

    @Test
    @DisplayName("Test createEmployee - Validation Error")
    void testCreateEmployee_ValidationError() {
        // Given
        EmployeeInput mockInput = org.mockito.Mockito.mock(EmployeeInput.class);

        // When
        when(mockInput.getValidationError()).thenReturn("Salary must be positive");

        // Then
        Exception ex = assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(mockInput));

        // Assertions
        assertEquals("Salary must be positive", ex.getMessage());
    }
}
