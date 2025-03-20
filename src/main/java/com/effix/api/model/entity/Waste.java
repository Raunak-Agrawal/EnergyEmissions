package com.effix.api.model.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "waste")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Waste {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String wasteCategory;

    private String disposalDate;

    private double weight;
    private String treatmentMethod;

    @ManyToOne
    private CompanyProfile companyProfile;

    @OneToOne
    private WasteEmission wasteEmission;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

