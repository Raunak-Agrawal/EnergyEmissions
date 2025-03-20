package com.effix.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "business_travels")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessTravel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int noOfEmployees;
    private String groupName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    private CompanyProfile companyProfile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commute", cascade = CascadeType.ALL)
    private List<BusinessTravelEmission> businessTravelEmissions;

}
