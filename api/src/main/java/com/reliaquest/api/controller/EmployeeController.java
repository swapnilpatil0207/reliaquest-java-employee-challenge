package com.reliaquest.api.controller;

import com.reliaquest.api.constants.EmployeeConstant;
import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.api.model.EmployeeResponse;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
@Slf4j
public class EmployeeController implements IEmployeeController<EmployeeResponse, EmployeeInput> {

    private EmployeeService employeeService;

    @Override
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() throws Exception {
        log.info("Fetching all employees");
        List<EmployeeResponse> employees = employeeService.fetchAllEmployees();
        if (employees == null || employees.isEmpty()) {
            log.info(EmployeeConstant.NO_EMPLOYEES_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(employees);
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByNameSearch(@RequestParam String searchString)
            throws Exception {
        log.info("Searching employees by name fragment: {}", searchString);
        List<EmployeeResponse> employees = employeeService.getEmployeesByNameSearch(searchString);
        if (employees == null || employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(employees);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable String id) throws Exception {
        log.info("Fetching employee by ID: {}", id);
        EmployeeResponse employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(employee);
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() throws Exception {
        log.info("Fetching highest salary among employees");
        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
        return ResponseEntity.ok(highestSalary);
    }

    @Override
    @GetMapping("/top10HighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() throws Exception {
        log.info("Fetching top 10 highest earning employee names");
        List<String> topNames = employeeService.getTop10HighestEarningEmployeeNames();
        return ResponseEntity.ok(topNames);
    }

    @Override
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeInput employeeInput) throws Exception {
        log.info("Creating new employee: {}", employeeInput);
        EmployeeResponse employee = employeeService.createEmployee(employeeInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) throws Exception {
        log.info("Deleting employee by ID: {}", id);
        String deletedName = employeeService.deleteEmployeeById(id);
        if (deletedName == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(deletedName);
    }
}
