package com.effix.api.repository;

import com.effix.api.model.entity.BusinessTravel;
import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.Waste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WasteRepository extends JpaRepository<Waste, Long> {
    List<Waste> findByCompanyProfile(CompanyProfile companyProfile);

    @Query("SELECT e FROM Waste e WHERE DATE(e.createdAt) BETWEEN :startDate AND :endDate AND e.companyProfile.id = :companyId")
    List<Waste> findByDateRangeAndCompanyId(@Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate,
                                                     @Param("companyId") Long companyId);
}

