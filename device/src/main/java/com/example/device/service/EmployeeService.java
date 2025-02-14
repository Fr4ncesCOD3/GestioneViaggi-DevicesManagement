package com.example.device.service;

import com.example.device.dto.EmployeeDTO;
import com.example.device.exception.ResourceNotFoundException;
import com.example.device.model.Employee;
import com.example.device.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dipendente non trovato con id: " + id));
    }
    
    @Transactional
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByUsername(employeeDTO.getUsername())) {
            throw new IllegalArgumentException("Username già in uso");
        }
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new IllegalArgumentException("Email già in uso");
        }
        
        Employee employee = new Employee();
        employee.setUsername(employeeDTO.getUsername());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        
        return employeeRepository.save(employee);
    }
    
    @Transactional
    public Employee updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = getEmployeeById(id);
        
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        
        return employeeRepository.save(employee);
    }
    
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dipendente non trovato con id: " + id);
        }
        employeeRepository.deleteById(id);
    }
} 