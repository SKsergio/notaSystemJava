package com.sistema.notas.entity;

import jakarta.persistence.Transient; 
import java.time.LocalDate;

import com.sistema.notas.entity.enums.GenderEnum;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import java.time.Period;

@MappedSuperclass
@Data
public class InstitutionalPerson extends AuditableEntity{
    
    @Column(name = "first_name", length = 25, nullable = false)
    protected String firstName;

    @Column(name = "second_name", length = 25, nullable = false)
    protected String secondName;
    
    @Column(name = "first_last_name", length = 25, nullable = false)
    protected String firstLastName;
    
    @Column(name = "second_last_name", length = 25, nullable = false)
    protected String secondLastName;
    
    @Column(name = "address", length = 255)
    protected String address;
    
    @Column(name = "phone_number", length = 15, nullable = false)
    protected String phoneNumber;

    @Column(name = "email", length = 50)
    protected String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 1)
    private GenderEnum gender;

    @Column(name = "route_photo", nullable = false, length = 255)
    private String routePhoto;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Transient
    public Integer getAge() {
        if (this.birthDate == null) {
            return null; 
        }
        return Period.between(this.birthDate, LocalDate.now()).getYears(); 
    }

    @Transient
    public String getfullName() {
        if (this.firstName == null && this.firstLastName == null) {
            return null; 
        }
        String completeName = this.firstName + " " + this.firstLastName;
        return completeName; 
    }
    
}
