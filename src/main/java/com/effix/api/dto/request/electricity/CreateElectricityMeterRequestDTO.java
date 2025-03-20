package com.effix.api.dto.request.electricity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class CreateElectricityMeterRequestDTO {
    private long meterId;

    @NotBlank(message = "meter supplier can not be blank")
    private String supplier;

    @NotNull(message = "contractual instruments field can not be blank")
    private Boolean hasContractualInstruments;
}