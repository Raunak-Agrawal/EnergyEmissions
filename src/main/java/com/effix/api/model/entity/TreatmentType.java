package com.effix.api.model.entity;

import javax.persistence.*;

@Entity
public class TreatmentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Lookup lookup;
}
