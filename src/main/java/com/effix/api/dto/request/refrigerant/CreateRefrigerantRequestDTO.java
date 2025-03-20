package com.effix.api.dto.request.refrigerant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class CreateRefrigerantRequestDTO {
    @NotBlank(message = "display name not be blank")
    private String refrigerantDisplayName;

    @NotBlank(message = "appliance name can not be blank")
    private String appliance;

    @NotBlank(message = "refrigerant type can not be blank")
    private String refrigerantType;

    private double refrigerantCapacity;

    private int yearsOfUsage;

    @NotNull(message = "charged field can not be blank")
    private Boolean isCharged;

    @NotNull(message = "disposed field can not be blank")
    private Boolean isDisposed;
}