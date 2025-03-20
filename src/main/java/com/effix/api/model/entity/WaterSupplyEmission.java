package com.effix.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "watersupply_emissions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaterSupplyEmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false)
    private Float waterSupplyReading;

    @OneToOne
    private Emissions emissions;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "water_supply_meter_id")
    private WaterSupplyMeter meter;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

