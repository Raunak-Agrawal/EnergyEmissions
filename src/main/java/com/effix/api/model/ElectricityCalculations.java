package com.effix.api.model;

import com.effix.api.dto.response.CalculationResponseDTO;

public class ElectricityCalculations {
    public String department;
    public String building;
    public CalculationResponseDTO co2Emissions;
    public CalculationResponseDTO recommendation_PotentialEmissionsSavingSolarPanels;
    public CalculationResponseDTO recommendation_PotentialEmissionsSavingWindmills;
    public CalculationResponseDTO recommendation_PotentialEmissionsSavingBatteries;
    public CalculationResponseDTO recommendation_PotentialEmissionsSavingSolarAndBattery;

}
