package com.employeeSystem.spring.springboot.springboot_rest.service;

import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTO;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import com.employeeSystem.spring.springboot.springboot_rest.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    public List<EmployeeDTO> getAllEmployees();

    public Employee saveEmployee(Employee employee) throws ResourceNotFoundException;

    public Optional<EmployeeDTO> getEmployee(int id);

    public Employee deleteEmployee(int id);

}
