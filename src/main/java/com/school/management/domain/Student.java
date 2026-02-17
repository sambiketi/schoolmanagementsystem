package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    
    @Id
    private Long id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;
    
    @Column(name = "admission_year", nullable = false)
    private Integer admissionYear;
    
    @Column(name = "current_class", length = 50)
    private String currentClass; // Keeping for backward compatibility
    
    @ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;
    
    @Column(name = "class_teacher_id")
    private Long classTeacherId;
    
    @Column(name = "parent_guardian_name", length = 255)
    private String parentGuardianName;
    
    @Column(name = "parent_relationship", length = 50)
    private String parentRelationship;
    
    @Column(name = "parent_phone", length = 20)
    private String parentPhone;
    
    @Column(name = "parent_email", length = 255)
    private String parentEmail;
    
    @Column(name = "parent_occupation", length = 100)
    private String parentOccupation;
    
    @Column(name = "emergency_contact_name", length = 255)
    private String emergencyContactName;
    
    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;
    
    @Column(name = "emergency_contact_relationship", length = 50)
    private String emergencyContactRelationship;
    
    @Column(name = "medical_conditions", columnDefinition = "TEXT")
    private String medicalConditions;
    
    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;
    
    @Column(name = "blood_group", length = 5)
    private String bloodGroup;
    
    @Column(name = "preferred_hospital", length = 255)
    private String preferredHospital;
    
    @Column(name = "previous_school", length = 255)
    private String previousSchool;
    
    @Column(name = "transfer_certificate_number", length = 100)
    private String transferCertificateNumber;
    
    @Column(name = "last_class_passed", length = 50)
    private String lastClassPassed;
    
    @Column(name = "student_status", length = 20)
    private String studentStatus;
    
    @Column(name = "exit_date")
    private LocalDate exitDate;
    
    @Column(name = "exit_reason", columnDefinition = "TEXT")
    private String exitReason;
}