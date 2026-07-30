package com.sistema.notas.entity.security;

import com.sistema.notas.entity.GlobalAuditableEntity;
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
public class User extends GlobalAuditableEntity {

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "first_login")
    private boolean firstLogin = true;

    @Column(name = "super_admin", nullable = false)
    private boolean superAdmin = false;
}
