package com.school.management.repository;

import com.school.management.domain.SpreadsheetData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpreadsheetDataRepository extends JpaRepository<SpreadsheetData, Long> {
    List<SpreadsheetData> findBySpreadsheetIdAndRowStatus(Long spreadsheetId, String rowStatus);
    List<SpreadsheetData> findBySpreadsheetId(Long spreadsheetId);
    void deleteBySpreadsheetIdAndRowStatus(Long spreadsheetId, String rowStatus);
}
