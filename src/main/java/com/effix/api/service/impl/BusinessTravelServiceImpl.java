package com.effix.api.service.impl;

import com.effix.api.dto.request.employeecommute.BusinessTravelTransportModeDTO;
import com.effix.api.dto.request.employeecommute.CreateBusinessTravelRequestDTO;
import com.effix.api.dto.response.employeecommute.EmployeeCommuteEmissionDetailResponseDTO;
import com.effix.api.dto.response.employeecommute.EmployeeCommuteEmissionResponseDTO;
import com.effix.api.enums.EmissionFactorType;
import com.effix.api.enums.LookupType;
import com.effix.api.enums.VehicleType;
import com.effix.api.exception.ServerException;
import com.effix.api.model.entity.*;
import com.effix.api.repository.*;
import com.effix.api.service.BusinessTravelService;
import com.effix.api.service.CompanyProfileService;
import com.effix.api.service.externalAPIs.DistanceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class BusinessTravelServiceImpl implements BusinessTravelService {
    @Autowired
    private CompanyProfileService companyProfileService;

    @Autowired
    private BusinessTravelRepository businessTravelRepository;

    @Autowired
    private BusinessTravelEmissionRepository businessTravelEmissionRepository;

    @Autowired
    private EmissionRepository emissionRepository;

    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private EmissionFactorRepository emissionFactorRepository;

    @Autowired
    private DistanceAPI distanceAPI;


    @Override
    @Transactional
    public BusinessTravel createBusinessTravel(CreateBusinessTravelRequestDTO businessTravelRequestDTO) throws ServerException {
        BusinessTravel businessTravel = BusinessTravel.builder()
                .noOfEmployees(businessTravelRequestDTO.getNoOfEmployees())
                .groupName(businessTravelRequestDTO.getEmployeeGroupName())
                .companyProfile(companyProfileService.getCompanyProfile())
                .build();

        BusinessTravel businessTravel1 = businessTravelRepository.save(businessTravel);

        for (BusinessTravelTransportModeDTO mode : businessTravelRequestDTO.getCommuteTransportDetails()) {
            float kmCovered = calculateBusinessTravelDistance(mode.getModeOfTransport(), mode.getSourceAddress(), mode.getDestinationAddress());

            Emissions businessTravelEmissions = calculateBusinessTravelEmissions(mode.getModeOfTransport(), mode.getType(), mode.getSubType(), kmCovered);

            Emissions emissionsSaved = null;
            if (businessTravelEmissions != null) emissionsSaved = emissionRepository.save(businessTravelEmissions);

            BusinessTravelEmission businessTravelEmission = BusinessTravelEmission.builder()
                    .commute(businessTravel1)
                    .modeOfTransport(mode.getModeOfTransport())
                    .emissions(emissionsSaved)
                    .kmCovered(kmCovered)
                    .build();
            businessTravelEmissionRepository.save(businessTravelEmission);
        }

        return businessTravel1;
    }

    private float calculateBusinessTravelDistance(String modeOfTransport, String sourceAddress, String destinationAddress) throws ServerException {
        Float[] distances = distanceAPI.getLandAndAirDistance(sourceAddress, destinationAddress);
        float airDistance = distances[0];
        float landDistance = distances[1];

        if (VehicleType.FLIGHT.getName().equalsIgnoreCase(modeOfTransport)) {
            if (airDistance == 0.0) {
                throw new ServerException("Unable to fetch air distance");
            }
            return airDistance;
        } else {
            if (landDistance == 0.0) {
                throw new ServerException("Unable to fetch air distance");
            }
            return landDistance;
        }
    }

    @Override
    public List<EmployeeCommuteEmissionResponseDTO> getAllBusinessTravelEmissions() {
        List<EmployeeCommuteEmissionResponseDTO> result = new ArrayList<>();

        Map<LocalDate, List<BusinessTravel>> dateToBusinessTravelMapping = getDateToBusinessTravelMapping();

        for (Map.Entry<LocalDate, List<BusinessTravel>> entry : dateToBusinessTravelMapping.entrySet()) {
            EmployeeCommuteEmissionResponseDTO emissionResponseDTO = EmployeeCommuteEmissionResponseDTO.builder().build();

            LocalDate date = entry.getKey();
            List<BusinessTravel> businessTravels = entry.getValue();

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

            double distanceCoveredFlight = 0.0;
            double flightCH4Emissions = 0.0;
            double flightC02Emissions = 0.0;
            double flightN20Emissions = 0.0;

            for (BusinessTravel businessTravel : businessTravels) {
                for (BusinessTravelEmission businessTravelEmission : businessTravel.getBusinessTravelEmissions()) {
                    VehicleType modeOfTransport = VehicleType.valueOf(businessTravelEmission.getModeOfTransport().toUpperCase());
                    Emissions emissions = businessTravelEmission.getEmissions();
                    switch (modeOfTransport) {
                        case CAR:
                            distanceCoveredCar += businessTravelEmission.getKmCovered();
                            carCH4Emissions += emissions.getCh4Emissions();
                            carC02Emissions += emissions.getCo2Emissions();
                            carN20Emissions += emissions.getN2oEmissions();
                            break;
                        case MOTORBIKE:
                            distanceCoveredBike += businessTravelEmission.getKmCovered();
                            bikeCH4Emissions += emissions.getCh4Emissions();
                            bikeC02Emissions += emissions.getCo2Emissions();
                            bikeN20Emissions += emissions.getN2oEmissions();
                            break;
                        case RAIL:
                            distanceCoveredRail += businessTravelEmission.getKmCovered();
                            railCH4Emissions += emissions.getCh4Emissions();
                            railC02Emissions += emissions.getCo2Emissions();
                            railN20Emissions += emissions.getN2oEmissions();
                            break;
                        case BUS:
                            distanceCoveredBus += businessTravelEmission.getKmCovered();
                            busCH4Emissions += emissions.getCh4Emissions();
                            busC02Emissions += emissions.getCo2Emissions();
                            busN20Emissions += emissions.getN2oEmissions();
                            break;
                        case TAXI:
                            distanceCoveredTaxi += businessTravelEmission.getKmCovered();
                            taxiCH4Emissions += emissions.getCh4Emissions();
                            taxiC02Emissions += emissions.getCo2Emissions();
                            taxiN20Emissions += emissions.getN2oEmissions();
                            break;
                        case FLIGHT:
                            distanceCoveredFlight += businessTravelEmission.getKmCovered();
                            flightCH4Emissions += emissions.getCh4Emissions();
                            flightC02Emissions += emissions.getCo2Emissions();
                            flightN20Emissions += emissions.getN2oEmissions();
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

            EmployeeCommuteEmissionDetailResponseDTO employeeCommuteEmissionFlight = EmployeeCommuteEmissionDetailResponseDTO.builder()
                    .modeOfTransport(VehicleType.FLIGHT.getName())
                    .distanceCovered(distanceCoveredFlight)
                    .ch4Emissions(flightCH4Emissions)
                    .n2oEmissions(flightN20Emissions)
                    .co2Emissions(flightC02Emissions)
                    .totalEmissions(flightCH4Emissions + flightC02Emissions + flightN20Emissions)
                    .build();

            emissionResponseDTO.setEmissions(List.of(employeeCommuteEmissionCar, employeeCommuteEmissionBike, employeeCommuteEmissionTaxi, employeeCommuteEmissionRail, employeeCommuteEmissionBus, employeeCommuteEmissionFlight));

            result.add(emissionResponseDTO);
        }

        result.sort(Comparator.comparing(EmployeeCommuteEmissionResponseDTO::getDate));
        return result;
    }

    private Map<LocalDate, List<BusinessTravel>> getDateToBusinessTravelMapping() {
        List<BusinessTravel> businessTravels = businessTravelRepository.findAll();

        businessTravels.sort(Comparator.comparing(BusinessTravel::getCreatedAt));
        BusinessTravel businessTravelInitialRecord = businessTravels.get(0);

        LocalDate endDate = LocalDate.now(); // or any end date you want
        LocalDate startDate = LocalDate.from(businessTravelInitialRecord.getCreatedAt()); // 1 week before endDate

        Map<LocalDate, List<BusinessTravel>> weeklyIds = new HashMap<>();
        LocalDate currentWeekStart = startDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        while (currentWeekStart.isBefore(endDate)) {
            LocalDate weekEnd = currentWeekStart.plusDays(6); // assuming week ends on Sunday

            List<BusinessTravel> commuteList = businessTravelRepository.findByDateRangeAndCompanyId(currentWeekStart, weekEnd, companyProfileService.getCompanyProfile().getId());
            weeklyIds.put(currentWeekStart, commuteList);
            currentWeekStart = currentWeekStart.plusWeeks(1); // move to next week
        }

        return weeklyIds;
    }

    private Emissions calculateBusinessTravelEmissions(String modeOfTransport, String type, String subType, float kmCovered) {
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
        } else if (VehicleType.FLIGHT.getName().equalsIgnoreCase(modeOfTransport)) {
            Lookup lookup = lookupRepository.findByTypeAndNameAndDescriptionAndVehicleType(LookupType.VEHICLE_SIZE, subType, type, VehicleType.FLIGHT);
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
