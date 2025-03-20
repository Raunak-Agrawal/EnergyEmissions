package com.effix.api.dto.response.employeecommute;

import com.effix.api.dto.response.electricity.ElectricityEmissionDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmployeeCommuteEmissionResponseDTO {
    private LocalDate date;
    private List<EmployeeCommuteEmissionDetailResponseDTO> emissions;
}
