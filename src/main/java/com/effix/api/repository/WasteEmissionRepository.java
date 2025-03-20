package com.effix.api.repository;

import com.effix.api.model.entity.WasteEmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WasteEmissionRepository extends JpaRepository<WasteEmission, Long> {
}
