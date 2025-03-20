package com.effix.api.dto.request.employeecommute;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class CreateBusinessTravelRequestDTO {
    private int noOfEmployees;
    @NotBlank(message = "employee group name can not be blank")
    private String employeeGroupName;

    @NotNull(message = "business transport details cannot be blank")
    private List<BusinessTravelTransportModeDTO> commuteTransportDetails;

}
