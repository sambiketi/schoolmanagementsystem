package com.school.management.service;

import com.school.management.domain.Department;
import com.school.management.dto.DepartmentDTO;
import com.school.management.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public DepartmentDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        return convertToDTO(department);
    }
    
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO, Long createdByUserId) {
        // Check if department code already exists
        if (departmentRepository.existsByCode(departmentDTO.getCode())) {
            throw new RuntimeException("Department code already exists: " + departmentDTO.getCode());
        }
        
        Department department = new Department();
        department.setCode(departmentDTO.getCode());
        department.setName(departmentDTO.getName());
        department.setDescription(departmentDTO.getDescription());
        department.setCanCreateSpreadsheets(departmentDTO.isCanCreateSpreadsheets());
        department.setCanSubmitReports(departmentDTO.isCanSubmitReports());
        department.setMaxSpreadsheets(departmentDTO.getMaxSpreadsheets());
        department.setDataRetentionDays(departmentDTO.getDataRetentionDays());
        department.setDepartmentHeadId(departmentDTO.getDepartmentHeadId());
        department.setIsActive(departmentDTO.isActive());
        department.setCreatedAt(LocalDateTime.now());
        department.setCreatedBy(createdByUserId);
        
        Department savedDepartment = departmentRepository.save(department);
        return convertToDTO(savedDepartment);
    }
    
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO, Long updatedByUserId) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        // Check if department code already exists (excluding current department)
        if (!department.getCode().equals(departmentDTO.getCode()) && 
            departmentRepository.existsByCode(departmentDTO.getCode())) {
            throw new RuntimeException("Department code already exists: " + departmentDTO.getCode());
        }
        
        department.setCode(departmentDTO.getCode());
        department.setName(departmentDTO.getName());
        department.setDescription(departmentDTO.getDescription());
        department.setCanCreateSpreadsheets(departmentDTO.isCanCreateSpreadsheets());
        department.setCanSubmitReports(departmentDTO.isCanSubmitReports());
        department.setMaxSpreadsheets(departmentDTO.getMaxSpreadsheets());
        department.setDataRetentionDays(departmentDTO.getDataRetentionDays());
        department.setDepartmentHeadId(departmentDTO.getDepartmentHeadId());
        department.setIsActive(departmentDTO.isActive());
        department.setUpdatedAt(LocalDateTime.now());
        department.setUpdatedBy(updatedByUserId);
        
        Department updatedDepartment = departmentRepository.save(department);
        return convertToDTO(updatedDepartment);
    }
    
    public void deleteDepartment(Long id, Long deletedByUserId) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        // Check if department has associated data
        // For now, just delete (cascade should handle it)
        departmentRepository.delete(department);
    }
    
    public DepartmentDTO toggleDepartmentActive(Long id, Long updatedByUserId) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        department.setIsActive(!department.getIsActive());
        department.setUpdatedAt(LocalDateTime.now());
        department.setUpdatedBy(updatedByUserId);
        
        Department updatedDepartment = departmentRepository.save(department);
        return convertToDTO(updatedDepartment);
    }
    
    public boolean existsByCode(String code) {
        return departmentRepository.existsByCode(code);
    }
    
    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setCode(department.getCode());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        dto.setCanCreateSpreadsheets(department.getCanCreateSpreadsheets());
        dto.setCanSubmitReports(department.getCanSubmitReports());
        dto.setMaxSpreadsheets(department.getMaxSpreadsheets());
        dto.setDataRetentionDays(department.getDataRetentionDays());
        dto.setDepartmentHeadId(department.getDepartmentHeadId());
        dto.setActive(department.getIsActive());
        dto.setCreatedAt(department.getCreatedAt());
        dto.setCreatedBy(department.getCreatedBy());
        dto.setUpdatedAt(department.getUpdatedAt());
        dto.setUpdatedBy(department.getUpdatedBy());
        return dto;
    }
}