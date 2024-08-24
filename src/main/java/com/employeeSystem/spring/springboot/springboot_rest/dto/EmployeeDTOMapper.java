package com.employeeSystem.spring.springboot.springboot_rest.dto;

import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EmployeeDTOMapper implements Function<Employee, EmployeeDTO> {
    @Override
    public EmployeeDTO apply(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getSurname(),
                employee.getDepartment()
        );
    }
}
