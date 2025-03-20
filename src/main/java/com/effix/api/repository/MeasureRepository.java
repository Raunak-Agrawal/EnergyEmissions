package com.effix.api.repository;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.Measure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Long> {
    List<Measure> findByCompanyProfile(CompanyProfile companyProfile);

    @Query("SELECT e FROM Measure e WHERE e.emissionCategory = :emissionCategory")
    List<Measure> findByEmissionCategory(String emissionCategory);

}

