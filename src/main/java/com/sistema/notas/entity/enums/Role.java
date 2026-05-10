package com.sistema.notas.entity.enums;

/**
 * Roles soportados por el sistema. Se almacenan como STRING en la BD.
 * - ADMIN  : acceso total (catalogos, usuarios, todo)
 * - TEACHER: acceso a operaciones del core academico
 * - STUDENT: acceso de lectura limitada (a futuro)
 */
public enum Role {
    ADMIN,
    TEACHER,
    STUDENT
}
