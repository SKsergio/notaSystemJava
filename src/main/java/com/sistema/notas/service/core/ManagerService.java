package com.sistema.notas.service.core;

import com.sistema.notas.dto.core.managers.ManagerRequestDTO;
import com.sistema.notas.dto.core.managers.ManagerRequestUpdateDTO;
import com.sistema.notas.dto.core.managers.ManagerResponseDTO;
import com.sistema.notas.dto.core.managers.*;
import com.sistema.notas.dto.generics.PaginateResponse;

import java.time.LocalDate;
import java.util.List;

public interface ManagerService {

    //crud basico
    ManagerResponseDTO save(ManagerRequestDTO manager);
    ManagerResponseDTO update(Integer id, ManagerRequestUpdateDTO manager);
    void delete(Integer id);
    PaginateResponse<ManagerResponseDTO> obtenerManagerPaginados(int page, int size, String search , LocalDate fromDate, LocalDate  toDate);
    List<ManagerSimpleResponseDTO> listartoSelect();
    ManagerFullResponseDTO obtenerManager(Integer id);
    ManagerResponseEditDTO obtenerManagertoEdit(Integer id);
}
