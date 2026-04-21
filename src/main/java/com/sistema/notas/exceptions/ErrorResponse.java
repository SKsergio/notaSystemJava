package com.sistema.notas.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String mensaje;
    private int status;
    private LocalDateTime timestamp;
    private String errorDetail;
    private Map<String, String> erroresEspecificos;

    public ErrorResponse(String mensaje, int status, String errorDetail) {
        this.mensaje = mensaje;
        this.status = status;
        this.errorDetail = errorDetail;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String mensaje, int status, String errorDetail, Map<String, String> erroresEspecificos) {
        this.mensaje = mensaje;
        this.status = status;
        this.errorDetail = errorDetail;
        this.timestamp = LocalDateTime.now();
        this.erroresEspecificos = erroresEspecificos;
    }
}