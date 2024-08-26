package com.employeeSystem.spring.springboot.springboot_rest.dao;

import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeDAO extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByEmail(String email);
}
