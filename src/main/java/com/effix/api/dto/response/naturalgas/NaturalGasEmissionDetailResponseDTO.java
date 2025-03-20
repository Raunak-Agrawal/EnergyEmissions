package com.effix.api.dto.response.naturalgas;

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
public class NaturalGasEmissionDetailResponseDTO {
    private Long id;
    private Long meterId;
    private String supplier;
    private List<NaturalGasEmissionDTO> emissions;
}
