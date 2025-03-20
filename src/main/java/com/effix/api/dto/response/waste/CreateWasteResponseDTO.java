package com.effix.api.dto.response.waste;

import com.effix.api.dto.request.waste.TreatmentMethod;
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
public class CreateWasteResponseDTO {
    private Long id;
    private String wasteCategory;
    private String disposalDate;
    private double weight;
    private String treatmentMethod;
}
