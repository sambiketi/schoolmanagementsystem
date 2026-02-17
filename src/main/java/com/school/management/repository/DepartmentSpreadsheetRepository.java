package com.school.management.repository;

import com.school.management.domain.DepartmentSpreadsheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentSpreadsheetRepository extends JpaRepository<DepartmentSpreadsheet, Long> {
    List<DepartmentSpreadsheet> findByDepartmentIdAndIsActiveTrue(Long departmentId);
    Optional<DepartmentSpreadsheet> findByDepartmentIdAndSpreadsheetName(Long departmentId, String spreadsheetName);
    List<DepartmentSpreadsheet> findByPreviousVersionId(Long previousVersionId);
    boolean existsByDepartmentIdAndSpreadsheetName(Long departmentId, String spreadsheetName);
}
