package com.employeeSystem.spring.springboot.springboot_rest.service;


import com.employeeSystem.spring.springboot.springboot_rest.dao.EmployeeDAO;
import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTO;
import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTOMapper;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import com.employeeSystem.spring.springboot.springboot_rest.exception.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;
    private final EmployeeDTOMapper employeeDTOMapper;
    private final BCryptPasswordEncoder passwordEncoder;  // Добавьте шифровальщик


    public EmployeeServiceImpl(EmployeeDTOMapper employeeDTOMapper, EmployeeDAO employeeDAO, BCryptPasswordEncoder passwordEncoder) {
        this.employeeDTOMapper = employeeDTOMapper;
        this.employeeDAO = employeeDAO;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public List<EmployeeDTO> getAllEmployees() {
        return employeeDAO.findAll()
                .stream()
                .map(employeeDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Employee saveEmployee(Employee employee) throws ResourceNotFoundException {
        Optional<Employee> savedEmployee = employeeDAO.findByEmail(employee.getEmail());
        if(savedEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee already exist with given email:" + employee.getEmail());
        }

        // Шифруем пароль перед сохранением
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        return  employeeDAO.save(employee);
    }

    @Override
    @Transactional
    public Employee updateEmployee(Employee employee) throws ResourceNotFoundException {
        return  employeeDAO.save(employee);
    }

    @Override
    @Transactional
    public Optional<EmployeeDTO> getEmployee(int id) {
        return employeeDAO.findById(id)
                .map(employeeDTOMapper);
    }

    @Override
    @Transactional
    public Employee deleteEmployee(int id) {
        employeeDAO.deleteById(id);
        return null;
    }
}
