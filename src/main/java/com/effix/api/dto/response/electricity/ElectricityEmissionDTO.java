package com.effix.api.dto.response.electricity;

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
public class ElectricityEmissionDTO {
    private LocalDateTime date;
    private float meterReading;
    private Double co2Emissions;
    private Double ch4Emissions;
    private Double n2oEmissions;
    private Double totalEmissions;
}
