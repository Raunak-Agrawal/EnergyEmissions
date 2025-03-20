package com.effix.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {

    @Id
    @GeneratedValue
    private Long id;

    private String areaForSolarPanel;

    private String discountRate;

    private String capitolRecoveryFactor;

    private String investmentTimeHorizon;

    @ManyToOne(targetEntity = UserModel.class)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserModel userModelId;

    private Date created;
}
