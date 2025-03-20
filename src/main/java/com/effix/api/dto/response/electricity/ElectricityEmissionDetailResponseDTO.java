package com.effix.api.dto.response.electricity;

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
public class ElectricityEmissionDetailResponseDTO {
    private Long id;
    private Long meterId;
    private String supplier;
    private boolean hasContractualInstruments;
    private List<ElectricityEmissionDTO> emissions;
}
