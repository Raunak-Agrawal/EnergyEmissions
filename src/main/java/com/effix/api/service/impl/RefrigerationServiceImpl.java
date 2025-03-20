package com.effix.api.service.impl;

import com.effix.api.dto.request.refrigerant.CreateRefrigerantRequestDTO;
import com.effix.api.dto.response.refrigerant.RefrigerantEmissionDTO;
import com.effix.api.dto.response.refrigerant.RefrigerantEmissionDetailResponseDTO;
import com.effix.api.dto.response.refrigerant.RefrigerantResponseDTO;
import com.effix.api.enums.Country;
import com.effix.api.enums.EmissionFactorType;
import com.effix.api.enums.LookupType;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.*;
import com.effix.api.repository.*;
import com.effix.api.service.CompanyProfileService;
import com.effix.api.service.RefrigerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RefrigerationServiceImpl implements RefrigerationService {

    @Autowired
    private RefrigerantEmissionRepository refrigerantEmissionRepository;

    @Autowired
    private EmissionRepository emissionRepository;
    @Autowired
    private EmissionFactorRepository emissionFactorRepository;

    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private CompanyProfileService companyProfileService;
    @Autowired
    private RefrigerantRepository refrigerantRepository;

    @Override
    public Refrigerant createRefrigerationRecord(CreateRefrigerantRequestDTO refrigeration) {
        Refrigerant refrigerant = Refrigerant.builder()
                .refrigerantCapacity(refrigeration.getRefrigerantCapacity())
                .refrigerantType(refrigeration.getRefrigerantType())
                .appliance(refrigeration.getAppliance())
                .name(refrigeration.getRefrigerantDisplayName())
                .isCharged(refrigeration.getIsCharged() != null ? refrigeration.getIsCharged() : false)
                .isDisposed(refrigeration.getIsDisposed() != null ? refrigeration.getIsDisposed() : false)
                .yearsOfUsage(refrigeration.getYearsOfUsage())
                .companyProfile(companyProfileService.getCompanyProfile())
                .build();

        Refrigerant refrigerant1 = refrigerantRepository.save(refrigerant);

        Emissions refrigerantEmissions = calculateRefrigerantEmissions(refrigerant1, refrigeration.getYearsOfUsage());

        Emissions emissionsSaved = emissionRepository.save(refrigerantEmissions);

        LocalDateTime currentTime = LocalDateTime.now();

        RefrigerantEmission refrigerantEmission = RefrigerantEmission.builder()
                .refrigerantYearsOfUsage((double) refrigerant1.getYearsOfUsage())
                .refrigerant(refrigerant1)
                .emissions(emissionsSaved)
                .updatedAt(currentTime)
                .createdAt(currentTime)
                .build();
        refrigerantEmissionRepository.save(refrigerantEmission);
        return refrigerant1;
    }

    @Override
    public RefrigerantEmissionDetailResponseDTO getRefrigerantDetail(Long id) throws BadRequestException {
        Optional<Refrigerant> refrigerantOptional = refrigerantRepository.findById(id);
        if (refrigerantOptional.isEmpty()) {
            throw new BadRequestException("Refrigerant not found");
        }
        Refrigerant refrigerant = refrigerantOptional.get();
        List<RefrigerantEmission> refrigerantEmissions = refrigerant.getRefrigerantEmissions();

        RefrigerantEmissionDetailResponseDTO refrigerantEmissionDetailResponseDTO = RefrigerantEmissionDetailResponseDTO.builder()
                .id(refrigerant.getId())
                .refrigerantCapacity(refrigerant.getRefrigerantCapacity())
                .refrigerantType(refrigerant.getRefrigerantType())
                .appliance(refrigerant.getAppliance())
                .refrigerantDisplayName(refrigerant.getName())
                .yearsOfUsage(refrigerant.getYearsOfUsage())
                .build();

        List<RefrigerantEmissionDTO> emissions = refrigerantEmissions
                .stream().sorted(Comparator.comparing(RefrigerantEmission::getCreatedAt).reversed())
                .map(refrigerantEmission -> {
                    RefrigerantEmissionDTO refrigerantEmissionDTO = RefrigerantEmissionDTO.builder()
                            .date(refrigerantEmission.getCreatedAt())
                            .yearsOfUsage(refrigerantEmission.getRefrigerantYearsOfUsage())
                            .build();
                    if (refrigerantEmission.getEmissions() != null) {
                        refrigerantEmissionDTO.setCo2Emissions(refrigerantEmission.getEmissions().getCo2Emissions());
                        refrigerantEmissionDTO.setCh4Emissions(refrigerantEmission.getEmissions().getCh4Emissions());
                        refrigerantEmissionDTO.setN2oEmissions(refrigerantEmission.getEmissions().getN2oEmissions());
                        refrigerantEmissionDTO.setTotalEmissions(refrigerantEmission.getEmissions().getTotalEmissions());
                    }
                    return refrigerantEmissionDTO;
                })
                .collect(Collectors.toList());
        refrigerantEmissionDetailResponseDTO.setEmissions(emissions);
        return refrigerantEmissionDetailResponseDTO;
    }

    @Override
    public List<RefrigerantResponseDTO> getAllRefrigerants() {
        return refrigerantRepository.findByCompanyProfile(companyProfileService.getCompanyProfile())
                .stream()
                .map(refrigeration -> RefrigerantResponseDTO.builder()
                        .id(refrigeration.getId())
                        .refrigerantCapacity(refrigeration.getRefrigerantCapacity())
                        .refrigerantType(refrigeration.getRefrigerantType())
                        .appliance(refrigeration.getAppliance())
                        .refrigerantDisplayName(refrigeration.getName())
                        .yearsOfUsage(refrigeration.getYearsOfUsage())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRefrigerant(Long id) {
        refrigerantRepository.deleteById(id);
    }

    @Override
    public void addRefrigerantUsage(Long id, LocalDateTime updatedTime, double newValue) throws BadRequestException {
        Optional<Refrigerant> refrigerantOptional = refrigerantRepository.findById(id);
        if (refrigerantOptional.isEmpty()) {
            throw new BadRequestException("Refrigerant not found");
        }
        Refrigerant refrigerant = refrigerantOptional.get();
        List<RefrigerantEmission> usages = refrigerant.getRefrigerantEmissions();

        if (usages.isEmpty()) {
            Emissions refrigerantEmissions = calculateRefrigerantEmissions(refrigerant, newValue);
            Emissions emissionsSaved = emissionRepository.save(refrigerantEmissions);
            RefrigerantEmission refrigerantEmission = RefrigerantEmission.builder()
                    .refrigerantYearsOfUsage(newValue)
                    .refrigerant(refrigerant)
                    .emissions(emissionsSaved)
                    .createdAt(updatedTime)
                    .updatedAt(updatedTime)
                    .build();
            refrigerantEmissionRepository.save(refrigerantEmission); // add usage for first time
        } else {
            usages.sort(Comparator.comparing(RefrigerantEmission::getCreatedAt).reversed());
            RefrigerantEmission latestReading = usages.get(0);
            double valueOfLatestEntry = latestReading.getRefrigerantYearsOfUsage();
            double differenceInReading = newValue - valueOfLatestEntry;
            if (differenceInReading <= 0) {
                throw new BadRequestException("Current years of usage should be greater than last reading");
            }

            Emissions refrigerantEmissions = calculateRefrigerantEmissions(refrigerant, newValue);
            Emissions emissionsSaved = emissionRepository.save(refrigerantEmissions);

            RefrigerantEmission refrigerantEmission = RefrigerantEmission.builder()
                    .refrigerantYearsOfUsage(newValue)
                    .refrigerant(refrigerant)
                    .emissions(emissionsSaved)
                    .createdAt(updatedTime)
                    .updatedAt(updatedTime)
                    .build();
            refrigerantEmissionRepository.save(refrigerantEmission);
        }
    }

    private Emissions calculateRefrigerantEmissions(Refrigerant refrigerant, double yearsOfUsage) {
        Lookup refrigerantDetails = lookupRepository.findByTypeAndName(LookupType.GWP, refrigerant.getRefrigerantType());
        Lookup applianceDetails = lookupRepository.findByTypeAndName(LookupType.REFRIGERANT_APPLIANCE, refrigerant.getAppliance());

        EmissionFactor refrigerantEmissionFactor = emissionFactorRepository.findByCategoryAndSubCategory(EmissionFactorType.REFRIGERANTS, refrigerantDetails);
        EmissionFactor applianceEmissionFactor = emissionFactorRepository.findByCategoryAndSubCategory(EmissionFactorType.REFRIGERANTS, applianceDetails);

        double finalVal = applyEquations(refrigerant, yearsOfUsage, applianceEmissionFactor);

        double kgCo2Value = finalVal * refrigerantEmissionFactor.getTotalEmissionFactor();

        Double co2Emissions = kgCo2Value;
        Double cH4Emissions = kgCo2Value * 28;
        Double n20Emissions = kgCo2Value * 265;

        return Emissions.builder()
                .co2Emissions(co2Emissions)
                .ch4Emissions(cH4Emissions)
                .n2oEmissions(n20Emissions)
                .totalEmissions(co2Emissions + cH4Emissions + n20Emissions)
                .build();
    }

    private static double applyEquations(Refrigerant refrigerant, double yearsOfUsage, EmissionFactor applianceEmissionFactor) {
        double equationone = refrigerant.getRefrigerantCapacity() * (applianceEmissionFactor.getCO2EmissionFactor() / 100);
        double equationtwo = refrigerant.getRefrigerantCapacity() * (applianceEmissionFactor.getCH4EmissionFactor() / 100) * yearsOfUsage;
        double equationthree = refrigerant.getRefrigerantCapacity() * (applianceEmissionFactor.getN2OEmissionFactor() / 100) * (1 - (applianceEmissionFactor.getTotalEmissionFactor() / 100));

        double finalVal = 0.0;
        finalVal+=equationtwo;

        if(refrigerant.isCharged()){
            finalVal+=equationone;
        }

        if(refrigerant.isDisposed()){
            finalVal+=equationthree;
        }
        return finalVal;
    }
}
