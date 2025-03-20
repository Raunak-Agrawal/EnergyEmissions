package com.effix.api.dto.request.vehicle;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.Emissions;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleRequestDTO {
    @NotBlank(message = "fuel type can not be blank")
    private String fuelType;

    @NotBlank(message = "vehicle model can not be blank")
    private String vehicleModel;

    @NotBlank(message = "vehicle size can not be blank")
    private String vehicleSize;

    private CreateVehicleUsageRequestDTO vehicleUsage;

//    private Emissions emissions;

//    public Boolean getFuelType(){
     //   return Boolean.valueOf(fuelType);}

}
/*
{
        "vehicle_brand" : "Audi",
        "km_driven_per_vehicle" : "2000",
        "fuel_use_per_vehicle" : "200",
        "fuel_type" : "Diesel",
        "fuel_bill" : "1000",
        "vehicle_model":"P",
        "co2_emissions": 250,
        "user" : 1
}

 */