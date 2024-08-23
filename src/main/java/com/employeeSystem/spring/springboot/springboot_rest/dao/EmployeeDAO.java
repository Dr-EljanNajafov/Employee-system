package com.employeeSystem.spring.springboot.springboot_rest.dao;

import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeDAO extends JpaRepository<Employee, Integer> {
}
