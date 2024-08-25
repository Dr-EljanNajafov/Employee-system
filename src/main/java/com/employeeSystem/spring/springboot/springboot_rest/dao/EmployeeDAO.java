package com.employeeSystem.spring.springboot.springboot_rest.dao;

import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeDAO extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByEmail(String email);
}
