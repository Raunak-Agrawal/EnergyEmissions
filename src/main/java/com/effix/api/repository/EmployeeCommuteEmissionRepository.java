package com.effix.api.repository;

import com.effix.api.model.entity.EmployeeCommuteEmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmployeeCommuteEmissionRepository extends JpaRepository<EmployeeCommuteEmission, Long> {
}

