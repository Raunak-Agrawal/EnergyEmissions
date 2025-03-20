package com.effix.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RecommendationRequestDTO {

    @JsonProperty("area_for_solar_panel")
    private String areaForSolarPanel;

    @JsonProperty("discount_rate")
    private String discountRate;

    @JsonProperty("capitol_recovery_rate")
    private String capitolRecoveryFactor;

    @JsonProperty("investment_time_horizon")
    private String investmentTimeHorizon;

    @JsonProperty("user")
    private Long user;
}
