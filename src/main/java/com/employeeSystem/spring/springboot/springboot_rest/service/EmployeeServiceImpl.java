package com.employeeSystem.spring.springboot.springboot_rest.service;


import com.employeeSystem.spring.springboot.springboot_rest.dao.EmployeeDAO;
import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTO;
import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTOMapper;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDAO employeeDAO;

    private final EmployeeDTOMapper employeeDTOMapper;

    public EmployeeServiceImpl(EmployeeDTOMapper employeeDTOMapper) {
        this.employeeDTOMapper = employeeDTOMapper;
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
    public void saveEmployee(Employee employee) {
        employeeDAO.save(employee);
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
