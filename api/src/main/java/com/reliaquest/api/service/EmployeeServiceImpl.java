package com.reliaquest.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.constants.EmployeeConstant;
import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.api.model.EmployeeResponse;
import com.reliaquest.api.translator.EmployeeTranslator;
import com.reliaquest.server.model.CreateMockEmployeeInput;
import com.reliaquest.server.model.DeleteMockEmployeeInput;
import com.reliaquest.server.model.MockEmployee;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
@Retry(name = "employeeApi", fallbackMethod = "fallbackEmployees")
public class EmployeeServiceImpl implements EmployeeService {

    private final com.reliaquest.server.service.MockEmployeeService mockEmployeeService;
    private final ObjectMapper objectMapper;
    private final EmployeeTranslator employeeTranslator;

    @Override
    public List<EmployeeResponse> fetchAllEmployees() {
        log.info("Fetching all employees from mock service via REST TEMPLATE");
        return mockEmployeeService.getMockEmployees().stream()
                .map(employeeTranslator::convertToEmployee)
                .toList();
    }

    @Override
    public List<EmployeeResponse> getEmployeesByNameSearch(String searchString) {
        log.info("Searching employees with name containing: {}", searchString);
        return mockEmployeeService.getMockEmployees().stream()
                .filter(e -> e.getName().toLowerCase().contains(searchString.toLowerCase()))
                .map(employeeTranslator::convertToEmployee)
                .toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(String id) {
        log.info("Fetching employee by ID: {}", id);
        if (!isValidUUID(id)) {
            throw new IllegalArgumentException(EmployeeConstant.INVALID_EMPLOYEE_ID_FORMAT);
        }
        Optional<MockEmployee> mockOpt = mockEmployeeService.getMockEmployees().stream()
                .filter(e -> id.equalsIgnoreCase(e.getId().toString()))
                .findFirst();
        if (mockOpt.isEmpty()) {
            throw new IllegalArgumentException(EmployeeConstant.EMPLOYEE_NOT_FOUND_FOR_ID + id);
        }
        return employeeTranslator.convertToEmployee(mockOpt.get());
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        log.info("Calculating highest salary among employees");
        return mockEmployeeService.getMockEmployees().stream()
                .map(MockEmployee::getSalary)
                .max(Integer::compareTo)
                .orElse(0);
    }

    @Override
    public List<String> getTop10HighestEarningEmployeeNames() {
        log.info("Fetching top 10 highest earning employee names");
        return mockEmployeeService.getMockEmployees().stream()
                .sorted(Comparator.comparingInt(MockEmployee::getSalary).reversed())
                .limit(10)
                .map(MockEmployee::getName)
                .toList();
    }

    @Override
    public String deleteEmployeeById(String id) {
        log.info("Deleting employee by ID: {}", id);
        if (!isValidUUID(id)) {
            throw new IllegalArgumentException(EmployeeConstant.INVALID_EMPLOYEE_ID_FORMAT);
        }
        Optional<MockEmployee> mockOpt = mockEmployeeService.getMockEmployees().stream()
                .filter(e -> id.equalsIgnoreCase(e.getId().toString()))
                .findFirst();
        if (mockOpt.isEmpty()) {
            throw new IllegalArgumentException(EmployeeConstant.EMPLOYEE_NOT_FOUND_FOR_ID + id);
        }
        DeleteMockEmployeeInput deleteMockEmployeeInput =
                employeeTranslator.toMockEmployeeToDeleteMockEmployee(mockOpt.get());
        mockEmployeeService.delete(deleteMockEmployeeInput);
        return mockOpt.get().getName();
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeInput employeeInput) {
        log.info("Creating employee: {}", employeeInput);
        if (employeeInput == null) {
            throw new IllegalArgumentException(EmployeeConstant.EMPLOYEE_INPUT_IS_NULL);
        }
        String validationError = employeeInput.getValidationError();
        if (validationError != null) {
            log.error("Employee input validation failed: {}", validationError);
            throw new IllegalArgumentException(validationError);
        }
        CreateMockEmployeeInput createMockEmployeeInput =
                objectMapper.convertValue(employeeInput, CreateMockEmployeeInput.class);
        MockEmployee mock = mockEmployeeService.create(createMockEmployeeInput);
        return employeeTranslator.convertToEmployee(mock);
    }

    private boolean isValidUUID(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<EmployeeResponse> fallbackEmployees(Throwable ex) {
        log.info("Fallback triggered due to: {}", ex.getMessage());

        return Collections.emptyList();
    }
}
