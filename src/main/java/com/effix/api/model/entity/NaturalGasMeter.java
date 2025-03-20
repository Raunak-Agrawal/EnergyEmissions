package com.effix.api.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "naturalgasmeters")
public class NaturalGasMeter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    private CompanyProfile companyProfile;

    private Long meterId;
    private String supplier;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meter", cascade = CascadeType.ALL)
    private List<NaturalGasEmission> naturalGasEmissions;
}
