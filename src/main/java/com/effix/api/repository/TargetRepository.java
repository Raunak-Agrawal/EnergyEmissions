package com.effix.api.repository;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.Target;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetRepository extends JpaRepository<Target, Long> {
    List<Target> findByCompanyProfile(CompanyProfile companyProfile);
    @Query("SELECT e FROM Target e WHERE e.emissionCategory = :emissionCategory")
    Target findByEmissionCategory(@Param("emissionCategory") String emissionCategory);
}
