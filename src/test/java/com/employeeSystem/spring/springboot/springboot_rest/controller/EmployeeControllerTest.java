package com.employeeSystem.spring.springboot.springboot_rest.controller;

import com.employeeSystem.spring.springboot.springboot_rest.config.JWTService;
import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTO;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Role;
import com.employeeSystem.spring.springboot.springboot_rest.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for this test
@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private JWTService jwtService; // Mock JWTService to resolve the NoSuchBeanDefinitionException

    @MockBean
    private UserDetailsService userDetailsService; // Mock UserDetailsService if needed

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
    private EmployeeDTO employeeDTO;
    private EmployeeDTO employeeDTO2;

    @Test
    public void getAllEmployeesAPI() throws Exception {
        // Initialize the test data
        employeeDTO = new EmployeeDTO(1, "John", "Doe", "HR");
        employeeDTO2 = new EmployeeDTO(2, "Jane", "Smith", "Finance");

        List<EmployeeDTO> employees = Arrays.asList(employeeDTO, employeeDTO2);

        // Mock the service call
        when(employeeService.getAllEmployees()).thenReturn(employees);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/employees/")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(employeeDTO.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(employeeDTO.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value(employeeDTO.surname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].department").value(employeeDTO.department()));
    }

    @Test
    public void getEmployeeByIdAPI() throws Exception {
        // Initialize the test data
        EmployeeDTO employeeDTO = new EmployeeDTO(1, "John", "Doe", "HR");

        // Mock the service call
        when(employeeService.getEmployee(employeeDTO.id())).thenReturn(Optional.of(employeeDTO));

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/employees/1") // URL with the ID
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(employeeDTO.id())) // Not an array, just a single object
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(employeeDTO.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(employeeDTO.surname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.department").value(employeeDTO.department()));
    }

    @Test
    public void addNewEmployeeAPI() throws Exception {
        // Prepare DTO for the request and response
        EmployeeDTO employeeDTO = new EmployeeDTO(1, "John", "Doe", "HR");

        // Mock the service call to return the expected result
        when(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .thenReturn(Optional.of(employeeDTO));
        when(employeeService.getEmployee(employeeDTO.id()))
                .thenReturn(Optional.of(employeeDTO));

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/employees/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)) // Convert DTO to JSON
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()) // or status().isCreated() depending on your implementation
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(employeeDTO.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(employeeDTO.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(employeeDTO.surname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.department").value(employeeDTO.department()));
    }

    @Test
    public void updateEmployeeAPI() throws Exception {
        // Initialize the EmployeeDTO for the update
        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO(
                1, "Updated Name", "Updated Surname", "Updated Department"
        );

        // Create a corresponding Employee object
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1);
        updatedEmployee.setName("Updated Name");
        updatedEmployee.setSurname("Updated Surname");
        updatedEmployee.setDepartment("Updated Department");

        // Mock the service method calls
        when(employeeService.getEmployee(1)).thenReturn(Optional.of(updatedEmployeeDTO));  // Simulate fetching the employee as DTO
        when(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class))).thenReturn(updatedEmployee);  // Simulate the update

        // Perform the PUT request
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/employees/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployeeDTO))  // Send the updated DTO as JSON
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())  // Expect a 200 OK status
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedEmployeeDTO.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedEmployeeDTO.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(updatedEmployeeDTO.surname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.department").value(updatedEmployeeDTO.department()));
    }

    @Test
    public void deleteEmployeeAPI() throws Exception {
        // Create a sample EmployeeDTO to return
        EmployeeDTO employeeDTO = new EmployeeDTO(1, "John", "Doe", "HR");

        // Mock service methods
        when(employeeService.getEmployee(1)).thenReturn(Optional.of(employeeDTO));

        // Perform DELETE request (no content in DELETE request body needed)
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/employees/{id}", 1)  // The ID is part of the path
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())  // Expect 200 OK since we return the employee
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(employeeDTO.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(employeeDTO.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(employeeDTO.surname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.department").value(employeeDTO.department()));
    }
}

