package com.effix.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "electricity_emissions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityEmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false)
    private Float electricityReading;

    @OneToOne
    private Emissions emissions;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "electricity_meter_id")
    private ElectricityMeter meter;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
