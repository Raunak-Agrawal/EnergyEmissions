package com.effix.api.repository;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.Refrigerant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefrigerantRepository extends JpaRepository<Refrigerant, Long> {
    List<Refrigerant> findByCompanyProfile(CompanyProfile companyProfile);
}