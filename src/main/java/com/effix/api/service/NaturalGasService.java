package com.effix.api.service;

import com.effix.api.dto.request.naturalgas.CreateNaturalGasMeterRequestDTO;
import com.effix.api.dto.response.naturalgas.NaturalGasEmissionDetailResponseDTO;
import com.effix.api.dto.response.naturalgas.NaturalGasMeterResponseDTO;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.NaturalGasMeter;

import java.time.LocalDateTime;
import java.util.List;

public interface NaturalGasService {
    List<NaturalGasMeterResponseDTO> getAllNaturalGasMeters();

    void deleteNaturalGasMeter(Long id);

    NaturalGasMeter createNaturalGasMeter(CreateNaturalGasMeterRequestDTO naturalGasMeter);

    void addMeterUsage(Long meter, LocalDateTime updatedTime, float initialFloatValue) throws BadRequestException;

    NaturalGasEmissionDetailResponseDTO getNaturalGasMeterDetail(Long id) throws BadRequestException;
}

