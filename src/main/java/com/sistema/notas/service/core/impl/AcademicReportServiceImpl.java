package com.sistema.notas.service.core.impl;

import com.sistema.notas.dto.core.grades.CourseAverageDTO;
import com.sistema.notas.dto.core.grades.ReportCardDTO;
import com.sistema.notas.entity.core.CourseRegistration;
import com.sistema.notas.entity.core.DegreeEnrollment;
import com.sistema.notas.entity.core.EvaluationDetail;
import com.sistema.notas.entity.core.Student;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.respository.core.CourseRegistrationRepository;
import com.sistema.notas.respository.core.DegreeEnrollmentRepository;
import com.sistema.notas.respository.core.EvaluationDetailRepository;
import com.sistema.notas.respository.core.StudentRepository;
import com.sistema.notas.service.core.AcademicReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicReportServiceImpl implements AcademicReportService {

    private final StudentRepository studentRepository;
    private final DegreeEnrollmentRepository degreeEnrollmentRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final EvaluationDetailRepository evaluationDetailRepository;

    @Override
    @Transactional(readOnly = true)
    public ReportCardDTO generateReportCard(Integer studentId, Integer gradeDetailId) {

        // 1. Validar que el alumno exista y esté matriculado en ese grado
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        DegreeEnrollment enrollment = degreeEnrollmentRepository
                .findActiveEnrollment(studentId, gradeDetailId, EnrollmentStatus.ACTIVE)
                .orElseThrow(() -> new BadRequestException("El estudiante no está matriculado en este grado activo."));

        // 2. Buscar en qué cursos de este grado está inscrito el alumno
        List<CourseRegistration> courses = courseRegistrationRepository
                .findActiveCoursesByStudentAndGrade(studentId, gradeDetailId, EnrollmentStatus.ACTIVE);


        List<CourseAverageDTO> courseAverages = new ArrayList<>();
        double totalScoreSum = 0.0;

        // 3. Calcular la nota por cada curso
        for (CourseRegistration registration : courses) {
            Integer courseId = registration.getCourse().getId();

            List<EvaluationDetail> grades = evaluationDetailRepository.findByStudentIdAndCourseId(studentId, courseId);

            double courseFinalScore = 0.0;
            double totalEvaluatedPercentage = 0.0; // <-- NUEVO ACUMULADOR

            for (EvaluationDetail detail : grades) {
                double evaluationPercentage = detail.getEvaluation().getPercentage();

                // Sumamos a la nota final
                courseFinalScore += (detail.getGrade() * evaluationPercentage) / 100.0;

                // Sumamos al porcentaje total evaluado
                totalEvaluatedPercentage += evaluationPercentage;
            }

            // Redondeamos a 2 decimales
            courseFinalScore = Math.round(courseFinalScore * 100.0) / 100.0;
            totalEvaluatedPercentage = Math.round(totalEvaluatedPercentage * 100.0) / 100.0;

            String status = courseFinalScore >= 6.0 ? "APROBADO" : "REPROBADO";

            courseAverages.add(new CourseAverageDTO(
                    courseId,
                    registration.getCourse().getName(),
                    courseFinalScore,
                    totalEvaluatedPercentage,
                    status
            ));

            totalScoreSum += courseFinalScore;
        }

        // 4. Calcular el promedio global de todas las materias
        double globalAverage = courses.isEmpty() ? 0.0 : (totalScoreSum / courses.size());
        globalAverage = Math.round(globalAverage * 100.0) / 100.0;

        // 5. Retornar la boleta armada
        return new ReportCardDTO(
                student.getId(),
                student.getfullName(),
                enrollment.getGradeDetail().getFullName(),
                globalAverage,
                courseAverages
        );
    }
}