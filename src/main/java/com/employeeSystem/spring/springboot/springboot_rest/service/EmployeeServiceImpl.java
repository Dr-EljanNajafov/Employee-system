package com.employeeSystem.spring.springboot.springboot_rest.service;


import com.employeeSystem.spring.springboot.springboot_rest.dao.EmployeeDAO;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDAO employeeDAO;

    @Override
    @Transactional
    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    @Override
    @Transactional
    public void saveEmployee(Employee employee) {
        employeeDAO.save(employee);
    }

    @Override
    @Transactional
    public Optional<Employee> getEmployee(int id) {
        return employeeDAO.findById(id);
    }

    @Override
    @Transactional
    public Employee deleteEmployee(int id) {
        employeeDAO.deleteById(id);
        return null;
    }
}
