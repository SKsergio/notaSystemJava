package com.sistema.notas.entity.core;

import com.sistema.notas.entity.InstitutionalPerson;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
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

    //luego que ya este la relacion hecha
//    @OneToMany(mappedBy = "manager")
//    private List<> StudenttoAsigned;
}
