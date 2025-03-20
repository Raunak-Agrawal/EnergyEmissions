package com.effix.api.service;

import com.effix.api.dto.request.vehicle.VehicleUsageRequestDTO;
import com.effix.api.dto.response.vehicle.VehicleDTO;
import com.effix.api.model.entity.Vehicle;
import com.effix.api.model.entity.VehicleUsage;

import java.util.List;

public interface FuelService {

    Vehicle addvehicle(VehicleDTO vehicleDTO) throws Exception;

    VehicleUsage addVehicleReading(VehicleUsageRequestDTO vehicleUsageDTO) throws Exception;

    List<VehicleUsage> getVehicleReading(Long vehicleId);

}
