package com.sistema.notas.entity.catalogues;

import com.sistema.notas.entity.AbstractCatalogo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE subjects SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class Subject extends AbstractCatalogo {
    @Column(name = "description", nullable = true, length = 255)
    private String description;
}