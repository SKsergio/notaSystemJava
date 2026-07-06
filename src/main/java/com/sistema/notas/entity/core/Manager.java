package com.sistema.notas.entity.core;

import com.sistema.notas.entity.InstitutionalPerson;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "managers")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
public class Manager extends InstitutionalPerson {

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<ManagerStudents> studentRelations;
}
