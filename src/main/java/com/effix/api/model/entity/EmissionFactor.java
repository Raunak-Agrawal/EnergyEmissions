package com.effix.api.model.entity;


import com.effix.api.enums.EmissionFactorType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class EmissionFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private EmissionFactorType category; // Diesel, Petrol, Refrigerants, Waste, Electricity, NaturalGas

    @ManyToOne
    private Lookup subCategory; // Vehicle Type, Appliance Type, Waste Type, Provider, etc.

    private String unit;

    private Double CO2EmissionFactor;
    private Double CH4EmissionFactor;
    private Double N2OEmissionFactor;
    private Double TotalEmissionFactor;

    // Add other fields as necessary

    // Standard getters and setters...
}


