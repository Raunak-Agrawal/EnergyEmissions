package com.effix.api.repository;

import com.effix.api.model.entity.BusinessTravel;
import com.effix.api.model.entity.EmployeeCommute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BusinessTravelRepository extends JpaRepository<BusinessTravel, Long> {
    @Query("SELECT e FROM BusinessTravel e WHERE DATE(e.createdAt) BETWEEN :startDate AND :endDate AND e.companyProfile.id = :companyId")
    List<BusinessTravel> findByDateRangeAndCompanyId(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate,
                                                      @Param("companyId") Long companyId);
}
