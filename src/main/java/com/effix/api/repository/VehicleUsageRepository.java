package com.effix.api.repository;

import com.effix.api.model.entity.Vehicle;
import com.effix.api.model.entity.VehicleUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleUsageRepository extends JpaRepository<VehicleUsage, Long> {

    List<VehicleUsage> findByVehicle(Vehicle vehicle);
}

