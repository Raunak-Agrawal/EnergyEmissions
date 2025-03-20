package com.effix.api.controller.v1;

import com.effix.api.model.entity.*;
import com.effix.api.repository.*;
import com.effix.api.service.ElectricityService;
import com.effix.api.service.NaturalGasService;
import com.effix.api.service.RefrigerationService;
import com.effix.api.service.WaterSupplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
public class SchedulerController {

    @Autowired
    private ElectricityMeterRepository electricityMeterRepository;

    @Autowired
    private ElectricityEmissionRepository electricityEmissionRepository;

    @Autowired
    private ElectricityService electricityService;

    @Autowired
    private NaturalGasMeterRepository naturalGasMeterRepository;

    @Autowired
    private WaterSupplyMeterRepository waterSupplyMeterRepository;

    @Autowired
    private RefrigerantRepository refrigerantRepository;

    @Autowired
    private NaturalGasEmissionRepository naturalGasEmissionRepository;

    @Autowired
    private WaterSupplyEmissionRepository waterSupplyEmissionRepository;

    @Autowired
    private NaturalGasService naturalGasService;

    @Autowired
    private WaterSupplyService waterSupplyService;

    @Autowired
    private RefrigerationService refrigerationService;

    @Scheduled(cron = "0 0 0/3 * * *") //  Cron expression for running every 3 hours
    @Transactional
    public void feedElectricityReadings() {
        List<ElectricityMeter> allMeters = electricityMeterRepository.findAll();
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("Schedule executed for electricity reading at time {}", currentTime);
        for (ElectricityMeter meter : allMeters) {
            List<ElectricityEmission> emissions = meter.getElectricityEmissions();

            try {
                if (emissions == null || emissions.isEmpty()) {
                    float initialFloatValue = (int) (Math.random() * (1000 - 500 + 1)) + 500; // generate initial value between 500 to 1000
                    electricityService.addMeterUsage(meter.getId(), currentTime, initialFloatValue);
                } else {
                    emissions.sort(Comparator.comparing(ElectricityEmission::getCreatedAt).reversed());
                    ElectricityEmission latestMeterReading = emissions.get(0);
                    Duration dateDifference = Duration.between(latestMeterReading.getCreatedAt(), currentTime);
                    if (Math.abs(dateDifference.toHours()) >= 4) {
                        Float valueOfLatestEntry = latestMeterReading.getElectricityReading();
                        LocalDateTime updatedTime = latestMeterReading.getCreatedAt().plusHours(4);
                        float newFloatValue = (int) (Math.random() * 501) + valueOfLatestEntry; // generate new value between valueOfLatestEntry and valueOfLatestEntry + 500
                        electricityService.addMeterUsage(meter.getId(), updatedTime, newFloatValue);
                    }
                }
            } catch (Exception ex) {
                log.error("Exception occurred while adding meter reading for electricity with message {}", ex.getMessage(), ex);
            }
        }
    }

    @Scheduled(cron = "0 0 0/3 * * *") //  Cron expression for running every 3 hours
    @Transactional
    public void feedNaturalGasReadings() {
        List<NaturalGasMeter> allMeters = naturalGasMeterRepository.findAll();
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("Schedule executed for natural gas reading at time {}", currentTime);
        for (NaturalGasMeter meter : allMeters) {
            List<NaturalGasEmission> emissions = meter.getNaturalGasEmissions();
            try {
                if (emissions == null || emissions.isEmpty()) {
                    float initialFloatValue = (int) (Math.random() * (10 - 1 + 1)) + 1; // generate initial value between 1 to 10
                    naturalGasService.addMeterUsage(meter.getId(), currentTime, initialFloatValue);
                } else {
                    emissions.sort(Comparator.comparing(NaturalGasEmission::getCreatedAt).reversed());
                    NaturalGasEmission latestMeterReading = emissions.get(0);
                    Duration dateDifference = Duration.between(latestMeterReading.getCreatedAt(), currentTime);
                    if(Math.abs(dateDifference.toHours()) >= 4){
                        Float valueOfLatestEntry = latestMeterReading.getNaturalGasReading();
                        LocalDateTime updatedTime = latestMeterReading.getCreatedAt().plusHours(4);
                        log.info("natural gas updated time {}", updatedTime);
                        float newFloatValue = (int) (Math.random() * 11) + valueOfLatestEntry; // generate new value between valueOfLatestEntry and valueOfLatestEntry + 10
                        naturalGasService.addMeterUsage(meter.getId(), updatedTime, newFloatValue);
                    }
                }
            } catch (Exception ex) {
                log.error("Exception occurred while adding meter reading for naturalgas with message {}", ex.getMessage(), ex);
            }
        }
    }

