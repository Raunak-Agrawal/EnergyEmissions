package com.effix.api.model;


import com.effix.api.dto.response.CalculationResponseDTO;


public class HeatingCalculations {
    public String department;
    public String building;
    public CalculationResponseDTO naturalGasUseInMJ;
    public CalculationResponseDTO emissionsFromNaturalGasUse;
    public CalculationResponseDTO recommendation_PotentialEmissionsSavingHeatPump;


}
