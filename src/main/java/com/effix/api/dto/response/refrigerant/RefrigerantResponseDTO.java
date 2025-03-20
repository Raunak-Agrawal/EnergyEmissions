package com.effix.api.dto.response.refrigerant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RefrigerantResponseDTO {
    private Long id;
    private String refrigerantDisplayName;
    private String appliance;
    private String refrigerantType;
    private double refrigerantCapacity;
    private int yearsOfUsage;
}
