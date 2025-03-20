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
@Table(name = "electricitymeters")
public class ElectricityMeter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    private CompanyProfile companyProfile;

    private Long meterId;
    private String supplier;
    private boolean hasContractualInstruments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meter", cascade = CascadeType.ALL)
    private List<ElectricityEmission> electricityEmissions;

}

