package com.employeeSystem.spring.springboot.springboot_rest.controller;

import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTO;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import com.employeeSystem.spring.springboot.springboot_rest.exception.ResourceNotFoundException;
import com.employeeSystem.spring.springboot.springboot_rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public List<EmployeeDTO> showAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Optional<EmployeeDTO> getEmployee(@PathVariable int id) {
        return employeeService.getEmployee(id);
    }

    @PostMapping("/")
    public Employee addNewEmployee (@RequestBody Employee employee) throws ResourceNotFoundException {
        employeeService.saveEmployee(employee);
        return employee;
    }

    @PutMapping("/")
    public Employee updateEmployee(@RequestBody Employee employee) throws ResourceNotFoundException {
        employeeService.updateEmployee(employee);
        return employee;
    }

    @DeleteMapping("/{id}")
    public Optional<EmployeeDTO> deleteEmployee(@PathVariable int id){
        Optional<EmployeeDTO> employee = employeeService.getEmployee(id);
        employeeService.deleteEmployee(id);
        return employee;
    }
}
