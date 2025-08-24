package com.reliaquest.api.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeInput {

    @NotBlank
    private String name;

    @Positive @NotNull private Integer salary;

    @Min(16)
    @Max(75)
    @NotNull private Integer age;

    @NotBlank
    private String title;

    public String getValidationError() {
        if (name == null || name.isBlank()) return "Employee name is blank.";
        if (salary == null) return "Employee salary is missing.";
        if (salary <= 0) return "Employee salary must be positive.";
        if (age == null) return "Employee age is missing.";
        if (age < 16) return "Employee age must be at least 16.";
        if (age > 75) return "Employee age must not be greater than 75.";
        if (title == null || title.isBlank()) return "Employee title is blank.";
        return null;
    }
}
