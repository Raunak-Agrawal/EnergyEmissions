package com.effix.api.service.impl;

import com.effix.api.dto.request.waste.CreateWasteRequestDTO;
import com.effix.api.dto.request.waste.TreatmentMethod;
import com.effix.api.dto.response.waste.WasteEmissionDetailResponseDTO;
import com.effix.api.dto.response.waste.WasteEmissionResponseDTO;
import com.effix.api.enums.EmissionFactorType;
import com.effix.api.enums.LookupType;
import com.effix.api.model.entity.*;
import com.effix.api.repository.*;
import com.effix.api.service.CompanyProfileService;
import com.effix.api.service.WasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.*;


@Service
public class WasteServiceImpl implements WasteService {
    @Autowired
    private WasteRepository wasteRepository;

    @Autowired
    private WasteEmissionRepository wasteEmissionRepository;

    @Autowired
    private CompanyProfileService companyProfileService;
    @Autowired
    private EmissionRepository emissionRepository;

    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private EmissionFactorRepository emissionFactorRepository;

    @Override
    public List<Waste> createWaste(CreateWasteRequestDTO wasteRequestDTO) {
        List<Waste> wasteList = new ArrayList<>();

        double sizeOfBag = wasteRequestDTO.getSizeOfBag() * 0.8; // 80% is filled is the assumption
        for (TreatmentMethod treatmentMethod : wasteRequestDTO.getTreatmentMethod()) {
            double weight = treatmentMethod.getPercent() * sizeOfBag;
            String method = treatmentMethod.getMethod();

            Emissions wasteEmissions = calculateWasteEmissions(wasteRequestDTO.getWasteCategory(), weight, method);

            Emissions emissionsSaved = null;
            if (wasteEmissions != null) emissionsSaved = emissionRepository.save(wasteEmissions);

            WasteEmission wasteEmission = WasteEmission.builder()
                    .emissions(emissionsSaved)
                    .build();
            wasteEmissionRepository.save(wasteEmission);

            Waste waste = Waste.builder()
                    .wasteCategory(wasteRequestDTO.getWasteCategory())
                    .disposalDate(wasteRequestDTO.getDisposalDate())
                    .companyProfile(companyProfileService.getCompanyProfile())
                    .weight(weight)
                    .treatmentMethod(method)
                    .wasteEmission(wasteEmission)
                    .build();
            Waste waste1 = wasteRepository.save(waste);
            wasteList.add(waste1);
        }

        return wasteList;
    }

    @Override
    public List<WasteEmissionResponseDTO> getAllWasteEmissions() {
        List<WasteEmissionResponseDTO> result = new ArrayList<>();

        Map<YearMonth, List<Waste>> dateToWasteMapping = getDateToWasteMapping();

        for (Map.Entry<YearMonth, List<Waste>> entry : dateToWasteMapping.entrySet()) {
            WasteEmissionResponseDTO wasteEmissionResponseDTO = WasteEmissionResponseDTO.builder().build();

            YearMonth date = entry.getKey();
            List<Waste> wasteList = entry.getValue();

            wasteEmissionResponseDTO.setDate(date);

            double co2Emissions = 0.0;
            double ch4Emissions = 0.0;
            double n2oEmissions = 0.0;
            for (Waste waste : wasteList) {
                Emissions emissions = waste.getWasteEmission().getEmissions();
                co2Emissions+=emissions.getCo2Emissions();
                ch4Emissions+=emissions.getCh4Emissions();
                n2oEmissions+=emissions.getN2oEmissions();
            }
            WasteEmissionDetailResponseDTO wasteEmissionDetailResponseDTO = WasteEmissionDetailResponseDTO.builder()
                    .ch4Emissions(ch4Emissions)
                    .co2Emissions(co2Emissions)
                    .n2oEmissions(n2oEmissions)
                    .totalEmissions(co2Emissions + ch4Emissions + n2oEmissions)
                    .build();
            wasteEmissionResponseDTO.setEmissions(wasteEmissionDetailResponseDTO);

            result.add(wasteEmissionResponseDTO);
        }
        result.sort(Comparator.comparing(WasteEmissionResponseDTO::getDate));
        return result;
    }

    private Emissions calculateWasteEmissions(String wasteCategory, double weight, String method) {
        Lookup lookup = lookupRepository.findByTypeAndNameAndDescription(LookupType.WASTE_TREATMENT, wasteCategory, method);
        EmissionFactor emissionFactor = emissionFactorRepository.findByCategoryAndSubCategoryAndUnit(EmissionFactorType.WASTE, lookup, "kgCO2/m3");
        Double co2Emissions = emissionFactor.getTotalEmissionFactor() * weight;
        Double cH4Emissions = emissionFactor.getTotalEmissionFactor() * weight * 25;
        Double n20Emissions = emissionFactor.getTotalEmissionFactor() * weight * 298;

        return Emissions.builder()
                .co2Emissions(co2Emissions)
                .ch4Emissions(cH4Emissions)
                .n2oEmissions(n20Emissions)
                .totalEmissions(co2Emissions + cH4Emissions + n20Emissions)
                .build();
    }

    private Map<YearMonth, List<Waste>> getDateToWasteMapping() {
        List<Waste> wasteList = wasteRepository.findByCompanyProfile(companyProfileService.getCompanyProfile());

        wasteList.sort(Comparator.comparing(Waste::getCreatedAt));
        Waste wasteInitialRecord = wasteList.get(0);

        LocalDate endDate = LocalDate.now(); // or any end date you want
        LocalDate startDate = LocalDate.from(wasteInitialRecord.getCreatedAt());

        Map<YearMonth, List<Waste>> monthlyIds = new HashMap<>();
        LocalDate currentMonthStart = startDate.withDayOfMonth(1);

        while (currentMonthStart.isBefore(endDate)) {
            YearMonth currentYearMonth = YearMonth.from(currentMonthStart);

            LocalDate monthEnd = currentMonthStart.with(TemporalAdjusters.lastDayOfMonth());

            List<Waste> wasteList1 = wasteRepository.findByDateRangeAndCompanyId(currentMonthStart, monthEnd, companyProfileService.getCompanyProfile().getId());
            monthlyIds.put(currentYearMonth, wasteList1);
            currentMonthStart = currentMonthStart.plusMonths(1);
        }

        return monthlyIds;
    }
}

