package com.sistema.notas.service.core;

import com.sistema.notas.dto.core.grades.ReportCardDTO;

public interface AcademicReportService {
    ReportCardDTO generateReportCard(Integer studentId, Integer gradeDetailId);
}
