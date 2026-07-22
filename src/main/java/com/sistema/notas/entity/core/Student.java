package com.sistema.notas.entity.core;

import jakarta.persistence.PrimaryKeyJoinColumn;

import com.sistema.notas.entity.InstitutionalPerson;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
public class Student extends InstitutionalPerson{
    @Column(name ="carnet", nullable = false, length = 10, unique = true)
    private String carnet;
}
