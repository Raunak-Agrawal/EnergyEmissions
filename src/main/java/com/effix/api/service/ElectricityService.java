package com.effix.api.service;

import com.effix.api.dto.request.electricity.CreateElectricityMeterRequestDTO;
import com.effix.api.dto.response.electricity.ElectricityEmissionDetailResponseDTO;
import com.effix.api.dto.response.electricity.ElectricityMeterResponseDTO;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.ElectricityMeter;

import java.time.LocalDateTime;
import java.util.List;

public interface ElectricityService {

    List<ElectricityMeterResponseDTO> getAllElectricityMeters();

    void deleteElectricityMeter(Long id);

    ElectricityMeter createElectricityMeter(CreateElectricityMeterRequestDTO electricityMeter);

    void addMeterUsage(Long meter, LocalDateTime updatedTime, float initialFloatValue) throws BadRequestException;

    ElectricityEmissionDetailResponseDTO getElectricityMeterDetail(Long id) throws BadRequestException;
}

