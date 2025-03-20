package com.effix.api.repository;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.WaterSupplyMeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterSupplyMeterRepository extends JpaRepository<WaterSupplyMeter, Long> {
    List<WaterSupplyMeter> findByCompanyProfile(CompanyProfile companyProfile);

}
