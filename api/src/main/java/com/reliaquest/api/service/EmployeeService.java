package com.reliaquest.api.service;

import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.api.model.EmployeeResponse;
import java.util.List;

public interface EmployeeService {
    List<EmployeeResponse> fetchAllEmployees() throws Exception;

    EmployeeResponse getEmployeeById(String id) throws Exception;

    List<EmployeeResponse> getEmployeesByNameSearch(String nameFragment) throws Exception;

    Integer getHighestSalaryOfEmployees() throws Exception;

    List<String> getTop10HighestEarningEmployeeNames() throws Exception;

    String deleteEmployeeById(String id) throws Exception;

    EmployeeResponse createEmployee(EmployeeInput employeeInput) throws Exception;

    List<EmployeeResponse> fallbackEmployees(Throwable ex);
}
