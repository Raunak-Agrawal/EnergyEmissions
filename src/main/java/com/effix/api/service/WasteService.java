package com.effix.api.service;

import com.effix.api.dto.request.waste.CreateWasteRequestDTO;
import com.effix.api.dto.response.waste.WasteEmissionResponseDTO;
import com.effix.api.model.entity.Waste;

import java.util.List;

public interface WasteService {
    List<Waste> createWaste(CreateWasteRequestDTO wasteRequestDTO);

    List<WasteEmissionResponseDTO> getAllWasteEmissions();
}

