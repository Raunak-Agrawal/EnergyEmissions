package com.effix.api.dto.request.waste;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateWasteRequestDTO {
    private String wasteCategory;
    private String disposalDate;
    private int sizeOfBag;
    private List<TreatmentMethod> treatmentMethod;
}
