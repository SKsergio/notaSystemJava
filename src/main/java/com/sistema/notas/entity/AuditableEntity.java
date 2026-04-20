package com.sistema.notas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public class AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    //autitoria
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    protected boolean active = true;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
}
