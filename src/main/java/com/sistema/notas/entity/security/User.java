package com.sistema.notas.entity.security;

import com.sistema.notas.entity.AuditableEntity;
import com.sistema.notas.entity.enums.Role;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * Usuario de aplicacion. Se mantiene como tabla separada para no modificar
 * Teacher/Student. Los campos teacherId/studentId enlazan opcionalmente al
 * registro de la persona correspondiente (sin FK estricta para evitar choques
 * con el soft-delete de teachers/students).
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE users SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class User extends AuditableEntity {

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    /** BCrypt hash. NUNCA se expone por DTO. */
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    /** Vinculo opcional con Teacher.id (cuando el usuario es docente). */
    @Column(name = "teacher_id")
    private Integer teacherId;

    /** Vinculo opcional con Student.id (cuando el usuario es estudiante). */
    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "first_login")
    private boolean firstLogin = true;
}
