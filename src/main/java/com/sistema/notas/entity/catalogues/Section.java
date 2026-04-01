package com.sistema.notas.entity.catalogues;

import com.sistema.notas.entity.AbstractCatalogo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "sections")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE sections SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class Section extends AbstractCatalogo { }