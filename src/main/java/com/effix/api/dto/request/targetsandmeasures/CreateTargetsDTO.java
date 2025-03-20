package com.effix.api.dto.request.targetsandmeasures;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class CreateTargetsDTO {
    @NotBlank(message = "emission category can not be blank")
    private String emissionCategory;

    private double reductionTarget;

    private String startDate;
}
