package com.effix.api.model.entity;

import com.effix.api.enums.Country;
import com.effix.api.enums.LookupType;
import com.effix.api.enums.VehicleType;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Lookup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.ORDINAL)
    private LookupType type;
    @Enumerated(EnumType.ORDINAL)
    private Country country;
    @Enumerated(EnumType.ORDINAL)
    private VehicleType vehicleType;
    private String description;
}
