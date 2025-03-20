package com.effix.api.service;

import com.effix.api.dto.response.employeecommute.EmployeeCommuteAndBusinessTravelBootstrapResponseDTO;
import com.effix.api.dto.response.refrigerant.RefrigerantBootStrapResponseDTO;
import com.effix.api.dto.response.vehicle.VehicleBootStrapResponseDTO;
import com.effix.api.dto.response.waste.WasteBootstrapResponseDTO;

import java.util.List;

public interface BootStrapService {
    VehicleBootStrapResponseDTO getVehicleBootStrapData();

    List<String> getElectricitySuppliersData();
    List<String> getNaturalGasSuppliersData();

    List<String> getWaterSupplySuppliersData();

    RefrigerantBootStrapResponseDTO getRefrigerantBootStrapData();

    EmployeeCommuteAndBusinessTravelBootstrapResponseDTO getEmployeeCommuteData();

    WasteBootstrapResponseDTO getWasteCategories();
}
