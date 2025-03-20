package com.effix.api.service;

import com.effix.api.dto.request.watersupply.CreateWaterSupplyMeterRequestDTO;
import com.effix.api.dto.response.watersupply.WaterSupplyEmissionDetailResponseDTO;
import com.effix.api.dto.response.watersupply.WaterSupplyMeterResponseDTO;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.WaterSupplyMeter;

import java.time.LocalDateTime;
import java.util.List;

public interface WaterSupplyService {
    List<WaterSupplyMeterResponseDTO> getAllWaterSupplyMeters();

    void deleteWaterSupplyMeter(Long id);

    WaterSupplyMeter createWaterSupplyMeter(CreateWaterSupplyMeterRequestDTO waterSupplyMeterRequestDTO);

    void addMeterUsage(Long meter, LocalDateTime updatedTime, float initialFloatValue) throws BadRequestException;

    WaterSupplyEmissionDetailResponseDTO getWaterSupplyMeterDetail(Long id) throws BadRequestException;
}

