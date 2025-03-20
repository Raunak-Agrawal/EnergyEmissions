package com.effix.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Water {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JsonIgnore
    private UserModel userModel;
    private Double waterConsumed;
    private Double CO2Emission;
    private Double CH4Emission;
    private Double N2OEmission;
}
