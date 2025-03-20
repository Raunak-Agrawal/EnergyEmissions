package com.effix.api.dto.response.naturalgas;

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
public class NaturalGasEmissionDTO {
    private LocalDateTime date;
    private float meterReading;
    private Double co2Emissions;
    private Double ch4Emissions;
    private Double n2oEmissions;
    private Double totalEmissions;
}
