package com.effix.api.repository;

import com.effix.api.model.entity.ElectricityEmission;
import com.effix.api.model.entity.ElectricityMeter;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ElectricityEmissionRepository extends JpaRepository<ElectricityEmission, Long> {
    ElectricityEmission findByMeter(ElectricityMeter meter);
}

