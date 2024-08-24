package com.employeeSystem.spring.springboot.springboot_rest.service;

import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTO;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    public List<EmployeeDTO> getAllEmployees();

    public void saveEmployee(Employee employee);

    public Optional<EmployeeDTO> getEmployee(int id);

    public Employee deleteEmployee(int id);

}
