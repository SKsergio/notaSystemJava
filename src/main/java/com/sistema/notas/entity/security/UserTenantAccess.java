package com.sistema.notas.entity.security;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.sistema.notas.entity.GlobalAuditableEntity;
import com.sistema.notas.entity.Tenant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_tenant_access", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "tenant_id", "role_id"})
})
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE user_tenant_access SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class UserTenantAccess extends GlobalAuditableEntity{

    //USUARIOS
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //TENANTS
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    //ROL
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    //persona institucional
    @Column(name = "institutional_person_id", nullable = true)
    private Integer institutionalPersonId; // Puede ser teacherId o studentId, dependiendo del rol

   //EXCPECIONES POR USUARIO
   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(
       name = "user_tenant_access_exceptions",
       joinColumns = @JoinColumn(name = "user_tenant_access_id"),
       inverseJoinColumns = @JoinColumn(name = "permission_id")
   )
   private Set<Permission> grantedPermissions = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_tenant_revoked_permissions",
            joinColumns = @JoinColumn(name = "user_tenant_access_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> revokedPermissions = new HashSet<>();

}
