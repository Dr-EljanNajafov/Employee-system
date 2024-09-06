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
    public Optional<EmployeeDTO> addNewEmployee (@RequestBody Employee employee) throws ResourceNotFoundException {
        employeeService.saveEmployee(employee);
        Optional<EmployeeDTO> addedEmployee = employeeService.getEmployee(employee.getId());
        return addedEmployee;
    }

    @PutMapping("/")
    public Optional<EmployeeDTO> updateEmployee(@RequestBody Employee employee) throws ResourceNotFoundException {
        employeeService.updateEmployee(employee);
        Optional<EmployeeDTO> updatedEmployee = employeeService.getEmployee(employee.getId());
        return updatedEmployee;
    }

    @DeleteMapping("/{id}")
    public Optional<EmployeeDTO> deleteEmployee(@PathVariable int id){
        Optional<EmployeeDTO> employee = employeeService.getEmployee(id);
        employeeService.deleteEmployee(id);
        return employee;
    }
}
