package com.effix.api.model.entity;

import com.effix.api.enums.FuelType;
import com.effix.api.enums.SizeCategory;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    private CompanyProfile companyProfile;

    private String model;
    private FuelType fuelType; // Enum: DIESEL, PETROL, HYBRID, CNG, LPG, PHEV, BEV
    private SizeCategory sizeCategory; // Enum: SMALL, MEDIUM, LARGE, AVERAGE

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<VehicleUsage> usages;

//    public Emissions getEmissions() {
//        return Emissions.builder()
//                .co2Emissions(usages.stream().mapToDouble(vehicleUsage ->
//                        vehicleUsage.getEmissions().getCo2Emissions()).sum())
//                .totalEmissions(usages.stream().mapToDouble(vehicleUsage ->
//                        vehicleUsage.getEmissions().getTotalEmissions()).sum())
//                .build();
//    }
}

