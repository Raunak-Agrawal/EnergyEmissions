package com.effix.api.model.entity;

import com.effix.api.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employeecommute_emissions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCommuteEmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_commute_id")
    private EmployeeCommute commute;

    private float kmCovered;

    private String modeOfTransport;

    @OneToOne
    private Emissions emissions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
