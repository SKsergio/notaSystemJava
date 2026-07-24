package com.sistema.notas.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.TenantId;

@MappedSuperclass
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public class AuditableEntity extends GlobalAuditableEntity {
    //multi empresa o institucion
    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
}
