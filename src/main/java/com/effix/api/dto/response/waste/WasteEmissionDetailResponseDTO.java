package com.effix.api.dto.response.waste;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WasteEmissionDetailResponseDTO {
    private Double co2Emissions;
    private Double ch4Emissions;
    private Double n2oEmissions;
    private Double totalEmissions;
}