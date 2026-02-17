package com.school.management.service;

import com.school.management.domain.Administrator;
import com.school.management.domain.User;
import com.school.management.dto.AdministratorDTO;
import com.school.management.repository.AdministratorRepository;
import com.school.management.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdministratorService {
    
    private final AdministratorRepository administratorRepository;
    private final UserRepository userRepository;
    
    public AdministratorService(AdministratorRepository administratorRepository, 
                               UserRepository userRepository) {
        this.administratorRepository = administratorRepository;
        this.userRepository = userRepository;
    }
    
    public List<AdministratorDTO> getAllAdministrators() {
        return administratorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public AdministratorDTO getAdministratorById(Long id) {
        Administrator administrator = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found with id: " + id));
        return convertToDTO(administrator);
    }
    
    public AdministratorDTO getAdministratorByUserId(Long userId) {
        Administrator administrator = administratorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Administrator not found for user id: " + userId));
        return convertToDTO(administrator);
    }
    
    public AdministratorDTO createAdministrator(AdministratorDTO administratorDTO, Long createdByUserId) {
        // Check if user exists
        User user = userRepository.findById(administratorDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + administratorDTO.getUserId()));
        
        // Check if user is already an administrator
        if (administratorRepository.findByUserId(administratorDTO.getUserId()).isPresent()) {
            throw new RuntimeException("User is already an administrator");
        }
        
        // Check if user type is ADMIN
        if (!"ADMIN".equals(user.getUserType())) {
            throw new RuntimeException("User must have ADMIN user type to be an administrator");
        }
        
        Administrator administrator = new Administrator();
        administrator.setUser(user);
        administrator.setAdminRole(administratorDTO.getAdminRole());
        administrator.setDepartmentId(administratorDTO.getDepartmentId());
        administrator.setCanManageUsers(administratorDTO.isCanManageUsers());
        administrator.setCanManageDepartments(administratorDTO.isCanManageDepartments());
        administrator.setCanViewAllData(administratorDTO.isCanViewAllData());
        administrator.setCanGenerateReports(administratorDTO.isCanGenerateReports());
        administrator.setCanManageBackups(administratorDTO.isCanManageBackups());
        administrator.setOfficeNumber(administratorDTO.getOfficeNumber());
        administrator.setOfficialTitle(administratorDTO.getOfficialTitle());
        administrator.setExtension(administratorDTO.getExtension());
        administrator.setDigitalSignaturePath(administratorDTO.getDigitalSignaturePath());
        administrator.setSignatureAuthorizedDate(administratorDTO.getSignatureAuthorizedDate());
        administrator.setCreatedAt(LocalDateTime.now());
        
        Administrator savedAdministrator = administratorRepository.save(administrator);
        return convertToDTO(savedAdministrator);
    }
    
    public AdministratorDTO updateAdministrator(Long id, AdministratorDTO administratorDTO, Long updatedByUserId) {
        Administrator administrator = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found with id: " + id));
        
        administrator.setAdminRole(administratorDTO.getAdminRole());
        administrator.setDepartmentId(administratorDTO.getDepartmentId());
        administrator.setCanManageUsers(administratorDTO.isCanManageUsers());
        administrator.setCanManageDepartments(administratorDTO.isCanManageDepartments());
        administrator.setCanViewAllData(administratorDTO.isCanViewAllData());
        administrator.setCanGenerateReports(administratorDTO.isCanGenerateReports());
        administrator.setCanManageBackups(administratorDTO.isCanManageBackups());
        administrator.setOfficeNumber(administratorDTO.getOfficeNumber());
        administrator.setOfficialTitle(administratorDTO.getOfficialTitle());
        administrator.setExtension(administratorDTO.getExtension());
        administrator.setDigitalSignaturePath(administratorDTO.getDigitalSignaturePath());
        administrator.setSignatureAuthorizedDate(administratorDTO.getSignatureAuthorizedDate());
        administrator.setUpdatedAt(LocalDateTime.now());
        
        Administrator updatedAdministrator = administratorRepository.save(administrator);
        return convertToDTO(updatedAdministrator);
    }
    
    public void deleteAdministrator(Long id, Long deletedByUserId) {
        Administrator administrator = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found with id: " + id));
        
        administratorRepository.delete(administrator);
    }
    
    public AdministratorDTO toggleAdministratorActive(Long id, Long updatedByUserId) {
        Administrator administrator = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found with id: " + id));
        
        // Get the associated user and toggle active status
        User user = administrator.getUser();
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
        
        administrator.setUpdatedAt(LocalDateTime.now());
        Administrator updatedAdministrator = administratorRepository.save(administrator);
        return convertToDTO(updatedAdministrator);
    }
    
    public boolean isUserAdministrator(Long userId) {
        return administratorRepository.findByUserId(userId).isPresent();
    }
    
    public boolean hasPermission(Long userId, String permission) {
        return administratorRepository.findByUserId(userId)
                .map(admin -> {
                    switch (permission) {
                        case "MANAGE_USERS":
                            return admin.getCanManageUsers();
                        case "MANAGE_DEPARTMENTS":
                            return admin.getCanManageDepartments();
                        case "VIEW_ALL_DATA":
                            return admin.getCanViewAllData();
                        case "GENERATE_REPORTS":
                            return admin.getCanGenerateReports();
                        case "MANAGE_BACKUPS":
                            return admin.getCanManageBackups();
                        default:
                            return false;
                    }
                })
                .orElse(false);
    }
    
    private AdministratorDTO convertToDTO(Administrator administrator) {
        AdministratorDTO dto = new AdministratorDTO();
        dto.setId(administrator.getId());
        
        User user = administrator.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
            dto.setDisplayId(user.getDisplayId());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setUserType(user.getUserType());
            dto.setActive(user.getIsActive());
        }
        
        dto.setAdminRole(administrator.getAdminRole());
        dto.setDepartmentId(administrator.getDepartmentId());
        dto.setCanManageUsers(administrator.getCanManageUsers());
        dto.setCanManageDepartments(administrator.getCanManageDepartments());
        dto.setCanViewAllData(administrator.getCanViewAllData());
        dto.setCanGenerateReports(administrator.getCanGenerateReports());
        dto.setCanManageBackups(administrator.getCanManageBackups());
        dto.setOfficeNumber(administrator.getOfficeNumber());
        dto.setOfficialTitle(administrator.getOfficialTitle());
        dto.setExtension(administrator.getExtension());
        dto.setDigitalSignaturePath(administrator.getDigitalSignaturePath());
        dto.setSignatureAuthorizedDate(administrator.getSignatureAuthorizedDate());
        dto.setCreatedAt(administrator.getCreatedAt());
        dto.setUpdatedAt(administrator.getUpdatedAt());
        
        return dto;
    }
}