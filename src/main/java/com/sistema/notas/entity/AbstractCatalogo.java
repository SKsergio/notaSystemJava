package com.sistema.notas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass//sera un molde, o clase abstracta
@Data
public class AbstractCatalogo extends AuditableEntity{

    @Column(name = "name", length = 100, nullable = false)
    protected String name;

    @Column(name = "code", nullable = false, length = 25, unique = true)
    protected String code;
    protected boolean active = true;
}
