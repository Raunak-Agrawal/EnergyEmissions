package com.effix.api.model.entity;

import com.effix.api.enums.Protocol;

import javax.persistence.*;

@Entity
@Table(name = "gas_meters")
public class GasMeter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private CompanyProfile companyProfile;

    private Integer numberOfMeters;
    private Protocol protocol;

    // Fields for S0, P1, HAN protocols can go here

    // ... additional fields, getters, setters, etc.
}

