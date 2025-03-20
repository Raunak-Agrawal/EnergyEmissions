package com.effix.api.service.impl;

import com.effix.api.dto.request.electricity.CreateElectricityMeterRequestDTO;
import com.effix.api.dto.response.electricity.ElectricityEmissionDTO;
import com.effix.api.dto.response.electricity.ElectricityEmissionDetailResponseDTO;
import com.effix.api.dto.response.electricity.ElectricityMeterResponseDTO;
import com.effix.api.enums.Country;
import com.effix.api.enums.EmissionFactorType;
import com.effix.api.enums.LookupType;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.*;
import com.effix.api.repository.*;
import com.effix.api.service.CompanyProfileService;
import com.effix.api.service.ElectricityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElectricityServiceImpl implements ElectricityService {
    @Autowired
    private ElectricityEmissionRepository electricityEmissionRepository;

    @Autowired
    private EmissionRepository emissionRepository;
    @Autowired
    private EmissionFactorRepository emissionFactorRepository;

    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private CompanyProfileService companyProfileService;
    @Autowired
    private ElectricityMeterRepository electricityMeterRepository;

    @Override
    public ElectricityMeter createElectricityMeter(CreateElectricityMeterRequestDTO createElectricityMeterRequestDTO) {
        ElectricityMeter electricityMeter = ElectricityMeter.builder()
                .meterId(createElectricityMeterRequestDTO.getMeterId())
                .supplier(createElectricityMeterRequestDTO.getSupplier())
                .companyProfile(companyProfileService.getCompanyProfile())
                .hasContractualInstruments(createElectricityMeterRequestDTO.getHasContractualInstruments() != null ? createElectricityMeterRequestDTO.getHasContractualInstruments() : false)
                .build();
        return electricityMeterRepository.save(electricityMeter);
    }

    @Override
    public void addMeterUsage(Long id, LocalDateTime updatedTime, float value) throws BadRequestException {
        Optional<ElectricityMeter> electricityMeterOptional = electricityMeterRepository.findById(id);
        if (electricityMeterOptional.isEmpty()) {
            throw new BadRequestException("Electricity meter not found");
        }
        ElectricityMeter electricityMeter = electricityMeterOptional.get();
        List<ElectricityEmission> usages = electricityMeter.getElectricityEmissions();

        if (usages.isEmpty()) {
            ElectricityEmission emission = ElectricityEmission.builder()
                    .meter(electricityMeter)
                    .electricityReading(value)
                    .createdAt(updatedTime)
                    .updatedAt(updatedTime)
                    .build();
            electricityEmissionRepository.save(emission); // add usage for first time
        } else {
            usages.sort(Comparator.comparing(ElectricityEmission::getCreatedAt).reversed());
            ElectricityEmission latestMeterReading = usages.get(0);
            float valueOfLatestEntry = latestMeterReading.getElectricityReading();
            double differenceInMeterReading = value - valueOfLatestEntry;
            if (differenceInMeterReading <= 0) {
                throw new BadRequestException("Current Meter reading should be greater than last reading");
            }
            Emissions electricityEmissions = calculateElectricityEmissions(electricityMeter, differenceInMeterReading);

            Emissions emissionsSaved = emissionRepository.save(electricityEmissions);

            ElectricityEmission emission = ElectricityEmission.builder()
                    .meter(electricityMeter)
                    .emissions(emissionsSaved)
                    .electricityReading(value)
                    .createdAt(updatedTime)
                    .updatedAt(updatedTime)
                    .build();
            electricityEmissionRepository.save(emission); // save meter reading after first time onwards
        }
    }

    @Override
    public ElectricityEmissionDetailResponseDTO getElectricityMeterDetail(Long id) throws BadRequestException {
        Optional<ElectricityMeter> electricityMeterOptional = electricityMeterRepository.findById(id);
        if (electricityMeterOptional.isEmpty()) {
            throw new BadRequestException("Electricity meter not found");
        }
        ElectricityMeter electricityMeter = electricityMeterOptional.get();
        List<ElectricityEmission> electricityEmissions = electricityMeter.getElectricityEmissions();

        ElectricityEmissionDetailResponseDTO electricityEmissionDetailResponseDTO = ElectricityEmissionDetailResponseDTO.builder()
                .id(electricityMeter.getId())
                .meterId(electricityMeter.getMeterId())
                .supplier(electricityMeter.getSupplier())
                .hasContractualInstruments(electricityMeter.isHasContractualInstruments())
                .build();

        List<ElectricityEmissionDTO> emissions = electricityEmissions
                .stream().sorted(Comparator.comparing(ElectricityEmission::getCreatedAt).reversed())
                .map(meter -> {
                    ElectricityEmissionDTO electricityEmissionDTO = ElectricityEmissionDTO.builder()
                            .date(meter.getCreatedAt())
                            .meterReading(meter.getElectricityReading())
                            .build();
                    if (meter.getEmissions() != null) {
                        electricityEmissionDTO.setCo2Emissions(meter.getEmissions().getCo2Emissions());
                        electricityEmissionDTO.setCh4Emissions(meter.getEmissions().getCh4Emissions());
                        electricityEmissionDTO.setN2oEmissions(meter.getEmissions().getN2oEmissions());
                        electricityEmissionDTO.setTotalEmissions(meter.getEmissions().getTotalEmissions());
                    }
                    return electricityEmissionDTO;
                })
                .collect(Collectors.toList());
        electricityEmissionDetailResponseDTO.setEmissions(emissions);
        return electricityEmissionDetailResponseDTO;
    }

    @Override
    public List<ElectricityMeterResponseDTO> getAllElectricityMeters() {
        return electricityMeterRepository.findByCompanyProfile(companyProfileService.getCompanyProfile())
                .stream()
                .map(meter -> ElectricityMeterResponseDTO.builder()
                        .id(meter.getId())
                        .meterId(meter.getMeterId())
                        .supplier(meter.getSupplier())
                        .hasContractualInstruments(meter.isHasContractualInstruments())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteElectricityMeter(Long id) {
        electricityMeterRepository.deleteById(id);
    }

    private Emissions calculateElectricityEmissions(ElectricityMeter meter, double meterReading) {
        Lookup lookup = lookupRepository.findByTypeAndNameAndCountry(LookupType.ELECTRICITY_PROVIDER, meter.getSupplier(), Country.valueOf(meter.getCompanyProfile().getCountry().toUpperCase()));
        EmissionFactor emissionFactor = emissionFactorRepository.findByCategoryAndSubCategoryAndUnit(EmissionFactorType.ELECTRICITY, lookup, "kgCO2/kWh");
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

