package com.effix.api.dto.response.employeecommute;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmployeeCommuteAndBusinessTravelBootstrapResponseDTO {
    private HashMap<String, List<CarCommuteResponseDTO>> carCommuteDetails;
    private List<MotorBikeCommuteResponseDTO> motorBikeCommuteDetails;
    private List<BusCommuteResponseDTO> busCommuteDetails;
    private List<RailCommuteResponseDTO> railCommuteDetails;
    private List<TaxiCommuteResponseDTO> taxiCommuteDetails;
    private FlightCommuteResponseDTO flightCommuteDetails;
}
