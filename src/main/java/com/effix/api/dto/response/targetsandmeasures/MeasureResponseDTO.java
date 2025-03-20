package com.effix.api.dto.response.targetsandmeasures;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeasureResponseDTO {
    private Long id;
    private String emissionCategory;

    private String typeOfMeasure;
    
    private String startDate;

    private String createdAt;
}
