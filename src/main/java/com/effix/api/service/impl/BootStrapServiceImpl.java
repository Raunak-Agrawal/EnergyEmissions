package com.effix.api.service.impl;

import com.effix.api.dto.response.employeecommute.*;
import com.effix.api.dto.response.refrigerant.RefrigerantBootStrapResponseDTO;
import com.effix.api.dto.response.vehicle.VehicleBootStrapResponseDTO;
import com.effix.api.dto.response.vehicle.VehicleFuelTypeDTO;
import com.effix.api.dto.response.vehicle.VehicleModelDTO;
import com.effix.api.dto.response.vehicle.VehicleSizeDTO;
import com.effix.api.dto.response.waste.WasteBootstrapResponseDTO;
import com.effix.api.enums.LookupType;
import com.effix.api.enums.VehicleType;
import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.Lookup;
import com.effix.api.repository.LookupRepository;
import com.effix.api.service.BootStrapService;
import com.effix.api.service.CompanyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BootStrapServiceImpl implements BootStrapService {
    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private CompanyProfileService companyProfileService;
    @Override
    public VehicleBootStrapResponseDTO getVehicleBootStrapData() {
        List<Lookup> fuelTypes = lookupRepository.findByType(LookupType.FUEL_TYPE);
        List<Lookup> vehicleModels = lookupRepository.findByType(LookupType.VEHICLE_MODEL);
        List<Lookup> vehicleSizes = lookupRepository.findByType(LookupType.VEHICLE_SIZE);

        return VehicleBootStrapResponseDTO.builder()
                        .sizes(vehicleSizes.stream().map(vehicleSize -> VehicleSizeDTO.builder()
                                .name(vehicleSize.getName())
                                .description(vehicleSize.getDescription())
                                .build()).collect(Collectors.toList()))
                        .models(vehicleModels.stream().map(vehicleModel -> VehicleModelDTO.builder()
                                .name(vehicleModel.getName())
                                .description(vehicleModel.getDescription())
                                .build()).collect(Collectors.toList()))
                        .fuelTypes(fuelTypes.stream().map(fuelType -> VehicleFuelTypeDTO.builder()
                                .name(fuelType.getName())
                                .description(fuelType.getDescription())
                                .build()).collect(Collectors.toList()))
                        .build();
    }

    @Override
    public List<String> getElectricitySuppliersData() {
        List<Lookup> electricityProviders = lookupRepository.findByType(LookupType.ELECTRICITY_PROVIDER);
        CompanyProfile profile = companyProfileService.getCompanyProfile();

        return electricityProviders.stream()
                .filter(lookup -> profile.getCountry().equalsIgnoreCase(lookup.getCountry().getCountry()))
                .map(Lookup::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getNaturalGasSuppliersData() {
        List<Lookup> naturalGasProviders = lookupRepository.findByType(LookupType.NATURAL_GAS_PROVIDER);
        CompanyProfile profile = companyProfileService.getCompanyProfile();

        return naturalGasProviders.stream()
                .filter(lookup -> profile.getCountry().equalsIgnoreCase(lookup.getCountry().getCountry()))
                .map(Lookup::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getWaterSupplySuppliersData() {
        List<Lookup> waterSupplyProviders = lookupRepository.findByType(LookupType.WATER_SUPPLY_PROVIDER);
        CompanyProfile profile = companyProfileService.getCompanyProfile();

        return waterSupplyProviders.stream()
                .filter(lookup -> profile.getCountry().equalsIgnoreCase(lookup.getCountry().getCountry()))
                .map(Lookup::getName)
                .collect(Collectors.toList());
    }

    @Override
    public RefrigerantBootStrapResponseDTO getRefrigerantBootStrapData() {
        List<Lookup> gwps = lookupRepository.findByType(LookupType.GWP);
        List<Lookup> appliances = lookupRepository.findByType(LookupType.REFRIGERANT_APPLIANCE);

        return RefrigerantBootStrapResponseDTO.builder()
                .appliances(appliances.stream().map(Lookup::getName).collect(Collectors.toList()))
                .refrigerantTypes(gwps.stream().map(Lookup::getName).collect(Collectors.toList()))
                .build();
    }

    @Override
    public EmployeeCommuteAndBusinessTravelBootstrapResponseDTO getEmployeeCommuteData() {

        CompanyProfile profile = companyProfileService.getCompanyProfile();

        List<Lookup> carfuelTypes = lookupRepository.findByType(LookupType.FUEL_TYPE);
        List<Lookup> carSizes = lookupRepository.findByTypeAndVehicleType(LookupType.VEHICLE_SIZE, VehicleType.CAR);

        List<Lookup> motorbikeSizes = lookupRepository.findByTypeAndVehicleType(LookupType.VEHICLE_SIZE, VehicleType.MOTORBIKE);

        List<Lookup> taxiSizes = lookupRepository.findByTypeAndVehicleType(LookupType.VEHICLE_SIZE, VehicleType.TAXI);

        List<Lookup> busSizes = lookupRepository.findByTypeAndVehicleType(LookupType.VEHICLE_SIZE, VehicleType.BUS);

        List<Lookup> railSizes = lookupRepository.findByTypeAndVehicleType(LookupType.VEHICLE_SIZE, VehicleType.RAIL);

        List<Lookup> flightSizes = lookupRepository.findByTypeAndVehicleType(LookupType.VEHICLE_SIZE, VehicleType.FLIGHT);

        // Create a map to store grouped results
        Map<String, List<String>> groupedResultsForFlights = new HashMap<>();

        // Grouping by description
        for (Lookup lookup : flightSizes) {
            String description = lookup.getDescription();
            String name = lookup.getName();

            if (lookup.getCountry() == null || profile.getCountry().equalsIgnoreCase(lookup.getCountry().getCountry())) {
                groupedResultsForFlights.putIfAbsent(description, new ArrayList<>());

                groupedResultsForFlights.get(description).add(name);
            }
        }

        HashMap<String, List<CarCommuteResponseDTO>> carCommuteDTO = new HashMap<>();
        carCommuteDTO.put("size", carSizes.stream().map(size -> CarCommuteResponseDTO.builder().name(size.getName()).description(size.getDescription()).build()).collect(Collectors.toList()));
        carCommuteDTO.put("fuel_types", carfuelTypes.stream().map(fuel -> CarCommuteResponseDTO.builder().name(fuel.getName()).description(fuel.getDescription()).build()).collect(Collectors.toList()));

        return EmployeeCommuteAndBusinessTravelBootstrapResponseDTO.builder()
                .carCommuteDetails(carCommuteDTO)
                .motorBikeCommuteDetails(motorbikeSizes.stream().map(motorbike -> MotorBikeCommuteResponseDTO.builder()
                        .name(motorbike.getName())
                        .description(motorbike.getDescription())
                        .build()).collect(Collectors.toList()))
                .busCommuteDetails(
                        busSizes.stream()
                                .filter(lookup -> lookup.getCountry() == null || profile.getCountry().equalsIgnoreCase(lookup.getCountry().getCountry()))
                                .map(bus -> BusCommuteResponseDTO.builder()
                                        .name(bus.getName())
                                        .description(bus.getDescription())
                                        .build()).collect(Collectors.toList()))
                .taxiCommuteDetails(
                        taxiSizes.stream()
                                .filter(lookup -> lookup.getCountry() == null || profile.getCountry().equalsIgnoreCase(lookup.getCountry().getCountry()))
                                .map(taxi -> TaxiCommuteResponseDTO.builder()
                                        .name(taxi.getName())
                                        .description(taxi.getDescription())
                                        .build()).collect(Collectors.toList()))
                .railCommuteDetails(
                        railSizes.stream()
                                .filter(lookup -> lookup.getCountry() == null || profile.getCountry().equalsIgnoreCase(lookup.getCountry().getCountry()))
                                .map(rail -> RailCommuteResponseDTO.builder()
                                        .name(rail.getName())
                                        .description(rail.getDescription())
                                        .build()).collect(Collectors.toList()))
                .flightCommuteDetails(FlightCommuteResponseDTO.builder()
                        .flightCommute(groupedResultsForFlights)
                        .build())
                .build();
    }

    @Override
    public WasteBootstrapResponseDTO getWasteCategories() {
        List<Lookup> waterSupplyProviders = lookupRepository.findByType(LookupType.WASTE_TREATMENT);

        Set<String> wasteCategories =  waterSupplyProviders.stream()
                .map(Lookup::getName)
                .collect(Collectors.toSet());

        Set<String> treatmentMethods =  waterSupplyProviders.stream()
                .map(Lookup::getDescription)
                .collect(Collectors.toSet());

        return WasteBootstrapResponseDTO.builder()
                .wasteCategories(wasteCategories)
                .treatmentMethods(treatmentMethods)
                .build();
    }
}
