package com.sistema.notas.controller.core;

import com.sistema.notas.dto.core.grades.ReportCardDTO;
import com.sistema.notas.service.core.AcademicReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/core/grades")
public class AcademicReporController {
    private final AcademicReportService academicReportService;

    @GetMapping("/report-card/student/{studentId}/grade/{gradeDetailId}")
    public ResponseEntity<ReportCardDTO> getReportCard(
            @PathVariable Integer studentId,
            @PathVariable Integer gradeDetailId) {

        return ResponseEntity.ok(academicReportService.generateReportCard(studentId, gradeDetailId));
    }
}
