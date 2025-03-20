package com.effix.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refrigerants")
public class Refrigerant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    private CompanyProfile companyProfile;

    private String name;

    private String appliance;

    private String refrigerantType;

    private double refrigerantCapacity;

    private int yearsOfUsage;

    private boolean isCharged;

    private boolean isDisposed;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "refrigerant", cascade = CascadeType.ALL)
    private List<RefrigerantEmission> refrigerantEmissions;

    // getters and setters...
}
