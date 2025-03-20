package com.effix.api.repository;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.ElectricityMeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ElectricityMeterRepository extends JpaRepository<ElectricityMeter, Long> {
    List<ElectricityMeter> findByCompanyProfile(CompanyProfile companyProfile);

}

