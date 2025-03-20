package com.effix.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refrigerant_emissions")
public class RefrigerantEmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false)
    private Double refrigerantYearsOfUsage;

    @OneToOne
    private Emissions emissions;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refrigerant_id")
    private Refrigerant refrigerant;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}





