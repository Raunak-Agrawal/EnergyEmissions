package com.effix.api.repository;

import com.effix.api.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NaturalGasEmissionRepository extends JpaRepository<NaturalGasEmission, Long> {
    NaturalGasEmission findByMeter(NaturalGasMeter meter);

}

