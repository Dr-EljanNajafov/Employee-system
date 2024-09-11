package com.employeeSystem.spring.springboot.springboot_rest.service;


import com.employeeSystem.spring.springboot.springboot_rest.dao.EmployeeDAO;
import com.employeeSystem.spring.springboot.springboot_rest.dao.EmployeeRedisRepository;
import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTO;
import com.employeeSystem.spring.springboot.springboot_rest.dto.EmployeeDTOMapper;
import com.employeeSystem.spring.springboot.springboot_rest.entity.Employee;
import com.employeeSystem.spring.springboot.springboot_rest.entity.EmployeeRedis;
import com.employeeSystem.spring.springboot.springboot_rest.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;
    private final EmployeeDTOMapper employeeDTOMapper;
    private final BCryptPasswordEncoder passwordEncoder;  // Добавьте шифровальщик
    private final EmployeeRedisRepository employeeRedisRepository;

//    public EmployeeServiceImpl(EmployeeDTOMapper employeeDTOMapper, EmployeeDAO employeeDAO, BCryptPasswordEncoder passwordEncoder) {
//        this.employeeDTOMapper = employeeDTOMapper;
//        this.employeeDAO = employeeDAO;
//        this.passwordEncoder = passwordEncoder;
//    }


    @Override
    @Transactional
    public List<EmployeeDTO> getAllEmployees() {
        return employeeDAO.findAll()
                .stream()
                .map(employeeDTOMapper)
                .collect(Collectors.toList());
    }

//    @Override
//    @Transactional
//    public Optional<EmployeeDTO> saveEmployee(Employee employee) throws ResourceNotFoundException {
//        Optional<Employee> savedEmployee = employeeDAO.findByEmail(employee.getEmail());
//        if(savedEmployee.isPresent()){
//            throw new ResourceNotFoundException("Employee already exist with given email:" + employee.getEmail());
//        }
//
//        // Шифруем пароль перед сохранением
//        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
//        Employee addedEmployee = employeeDAO.save(employee);
//        return getEmployee(addedEmployee.getId());
//    }

    @Override
    @Transactional
    public Optional<EmployeeDTO> saveEmployee(Employee employee) throws ResourceNotFoundException {
        Optional<Employee> savedEmployee = employeeDAO.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exists with given email: " + employee.getEmail());
        }

        // Шифруем пароль перед сохранением
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        Employee addedEmployee = employeeDAO.save(employee);

        // Сохранение в Redis
        EmployeeRedis employeeRedis = new EmployeeRedis();
        employeeRedis.setId(addedEmployee.getId());
        employeeRedis.setName(addedEmployee.getName());
        employeeRedis.setSurname(addedEmployee.getSurname());
        employeeRedis.setDepartment(addedEmployee.getDepartment());
        employeeRedis.setSalary(String.valueOf(addedEmployee.getSalary()));
        employeeRedis.setEmail(addedEmployee.getEmail());
        employeeRedis.setPassword(addedEmployee.getPassword());
        employeeRedis.setRole(addedEmployee.getRole());
        employeeRedisRepository.save(employeeRedis);

        return getEmployee(addedEmployee.getId());
    }


//    @Override
//    @Transactional
//    public Employee updateEmployee(Employee employee) throws ResourceNotFoundException {
//        return employeeDAO.save(employee);
//    }

    @Override
    @Transactional
    public Employee updateEmployee(Employee employee) throws ResourceNotFoundException {
        Employee updatedEmployee = employeeDAO.save(employee);

        // Обновляем данные в Redis
        EmployeeRedis employeeRedis = new EmployeeRedis();
        employeeRedis.setId(updatedEmployee.getId());
        employeeRedis.setName(updatedEmployee.getName());
        employeeRedis.setSurname(updatedEmployee.getSurname());
        employeeRedis.setDepartment(updatedEmployee.getDepartment());
        employeeRedis.setSalary(String.valueOf(updatedEmployee.getSalary()));
        employeeRedis.setEmail(updatedEmployee.getEmail());
        employeeRedis.setPassword(updatedEmployee.getPassword());
        employeeRedis.setRole(updatedEmployee.getRole());
        employeeRedisRepository.save(employeeRedis);

        return updatedEmployee;
    }


//    @Override
//    @Transactional
//    public Optional<EmployeeDTO> getEmployee(int id) {
//        return employeeDAO.findById(id)
//                .map(employeeDTOMapper);
//    }

    @Override
    @Transactional
    public Optional<EmployeeDTO> getEmployee(int id) {
        // Попробуем получить данные из Redis
        Optional<EmployeeRedis> employeeRedisOpt = employeeRedisRepository.findById(String.valueOf(id));

        if (employeeRedisOpt.isPresent()) {
            EmployeeRedis employeeRedis = employeeRedisOpt.get();
            EmployeeDTO employeeDTO = new EmployeeDTO(
                    employeeRedis.getId(),
                    employeeRedis.getName(),
                    employeeRedis.getSurname(),
                    employeeRedis.getDepartment()
            );
            return Optional.of(employeeDTO);
        }

        // Если в Redis нет, достаем из базы данных
        Optional<EmployeeDTO> employeeDTOOpt = employeeDAO.findById(id).map(employeeDTOMapper);
        employeeDTOOpt.ifPresent(employeeDTO -> {
            // Сохраняем в Redis
            EmployeeRedis employeeRedis = new EmployeeRedis();
            employeeRedis.setId(employeeDTO.id());
            employeeRedis.setName(employeeDTO.name());
            employeeRedis.setSurname(employeeDTO.surname());
            employeeRedis.setDepartment(employeeDTO.department());
            employeeRedisRepository.save(employeeRedis);
        });

        return employeeDTOOpt;
    }


    //    @Override
//    @Transactional
//    public Employee deleteEmployee(int id) {
//        employeeDAO.deleteById(id);
//        return null;
//    }
    @Override
    @Transactional
    public Employee deleteEmployee(int id) {
        employeeDAO.deleteById(id);

        // Удаляем данные из Redis
        employeeRedisRepository.deleteById(String.valueOf(id));

        return null;
    }

}
