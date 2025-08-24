package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponse {

    private UUID id;
    private String name;
    private Integer salary;
    private Integer age;
    private String title;
    private String email;
    private String error;
}
