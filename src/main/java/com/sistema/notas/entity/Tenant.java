package com.sistema.notas.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import lombok.EqualsAndHashCode;


@Entity
@Table(name = "tenant")
@Getter @Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE tenant SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "nit", nullable = false, unique = true)
    private String nit;

    @Column(name = "legalMane", nullable = false, unique = true)
    private String legalName;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "url", nullable = false, unique = true)
    private String domainOrSlug;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    protected boolean active = true;
}
