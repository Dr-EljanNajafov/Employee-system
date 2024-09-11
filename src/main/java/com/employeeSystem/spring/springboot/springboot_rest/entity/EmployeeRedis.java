package com.employeeSystem.spring.springboot.springboot_rest.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@RedisHash("employees")
@Getter
@Setter
public class EmployeeRedis implements Serializable {

    @Id
    private int id;
    private String name;
    private String surname;
    private String department;
    private String salary;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;


}
