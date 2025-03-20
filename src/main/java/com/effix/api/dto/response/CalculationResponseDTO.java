package com.effix.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalculationResponseDTO {



    @JsonProperty("per_day")
    Double perDay;

    @JsonProperty("per_week")
    Double perWeek;

    @JsonProperty("per_month")
    Double perMonth;

    @JsonProperty("per_year")
    Double perYear;
}
