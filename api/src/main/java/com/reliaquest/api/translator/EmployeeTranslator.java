package com.reliaquest.api.translator;

import com.reliaquest.api.model.EmployeeResponse;
import com.reliaquest.server.model.DeleteMockEmployeeInput;
import com.reliaquest.server.model.MockEmployee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeTranslator {

    public DeleteMockEmployeeInput toMockEmployeeToDeleteMockEmployee(MockEmployee mockEmployee) {
        final var input = new DeleteMockEmployeeInput();
        input.setName(mockEmployee.getName());
        return input;
    }

    public EmployeeResponse convertToEmployee(MockEmployee mock) {
        EmployeeResponse emp = new EmployeeResponse();
        emp.setId(mock.getId());
        emp.setName(mock.getName());
        emp.setAge(mock.getAge());
        emp.setTitle(mock.getTitle());
        emp.setSalary(mock.getSalary());
        emp.setEmail(mock.getEmail());
        return emp;
    }
}
