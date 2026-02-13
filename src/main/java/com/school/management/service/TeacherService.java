package com.school.management.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.management.domain.Teacher;
import com.school.management.domain.User;
import com.school.management.dto.TeacherDTO;
import com.school.management.dto.TeacherRegistrationDTO;
import com.school.management.repository.TeacherRepository;
import com.school.management.repository.UserRepository;
import com.school.management.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeacherService {
    
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final DepartmentRepository departmentRepository;
    private final ObjectMapper objectMapper;
    
    public TeacherService(
            TeacherRepository teacherRepository,
            UserRepository userRepository,
            UserService userService,
            DepartmentRepository departmentRepository,
            ObjectMapper objectMapper) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.departmentRepository = departmentRepository;
        this.objectMapper = objectMapper;
    }
    
    /**
     * Register a new teacher
     */
    public TeacherDTO registerTeacher(TeacherRegistrationDTO registrationDTO) {
        try {
            // 1. Create the user account
            User user = new User();
            user.setDisplayIdPrefix("TEA");
            user.setDisplayIdYear(LocalDate.now().getYear());
            
            // Get next sequence number for teachers
            Long nextSequence = getNextTeacherSequence();
            user.setDisplayIdSequence(nextSequence.intValue());
            
            user.setAuthType("PASSWORD");
            user.setUsername(registrationDTO.getUsername());
            user.setEmail(registrationDTO.getEmail());
            user.setFullName(registrationDTO.getFullName());
            user.setDateOfBirth(registrationDTO.getDateOfBirth());
            user.setGender(registrationDTO.getGender());
            user.setPhoneNumber(registrationDTO.getPhoneNumber());
            user.setAddress(registrationDTO.getAddress());
            user.setUserType("TEACHER");
            user.setRoleSubtype("SUBJECT_TEACHER"); // Default, can be updated later
            user.setDepartmentId(
                registrationDTO.getDepartmentAffiliations() != null && 
                !registrationDTO.getDepartmentAffiliations().isEmpty() 
                    ? registrationDTO.getDepartmentAffiliations().get(0) 
                    : null
            );
            
            // Create user with password
            User savedUser = userService.createUser(user, registrationDTO.getPassword());
            
            // 2. Create the teacher record
            Teacher teacher = new Teacher();
            teacher.setUser(savedUser);
            teacher.setEmployeeNumber(registrationDTO.getEmployeeNumber());
            teacher.setHireDate(registrationDTO.getHireDate());
            teacher.setEmploymentType(registrationDTO.getEmploymentType());
            teacher.setContractEndDate(registrationDTO.getContractEndDate());
            teacher.setSalaryGrade(registrationDTO.getSalaryGrade());
            teacher.setQualificationSummary(registrationDTO.getQualificationSummary());
            
            // Convert List to JSON String
            if (registrationDTO.getSubjectsCertified() != null) {
                teacher.setSubjectsCertified(
                    objectMapper.writeValueAsString(registrationDTO.getSubjectsCertified())
                );
            }
            
            // Convert department affiliations to JSON
            if (registrationDTO.getDepartmentAffiliations() != null) {
                teacher.setDepartmentAffiliations(
                    objectMapper.writeValueAsString(registrationDTO.getDepartmentAffiliations())
                );
            }
            
            teacher.setOfficialEmail(registrationDTO.getOfficialEmail() != null 
                ? registrationDTO.getOfficialEmail() 
                : registrationDTO.getEmail());
            teacher.setOfficeExtension(registrationDTO.getOfficeExtension());
            teacher.setOfficeRoom(registrationDTO.getOfficeRoom());
            teacher.setTeachingHoursPerWeek(registrationDTO.getTeachingHoursPerWeek() != null 
                ? registrationDTO.getTeachingHoursPerWeek() 
                : 0);
            teacher.setAdministrativeHoursPerWeek(registrationDTO.getAdministrativeHoursPerWeek() != null 
                ? registrationDTO.getAdministrativeHoursPerWeek() 
                : 0);
            teacher.setEmploymentStatus("ACTIVE");
            teacher.setLeaveBalanceDays(30); // Default leave balance
            
            // Initialize empty JSON arrays if not provided
            if (teacher.getClassesAssigned() == null) {
                teacher.setClassesAssigned("{}");
            }
            if (teacher.getTrainingCertifications() == null) {
                teacher.setTrainingCertifications("[]");
            }
            
            Teacher savedTeacher = teacherRepository.save(teacher);
            
            // 3. Convert to DTO and return
            return convertToDTO(savedTeacher);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all teachers
     */
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get active teachers only
     */
    public List<TeacherDTO> getAllActiveTeachers() {
        return teacherRepository.findAllActiveTeachers().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get teacher by ID
     */
    public TeacherDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        return convertToDTO(teacher);
    }
    
    /**
     * Get teacher by user ID
     */
    public TeacherDTO getTeacherByUserId(Long userId) {
        User user = userService.findById(userId);
        if (user.getTeacher() == null) {
            throw new RuntimeException("Teacher not found for user id: " + userId);
        }
        return convertToDTO(user.getTeacher());
    }
    
    /**
     * Get teacher by employee number
     */
    public TeacherDTO getTeacherByEmployeeNumber(String employeeNumber) {
        Teacher teacher = teacherRepository.findByEmployeeNumber(employeeNumber)
            .orElseThrow(() -> new RuntimeException("Teacher not found with employee number: " + employeeNumber));
        return convertToDTO(teacher);
    }
    
    /**
     * Update teacher information
     */
    public TeacherDTO updateTeacher(Long id, TeacherRegistrationDTO updateDTO) {
        try {
            Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
            
            User user = teacher.getUser();
            
            // Update user fields
            user.setFullName(updateDTO.getFullName());
            user.setEmail(updateDTO.getEmail());
            user.setPhoneNumber(updateDTO.getPhoneNumber());
            user.setGender(updateDTO.getGender());
            user.setDateOfBirth(updateDTO.getDateOfBirth());
            user.setAddress(updateDTO.getAddress());
            user.setDepartmentId(
                updateDTO.getDepartmentAffiliations() != null && 
                !updateDTO.getDepartmentAffiliations().isEmpty() 
                    ? updateDTO.getDepartmentAffiliations().get(0) 
                    : null
            );
            
            // Update teacher fields
            teacher.setEmployeeNumber(updateDTO.getEmployeeNumber());
            teacher.setHireDate(updateDTO.getHireDate());
            teacher.setEmploymentType(updateDTO.getEmploymentType());
            teacher.setContractEndDate(updateDTO.getContractEndDate());
            teacher.setSalaryGrade(updateDTO.getSalaryGrade());
            teacher.setQualificationSummary(updateDTO.getQualificationSummary());
            
            if (updateDTO.getSubjectsCertified() != null) {
                teacher.setSubjectsCertified(
                    objectMapper.writeValueAsString(updateDTO.getSubjectsCertified())
                );
            }
            
            if (updateDTO.getDepartmentAffiliations() != null) {
                teacher.setDepartmentAffiliations(
                    objectMapper.writeValueAsString(updateDTO.getDepartmentAffiliations())
                );
            }
            
            teacher.setOfficialEmail(updateDTO.getOfficialEmail());
            teacher.setOfficeExtension(updateDTO.getOfficeExtension());
            teacher.setOfficeRoom(updateDTO.getOfficeRoom());
            teacher.setTeachingHoursPerWeek(updateDTO.getTeachingHoursPerWeek());
            teacher.setAdministrativeHoursPerWeek(updateDTO.getAdministrativeHoursPerWeek());
            
            teacher.setUpdatedAt(LocalDateTime.now());
            
            userRepository.save(user);
            Teacher updatedTeacher = teacherRepository.save(teacher);
            
            return convertToDTO(updatedTeacher);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update teacher status (ACTIVE, ON_LEAVE, etc.)
     */
    public TeacherDTO updateTeacherStatus(Long id, String status) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        
        teacher.setEmploymentStatus(status);
        
        // If teacher is resigned or retired, deactivate user account
        if (status.equals("RESIGNED") || status.equals("RETIRED")) {
            teacher.getUser().setIsActive(false);
        }
        
        Teacher updatedTeacher = teacherRepository.save(teacher);
        return convertToDTO(updatedTeacher);
    }
    
    /**
     * Assign classes to teacher
     */
    public TeacherDTO assignClasses(Long teacherId, Map<String, Object> classAssignments) {
        try {
            Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));
            
            teacher.setClassesAssigned(objectMapper.writeValueAsString(classAssignments));
            
            Teacher updatedTeacher = teacherRepository.save(teacher);
            return convertToDTO(updatedTeacher);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error assigning classes: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get teachers by department
     */
    public List<TeacherDTO> getTeachersByDepartment(Long departmentId) {
        // Since departmentAffiliations is JSONB, we need to filter in memory
        // For production, consider a native query
        return teacherRepository.findAll().stream()
            .filter(teacher -> {
                try {
                    if (teacher.getDepartmentAffiliations() != null) {
                        List<Long> depts = objectMapper.readValue(
                            teacher.getDepartmentAffiliations(), 
                            new TypeReference<List<Long>>() {}
                        );
                        return depts.contains(departmentId);
                    }
                } catch (JsonProcessingException e) {
                    // Log error
                }
                return false;
            })
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get teachers by subject certified
     */
    public List<TeacherDTO> getTeachersBySubject(String subject) {
        return teacherRepository.findAll().stream()
            .filter(teacher -> {
                try {
                    if (teacher.getSubjectsCertified() != null) {
                        List<String> subjects = objectMapper.readValue(
                            teacher.getSubjectsCertified(), 
                            new TypeReference<List<String>>() {}
                        );
                        return subjects.contains(subject);
                    }
                } catch (JsonProcessingException e) {
                    // Log error
                }
                return false;
            })
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Delete/deactivate teacher
     */
    @Transactional
    public void deactivateTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        
        teacher.setEmploymentStatus("RESIGNED");
        teacher.getUser().setIsActive(false);
        teacherRepository.save(teacher);
    }
    
    /**
     * Get next sequence number for teacher display ID
     */
    private Long getNextTeacherSequence() {
        Long maxId = teacherRepository.count() + 1;
        return maxId;
    }
    
    /**
     * Convert Teacher entity to TeacherDTO
     */
    private TeacherDTO convertToDTO(Teacher teacher) {
        TeacherDTO dto = new TeacherDTO();
        User user = teacher.getUser();
        
        // User fields
        dto.setId(teacher.getId());
        dto.setDisplayId(user.getDisplayId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setGender(user.getGender());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setAddress(user.getAddress());
        
        // Teacher fields
        dto.setEmployeeNumber(teacher.getEmployeeNumber());
        dto.setHireDate(teacher.getHireDate());
        dto.setEmploymentType(teacher.getEmploymentType());
        dto.setContractEndDate(teacher.getContractEndDate());
        dto.setSalaryGrade(teacher.getSalaryGrade());
        dto.setQualificationSummary(teacher.getQualificationSummary());
        dto.setOfficialEmail(teacher.getOfficialEmail());
        dto.setOfficeExtension(teacher.getOfficeExtension());
        dto.setOfficeRoom(teacher.getOfficeRoom());
        dto.setLastTrainingDate(teacher.getLastTrainingDate());
        dto.setNextTrainingDate(teacher.getNextTrainingDate());
        dto.setTeachingHoursPerWeek(teacher.getTeachingHoursPerWeek());
        dto.setAdministrativeHoursPerWeek(teacher.getAdministrativeHoursPerWeek());
        dto.setEmploymentStatus(teacher.getEmploymentStatus());
        dto.setLeaveBalanceDays(teacher.getLeaveBalanceDays());
        
        // User account fields
        dto.setIsActive(user.getIsActive());
        dto.setLastLogin(user.getLastLogin());
        dto.setCreatedAt(user.getCreatedAt());
        
        // Parse JSON fields
        try {
            if (teacher.getSubjectsCertified() != null) {
                dto.setSubjectsCertified(
                    objectMapper.readValue(teacher.getSubjectsCertified(), new TypeReference<List<String>>() {})
                );
            } else {
                dto.setSubjectsCertified(new ArrayList<>());
            }
            
            if (teacher.getClassesAssigned() != null && !teacher.getClassesAssigned().equals("{}")) {
                dto.setClassesAssigned(
                    objectMapper.readValue(teacher.getClassesAssigned(), new TypeReference<Map<String, Object>>() {})
                );
            } else {
                dto.setClassesAssigned(new HashMap<>());
            }
            
            if (teacher.getDepartmentAffiliations() != null) {
                dto.setDepartmentAffiliations(
                    objectMapper.readValue(teacher.getDepartmentAffiliations(), new TypeReference<List<Long>>() {})
                );
                
                // Get primary department name if available
                if (!dto.getDepartmentAffiliations().isEmpty()) {
                    Long primaryDeptId = dto.getDepartmentAffiliations().get(0);
                    dto.setPrimaryDepartmentId(primaryDeptId);
                    
                    // You can fetch department name here if needed
                    // departmentRepository.findById(primaryDeptId).ifPresent(dept -> dto.setPrimaryDepartmentName(dept.getName()));
                }
            } else {
                dto.setDepartmentAffiliations(new ArrayList<>());
            }
            
            if (teacher.getTrainingCertifications() != null) {
                dto.setTrainingCertifications(
                    objectMapper.readValue(teacher.getTrainingCertifications(), new TypeReference<List<Map<String, Object>>>() {})
                );
            }
            
        } catch (JsonProcessingException e) {
            // Log error and return DTO with empty collections
            System.err.println("Error parsing JSON for teacher " + teacher.getId() + ": " + e.getMessage());
        }
        
        return dto;
    }
}