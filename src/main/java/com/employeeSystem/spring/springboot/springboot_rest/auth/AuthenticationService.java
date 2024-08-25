package com.employeeSystem.spring.springboot.springboot_rest.auth;

import com.employeeSystem.spring.springboot.springboot_rest.config.JWTService;
import com.employeeSystem.spring.springboot.springboot_rest.dao.EmployeeDAO;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EmployeeDAO employeeDAO;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = Employee.builder()
                .name(request.getFirstname())
                .surname(request.getLastname())
                .department(request.getDepartment())
                .salary(request.getSalary())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        employeeDAO.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = employeeDAO.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
