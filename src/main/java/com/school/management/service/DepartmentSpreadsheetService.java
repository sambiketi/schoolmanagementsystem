package com.school.management.service;

import com.school.management.domain.DepartmentSpreadsheet;
import com.school.management.domain.SpreadsheetData;
import com.school.management.repository.DepartmentSpreadsheetRepository;
import com.school.management.repository.SpreadsheetDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentSpreadsheetService {

    private final DepartmentSpreadsheetRepository spreadsheetRepository;
    private final SpreadsheetDataRepository dataRepository;

    @Transactional
    public DepartmentSpreadsheet create(Long departmentId, String name,
                                         String description, String columnsJson,
                                         Long createdBy) {
        if (spreadsheetRepository.existsByDepartmentIdAndSpreadsheetName(departmentId, name)) {
            throw new RuntimeException("Spreadsheet already exists: " + name);
        }
        DepartmentSpreadsheet sheet = DepartmentSpreadsheet.builder()
                .departmentId(departmentId)
                .spreadsheetName(name)
                .description(description)
                .columnsJson(columnsJson)
                .canEdit("[\"DEPARTMENT_ADMIN\"]")
                .canView("[\"DEPARTMENT_ADMIN\", \"TEACHER\"]")
                .version(1)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .createdBy(createdBy)
                .build();
        return spreadsheetRepository.save(sheet);
    }

    @Transactional
    public SpreadsheetData addRow(Long spreadsheetId, String rowDataJson, Long createdBy) {
        SpreadsheetData row = SpreadsheetData.builder()
                .spreadsheetId(spreadsheetId)
                .rowData(rowDataJson)
                .rowHash("")
                .rowStatus("ACTIVE")
                .version(1)
                .createdAt(LocalDateTime.now())
                .createdBy(createdBy)
                .build();
        return dataRepository.save(row);
    }

    @Transactional
    public void deleteRow(Long rowId, Long updatedBy) {
        dataRepository.findById(rowId).ifPresent(row -> {
            row.setRowStatus("DELETED");
            row.setUpdatedAt(LocalDateTime.now());
            row.setUpdatedBy(updatedBy);
            dataRepository.save(row);
        });
    }

    @Transactional(readOnly = true)
    public List<DepartmentSpreadsheet> getByDepartment(Long departmentId) {
        return spreadsheetRepository.findByDepartmentIdAndIsActiveTrue(departmentId);
    }

    @Transactional(readOnly = true)
    public List<SpreadsheetData> getActiveRows(Long spreadsheetId) {
        return dataRepository.findBySpreadsheetIdAndRowStatus(spreadsheetId, "ACTIVE");
    }

    @Transactional(readOnly = true)
    public Optional<DepartmentSpreadsheet> findById(Long id) {
        return spreadsheetRepository.findById(id);
    }
}
