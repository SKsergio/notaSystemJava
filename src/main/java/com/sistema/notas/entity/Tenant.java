package com.sistema.notas.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Table(name = "tenant")
@Getter @Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE tenant SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class Tenant extends GlobalAuditableEntity{

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "nit", length = 14, nullable = false, unique = true)
    private String nit;

     @Column(name = "nrc", length = 8, nullable = false, unique = true)
    private String nrc;

    @Column(name = "legal_name", nullable = false, unique = true)
    private String legalName;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "logo_url", nullable = true)
    private String logoUrl;
}
