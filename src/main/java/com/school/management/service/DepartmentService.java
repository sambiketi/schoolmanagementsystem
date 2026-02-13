package com.school.management.service;

import com.school.management.domain.Department;
import com.school.management.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }
}