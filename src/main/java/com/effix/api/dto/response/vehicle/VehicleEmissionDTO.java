package com.effix.api.dto.response.vehicle;

import com.effix.api.model.entity.Emissions;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VehicleEmissionDTO {
    private LocalDateTime date;
    private Double kmReading;
    private Double co2Emissions;
    private Double ch4Emissions;
    private Double n2oEmissions;
    private Double totalEmissions;
}
