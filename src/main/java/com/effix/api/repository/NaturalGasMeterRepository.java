package com.effix.api.repository;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.NaturalGasMeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NaturalGasMeterRepository extends JpaRepository<NaturalGasMeter, Long> {
    List<NaturalGasMeter> findByCompanyProfile(CompanyProfile companyProfile);

}
