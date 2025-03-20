package com.effix.api.repository;

import com.effix.api.model.entity.Emissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmissionRepository extends JpaRepository<Emissions, Long> {
}
