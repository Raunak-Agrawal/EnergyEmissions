package com.effix.api.model;

import com.effix.api.dto.response.CalculationResponseDTO;

public class TransportationCalculations {
    public String vehicle;
    public CalculationResponseDTO energyUsePerKm;
    public CalculationResponseDTO emissionPerVehiclePerKm;
    public CalculationResponseDTO totalEmissionTransportation;
    public CalculationResponseDTO recommendation_PotentialEmissionsSavingBatteryElectricVehicle;
}
