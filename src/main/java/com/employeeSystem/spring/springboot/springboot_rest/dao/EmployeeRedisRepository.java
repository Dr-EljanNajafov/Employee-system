package com.employeeSystem.spring.springboot.springboot_rest.dao;

import com.employeeSystem.spring.springboot.springboot_rest.entity.EmployeeRedis;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRedisRepository extends CrudRepository<EmployeeRedis, String> {
}
