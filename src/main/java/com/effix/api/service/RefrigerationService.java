package com.effix.api.service;

import com.effix.api.dto.request.refrigerant.CreateRefrigerantRequestDTO;
import com.effix.api.dto.response.refrigerant.RefrigerantEmissionDetailResponseDTO;
import com.effix.api.dto.response.refrigerant.RefrigerantResponseDTO;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.Refrigerant;

import java.time.LocalDateTime;
import java.util.List;

public interface RefrigerationService {

    Refrigerant createRefrigerationRecord(CreateRefrigerantRequestDTO refrigeration);

    RefrigerantEmissionDetailResponseDTO getRefrigerantDetail(Long id) throws BadRequestException;

    List<RefrigerantResponseDTO> getAllRefrigerants();

    void deleteRefrigerant(Long id);

    void addRefrigerantUsage(Long id, LocalDateTime currentTime, double newValue) throws BadRequestException;
}

