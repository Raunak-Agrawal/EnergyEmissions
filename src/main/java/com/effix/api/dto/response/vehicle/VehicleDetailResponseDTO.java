package com.effix.api.dto.response.vehicle;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VehicleDetailResponseDTO {
    private VehicleModelDTO model;
    private VehicleSizeDTO size;
    private VehicleFuelTypeDTO fuelType;
    private List<VehicleEmissionDTO> emissions;
}
