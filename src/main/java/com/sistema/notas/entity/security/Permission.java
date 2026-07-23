package com.sistema.notas.entity.security;

import com.sistema.notas.entity.GlobalAuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "permissions", indexes = {
        @Index(name = "idx_permissions_code", columnList = "code", unique = true)
})
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE permissions SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class Permission extends GlobalAuditableEntity{
    
    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code; // Ej: "student:read", "grade:write"

    @Column(name = "name", length = 100, nullable = false)
    private String name; // Ej: "Ver Estudiantes", "Modificar Notas"

    @Column(name = "module", length = 50, nullable = false)
    private String module; // Ej: "ESTUDIANTES", "NOTAS", "SISTEMA"
}
