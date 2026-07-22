package com.sistema.notas.service.core;

import com.sistema.notas.dto.core.managerStudents.AssignedStudentDetailDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentRequestDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentRequestEditDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;

import java.time.LocalDate;
import java.util.List;

public interface ManagerStudentService {
    ManagerStudentResponseDTO save(ManagerStudentRequestDTO requestDTO);
    ManagerStudentResponseDTO update(ManagerStudentRequestEditDTO requestDTO, Integer Id);//SE VA CAMBIAR
    void delete(Integer id);
    PaginateResponse<ManagerStudentResponseDTO> obtenerManagerStudents(int page, int size, String search , LocalDate fromDate, LocalDate  toDate);
    PaginateResponse<AssignedStudentDetailDTO> getAsignedStudents(int page, int size, String search, Integer managerId);
    List<ManagerStudentResponseDTO> listarToSelects();
}
