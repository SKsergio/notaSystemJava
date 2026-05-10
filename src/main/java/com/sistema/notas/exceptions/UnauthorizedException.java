package com.sistema.notas.exceptions;

/**
 * Lanzada cuando las credenciales son invalidas o falta autenticacion.
 * Sera mapeada a HTTP 401 por el GlobalExceptionHandler.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
