package com.effix.api.repository;

import com.effix.api.model.entity.BusinessTravelEmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BusinessTravelEmissionRepository extends JpaRepository<BusinessTravelEmission, Long> {
}

