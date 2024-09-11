package com.employeeSystem.spring.springboot.springboot_rest.service;

import com.employeeSystem.spring.springboot.springboot_rest.dao.EmployeeDAO;
import com.employeeSystem.spring.springboot.springboot_rest.dao.EmployeeRedisRepository;
import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTO;
import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTOMapper;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Role;
import com.employeeSystem.spring.springboot.springboot_rest.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private EmployeeRedisRepository employeeRedisRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

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
        // Create DTO for the employee
        EmployeeDTO employeeDTO = new EmployeeDTO(1, "XXX", "YYY", "IT");

        // Mock the mapping from Employee to EmployeeDTO
        given(employeeDAO.findAll()).willReturn(List.of(employee));
        given(employeeDTOMapper.apply(employee)).willReturn(employeeDTO);

        // When - call the service method
        List<EmployeeDTO> employeeDTOList = employeeService.getAllEmployees();

        // Then - assert that the list of employees is not null and contains the correct number of DTOs
        assertThat(employeeDTOList).isNotNull();
        assertThat(employeeDTOList.size()).isEqualTo(1);
        List<EmployeeDTO> expectedList = List.of(employeeDTO);
        assertThat(employeeDTOList.containsAll(expectedList) && expectedList.containsAll(employeeDTOList)).isTrue();
    }

    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() throws ResourceNotFoundException {
        // Given - precondition or setup
        given(employeeDAO.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeDAO.save(employee)).willReturn(employee);

        // Mock Redis repository save to do nothing
        given(employeeRedisRepository.save(any())).willReturn(null);

        // When - action or the behaviour that we are going to test
        Optional<EmployeeDTO> savedEmployee = employeeService.saveEmployee(employee);

        // Then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        // Given - precondition or setup
        given(employeeDAO.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        // When - action or the behaviour that we are going to test
        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));

        // Then
        verify(employeeDAO, never()).save(any(Employee.class));
    }

    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        EmployeeDTO employeeDTO = new EmployeeDTO(1, "XXX", "YYY", "IT");

        // Given - mock DAO method to return a valid employee
        given(employeeDAO.findById(1)).willReturn(Optional.of(employee));

        // Mock the mapping from Employee to EmployeeDTO
        given(employeeDTOMapper.apply(employee)).willReturn(employeeDTO);

        // When - call the service method
        Optional<EmployeeDTO> retrievedEmployee = employeeService.getEmployee(1);

        // Then - assert that the employee was found and is not null
        assertThat(retrievedEmployee).isPresent();
        assertThat(retrievedEmployee.get()).isNotNull();
        assertThat(retrievedEmployee.get().name()).isEqualTo(employee.getName());
    }

    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        // Given - precondition or setup
        int employeeId = 1;

        willDoNothing().given(employeeDAO).deleteById(employeeId);
        willDoNothing().given(employeeRedisRepository).deleteById(String.valueOf(employeeId));

        // When - action or the behaviour that we are going to test
        employeeService.deleteEmployee(employeeId);

        // Then - verify the output
        verify(employeeDAO, times(1)).deleteById(employeeId);
        verify(employeeRedisRepository, times(1)).deleteById(String.valueOf(employeeId));
    }
}
