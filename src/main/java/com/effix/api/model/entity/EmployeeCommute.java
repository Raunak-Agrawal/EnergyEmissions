package com.effix.api.model.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employee_commutes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCommute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int workingDays;
    private String employeeName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    private CompanyProfile companyProfile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commute", cascade = CascadeType.ALL)
    private List<EmployeeCommuteEmission> employeeCommuteEmissions;
}
