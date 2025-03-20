package com.effix.api.dto.request.employeecommute;

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
public class EmployeeCommuteTransportModeDTO {
    private int noOfOneWayJourney;

    @NotBlank(message = "Mode of transport cannot be blank")
    private String modeOfTransport;

    @NotBlank(message = "Type cannot be blank")
    private String type;

    @NotBlank(message = "sub Type cannot be blank")
    private String subType;
}
