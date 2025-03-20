package com.effix.api.dto.request.targetsandmeasures;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMeasuresDTO {
    @NotBlank(message = "emission category can not be blank")
    private String emissionCategory;

    @NotBlank(message = "measure can not be blank")
    private String typeOfMeasure;

    private String startDate;
}
