package com.effix.api.repository;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.GasMeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GasMeterRepository extends JpaRepository<GasMeter, Long> {

    List<GasMeter> findByCompanyProfile(CompanyProfile companyProfile);
}

