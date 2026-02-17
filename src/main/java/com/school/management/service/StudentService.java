package com.school.management.service;

import com.school.management.domain.Student;
import com.school.management.domain.User;
import com.school.management.repository.StudentRepository;
import com.school.management.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public StudentService(StudentRepository studentRepository, 
                         UserRepository userRepository,
                         UserService userService) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Student registerStudent(StudentRegistrationDto dto, Long createdByUserId) {
        // Create user account first (ID_ONLY auth type)
        User user = new User();
        user.setDisplayIdPrefix("STU");
        // Use current year for display ID, not admission year
        user.setDisplayIdYear(java.time.Year.now().getValue());
        user.setDisplayIdSequence(generateNextSequence());
        user.setAuthType("ID_ONLY");
        user.setFullName(dto.getFullName());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGender(dto.getGender());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setUserType("STUDENT");
        user.setIsActive(true);
        user.setIsLocked(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy(createdByUserId);

        User savedUser = userRepository.save(user);

        // Create student profile
        Student student = new Student();
        student.setId(savedUser.getId());
        student.setUser(savedUser);
        student.setAdmissionDate(dto.getAdmissionDate());
        student.setAdmissionYear(dto.getAdmissionYear());
        student.setCurrentClass(dto.getCurrentClass());
        student.setClassTeacherId(dto.getClassTeacherId());
        student.setParentGuardianName(dto.getParentGuardianName());
        student.setParentRelationship(dto.getParentRelationship());
        student.setParentPhone(dto.getParentPhone());
        student.setParentEmail(dto.getParentEmail());
        student.setEmergencyContactName(dto.getEmergencyContactName());
        student.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        student.setEmergencyContactRelationship(dto.getEmergencyContactRelationship());
        student.setMedicalConditions(dto.getMedicalConditions());
        student.setAllergies(dto.getAllergies());
        student.setBloodGroup(dto.getBloodGroup());
        student.setPreviousSchool(dto.getPreviousSchool());
        student.setStudentStatus("ACTIVE");

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> getStudentsByClass(String className) {
        return studentRepository.findByCurrentClass(className);
    }

    public List<Student> getStudentsByTeacher(Long teacherId) {
        return studentRepository.findByClassTeacherId(teacherId);
    }

    public List<Student> getActiveStudents() {
        return studentRepository.findByStudentStatus("ACTIVE");
    }

    public Student updateStudent(Long id, StudentUpdateDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Update student fields
        student.setCurrentClass(dto.getCurrentClass());
        student.setClassTeacherId(dto.getClassTeacherId());
        student.setParentPhone(dto.getParentPhone());
        student.setParentEmail(dto.getParentEmail());
        student.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        student.setMedicalConditions(dto.getMedicalConditions());
        student.setStudentStatus(dto.getStudentStatus());

        return studentRepository.save(student);
    }

    public void transferStudent(Long id, String newClass, Long newTeacherId) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        student.setCurrentClass(newClass);
        student.setClassTeacherId(newTeacherId);
        studentRepository.save(student);
    }

    public void graduateStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        student.setStudentStatus("GRADUATED");
        student.setExitDate(LocalDate.now());
        student.setExitReason("Graduated");
        studentRepository.save(student);
        
        // Deactivate user account
        User user = student.getUser();
        user.setIsActive(false);
        userRepository.save(user);
    }

    private Integer generateNextSequence() {
        // Simple implementation - you might want to make this more sophisticated
        return (int) (studentRepository.count() + 1);
    }

    // DTO Classes
    public static class StudentRegistrationDto {
        private String fullName;
        private LocalDate dateOfBirth;
        private String gender;
        private String phoneNumber;
        private String address;
        private LocalDate admissionDate;
        private Integer admissionYear;
        private String currentClass;
        private Long classTeacherId;
        private String parentGuardianName;
        private String parentRelationship;
        private String parentPhone;
        private String parentEmail;
        private String parentOccupation;
        private String emergencyContactName;
        private String emergencyContactPhone;
        private String emergencyContactRelationship;
        private String medicalConditions;
        private String allergies;
        private String bloodGroup;
        private String previousSchool;

        // Getters and Setters
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public LocalDate getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public LocalDate getAdmissionDate() { return admissionDate; }
        public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }
        public Integer getAdmissionYear() { return admissionYear; }
        public void setAdmissionYear(Integer admissionYear) { this.admissionYear = admissionYear; }
        public String getCurrentClass() { return currentClass; }
        public void setCurrentClass(String currentClass) { this.currentClass = currentClass; }
        public Long getClassTeacherId() { return classTeacherId; }
        public void setClassTeacherId(Long classTeacherId) { this.classTeacherId = classTeacherId; }
        public String getParentGuardianName() { return parentGuardianName; }
        public void setParentGuardianName(String parentGuardianName) { this.parentGuardianName = parentGuardianName; }
        public String getParentRelationship() { return parentRelationship; }
        public void setParentRelationship(String parentRelationship) { this.parentRelationship = parentRelationship; }
        public String getParentPhone() { return parentPhone; }
        public void setParentPhone(String parentPhone) { this.parentPhone = parentPhone; }
        public String getParentEmail() { return parentEmail; }
        public void setParentEmail(String parentEmail) { this.parentEmail = parentEmail; }
        public String getParentOccupation() { return parentOccupation; }
        public void setParentOccupation(String parentOccupation) { this.parentOccupation = parentOccupation; }
        public String getEmergencyContactName() { return emergencyContactName; }
        public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
        public String getEmergencyContactPhone() { return emergencyContactPhone; }
        public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
        public String getEmergencyContactRelationship() { return emergencyContactRelationship; }
        public void setEmergencyContactRelationship(String emergencyContactRelationship) { this.emergencyContactRelationship = emergencyContactRelationship; }
        public String getMedicalConditions() { return medicalConditions; }
        public void setMedicalConditions(String medicalConditions) { this.medicalConditions = medicalConditions; }
        public String getAllergies() { return allergies; }
        public void setAllergies(String allergies) { this.allergies = allergies; }
        public String getBloodGroup() { return bloodGroup; }
        public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
        public String getPreviousSchool() { return previousSchool; }
        public void setPreviousSchool(String previousSchool) { this.previousSchool = previousSchool; }
    }

    public static class StudentUpdateDto {
        private String currentClass;
        private Long classTeacherId;
        private String parentPhone;
        private String parentEmail;
        private String emergencyContactPhone;
        private String medicalConditions;
        private String studentStatus;

        // Getters and Setters
        public String getCurrentClass() { return currentClass; }
        public void setCurrentClass(String currentClass) { this.currentClass = currentClass; }
        public Long getClassTeacherId() { return classTeacherId; }
        public void setClassTeacherId(Long classTeacherId) { this.classTeacherId = classTeacherId; }
        public String getParentPhone() { return parentPhone; }
        public void setParentPhone(String parentPhone) { this.parentPhone = parentPhone; }
        public String getParentEmail() { return parentEmail; }
        public void setParentEmail(String parentEmail) { this.parentEmail = parentEmail; }
        public String getEmergencyContactPhone() { return emergencyContactPhone; }
        public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
        public String getMedicalConditions() { return medicalConditions; }
        public void setMedicalConditions(String medicalConditions) { this.medicalConditions = medicalConditions; }
        public String getStudentStatus() { return studentStatus; }
        public void setStudentStatus(String studentStatus) { this.studentStatus = studentStatus; }
    }
}