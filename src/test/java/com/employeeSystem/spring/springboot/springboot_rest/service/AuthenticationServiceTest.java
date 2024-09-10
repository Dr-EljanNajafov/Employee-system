package com.employeeSystem.spring.springboot.springboot_rest.service;

import com.employeeSystem.spring.springboot.springboot_rest.auth.AuthenticationRequest;
import com.employeeSystem.spring.springboot.springboot_rest.auth.AuthenticationResponse;
import com.employeeSystem.spring.springboot.springboot_rest.auth.AuthenticationService;
import com.employeeSystem.spring.springboot.springboot_rest.auth.RegisterRequest;
import com.employeeSystem.spring.springboot.springboot_rest.config.JWTService;
import com.employeeSystem.spring.springboot.springboot_rest.dao.EmployeeDAO;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class AuthenticationServiceTest {

    @Mock
    private EmployeeDAO employeeDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(employeeDAO, passwordEncoder, jwtService, authenticationManager);
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "IT", 50000, "john@example.com", "password");

        Employee savedEmployee = Employee.builder()
                .name("John")
                .surname("Doe")
                .department("IT")
                .salary(50000)
                .email("john@example.com")
                .password("encoded-password")
                .role(Role.USER)
                .build();

        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-password");
        Mockito.when(employeeDAO.save(any(Employee.class))).thenReturn(savedEmployee);
        Mockito.when(jwtService.generateToken(any(Employee.class))).thenReturn("dummy-jwt-token");

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("dummy-jwt-token", response.getToken());
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "password");

        Employee employee = Employee.builder()
                .name("John")
                .surname("Doe")
                .email("john@example.com")
                .password("encoded-password")
                .role(Role.USER)
                .build();

        Mockito.when(employeeDAO.findByEmail(request.getEmail())).thenReturn(Optional.of(employee));
        Mockito.when(jwtService.generateToken(any(Employee.class))).thenReturn("dummy-jwt-token");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("dummy-jwt-token", response.getToken());
    }
}