    @Scheduled(cron = "0 0 0/3 * * *") //  Cron expression for running every 3 hours
    @Transactional
    public void feedWaterSupplyReadings() {
        List<WaterSupplyMeter> allMeters = waterSupplyMeterRepository.findAll();
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("Schedule executed for water supply reading at time {}", currentTime);
        for (WaterSupplyMeter meter : allMeters) {
            List<WaterSupplyEmission> emissions = meter.getWaterSupplyEmissions();
            try {
                if (emissions == null || emissions.isEmpty()) {
                    float initialFloatValue = (int) (Math.random() * (10 - 1 + 1)) + 1; // generate initial value between 1 to 10
                    waterSupplyService.addMeterUsage(meter.getId(), currentTime, initialFloatValue);
                } else {
                    emissions.sort(Comparator.comparing(WaterSupplyEmission::getCreatedAt).reversed());
                    WaterSupplyEmission latestMeterReading = emissions.get(0);
                    Duration dateDifference = Duration.between(latestMeterReading.getCreatedAt(), currentTime);
                    if(Math.abs(dateDifference.toHours()) >= 4){
                        Float valueOfLatestEntry = latestMeterReading.getWaterSupplyReading();
                        LocalDateTime updatedTime = latestMeterReading.getCreatedAt().plusHours(4);
                        log.info("water supply updated time {}", updatedTime);
                        float newFloatValue = (int) (Math.random() * 11) + valueOfLatestEntry; // generate new value between valueOfLatestEntry and valueOfLatestEntry + 10
                        waterSupplyService.addMeterUsage(meter.getId(), updatedTime, newFloatValue);
                    }
                }
            } catch (Exception ex) {
                log.error("Exception occurred while adding meter reading for water supply with message {}", ex.getMessage(), ex);
            }
        }
    }

    @Scheduled(cron = "0 0 0/10 * * *") //  Cron expression for running every 10 hours
    @Transactional
    public void feedRefrigerantReadings() {
        List<Refrigerant> allRefrigerants = refrigerantRepository.findAll();
        LocalDateTime currentTime = LocalDateTime.now();
        for (Refrigerant refrigerant : allRefrigerants) {
            List<RefrigerantEmission> emissions = refrigerant.getRefrigerantEmissions();
            try {
                if (emissions != null && !emissions.isEmpty()) {
                    emissions.sort(Comparator.comparing(RefrigerantEmission::getCreatedAt).reversed());
                    RefrigerantEmission latestEntry = emissions.get(0);
                    Duration dateDifference = Duration.between(latestEntry.getCreatedAt(), currentTime);

                    if(Math.abs(dateDifference.toDays()) >= 30) {
                        Double valueOfLatestEntry = latestEntry.getRefrigerantYearsOfUsage();
                        LocalDateTime updatedTime = latestEntry.getCreatedAt().plusDays(30);
                        double newValue = valueOfLatestEntry + ((double) 1 / 12); // generate new value between valueOfLatestEntry and valueOfLatestEntry + 10
                        refrigerationService.addRefrigerantUsage(refrigerant.getId(), updatedTime, newValue);
                    }
                }
            } catch (Exception ex) {
                log.error("Exception occurred while adding refrigerant reading with message {}", ex.getMessage(), ex);
            }
        }
    }
}
