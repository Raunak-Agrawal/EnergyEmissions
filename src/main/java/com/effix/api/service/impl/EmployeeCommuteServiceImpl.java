package com.effix.api.service.impl;

import com.effix.api.dto.request.employeecommute.CreateEmployeeCommuteRequestDTO;
import com.effix.api.dto.request.employeecommute.EmployeeCommuteTransportModeDTO;
import com.effix.api.dto.response.employeecommute.EmployeeCommuteEmissionDetailResponseDTO;
import com.effix.api.dto.response.employeecommute.EmployeeCommuteEmissionResponseDTO;
import com.effix.api.enums.EmissionFactorType;
import com.effix.api.enums.LookupType;
import com.effix.api.enums.VehicleType;
import com.effix.api.model.entity.*;
import com.effix.api.repository.*;
import com.effix.api.service.CompanyProfileService;
import com.effix.api.service.EmployeeCommuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class EmployeeCommuteServiceImpl implements EmployeeCommuteService {
    @Autowired
    private CompanyProfileService companyProfileService;

    @Autowired
    private EmployeeCommuteRepository employeeCommuteRepository;

    @Autowired
    private EmployeeCommuteEmissionRepository employeeCommuteEmissionRepository;

    @Autowired
    private EmissionRepository emissionRepository;

    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private EmissionFactorRepository emissionFactorRepository;

    @Override
    public EmployeeCommute createEmployeeCommute(CreateEmployeeCommuteRequestDTO employeeCommuteRequestDTO) {
        EmployeeCommute employeeCommute = EmployeeCommute.builder()
                .employeeName(employeeCommuteRequestDTO.getEmployeeName())
                .workingDays(employeeCommuteRequestDTO.getNoOfWorkingDays())
                .companyProfile(companyProfileService.getCompanyProfile())
                .build();

        EmployeeCommute employeeCommute1 = employeeCommuteRepository.save(employeeCommute);

        for (EmployeeCommuteTransportModeDTO mode : employeeCommuteRequestDTO.getCommuteTransportDetails()) {
            float kmCovered = employeeCommuteRequestDTO.getDailyDistanceCovered() * mode.getNoOfOneWayJourney();

            Emissions employeeCommuteEmissions = calculateEmployeeCommuteEmissions(mode.getModeOfTransport(), mode.getType(), mode.getSubType(), kmCovered);
            Emissions emissionsSaved = null;
            if (employeeCommuteEmissions != null) emissionsSaved = emissionRepository.save(employeeCommuteEmissions);

            EmployeeCommuteEmission employeeCommuteEmission = EmployeeCommuteEmission.builder()
                    .commute(employeeCommute1)
                    .modeOfTransport(mode.getModeOfTransport())
                    .emissions(emissionsSaved)
                    .kmCovered(kmCovered)
                    .build();
            employeeCommuteEmissionRepository.save(employeeCommuteEmission);
        }

        return employeeCommute1;
    }

    @Override
    public List<EmployeeCommuteEmissionResponseDTO> getAllEmployeeCommuteEmissions() {
        List<EmployeeCommuteEmissionResponseDTO> result = new ArrayList<>();

        Map<LocalDate, List<EmployeeCommute>> dateToEmployeeCommuteMapping = getDateToEmployeeCommuteMapping();

        for (Map.Entry<LocalDate, List<EmployeeCommute>> entry : dateToEmployeeCommuteMapping.entrySet()) {
            EmployeeCommuteEmissionResponseDTO emissionResponseDTO = EmployeeCommuteEmissionResponseDTO.builder().build();

            LocalDate date = entry.getKey();
            List<EmployeeCommute> employeeCommutes = entry.getValue();

            emissionResponseDTO.setDate(date);

            double distanceCoveredCar = 0.0;
            double carCH4Emissions = 0.0;
            double carC02Emissions = 0.0;
            double carN20Emissions = 0.0;

            double distanceCoveredBike = 0.0;
            double bikeCH4Emissions = 0.0;
            double bikeC02Emissions = 0.0;
            double bikeN20Emissions = 0.0;

            double distanceCoveredRail = 0.0;
            double railCH4Emissions = 0.0;
            double railC02Emissions = 0.0;
            double railN20Emissions = 0.0;

            double distanceCoveredTaxi = 0.0;
            double taxiCH4Emissions = 0.0;
            double taxiC02Emissions = 0.0;
            double taxiN20Emissions = 0.0;

            double distanceCoveredBus = 0.0;
            double busCH4Emissions = 0.0;
            double busC02Emissions = 0.0;
            double busN20Emissions = 0.0;

            for (EmployeeCommute employeeCommute : employeeCommutes) {
                for (EmployeeCommuteEmission employeeCommuteEmission : employeeCommute.getEmployeeCommuteEmissions()) {
                    VehicleType modeOfTransport = VehicleType.valueOf(employeeCommuteEmission.getModeOfTransport().toUpperCase());
                    Emissions emissions = employeeCommuteEmission.getEmissions();
                    switch (modeOfTransport) {
                        case CAR:
                            distanceCoveredCar += employeeCommuteEmission.getKmCovered();
                            carCH4Emissions += emissions.getCh4Emissions();
                            carC02Emissions += emissions.getCo2Emissions();
                            carN20Emissions += emissions.getN2oEmissions();
                            break;
                        case MOTORBIKE:
                            distanceCoveredBike += employeeCommuteEmission.getKmCovered();
                            bikeCH4Emissions += emissions.getCh4Emissions();
                            bikeC02Emissions += emissions.getCo2Emissions();
                            bikeN20Emissions += emissions.getN2oEmissions();
                            break;
                        case RAIL:
                            distanceCoveredRail += employeeCommuteEmission.getKmCovered();
                            railCH4Emissions += emissions.getCh4Emissions();
                            railC02Emissions += emissions.getCo2Emissions();
                            railN20Emissions += emissions.getN2oEmissions();
                            break;
                        case BUS:
                            distanceCoveredBus += employeeCommuteEmission.getKmCovered();
                            busCH4Emissions += emissions.getCh4Emissions();
                            busC02Emissions += emissions.getCo2Emissions();
                            busN20Emissions += emissions.getN2oEmissions();
                            break;
                        case TAXI:
                            distanceCoveredTaxi += employeeCommuteEmission.getKmCovered();
                            taxiCH4Emissions += emissions.getCh4Emissions();
                            taxiC02Emissions += emissions.getCo2Emissions();
                            taxiN20Emissions += emissions.getN2oEmissions();
                            break;
                    }
                }
            }

            EmployeeCommuteEmissionDetailResponseDTO employeeCommuteEmissionCar = EmployeeCommuteEmissionDetailResponseDTO.builder()
                    .modeOfTransport(VehicleType.CAR.getName())
                    .distanceCovered(distanceCoveredCar)
                    .ch4Emissions(carCH4Emissions)
                    .n2oEmissions(carN20Emissions)
                    .co2Emissions(carC02Emissions)
                    .totalEmissions(carCH4Emissions + carC02Emissions + carN20Emissions)
                    .build();

            EmployeeCommuteEmissionDetailResponseDTO employeeCommuteEmissionBike = EmployeeCommuteEmissionDetailResponseDTO.builder()
                    .modeOfTransport(VehicleType.MOTORBIKE.getName())
                    .distanceCovered(distanceCoveredBike)
                    .ch4Emissions(bikeCH4Emissions)
                    .n2oEmissions(bikeN20Emissions)
                    .co2Emissions(bikeC02Emissions)
                    .totalEmissions(bikeCH4Emissions + bikeC02Emissions + bikeN20Emissions)
                    .build();

            EmployeeCommuteEmissionDetailResponseDTO employeeCommuteEmissionTaxi = EmployeeCommuteEmissionDetailResponseDTO.builder()
                    .modeOfTransport(VehicleType.TAXI.getName())
                    .distanceCovered(distanceCoveredTaxi)
                    .ch4Emissions(taxiCH4Emissions)
                    .n2oEmissions(taxiN20Emissions)
                    .co2Emissions(taxiC02Emissions)
                    .totalEmissions(taxiCH4Emissions + taxiC02Emissions + taxiN20Emissions)
                    .build();

            EmployeeCommuteEmissionDetailResponseDTO employeeCommuteEmissionRail = EmployeeCommuteEmissionDetailResponseDTO.builder()
                    .modeOfTransport(VehicleType.RAIL.getName())
                    .distanceCovered(distanceCoveredRail)
                    .ch4Emissions(railCH4Emissions)
                    .n2oEmissions(railN20Emissions)
                    .co2Emissions(railC02Emissions)
                    .totalEmissions(railCH4Emissions + railC02Emissions + railN20Emissions)
                    .build();

            EmployeeCommuteEmissionDetailResponseDTO employeeCommuteEmissionBus = EmployeeCommuteEmissionDetailResponseDTO.builder()
                    .modeOfTransport(VehicleType.BUS.getName())
                    .distanceCovered(distanceCoveredBus)
                    .ch4Emissions(busCH4Emissions)
                    .n2oEmissions(busN20Emissions)
                    .co2Emissions(busC02Emissions)
                    .totalEmissions(busCH4Emissions + busC02Emissions + busN20Emissions)
                    .build();

            emissionResponseDTO.setEmissions(List.of(employeeCommuteEmissionCar, employeeCommuteEmissionBike, employeeCommuteEmissionTaxi, employeeCommuteEmissionRail, employeeCommuteEmissionBus));

            result.add(emissionResponseDTO);
        }

        result.sort(Comparator.comparing(EmployeeCommuteEmissionResponseDTO::getDate));
        return result;
    }

    private Map<LocalDate, List<EmployeeCommute>> getDateToEmployeeCommuteMapping() {
        List<EmployeeCommute> employeeCommutes = employeeCommuteRepository.findAll();

        employeeCommutes.sort(Comparator.comparing(EmployeeCommute::getCreatedAt));
        EmployeeCommute employeeCommuteInitialRecord = employeeCommutes.get(0);

        LocalDate endDate = LocalDate.now(); // or any end date you want
        LocalDate startDate = LocalDate.from(employeeCommuteInitialRecord.getCreatedAt()); // 1 week before endDate

        Map<LocalDate, List<EmployeeCommute>> weeklyIds = new HashMap<>();
        LocalDate currentWeekStart = startDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        while (currentWeekStart.isBefore(endDate)) {
            LocalDate weekEnd = currentWeekStart.plusDays(6); // assuming week ends on Sunday

            List<EmployeeCommute> commuteList = employeeCommuteRepository.findByDateRangeAndCompanyId(currentWeekStart, weekEnd, companyProfileService.getCompanyProfile().getId());
            weeklyIds.put(currentWeekStart, commuteList);
            currentWeekStart = currentWeekStart.plusWeeks(1); // move to next week
        }

        return weeklyIds;
    }

    private Emissions calculateEmployeeCommuteEmissions(String modeOfTransport, String type, String subType, float kmCovered) {
        EmissionFactor emissionFactor = null;

        if (VehicleType.CAR.getName().equalsIgnoreCase(modeOfTransport)) {
            Lookup lookup = lookupRepository.findByTypeAndNameAndVehicleType(LookupType.VEHICLE_SIZE, type, VehicleType.CAR);
            emissionFactor = emissionFactorRepository.findByCategoryAndSubCategoryAndUnit(EmissionFactorType.valueOf(subType), lookup, "km");
        } else if (VehicleType.MOTORBIKE.getName().equalsIgnoreCase(modeOfTransport)) {
            Lookup lookup = lookupRepository.findByTypeAndNameAndVehicleType(LookupType.VEHICLE_SIZE, type, VehicleType.MOTORBIKE);
            emissionFactor = emissionFactorRepository.findByCategoryAndSubCategoryAndUnit(EmissionFactorType.EMPLOYEE_COMMUTE_AND_BUSINESS_TRAVEL, lookup, "km");
        } else if (VehicleType.BUS.getName().equalsIgnoreCase(modeOfTransport)) {
            Lookup lookup = lookupRepository.findByTypeAndNameAndVehicleType(LookupType.VEHICLE_SIZE, type, VehicleType.BUS);
            emissionFactor = emissionFactorRepository.findByCategoryAndSubCategoryAndUnit(EmissionFactorType.EMPLOYEE_COMMUTE_AND_BUSINESS_TRAVEL, lookup, "km");
        } else if (VehicleType.RAIL.getName().equalsIgnoreCase(modeOfTransport)) {
            Lookup lookup = lookupRepository.findByTypeAndNameAndVehicleType(LookupType.VEHICLE_SIZE, type, VehicleType.RAIL);
            emissionFactor = emissionFactorRepository.findByCategoryAndSubCategoryAndUnit(EmissionFactorType.EMPLOYEE_COMMUTE_AND_BUSINESS_TRAVEL, lookup, "km");
        } else if (VehicleType.TAXI.getName().equalsIgnoreCase(modeOfTransport)) {
            Lookup lookup = lookupRepository.findByTypeAndNameAndVehicleType(LookupType.VEHICLE_SIZE, type, VehicleType.TAXI);
            emissionFactor = emissionFactorRepository.findByCategoryAndSubCategoryAndUnit(EmissionFactorType.EMPLOYEE_COMMUTE_AND_BUSINESS_TRAVEL, lookup, "km");
        } else return null;

        Double co2Emissions = emissionFactor.getCO2EmissionFactor() * kmCovered;
        Double cH4Emissions = emissionFactor.getCH4EmissionFactor() * kmCovered;
        Double n20Emissions = emissionFactor.getN2OEmissionFactor() * kmCovered;

        return Emissions.builder()
                .co2Emissions(co2Emissions)
                .ch4Emissions(cH4Emissions)
                .n2oEmissions(n20Emissions)
                .totalEmissions(co2Emissions + cH4Emissions + n20Emissions)
                .build();
    }
}
