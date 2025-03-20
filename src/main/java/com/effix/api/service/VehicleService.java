package com.effix.api.service;

import com.effix.api.dto.request.vehicle.CreateVehicleRequestDTO;
import com.effix.api.dto.request.vehicle.VehicleUsageRequestDTO;
import com.effix.api.dto.response.vehicle.VehicleDTO;
import com.effix.api.dto.response.vehicle.VehicleDetailResponseDTO;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.Vehicle;
import com.effix.api.model.entity.VehicleUsage;

import java.util.List;

public interface VehicleService {

    List<VehicleDTO> getVehicles();

    VehicleDetailResponseDTO getVehicleDetail(Long vehicleId) throws BadRequestException;

    Vehicle createVehicle(CreateVehicleRequestDTO vehicle);

    void deleteVehicle(Long id);

    VehicleUsage addVehicleUsage(Long vehicleId, VehicleUsageRequestDTO vehicleUsageRequestDTO) throws BadRequestException;
}

