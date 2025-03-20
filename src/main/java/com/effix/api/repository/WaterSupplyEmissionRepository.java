package com.effix.api.repository;

import com.effix.api.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WaterSupplyEmissionRepository extends JpaRepository<WaterSupplyEmission, Long> {
    WaterSupplyEmission findByMeter(WaterSupplyMeter meter);

}

