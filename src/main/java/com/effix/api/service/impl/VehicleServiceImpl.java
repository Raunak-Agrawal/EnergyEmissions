package com.effix.api.service.impl;

import com.effix.api.dto.request.vehicle.CreateVehicleRequestDTO;
import com.effix.api.dto.request.vehicle.VehicleUsageRequestDTO;
import com.effix.api.dto.response.vehicle.*;
import com.effix.api.enums.*;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.*;
import com.effix.api.repository.*;
import com.effix.api.service.CompanyProfileService;
import com.effix.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    EmissionRepository emissionRepository;

    @Autowired
    LookupRepository lookupRepository;

    @Autowired
    EmissionFactorRepository emissionFactorRepository;

    @Autowired
    VehicleUsageRepository vehicleUsageRepository;

    @Autowired
    CompanyProfileService companyProfileService;

    @Override
    public List<VehicleDTO> getVehicles() {
        return vehicleRepository.findByCompanyProfile(companyProfileService.getCompanyProfile())
                .stream()
                .map(vehicle -> VehicleDTO.builder()
                        .id(vehicle.getId())
                        .model(VehicleModelDTO.builder().name(vehicle.getModel()).build())
                        .size(VehicleSizeDTO.builder().name(vehicle.getSizeCategory().name()).build())
                        .fuelType(VehicleFuelTypeDTO.builder().name(vehicle.getFuelType().name()).build())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDetailResponseDTO getVehicleDetail(Long vehicleId) throws BadRequestException {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            throw new BadRequestException("Vehicle not found");
        }
        Vehicle vehicle = vehicleOptional.get();
        List<VehicleUsage> usages = vehicle.getUsages();

        VehicleDetailResponseDTO vehicleDetailResponseDTO = VehicleDetailResponseDTO.builder()
                .model(VehicleModelDTO.builder().name(vehicle.getModel()).build())
                .size(VehicleSizeDTO.builder().name(vehicle.getSizeCategory().name()).build())
                .fuelType(VehicleFuelTypeDTO.builder().name(vehicle.getFuelType().name()).build())
                .build();
        List<VehicleEmissionDTO> emissions = usages
                .stream().sorted(Comparator.comparing(VehicleUsage::getCreatedAt).reversed())
                .map(vehicleUsage -> {
                    VehicleEmissionDTO vehicleEmissionDTO = VehicleEmissionDTO.builder()
                            .date(vehicleUsage.getCreatedAt())
                            .kmReading(vehicleUsage.getKmReading())
                            .build();
                    if (vehicleUsage.getEmissions() != null) {
                        vehicleEmissionDTO.setCo2Emissions(vehicleUsage.getEmissions().getCo2Emissions());
                        vehicleEmissionDTO.setCh4Emissions(vehicleUsage.getEmissions().getCh4Emissions());
                        vehicleEmissionDTO.setN2oEmissions(vehicleUsage.getEmissions().getN2oEmissions());
                        vehicleEmissionDTO.setTotalEmissions(vehicleUsage.getEmissions().getTotalEmissions());
                    }
                    return vehicleEmissionDTO;
                })
                .collect(Collectors.toList());
        vehicleDetailResponseDTO.setEmissions(emissions);
        return vehicleDetailResponseDTO;
    }

    @Override
    public Vehicle createVehicle(CreateVehicleRequestDTO createVehicleRequestDTO) {
        Vehicle vehicle = Vehicle.builder()
                .fuelType(FuelType.valueOf(createVehicleRequestDTO.getFuelType()))
                .sizeCategory(SizeCategory.valueOf(createVehicleRequestDTO.getVehicleSize()))
                .model(createVehicleRequestDTO.getVehicleModel())
                .companyProfile(companyProfileService.getCompanyProfile())
                .build();

        if (createVehicleRequestDTO.getVehicleUsage() != null && createVehicleRequestDTO.getVehicleUsage().getKmReading() != null) {
            VehicleUsage vehicleUsage = VehicleUsage.builder()
                    .kmReading(createVehicleRequestDTO.getVehicleUsage().getKmReading())
                    .vehicle(vehicle)
                    .build();
            vehicleUsageRepository.save(vehicleUsage);
        }

        return vehicleRepository.save(vehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    @Override
    public VehicleUsage addVehicleUsage(Long vehicleId, VehicleUsageRequestDTO vehicleUsageRequestDTO) throws BadRequestException {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            throw new BadRequestException("Vehicle not found");
        }
        Vehicle vehicle = vehicleOptional.get();
        Long vehicleCompanyProfileId = vehicle.getCompanyProfile().getId();
        Long loggedInUserCompanyProfileId = companyProfileService.getCompanyProfile().getId();

        if(!Objects.equals(vehicleCompanyProfileId, loggedInUserCompanyProfileId)){
            throw new BadRequestException("Vehicle not associated with the company");
        }

        List<VehicleUsage> usages = vehicle.getUsages();
        if (usages.isEmpty()) {
            VehicleUsage vehicleUsage = VehicleUsage.builder()
                    .kmReading(vehicleUsageRequestDTO.getKmReading())
                    .vehicle(vehicle)
                    .build();
            return vehicleUsageRepository.save(vehicleUsage); // add usage for first time
        } else {
            usages.sort(Comparator.comparing(VehicleUsage::getCreatedAt).reversed());
            VehicleUsage latestVehicleUsageEntry = usages.get(0);
            Double kmReadingOfLatestEntry = latestVehicleUsageEntry.getKmReading();

            double differenceInKmReading = vehicleUsageRequestDTO.getKmReading() - kmReadingOfLatestEntry;
            if (differenceInKmReading <= 0) {
                throw new BadRequestException("Current Km reading should be greater than last reading");
            }

            Emissions vehicleEmissions = calculateVehicleEmissions(vehicle, differenceInKmReading);
            Emissions emissionsSaved = emissionRepository.save(vehicleEmissions);

            VehicleUsage vehicleUsage = VehicleUsage.builder()
                    .emissions(emissionsSaved)
                    .kmReading(vehicleUsageRequestDTO.getKmReading())
                    .vehicle(vehicle)
                    .build();
            return vehicleUsageRepository.save(vehicleUsage); // save vehicle usage after first time onwards
        }
    }

    private Emissions calculateVehicleEmissions(Vehicle vehicle, double kmReading) {
        Lookup lookup = lookupRepository.findByTypeAndNameAndVehicleType(LookupType.VEHICLE_SIZE, vehicle.getSizeCategory().name(), VehicleType.CAR);
        EmissionFactor emissionFactor = emissionFactorRepository.findByCategoryAndSubCategoryAndUnit(EmissionFactorType.valueOf(vehicle.getFuelType().name()), lookup , "km");
        Double co2Emissions = emissionFactor.getCO2EmissionFactor() * kmReading;
        Double cH4Emissions = emissionFactor.getCH4EmissionFactor() * kmReading;
        Double n20Emissions = emissionFactor.getN2OEmissionFactor() * kmReading;

        return Emissions.builder()
                .co2Emissions(co2Emissions)
                .ch4Emissions(cH4Emissions)
                .n2oEmissions(n20Emissions)
                .totalEmissions(co2Emissions + cH4Emissions + n20Emissions)
                .build();
    }
}
