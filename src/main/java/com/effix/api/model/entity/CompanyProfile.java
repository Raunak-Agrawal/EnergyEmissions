package com.effix.api.model.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "company_profiles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NonNull
    private UserModel userModel;

    private String companyName;
    private String jobTitle;
    private String location;
    private String country;
    private String industry;
    private Integer numberOfEmployees;
    private Double totalOfficeArea;
    private String natureOfBusiness;
    @Lob
    private byte[] logo;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
