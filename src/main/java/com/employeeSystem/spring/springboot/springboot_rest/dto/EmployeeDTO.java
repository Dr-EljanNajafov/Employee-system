package com.employeeSystem.spring.springboot.springboot_rest.dto;

public record EmployeeDTO(
        Integer id,
        String name,
        String surname,
        String department
) {
}
