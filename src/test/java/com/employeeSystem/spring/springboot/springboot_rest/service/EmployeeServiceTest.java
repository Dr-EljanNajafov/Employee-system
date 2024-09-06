package com.employeeSystem.spring.springboot.springboot_rest.service;

import com.employeeSystem.spring.springboot.springboot_rest.dao.EmployeeDAO;
import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTO;
import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTOMapper;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Role;
import com.employeeSystem.spring.springboot.springboot_rest.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeDAO employeeDAO;
    @Mock
    private EmployeeDTOMapper employeeDTOMapper;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1)
                .name("XXX")
                .surname("YYY")
                .email("xxx@yyy.com")
                .salary(1200)
                .department("IT")
                .role(Role.USER)
                .password("12345")
                .build();
    }

    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesDTOList() {

        // Create DTOs for the employees
        EmployeeDTO employeeDTO = new EmployeeDTO(1, "XXX", "YYY", "IT");

        // mock the mapping from Employee to EmployeeDTO
        given(employeeDAO.findAll()).willReturn(List.of(employee));
        given(employeeDTOMapper.apply(employee)).willReturn(employeeDTO);

        // when - call the service method
        List<EmployeeDTO> employeeDTOList = employeeService.getAllEmployees();

        // then - assert that the list of employees is not null and contains the correct number of DTOs
        assertThat(employeeDTOList).isNotNull();
        assertThat(employeeDTOList.size()).isEqualTo(1);
        List<EmployeeDTO> expectedList = List.of(employeeDTO);
        assertThat(employeeDTOList.containsAll(expectedList) && expectedList.containsAll(employeeDTOList)).isTrue();
    }

    // JUnit test for saveEmployee method
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() throws ResourceNotFoundException {
        // given - precondition or setup
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  // Инициализация
        EmployeeServiceImpl employeeService = new EmployeeServiceImpl(employeeDTOMapper, employeeDAO, passwordEncoder);

        given(employeeDAO.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeDAO.save(employee)).willReturn(employee);

        // when -  action or the behaviour that we are going test
        Optional<EmployeeDTO> savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        // given - precondition or setup
        given(employeeDAO.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        System.out.println(employeeDAO);
        System.out.println(employeeService);

        // when -  action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // then
        verify(employeeDAO, never()).save(any(Employee.class));
    }


    // JUnit test for getEmployeeById method
    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){

        EmployeeDTO employeeDTO = new EmployeeDTO(1, "XXX", "YYY", "IT");

        // given - mock the DAO method to return a valid employee
        given(employeeDAO.findById(1)).willReturn(Optional.of(employee));

        // mock the mapping from Employee to EmployeeDTO
        given(employeeDTOMapper.apply(employee)).willReturn(employeeDTO);

        // when - call the service method
        Optional<EmployeeDTO> retrievedEmployee = employeeService.getEmployee(employeeDTO.id());

        // then - assert that the employee was found and is not null
        assertThat(retrievedEmployee).isPresent();
        assertThat(retrievedEmployee.get()).isNotNull();
        assertThat(retrievedEmployee.get().name()).isEqualTo(employee.getName());
    }

    // JUnit test for deleteEmployee method
    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        // given - precondition or setup
        int employeeId = 1;

        willDoNothing().given(employeeDAO).deleteById(employeeId);

        // when -  action or the behaviour that we are going test
        employeeService.deleteEmployee(employeeId);

        // then - verify the output
        verify(employeeDAO, times(1)).deleteById(employeeId);
    }
}