package com.sistema.notas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.TenantId;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public class AuditableEntity extends GlobalAuditableEntity {
    //multi empresa o institucion
    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private Integer tenantId;
}
