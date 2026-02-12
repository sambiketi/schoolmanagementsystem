package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {
    
    @Id
    private Long id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "employee_number", length = 50, unique = true)
    private String employeeNumber;
    
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
    
    @Column(name = "employment_type", length = 20, nullable = false)
    private String employmentType;
    
    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;
    
    @Column(name = "salary_grade", length = 10)
    private String salaryGrade;
    
    @Column(name = "qualification_summary", columnDefinition = "TEXT")
    private String qualificationSummary;
    
    @Column(name = "subjects_certified", columnDefinition = "jsonb")
    private String subjectsCertified;
    
    @Column(name = "classes_assigned", columnDefinition = "jsonb")
    private String classesAssigned;
    
    @Column(name = "department_affiliations", columnDefinition = "jsonb")
    private String departmentAffiliations;
    
    @Column(name = "official_email", length = 255)
    private String officialEmail;
    
    @Column(name = "office_extension", length = 10)
    private String officeExtension;
    
    @Column(name = "office_room", length = 20)
    private String officeRoom;
    
    @Column(name = "last_training_date")
    private LocalDate lastTrainingDate;
    
    @Column(name = "next_training_date")
    private LocalDate nextTrainingDate;
    
    @Column(name = "training_certifications", columnDefinition = "jsonb")
    private String trainingCertifications;
    
    @Column(name = "teaching_hours_per_week")
    private Integer teachingHoursPerWeek;
    
    @Column(name = "administrative_hours_per_week")
    private Integer administrativeHoursPerWeek;
    
    @Column(name = "employment_status", length = 20)
    private String employmentStatus;
    
    @Column(name = "leave_balance_days")
    private Integer leaveBalanceDays;
}