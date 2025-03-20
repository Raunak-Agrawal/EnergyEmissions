package com.effix.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "naturalgas_emissions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NaturalGasEmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false)
    private Float naturalGasReading;

    @OneToOne
    private Emissions emissions;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "natural_gas_meter_id")
    private NaturalGasMeter meter;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

