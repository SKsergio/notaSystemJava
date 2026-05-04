package com.sistema.notas.entity.catalogues;

import com.sistema.notas.entity.AuditableEntity;
import com.sistema.notas.entity.enums.GenderEnum;
import com.sistema.notas.entity.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "periods")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE periods SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class Period extends AuditableEntity {
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.OPEN;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
