package com.sistema.notas.entity.catalogues;

import com.sistema.notas.entity.AbstractCatalogo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "degrees")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE degrees SET active false where id = ?")
@SQLRestriction("active=true")
public class Degree extends AbstractCatalogo {}