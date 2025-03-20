package com.effix.api.service.impl;

import com.effix.api.dto.request.watersupply.CreateWaterSupplyMeterRequestDTO;
import com.effix.api.dto.response.watersupply.WaterSupplyEmissionDTO;
import com.effix.api.dto.response.watersupply.WaterSupplyEmissionDetailResponseDTO;
import com.effix.api.dto.response.watersupply.WaterSupplyMeterResponseDTO;
import com.effix.api.enums.Country;
import com.effix.api.enums.EmissionFactorType;
import com.effix.api.enums.LookupType;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.*;
import com.effix.api.repository.*;
import com.effix.api.service.CompanyProfileService;
import com.effix.api.service.WaterSupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WaterSupplyServiceImpl implements WaterSupplyService {
    @Autowired
    private WaterSupplyEmissionRepository waterSupplyEmissionRepository;

    @Autowired
    private EmissionRepository emissionRepository;
    @Autowired
    private EmissionFactorRepository emissionFactorRepository;

    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private CompanyProfileService companyProfileService;
    @Autowired
    private WaterSupplyMeterRepository waterSupplyMeterRepository;

    @Override
    public WaterSupplyMeter createWaterSupplyMeter(CreateWaterSupplyMeterRequestDTO createWaterSupplyMeterRequestDTO) {
        WaterSupplyMeter waterSupplyMeter = WaterSupplyMeter.builder()
                .meterId(createWaterSupplyMeterRequestDTO.getMeterId())
                .supplier(createWaterSupplyMeterRequestDTO.getSupplier())
                .companyProfile(companyProfileService.getCompanyProfile())
                .build();
        return waterSupplyMeterRepository.save(waterSupplyMeter);
    }

    @Override
    public void addMeterUsage(Long id, LocalDateTime updatedTime, float value) throws BadRequestException {
        Optional<WaterSupplyMeter> waterSupplyMeterOptional = waterSupplyMeterRepository.findById(id);
        if (waterSupplyMeterOptional.isEmpty()) {
            throw new BadRequestException("Water supply meter not found");
        }
        WaterSupplyMeter waterSupplyMeter = waterSupplyMeterOptional.get();
        List<WaterSupplyEmission> usages = waterSupplyMeter.getWaterSupplyEmissions();

        if (usages.isEmpty()) {
            WaterSupplyEmission emission = WaterSupplyEmission.builder()
                    .meter(waterSupplyMeter)
                    .waterSupplyReading(value)
                    .createdAt(updatedTime)
                    .updatedAt(updatedTime)
                    .build();
            waterSupplyEmissionRepository.save(emission); // add usage for first time
        } else {
            usages.sort(Comparator.comparing(WaterSupplyEmission::getCreatedAt).reversed());
            WaterSupplyEmission latestMeterReading = usages.get(0);
            float valueOfLatestEntry = latestMeterReading.getWaterSupplyReading();
            double differenceInMeterReading = value - valueOfLatestEntry;
            if (differenceInMeterReading <= 0) {
                throw new BadRequestException("Current Meter reading should be greater than last reading");
            }
            Emissions naturalGasEmissions = calculateWaterSupplyEmissions(waterSupplyMeter, differenceInMeterReading);

            Emissions emissionsSaved = emissionRepository.save(naturalGasEmissions);

            WaterSupplyEmission emission = WaterSupplyEmission.builder()
                    .meter(waterSupplyMeter)
                    .emissions(emissionsSaved)
                    .waterSupplyReading(value)
                    .createdAt(updatedTime)
                    .updatedAt(updatedTime)
                    .build();
            waterSupplyEmissionRepository.save(emission); // save meter reading after first time onwards
        }
    }

    @Override
    public WaterSupplyEmissionDetailResponseDTO getWaterSupplyMeterDetail(Long id) throws BadRequestException {
        Optional<WaterSupplyMeter> waterSupplyMeterOptional = waterSupplyMeterRepository.findById(id);
        if (waterSupplyMeterOptional.isEmpty()) {
            throw new BadRequestException("Water supply meter not found");
        }
        WaterSupplyMeter waterSupplyMeter = waterSupplyMeterOptional.get();
        List<WaterSupplyEmission> waterSupplyEmissions = waterSupplyMeter.getWaterSupplyEmissions();

        WaterSupplyEmissionDetailResponseDTO waterSupplyEmissionDetailResponseDTO = WaterSupplyEmissionDetailResponseDTO.builder()
                .id(waterSupplyMeter.getId())
                .meterId(waterSupplyMeter.getMeterId())
                .supplier(waterSupplyMeter.getSupplier())
                .build();

        List<WaterSupplyEmissionDTO> emissions = waterSupplyEmissions
                .stream().sorted(Comparator.comparing(WaterSupplyEmission::getCreatedAt).reversed())
                .map(meter -> {
                    WaterSupplyEmissionDTO waterSupplyEmissionDTO = WaterSupplyEmissionDTO.builder()
                            .date(meter.getCreatedAt())
                            .meterReading(meter.getWaterSupplyReading())
                            .build();
                    if (meter.getEmissions() != null) {
                        waterSupplyEmissionDTO.setCo2Emissions(meter.getEmissions().getCo2Emissions());
                        waterSupplyEmissionDTO.setCh4Emissions(meter.getEmissions().getCh4Emissions());
                        waterSupplyEmissionDTO.setN2oEmissions(meter.getEmissions().getN2oEmissions());
                        waterSupplyEmissionDTO.setTotalEmissions(meter.getEmissions().getTotalEmissions());
                    }
                    return waterSupplyEmissionDTO;
                })
                .collect(Collectors.toList());
        waterSupplyEmissionDetailResponseDTO.setEmissions(emissions);
        return waterSupplyEmissionDetailResponseDTO;
    }

    @Override
    public List<WaterSupplyMeterResponseDTO> getAllWaterSupplyMeters() {
        return waterSupplyMeterRepository.findByCompanyProfile(companyProfileService.getCompanyProfile())
                .stream()
                .map(meter -> WaterSupplyMeterResponseDTO.builder()
                        .id(meter.getId())
                        .meterId(meter.getMeterId())
                        .supplier(meter.getSupplier())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWaterSupplyMeter(Long id) {
        waterSupplyMeterRepository.deleteById(id);
    }

    private Emissions calculateWaterSupplyEmissions(WaterSupplyMeter meter, double meterReading) {
        Lookup lookup = lookupRepository.findByTypeAndNameAndCountry(LookupType.WATER_SUPPLY_PROVIDER, meter.getSupplier(), Country.valueOf(meter.getCompanyProfile().getCountry().toUpperCase()));
        EmissionFactor emissionFactor = emissionFactorRepository.findByCategoryAndSubCategoryAndUnit(EmissionFactorType.WATER_SUPPLY, lookup, "kgCO2/m3");
        Double co2Emissions = emissionFactor.getTotalEmissionFactor() * meterReading;
        Double cH4Emissions = emissionFactor.getTotalEmissionFactor() * meterReading * 25;
        Double n20Emissions = emissionFactor.getTotalEmissionFactor() * meterReading * 298;

        return Emissions.builder()
                .co2Emissions(co2Emissions)
                .ch4Emissions(cH4Emissions)
                .n2oEmissions(n20Emissions)
                .totalEmissions(co2Emissions + cH4Emissions + n20Emissions)
                .build();
    }
}
