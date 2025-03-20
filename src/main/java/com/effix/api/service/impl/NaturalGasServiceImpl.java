package com.effix.api.service.impl;
import com.effix.api.dto.request.naturalgas.CreateNaturalGasMeterRequestDTO;
import com.effix.api.dto.response.naturalgas.NaturalGasEmissionDTO;
import com.effix.api.dto.response.naturalgas.NaturalGasEmissionDetailResponseDTO;
import com.effix.api.dto.response.naturalgas.NaturalGasMeterResponseDTO;
import com.effix.api.enums.Country;
import com.effix.api.enums.EmissionFactorType;
import com.effix.api.enums.LookupType;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.*;
import com.effix.api.repository.*;
import com.effix.api.service.CompanyProfileService;
import com.effix.api.service.NaturalGasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NaturalGasServiceImpl implements NaturalGasService {
    @Autowired
    private NaturalGasEmissionRepository naturalGasEmissionRepository;

    @Autowired
    private EmissionRepository emissionRepository;
    @Autowired
    private EmissionFactorRepository emissionFactorRepository;

    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private CompanyProfileService companyProfileService;
    @Autowired
    private NaturalGasMeterRepository naturalGasMeterRepository;

    @Override
    public NaturalGasMeter createNaturalGasMeter(CreateNaturalGasMeterRequestDTO createNaturalGasMeterRequestDTO) {
        NaturalGasMeter naturalGasMeter = NaturalGasMeter.builder()
                .meterId(createNaturalGasMeterRequestDTO.getMeterId())
                .supplier(createNaturalGasMeterRequestDTO.getSupplier())
                .companyProfile(companyProfileService.getCompanyProfile())
                .build();
        return naturalGasMeterRepository.save(naturalGasMeter);
    }

    @Override
    public void addMeterUsage(Long id, LocalDateTime updatedTime, float value) throws BadRequestException {
        Optional<NaturalGasMeter> naturalGasMeterOptional = naturalGasMeterRepository.findById(id);
        if (naturalGasMeterOptional.isEmpty()) {
            throw new BadRequestException("Natural gas meter not found");
        }
        NaturalGasMeter naturalGasMeter = naturalGasMeterOptional.get();
        List<NaturalGasEmission> usages = naturalGasMeter.getNaturalGasEmissions();

        if (usages.isEmpty()) {
            NaturalGasEmission emission = NaturalGasEmission.builder()
                    .meter(naturalGasMeter)
                    .naturalGasReading(value)
                    .createdAt(updatedTime)
                    .updatedAt(updatedTime)
                    .build();
            naturalGasEmissionRepository.save(emission); // add usage for first time
        } else {
            usages.sort(Comparator.comparing(NaturalGasEmission::getCreatedAt).reversed());
            NaturalGasEmission latestMeterReading = usages.get(0);
            float valueOfLatestEntry = latestMeterReading.getNaturalGasReading();
            double differenceInMeterReading = value - valueOfLatestEntry;
            if (differenceInMeterReading <= 0) {
                throw new BadRequestException("Current Meter reading should be greater than last reading");
            }
            Emissions naturalGasEmissions = calculateNaturalGasEmissions(naturalGasMeter, differenceInMeterReading);

            Emissions emissionsSaved = emissionRepository.save(naturalGasEmissions);

            NaturalGasEmission emission = NaturalGasEmission.builder()
                    .meter(naturalGasMeter)
                    .emissions(emissionsSaved)
                    .naturalGasReading(value)
                    .createdAt(updatedTime)
                    .updatedAt(updatedTime)
                    .build();
            naturalGasEmissionRepository.save(emission); // save meter reading after first time onwards
        }
    }

    @Override
    public NaturalGasEmissionDetailResponseDTO getNaturalGasMeterDetail(Long id) throws BadRequestException {
        Optional<NaturalGasMeter> naturalGasMeterOptional = naturalGasMeterRepository.findById(id);
        if (naturalGasMeterOptional.isEmpty()) {
            throw new BadRequestException("Natural gas meter not found");
        }
        NaturalGasMeter naturalGasMeter = naturalGasMeterOptional.get();
        List<NaturalGasEmission> naturalGasEmissions = naturalGasMeter.getNaturalGasEmissions();

        NaturalGasEmissionDetailResponseDTO naturalGasEmissionDetailResponseDTO = NaturalGasEmissionDetailResponseDTO.builder()
                .id(naturalGasMeter.getId())
                .meterId(naturalGasMeter.getMeterId())
                .supplier(naturalGasMeter.getSupplier())
                .build();

        List<NaturalGasEmissionDTO> emissions = naturalGasEmissions
                .stream().sorted(Comparator.comparing(NaturalGasEmission::getCreatedAt).reversed())
                .map(meter -> {
                    NaturalGasEmissionDTO naturalGasEmissionDTO = NaturalGasEmissionDTO.builder()
                            .date(meter.getCreatedAt())
                            .meterReading(meter.getNaturalGasReading())
                            .build();
                    if (meter.getEmissions() != null) {
                        naturalGasEmissionDTO.setCo2Emissions(meter.getEmissions().getCo2Emissions());
                        naturalGasEmissionDTO.setCh4Emissions(meter.getEmissions().getCh4Emissions());
                        naturalGasEmissionDTO.setN2oEmissions(meter.getEmissions().getN2oEmissions());
                        naturalGasEmissionDTO.setTotalEmissions(meter.getEmissions().getTotalEmissions());
                    }
                    return naturalGasEmissionDTO;
                })
                .collect(Collectors.toList());
        naturalGasEmissionDetailResponseDTO.setEmissions(emissions);
        return naturalGasEmissionDetailResponseDTO;
    }

    @Override
    public List<NaturalGasMeterResponseDTO> getAllNaturalGasMeters() {
        return naturalGasMeterRepository.findByCompanyProfile(companyProfileService.getCompanyProfile())
                .stream()
                .map(meter -> NaturalGasMeterResponseDTO.builder()
                        .id(meter.getId())
                        .meterId(meter.getMeterId())
                        .supplier(meter.getSupplier())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteNaturalGasMeter(Long id) {
        naturalGasMeterRepository.deleteById(id);
    }

    private Emissions calculateNaturalGasEmissions(NaturalGasMeter meter, double meterReading) {
        Lookup lookup = lookupRepository.findByTypeAndNameAndCountry(LookupType.NATURAL_GAS_PROVIDER, meter.getSupplier(), Country.valueOf(meter.getCompanyProfile().getCountry().toUpperCase()));
        EmissionFactor emissionFactor = emissionFactorRepository.findByCategoryAndSubCategoryAndUnit(EmissionFactorType.NATURAL_GAS, lookup, "kgCO2/m3");
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

