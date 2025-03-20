package com.effix.api.dto.request.targetsandmeasures;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class CreateMeasuresDTO {
    @NotBlank(message = "emission category can not be blank")
    private String emissionCategory;

    @NotEmpty(message = "measure can not be blank")
    private List<String> typeOfMeasure;

    private String startDate;
}
